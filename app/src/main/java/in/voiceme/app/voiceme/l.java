package in.voiceme.app.voiceme;

import android.util.Log;

public class l {
    static public void a(Object o) {
        Log.v("iiiiiiiiiiiiii", o.toString());
    }

    static public void a(int o) {
        Log.v("iiiiiiiiiiiiiii", "" + o);
    }

    static public void pause(long i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
            try {
                Thread.sleep(i);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
                try {
                    Thread.sleep(i);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}
