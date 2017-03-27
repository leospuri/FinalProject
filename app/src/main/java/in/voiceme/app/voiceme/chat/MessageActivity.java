package in.voiceme.app.voiceme.chat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.DTO.MessagePojo;
import in.voiceme.app.voiceme.DTO.UserPojo;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MessageActivity extends BaseActivity implements MessagesListAdapter.SelectionListener {

    private MessagesList messagesList = null;
    public MessagesListAdapter<MessagePojo> adapter;
    public ArrayList<MessagePojo> messageArray;
    private MessageInput input;
    private View progressFrame;
 //   private List<MessageActivity.Message> chatMessages;
    private int selectionCount;

    public static MessageActivity mThis = null;
    private Menu menu;
    public static String messageActivityuserId = null;
    private DateTime currentTime = new DateTime(DateTimeZone.UTC);
   // List<MessagePojo> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        progressFrame = findViewById(R.id.chat_details);
        messageActivityuserId = getIntent().getStringExtra(Constants.YES);
     //   Toast.makeText(this, "User ID: " + messageActivityuserId, Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle("Private Messages");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        messagesList = (MessagesList) findViewById(R.id.messagesList);


        input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {


                sendMessage(input.toString());
                adapter.addToStart(new MessagePojo(MySharedPreferences.getUserId(preferences), input.toString(), new UserPojo(MySharedPreferences.getUserId(preferences),
                                "harish", "", String.valueOf(true))), true);

       //         adapter.addToStart(new MessagePojo(input.toString()), true);
                return true;
            }
        });

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

        progressFrame.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThis = this;
    }
    @Override
    protected void onPause() {
        super.onPause();
        mThis = null;
        messageActivityuserId = null;
    }



    private void initMessagesAdapter(List<MessagePojo> response) {
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
              //  Picasso.with(MessageActivity.this).load(url).placeholder(getResources().getDrawable(R.drawable.user)).error(getResources().getDrawable(R.drawable.user)).into(imageView);
              //  Picasso.with(MessageActivity.this).load(url).into(imageView);
                if (imageView.equals(null)|| url.isEmpty()){
                    Picasso.with(MessageActivity.this).load(R.drawable.user).into(imageView);
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
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String response) {
                        Timber.d("Got user details");
                //        Toast.makeText(MessageActivity.this, "Response from message: " + response, Toast.LENGTH_SHORT).show();
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Timber.d("Message from server" + response);
                    }

                });
    }

    private void chatMessages() throws Exception {
        application.getWebService()
                .getChatMessages(MySharedPreferences.getUserId(preferences), messageActivityuserId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<List<MessagePojo>>() {
                    @Override
                    public void onNext(List<MessagePojo> response) {
             //          Toast.makeText(MessageActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                 //       String text = response.get(0).getText();
                  //    MessagePojo pojo = response.get(0).getMessage();
                        //messages = response;
                        initMessagesAdapter(response);
                    }
                });
    }

    private void deleteChat(String messageId) throws Exception {
        application.getWebService()
                .deleteChat(messageId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<UserResponse>() {
                    @Override
                    public void onNext(UserResponse response) {
             //          Toast.makeText(MessageActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                 //       String text = response.get(0).getText();
                  //    MessagePojo pojo = response.get(0).getMessage();
                        //messages = response;
                     //   Toast.makeText(MessageActivity.this, "deleted message", Toast.LENGTH_SHORT).show();
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
                    }
                //    Toast.makeText(this, "selected IDs: " + messageArray.get(i).getId(), Toast.LENGTH_SHORT).show();
                }
                adapter.deleteSelectedMessages();
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
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            adapter.unselectAllItems();
        }
    }
}
