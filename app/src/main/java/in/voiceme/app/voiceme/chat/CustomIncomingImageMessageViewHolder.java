package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessageHolders;

import in.voiceme.app.voiceme.DTO.MessagePojo;
import in.voiceme.app.voiceme.R;

/*
 * Created by troy379 on 05.04.17.
 */
public class CustomIncomingImageMessageViewHolder
        extends MessageHolders.IncomingImageMessageViewHolder<MessagePojo> {

    private View onlineIndicator;

    public CustomIncomingImageMessageViewHolder(View itemView) {
        super(itemView);
        onlineIndicator = itemView.findViewById(R.id.onlineIndicator);
    }

    @Override
    public void onBind(MessagePojo message) {
        super.onBind(message);

        boolean isOnline = message.getUser().isOnline();
        if (isOnline) {
            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online);
        } else {
            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline);
        }
    }
}