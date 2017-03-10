package in.voiceme.app.voiceme.chat;

import android.content.Intent;
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
import in.voiceme.app.voiceme.chat.models.ChatDialogPojo;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class DialogDetailsActivity extends BaseActivity {

    private DialogsListAdapter<ChatDialogPojo> dialogsListAdapter;
    private List<ChatDialogPojo> messages;

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
             //   Picasso.with(DialogDetailsActivity.this).load(url).placeholder(getResources().getDrawable(R.drawable.user)).error(getResources().getDrawable(R.drawable.user)).into(imageView);

                if (imageView.equals(null)|| url.isEmpty()){
                    Picasso.with(DialogDetailsActivity.this).load(R.drawable.user).into(imageView);
                } else {
                    Picasso.with(DialogDetailsActivity.this).load(url).into(imageView);
                }

            }
        };

        dialogsListAdapter = new DialogsListAdapter<>(R.layout.item_dialog_custom_view_holder,
                CustomDialogViewHolder.class, imageLoader);

        try {
            chatMessages();
            dialogInit(dialogsListView);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected void getChat() {
        application.getWebService()
                .getResponse("senderid@2_contactId@1_chat@yes", "after current message")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Timber.d("Message from server" + response);
                    }
                });


    }

    private void dialogInit(DialogsList dialogsListView) {
        dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<ChatDialogPojo>() {
            @Override
            public void onDialogClick(ChatDialogPojo dialog) {
                // Todo add methods to get user ID of the other user, add own ID
                Toast.makeText(DialogDetailsActivity.this, "Dialog Clicked", Toast.LENGTH_SHORT).show();
             //   startActivity(new Intent(DialogDetailsActivity.this, MessageActivity.class));

                Intent intent = new Intent(DialogDetailsActivity.this, MessageActivity.class);
                intent.putExtra(Constants.YES, /* messages.get(0).getId()*/ "2");
                startActivity(intent);

                getChat();
            }
        });

        dialogsListAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<ChatDialogPojo>() {
            @Override
            public void onDialogLongClick(ChatDialogPojo dialog) {
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

    private void onNewDialog(ChatDialogPojo dialog) {
        // Todo add network calls to add new dialog
        dialogsListAdapter.addItem(dialog);
    }

    private void chatMessages() throws Exception {
        application.getWebService()
                .getAllChatMessages("1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<ChatDialogPojo>>() {
                    @Override
                    public void onNext(List<ChatDialogPojo> response) {
                        Toast.makeText(DialogDetailsActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                        //    MessagePojo pojo = response.get(0).getMessage();
                        messages = response;
                        dialogsListAdapter.setItems(response);
                    }
                });
    }


}