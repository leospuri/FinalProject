package in.voiceme.app.voiceme.ProfilePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import in.voiceme.app.voiceme.DiscoverPage.LikeUnlikeClickListener;
import in.voiceme.app.voiceme.DiscoverPage.PostsCardViewHolder;
import in.voiceme.app.voiceme.PostsDetails.UserCategoryActivity;
import in.voiceme.app.voiceme.PostsDetails.UserFeelingActivity;
import in.voiceme.app.voiceme.PostsDetails.UserHugCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserLikeCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserListenCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserSameCounterActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.WasLoggedInInterface;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.PostsModel;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

/**
 * Created by harish on 12/29/2016.
 */

public class TotalPostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static LikeUnlikeClickListener myClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public List<PostsModel> dataSet;
    private Context mContext;
    private int mLastPosition = 5;
    private double current_lat, current_long;

    public TotalPostsAdapter(List<PostsModel> productLists, Context mContext) {
        this.mContext = mContext;
        this.dataSet = productLists;
    }

    public void setOnItemClickListener(LikeUnlikeClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void animateTo(List<PostsModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<PostsModel> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final PostsModel model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<PostsModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final PostsModel model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }


    private void applyAndAnimateMovedItems(List<PostsModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final PostsModel model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(PostsModel item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void addItem(int position, PostsModel model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void removeItem(PostsModel item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public PostsModel removeItem(int position) {
        final PostsModel model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void clearItem() {
        if (dataSet != null)
            dataSet.clear();
    }

    public void moveItem(int fromPosition, int toPosition) {
        final PostsModel model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public PostsModel getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.list_item_posts_cardview, parent, false);
            vh = new TotalPostsAdapter.EventViewHolder(itemView);
        } else if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new TotalPostsAdapter.ProgressViewHolder(v);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TotalPostsAdapter.EventViewHolder) {
            PostsModel dataItem = dataSet.get(position);
            ((TotalPostsAdapter.EventViewHolder) holder).bind(dataItem);
        } else {
            ((TotalPostsAdapter.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        if (dataSet != null)
            return dataSet.size();
        else
            return 0;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventViewHolder extends PostsCardViewHolder implements View.OnClickListener, OnLikeListener, WasLoggedInInterface {

        private boolean doDislike;

        public EventViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            processLoggedState(view);
            try {
                if (myClickListener != null) {
                    myClickListener.onItemClick(dataItem, view);
                } else {
                    Toast.makeText(view.getContext(), "Click Event Null", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                Toast.makeText(view.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
            }
        }

        protected void secondUserProfileClicked(View view){
            Intent intent = new Intent(view.getContext(), SecondProfile.class);
            Toast.makeText(view.getContext(), "Post ID is " + dataItem.getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.SECOND_PROFILE_ID, dataItem.getIdUserName());
            view.getContext().startActivity(intent);
        }

        protected void playButton(View view){

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
                play_button.showProgress(true);
                try {
                    mediaPlayer.setDataSource(dataItem.getAudioFileLink());
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            try {
                                play_button.showProgress(false);
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
                    play_button.showProgress(false);
                    flipPlayPauseButton(false);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        public void flipPlayPauseButton(boolean isPlaying){
            if (isPlaying){
                play_button.setIcon(R.drawable.stop_button, R.drawable.play_button);
            } else  {
                play_button.setIcon(R.drawable.play_button, R.drawable.stop_button);
            }
        }

        @Override
        protected void likeCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserLikeCounterActivity.class);
            Toast.makeText(v.getContext(), "Post ID is " + dataItem.getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.LIKE_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void listenCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserListenCounterActivity.class);
            Toast.makeText(v.getContext(), "Post ID is " + dataItem.getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.LISTEN_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void categoryClicked(View v) {
            processLoggedState(v);
            Intent intent = new Intent(v.getContext(), UserCategoryActivity.class);
            intent.putExtra(Constants.CATEGORY, getCategory().getText().toString());
            Toast.makeText(v.getContext(), "category ID is " + getCategory().getText().toString(), Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        }

        @Override
        protected void hugCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserHugCounterActivity.class);
            Toast.makeText(v.getContext(), "Post ID is " + dataItem.getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.HUG_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void feelingClicked(View v) {
            processLoggedState(v);
            Intent intent = new Intent(v.getContext(), UserFeelingActivity.class);
            intent.putExtra(Constants.EMOTION, getFeeling().getText().toString());
            Toast.makeText(v.getContext(), "Feeling ID is " + getFeeling().getText().toString(), Toast.LENGTH_SHORT).show();
            v.getContext().startActivity(intent);
        }

        @Override
        protected void sameCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserSameCounterActivity.class);
            Toast.makeText(v.getContext(), "Post ID is " + dataItem.getIdPosts(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.SAME_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        @Override
        public void liked(LikeButton likeButton) {
            processLoggedState(likeButton);

            likeCounter = Integer.parseInt(like_counter.getText().toString());
            hugCounter = Integer.parseInt(hug_counter.getText().toString());
            sameCounter = Integer.parseInt(same_counter.getText().toString());
            try {
                if (myClickListener != null) {
                    myClickListener.onLikeUnlikeClick(dataItem, likeButton);
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
                SharedPreferences preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);
                String userId = MySharedPreferences.getUserId(preferences);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName()  + "_postId@" + dataItem.getIdPosts()  + "_click@" + "1";


                sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 0, 0, "clicked like button");
                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    Toast.makeText(itemView.getContext(), "same user", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                }

            } else if (likeButton == HugButtonMain) {
                hugCounter++;
                hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
                SharedPreferences preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);
                String userId = MySharedPreferences.getUserId(preferences);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName() + "_postId@" + dataItem.getIdPosts()  + "_click@" + "2";


                sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 1, 0, 0, "clicked hug button");
                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    Toast.makeText(itemView.getContext(), "same user", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                }
            } else if (likeButton == SameButtonMain) {
                sameCounter++;
                same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));

                SharedPreferences preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);
                String userId = MySharedPreferences.getUserId(preferences);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName() + "_postId@" + dataItem.getIdPosts()  + "_click@" + "3";

                sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 0, 1, 0, "clicked same button");
                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    Toast.makeText(itemView.getContext(), "same user", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                }
            }
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            processLoggedState(likeButton);

            likeCounter = Integer.parseInt(like_counter.getText().toString());
            hugCounter = Integer.parseInt(hug_counter.getText().toString());
            sameCounter = Integer.parseInt(same_counter.getText().toString());

            if (doDislike)
                return;
            try {
                if (myClickListener != null) {
                    myClickListener.onLikeUnlikeClick(dataItem, likeButton);
                } else {
                    Toast.makeText(likeButton.getContext(), "Click Event Null", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                Toast.makeText(likeButton.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
            }

            if (likeButton == likeButtonMain) {
                --likeCounter;
                like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 1, 1, 1, "clicked unlike button");
            } else if (likeButton == HugButtonMain) {
                --hugCounter;
                hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 1, 1, "clicked unlike button");
            } else if (likeButton == SameButtonMain) {
                --sameCounter;
                same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 1, 0, 1, "clicked unlike button");
            }
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

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}
