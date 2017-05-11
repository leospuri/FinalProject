package in.voiceme.app.voiceme.DiscoverPage;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import in.voiceme.app.voiceme.DTO.PostLikesResponse;
import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.CurrentTime;
import mbanje.kurt.fabbutton.FabButton;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

public abstract class PostsCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected int likeCounter;
    protected int hugCounter;
    protected int sameCounter;
    //Imageview for avatar and play pause button

    protected SimpleDraweeView  user_avatar;
    protected ImageView more_button;
    protected FabButton play_button;

    //username, feeling and category
    protected TextView user_name;
    protected TextView isPost;
    protected TextView feeling;
    protected TextView category;

    //post data
    protected TextView timeStamp;
    protected TextView postMessage;
    protected TextView postReadMore;
    protected TextView post_audio_duration;

    //counter numbers
    protected TextView like_counter;
    protected TextView hug_counter;
    protected TextView same_counter;
    protected TextView post_comments;
//    protected TextView post_listen;

    //emoji for like, hug and same above
    protected ImageView commentCounterImage;
  //  protected ImageView listenCounterImage;
    protected MediaPlayer mediaPlayer = new MediaPlayer();


    //animated buttons
    protected MaterialFavoriteButton likeButtonMain, HugButtonMain, SameButtonMain;

    protected View parent_row;

    protected PostsModel dataItem;

    public PostsCardViewHolder(View itemView) {
        super(itemView);
        //Imageview for avatar and play pause button
        user_avatar = (SimpleDraweeView) itemView.findViewById(R.id.list_item_posts_avatar);
        play_button = (FabButton) itemView.findViewById(R.id.list_item_posts_play_button);
        more_button = (ImageView) itemView.findViewById(R.id.status_more);

        //username, feeling and category
        user_name = (TextView) itemView.findViewById(R.id.list_item_post_userNickName);
        isPost = (TextView) itemView.findViewById(R.id.list_item_post_is);
        feeling = (TextView) itemView.findViewById(R.id.list_item_posts_feeling);
        category = (TextView) itemView.findViewById(R.id.list_item_posts_category);

        //post data
        post_audio_duration = (TextView) itemView.findViewById(R.id.list_item_posts_duration_count);
        timeStamp = (TextView) itemView.findViewById(R.id.list_item_posts_timeStamp);
        postMessage = (TextView) itemView.findViewById(R.id.list_item_posts_message);
        postReadMore = (TextView) itemView.findViewById(R.id.list_item_posts_read_more);

        postReadMore.setVisibility(View.GONE);
        //counter numbers
        like_counter = (TextView) itemView.findViewById(R.id.post_likes_counter);
        hug_counter = (TextView) itemView.findViewById(R.id.post_hugs_counter);
        same_counter = (TextView) itemView.findViewById(R.id.post_same_counter);
        post_comments = (TextView) itemView.findViewById(R.id.post_comment_counter);
    //    post_listen = (TextView) itemView.findViewById(R.id.post_listen_counter);

        //emoji for like, hug and same above
        commentCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_comment);
       // listenCounterImage = (ImageView) itemView.findViewById(R.id.emoji_above_listen);

        //animated buttons
        likeButtonMain = (MaterialFavoriteButton) itemView.findViewById(R.id.list_item_like_button);
        HugButtonMain = (MaterialFavoriteButton) itemView.findViewById(R.id.list_item_hug_button);
        SameButtonMain = (MaterialFavoriteButton) itemView.findViewById(R.id.list_item_same_button);

        parent_row = (View) itemView.findViewById(R.id.parent_row);

        //OnClickListeners

        postReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardBackground(v);
            }
        });
        postMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardBackground(v);
            }
        });
        likeButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeButtonMethod(view);
            }
        });

        HugButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hugButtonMethod(view);
            }
        });

        SameButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sameButtonMethod(view);
            }
        });



        like_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCounterClicked(v);
            }
        });
        hug_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hugCounterClicked(v);
            }
        });
        same_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sameCounterClicked(v);
            }
        });
        post_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardBackground(v);
            }
        });


    /*    post_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenCounterClicked(v);
            }
        });

        */
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playButton(view);
            }
        });


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryClicked(view);
            }
        });
        feeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feelingClicked(view);
            }
        });

        commentCounterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardBackground(v);
            }
        });

        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondUserProfileClicked(view);
            }
        });

        user_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                secondUserProfileClicked(view);
            }
        });

        parent_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardBackground(view);
            }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreClick(view);
            }
        });
    }

    protected void likeButtonMethod(View view) {

    }

    protected void hugButtonMethod(View view) {

    }

    protected void sameButtonMethod(View view) {

    }

    protected void moreClick(View view){

    }

    protected void secondUserProfileClicked(View view){

    }

    protected void playButton(View view){

    }

    protected void cardBackground(View view) {

    }

    protected void categoryClicked(View view) {

    }

    protected void feelingClicked(View view) {

    }

    protected void listenCounterClicked(View v) {
    }

    protected void commentsCounterClicked(View v) {
    }

    protected void likeCounterClicked(View v) {
    }

    protected void hugCounterClicked(View v) {
    }

    protected void sameCounterClicked(View v) {
    }

    @Override
    public void onClick(View v) {
    }

    public void bind(PostsModel dataItem) {
        this.dataItem = dataItem;

        if (dataItem.getLikes() != null && dataItem.getHug() != null && dataItem.getSame() != null){
            setLikeCounter(dataItem.getLikes());
            setHugCounter(dataItem.getHug());
            setSameCounter(dataItem.getSame());
        } else {
            return;
        }

        user_name.setText(dataItem.getUserNicName());
        feeling.setText(dataItem.getEmotions());
        category.setText(dataItem.getCategory());

        if (dataItem.getPostTime() == null){
            return;
        } else {
            timeStamp.setText(CurrentTime.getCurrentTime(dataItem.getPostTime(), itemView.getContext()));
        }

        if (dataItem.getTextStatus().length() > 140){
            postReadMore.setVisibility(View.VISIBLE);
        }
        postMessage.setText(dataItem.getTextStatus());
        post_comments.setText(String.valueOf(dataItem.getComments()));
        like_counter.setText(String.valueOf(dataItem.getLikes()));
        hug_counter.setText(String.valueOf(dataItem.getHug()));
        same_counter.setText(String.valueOf(dataItem.getSame()));


            user_avatar.setImageURI(dataItem.getAvatarPics());



       /* if (!dataItem.getAvatarPics().equals("") || dataItem.getAvatarPics() != null) {
            Picasso.with(itemView.getContext())
                    .load(dataItem.getAvatarPics())
                    .resize(75, 75)
                    .centerInside()
                    .into(user_avatar);
        } */

        if (dataItem.getAudioDuration() != null && !dataItem.getAudioDuration().isEmpty()){
            post_audio_duration.setText(String.valueOf(dataItem.getAudioDuration()));
     //       post_listen.setText(String.valueOf(dataItem.getListen()));
        } else {
            post_audio_duration.setText(null);
        }

        if (dataItem.getUserLike() != null){
            if (dataItem.getUserLike()){
                likeButtonMain.setFavorite(true, false);
             //   likeButtonMain.setFavoriteResource(like_after);
            } else {
                likeButtonMain.setFavorite(false, false);
             //   likeButtonMain.setFavoriteResource(like_before);
            }


            if (dataItem.getUserHuge()){
            //    HugButtonMain.setFavoriteResource(hug_after);
                HugButtonMain.setFavorite(true, false);
            } else {
                HugButtonMain.setFavorite(false, false);
           //     HugButtonMain.setFavoriteResource(status_before);
            }


            if (dataItem.getUserSame()){
                SameButtonMain.setFavorite(true, false);
          //      SameButtonMain.setFavoriteResource(sad);
            } else {
                SameButtonMain.setFavorite(false, false);
              //  SameButtonMain.setFavoriteResource(status_before);
            }
        } else {
            return;
        }

        if (dataItem.getAudioFileLink() == null || dataItem.getAudioFileLink().isEmpty()){
            hideAudioButton(View.GONE);
        } else {
            hideAudioButton(View.VISIBLE);
        }

        if (dataItem.getReportAbuseCount() >= 2){
            postMessage.setText("***** This post is flagged as Abusive for General Public ******");
            hideAudioButton(View.GONE);

        } else {
            return;
        }

    }

    private void hideAudioButton(int gone) {
        play_button.setVisibility(gone);
        post_audio_duration.setVisibility(gone);
   //     post_listen.setVisibility(gone);
        // listenCounterImage.setVisibility(gone);
    }


    protected void sendLikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        SharedPreferences preferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
        String userId = MySharedPreferences.getUserId(preferences);
        String postId = dataItem.getIdPosts();
        application.getWebService()
                .likes(userId, postId, like, hug, same, listen)
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PostLikesResponse>() {
                    @Override
                    public void onNext(PostLikesResponse postLikesResponse) {
                    }
                });
    }

    protected void sendUnlikeToServer(final VoicemeApplication application, int like, int hug, int same, int listen, final String message) {
        SharedPreferences preferences = application.getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
        String userId = MySharedPreferences.getUserId(preferences);
        String postId = dataItem.getIdPosts();
        application.getWebService()
                .unlikes(userId, postId, like, hug, same, listen)
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PostLikesResponse>() {
                    @Override
                    public void onNext(PostLikesResponse postLikesResponse) {
                    }
                });
    }





    protected void sendLikeNotification(final VoicemeApplication application, String likeUrl) {


        application.getWebService()
                .sendLikeNotification(likeUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                          Timber.d("Message from server" + response);
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
}