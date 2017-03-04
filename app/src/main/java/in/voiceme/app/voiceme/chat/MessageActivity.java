package in.voiceme.app.voiceme.chat;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.chat.models.MessagePojo;
import in.voiceme.app.voiceme.chat.models.UserPojo;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class MessageActivity extends BaseActivity {

    private MessagesList messagesList;
    public MessagesListAdapter<MessagePojo> adapter;
    private MessageInput input;
 //   private List<MessageActivity.Message> chatMessages;
    private int selectionCount;

    public static MessageActivity mThis = null;
    public static String userId = null;
   // List<MessagePojo> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userId = getIntent().getStringExtra(Constants.YES);

        getSupportActionBar().setTitle("Private Messages");
        setNavDrawer(new MainNavDrawer(this));

        messagesList = (MessagesList) findViewById(R.id.messagesList);


        input = (MessageInput) findViewById(R.id.input);
        input.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {

                adapter.addToStart(new MessagePojo(String.valueOf(UUID.randomUUID().getLeastSignificantBits()),
                        input.toString(), String.valueOf(new Date(System.currentTimeMillis())),
                        new UserPojo("1", "harish", "", String.valueOf(true))), true);
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
            Toast.makeText(this, "You are not connected to internet", Toast.LENGTH_SHORT).show();
        }
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
    }

    @Override
    public void onBackPressed() {
        if (selectionCount == 0) {
            super.onBackPressed();
        } else {
            adapter.unselectAllItems();
        }
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

        MessagesListAdapter.HoldersConfig holdersConfig = new MessagesListAdapter.HoldersConfig();
        holdersConfig.setIncoming(CustomIncomingMessageViewHolder.class, R.layout.item_custom_holder_incoming_message);
        holdersConfig.setOutcoming(CustomOutcomingMessageViewHolder.class, R.layout.item_custom_holder_outcoming_message);
        adapter = new MessagesListAdapter<>("1", holdersConfig, imageLoader);
        adapter.setOnMessageLongClickListener(new MessagesListAdapter.OnMessageLongClickListener<MessagePojo>() {
            @Override
            public void onMessageLongClick(MessagePojo message) {
                //Yor custom long click handler
                Toast.makeText(MessageActivity.this,
                        "Long Message Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        // Todo place where we will add initial chatting messages
        if (response==null){
            Timber.d("message is null");
        } else if (response.get(0)==null){
            Timber.e("MessagePojo null");
        } else {
            adapter.addToStart(response.get(0), false);
            adapter.addToEnd(response, true);
        }


        adapter.setLoadMoreListener(new MessagesListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Todo Load more method network request
                if (totalItemsCount < 50) {
                    adapter.addToEnd(response, true);
                }
            }
        });

        messagesList.setAdapter(adapter);
    }

    private void chatMessages() throws Exception {
        application.getWebService()
                .getChatMessages("1", userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<MessagePojo>>() {
                    @Override
                    public void onNext(List<MessagePojo> response) {
                       Toast.makeText(MessageActivity.this, response.get(0).getId(), Toast.LENGTH_SHORT).show();
                        String text = response.get(0).getText();
                  //    MessagePojo pojo = response.get(0).getMessage();
                        //messages = response;
                        initMessagesAdapter(response);
                    }
                });
    }

}
