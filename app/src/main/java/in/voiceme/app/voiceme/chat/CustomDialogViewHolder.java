package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.models.DefaultDialog;
import in.voiceme.app.voiceme.chat.models.DefaultUser;

/**
 * Created by Anton Bevza on 1/18/17.
 */

public class CustomDialogViewHolder extends DialogsListAdapter.DialogViewHolder<DefaultDialog> {
    private View onlineView;

    public CustomDialogViewHolder(View itemView) {
        super(itemView);
        onlineView = itemView.findViewById(R.id.online);
    }

    @Override
    public void onBind(DefaultDialog dialog) {
        super.onBind(dialog);
            boolean isOnline = ((DefaultUser)dialog.getUsers().get(0)).isOnline();
            if (isOnline) {
                onlineView.setBackgroundResource(R.drawable.shape_bubble_online);
            } else {
                onlineView.setBackgroundResource(R.drawable.shape_bubble_offline);
            }

    }
}
