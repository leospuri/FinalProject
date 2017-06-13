package in.voiceme.app.voiceme.chat;

import android.view.View;

import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import in.voiceme.app.voiceme.DTO.ChatDialogPojo;
import in.voiceme.app.voiceme.R;
import timber.log.Timber;

/*
 * Created by Anton Bevza on 1/18/17.
 */
public class CustomDialogViewHolder
        extends DialogsListAdapter.DialogViewHolder<ChatDialogPojo> {
    protected View onlineIndicator;


    public CustomDialogViewHolder(View itemView) {
        super(itemView);

        onlineIndicator = itemView.findViewById(R.id.onlineIndicator);

    }

    @Override
    public void onBind(ChatDialogPojo dialog) {
        super.onBind(dialog);

                if (dialog.getUser() != null){
                    if (dialog.getUsers().size() > 1) {
                        onlineIndicator.setVisibility(View.GONE);
                    } else {
                        boolean isOnline = dialog.getUser().isOnline();
               //         Timber.e(String.valueOf("First Boolean Value" + dialog.getUsers().get(0).isOnline()));
                        Timber.e(String.valueOf("Second Boolean Value" + dialog.getUser().isOnline()));

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
