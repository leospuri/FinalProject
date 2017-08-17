package in.voiceme.app.voiceme.ProfilePage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.DiscoverPage.LikeUnlikeClickListener;
import in.voiceme.app.voiceme.DiscoverPage.PostsCardViewHolder;
import in.voiceme.app.voiceme.PostsDetails.PostsDetailsActivity;
import in.voiceme.app.voiceme.PostsDetails.UserCategoryActivity;
import in.voiceme.app.voiceme.PostsDetails.UserFeelingActivity;
import in.voiceme.app.voiceme.PostsDetails.UserHugCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserLikeCounterActivity;
import in.voiceme.app.voiceme.PostsDetails.UserSameCounterActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.WasLoggedInInterface;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.userpost.EditPost;
import in.voiceme.app.voiceme.userpost.ReportAbuseActivity;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import in.voiceme.app.voiceme.utils.PaginationAdapterCallback;
import timber.log.Timber;

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
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String errorMsg;
    private PaginationAdapterCallback mCallback;
    private static SharedPreferences totalpreference;

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
      //  dataSet.getItemViewType(position);


       return dataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
       // return 0;
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
        return dataSet == null ? 0 : dataSet.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class EventViewHolder extends PostsCardViewHolder implements  View.OnClickListener, WasLoggedInInterface { //MaterialFavoriteButton.OnFavoriteChangeListener,

        boolean isPlaying = false;
        private boolean doDislike;
        private PopupMenu popupMenu;
        private Menu menu;
        private Bitmap bmp;

        public EventViewHolder(View itemView) {
            super(itemView);
            totalpreference = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
        }

        @Override
        public void onClick(View view) {
            processLoggedState(view);
            try {
                if (myClickListener != null) {
                    myClickListener.onItemClick(dataItem, view);
                } else {
                }
            } catch (NullPointerException e) {
            }
        }


        protected void secondUserProfileClicked(View view){
            if (dataItem.getIdUserName() != null){
                if (dataItem.getIdUserName().equals(MySharedPreferences.getUserId(totalpreference))) {
                    view.getContext().startActivity(new Intent(view.getContext(), ProfileActivity.class));
                } else {
                    Intent intent = new Intent(view.getContext(), SecondProfile.class);
                    intent.putExtra(Constants.SECOND_PROFILE_ID, dataItem.getIdUserName());
                    view.getContext().startActivity(intent);
                }
            } else {
                Toast.makeText(itemView.getContext(), "User doesn't exists", Toast.LENGTH_SHORT).show();
            }


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


        /*
        @Override
        protected void listenCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserListenCounterActivity.class);
            intent.putExtra(Constants.LISTEN_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }
        */

        @Override
        protected void categoryClicked(View v) {
            Intent intent = new Intent(v.getContext(), UserCategoryActivity.class);
            intent.putExtra(Constants.CATEGORY, dataItem.getIdCategory());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void cardBackground(View view) {
            if (processLoggedState(view))
                return;
            Intent intent = new Intent(view.getContext(), PostsDetailsActivity.class);
            intent.putExtra(Constants.POST_BACKGROUND, dataItem.getIdPosts());
            intent.putExtra(Constants.IDUSERNAME, dataItem.getIdUserName());
            view.getContext().startActivity(intent);
        }

        @Override
        protected void feelingClicked(View v) {
            processLoggedState(v);
            // add feeling ID to get feeling Posts from current pojo

            Intent intent = new Intent(v.getContext(), UserFeelingActivity.class);
            intent.putExtra(Constants.EMOTION, dataItem.getIdFeeling());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void likeCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserLikeCounterActivity.class);
            intent.putExtra(Constants.LIKE_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        protected void moreClick(View view){
            popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.pop_menu, popupMenu.getMenu());
            //    this.menu = popupMenu.getMenu();

            bmp = drawTextToBitmap(view.getContext(), dataItem.getTextStatus());

            SharedPreferences preferences;
            preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);

            if (MySharedPreferences.getUserId(preferences) == null){
                Toast.makeText(itemView.getContext(), "You are not logged In", Toast.LENGTH_SHORT).show();
            } else {
                if (MySharedPreferences.getUserId(preferences).equals(dataItem.getIdUserName())){
                    if(popupMenu.getMenu() == null)
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
                        switch (item.getItemId()){
                            case R.id.edit_post:

                                Intent editIntent = new Intent(itemView.getContext(), EditPost.class);
                                editIntent.putExtra(Constants.IDPOST, dataItem.getIdPosts());
                                editIntent.putExtra(Constants.STATUS_POST, dataItem.getTextStatus());
                                if (dataItem.getAudioFileLink().isEmpty() || dataItem.getAudioFileLink() == null){
                                    Timber.e("No audio attached");
                                } else {
                                    editIntent.putExtra(Constants.AUDIO, dataItem.getAudioFileLink());
                                }

                                itemView.getContext().startActivity(editIntent);
                                return true;

                            case R.id.report_post:

                                Intent reportIntent = new Intent(itemView.getContext(), ReportAbuseActivity.class);
                                reportIntent.putExtra(Constants.IDPOST, dataItem.getIdPosts());
                                reportIntent.putExtra(Constants.IDUSERNAME, dataItem.getIdUserName());
                                reportIntent.putExtra(Constants.STATUS_POST, dataItem.getTextStatus());
                                itemView.getContext().startActivity(reportIntent);
                                return true;

                            case R.id.make_popular:

                                removePopularPost((VoicemeApplication) itemView.getContext().getApplicationContext(), dataItem.getIdPosts());

                                return true;

                            case R.id.remove_make_popular:

                                sendPopularPost((VoicemeApplication) itemView.getContext().getApplicationContext(), dataItem.getIdPosts(), dataItem.getTextStatus());

                                return true;

                            case R.id.menu_item_share:

                                if (ActivityUtils.deleteAudioFile(itemView.getContext())){
                                    shareImage();
                                }

                               // itemView.getContext().startActivity(Intent.createChooser(sharedIntentMaker(), "Choose an app"));

                                return true;

                            default:
                                return false;

                        }
                    }
                });
                popupMenu.show();
            }

        }

        private void shareImage() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());
            LayoutInflater inflater = (LayoutInflater) itemView.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View dialogView = inflater.inflate(R.layout.custom_dialog_share_image, null);
            dialogBuilder.setView(dialogView);

            AlertDialog b = dialogBuilder.create();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((WindowManager)itemView.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
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
                    createDirectoryAndSaveFile(itemView.getContext(), bmp, "HelloAndroid", "com.facebook.katana");
                    b.cancel();
                }
            });

            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDirectoryAndSaveFile(itemView.getContext(),bmp, "HelloAndroid", "com.twitter.android");
                    b.cancel();
                }
            });

            linkIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDirectoryAndSaveFile(itemView.getContext(),bmp, "HelloAndroid", "com.linkedin.android");
                    b.cancel();
                }
            });

            Instagram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDirectoryAndSaveFile(itemView.getContext(),bmp, "HelloAndroid", "com.instagram.android");
                    b.cancel();
                }
            });

            pinist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDirectoryAndSaveFile(itemView.getContext(),bmp, "HelloAndroid", "com.pinterest");
                    b.cancel();
                }
            });
            tumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDirectoryAndSaveFile(itemView.getContext(),bmp, "HelloAndroid", "com.tumblr");
                    b.cancel();
                }
            });
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(Intent.createChooser(sharedIntentMaker(), "Choose an app"));

                    b.cancel();
                }
            });
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

        private void createDirectoryAndSaveFile(Context context, Bitmap imageToSave, String fileName, String PackageApp) {

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
                context.startActivity(Intent.createChooser(share, "Share image File"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private Intent sharedIntentMaker() {
            String sharebody = String.valueOf(dataItem.getUserNicName() + " " + "said:"
                    + " " + dataItem.getTextStatus() + " " + "inside Voiceme Android App. " +
                    "You can download from www.beacandid.com/candid?Post=" + dataItem.getIdPosts() + "");
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharebody);
            return shareIntent;
        }

        @Override
        protected void hugCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserHugCounterActivity.class);
            intent.putExtra(Constants.HUG_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }

        @Override
        protected void sameCounterClicked(View v) {
            if (processLoggedState(v))
                return;
            Intent intent = new Intent(v.getContext(), UserSameCounterActivity.class);
            intent.putExtra(Constants.SAME_FEELING, dataItem.getIdPosts());
            v.getContext().startActivity(intent);
        }



        protected void likeButtonMethod(View view) {
        //    likeCounter = Integer.parseInt(like_counter.getText().toString());

            if (like_button_true){
                like_button_true = false;
                unLikeMethod();
            } else {
                like_button_true = true;
                if (hug_button_true){
                    hug_button_true = false;
                    unHugMethod();
                }
                if (sad_button_true){
                    sad_button_true = false;
                    unSameMethod();
                }
                likeCounter++;
                String userId = MySharedPreferences.getUserId(totalpreference);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName() + "_postId@" + dataItem.getIdPosts()  + "_click@" + "1";


                if (MySharedPreferences.getUserId(totalpreference) == null){
                    Toast.makeText(itemView.getContext(), "You are not logged In", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 0, 0, "clicked like button");

                    if (MySharedPreferences.getUserId(totalpreference).equals(dataItem.getIdUserName())){
                        Timber.e("same user");
                    } else {
                        sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                    }
                }

                new_counter_like_number.setText(String.valueOf(NumberFormat.getIntegerInstance().format(likeCounter)));
                like_counter.setText(String.valueOf("LIKES"));
                like_counter.setBackgroundColor(itemView.getResources().getColor(R.color.md_blue_300));
                like_counter.setTextColor(itemView.getResources().getColor(R.color.white));
            }

            // if (favorite){

            //     }
        }

        private void unLikeMethod() {
            if (MySharedPreferences.getUserId(totalpreference) == null){
                Toast.makeText(itemView.getContext(), "You are not logged In", Toast.LENGTH_SHORT).show();
            } else {
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 1, 1, 1, "clicked unlike button");
            }
            // sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext());
            likeCounter--;

            new_counter_like_number.setText(String.valueOf(NumberFormat.getIntegerInstance().format(likeCounter)));
            like_counter.setText(String.valueOf("LIKES"));
            like_counter.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            like_counter.setTextColor(itemView.getResources().getColor(R.color.black));
        }



        protected void hugButtonMethod(View view) {
       //     hugCounter = Integer.parseInt(hug_counter.getText().toString());

            if (hug_button_true){
                hug_button_true = false;
                unHugMethod();
            } else {
                hug_button_true = true;
                if (like_button_true){
                    like_button_true = false;
                    unLikeMethod();
                }
                if (sad_button_true){
                    sad_button_true = false;
                    unSameMethod();
                }

                hugCounter++;
                String userId = MySharedPreferences.getUserId(totalpreference);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName() + "_postId" + dataItem.getIdPosts()  + "_click" + "2";


                if (MySharedPreferences.getUserId(totalpreference) == null){
                    Toast.makeText(itemView.getContext(), "You are not logged In", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 1, 0, 0, "clicked hug button");
                    if (MySharedPreferences.getUserId(totalpreference).equals(dataItem.getIdUserName())){
                        Timber.e("same user");
                    } else {
                        sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                    }
                }

                new_counter_hug_number.setText(String.valueOf(NumberFormat.getIntegerInstance().format(hugCounter)));
                hug_counter.setText(String.valueOf("HUGS"));
                hug_counter.setBackgroundColor(itemView.getResources().getColor(R.color.md_blue_300));
                hug_counter.setTextColor(itemView.getResources().getColor(R.color.white));
            }
        }

        private void unHugMethod() {
            hugCounter--;

            if (MySharedPreferences.getUserId(totalpreference) == null){
                Toast.makeText(itemView.getContext(), "You are not logged In", Toast.LENGTH_SHORT).show();
            } else {
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 0, 1, 1, "clicked unlike button");
            }
            new_counter_hug_number.setText(String.valueOf(NumberFormat.getIntegerInstance().format(hugCounter)));
            hug_counter.setText(String.valueOf("HUGS"));
            hug_counter.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            hug_counter.setTextColor(itemView.getResources().getColor(R.color.black));
        }



        protected void sameButtonMethod(View view) {
         //   sameCounter = Integer.parseInt(same_counter.getText().toString());
            if (sad_button_true){
                sad_button_true = false;
                unSameMethod();
            } else {
                sad_button_true = true;
                if (like_button_true){
                    like_button_true = false;
                    unLikeMethod();

                }
                if (hug_button_true){
                    hug_button_true = false;
                    unHugMethod();
                }
                sameCounter++;
                String userId = MySharedPreferences.getUserId(totalpreference);
                String sendLike = "senderid@" + userId + "_contactId@" +
                        dataItem.getIdUserName() + "_postId@" + dataItem.getIdPosts()  + "_click@" + "3";

                if (MySharedPreferences.getUserId(totalpreference) == null){
                    Toast.makeText(itemView.getContext(), "You are not logged In", Toast.LENGTH_SHORT).show();
                } else {
                    sendLikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 0, 0, 1, 0, "clicked same button");
                    if (MySharedPreferences.getUserId(totalpreference).equals(dataItem.getIdUserName())){
                        Timber.e("same user");
                    } else {
                        sendLikeNotification((VoicemeApplication) itemView.getContext().getApplicationContext(), sendLike);
                    }
                }

                new_counter_same_number.setText(String.valueOf(NumberFormat.getIntegerInstance().format(sameCounter)));
                same_counter.setText(String.valueOf("SAD"));
                same_counter.setBackgroundColor(itemView.getResources().getColor(R.color.md_blue_300));
                same_counter.setTextColor(itemView.getResources().getColor(R.color.white));
            }
        }

        private void unSameMethod() {
            sameCounter--;

            if (MySharedPreferences.getUserId(totalpreference) == null){
                Toast.makeText(itemView.getContext(), "You are not logged In", Toast.LENGTH_SHORT).show();
            } else {
                sendUnlikeToServer((VoicemeApplication) itemView.getContext().getApplicationContext(), 1, 1, 0, 1, "clicked unlike button");
            }
            new_counter_same_number.setText(String.valueOf(NumberFormat.getIntegerInstance().format(sameCounter)));
            same_counter.setText(String.valueOf("SAD"));
            same_counter.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            same_counter.setTextColor(itemView.getResources().getColor(R.color.black));
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
