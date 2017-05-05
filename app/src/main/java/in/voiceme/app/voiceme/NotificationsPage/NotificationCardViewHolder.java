package in.voiceme.app.voiceme.NotificationsPage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import in.voiceme.app.voiceme.DTO.PostUserListModel;
import in.voiceme.app.voiceme.R;

/**
 * Created by harishpc on 5/5/2017.
 */
public class NotificationCardViewHolder extends RecyclerView.ViewHolder {
    protected NotificationPojo dataItem;
    TextView personName;
    RelativeLayout notification_background;
    TextView notificationFeeling;
    TextView notificationTime;
    TextView notificationPostText;
    SimpleDraweeView personPhoto;

    public NotificationCardViewHolder(View itemView) {
        super(itemView);
        personName = (TextView) itemView.findViewById(R.id.notification_user);
        notificationFeeling = (TextView) itemView.findViewById(R.id.notification_feeling_text);
        notification_background = (RelativeLayout) itemView.findViewById(R.id.notification_background);
        notificationTime = (TextView) itemView.findViewById(R.id.notification_post_time);
        notificationPostText = (TextView) itemView.findViewById(R.id.notification_post_text);
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

        notificationPostText.setOnClickListener(new View.OnClickListener() {
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
        notificationFeeling.setText(dataItem.getActivity());
        notificationTime.setText(dataItem.getTime());
        notificationPostText.setText(dataItem.getTextStatus());

        if (dataItem.getAvatarPic().equals("") || dataItem.getAvatarPic() == null){
            personPhoto.setImageDrawable(itemView.getResources().getDrawable(R.drawable.user));
        } else {
            dataItem.getAvatarPic();
        }
    }
}
