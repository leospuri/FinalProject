package in.voiceme.app.voiceme.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.otto.Subscribe;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.NotificationsPage.NotificationsActivity;
import in.voiceme.app.voiceme.ProfilePage.ProfileActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.WasLoggedInInterface;
import in.voiceme.app.voiceme.chat.DialogDetailsActivity;
import in.voiceme.app.voiceme.contactPage.ContactListActivity;
import in.voiceme.app.voiceme.l;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;


public class MainNavDrawer extends NavDrawer implements WasLoggedInInterface {
    private TextView displayNameText;
    private SimpleDraweeView avatarImage;
    private SharedPreferences prefs;
    private SharedPreferences recyclerviewpreferences;

    public MainNavDrawer(final BaseActivity activity) {
        super(activity);

        addItem(new ActivityNavDrawerItem(ProfileActivity.class, "Profile", null, R.mipmap.ic_action_unread, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(NotificationsActivity.class, "Notification", null, R.mipmap.ic_action_send_now, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(MainActivity.class, "Activity", null, R.drawable.activity_icon, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(DiscoverActivity.class, "Discover", null, R.drawable.discover, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(ContactListActivity.class, "Contacts", null, R.drawable.contacts, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(DialogDetailsActivity.class, "Chat Messages", null, R.drawable.chat_icon, R.id.include_main_nav_drawer_topItems));
     //    addItem(new ActivityNavDrawerItem(LicenseActivity.class, "license", null, R.mipmap.ic_action_person, R.id.include_main_nav_drawer_topItems));
        prefs = activity.getSharedPreferences("Logged in or not", Context.MODE_PRIVATE);
        String sLcl = "Logout";
        if (prefs.getBoolean("is this demo mode", false)) sLcl = "Quit";
        addItem(new BasicNavDrawerItem(sLcl, null, R.mipmap.ic_action_backspace, R.id.include_main_nav_drawer_bottomItems) {
            @Override
            public void onClick(View view) {
                if (processLoggedState(view))
                    prefs.edit().putBoolean("is this demo mode", false).apply();
                ;
                /*Toast.makeText(activity, "You have logged out!", Toast.LENGTH_SHORT).show();*/
                activity.getVoicemeApplication().getAuth().logout();
            }
        });

        recyclerviewpreferences = activity.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
        displayNameText = (TextView) navDrawerView.findViewById(R.id.include_main_nav_drawer_displayName);
        avatarImage = (SimpleDraweeView) navDrawerView.findViewById(R.id.include_main_nav_drawer_avatar);
        if (MySharedPreferences.getImageUrl(recyclerviewpreferences) == null){
            return;
        } else {
            avatarImage.setImageURI(MySharedPreferences.getImageUrl(recyclerviewpreferences));
        }

        if (MySharedPreferences.getUsername(recyclerviewpreferences) == null){
            displayNameText.setText("");
        } else {
            displayNameText.setText(MySharedPreferences.getUsername(recyclerviewpreferences));
        }

  //      User loggedInUser = VoicemeApplication.getAuth().getUser();
     //   displayNameText.setText(loggedInUser.getUserNickName());

        // Todo Nav Drawer Avatar
     //   Picasso.with(activity).load(loggedInUser.getAvatarPics()).into(avatarImage);
    }

    @Subscribe
    public void onUserDetailsUpdated(Account.UserDetailsUpdatedEvent event) {
   //     Picasso.with(activity).load(event.User.getAvatarPics()).into(avatarImage);
  //      displayNameText.setText(event.User.getUserNickName());
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }
}
