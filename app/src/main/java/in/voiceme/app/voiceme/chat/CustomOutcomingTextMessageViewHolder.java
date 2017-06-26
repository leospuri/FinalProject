package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessageHolders;

import in.voiceme.app.voiceme.DTO.MessagePojo;

public class CustomOutcomingTextMessageViewHolder
        extends MessageHolders.OutcomingTextMessageViewHolder<MessagePojo> {

    public CustomOutcomingTextMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(MessagePojo message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
    }
}
