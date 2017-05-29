package com.anders.botcamera;

import android.os.AsyncTask;
import android.widget.Toast;

import org.opencv.core.Point;
import org.opencv.core.Rect;

import java.io.IOException;

import lejos.remote.ev3.RemoteRequestEV3;
import lejos.remote.ev3.RemoteRequestPilot;

import static android.R.attr.angle;
import static android.widget.Toast.LENGTH_LONG;

/**
 * Created by ander on 20-05-2017.
 */

public class AutoControl {

    private static final String HOST = "10.0.1.1";
    private static RemoteRequestEV3 ev3;
    private static RemoteRequestPilot pilot;
    private static int leftTurn = 10;
    private static int rightTurn = -10;
    public static Boolean connected = false;

    public AutoControl()
    {
        new Control().execute("connect", HOST);
    }

    public static void Drive(Point c, Rect rect)
    {
        new Control().execute("forward");
        while(rect.tl().x > 200 && rect.br().x < 650)
        {
            new Control().execute("forward");
            if(c.x < 200)
            {
                new Control().execute("stop");
                new Control().execute("rotate", "" + leftTurn);
                return;
            }
            if(c.x > 700)
            {
                new Control().execute("stop");
                new Control().execute("rotate", "" + rightTurn);
                return;
            }
        }
    }
    public static void Connect()
    {
        new Control().execute("connect", HOST);
    }

    public static void Shutdown()
    {
        new Control().execute("close");
    }

    private static class Control extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... cmd) {
            if (cmd[0].equals("connect")) {
                try {
                    ev3 = new RemoteRequestEV3(cmd[1]);
                    pilot = (RemoteRequestPilot) ev3.createPilot(3.5f, 20f, "A", "B");
                    connected = true;
                } catch (IOException e) {
                    connected = false;
                    return 1l;
                }
            } else if (cmd[0].equals("rotate")) {
                if (ev3 == null)
                    return 2l;
                pilot.rotate(leftTurn);
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

    }


}
