package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.models.ChatDialogPojo;

/**
 * Created by Anton Bevza on 1/18/17.
 */

public class CustomDialogViewHolder extends DialogsListAdapter.DialogViewHolder<ChatDialogPojo> {
    private View onlineView;

    public CustomDialogViewHolder(View itemView) {
        super(itemView);
        onlineView = itemView.findViewById(R.id.online);
    }

    @Override
    public void onBind(ChatDialogPojo dialog) {
        super.onBind(dialog);

    }
}
