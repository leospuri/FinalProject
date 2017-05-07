package in.voiceme.app.voiceme.NotificationsPage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import in.voiceme.app.voiceme.DTO.PostUserListModel;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.utils.CurrentTime;
import in.voiceme.app.voiceme.utils.CurrentTimeLong;

/**
 * Created by harishpc on 5/5/2017.
 */
public class NotificationCardViewHolder extends RecyclerView.ViewHolder {
    protected NotificationPojo dataItem;
    TextView personName;
    RelativeLayout notification_background;
    TextView notificationFeeling;
    TextView notificationTime;
    TextView notification_view_post;
    TextView notification_feeling_status;
    SimpleDraweeView personPhoto;

    public NotificationCardViewHolder(View itemView) {
        super(itemView);
        personName = (TextView) itemView.findViewById(R.id.notification_user);
        notificationFeeling = (TextView) itemView.findViewById(R.id.notification_feeling_text);
        notification_background = (RelativeLayout) itemView.findViewById(R.id.notification_background);
        notificationTime = (TextView) itemView.findViewById(R.id.notification_post_time);
        notification_feeling_status = (TextView) itemView.findViewById(R.id.notification_feeling_status);
        notification_view_post = (TextView) itemView.findViewById(R.id.notification_view_post);
        personPhoto = (SimpleDraweeView) itemView.findViewById(R.id.notification_avatar);

        personName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameClicked(view);
            }
        });

        notificationFeeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationBackClick(view);
            }
        });

        notificationTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationBackClick(view);
            }
        });

        notification_view_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationBackClick(view);
            }
        });

        notification_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationBackClick(view);
            }
        });

        personPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfileClicked(view);
            }
        });

        notification_feeling_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationBackClick(view);
            }
        });

    }

    protected void notificationBackClick(View view) {
    }

    protected void userNameClicked(View view) {

    }

    protected void userProfileClicked(View view) {

    }
    public void bind(NotificationPojo dataItem) {
        this.dataItem = dataItem;

        personName.setText(dataItem.getUsername());
        // Todo Write the logic to get activity as per the feeling name
        notificationFeeling.setText(String.valueOf(" just " + getEmotionValue(dataItem) + " your post: "));
        notificationTime.setText(CurrentTimeLong.getCurrentTime(dataItem.getTime().trim(), itemView.getContext()));
        notification_feeling_status.setText(dataItem.getTextStatus().trim());
        personPhoto.setImageURI(dataItem.getAvatarPic());
    }

    private String getEmotionValue(NotificationPojo dataItem){
        String emotion = null;
        if(dataItem.getActivity().equals("1")){
            emotion = "liked";
        } else if(dataItem.getActivity().equals("2")){
            emotion = "hugged";
        } else if (dataItem.getActivity().equals("3")){
            emotion = "felt sad for";
        } else if(dataItem.getActivity().equals("5")){
            emotion = "commented on";
        }

        return emotion;
    }
}
