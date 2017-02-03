package in.voiceme.app.voiceme.PostsDetails;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import in.voiceme.app.voiceme.DiscoverPage.LikeUnlikeClickListener;
import in.voiceme.app.voiceme.ProfilePage.SecondProfile;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.LikesResponse;
import in.voiceme.app.voiceme.services.PostsModel;
import in.voiceme.app.voiceme.userpost.UserResponse;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.R.id.detail_list_item_posts_avatar;

public class PostsDetailsActivity extends BaseActivity implements View.OnClickListener, OnLikeListener {

    EditText mMessageEditText;
    ImageButton mSendMessageImageButton;
    RecyclerView mMessageRecyclerView;
    private MessageAdapter mMessageAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String postId;
    private List<PostsModel> myList;
    List<UserCommentModel> myCommentList;
    private static LikeUnlikeClickListener myClickListener;

    private boolean doDislike;

    private ImageView user_avatar;
    private ImageView play_button;

    private TextView user_name;
    private TextView isPost;
    private TextView feeling;
    private TextView category;

    //post data
    private TextView timeStamp;
    private TextView postMessage;
    private TextView postReadMore;
    private TextView post_audio_duration;

    //counter numbers
    private TextView like_counter;
    private TextView hug_counter;
    private TextView same_counter;
    private TextView post_comments;
    private TextView post_listen;

    //emoji for like, hug and same above
    private ImageView likeCounterImage;
    private ImageView hugCounterImage;
    private ImageView sameCounterImage;
    private ImageView commentCounterImage;
    private ImageView listenCounterImage;

    private int likeCounter;
    private int hugCounter;
    private int sameCounter;
    protected MediaPlayer mediaPlayer = new MediaPlayer();


    //animated buttons
    private LikeButton likeButtonMain, HugButtonMain, SameButtonMain;



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
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        postId = getIntent().getStringExtra(Constants.POST_BACKGROUND);



        //Imageview for avatar and play pause button
        user_avatar = (ImageView) findViewById(detail_list_item_posts_avatar);
        play_button = (ImageView) findViewById(R.id.detail_list_item_posts_play_button);

        //username, feeling and category
        user_name = (TextView) findViewById(R.id.detail_list_item_post_userNickName);
        isPost = (TextView) findViewById(R.id.detail_list_item_post_is);
        feeling = (TextView) findViewById(R.id.detail_list_item_posts_feeling);
        category = (TextView) findViewById(R.id.detail_list_item_posts_category);

        //post data
        post_audio_duration = (TextView) findViewById(R.id.detail_list_item_posts_duration_count);
        timeStamp = (TextView) findViewById(R.id.detail_list_item_posts_timeStamp);
        postMessage = (TextView) findViewById(R.id.detail_list_item_posts_message);
        postReadMore = (TextView) findViewById(R.id.detail_list_item_posts_read_more);

        //counter numbers
        like_counter = (TextView) findViewById(R.id.detail_post_likes_counter);
        hug_counter = (TextView) findViewById(R.id.detail_post_hugs_counter);
        same_counter = (TextView) findViewById(R.id.detail_post_same_counter);
        post_comments = (TextView) findViewById(R.id.detail_post_comment_counter);
        post_listen = (TextView) findViewById(R.id.detail_post_listen_counter);

