package in.voiceme.app.voiceme.PostsDetails;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.DTO.PostUserCommentModel;
import in.voiceme.app.voiceme.R;

/**
 * Created by User on 07.12.2016.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final static int MAXIMUM_VISIBLE_ITEM_COUNT = 4;

    private final Context mContext;
    private InsertMessageListener mInsertMessageListener;
    private List<PostUserCommentModel> mMessageList;
    private List<MessageViewHolder> mMessageHolderList = new ArrayList<>();

    public MessageAdapter(Context context, List<PostUserCommentModel> mMessageList, InsertMessageListener insertMessageListener) {
        mContext = context;
        mInsertMessageListener = insertMessageListener;
        this.mMessageList = mMessageList;
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

    @Override
    public int getItemCount() {
        if (mMessageList == null){
            return 0;
        } else {
            return mMessageList.size();
        }

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

        private Animation mFadeOutAnimation;
        private boolean isAnimating = false;
        private boolean isVisible = true;

        private TextView username;
        private TextView messageCard;
        private SimpleDraweeView userImage;

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

            username = (TextView) itemView.findViewById(R.id.tv_user_name);
            messageCard = (TextView) itemView.findViewById(R.id.tv_message_card);
            userImage = (SimpleDraweeView) itemView.findViewById(R.id.iv_user_image);


            mHolderView = itemView;

            mFadeOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_anim);
            mFadeOutAnimation.setAnimationListener(mFadeOutAnimationListener);
        }

        public void onBind(int position, PostUserCommentModel messageItem) {
            mPosition = position;
            mHolderView.setAlpha(ALPHA_VISIBLE);
            isVisible = true;

            String message = messageItem.getComment();
            String imageUri = messageItem.getAvatar();
            String userName = messageItem.getUserName();

            messageCard.setText(message);
            username.setText(userName);
            userImage.setImageURI(imageUri);
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
}
