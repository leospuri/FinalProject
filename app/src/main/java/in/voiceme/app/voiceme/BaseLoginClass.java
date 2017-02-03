package in.voiceme.app.voiceme;

import android.content.Context;
import android.view.View;

/**
 * Created by Nayomnik on 1/3/2017.
 */

/**
 * This class has alone method to define
 * is the user  in demo mode or not
 *@author Iskandar Huseynov (Nayomnik)
 * @version 1.0
 */
public class BaseLoginClass {

    /**
     *  Returns false  if  the user is logged in.
     *  Otherwise returns true and he is in demo mode.
     * @param  viewPrm  the View object who has been clicked
     * @see WasLoggedInInterface WasLoggedInInterface interface
     */

    public boolean isDemoMode(View viewPrm) {
        Context contextLcl = viewPrm.getContext();
        return contextLcl.getSharedPreferences("Logged in or not", Context.MODE_WORLD_WRITEABLE).getBoolean("is this demo mode", false);
    }
}