        //emoji for like, hug and same above
        likeCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_like);
        hugCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_hug);
        sameCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_same);
        commentCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_comment);
        listenCounterImage = (ImageView) findViewById(R.id.detail_emoji_above_listen);

        //animated buttons
        likeButtonMain = (LikeButton) findViewById(R.id.detail_list_item_like_button);
        HugButtonMain = (LikeButton) findViewById(R.id.detail_list_item_hug_button);
        SameButtonMain = (LikeButton) findViewById(R.id.detail_list_item_same_button);

        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mMessageEditText = (EditText) findViewById(R.id.detail_et_message);
        mSendMessageImageButton = (ImageButton) findViewById(R.id.detail_btn_send_message);
        mMessageRecyclerView = (RecyclerView) findViewById(R.id.detail_rv_messages);

        mSendMessageImageButton.setOnClickListener(this);
        //OnClickListeners
        likeButtonMain.setOnLikeListener(this);
        HugButtonMain.setOnLikeListener(this);
        SameButtonMain.setOnLikeListener(this);

        like_counter.setOnClickListener(this);
        hug_counter.setOnClickListener(this);
        same_counter.setOnClickListener(this);
        post_comments.setOnClickListener(this);
        post_listen.setOnClickListener(this);
        category.setOnClickListener(this);
        feeling.setOnClickListener(this);
        likeCounterImage.setOnClickListener(this);
        hugCounterImage.setOnClickListener(this);
        commentCounterImage.setOnClickListener(this);
        listenCounterImage.setOnClickListener(this);
        user_name.setOnClickListener(this);
        user_avatar.setOnClickListener(this);
        play_button.setOnClickListener(this);

        try {
            if (postId != null){
                Toast.makeText(this, "post is not null: " + postId, Toast.LENGTH_SHORT).show();
            }
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

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setAdapter(mMessageAdapter);
    }


    @Override
    public void onClick(View view) {
        processLoggedState(view);
        if (view.getId() == R.id.detail_btn_send_message) {
            sendMessage();
        } else if (view.getId() == R.id.detail_list_item_post_userNickName ||
                view.getId() == R.id.detail_list_item_posts_avatar){
            Intent intent = new Intent(this, SecondProfile.class);
            Toast.makeText(view.getContext(), "Post ID is " + myList.get(0).getIdUserName(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.SECOND_PROFILE_ID, myList.get(0).getIdUserName());
            startActivity(intent);
        } else if (view.getId() == R.id.detail_list_item_posts_feeling){
            Intent intent = new Intent(this, UserFeelingActivity.class);
            intent.putExtra(Constants.EMOTION, myList.get(0).getEmotions());
            startActivity(intent);
        } else if (view.getId() == R.id.detail_list_item_posts_category){
            Intent intent = new Intent(this, UserCategoryActivity.class);
            intent.putExtra(Constants.CATEGORY, myList.get(0).getCategory());
            startActivity(intent);
        } else if (view.getId() == R.id.detail_post_likes_counter){

            Intent intent = new Intent(this, UserLikeCounterActivity.class);
            Toast.makeText(this, "Post ID is " + myList.get(0).getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.LIKE_FEELING, myList.get(0).getIdPosts());
            startActivity(intent);
        } else if(view.getId() == R.id.detail_post_hugs_counter){
            Intent intent = new Intent(this, UserHugCounterActivity.class);
            Toast.makeText(this, "Post ID is " + myList.get(0).getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.HUG_FEELING, myList.get(0).getIdPosts());
            startActivity(intent);
        } else if(view.getId() == R.id.detail_post_same_counter){
            Intent intent = new Intent(this, UserSameCounterActivity.class);
            Toast.makeText(this, "Post ID is " + myList.get(0).getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.SAME_FEELING, myList.get(0).getIdPosts());
            startActivity(intent);

        } else if(view.getId() == R.id.detail_post_listen_counter){
            Intent intent = new Intent(this, UserListenCounterActivity.class);
            Toast.makeText(this, "Post ID is " + myList.get(0).getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.LISTEN_FEELING, myList.get(0).getIdPosts());
            startActivity(intent);
        } else if(view.getId() == R.id.detail_list_item_posts_play_button){
            if (!mediaPlayer.isPlaying()){
                if (mediaPlayer != null){
                    try {
                        mediaPlayer.stop();
                    } catch (Exception e){

                    }
                    mediaPlayer = null;
                }

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(myList.get(0).getAudioFileLink());
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
                String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" + "21"
                       /* "dataItem.getIdUserName()" */ + "_postId" + postId  + "_click" + "5";
                sendLikeNotification(application, sendLike);

            } catch (Exception e) {
                e.printStackTrace();
            }
            mMessageAdapter.addMessage(new UserCommentModel(message,
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<UserCommentModel>>() {
                    @Override
                    public void onNext(List<UserCommentModel> response) {
                        Log.e("RESPONSE:::", "Size===" + response.size());
                        showComments(response);
                    }
                });
    }

    private void showComments(final List<UserCommentModel> myList) {
        this.myCommentList = myList;
        mMessageAdapter = new MessageAdapter(this, myList, mInsertMessageListener);
        mMessageRecyclerView.setAdapter(mMessageAdapter);
    }


    private void postComment(String message) throws Exception {
        application.getWebService()
                .sendComment(MySharedPreferences.getUserId(preferences), postId, message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse userResponse) {
                        Toast.makeText(PostsDetailsActivity.this, "UserResponse for posting comments " + userResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getData(String postId) throws Exception {
        application.getWebService()
                .getSinglePost(postId, MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        String name = response.get(0).getIdUserName();
                        Toast.makeText(PostsDetailsActivity.this, "response for details", Toast.LENGTH_SHORT).show();

                        showRecycleWithDataFilled(response);
                    }
                });
    }


    private void showRecycleWithDataFilled(final List<PostsModel> myList) {

        this.myList = myList;

        user_name.setText(myList.get(0).getUserNicName());
        timeStamp.setText(myList.get(0).getPostTime());
        postMessage.setText(myList.get(0).getTextStatus());
        feeling.setText(myList.get(0).getEmotions());
        category.setText(myList.get(0).getCategory());
        post_comments.setText(myList.get(0).getComments());
        like_counter.setText(myList.get(0).getLikes());
        same_counter.setText(myList.get(0).getSame());
        hug_counter.setText(myList.get(0).getHug());


        if (myList.get(0).getAudioDuration() != null){
            post_audio_duration.setText(myList.get(0).getAudioDuration());
            post_listen.setText(myList.get(0).getListen());
        }

        likeCounter = myList.get(0).getLikes();
        hugCounter = myList.get(0).getHug();
        sameCounter = myList.get(0).getSame();

        if (!myList.get(0).getAvatarPics().equals("")) {
            Picasso.with(this)
                    .load(myList.get(0).getAvatarPics())
                    .resize(75, 75)
                    .centerInside()
                    .into(user_avatar);
        }

        if (myList.get(0).getUserLike() != null){
            if (myList.get(0).getUserLike()){
                likeButtonMain.setLiked(true);
            } else {
                likeButtonMain.setLiked(false);
            }


            if (myList.get(0).getUserHuge()){
                HugButtonMain.setLiked(true);
            } else {
                HugButtonMain.setLiked(false);
            }


            if (myList.get(0).getUserSame()){
                SameButtonMain.setLiked(true);
            } else {
                SameButtonMain.setLiked(false);
            }
        }

        if (myList.get(0).getAudioFileLink() == null){
            play_button.setVisibility(View.GONE);
            post_audio_duration.setVisibility(View.GONE);
            post_listen.setVisibility(View.GONE);
            listenCounterImage.setVisibility(View.GONE);
        } else {
            play_button.setVisibility(View.VISIBLE);
            post_audio_duration.setVisibility(View.VISIBLE);
            post_listen.setVisibility(View.VISIBLE);
            listenCounterImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void liked(LikeButton likeButton) {
        processLoggedState(likeButton);

        try {
            if (myClickListener != null) {
                myClickListener.onLikeUnlikeClick(myList.get(0), likeButton);
                final LikeButton likeButtonLcl = likeButton;
                if (doDislike) new Thread(new Runnable() {
                    @Override
                    public void run() {
                        l.pause(1000);
                        likeButtonLcl.post(new Runnable() {
                            @Override
                            public void run() {
                                likeButtonLcl.setLiked(false);
                            }
                        });
                    }
                }).start();
            } else {
                Toast.makeText(likeButton.getContext(), "Click Event Null", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(likeButton.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
        }
        if (doDislike)
            return;
        if (likeButton == likeButtonMain) {
            likeCounter++;
            like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));

            String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                    myList.get(0).getIdUserName() + "_postId@" + postId  + "_click@" + "1";



            sendLikeToServer(application, 1, 0, 0, 0, "clicked like button");
            if (MySharedPreferences.getUserId(preferences).equals(myList.get(0).getIdUserName())){
                Toast.makeText(this, "same user", Toast.LENGTH_SHORT).show();
            } else {
                sendLikeNotification(application, sendLike);
            }

        } else if (likeButton == HugButtonMain) {
            hugCounter++;
            hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
            String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" + myList.get(0).getIdUserName()
                    + "_postId@" + postId  + "_click@" + "2";

            sendLikeToServer(application, 0, 1, 0, 0, "clicked hug button");
            if (MySharedPreferences.getUserId(preferences).equals(myList.get(0).getIdUserName())){
                Toast.makeText(this, "same user", Toast.LENGTH_SHORT).show();
            } else {
                sendLikeNotification(application, sendLike);
            }
        } else if (likeButton == SameButtonMain) {
            sameCounter++;
            same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
            String sendLike = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                    myList.get(0).getIdUserName() + "_postId@" + postId  + "_click@" + "3";
            sendLikeToServer(application, 0, 0, 1, 0, "clicked same button");
            if (MySharedPreferences.getUserId(preferences).equals(myList.get(0).getIdUserName())){
                Toast.makeText(this, "same user", Toast.LENGTH_SHORT).show();
            } else {
                sendLikeNotification(application, sendLike);
            }
        }
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        processLoggedState(likeButton);

        if (doDislike)
            return;
        try {
            if (myClickListener != null) {
                return;
            } else {
                Toast.makeText(likeButton.getContext(), "Click Event Null", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            Toast.makeText(likeButton.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
        }

        if (likeButton == likeButtonMain) {
            likeCounter--;
            like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
            sendUnlikeToServer(application, 0, 1, 1, 1, "clicked unlike button");
        } else if (likeButton == HugButtonMain) {
            hugCounter--;
            hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
            sendUnlikeToServer(application, 1, 0, 1, 1, "clicked unlike button");
        } else if (likeButton == SameButtonMain) {
            sameCounter--;
            same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
            sendUnlikeToServer(application, 1, 1, 0, 1, "clicked unlike button");
        }
    }

    protected void sendLikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        application.getWebService().likes(MySharedPreferences.getUserId(preferences), postId, like, hug, same, listen)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LikesResponse>() {
                    @Override
                    public void onNext(LikesResponse likesResponse) {
                        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected void sendLikeNotification(final VoicemeApplication application, String likeUrl) {
        application.getWebService()
                .sendLikeNotification(likeUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        //  Timber.d("Message from server" + response);
                    }
                });
    }

    protected void sendUnlikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        application.getWebService().unlikes(MySharedPreferences.getUserId(preferences), postId, like, hug, same, listen)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LikesResponse>() {
                    @Override
                    public void onNext(LikesResponse likesResponse) {
                        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            if (!viewPrm.getClass().getCanonicalName().contains(("LikeButton")))
                Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show();
            else
                doDislike = true;
            return true;
        }
        return false;

    }

}
