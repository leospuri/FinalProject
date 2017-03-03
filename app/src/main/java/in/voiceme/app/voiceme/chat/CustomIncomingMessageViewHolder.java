package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessagesListAdapter;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.models.MessagePojo;
import in.voiceme.app.voiceme.chat.models.UserPojo;

public class CustomIncomingMessageViewHolder
        extends MessagesListAdapter.IncomingMessageViewHolder<MessagePojo> {
    private View onlineView;

    public CustomIncomingMessageViewHolder(View itemView) {
        super(itemView);
        onlineView = itemView.findViewById(R.id.online);
    }

    @Override
    public void onBind(MessagePojo message) {
        super.onBind(message);

        boolean isOnline = Boolean.parseBoolean(((UserPojo) message.getUser()).getIsOnline());
        if (isOnline) {
            onlineView.setBackgroundResource(R.drawable.shape_bubble_online);
        } else {
            onlineView.setBackgroundResource(R.drawable.shape_bubble_offline);
        }
    }
}
