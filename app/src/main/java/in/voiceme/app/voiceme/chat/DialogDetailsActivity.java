package in.voiceme.app.voiceme.chat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.fixtures.DialogsListFixtures;
import in.voiceme.app.voiceme.chat.models.DefaultDialog;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;

public class DialogDetailsActivity extends BaseActivity {

    private DialogsListAdapter<DefaultDialog> dialogsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_details);
        getSupportActionBar().setTitle("Chat Messages");
        setNavDrawer(new MainNavDrawer(this));



        DialogsList dialogsListView = (DialogsList) findViewById(R.id.dialogsList);

        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //If you using another library - write here your way to load image
                Picasso.with(DialogDetailsActivity.this).load(url).into(imageView);
            }
        };

        dialogsListAdapter = new DialogsListAdapter<>(R.layout.item_dialog_custom_view_holder,
                CustomDialogViewHolder.class, imageLoader);
        dialogsListAdapter.setItems(getDialogs());

        dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<DefaultDialog>() {
            @Override
            public void onDialogClick(DefaultDialog dialog) {
                // Todo add methods to get user ID of the other user, add own ID
                Toast.makeText(DialogDetailsActivity.this, "Dialog Clicked", Toast.LENGTH_SHORT).show();
              //  startActivity(new Intent(DialogDetailsActivity.this, MessagesListActivity.class));
            }
        });

        dialogsListAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<DefaultDialog>() {
            @Override
            public void onDialogLongClick(DefaultDialog dialog) {
                Toast.makeText(DialogDetailsActivity.this, dialog.getDialogName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        dialogsListView.setAdapter(dialogsListAdapter);
    }

    private void onNewMessage(String dialogId, IMessage message) {
        if (!dialogsListAdapter.updateDialogWithMessage(dialogId, message)) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or update all dialogs list
        }
    }

    private void onNewDialog(DefaultDialog dialog) {
        // Todo add network calls to add new dialog
        dialogsListAdapter.addItem(dialog);
    }

    private List<DefaultDialog> getDialogs() {
        // Todo Network call will return all the dialogs
        // ArrayList<DefaultDialog> getChatList()
        // DefaultDialog

        return DialogsListFixtures.getChatList();
    }
}
