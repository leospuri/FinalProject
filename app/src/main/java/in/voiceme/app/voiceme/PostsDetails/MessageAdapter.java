package in.voiceme.app.voiceme.PostsDetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.DTO.PostUserCommentModel;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.NotificationsPage.SimpleDividerItemDecoration;
import in.voiceme.app.voiceme.ProfilePage.ProfileActivity;
import in.voiceme.app.voiceme.ProfilePage.SecondProfile;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.Account;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.CurrentTime;
import rx.android.schedulers.AndroidSchedulers;

import static in.voiceme.app.voiceme.infrastructure.Constants.CONSTANT_PREF_FILE;

/**
 * Created by User on 07.12.2016.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private final static int MAXIMUM_VISIBLE_ITEM_COUNT = 4;

    private SharedPreferences preferences;
    private final Context mContext;
    private InsertMessageListener mInsertMessageListener;
    private List<PostUserCommentModel> mMessageList;
    private List<MessageViewHolder> mMessageHolderList = new ArrayList<>();
    private static SharedPreferences recyclerviewpreferences;
    protected final Bus bus;

    public MessageAdapter(Context context, List<PostUserCommentModel> mMessageList, InsertMessageListener insertMessageListener) {
        mContext = context;
        bus = ((VoicemeApplication)context.getApplicationContext()).getBus();
        bus.register(this);
        mInsertMessageListener = insertMessageListener;
        this.mMessageList = mMessageList;
        recyclerviewpreferences = ((VoicemeApplication) context.getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false);

        MessageViewHolder messageViewHolder = new MessageViewHolder(v);
        mMessageHolderList.add(messageViewHolder);

        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.onBind(position, mMessageList.get(position));
    }



    public void addMessage(PostUserCommentModel messageItem) {
        mMessageList.add(messageItem);

        int position = mMessageList.size() - 1;
        mInsertMessageListener.onMessageInserted(position);
        notifyItemInserted(position);
    }

    private MessageViewHolder getViewHolderByPosition(int position) {
        for (MessageViewHolder viewHolder : mMessageHolderList) {
            if (viewHolder.getBoundPosition() == position) {
                return viewHolder;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mMessageList == null){
            return 0;
        } else {
            return mMessageList.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        //   return (position == dataSet.size() - 1) ? VIEW_ITEM : VIEW_PROG;
        // current   return dataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        return mMessageList.get((mMessageList.size() - 1)) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public interface InsertMessageListener {
        void onMessageInserted(int position);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private static final float ALPHA_INVISIBLE = 0f;
        private static final float ALPHA_VISIBLE = 1f;
        private static final int ANIMATION_DELAY = 5000;
        private final Handler mDelayHandler = new Handler();
        private View mHolderView;
        private int mPosition;
        private PostUserCommentModel user_comment;

        private Animation mFadeOutAnimation;
        private boolean isAnimating = false;
        private boolean isVisible = true;

        private TextView username;
        private TextView comment_time;
        private TextView like_below_comment_reply;
        private TextView like_below_comment_counter;
        private ImageView like_below_comment_direct;
        private TextView messageCard;
        private ImageView commentMore;
        private SimpleDraweeView userImage;
        private PopupMenu popupMenu;
        private CommentReplyAdapter commentReplyAdapter;
        List<ReplyCommentPojo> mReplyMessageList;
        private RecyclerView replyRecyclerview;
        private LinearLayoutManager mReplyLinearLayoutManager;
        protected int comment_likes;
        protected boolean comment_likes_true = false;


        private Animation.AnimationListener mFadeOutAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHolderView.setAlpha(ALPHA_INVISIBLE);
                isAnimating = false;
                isVisible = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        public MessageViewHolder(final View itemView) {
            super(itemView);
            mHolderView = itemView;

            username = (TextView) itemView.findViewById(R.id.tv_user_name);
            comment_time = (TextView) itemView.findViewById(R.id.comment_time);
            like_below_comment_direct = (ImageView) itemView.findViewById(R.id.like_below_comment_direct);
            like_below_comment_counter = (TextView) itemView.findViewById(R.id.like_below_comment_counter);
            like_below_comment_reply = (TextView) itemView.findViewById(R.id.like_below_comment_reply);
            messageCard = (TextView) itemView.findViewById(R.id.tv_message_card);
            userImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_user_image);
            replyRecyclerview = (RecyclerView) itemView.findViewById(R.id.reply_comment_recyclerview);

            preferences = ((VoicemeApplication) itemView.getContext().getApplicationContext()).getSharedPreferences(CONSTANT_PREF_FILE, Context.MODE_PRIVATE);

            commentMore = (ImageView) itemView.findViewById(R.id.comment_more);
            mFadeOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_anim);
            mFadeOutAnimation.setAnimationListener(mFadeOutAnimationListener);
        }

        protected void commentMoreMethod(View view, int position) {
            popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.comment_more, popupMenu.getMenu());
            //    this.menu = popupMenu.getMenu();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.commentDelete:

                            try {
                                deleteChat(view, mMessageList.get(mPosition).getCommentId());
                                //      Toast.makeText(view.getContext(), "comment ID: " + mMessageList.get(position).getCommentId(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            remove(position);
                            notifyItemRemoved(position);

                            return true;

                        default:
                            return false;

                    }
                }
            });
            popupMenu.show();
        }

        private void remove(int user_comment) {
            mMessageList.remove(user_comment);
            notifyItemRemoved(user_comment);
        }

        public void onBind(int position, PostUserCommentModel messageItem) {

            mPosition = position;
            user_comment = messageItem;
            mHolderView.setAlpha(ALPHA_VISIBLE);
            isVisible = true;
            if (messageItem.getPost_comment_like_true()!= null){
                if (messageItem.getPost_comment_like_true().equals("true")){
                    comment_likes_true = true;
                } else {
                    comment_likes_true = false;
                }
            }


            comment_likes = messageItem.getComment_likes();

            like_below_comment_counter.setText(String.valueOf(comment_likes));

            String message = messageItem.getComment();
            String imageUri = messageItem.getAvatar();
            String userName = messageItem.getUserName();

            comment_time.setText(CurrentTime.getCurrentTime(messageItem.getCommentTime(), itemView.getContext()));
            commentReplyAdapter = new CommentReplyAdapter(itemView.getContext(), mMessageList, mPosition);

            mReplyLinearLayoutManager = new LinearLayoutManager(itemView.getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mReplyLinearLayoutManager.setStackFromEnd(true);
//        mLinearLayoutManager.setReverseLayout(true);

            replyRecyclerview.setLayoutManager(mReplyLinearLayoutManager);
            replyRecyclerview.addItemDecoration(new SimpleDividerItemDecoration(itemView.getContext()));
            replyRecyclerview.setAdapter(commentReplyAdapter);

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userProfile(view, messageItem);
                }
            });

            like_below_comment_counter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentlikeCounter(view, messageItem);
                }
            });

            like_below_comment_direct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentlikeCounter(view, messageItem);
                }
            });
            like_below_comment_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentReply(view, messageItem);
                }
            });


            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userProfile(view, messageItem);
                }
            });

            messageCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userProfile(view, messageItem);
                }
            });

            commentMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentMoreMethod(view, position);

                }
            });

            messageCard.setText(message);
            username.setText(userName);
            userImage.setImageURI(imageUri);

            if (messageItem.getCommentUserId() != null){
                if (messageItem.getCommentUserId().equals(MySharedPreferences.getUserId(preferences))||
                        messageItem.getId_post_user_name().equals(MySharedPreferences.getUserId(preferences))){
                    commentMore.setVisibility(View.VISIBLE);
                } else {
                    commentMore.setVisibility(View.INVISIBLE);

                }
            }

        }

        private void userProfile(View view, PostUserCommentModel messageItem) {
            if (messageItem.getCommentUserId().equals(MySharedPreferences.getUserId(recyclerviewpreferences))){
                view.getContext().startActivity(new Intent(view.getContext(), ProfileActivity.class));
            } else {
                Intent intent = new Intent(view.getContext(), SecondProfile.class);
                intent.putExtra(Constants.SECOND_PROFILE_ID, messageItem.getCommentUserId());
                view.getContext().startActivity(intent);
            }
        }

        private void CommentlikeCounter(View view, PostUserCommentModel messageItem) {


            if (comment_likes_true){
                comment_likes_true = false;
                comment_likes--;

                try {
                    postUnLike(messageItem.getCommentId(), messageItem.getPost_comment_id());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // unlike network call
            } else {
                comment_likes_true = true;
                try {
                    postLike(messageItem.getCommentId(), messageItem.getPost_comment_id());
                    postCommentReplyLikeNotification(messageItem.getId_post_user_name(), messageItem.getCommentUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                comment_likes++;
            }
            like_below_comment_counter.setText(String.valueOf(comment_likes));
         //   Toast.makeText(view.getContext(), "Counter clicked", Toast.LENGTH_SHORT).show();
        }

        private void CommentReply(View view, PostUserCommentModel messageItem) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            final View dialogView = inflater.inflate(R.layout.reply_comment_dialog, null);
            dialogBuilder.setView(dialogView);

            final EditText edt = (EditText) dialogView.findViewById(R.id.edit_reply_comment);

            dialogBuilder.setTitle("Reply to " + messageItem.getUserName());
            dialogBuilder.setMessage("Enter text below");
            dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //do something with edt.getText().toString();
                    commentReplyAdapter.addMessage(new ReplyCommentPojo(String.valueOf("@" + messageItem.getUserName() + " " + edt.getText().toString()),
                            MySharedPreferences.getImageUrl(preferences),
                            MySharedPreferences.getUsername(preferences), messageItem.getUserName(), String.valueOf(System.currentTimeMillis()/1000), 0, "1"));
                    try {
                        postComment(view, (String.valueOf("@" + messageItem.getUserName() + " " + edt.getText().toString())),messageItem.getCommentId(), messageItem.getCommentUserId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            AlertDialog b = dialogBuilder.create();
            b.show();
        }

        public int getBoundPosition() {
            return mPosition;
        }

        public boolean isAnimating() {
            return isAnimating;
        }

        public boolean isVisible() {
            return isVisible;
        }
    }

    private void postComment(View view, String message, String commentId, String idPostUsername) throws Exception {
        bus.post(new Account.sendCommentReply(commentId, idPostUsername, message));

    }

    private void postLike(String id_post_comment, String id_user_name) throws Exception {
        bus.post(new Account.sendCommentLike(id_post_comment, id_user_name, "1"));

    }

    private void postUnLike(String id_post_comment, String id_user_name) throws Exception {
        bus.post(new Account.sendCommentLike(id_post_comment, id_user_name, null));

    }

    private void postCommentReplyLikeNotification(String user_id, String post_id_user) throws Exception {
        bus.post(new Account.sendLikeUserId(user_id, post_id_user));

    }


    private void deleteChat(View view, String messageId) throws Exception {
        ((VoicemeApplication) view.getContext().getApplicationContext()).getWebService()
                .deleteComment(messageId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {

                        //   Toast.makeText(view.getContext(), "Comment was successfully deleted", Toast.LENGTH_LONG).show();
                        //          Toast.makeText(MessageActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                        //       String text = response.get(0).getText();
                        //    MessagePojo pojo = response.get(0).getMessage();
                        //messages = response;
                        //   Toast.makeText(MessageActivity.this, "deleted message", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }
}
