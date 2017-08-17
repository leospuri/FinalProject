package in.voiceme.app.voiceme.PostsDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import in.voiceme.app.voiceme.DTO.PostLikesResponse;
import in.voiceme.app.voiceme.DTO.PostUserCommentModel;
import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.DTO.SuccessResponse;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.NotificationsPage.SimpleDividerItemDecoration;
import in.voiceme.app.voiceme.ProfilePage.ProfileActivity;
import in.voiceme.app.voiceme.ProfilePage.SecondProfile;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.Account;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.RegisterActivity;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.userpost.EditPost;
import in.voiceme.app.voiceme.userpost.ReportAbuseActivity;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import in.voiceme.app.voiceme.utils.CurrentTime;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.R.id.detail_list_item_posts_avatar;
import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public class PostsDetailsActivity extends BaseActivity implements View.OnClickListener {

    EditText mMessageEditText;
    ImageButton mSendMessageImageButton;
    RecyclerView mMessageRecyclerView;
    private MessageAdapter mMessageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String postId;
    private PostsModel myList = null;
    List<PostUserCommentModel> myCommentList = null;
    private boolean doDislike;
    private int commentCount;
    private Bitmap bmp;

    private SimpleDraweeView user_avatar = null;
    private ImageView play_button = null;

    private TextView user_name;
    private TextView isPost;
    private TextView feeling;
    private TextView category;

    //post data
    private TextView timeStamp;
    private TextView postMessage;
  //  private TextView postReadMore;
    private TextView post_audio_duration;

    //counter numbers
    private TextView like_counter;
    private TextView hug_counter;
    private TextView same_counter;
    private TextView post_comments;
//    private TextView post_listen;

    //emoji for like, hug and same above
    private ImageView commentCounterImage;
 //   private ImageView listenCounterImage;
    private ImageView moreButton;

    private int likeCounter;
    private int hugCounter;
    private int sameCounter;
    private PopupMenu popupMenu;
    private View progressFrame;
    protected MediaPlayer mediaPlayer = new MediaPlayer();
    private String idusername;


    //animated buttons
    private ImageView likeButtonMain, HugButtonMain, SameButtonMain;

    private boolean like_button_true;
    private boolean hug_button_true;
    private boolean sad_button_true;

    protected TextView new_counter_like_number;
    protected TextView new_counter_hug_number;
    protected TextView new_counter_same_number;
    protected TextView new_counter_cmt_number;

    private MessageAdapter.InsertMessageListener mInsertMessageListener = new MessageAdapter.InsertMessageListener() {

        @Override
        public void onMessageInserted(int position) {
            mLinearLayoutManager.scrollToPosition(position);
        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_details);
        getSupportActionBar().setTitle("Post Details");

       progressFrame = findViewById(R.id.post_details_progressBar);

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        Intent intent = getIntent();
        postId = intent.getStringExtra(Constants.POST_BACKGROUND);
        idusername = intent.getStringExtra(Constants.IDUSERNAME);

        //Imageview for avatar and play pause button
        user_avatar = (SimpleDraweeView) findViewById(detail_list_item_posts_avatar);
        play_button = (ImageView) findViewById(R.id.detail_list_item_posts_play_button);
        moreButton = (ImageView) findViewById(R.id.detail_status_more);

        //username, feeling and category
        user_name = (TextView) findViewById(R.id.detail_list_item_post_userNickName);
        isPost = (TextView) findViewById(R.id.detail_list_item_post_is);
        feeling = (TextView) findViewById(R.id.detail_list_item_posts_feeling);
        category = (TextView) findViewById(R.id.detail_list_item_posts_category);

        //post data
        post_audio_duration = (TextView) findViewById(R.id.detail_list_item_posts_duration_count);
        timeStamp = (TextView) findViewById(R.id.detail_list_item_posts_timeStamp);
        postMessage = (TextView) findViewById(R.id.detail_list_item_posts_message);
      //  postReadMore = (TextView) findViewById(R.id.detail_list_item_posts_read_more);

        new_counter_like_number = (TextView) findViewById(R.id.new_counter_like_number_detail);
        new_counter_hug_number = (TextView) findViewById(R.id.new_counter_hug_number_detail);
        new_counter_same_number = (TextView) findViewById(R.id.new_counter_same_number_detail);
        new_counter_cmt_number = (TextView) findViewById(R.id.new_counter_cmt_number_detail);
      //  postReadMore = (TextView) findViewById(R.id.detail_list_item_posts_read_more);

        //counter numbers
        like_counter = (TextView) findViewById(R.id.detail_post_likes_counter);
        hug_counter = (TextView) findViewById(R.id.detail_post_hugs_counter);
        same_counter = (TextView) findViewById(R.id.detail_post_same_counter);
        post_comments = (TextView) findViewById(R.id.post_comment_counter);
     //   post_listen = (TextView) findViewById(R.id.detail_post_listen_counter);

        //emoji for like, hug and same above
        commentCounterImage = (ImageView) findViewById(R.id.detail_post_comment_counter);
      //  listenCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_listen);

        //animated buttons
        likeButtonMain = (ImageView) findViewById(R.id.detail_list_item_like_button);
        HugButtonMain = (ImageView) findViewById(R.id.detail_list_item_hug_button);
        SameButtonMain = (ImageView) findViewById(R.id.detail_list_item_same_button);

        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mSendMessageImageButton = (ImageButton) findViewById(R.id.detail_btn_send_message);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.detail_rv_messages);

        mSendMessageImageButton.setOnClickListener(this);
        //OnClickListeners
        likeButtonMain.setOnClickListener(this);
        HugButtonMain.setOnClickListener(this);
        SameButtonMain.setOnClickListener(this);

        like_counter.setOnClickListener(this);
        hug_counter.setOnClickListener(this);
        same_counter.setOnClickListener(this);
        post_comments.setOnClickListener(this);
        new_counter_like_number.setOnClickListener(this);
        new_counter_hug_number.setOnClickListener(this);
        new_counter_same_number.setOnClickListener(this);
        new_counter_cmt_number.setOnClickListener(this);
     //   post_listen.setOnClickListener(this);
        category.setOnClickListener(this);
        feeling.setOnClickListener(this);

        commentCounterImage.setOnClickListener(this);
     //   listenCounterImage.setOnClickListener(this);
        user_name.setOnClickListener(this);
        user_avatar.setOnClickListener(this);
        play_button.setOnClickListener(this);
        moreButton.setOnClickListener(this);


        initRecyclerView();
        try {
            getData(postId);
         //   getComments(postId);
            getComments(postId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initRecyclerView() {
        /**code edited by nirmal
         * Replacing the linearlayout manager
         */


    //    mLinearLayoutManager = new LinearLayoutManager(PostsDetailsActivity.this,LinearLayoutManager.VERTICAL,true);

        mMessageAdapter = new MessageAdapter(PostsDetailsActivity.this, myCommentList, mInsertMessageListener);

       mLinearLayoutManager = new LinearLayoutManager(PostsDetailsActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
           }
       };
        mLinearLayoutManager.setStackFromEnd(true);
//        mLinearLayoutManager.setReverseLayout(true);

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mMessageRecyclerView.setAdapter(mMessageAdapter);

    }


    @Override
    public void onClick(View view) {
   //     processLoggedState(view);

        switch (view.getId()) {
            case R.id.detail_btn_send_message:
          //      if (processLoggedState(view)) {
         //           return;
         //       } else {
                checkUserBlockText();
         //       }
                break;
            case R.id.detail_list_item_like_button:
                if (processLoggedState(view)) {
                    return;
                } else {

                    if (like_button_true) {
                        like_button_true = false;
                        // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                        unLikeMethod();
                    } else {
                        like_button_true = true;
                        if (hug_button_true) {
                            hug_button_true = false;
                            unHugMethod();
                        }
                        if (sad_button_true) {
                            sad_button_true = false;
                            unSameMethod();
                        }
                        likeCounter++;

                        String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                                myList.getIdUserName() + "_postId@" + myList.getIdPosts() + "_click@" + "1";

                        if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())) {
                            Timber.e("Same user");
                        } else {
                            sendLikeNotification(application, sendLike);
                        }
                        like_counter.setBackgroundColor(getResources().getColor(R.color.md_blue_300));
                        like_counter.setTextColor(getResources().getColor(R.color.white));
                        like_counter.setText("LIKE");
                        sendLikeToServer(application, 1, 0, 0, 0, "clicked like button");
                        new_counter_like_number.setText(NumberFormat.getIntegerInstance().format(likeCounter));
                    }
                }
                break;
            case R.id.detail_list_item_hug_button:
                if (processLoggedState(view)) {
                    return;
                } else {
                    if (hug_button_true) {
                        hug_button_true = false;
                        // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                        unHugMethod();
                    } else {
                        hug_button_true = true;
                        if (like_button_true) {
                            like_button_true = false;
                            unLikeMethod();
                        }
                        if (sad_button_true) {
                            sad_button_true = false;
                            unSameMethod();
                        }
                        hugCounter++;
                        String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                                myList.getIdUserName() + "_postId@" + myList.getIdPosts() + "_click@" + "2";

                        if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())) {

                        } else {
                            sendLikeNotification(application, sendLike);
                        }
                        hug_counter.setBackgroundColor(getResources().getColor(R.color.md_blue_300));
                        hug_counter.setTextColor(getResources().getColor(R.color.white));
                        hug_counter.setText("HUG");
                        sendLikeToServer(application, 0, 1, 0, 0, "clicked hug button");
                        new_counter_hug_number.setText(NumberFormat.getIntegerInstance().format(hugCounter));
                    }
                }
                break;
            case R.id.detail_list_item_same_button:
                if (processLoggedState(view)) {
                    return;
                } else {

                    if (sad_button_true) {
                        sad_button_true = false;
                        // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                        unSameMethod();
                    } else {
                        sad_button_true = true;
                        if (like_button_true) {
                            like_button_true = false;
                            unLikeMethod();
                        }
                        if (hug_button_true) {
                            hug_button_true = false;
                            unHugMethod();
                        }

                        sameCounter++;
                        String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                                myList.getIdUserName() + "_postId@" + myList.getIdPosts() + "_click@" + "3";

                        if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())) {
                        } else {
                            sendLikeNotification(application, sendLike);
                        }
                        same_counter.setBackgroundColor(getResources().getColor(R.color.md_blue_300));
                        same_counter.setTextColor(getResources().getColor(R.color.white));
                        same_counter.setText("SAD");
                        sendLikeToServer(application, 0, 0, 1, 0, "clicked sad button");
                        new_counter_same_number.setText(NumberFormat.getIntegerInstance().format(sameCounter));
                    }

                }
                break;
            case R.id.detail_status_more:
                if (processLoggedState(view)) {
                    return;
                } else {
                    popupMenu = new PopupMenu(view.getContext(), view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.pop_menu, popupMenu.getMenu());
                    //    this.menu = popupMenu.getMenu();

                    SharedPreferences preferences;
                    preferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);

                    if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())) {
                        if (popupMenu.getMenu() == null)
                            return;
                        popupMenu.getMenu().setGroupVisible(R.id.main_menu_group, true);
                    } else {
                        popupMenu.getMenu().setGroupVisible(R.id.main_menu_group, false);
                    }

                    if (MySharedPreferences.getUserId(preferences).equals("2")){
                        popupMenu.getMenu().setGroupVisible(R.id.main_menu_popular_group, true);
                        popupMenu.getMenu().setGroupVisible(R.id.main_menu_remove_popular_group, true);
                    } else {
                        popupMenu.getMenu().setGroupVisible(R.id.main_menu_popular_group, false);
                        popupMenu.getMenu().setGroupVisible(R.id.main_menu_remove_popular_group, false);
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit_post:

                                    Intent editIntent = new Intent(PostsDetailsActivity.this, EditPost.class);
                                    editIntent.putExtra(Constants.IDPOST, myList.getIdPosts());
                                    editIntent.putExtra(Constants.STATUS_POST, myList.getTextStatus());
                                    if (myList.getAudioFileLink().isEmpty() || myList.getAudioFileLink() == null) {
                                        Timber.e("No audio attached");
                                    } else {
                                        editIntent.putExtra(Constants.AUDIO, myList.getAudioFileLink());
                                    }
                                    startActivity(editIntent);
                                    return true;

                                case R.id.report_post:

                                    Intent reportIntent = new Intent(PostsDetailsActivity.this, ReportAbuseActivity.class);
                                    reportIntent.putExtra(Constants.IDPOST, postId);
                                    reportIntent.putExtra(Constants.IDUSERNAME, myList.getIdUserName());
                                    reportIntent.putExtra(Constants.STATUS_POST, myList.getTextStatus());
                                    startActivity(reportIntent);
                                    return true;

                                case R.id.make_popular:

                                    removePopularPost(myList.getIdPosts());

                                    return true;

                                case R.id.remove_make_popular:

                                    sendPopularPost(myList.getIdPosts(), myList.getTextStatus());

                                    return true;

                                case R.id.menu_item_share:

                                    if (ActivityUtils.deleteAudioFile(PostsDetailsActivity.this)){
                                        shareImage();
                                    }
                                    return true;

                                default:
                                    return false;

                            }
                        }
                    });
                    popupMenu.show();

                }
                break;
            case R.id.detail_list_item_post_userNickName:
                if (myList.getIdUserName() != null){
                    if (myList.getIdUserName().equals(MySharedPreferences.getUserId(preferences))) {
                        startActivity(new Intent(view.getContext(), ProfileActivity.class));
                    } else {
                        Intent intent = new Intent(this, SecondProfile.class);
                        intent.putExtra(Constants.SECOND_PROFILE_ID, myList.getIdUserName());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(PostsDetailsActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.detail_list_item_posts_avatar:
                if (myList.getIdUserName() != null){
                    if (myList.getIdUserName().equals(MySharedPreferences.getUserId(preferences))) {
                        startActivity(new Intent(view.getContext(), ProfileActivity.class));
                    } else {
                        Intent intent = new Intent(this, SecondProfile.class);
                        intent.putExtra(Constants.SECOND_PROFILE_ID, myList.getIdUserName());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(PostsDetailsActivity.this, "User doesn't exist", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.detail_list_item_posts_feeling:
                Intent intent = new Intent(this, UserFeelingActivity.class);
                intent.putExtra(Constants.EMOTION, myList.getIdFeeling());
                startActivity(intent);
                break;
            case R.id.detail_list_item_posts_category:
                Intent in = new Intent(this, UserCategoryActivity.class);
                in.putExtra(Constants.CATEGORY, myList.getIdCategory());
                startActivity(in);
                break;
            case R.id.detail_post_likes_counter:
                Intent intent02 = new Intent(this, UserLikeCounterActivity.class);
                intent02.putExtra(Constants.LIKE_FEELING, myList.getIdPosts());
                startActivity(intent02);
                break;
            case R.id.new_counter_like_number_detail:
                Intent intent03 = new Intent(this, UserLikeCounterActivity.class);
                intent03.putExtra(Constants.LIKE_FEELING, myList.getIdPosts());
                startActivity(intent03);
                break;
            case R.id.detail_post_hugs_counter:
                Intent intent04 = new Intent(this, UserHugCounterActivity.class);
                intent04.putExtra(Constants.HUG_FEELING, myList.getIdPosts());
                startActivity(intent04);
                break;
            case R.id.new_counter_hug_number_detail:
                Intent intent05 = new Intent(this, UserHugCounterActivity.class);
                intent05.putExtra(Constants.HUG_FEELING, myList.getIdPosts());
                startActivity(intent05);
                break;
            case R.id.detail_post_same_counter:
                Intent intent06 = new Intent(this, UserSameCounterActivity.class);
                intent06.putExtra(Constants.SAME_FEELING, myList.getIdPosts());
                startActivity(intent06);
                break;
            case R.id.new_counter_same_number_detail:
                Intent intent07 = new Intent(this, UserSameCounterActivity.class);
                intent07.putExtra(Constants.SAME_FEELING, myList.getIdPosts());
                startActivity(intent07);
                break;

            /*     else if(view.getId() == R.id.detail_post_listen_counter){
            Intent intent = new Intent(this, UserListenCounterActivity.class);
            intent.putExtra(Constants.LISTEN_FEELING, myList.getIdPosts());
            startActivity(intent); */
            case R.id.detail_list_item_posts_play_button:
                if (!mediaPlayer.isPlaying()){
                    if (mediaPlayer != null){
                        try {
                            mediaPlayer.stop();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        mediaPlayer = null;
                    }

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(myList.getAudioFileLink());
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                try {
                                    mediaPlayer.start();
                                    flipPlayPauseButton(true);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                flipPlayPauseButton(false);
                            }
                        });
                        mediaPlayer.prepareAsync();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } else {
                    try {
                        mediaPlayer.pause();
                        flipPlayPauseButton(false);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }

    }

    private void shareImage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_share_image, null);
        dialogBuilder.setView(dialogView);

        bmp = drawTextToBitmap(PostsDetailsActivity.this, myList.getTextStatus());
        AlertDialog b = dialogBuilder.create();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        b.getWindow().setLayout(width, 110);
        b.show();

        ImageView facebook = (ImageView) dialogView.findViewById(R.id.facebook);
        ImageView twitter = (ImageView) dialogView.findViewById(R.id.twitter);
        ImageView linkIn = (ImageView) dialogView.findViewById(R.id.linkIn);
        ImageView Instagram = (ImageView) dialogView.findViewById(R.id.instagram);
        ImageView pinist = (ImageView) dialogView.findViewById(R.id.pinest);
        ImageView tumber = (ImageView) dialogView.findViewById(R.id.tumber);
        ImageView more = (ImageView) dialogView.findViewById(R.id.more);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDirectoryAndSaveFile(bmp, "HelloAndroid", "com.facebook.katana");
                b.cancel();
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDirectoryAndSaveFile(bmp, "HelloAndroid", "com.twitter.android");
                b.cancel();
            }
        });

        linkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDirectoryAndSaveFile(bmp, "HelloAndroid", "com.linkedin.android");
                b.cancel();
            }
        });

        Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDirectoryAndSaveFile(bmp, "HelloAndroid", "com.instagram.android");
                b.cancel();
            }
        });

        pinist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDirectoryAndSaveFile(bmp, "HelloAndroid", "com.pinterest");
                b.cancel();
            }
        });
        tumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDirectoryAndSaveFile(bmp, "HelloAndroid", "com.tumblr");
                b.cancel();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser(sharedIntentMaker(), "Choose an app"));

                b.cancel();
            }
        });
    }



    private Intent sharedIntentMaker() {
        String sharebody = String.valueOf(myList.getUserNicName() + " " + "said:"
                + " " + myList.getTextStatus() + " " + "inside Voiceme Android App. " +
                "You can download from www.voiceme.in/voiceme?Post=" + postId + "");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sharebody);
        return shareIntent;
    }



    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void unSameMethod() {
        sendUnlikeToServer(application, 1, 1, 0, 1, "clicked unlike button");
        sameCounter--;
        same_counter.setBackgroundColor(getResources().getColor(R.color.white));
        same_counter.setTextColor(getResources().getColor(R.color.black));
        same_counter.setText("SAD");

        new_counter_same_number.setText(NumberFormat.getIntegerInstance().format(sameCounter));
    }

    private void unHugMethod() {
        sendUnlikeToServer(application, 1, 0, 1, 1, "clicked unlike button");
        hugCounter--;
        hug_counter.setBackgroundColor(getResources().getColor(R.color.white));
        hug_counter.setTextColor(getResources().getColor(R.color.black));
        hug_counter.setText("HUG");
        new_counter_hug_number.setText(NumberFormat.getIntegerInstance().format(hugCounter));
    }

    private void unLikeMethod() {
        sendUnlikeToServer(application, 0, 1, 1, 1, "clicked unlike button");
        likeCounter--;
        like_counter.setBackgroundColor(getResources().getColor(R.color.white));
        like_counter.setTextColor(getResources().getColor(R.color.black));
        like_counter.setText("LIKE");
        new_counter_like_number.setText(NumberFormat.getIntegerInstance().format(likeCounter));
    }

    public void flipPlayPauseButton(boolean isPlaying){
        if (isPlaying){
            play_button.setImageResource(R.drawable.stop_button);
        } else  {
            play_button.setImageResource(R.drawable.play_button);
        }
    }


    void sendMessage() {
        String message = mMessageEditText.getText().toString();
        String currentUser = MySharedPreferences.getUserId(preferences);

        if (!mMessageEditText.getText().toString().isEmpty()) {
            // Todo post comment on server

            commentCount = mMessageEditText.getText().toString().length();
            if (commentCount > 500){
                Toast.makeText(this, "Please enter short messages", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    postComment(message);
                    if (idusername.equals(currentUser)){
                        Timber.e("same user");
                    } else {
                        String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                                idusername + "_postId@" + postId  + "_click@" + "5";
                        sendLikeNotification(application, sendLike);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                mMessageAdapter.addMessage(new PostUserCommentModel(message,
                        MySharedPreferences.getImageUrl(preferences),
                        MySharedPreferences.getUsername(preferences), String.valueOf(System.currentTimeMillis()/1000), 0, "1"));
            }

            mMessageEditText.setText("");
        } else {
            Toast.makeText(this, "You have not entered anything", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (postId != null){
            try {
                getData(postId);
             //   getComments(postId);
                getComments(postId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            startActivity(new Intent(this, DiscoverActivity.class));
        }
    }

    private void getComments(String postId) throws Exception {
        application.getWebService()
                .getUserComments(postId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<List<PostUserCommentModel>>() {
                    @Override
                    public void onNext(List<PostUserCommentModel> response) {
                        Log.e("RESPONSE:::", "Size===" + response.size());
                        showComments(response);
                        progressFrame.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                        progressFrame.setVisibility(View.GONE);
                        try {
                       //     Toast.makeText(PostsDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Timber.e("message error " + e);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void showComments(final List<PostUserCommentModel> myList) {
        this.myCommentList = myList;
        mMessageAdapter = new MessageAdapter(this, myList, mInsertMessageListener);
        mMessageRecyclerView.setAdapter(mMessageAdapter);



    }

    @Subscribe
    public void postReplyComment(Account.sendCommentReply sendReply) throws Exception {

        checkUserBlockReply(sendReply);

    }

    private void postReplyCommentFinal(Account.sendCommentReply sendReply) throws Exception {

        application.getWebService()
                .sendCommentReply(sendReply.id_post_comments, MySharedPreferences.getUserId(preferences), sendReply.id_post_user_name, postId, sendReply.message)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                        Timber.e(userResponse.getMsg());

                        if (idusername.equals(MySharedPreferences.getUserId(preferences))){
                            Timber.e("Same User");
                        } else {
                            FirebaseMessaging.getInstance().subscribeToTopic("SUB_POST_" + postId);
                        }

                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);

                    }
                });
    }

    @Subscribe
    public void postLikeNotification(Account.sendLikeUserId sendReply) throws Exception {

        if (MySharedPreferences.getUserId(preferences).equals(sendReply.id_user)){
            Timber.e("same user who posted the post");
            String sendLike01 = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                    sendReply.post_id_user + "_postId@" + postId  + "_click@" + "6"; // 6 for like the comment. 7 for replied to comment
            postLikeNotification(sendLike01);
        } else if (MySharedPreferences.getUserId(preferences).equals(sendReply.post_id_user)){
            Timber.e("same user who commented the post");
            String sendLike02 = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                    sendReply.id_user + "_postId@" + postId  + "_click@" + "6"; // 6 for like the comment. 7 for replied to comment
            postLikeNotification(sendLike02);
        } else {
            Timber.e("send notification to user who posted");
            String sendLike03 = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                    sendReply.id_user + "_postId@" + postId  + "_click@" + "6"; // 6 for like the comment. 7 for replied to comment
            postLikeNotification(sendLike03);
        }


    }

    private void postLikeNotification(String url){

        application.getWebService()
                .sendLikeNotification(url)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String userResponse) {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);

                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1322) {
                shareImage();
            }
        }
    }


    @Subscribe
    public void postCommentLike(Account.sendCommentLike sendReply) throws Exception {

        application.getWebService()
                .sendCommentLike(sendReply.id_post_comment, MySharedPreferences.getUserId(preferences), "1", sendReply.id_post_comment)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse userResponse) {


                        // Send notification to user with id_user_name ID
                      //  Toast.makeText(PostsDetailsActivity.this, "success comment like", Toast.LENGTH_SHORT).show();

                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);

                    }
                });
    }

    @Subscribe
    public void postCommentReplyLike(Account.sendCommentLike sendReply) throws Exception {

        application.getWebService()
                .sendCommentReplyLike(sendReply.id_post_comment, MySharedPreferences.getUserId(preferences), sendReply.like, sendReply.id_post_comment)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse userResponse) {

                        if (sendReply.like != null){
                            Toast.makeText(PostsDetailsActivity.this, "notification subscribed", Toast.LENGTH_SHORT).show();
                            FirebaseMessaging.getInstance().subscribeToTopic("SUB_POST_" + postId);
                        } else {
                            // unsubscribe notification
                            Toast.makeText(PostsDetailsActivity.this, "notification unsubscribed", Toast.LENGTH_SHORT).show();

                            FirebaseMessaging.getInstance().unsubscribeFromTopic("SUB_POST_" + postId);

                        }

                        // Send notification to user with id_user_name ID


                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);

                    }
                });
    }

    private void postComment(String message) throws Exception {
        application.getWebService()
                .sendComment(MySharedPreferences.getUserId(preferences), idusername, postId, message)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {

                        if (idusername.equals(MySharedPreferences.getUserId(preferences))){
                            Timber.e("Same User");
                        } else {
                            FirebaseMessaging.getInstance().subscribeToTopic("SUB_POST_" + postId);
                        }

                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);

                    }
                });
    }








    private void getData(String postId) throws Exception {
        application.getWebService()
                .getSinglePost(postId, MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                 //       String name = response.get(0).getIdUserName();
                        Timber.e(String.valueOf(response.size()));

                        showRecycleWithDataFilled(response.get(0));
                    }
                    @Override
                    public void onError(Throwable e){
                        Crashlytics.logException(e);
                        Timber.e("error loading the contents" + e);
                        try {
                            Toast.makeText(PostsDetailsActivity.this,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT)
                                    .show();
                            Timber.e(String.valueOf("error message inside Post Details: " + e));
                        }catch (Exception ex){

                            if (ex instanceof IndexOutOfBoundsException) {
                                Toast.makeText(PostsDetailsActivity.this,
                                        e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                                ex.printStackTrace();
                                Timber.e(String.valueOf("IndexOutOfBoundException inside Post Details: " + e));


                            } else {
                                throw new RuntimeException(ex);
                            }

                        }
                    }
                });
    }


    private void showRecycleWithDataFilled(final PostsModel myList) {

        this.myList = myList;

        setLikeCounter(myList.getLikes());
        setHugCounter(myList.getHug());
        setSameCounter(myList.getSame());



        user_name.setText(myList.getUserNicName());

        like_counter.setText("LIKE");
        hug_counter.setText("HUG");
        same_counter.setText("SAD");
        post_comments.setText("REPLY");

        // Todo make JodaTIme

        if (myList.getPostTime() == null){
            return;
        } else {
            timeStamp.setText(CurrentTime.getCurrentTime(myList.getPostTime(), PostsDetailsActivity.this));
        }
        postMessage.setText(myList.getTextStatus());
        feeling.setText(myList.getEmotions());

        switch (myList.getIdFeeling()){
            case 1: feeling.setBackgroundColor(getResources().getColor(R.color.md_blue_200)); //happy
                break;
            case 2: feeling.setBackgroundColor(getResources().getColor(R.color.md_blue_200)); //relaxed
                break;
            case 3: feeling.setBackgroundColor(getResources().getColor(R.color.md_red_200)); // angry
                break;
            case 4: feeling.setBackgroundColor(getResources().getColor(R.color.md_red_200)); // sad
                break;
            case 5: feeling.setBackgroundColor(getResources().getColor(R.color.md_green_200)); // bored
                break;
            case 6: feeling.setBackgroundColor(getResources().getColor(R.color.md_blue_200)); // loved
                break;
            case 7: feeling.setBackgroundColor(getResources().getColor(R.color.md_blue_200)); // sleepy
                break;
            case 8: feeling.setBackgroundColor(getResources().getColor(R.color.md_green_200)); // flirty
                break;
            case 9: feeling.setBackgroundColor(getResources().getColor(R.color.md_red_200)); // sick
                break;
            case 10: feeling.setBackgroundColor(getResources().getColor(R.color.md_red_200)); // tired
                break;
            case 11: feeling.setBackgroundColor(getResources().getColor(R.color.md_green_200)); // sexy
                break;
            case 12: feeling.setBackgroundColor(getResources().getColor(R.color.md_brown_200)); // sexy
                break;
            case 13: feeling.setBackgroundColor(getResources().getColor(R.color.md_brown_200)); // sexy
                break;
            case 14: feeling.setBackgroundColor(getResources().getColor(R.color.md_green_200)); // sexy
                break;

        }

        category.setText(myList.getCategory());

        new_counter_like_number.setText(String.valueOf(myList.getLikes()));
        new_counter_hug_number.setText(String.valueOf(myList.getHug()));
        new_counter_same_number.setText(String.valueOf(myList.getSame()));
        new_counter_cmt_number.setText(String.valueOf(myList.getComments() + myList.getComments_reply()));

        if (myList.getAudioDuration() != null){
            post_audio_duration.setText(myList.getAudioDuration());
         //   post_listen.setText(String.valueOf(myList.getListen()));
        }

        user_avatar.setImageURI(myList.getAvatarPics());

        if (myList.getAudioFileLink() == null || myList.getAudioFileLink().isEmpty()){
            hideAudioButton(View.GONE);
        } else {
            hideAudioButton(View.VISIBLE);
        }

        if (myList.getUserLike() != null){
            if (myList.getUserLike()){
                like_counter.setBackgroundColor(getResources().getColor(R.color.md_blue_300));
                like_counter.setTextColor(getResources().getColor(R.color.white));
                like_button_true = true;
                like_counter.setText("LIKE");
                //   likeButtonMain.setFavoriteResource(like_after);
            } else {
                like_counter.setBackgroundColor(getResources().getColor(R.color.white));
                like_counter.setTextColor(getResources().getColor(R.color.black));
                like_counter.setText("LIKE");
                like_button_true = false;
                //   likeButtonMain.setFavoriteResource(like_before);
            }


            if (myList.getUserHuge()){
                //    HugButtonMain.setFavoriteResource(hug_after);
                hug_counter.setBackgroundColor(getResources().getColor(R.color.md_blue_300));
                hug_counter.setTextColor(getResources().getColor(R.color.white));
                hug_counter.setText("HUG");
                hug_button_true = true;
            } else {
                hug_counter.setBackgroundColor(getResources().getColor(R.color.white));
                hug_counter.setTextColor(getResources().getColor(R.color.black));
                hug_counter.setText("HUG");

                //     HugButtonMain.setFavoriteResource(status_before);
                hug_button_true = false;
                //     HugButtonMain.setFavoriteResource(status_before);
            }


            if (myList.getUserSame()){
                same_counter.setBackgroundColor(getResources().getColor(R.color.md_blue_300));
                same_counter.setTextColor(getResources().getColor(R.color.white));
                same_counter.setText("SAD");
                sad_button_true = true;
                //      SameButtonMain.setFavoriteResource(sad);
            } else {
                same_counter.setTextColor(getResources().getColor(R.color.black));
                same_counter.setBackgroundColor(getResources().getColor(R.color.white));
                same_counter.setText("SAD");
                sad_button_true = false;
                //  SameButtonMain.setFavoriteResource(status_before);
            }
        }

        if (myList.getReportAbuseCount() >= 2){
            postMessage.setText("******** This post is flagged as Abusive for General Public *********");
            hideAudioButton(View.GONE);

        }
    }

    private void checkUserBlockText(){

        application.getWebService()
                .block_user_check(idusername, MySharedPreferences.getUserId(preferences))
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse response) {

                        if (response.getSuccess()){
                            Toast.makeText(PostsDetailsActivity.this, "This user has blocked You", Toast.LENGTH_LONG).show();
                        } else {
                            sendMessage();
                        }
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                        progressFrame.setVisibility(View.GONE);
                    }

                });
    }

    private void checkUserBlockReply(Account.sendCommentReply sendReply){

        application.getWebService()
                .block_user_check(idusername, MySharedPreferences.getUserId(preferences))
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse response) {

                        if (response.getSuccess()){
                            Toast.makeText(PostsDetailsActivity.this, "This user has blocked You", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                postReplyCommentFinal(sendReply);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                        progressFrame.setVisibility(View.GONE);
                    }

                });
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }

    public void setHugCounter(int hugCounter) {
        this.hugCounter = hugCounter;
    }

    public void setSameCounter(int sameCounter) {
        this.sameCounter = sameCounter;
    }

    private void hideAudioButton(int gone) {
        play_button.setVisibility(gone);
        post_audio_duration.setVisibility(gone);
    //    post_listen.setVisibility(gone);
      //  listenCounterImage.setVisibility(gone);
    }

    protected void sendLikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        application.getWebService()
                .likes(MySharedPreferences.getUserId(preferences), postId, like, hug, same, listen)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<PostLikesResponse>() {
                    @Override
                    public void onNext(PostLikesResponse postLikesResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                        try {
                            Timber.e(e.getMessage());
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    protected void sendLikeNotification(final VoicemeApplication application, String likeUrl) {
        application.getWebService()
                .sendLikeNotification(likeUrl)
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        //  Timber.d("Message from server" + response);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                        try {
                            Toast.makeText(PostsDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    protected void sendUnlikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        application.getWebService().unlikes(MySharedPreferences.getUserId(preferences), postId, like, hug, same, listen)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<PostLikesResponse>() {
                    @Override
                    public void onNext(PostLikesResponse postLikesResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Crashlytics.logException(e);
                        try {
                            Toast.makeText(PostsDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
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
                    startActivity(new Intent(PostsDetailsActivity.this, RegisterActivity.class));
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }

    // Todo code to calculate text length - https://stackoverflow.com/questions/4794484/calculate-text-size-according-to-width-of-text-area
    private Bitmap drawTextToBitmap(Context mContext, String text) {
        try {
            /*String text_line1 = myList.getUserNicName() + " " + "said:";*/
            String text_line2 = text + " " + " ";
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.image);
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            if (text_line2.length() <= 30) {
                paint.setTextSize((int) (45 * scale));
                // Todo write way to prevent cutting of words

                String desiredString = text_line2.substring(0, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 50 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 60) {
                paint.setTextSize((int) (45 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 90) {
                paint.setTextSize((int) (45 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 20 * scale, y + 180 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 120) {
                paint.setTextSize((int) (45 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, 75);
                String desiredString3 = text_line2.substring(75, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 3;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 20 * scale, y + 180 * scale, paint);
                canvas.drawText(desiredString3, x + 20 * scale, y + 240 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 150) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, 75);
                String desiredString3 = text_line2.substring(75, 100);
                String desiredString4 = text_line2.substring(100, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 12;
                int y = (bitmap.getHeight() + bounds.height()) / 4;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 0 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 0 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 0 * scale, y + 180 * scale, paint);
                canvas.drawText(desiredString3, x + 0 * scale, y + 240 * scale, paint);
                canvas.drawText(desiredString4, x + 0 * scale, y + 300 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 180) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 25);
                String desiredString1 = text_line2.substring(25, 50);
                String desiredString2 = text_line2.substring(50, 75);
                String desiredString3 = text_line2.substring(75, 100);
                String desiredString4 = text_line2.substring(100, 125);
                String desiredString5 = text_line2.substring(125, 150);
                String desiredString6 = text_line2.substring(150, text_line2.length());
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 4;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 20 * scale, y + 60 * scale, paint);
                canvas.drawText(desiredString1, x + 20 * scale, y + 120 * scale, paint);
                canvas.drawText(desiredString2, x + 20 * scale, y + 180 * scale, paint);
                canvas.drawText(desiredString3, x + 20 * scale, y + 240 * scale, paint);
                canvas.drawText(desiredString4, x + 20 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString5, x + 20 * scale, y + 360 * scale, paint);
                canvas.drawText(desiredString6, x + 20 * scale, y + 420 * scale, paint);
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 210) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, text_line2.length());
                /*String desiredString7 = text_line2.substring(175, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                /*paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 5;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                /*canvas.drawText(desiredString7, x + 0 * scale, y + 480 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"210++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 240) {
                paint.setTextSize((int) (40 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, 210);
                String desiredString7 = text_line2.substring(210, text_line2.length());
                /*String desiredString8 = text_line2.substring(200, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                /*paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 5;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 0 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 0 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 0 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 0 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 0 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 0 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 0 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 0 * scale, y + 400 * scale, paint);
                /*canvas.drawText(desiredString8, x + 0 * scale, y + 540 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"240++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 270) {
                paint.setTextSize((int) (35 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, 210);
                String desiredString7 = text_line2.substring(210, 240);
                String desiredString8 = text_line2.substring(240, text_line2.length());
                /*String desiredString9 = text_line2.substring(225, 250);
                String desiredString10 = text_line2.substring(250, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);
               /* paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);
                paint.getTextBounds(desiredString10, 0, desiredString10.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 10;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 25 * scale, y + 400 * scale, paint);
                canvas.drawText(desiredString8, x + 25 * scale, y + 450 * scale, paint);
                /*canvas.drawText(desiredString9, x + 25 * scale, y + 600 * scale, paint);
                canvas.drawText(desiredString10, x + 25 * scale, y + 660 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"270++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else if (text_line2.length() <= 300) {
                paint.setTextSize((int) (35 * scale));
                String desiredString = text_line2.substring(0, 30);
                String desiredString1 = text_line2.substring(30, 60);
                String desiredString2 = text_line2.substring(60, 90);
                String desiredString3 = text_line2.substring(90, 120);
                String desiredString4 = text_line2.substring(120, 150);
                String desiredString5 = text_line2.substring(150, 180);
                String desiredString6 = text_line2.substring(180, 210);
                String desiredString7 = text_line2.substring(210, 240);
                String desiredString8 = text_line2.substring(240, 270);
                String desiredString9 = text_line2.substring(270, text_line2.length());
                /*String desiredString10 = text_line2.substring(250, 275);
                String desiredString11 = text_line2.substring(275, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);
                paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);
                /*paint.getTextBounds(desiredString10, 0, desiredString10.length(), bounds);
                paint.getTextBounds(desiredString11, 0, desiredString11.length(), bounds);*/
                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 10;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 25 * scale, y + 400 * scale, paint);
                canvas.drawText(desiredString8, x + 25 * scale, y + 450 * scale, paint);
                canvas.drawText(desiredString9, x + 25 * scale, y + 500 * scale, paint);
                /*canvas.drawText(desiredString10, x + 25 * scale, y + 660 * scale, paint);
                canvas.drawText(desiredString11, x + 25 * scale, y + 720 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,"300++"+text_line2.length(),Toast.LENGTH_SHORT).show();*/
            } else {
                paint.setTextSize((int) (35 * scale));
                String desiredString = text_line2.substring(0, 35);
                String desiredString1 = text_line2.substring(35, 70);
                String desiredString2 = text_line2.substring(70, 105);
                String desiredString3 = text_line2.substring(105, 140);
                String desiredString4 = text_line2.substring(140, 175);
                String desiredString5 = text_line2.substring(175, 210);
                String desiredString6 = text_line2.substring(210, 245);
                String desiredString7 = text_line2.substring(245, 280);
                String desiredString8 = text_line2.substring(280, 305);
                //   String desiredString9 = text_line2.substring(301, text_line2.length());
                /*String desiredString11 = text_line2.substring(301, text_line2.length());*/
                Rect bounds = new Rect();
                /*paint.getTextBounds(text_line1, 0, text_line1.length(), bounds);*/
                paint.getTextBounds(desiredString, 0, desiredString.length(), bounds);
                paint.getTextBounds(desiredString1, 0, desiredString1.length(), bounds);
                paint.getTextBounds(desiredString2, 0, desiredString2.length(), bounds);
                paint.getTextBounds(desiredString3, 0, desiredString3.length(), bounds);
                paint.getTextBounds(desiredString4, 0, desiredString4.length(), bounds);
                paint.getTextBounds(desiredString5, 0, desiredString5.length(), bounds);
                paint.getTextBounds(desiredString6, 0, desiredString6.length(), bounds);
                paint.getTextBounds(desiredString7, 0, desiredString7.length(), bounds);
                paint.getTextBounds(desiredString8, 0, desiredString8.length(), bounds);
                //    paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);
                /*paint.getTextBounds(desiredString9, 0, desiredString9.length(), bounds);*/
                /*paint.getTextBounds(desiredString10, 0, desiredString10.length(), bounds);*/
                /*paint.getTextBounds(desiredString11, 0, desiredString11.length(), bounds);*/

                int x = (bitmap.getWidth() - bounds.width()) / 10;
                int y = (bitmap.getHeight() + bounds.height()) / 10;
                /*canvas.drawText(text_line1, x + 40 * scale, y, paint);*/
                canvas.drawText(desiredString, x + 25 * scale, y + 50 * scale, paint);
                canvas.drawText(desiredString1, x + 25 * scale, y + 100 * scale, paint);
                canvas.drawText(desiredString2, x + 25 * scale, y + 150 * scale, paint);
                canvas.drawText(desiredString3, x + 25 * scale, y + 200 * scale, paint);
                canvas.drawText(desiredString4, x + 25 * scale, y + 250 * scale, paint);
                canvas.drawText(desiredString5, x + 25 * scale, y + 300 * scale, paint);
                canvas.drawText(desiredString6, x + 25 * scale, y + 350 * scale, paint);
                canvas.drawText(desiredString7, x + 25 * scale, y + 400 * scale, paint);
                canvas.drawText(desiredString8, x + 25 * scale, y + 450 * scale, paint);
                //  canvas.drawText(desiredString9 + "...", x + 25 * scale, y + 500 * scale, paint);
                /*canvas.drawText(desiredString9+"...", x + 25 * scale, y + 500 * scale, paint);*/
                /*Toast.makeText(PostsDetailsActivity.this,""+text_line2.length(),Toast.LENGTH_SHORT).show();*/
                /*canvas.drawText(desiredString10, x + 25 * scale, y + 660 * scale, paint);*/
                /*canvas.drawText(desiredString11+"...", x + 25 * scale, y + 720 * scale, paint);*/
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName, String PackageApp) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/DirName/"), fileName + ".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setPackage(PackageApp);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setType("image/png");
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(share, "Share image File"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removePopularPost(String post_id) {

        application.getWebService()
                .deletePopularPost(post_id)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse response) {
                        Toast.makeText(PostsDetailsActivity.this, "Successfully removed Post Popular", Toast.LENGTH_SHORT).show();
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Timber.d("Message from server" + response);
                    }
                });
    }

    private void sendPopularPost(String post_id, String post_text) {

        application.getWebService()
                .insertPopularPost(post_id, post_text)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse response) {
                        Toast.makeText(PostsDetailsActivity.this, "Successfully made Post Popular", Toast.LENGTH_SHORT).show();
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Timber.d("Message from server" + response);
                    }
                });
    }



}
