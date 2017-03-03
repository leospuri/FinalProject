package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.messages.MessagesListAdapter;

import in.voiceme.app.voiceme.chat.models.MessagePojo;

public class CustomOutcomingMessageViewHolder
        extends MessagesListAdapter.OutcomingMessageViewHolder<MessagePojo> {

    public CustomOutcomingMessageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(MessagePojo message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
    }
}
