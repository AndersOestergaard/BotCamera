package com.anders.botcamera;

/**
 * Created by ander on 20-05-2017.
 */

import java.io.IOException;

import com.anders.botcamera.RotationControl.RotationListener;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.remote.ev3.RemoteRequestPilot;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class BotControl extends Activity implements OnTouchListener,
        RotationListener {
    private static final String HOST = "10.0.1.1";
    private RemoteRequestEV3 ev3;
    private RemoteRequestPilot pilot;
    private int angle;
    private ImageButton forward, backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bot_control);

        forward = (ImageButton) findViewById(R.id.forward);
        forward.setOnTouchListener(this);

        backward = (ImageButton) findViewById(R.id.backward);
        backward.setOnTouchListener(this);

        RotationControl rotateView = (RotationControl) findViewById(R.id.rotate);
        rotateView.setRotationListener(this);

        // Connect to the EV3
        new Control().execute("connect", HOST);
    }

    @Override
    public void onAngleChanged(int angle) {
        new Control().execute("rotate", "" + angle);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP)
            new Control().execute("stop");
        else if (action == MotionEvent.ACTION_DOWN) {
            if (v == forward)
                new Control().execute("forward");
            else if (v == backward)
                new Control().execute("backward");
        }
        return false;
    }

    private class Control extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... cmd) {
            if (cmd[0].equals("connect")) {
                try {
                    ev3 = new RemoteRequestEV3(cmd[1]);
                    pilot = (RemoteRequestPilot) ev3.createPilot(3.5f, 20f, "A", "B");
                } catch (IOException e) {
                    return 1l;
                }
            } else if (cmd[0].equals("rotate")) {
                if (ev3 == null)
                    return 2l;
                int newAngle = Integer.parseInt(cmd[1]);
                pilot.rotate(angle - newAngle);
                angle = newAngle;
            } else if (cmd[0].equals("forward")) {
                if (ev3 == null)
                    return 2l;
                pilot.forward();
            } else if (cmd[0].equals("backward")) {
                if (ev3 == null)
                    return 2l;
                pilot.backward();
            } else if (cmd[0].equals("stop")) {
                if (ev3 == null)
                    return 2l;
                pilot.stop();
            } else if (cmd[0].equals("close")) {
                if (ev3 == null)
                    return 2l;
                pilot.close();
                ev3.disConnect();
            }
            return 0l;
        }

        protected void onPostExecute(Long result) {
            if (result == 1l)
                Toast.makeText(BotControl.this, "Could not connect to EV3",
                        Toast.LENGTH_LONG).show();
            else if (result == 2l)
                Toast.makeText(BotControl.this, "Not connected",
                        Toast.LENGTH_LONG).show();
        }
    }
}
