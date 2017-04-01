package in.voiceme.app.voiceme.PostsDetails;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import in.voiceme.app.voiceme.DTO.PostLikesResponse;
import in.voiceme.app.voiceme.DTO.PostUserCommentModel;
import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.ProfilePage.SecondProfile;
import in.voiceme.app.voiceme.R;
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
import in.voiceme.app.voiceme.utils.CurrentTime;
import rx.android.schedulers.AndroidSchedulers;
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
    private ImageView likeCounterImage;
    private ImageView hugCounterImage;
    private ImageView sameCounterImage;
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
    private MaterialFavoriteButton likeButtonMain, HugButtonMain, SameButtonMain;



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

        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
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

        //counter numbers
        like_counter = (TextView) findViewById(R.id.detail_post_likes_counter);
        hug_counter = (TextView) findViewById(R.id.detail_post_hugs_counter);
        same_counter = (TextView) findViewById(R.id.detail_post_same_counter);
        post_comments = (TextView) findViewById(R.id.detail_post_comment_counter);
     //   post_listen = (TextView) findViewById(R.id.detail_post_listen_counter);

        //emoji for like, hug and same above
        likeCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_like);
        hugCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_hug);
        sameCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_same);
        commentCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_comment);
      //  listenCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_listen);

        //animated buttons
        likeButtonMain = (MaterialFavoriteButton) findViewById(R.id.detail_list_item_like_button);
        HugButtonMain = (MaterialFavoriteButton) findViewById(R.id.detail_list_item_hug_button);
        SameButtonMain = (MaterialFavoriteButton) findViewById(R.id.detail_list_item_same_button);

        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
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
     //   post_listen.setOnClickListener(this);
        category.setOnClickListener(this);
        feeling.setOnClickListener(this);
        likeCounterImage.setOnClickListener(this);
        hugCounterImage.setOnClickListener(this);
        sameCounterImage.setOnClickListener(this);
        commentCounterImage.setOnClickListener(this);
     //   listenCounterImage.setOnClickListener(this);
        user_name.setOnClickListener(this);
        user_avatar.setOnClickListener(this);
        play_button.setOnClickListener(this);
        moreButton.setOnClickListener(this);

        try {
            getData(postId);
            getComments(postId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initRecyclerView();


    }

    private void initRecyclerView() {
        mMessageAdapter = new MessageAdapter(PostsDetailsActivity.this, myCommentList, mInsertMessageListener);

        mLinearLayoutManager = new LinearLayoutManager(PostsDetailsActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mLinearLayoutManager.setStackFromEnd(true);
        mLinearLayoutManager.setReverseLayout(true);

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mMessageAdapter);

    }


    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view.getId() == R.id.detail_btn_send_message) {
            if (processLoggedState(view)) {
                return;
            } else {
                sendMessage();
            }

        }  else if(view.getId() == R.id.detail_list_item_like_button){

            if (processLoggedState(view)) {
                return;
            } else {

                if (likeButtonMain.isFavorite()){
                    likeButtonMain.setFavorite(false);
                    // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                    unLikeMethod();
                } else {
                    likeButtonMain.setFavorite(true);
                    if (HugButtonMain.isFavorite()){
                        HugButtonMain.setFavorite(false);
                        unHugMethod();
                    }
                    if (SameButtonMain.isFavorite()){
                        SameButtonMain.setFavorite(false);
                        unSameMethod();
                    }
                    likeCounter++;

                    String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                            myList.getIdUserName() + "_postId@" + myList.getIdPosts()  + "_click@" + "1";

                    if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())){
                        Timber.e("Same user");
                    } else {
                        sendLikeNotification(application, sendLike);
                    }
                    sendLikeToServer(application, 1, 0, 0, 0, "clicked like button");
                    like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
                }
            }

        } else if(view.getId() == R.id.detail_list_item_hug_button){
            if (processLoggedState(view)) {
                return;
            } else {
                if (HugButtonMain.isFavorite()){
                    HugButtonMain.setFavorite(false);
                    // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                    unHugMethod();
                } else {
                    HugButtonMain.setFavorite(true);
                    if (likeButtonMain.isFavorite()){
                        likeButtonMain.setFavorite(false);
                        unLikeMethod();
                    }
                    if (SameButtonMain.isFavorite()){
                        SameButtonMain.setFavorite(false);
                        unSameMethod();
                    }
                    hugCounter++;
                    String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                            myList.getIdUserName() + "_postId@" + myList.getIdPosts()  + "_click@" + "2";

                    if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())){

                    } else {
                        sendLikeNotification(application, sendLike);
                    }
                    sendLikeToServer(application, 0, 1, 0, 0, "clicked hug button");
                    hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
                }
            }



        } else if(view.getId() == R.id.detail_list_item_same_button){
            if (processLoggedState(view)) {
                return;
            } else {

                if (SameButtonMain.isFavorite()){
                    SameButtonMain.setFavorite(false);
                    // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                    unSameMethod();
                } else {
                    SameButtonMain.setFavorite(true);
                    if (likeButtonMain.isFavorite()){
                        likeButtonMain.setFavorite(false);
                        unLikeMethod();
                    }
                    if (HugButtonMain.isFavorite()){
                        HugButtonMain.setFavorite(false);
                        unHugMethod();
                    }

                    sameCounter++;
                    String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                            myList.getIdUserName() + "_postId@" + myList.getIdPosts()  + "_click@" + "3";

                    if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())){
                    } else {
                        sendLikeNotification(application, sendLike);
                    }
                    sendLikeToServer(application, 0, 0, 1, 0, "clicked sad button");
                    same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
                }

            }

        }

        else if(view.getId() == R.id.detail_status_more){

            if (processLoggedState(view)) {
                return;
            } else {
                popupMenu = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.pop_menu, popupMenu.getMenu());
                //    this.menu = popupMenu.getMenu();

                SharedPreferences preferences;
                preferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);

                if (MySharedPreferences.getUserId(preferences).equals(myList.getIdUserName())){
                    if(popupMenu.getMenu() == null)
                        return;
                    popupMenu.getMenu().setGroupVisible(R.id.main_menu_group, true);
                } else {
                    popupMenu.getMenu().setGroupVisible(R.id.main_menu_group, false);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit_post:

                                Intent editIntent = new Intent(PostsDetailsActivity.this, EditPost.class);
                                editIntent.putExtra(Constants.IDPOST, myList.getIdPosts());
                                editIntent.putExtra(Constants.STATUS_POST, myList.getTextStatus());
                                if (myList.getAudioFileLink().isEmpty() || myList.getAudioFileLink() == null){
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

                            case R.id.menu_item_share:
                                //            MenuItem menuItem = popupMenu.getMenu().findItem(R.id.menu_item_share);
                                //            Toast.makeText(itemView.getContext(), "Clicked report edit", Toast.LENGTH_SHORT).show();

                  /*          mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
                        //    int currentViewPagerItem = ((ViewPager) itemView.findViewById(R.id.viewpager)).getCurrentItem();

                            if (mShareActionProvider != null) {
                                mShareActionProvider.setShareIntent(sharedIntentMaker());
                            } else {
                                Toast.makeText(itemView.getContext(), "Share Action Provider is null", Toast.LENGTH_SHORT).show();
                              //  Log.d(LOG_TAG, "Share Action Provider is null?");
                            } */
                                startActivity(Intent.createChooser(sharedIntentMaker(), "Choose an app"));


                                return true;

                            default:
                                return false;

                        }
                    }
                });
                popupMenu.show();

            }


        } else if (view.getId() == R.id.detail_list_item_post_userNickName ||
                view.getId() == R.id.detail_list_item_posts_avatar){

            Intent intent = new Intent(this, SecondProfile.class);
            intent.putExtra(Constants.SECOND_PROFILE_ID, myList.getIdUserName());
            startActivity(intent);
        } else if (view.getId() == R.id.detail_list_item_posts_feeling){
            Intent intent = new Intent(this, UserFeelingActivity.class);
            intent.putExtra(Constants.EMOTION, myList.getIdFeeling());
            startActivity(intent);
        } else if (view.getId() == R.id.detail_list_item_posts_category){
            Intent intent = new Intent(this, UserCategoryActivity.class);
            intent.putExtra(Constants.CATEGORY, myList.getCategory());
            startActivity(intent);
        } else if (view.getId() == R.id.detail_post_likes_counter || view.getId() == R.id.detail_emoji_above_like){

            Intent intent = new Intent(this, UserLikeCounterActivity.class);
            intent.putExtra(Constants.LIKE_FEELING, myList.getIdPosts());
            startActivity(intent);
        } else if(view.getId() == R.id.detail_post_hugs_counter || view.getId() == R.id.detail_emoji_above_hug){
            Intent intent = new Intent(this, UserHugCounterActivity.class);
            intent.putExtra(Constants.HUG_FEELING, myList.getIdPosts());
            startActivity(intent);
        } else if(view.getId() == R.id.detail_post_same_counter || view.getId() == R.id.detail_emoji_above_same){
            Intent intent = new Intent(this, UserSameCounterActivity.class);
            intent.putExtra(Constants.SAME_FEELING, myList.getIdPosts());
            startActivity(intent);

        }

   /*     else if(view.getId() == R.id.detail_post_listen_counter){
            Intent intent = new Intent(this, UserListenCounterActivity.class);
            intent.putExtra(Constants.LISTEN_FEELING, myList.getIdPosts());
            startActivity(intent); */


    //    }
        else if(view.getId() == R.id.detail_list_item_posts_play_button){
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
        }
    }

    private void unSameMethod() {
        sendUnlikeToServer(application, 0, 1, 1, 1, "clicked unlike button");
        sameCounter--;
        same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
    }

    private void unHugMethod() {
        sendUnlikeToServer(application, 1, 0, 1, 1, "clicked unlike button");
        hugCounter--;
        hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
    }

    private void unLikeMethod() {
        sendUnlikeToServer(application, 1, 1, 0, 1, "clicked unlike button");
        likeCounter--;
        like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
    }

    private Intent sharedIntentMaker(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, myList.getTextStatus() + " " + "Voiceme");
        return shareIntent;
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

        if (!TextUtils.isEmpty(message)) {
            // Todo post comment on server

            try {
                postComment(message);
                if (idusername.equals(MySharedPreferences.getUserId(preferences))){
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
                    MySharedPreferences.getUsername(preferences)));
            mMessageEditText.setText("");
        } else {
            Toast.makeText(this, "You have not entered anything", Toast.LENGTH_SHORT).show();
        }
    }

    private void getComments(String postId) throws Exception {
        application.getWebService()
                .getUserComments(postId)
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
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
                        try {
                            Toast.makeText(PostsDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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


    private void postComment(String message) throws Exception {
        application.getWebService()
                .sendComment(MySharedPreferences.getUserId(preferences), idusername, postId, message)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Toast.makeText(PostsDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }








    private void getData(String postId) throws Exception {
        application.getWebService()
                .getSinglePost(postId, MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
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

        // Todo make JodaTIme

        if (myList.getPostTime() == null){
            return;
        } else {
            timeStamp.setText(CurrentTime.getCurrentTime(myList.getPostTime(), PostsDetailsActivity.this));
        }
        postMessage.setText(myList.getTextStatus());
        feeling.setText(myList.getEmotions());
        category.setText(myList.getCategory());
        post_comments.setText(String.valueOf(myList.getComments()));
        like_counter.setText(String.valueOf(myList.getLikes()));
        same_counter.setText(String.valueOf(myList.getSame()));
        hug_counter.setText(String.valueOf(myList.getHug()));


        if (myList.getAudioDuration() != null){
            post_audio_duration.setText(myList.getAudioDuration());
         //   post_listen.setText(String.valueOf(myList.getListen()));
        }

      //  likeCounter = myList.get(0).getLikes();
      //  hugCounter = myList.get(0).getHug();
     //   sameCounter = myList.get(0).getSame();

        user_avatar.setImageURI(myList.getAvatarPics());

        if (myList.getAudioFileLink() == null || myList.getAudioFileLink().isEmpty()){
            hideAudioButton(View.GONE);
        } else {
            hideAudioButton(View.VISIBLE);
        }

        if (myList.getUserLike() != null){
            if (myList.getUserLike()){
                likeButtonMain.setFavorite(true, false);
                //   likeButtonMain.setFavoriteResource(like_after);
            } else {
                likeButtonMain.setFavorite(false, false);
                //   likeButtonMain.setFavoriteResource(like_before);
            }


            if (myList.getUserHuge()){
                //    HugButtonMain.setFavoriteResource(hug_after);
                HugButtonMain.setFavorite(true, false);
            } else {
                HugButtonMain.setFavorite(false, false);
                //     HugButtonMain.setFavoriteResource(status_before);
            }


            if (myList.getUserSame()){
                SameButtonMain.setFavorite(true, false);
                //      SameButtonMain.setFavoriteResource(sad);
            } else {
                SameButtonMain.setFavorite(false, false);
                //  SameButtonMain.setFavoriteResource(status_before);
            }
        }

        if (myList.getReportAbuseCount() >= 2){
            postMessage.setText("******** This post is flagged as Abusive for General Public *********");
            hideAudioButton(View.GONE);

        }
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

    //LikeButton likeButton
      //  processLoggedState(buttonView.);

    /*    try {
            if (myClickListener != null) {
                myClickListener.onLikeUnlikeClick(myList.get(0), buttonView);
                final MaterialFavoriteButton likeButtonLcl = buttonView;
                if (doDislike) new Thread(new Runnable() {
                    @Override
                    public void run() {
                        l.pause(1000);
                        likeButtonLcl.post(new Runnable() {
                            @Override
                            public void run() {
                                likeButtonLcl.setFavorite(false, false);
                            }
                        });
                    }
                }).start();
            } else {
                Toast.makeText(buttonView.getContext(), "Click Event Null", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(buttonView.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
        }
        if (doDislike)
            return; */

  /*  @Override
    public void unLiked(MaterialFavoriteButton buttonView, boolean favorite likeButton) {
     //   processLoggedState(buttonView);

        if (doDislike)
            return;
        try {
            if (myClickListener != null) {
                return;
            } else {
                Toast.makeText(buttonView.getContext(), "Click Event Null", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(buttonView.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
        }



        if (buttonView == likeButtonMain) {
            likeCounter--;
            like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
            sendUnlikeToServer(application, 0, 1, 1, 1, "clicked unlike button");
        } else if (buttonView == HugButtonMain) {
            hugCounter--;
            hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
            sendUnlikeToServer(application, 1, 0, 1, 1, "clicked unlike button");
        } else if (buttonView == SameButtonMain) {
            sameCounter--;
            same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
            sendUnlikeToServer(application, 1, 1, 0, 1, "clicked unlike button");
        }
    } */

    protected void sendLikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        application.getWebService()
                .likes(MySharedPreferences.getUserId(preferences), postId, like, hug, same, listen)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<PostLikesResponse>() {
                    @Override
                    public void onNext(PostLikesResponse postLikesResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Toast.makeText(PostsDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                .subscribe(new BaseSubscriber<PostLikesResponse>() {
                    @Override
                    public void onNext(PostLikesResponse postLikesResponse) {
                    }
                    @Override
                    public void onError(Throwable e) {
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


}
