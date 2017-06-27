package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessageHolders;

import in.voiceme.app.voiceme.DTO.MessagePojo;

/*
 * Created by troy379 on 05.04.17.
 */
public class CustomOutcomingImageMessageViewHolder
        extends MessageHolders.OutcomingImageMessageViewHolder<MessagePojo> {

    public CustomOutcomingImageMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(MessagePojo message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
    }
}