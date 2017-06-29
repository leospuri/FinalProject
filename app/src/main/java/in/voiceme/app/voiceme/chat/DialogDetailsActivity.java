package in.voiceme.app.voiceme.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;

import in.voiceme.app.voiceme.DTO.ChatDialogPojo;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.ProfilePage.SecondProfile;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DialogDetailsActivity extends DemoDialogsActivity {


    private List<ChatDialogPojo> messages = null;
    private Menu menu;

  //  private View progressFrame;
    private View progressFrame;
    private Button error_btn_retry;
    private DialogsList dialogsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_details);
        getSupportActionBar().setTitle("Chat Messages");
        setNavDrawer(new MainNavDrawer(this));

        progressFrame = findViewById(R.id.dialog_details);
        error_btn_retry = (Button) findViewById(R.id.error_btn_retry);

        dialogsListView = (DialogsList) findViewById(R.id.dialogsList);
        dialogInit();

     //   dialogsListAdapter = new DialogsListAdapter<>(imageLoader);

        if (MySharedPreferences.getUserId(preferences) == null){
            Toast.makeText(this, "You are not logged In", Toast.LENGTH_SHORT).show();
            progressFrame.setVisibility(View.GONE);
        } else {
            try {
                chatMessages();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    chatMessages();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void dialogInit() {


        super.dialogsListAdapter = new DialogsListAdapter<>(
                super.imageLoader);

        super.dialogsListAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<ChatDialogPojo>() {
            @Override
            public void onDialogClick(ChatDialogPojo dialog) {
                // Todo add methods to get user ID of the other user, add own ID
                //   startActivity(new Intent(DialogDetailsActivity.this, MessageActivity.class));

                Intent intent = new Intent(DialogDetailsActivity.this, MessageActivity.class);
                intent.putExtra(Constants.YES, dialog.getId());
                intent.putExtra(Constants.USERNAME, dialog.getDialogName());
                startActivity(intent);

            }
        });

        super.dialogsListAdapter.setOnUsernameClicked(new DialogsListAdapter.OnClickUsername<ChatDialogPojo>() {
            @Override
            public void onClick(ChatDialogPojo dialog) {
                Intent intent = new Intent(DialogDetailsActivity.this, SecondProfile.class);
                intent.putExtra(Constants.SECOND_PROFILE_ID, dialog.getId());
                startActivity(intent);
            }
        });

        super.dialogsListAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<ChatDialogPojo>() {
            @Override
            public void onDialogLongClick(ChatDialogPojo dialog) {
                //          Toast.makeText(DialogDetailsActivity.this, dialog.getDialogName(),
                //                  Toast.LENGTH_SHORT).show();
                // Todo Delete the entire chat
                // Working delete query
              //  dialogsListAdapter.deleteById(dialog.getId());
            }
        });

        dialogsListView.setAdapter(dialogsListAdapter);
    }


    /* Menu for deleting
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.chat_dialog_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_dialog:

                //
                break;
        }
        return true;
    }
   */

    private void onNewMessage(String dialogId, IMessage message) {
        if (!super.dialogsListAdapter.updateDialogWithMessage(dialogId, message)) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or update all dialogs list
        }
    }

    private void onNewDialog(ChatDialogPojo dialog) {
        // Todo add network calls to add new dialog
        super.dialogsListAdapter.addItem(dialog);
    }

    private void chatMessages() throws Exception {
        application.getWebService()
                .getAllChatMessages(MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<List<ChatDialogPojo>>() {
                    @Override
                    public void onNext(List<ChatDialogPojo> response) {
                        //     Toast.makeText(DialogDetailsActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                        //    MessagePojo pojo = response.get(0).getMessage();
                        messages = response;
                        dialogsListAdapter.setItems(response);

                        progressFrame.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        progressFrame.setVisibility(View.GONE);
                    }

                });
    }



    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent myActivityIntent = new Intent(DialogDetailsActivity.this, DiscoverActivity.class);
        startActivity(myActivityIntent);
        finish();
    }


}
