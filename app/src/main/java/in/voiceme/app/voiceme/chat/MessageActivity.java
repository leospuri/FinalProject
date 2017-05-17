package in.voiceme.app.voiceme.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.emoji.Emoji;
import com.vanniktech.emoji.listeners.OnEmojiBackspaceClickListener;
import com.vanniktech.emoji.listeners.OnEmojiClickedListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupDismissListener;
import com.vanniktech.emoji.listeners.OnEmojiPopupShownListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardCloseListener;
import com.vanniktech.emoji.listeners.OnSoftKeyboardOpenListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.DTO.MessagePojo;
import in.voiceme.app.voiceme.DTO.UserPojo;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MessageActivity extends BaseActivity implements MessagesListAdapter.SelectionListener {

    private EmojiPopup emojiPopup;
    private MessagesList messagesList = null;
    private ViewGroup rootView;
    public MessagesListAdapter<MessagePojo> adapter;
    public ArrayList<MessagePojo> messageArray;
    // private MessageInput input;
    private View progressFrame;
    //   private List<MessageActivity.Message> chatMessages;
    private int selectionCount;
    private EmojiEditText editText;
    private ImageButton emojiButton;
    private ImageButton sendButton;
    private LinearLayout no_post_layout;
    private TextView no_post_textview;
    private int messageCount;


    public static MessageActivity mThis = null;
    private Menu menu;
    public static String messageActivityuserId = null;
    private DateTime currentTime = new DateTime(DateTimeZone.UTC);
    private String base64String;
    private String username;
    // List<MessagePojo> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        no_post_textview = (TextView) findViewById(R.id.no_post_textview);
        no_post_layout = (LinearLayout) findViewById(R.id.no_post_layout);
        progressFrame = findViewById(R.id.chat_details);
        rootView = (ViewGroup) findViewById(R.id.message_rootview);
        messageActivityuserId = getIntent().getStringExtra(Constants.YES);
        username = getIntent().getStringExtra(Constants.USERNAME);
        //   Toast.makeText(this, "User ID: " + messageActivityuserId, Toast.LENGTH_SHORT).show();

        if (!username.isEmpty() || username != null){
            getSupportActionBar().setTitle(username);
        } else {
            getSupportActionBar().setTitle("Private chat");
        }

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        editText = (EmojiEditText) findViewById(R.id.messageEmojiEdittext);
        emojiButton = (ImageButton) findViewById(R.id.main_activity_emoji);
        sendButton = (ImageButton) findViewById(R.id.emoji_send_message);

        messagesList = (MessagesList) findViewById(R.id.messagesList);


        //  input = (MessageInput) findViewById(R.id.input);
        if (MySharedPreferences.getUserId(preferences) == null){
            Toast.makeText(this, "Please Login to interact  with the app", Toast.LENGTH_SHORT).show();
        } else {

            emojiButton.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(final View v) {
                    emojiPopup.toggle();
                }
            });

        /*    input.setInputListener(new MessageInput.InputListener() {
                @Override
                public boolean onSubmit(CharSequence input) {


                    sendMessage(input.toString());
                    adapter.addToStart(new MessagePojo(MySharedPreferences.getUserId(preferences), input.toString(), new UserPojo(MySharedPreferences.getUserId(preferences),
                            "harish", "", String.valueOf(true))), true);

                    //         adapter.addToStart(new MessagePojo(input.toString()), true);
                    return true;
                }
            }); */
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editText.getText().toString().trim().isEmpty()){
                    Toast.makeText(MessageActivity.this, "Please enter some message", Toast.LENGTH_SHORT).show();
                } else {
                    byte[] data = new byte[0];
                    try {
                        String editData = editText.getText().toString().trim();
                        data = editData.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    base64String = Base64.encodeToString(data, Base64.DEFAULT);
                    messageCount = base64String.length();
                    if (messageCount > 1499){
                        Toast.makeText(MessageActivity.this, "Please enter short messages", Toast.LENGTH_SHORT).show();
                    } else {
                        sendMessage(base64String);
                        if (isNetworkConnected()){
                            adapter.addToStart(new MessagePojo(MySharedPreferences.getUserId(preferences), base64String, new UserPojo(MySharedPreferences.getUserId(preferences),
                                    "harish", "", String.valueOf(true))), true);
                        } else {
                            Toast.makeText(MessageActivity.this, "You are not connected to internet", Toast.LENGTH_SHORT).show();
                        }
                        editText.setText("");
                    }
                }
            }
        });


        if (MySharedPreferences.getUserId(preferences) == null){
            Timber.e("Not Logged In");
            progressFrame.setVisibility(View.GONE);

        } else {
            if (isNetworkConnected()){
                try {
                    chatMessages();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //        startActivity(new Intent(this, OfflineActivity.class));
                Toast.makeText(this, "You are not connected to internet", Toast.LENGTH_SHORT).show();
            }
        }

        setUpEmojiPopup();



    }

    private void showEmptyView() {
        if (no_post_layout.getVisibility() == View.GONE) {
            no_post_layout.setVisibility(View.VISIBLE);
            no_post_textview.setText("There are no messages with this User");
        }
    }



    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(rootView)
                .setOnEmojiBackspaceClickListener(new OnEmojiBackspaceClickListener() {
                    @Override public void onEmojiBackspaceClicked(final View v) {
                        if(emojiPopup.isShowing()){
                            emojiPopup.dismiss();
                        }
                        Timber.d("Clicked on Backspace");
                    }
                })
                .setOnEmojiClickedListener(new OnEmojiClickedListener() {
                    @Override public void onEmojiClicked(final Emoji emoji) {
                        Timber.d("Clicked on emoji");
                    }
                })
                .setOnEmojiPopupShownListener(new OnEmojiPopupShownListener() {
                    @Override public void onEmojiPopupShown() {
                        emojiButton.setImageResource(R.drawable.ic_keyboard);
                    }
                })
                .setOnSoftKeyboardOpenListener(new OnSoftKeyboardOpenListener() {
                    @Override public void onKeyboardOpen(final int keyBoardHeight) {
                        Timber.d("Opened soft keyboard");
                    }
                })
                .setOnEmojiPopupDismissListener(new OnEmojiPopupDismissListener() {
                    @Override public void onEmojiPopupDismiss() {
                        emojiButton.setImageResource(R.drawable.emoji_ios_category_people);
                    }
                })
                .setOnSoftKeyboardCloseListener(new OnSoftKeyboardCloseListener() {
                    @Override public void onKeyboardClose() {
                        if (emojiPopup.isShowing()){
                            emojiPopup.dismiss();
                        }
                        Timber.d("Closed soft keyboard");
                    }
                })
                .build(editText);
    }

    @Override protected void onStop() {
        if (emojiPopup != null) {
            emojiPopup.dismiss();
        }

        super.onStop();
    }

    @Override
    protected void onResume() {
        mThis = this;
        super.onResume();
    }
    @Override
    protected void onPause() {

        mThis = null;
        messageActivityuserId = null;
        super.onPause();
    }



    private void initMessagesAdapter(List<MessagePojo> response) {
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //  Picasso.with(MessageActivity.this).load(url).placeholder(getResources().getDrawable(R.drawable.user)).error(getResources().getDrawable(R.drawable.user)).into(imageView);
                //  Picasso.with(MessageActivity.this).load(url).into(imageView);

                if (url != null){
                    if (url.isEmpty()){
                        Picasso.with(MessageActivity.this).load(R.drawable.user).into(imageView);
                    } else {
                        Picasso.with(MessageActivity.this).load(url).into(imageView);
                    }
                } else {
                    Picasso.with(MessageActivity.this).load(url).into(imageView);
                }
            }
        };

        adapter = new MessagesListAdapter<>(MySharedPreferences.getUserId(preferences), imageLoader);
        adapter.enableSelectionMode(this);


        // Todo place where we will add initial chatting messages
        if (response==null || response.isEmpty()){
            Timber.d("message is null");
        } else if (response.get(0)==null){
            Timber.e("MessagePojo null");
        } else {
            adapter.addToStart(response.get(response.size() - 1), false);
            //  response = new ArrayList<>();
            response.remove(response.size() - 1);
            if (response.size() == 0){
                Timber.e("Single Message inside chat");
            } else {
                adapter.addToEnd(response, true);
            }

        }


      /*  adapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Todo Load more method network request
                if (totalItemsCount < 50) {
                    adapter.addToEnd(response, true);
                }
            }
        }); */

        messagesList.setAdapter(adapter);
    }

    private void sendMessage(String message){



        String sendChat = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                messageActivityuserId + "_chat@yes";
        Timber.e(sendChat);

        application.getWebService()
                .getResponse(sendChat, message)
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                        //        Toast.makeText(MessageActivity.this, "Response from message: " + response, Toast.LENGTH_SHORT).show();
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Timber.d("Message from server" + response);
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                        progressFrame.setVisibility(View.GONE);
                    }

                });
    }

    private void chatMessages() throws Exception {
        application.getWebService()
                .getChatMessages(MySharedPreferences.getUserId(preferences), messageActivityuserId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<List<MessagePojo>>() {
                    @Override
                    public void onNext(List<MessagePojo> response) {
                        //          Toast.makeText(MessageActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                        //       String text = response.get(0).getText();
                        //    MessagePojo pojo = response.get(0).getMessage();
                        //messages = response;
                        initMessagesAdapter(response);

                        progressFrame.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                        progressFrame.setVisibility(View.GONE);
                    }
                });
    }

    private void deleteChat(String messageId) throws Exception {
        application.getWebService()
                .deleteChat(messageId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
                        //          Toast.makeText(MessageActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                        //       String text = response.get(0).getText();
                        //    MessagePojo pojo = response.get(0).getMessage();
                        //messages = response;
                        //   Toast.makeText(MessageActivity.this, "deleted message", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        progressFrame.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.chat_actions_menu, menu);
        onSelectionChanged(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                messageArray = adapter.getSelectedMessages();
                for (int i = 0; i < this.selectionCount; i++){
                    try {
                        deleteChat(messageArray.get(i).getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Crashlytics.logException(e);
                    }
                    //    Toast.makeText(this, "selected IDs: " + messageArray.get(i).getId(), Toast.LENGTH_SHORT).show();
                }
                adapter.deleteSelectedMessages();
                adapter.notifyDataSetChanged();
                //
                break;
        }
        return true;
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
        menu.findItem(R.id.action_delete).setVisible(count > 0);
    }

    @Override
    public void onBackPressed() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        }

        if (adapter!= null){
            if (selectionCount == 0) {
                super.onBackPressed();
            } {
                adapter.unselectAllItems();
            }

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Reminder");
            alertDialog.setMessage("You cannot interact\nunless you logged in");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }


}
