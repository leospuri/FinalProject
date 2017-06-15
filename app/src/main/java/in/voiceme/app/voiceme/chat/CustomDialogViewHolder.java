package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import in.voiceme.app.voiceme.DTO.ChatDialogPojo;

/*
 * Created by Anton Bevza on 1/18/17.
 */
public class CustomDialogViewHolder
        extends DialogsListAdapter.DialogViewHolder<ChatDialogPojo> {


    public CustomDialogViewHolder(View itemView) {
        super(itemView);

    }

    @Override
    public void onBind(ChatDialogPojo dialog) {
        super.onBind(dialog);

    }
}
