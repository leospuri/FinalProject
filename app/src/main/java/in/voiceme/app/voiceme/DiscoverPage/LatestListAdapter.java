package in.voiceme.app.voiceme.DiscoverPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.PostsDetails.PostsDetailsActivity;
import in.voiceme.app.voiceme.PostsDetails.UserCategoryActivity;
import in.voiceme.app.voiceme.PostsDetails.UserFeelingActivity;
import in.voiceme.app.voiceme.PostsDetails.UserHugCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserLikeCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserListenCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserSameCounterActivity;
import in.voiceme.app.voiceme.ProfilePage.SecondProfile;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.WasLoggedInInterface;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.userpost.EditPost;
import in.voiceme.app.voiceme.userpost.ReportAbuseActivity;
import in.voiceme.app.voiceme.utils.PaginationAdapterCallback;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;


/**
 * Created by ericbasendra on 02/12/15.
 */
public class LatestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static LikeUnlikeClickListener myClickListener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public List<PostsModel> dataSet;
   // private PostsModel postsModel;
    private Context mContext;
    private int mLastPosition = 5;
    private double current_lat, current_long;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;
    private PaginationAdapterCallback mCallback;
   // private static PostMoreButton postMoreButton;


    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = dataSet.size() - 1;
        PostsModel result = getItem(position);

        if (result != null) {
            dataSet.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void add(PostsModel r) {
        dataSet.add(r);
        notifyItemInserted(dataSet.size() - 1);
    }

    public void addAll(List<PostsModel> moveResults) {
        for (PostsModel result : moveResults) {
            add(result);
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        addItem(dataSet.size(), new PostsModel() );
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(dataSet.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    public LatestListAdapter(Context context) {
        this.mContext = context;
   //     this.mCallback = (PaginationAdapterCallback) context;
        dataSet = new ArrayList<>();
    }

    public LatestListAdapter(List<PostsModel> productLists, Context mContext) {
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
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
     //   return (position == dataSet.size() - 1) ? VIEW_ITEM : VIEW_PROG;
     // current   return dataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        return dataSet.get((dataSet.size() - 1)) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.list_item_posts_cardview, parent, false);
            vh = new EventViewHolder(itemView);
        } else if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new ProgressViewHolder(v);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder) {
          //  position = (dataSet.size() - 1);
            PostsModel dataItem = dataSet.get(position);
            ((EventViewHolder) holder).bind(dataItem);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventViewHolder extends PostsCardViewHolder implements View.OnClickListener, WasLoggedInInterface {

        boolean isPlaying = false;
        private boolean doDislike;
        private PopupMenu popupMenu;
        private Menu menu;

        public EventViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
  //          processLoggedState(view);
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
            processLoggedState(view);
            Intent intent = new Intent(view.getContext(), SecondProfile.class);
            intent.putExtra(Constants.SECOND_PROFILE_ID, dataItem.getIdUserName());
            view.getContext().startActivity(intent);
        }

        protected void playButton(View view){

                if (!mediaPlayer.isPlaying()){
                    if (mediaPlayer != null){
                        try {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();
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
                                    mediaPlayer.start();
                                    play_button.showProgress(false);
                                    flipPlayPauseButton(true);
                                } catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                play_button.showProgress(false);
                                flipPlayPauseButton(false);
                                mediaPlayer.stop();
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
        protected void listenCounterClicked(View v) {
            processLoggedState(v);
     //       if (processLoggedState(v))
     //           return;
            Intent intent = new Intent(v.getContext(), UserListenCounterActivity.class);
            intent.putExtra(Constants.LISTEN_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void categoryClicked(View v) {
            processLoggedState(v);
            Intent intent = new Intent(v.getContext(), UserCategoryActivity.class);
            intent.putExtra(Constants.CATEGORY, getCategory().getText().toString());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void cardBackground(View view) {
            processLoggedState(view);
      //      if (processLoggedState(view))
      //          return;
            Intent intent = new Intent(view.getContext(), PostsDetailsActivity.class);
            intent.putExtra(Constants.POST_BACKGROUND, dataItem.getIdPosts());
            view.getContext().startActivity(intent);
        }

        @Override
        protected void feelingClicked(View v) {
            processLoggedState(v);
  //          processLoggedState(v);
            // add feeling ID to get feeling Posts from current pojo

            Intent intent = new Intent(v.getContext(), UserFeelingActivity.class);
            intent.putExtra(Constants.EMOTION, getFeeling().getText().toString());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void likeCounterClicked(View v) {
            processLoggedState(v);
     //       if (processLoggedState(v))
     //           return;

            Intent intent = new Intent(v.getContext(), UserLikeCounterActivity.class);
            intent.putExtra(Constants.LIKE_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        protected void moreClick(View view){
             processLoggedState(view);

                popupMenu = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.pop_menu, popupMenu.getMenu());
                //    this.menu = popupMenu.getMenu();

                SharedPreferences preferences;
                preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);

                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    popupMenu.getMenu().setGroupVisible(R.id.main_menu_group, true);
                } else {
                    popupMenu.getMenu().setGroupVisible(R.id.main_menu_group, false);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit_post:

                                Intent editIntent = new Intent(itemView.getContext(), EditPost.class);
                                editIntent.putExtra(Constants.IDPOST, dataItem.getIdPosts());
                                editIntent.putExtra(Constants.IDUSERNAME, dataItem.getIdUserName());
                                itemView.getContext().startActivity(editIntent);
                                return true;

                            case R.id.report_post:

                                Intent reportIntent = new Intent(itemView.getContext(), ReportAbuseActivity.class);
                                reportIntent.putExtra(Constants.IDPOST, dataItem.getIdPosts());
                                reportIntent.putExtra(Constants.IDUSERNAME, dataItem.getIdUserName());
                                itemView.getContext().startActivity(reportIntent);
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
                                itemView.getContext().startActivity(Intent.createChooser(sharedIntentMaker(), "Choose an app"));


                                return true;

                            default:
                                return false;

                        }
                    }
                });
                popupMenu.show();




        }

        private Intent sharedIntentMaker(){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, dataItem.getTextStatus() + " " + "Voiceme");
            return shareIntent;
        }

        @Override
        protected void hugCounterClicked(View v) {
            processLoggedState(v);
//            if (processLoggedState(v))
 //               return;
            Intent intent = new Intent(v.getContext(), UserHugCounterActivity.class);
            intent.putExtra(Constants.HUG_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void sameCounterClicked(View v) {
            processLoggedState(v);
 //           if (processLoggedState(v))
  //              return;
            Intent intent = new Intent(v.getContext(), UserSameCounterActivity.class);
            intent.putExtra(Constants.SAME_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        protected void likeButtonMethod(View view) {
            likeCounter = Integer.parseInt(like_counter.getText().toString());

            if (likeButtonMain.isFavorite()){
                Toast.makeText(itemView.getContext(), "unLiked", Toast.LENGTH_SHORT).show();
                likeButtonMain.setFavorite(false);
                // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 1, 1, 1, "clicked unlike button");
                likeCounter--;
                like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
            } else {
                Toast.makeText(itemView.getContext(), "Liked", Toast.LENGTH_SHORT).show();
                likeButtonMain.setFavorite(true);
                likeCounter++;
                SharedPreferences preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);
                String userId = MySharedPreferences.getUserId(preferences);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName() + "_postId@" + dataItem.getIdPosts()  + "_click@" + "1";

                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    Toast.makeText(itemView.getContext(), "same user", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                }


                sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 0, 0, "clicked like button");



                like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
            }

        }

        protected void hugButtonMethod(View view) {
            hugCounter = Integer.parseInt(hug_counter.getText().toString());

            if (HugButtonMain.isFavorite()){
                Toast.makeText(itemView.getContext(), "unLiked", Toast.LENGTH_SHORT).show();
                HugButtonMain.setFavorite(false);
                hugCounter--;
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 1, 1, "clicked unlike button");
                hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
            } else {
                Toast.makeText(itemView.getContext(), "Liked", Toast.LENGTH_SHORT).show();
                HugButtonMain.setFavorite(true);
                hugCounter++;
                SharedPreferences preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);
                String userId = MySharedPreferences.getUserId(preferences);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName() + "_postId" + dataItem.getIdPosts()  + "_click" + "2";


                sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 1, 0, 0, "clicked hug button");
                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    Toast.makeText(itemView.getContext(), "same user", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                }
                hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
            }
        }

        protected void sameButtonMethod(View view) {
            sameCounter = Integer.parseInt(same_counter.getText().toString());
            if (SameButtonMain.isFavorite()){
                Toast.makeText(itemView.getContext(), "unLiked", Toast.LENGTH_SHORT).show();
                SameButtonMain.setFavorite(false);
                sameCounter--;
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 1, 0, 1, "clicked unlike button");
                same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
            } else {
                Toast.makeText(itemView.getContext(), "Liked", Toast.LENGTH_SHORT).show();
                SameButtonMain.setFavorite(true);
                sameCounter++;
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
                same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
            }
        }


  /*      @Override
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
                dataItem.getIdUserName() + "_postId@" + dataItem.getIdPosts()  + "_click@" + "1";

                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    Toast.makeText(itemView.getContext(), "same user", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                }


                sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 0, 0, "clicked like button");



            } else if (likeButton == HugButtonMain) {
                hugCounter++;
                hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
                SharedPreferences preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_WORLD_WRITEABLE);
                String userId = MySharedPreferences.getUserId(preferences);
                String sendLike = "senderid@" + userId + "_contactId@" +
                dataItem.getIdUserName() + "_postId" + dataItem.getIdPosts()  + "_click" + "2";


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
                likeCounter--;
                like_counter.setText(NumberFormat.getIntegerInstance().format(likeCounter));
                // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 1, 1, 1, "clicked unlike button");
            } else if (likeButton == HugButtonMain) {
                hugCounter--;
                hug_counter.setText(NumberFormat.getIntegerInstance().format(hugCounter));
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 1, 1, "clicked unlike button");
            } else if (likeButton == SameButtonMain) {
                sameCounter--;
                same_counter.setText(NumberFormat.getIntegerInstance().format(sameCounter));
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 1, 0, 1, "clicked unlike button");
            }
        }

*/

        @Override
        public boolean processLoggedState(View viewPrm) {
            if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
              //  l.a(666);
                if (!viewPrm.getClass().getCanonicalName().contains(("MaterialFavoriteButton")))
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
