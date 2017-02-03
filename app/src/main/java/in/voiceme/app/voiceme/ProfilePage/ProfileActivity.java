package in.voiceme.app.voiceme.ProfilePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    private View avatarProgressFrame;

    private TextView username;
    private TextView about;
    private TextView total_posts;
    private TextView total_posts_counter;
    private TextView followers;
    private TextView followers_counter;
    private TextView following_counter;
    private TextView following;
    private CircleImageView image;
    private LinearLayout followerLayout;
    private LinearLayout followingLayout;
    private LinearLayout totalPostLayout;

    private TextView age;
    private TextView gender;
    private TextView location;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile Page");
        setNavDrawer(new MainNavDrawer(this));

        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        avatarProgressFrame = findViewById(R.id.activity_profile_avatarProgressFrame);

        image = (CircleImageView) findViewById(R.id.profile_image);
        username = (TextView) findViewById(R.id.name);
        followers = (TextView) findViewById(R.id.profile_followers);
        followers_counter = (TextView) findViewById(R.id.action_followers);
        following_counter = (TextView) findViewById(R.id.action_following);
        following = (TextView) findViewById(R.id.profile_following);
        about = (TextView) findViewById(R.id.about);
        total_posts = (TextView) findViewById(R.id.user_profile_textview);
        total_posts_counter = (TextView) findViewById(R.id.total_posts_counter);
        followerLayout = (LinearLayout) findViewById(R.id.follower_column);
        followingLayout = (LinearLayout) findViewById(R.id.following_layout);
        totalPostLayout = (LinearLayout) findViewById(R.id.total_post_layout);

        age = (TextView) findViewById(R.id.age_value);
        gender = (TextView) findViewById(R.id.gender_value);
        location = (TextView) findViewById(R.id.location_value);

        followers.setOnClickListener(this);
        following.setOnClickListener(this);
        age.setOnClickListener(this);
        gender.setOnClickListener(this);
        location.setOnClickListener(this);
        total_posts.setOnClickListener(this);
        followers_counter.setOnClickListener(this);
        following_counter.setOnClickListener(this);
        total_posts_counter.setOnClickListener(this);
        followerLayout.setOnClickListener(this);
        totalPostLayout.setOnClickListener(this);

        avatarProgressFrame.setVisibility(View.GONE);


        //   if (isProgressBarVisible)
        //     setProgressBarVisible(true);

    }

    @Override
    public void onClick(View view) {
        if (processLoggedState(view))
            return;
        int viewId = view.getId();

        if (viewId == R.id.total_post_layout || viewId == R.id.user_profile_textview || viewId == R.id.total_posts_counter) {
            Intent intent = new Intent(this, TotalPostsActivity.class);
            intent.putExtra(Constants.TOTAL_POST, MySharedPreferences.getUserId(preferences));
            startActivity(intent);
        } else if (viewId == R.id.follower_column || viewId == R.id.profile_followers || viewId == R.id.action_followers) {
            Intent intent = new Intent(this, FollowersActivity.class);
            intent.putExtra(Constants.USER_FOLLOW, MySharedPreferences.getUserId(preferences));
            startActivity(intent);
        } else if (viewId == R.id.following_layout || viewId == R.id.profile_following || viewId == R.id.action_following) {
            Intent intent = new Intent(this, FollowingActivity.class);
            intent.putExtra(Constants.USER_FOLLOWING, MySharedPreferences.getUserId(preferences));
            startActivity(intent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (processLoggedState(username))
            return false;
        l.a(111111);
        int itemId = item.getItemId();

        if (itemId == R.id.activity_profile_menuEdit) {
            startActivity(new Intent(this, ChangeProfileActivity.class));
            return true;
        }

        return false;
    }

    private void getData() throws Exception {
        application.getWebService()
                .getUserProfile(MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ProfileUserList>() {
                    @Override
                    public void onNext(ProfileUserList response) {
                        Timber.e("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        profileData(response);
                    }
                });
    }

    private void profileData(ProfileUserList response) {
        username.setText(response.getData().getUserNickName());
        about.setText(response.getData().getAboutMe());
        total_posts_counter.setText(response.getData().getPosts());
        followers_counter.setText(response.getData().getFollowers());
        following_counter.setText(response.getData().getFollowing());
        age.setText(response.getData().getUserDateOfBirth());
        gender.setText(response.getData().getGender());
        location.setText(response.getData().getLocation());

        if (!response.getData().getAvatarPics().equals("")) {
            Picasso.with(this)
                    .load(response.getData().getAvatarPics())
                    .resize(75, 75)
                    .centerInside()
                    .into(image);
        }
    }


    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Reminder");
            alertDialog.setMessage("You cannot interact\nunless you logged in");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }
}