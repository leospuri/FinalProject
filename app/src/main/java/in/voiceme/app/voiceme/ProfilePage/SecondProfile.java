package in.voiceme.app.voiceme.ProfilePage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import in.voiceme.app.voiceme.DTO.ProfileUserList;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.MessageActivity;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SecondProfile extends BaseActivity implements View.OnClickListener {

    private TextView username;
    private TextView about;
    private TextView total_posts;
    private TextView total_posts_counter;
    private TextView followers;
    private TextView following;
    private Button followMe;
    private LinearLayout followerPost;
    private LinearLayout followingPost;
    private int followCounter;
    private int followingCounter;

    private SimpleDraweeView image;

    private TextView followersCount;
    private TextView followingCount;
    private ProfileUserList response;

    private TextView age;
    private TextView gender;
    private String profileUserId;
    protected Boolean currentFollowing;
    private ProgressBar progressBar;
    private View progressFrame;
    private ImageView send_private_message;
    private LinearLayout second_new_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second_profile);
        progressFrame = findViewById(R.id.activity_second_profile_progress);
        progressBar = (ProgressBar) findViewById(R.id.second_profile_progress);
        progressBar.setVisibility(View.VISIBLE);
        profileUserId = getIntent().getStringExtra(Constants.SECOND_PROFILE_ID);

        send_private_message = (ImageView) findViewById(R.id.send_private_message);
        second_new_username = (LinearLayout) findViewById(R.id.layout_second_profile_total);

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        username = (TextView) findViewById(R.id.second_name);
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
        second_new_username.setOnClickListener(this);

        age.setOnClickListener(this);
        followerPost.setOnClickListener(this);
        followingPost.setOnClickListener(this);
        total_posts_counter.setOnClickListener(this);
        gender.setOnClickListener(this);
        total_posts.setOnClickListener(this);
        total_posts_counter.setOnClickListener(this);
        send_private_message.setOnClickListener(this);

        followMe.setOnClickListener(this);

        //   if (isProgressBarVisible)
        //     setProgressBarVisible(true);

        try {
            getData(profileUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (processLoggedState(view))
            return;
        int viewId = view.getId();
        switch (view.getId()){
            case R.id.second_user_profile_textview:
                Intent intent = new Intent(this, TotalPostsActivity.class);
                intent.putExtra(Constants.TOTAL_POST, profileUserId);
                startActivity(intent);
                break;
            case R.id.second_total_posts_counter:
                Intent intent02 = new Intent(this, TotalPostsActivity.class);
                intent02.putExtra(Constants.TOTAL_POST, profileUserId);
                startActivity(intent02);
                break;
            case R.id.layout_second_profile_total:
                Intent intent03 = new Intent(this, TotalPostsActivity.class);
                intent03.putExtra(Constants.TOTAL_POST, profileUserId);
                startActivity(intent03);
                break;
            case R.id.second_profile_followers:
                Intent intent04 = new Intent(this, FollowersActivity.class);
                intent04.putExtra(Constants.USER_FOLLOW, profileUserId);
                startActivity(intent04);
                break;
            case R.id.second_action_followers:
                Intent intent05 = new Intent(this, FollowersActivity.class);
                intent05.putExtra(Constants.USER_FOLLOW, profileUserId);
                startActivity(intent05);
                break;
            case R.id.layout_second_profile_followers:
                Intent intent06 = new Intent(this, FollowersActivity.class);
                intent06.putExtra(Constants.USER_FOLLOW, profileUserId);
                startActivity(intent06);
                break;
            case R.id.second_profile_following:
                Intent intent07 = new Intent(this, FollowingActivity.class);
                intent07.putExtra(Constants.USER_FOLLOWING, profileUserId);
                startActivity(intent07);
                break;
            case R.id.second_action_following:
                Intent intent08 = new Intent(this, FollowingActivity.class);
                intent08.putExtra(Constants.USER_FOLLOWING, profileUserId);
                startActivity(intent08);
                break;
            case R.id.layout_second_profile_following:
                Intent intent09 = new Intent(this, FollowingActivity.class);
                intent09.putExtra(Constants.USER_FOLLOWING, profileUserId);
                startActivity(intent09);
                break;
            case R.id.second_profile_follow_me:

                if (currentFollowing){
                    try {
                        followCounter--;
                        followersCount.setText(String.valueOf(followCounter));
                        removeFollower(profileUserId, Constants.REMOVE);
                   //     sendUnFollowNotification();
                        followMe.setText("Follow");
                        currentFollowing = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else{
                    try {
                        addFollower(profileUserId, Constants.ADD);
                        followCounter++;
                        followersCount.setText(String.valueOf(followCounter));

                      //  sendFollowNotification();
                        followMe.setText("Following");
                        currentFollowing = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.send_private_message:
                if (processLoggedState(view))
                    return;

                if (isNetworkConnected()){
                    Intent intent10 = new Intent(this, MessageActivity.class);
                    intent10.putExtra(Constants.YES, profileUserId);
                    if (!response.getData().getUserNickName().trim().isEmpty() && response.getData().getUserNickName().trim() != null){

                        intent10.putExtra(Constants.USERNAME, response.getData().getUserNickName());
                    } else {
                        intent10.putExtra(Constants.USERNAME, "");
                    }

                    startActivity(intent10);
                } else {
                    Toast.makeText(this, "Not connected to internet", Toast.LENGTH_SHORT).show();
                }

        }


    }

    private void sendFollowNotification() {
        application.getWebService()
                .sendFollowNotification("senderid@1_contactId@21_follow@1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        //  Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        // Timber.d("Message from server" + response);
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Timber.e(e.getMessage());
                       //     Toast.makeText(SecondProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void sendUnFollowNotification() {
        application.getWebService()
                .sendFollowNotification("senderid@1_contactId@21_follow@1")
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        //  Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        // Timber.d("Message from server" + response);
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Timber.e(e.getMessage());
                          //  Toast.makeText(SecondProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void addFollower(String secondUserId, String addOrRemove) throws Exception {
        application.getWebService()
                .addFollower(secondUserId, MySharedPreferences.getUserId(preferences), addOrRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Timber.e(e.getMessage());
                        //    Toast.makeText(SecondProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });

    }

    private void removeFollower(String secondUserId, String addOrRemove) throws Exception {
        application.getWebService()
                .addFollower(secondUserId, MySharedPreferences.getUserId(preferences), addOrRemove)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Timber.e(e.getMessage());
                        //    Toast.makeText(SecondProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void getData(String secondUserId) throws Exception {
        application.getWebService()
                .getOtherUserProfile(secondUserId, MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<ProfileUserList>() {
                    @Override
                    public void onNext(ProfileUserList response) {
                        currentFollowing = response.getFollower();
                        secondProfileData(response);
                        progressFrame.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressFrame.setVisibility(View.GONE);
                        try {
                            Timber.e(e.getMessage());
                         //   Toast.makeText(SecondProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void secondProfileData(ProfileUserList response) {
        this.response = response;
        username.setText(response.getData().getUserNickName());
        about.setText(response.getData().getAboutMe());
        total_posts_counter.setText(response.getData().getPosts());
        followCounter = response.getData().getFollowers();
        followingCounter = response.getData().getFollowing();
        followersCount.setText(String.valueOf(response.getData().getFollowers()));
        followingCount.setText(String.valueOf(response.getData().getFollowing()));
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
