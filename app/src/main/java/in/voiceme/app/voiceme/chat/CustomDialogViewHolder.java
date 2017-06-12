package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import in.voiceme.app.voiceme.DTO.ChatDialogPojo;
import in.voiceme.app.voiceme.R;

/*
 * Created by Anton Bevza on 1/18/17.
 */
public class CustomDialogViewHolder
        extends DialogsListAdapter.DialogViewHolder<ChatDialogPojo> {
    protected View onlineIndicator;


    public CustomDialogViewHolder(View itemView) {
        super(itemView);

        onlineIndicator = itemView.findViewById(com.stfalcon.chatkit.R.id.onlineIndicator);

    }

    @Override
    public void onBind(ChatDialogPojo dialog) {
        super.onBind(dialog);

        if (dialog != null){
            if (dialog.getUsers() != null){
                if (dialog.getUsers().get(0) != null){
                    if (dialog.getUsers().size() > 1) {
                        onlineIndicator.setVisibility(View.GONE);
                    } else {
                        boolean isOnline = dialog.getUsers().get(0).getIsOnline();
                        onlineIndicator.setVisibility(View.VISIBLE);
                        if (isOnline) {
                            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_online);
                        } else {
                            onlineIndicator.setBackgroundResource(R.drawable.shape_bubble_offline);
                        }
                    }
                }

            }
        }



    }
}
