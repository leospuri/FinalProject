package in.voiceme.app.voiceme.DiscoverPage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by Loyalty Android on 31-07-2017.
 */

public class FakeHome {

    public static void makePrefered(Context c) {
        PackageManager p = c.getPackageManager();
        ComponentName cN = new ComponentName(c, FakeHome.class);
        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        c.startActivity(selector);

        p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
