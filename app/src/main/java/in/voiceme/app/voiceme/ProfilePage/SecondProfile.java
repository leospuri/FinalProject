package in.voiceme.app.voiceme.ProfilePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.analytics.HitBuilders;

import in.voiceme.app.voiceme.DTO.ProfileUserList;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class SecondProfile extends BaseActivity implements View.OnClickListener {

    private TextView username;
    private TextView about;
    private TextView total_posts;
    private TextView total_posts_counter;
    private TextView followers;
    private TextView following;
    private Button followMe;
    private LinearLayout totalPost;
    private LinearLayout followerPost;
    private LinearLayout followingPost;

    private SimpleDraweeView image;

    private TextView followersCount;
    private TextView followingCount;

    private TextView age;
    private TextView gender;
    private String profileUserId;
    protected Boolean currentFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileUserId = getIntent().getStringExtra(Constants.SECOND_PROFILE_ID);

        try {
            getData(profileUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_second_profile);
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toast.makeText(this, "user ID: " + profileUserId, Toast.LENGTH_SHORT).show();

        username = (TextView) findViewById(R.id.second_name);
        totalPost = (LinearLayout) findViewById(R.id.layout_second_user_profile_textview);
        followerPost = (LinearLayout) findViewById(R.id.layout_second_profile_followers);
        followingPost = (LinearLayout) findViewById(R.id.layout_second_profile_following);
        image = (SimpleDraweeView) findViewById(R.id.second_image);
        followers = (TextView) findViewById(R.id.second_profile_followers);
        following = (TextView) findViewById(R.id.second_profile_following);
        about = (TextView) findViewById(R.id.second_about);
        total_posts = (TextView) findViewById(R.id.second_user_profile_textview);
        total_posts_counter = (TextView) findViewById(R.id.second_total_posts_counter);
        followMe = (Button) findViewById(R.id.second_profile_follow_me);

        followersCount = (TextView) findViewById(R.id.second_action_followers);
        followingCount = (TextView) findViewById(R.id.second_action_following);

        age = (TextView) findViewById(R.id.second_age_value);
        gender = (TextView) findViewById(R.id.second_gender_value);

        followers.setOnClickListener(this);
        following.setOnClickListener(this);
        followersCount.setOnClickListener(this);
        followingCount.setOnClickListener(this);

        age.setOnClickListener(this);
        followerPost.setOnClickListener(this);
        followingPost.setOnClickListener(this);
        total_posts_counter.setOnClickListener(this);
        totalPost.setOnClickListener(this);
        gender.setOnClickListener(this);
        total_posts.setOnClickListener(this);
        total_posts_counter.setOnClickListener(this);

        followMe.setOnClickListener(this);


        //   if (isProgressBarVisible)
        //     setProgressBarVisible(true);

    }

    @Override
    public void onClick(View view) {
        if (processLoggedState(view))
            return;
        int viewId = view.getId();

        if (viewId == R.id.second_user_profile_textview || viewId == R.id.layout_second_user_profile_textview || viewId == R.id.second_total_posts_counter) {
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("SecondProfile")
                    .setAction("Second Profile Posts Clicked")
                    .build());
            // [END custom_event]

            Intent intent = new Intent(this, TotalPostsActivity.class);
            intent.putExtra(Constants.TOTAL_POST, profileUserId);
            startActivity(intent);
        } else if (viewId == R.id.second_profile_followers || viewId == R.id.second_action_followers || viewId == R.id.layout_second_profile_followers) {
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("SecondProfile")
                    .setAction("Second followers Clicked")
                    .build());
            // [END custom_event]

            Intent intent = new Intent(this, FollowersActivity.class);
            intent.putExtra(Constants.USER_FOLLOW, profileUserId);
            startActivity(intent);
        } else if (viewId == R.id.second_profile_following || viewId == R.id.second_action_following || viewId == R.id.layout_second_profile_following) {

            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("SecondProfile")
                    .setAction("Second following Clicked")
                    .build());
            // [END custom_event]

            Intent intent = new Intent(this, FollowingActivity.class);
            intent.putExtra(Constants.USER_FOLLOWING, profileUserId);
            startActivity(intent);

        } else if (viewId == R.id.second_profile_follow_me){
            if (currentFollowing){
                try {

                    // [START custom_event]
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("SecondProfile")
                            .setAction("Remove following Clicked")
                            .build());
                    // [END custom_event]


                    removeFollower(profileUserId, Constants.REMOVE);
                    sendUnFollowNotification();
                    followMe.setText("Follow");
                    currentFollowing = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else{
                try {
                    addFollower(profileUserId, Constants.ADD);
                    // [START custom_event]
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("SecondProfile")
                            .setAction("Add following Clicked")
                            .build());
                    // [END custom_event]
                    sendFollowNotification();
                    // follow
                    followMe.setText("Following");
                    currentFollowing = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void sendFollowNotification() {
        application.getWebService()
                .sendFollowNotification("senderid@1_contactId@21_follow@1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        //  Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        // Timber.d("Message from server" + response);
                    }
                });
    }

    private void sendUnFollowNotification() {
        application.getWebService()
                .sendFollowNotification("senderid@1_contactId@21_follow@1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        //  Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        // Timber.d("Message from server" + response);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        l.a(2222);
        if (itemId == R.id.activity_profile_menuEdit) {
            return true;
        }

        return false;
    }

    private void addFollower(String secondUserId, String addOrRemove) throws Exception {
        application.getWebService()
                .addFollower(secondUserId, MySharedPreferences.getUserId(preferences), addOrRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                        Toast.makeText(SecondProfile.this, "UserResponse from server: " + userResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeFollower(String secondUserId, String addOrRemove) throws Exception {
        application.getWebService()
                .addFollower(secondUserId, MySharedPreferences.getUserId(preferences), addOrRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                        Toast.makeText(SecondProfile.this, "UserResponse from server: " + userResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getData(String secondUserId) throws Exception {
        application.getWebService()
                .getOtherUserProfile(secondUserId, MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ProfileUserList>() {
                    @Override
                    public void onNext(ProfileUserList response) {
                        currentFollowing = response.getFollower();
                        secondProfileData(response);
                        Toast.makeText(SecondProfile.this, "Value of follow " + response.getFollower().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void secondProfileData(ProfileUserList response) {
        username.setText(response.getData().getUserNickName());
        about.setText(response.getData().getAboutMe());
        total_posts_counter.setText(response.getData().getPosts());
        followersCount.setText(response.getData().getFollowers());
        followingCount.setText(response.getData().getFollowing());
        age.setText(response.getData().getUserDateOfBirth());
        gender.setText(response.getData().getGender());
        if (response.getFollower()){
            followMe.setText("Following");
        } else {
            followMe.setText("Follow");
        }
        image.setImageURI(response.getData().getAvatarPics());

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
