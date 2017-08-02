package in.voiceme.app.voiceme.chat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
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
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;
import com.yalantis.ucrop.view.CropImageView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.DTO.MessagePojo;
import in.voiceme.app.voiceme.DTO.SuccessResponse;
import in.voiceme.app.voiceme.DTO.UserPojo;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.Account;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import in.voiceme.app.voiceme.utils.CurrentTimeLong;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class  MessageActivity extends BaseActivity implements MessagesListAdapter.SelectionListener,MessagesListAdapter.OnMessageClickListener<MessagePojo> {

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
    private ImageButton chat_image;
    private ImageButton sendButton;
    private int messageCount;
    private List<MessagePojo> messages;
    private String messageCopied;

    static final String USER_ID_SECOND = "USER_ID_SECOND";
    static final String USERNAME = "USERNAME";

    private String onlineString = " ";

    private static final int REQUEST_SELECT_IMAGE = 100;

    public static MessageActivity mThis = null;
    private Menu menu;
    public static String messageActivityuserId;
    private DateTime currentTime = new DateTime(DateTimeZone.UTC);
    private String base64String;
    private String username;
    private LinearLayout chat_message_below_button;
    private MessagePojo.Image image_Url = null;
    private File tempOutputFile;
    // List<MessagePojo> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        progressFrame = findViewById(R.id.chat_details);
        rootView = (ViewGroup) findViewById(R.id.message_rootview);

        chat_message_below_button = (LinearLayout) findViewById(R.id.chat_message_below_button);


        Intent intent = getIntent();
        messageActivityuserId = intent.getStringExtra(Constants.YES);
        username = intent.getStringExtra(Constants.USERNAME);


        if (savedInstanceState != null){
            messageActivityuserId = savedInstanceState.getString(USER_ID_SECOND);
            username = savedInstanceState.getString(USERNAME);
        }

        tempOutputFile = new File(getExternalCacheDir(), "temp-profile_image.jpg");

        //   Toast.makeText(this, "User ID: " + messageActivityuserId, Toast.LENGTH_SHORT).show();

        final Handler handler = new Handler();
        scheduler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds
                checkOnlineIndicator(messageActivityuserId);
                handler.postDelayed(this, 20000);
            }
        }, 500);

        final Handler handler4 = new Handler();
        scheduler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 20 seconds
                getSupportActionBar().setSubtitle(onlineString);
                handler4.postDelayed(this, 2000);
            }
        }, 1000);


        if (!username.isEmpty() && username != null){
            getSupportActionBar().setTitle(username);

        } else {
            getSupportActionBar().setTitle("Private chat");
         //   getSupportActionBar().setSubtitle(getResources().getString(R.string.mySubTitle));
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
        chat_image = (ImageButton) findViewById(R.id.chat_image);
        sendButton = (ImageButton) findViewById(R.id.emoji_send_message);

        messagesList = (MessagesList) findViewById(R.id.messagesList);

        // Repeating code



        chat_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserBlock();
            }
        });



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
                    checkUserBlockText();
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

    private void sendTextMessage() {
        byte[] data = new byte[0];
        try {
            String editData = editText.getText().toString().trim();
            data = editData.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        base64String = Base64.encodeToString(data, Base64.DEFAULT);
        messageCount = base64String.length();
        if (messageCount > 800){
            Toast.makeText(MessageActivity.this, "Please enter short messages", Toast.LENGTH_SHORT).show();
        } else {
            sendMessage(base64String);
            if (isNetworkConnected()){
                adapter.addToStart(new MessagePojo(MySharedPreferences.getUserId(preferences), base64String, new UserPojo(MySharedPreferences.getUserId(preferences),
                        "harish", "", true)), true);
            } else {
                Toast.makeText(MessageActivity.this, "You are not connected to internet", Toast.LENGTH_SHORT).show();
            }
            editText.setText("");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(USER_ID_SECOND, messageActivityuserId);
        outState.putString(USERNAME, username);
        super.onSaveInstanceState(outState);
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

    @Subscribe
    public void getChatMessageFromNotificarion(Account.ChatMessageBusEvent message){
        adapter.addToStart(new
                MessagePojo(message.messagePojo.getSenderId(), message.messagePojo.getChatText(),
                String.valueOf(System.currentTimeMillis()), new UserPojo(message.messagePojo.getSenderId(),
                message.messagePojo.getSenderName(), "", true)), true);
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
        super.onPause();
    }


    private void changeProfileRequest() {
        //  ActivityUtils.openGallery(this);
        ActivityUtils.cameraPermissionGranted(this);
        changeAvatar();
    }

    private void changeAvatar() {
        List<Intent> otherImageCaptureIntent = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities =
                getPackageManager().queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                        0); // finding all intents in apps which can handle capture image
        // loop through all these intents and for each of these activities we need to store an intent
        for (ResolveInfo info : otherImageCaptureActivities) { // Resolve info represents an activity on the system that does our work
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName,
                    info.activityInfo.name); // declaring explicitly the class where we will go
            // where the picture activity dump the image
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutputFile));
            otherImageCaptureIntent.add(captureIntent);
        }

        // above code is only for taking picture and letting it go through another app for cropping before setting to imageview
        // now below is for choosing the image from device

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser = Intent.createChooser(selectImageIntent, "Choose Avatar");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, otherImageCaptureIntent.toArray(
                new Parcelable[otherImageCaptureActivities.size()]));  // add 2nd para as intent of parcelables.

        startActivityForResult(chooser, REQUEST_SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            tempOutputFile.delete();
            return;
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_IMAGE) {
                // user selected an image off their device. other condition they took the image and that image is in our tempoutput file
                Uri outputFile;
                Uri tempFileUri = Uri.fromFile(tempOutputFile);
                // if statement will detect if the user selected an image from the device or took an image
                if (data != null && (data.getAction() == null || !data.getAction()
                        .equals(MediaStore.ACTION_IMAGE_CAPTURE))) {
                    //then it means user selected an image off the device
                    // so we can get the Uri of that image using data.getData
                    outputFile = data.getData();
                    // Now we need to do the crop
                } else {
                    // image was out temp file. user took an image using camera
                    outputFile = tempFileUri;
                    // Now we need to do the crop
                }
                startCropActivity(outputFile);
            } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {


                uploadFile(Uri.parse(tempOutputFile.getPath()));

                Timber.e(String.valueOf(Uri.fromFile(tempOutputFile)));

                // avatarProgressFrame.setVisibility(View.VISIBLE);
                // bus.post(new Account.ChangeAvatarRequest(Uri.fromFile(tempOutputFile)));
            }
        }
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        //    options.setCompressionQuality(DEFAULT_COMPRESS_QUALITY);
        options.setFreeStyleCropEnabled(false);
        options.setAspectRatioOptions(1,
                new AspectRatio("1:2", 1, 2),
                new AspectRatio("3:4", 3, 4),
                new AspectRatio("DEFAULT", CropImageView.DEFAULT_ASPECT_RATIO, CropImageView.DEFAULT_ASPECT_RATIO),
                new AspectRatio("16:9", 16, 9),
                new AspectRatio("1:1", 1, 1));
        //  options.setImageToCropBoundsAnimDuration(CROP_BOUNDS_ANIMATION_DURATION);
        options.setShowCropGrid(false);
        options.setMaxScaleMultiplier(10.0f);
        return uCrop.withOptions(options);
    }

    private void startCropActivity(@NonNull Uri uri) {

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(tempOutputFile));

        uCrop = advancedConfig(uCrop);
        uCrop.start(MessageActivity.this);
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

        MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextConfig(
                        CustomIncomingTextMessageViewHolder.class,
                        R.layout.item_custom_incoming_text_message)
                .setOutcomingTextConfig(
                        CustomOutcomingTextMessageViewHolder.class,
                        R.layout.item_custom_outcoming_text_message)
                .setIncomingImageConfig(
                        CustomIncomingImageMessageViewHolder.class,
                        R.layout.item_custom_incoming_image_message)
                .setOutcomingImageConfig(
                        CustomOutcomingImageMessageViewHolder.class,
                        R.layout.item_custom_outcoming_image_message);


        adapter = new MessagesListAdapter<>(MySharedPreferences.getUserId(preferences), holdersConfig, imageLoader);
        adapter.setOnMessageClickListener(this);
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
                messageActivityuserId  + "_chat@yes";
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        messageActivityuserId = savedInstanceState.getString(USER_ID_SECOND);
        username = savedInstanceState.getString(USERNAME);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void sendImageMessage(String message){

        String sendChat = "senderid@" + MySharedPreferences.getUserId(preferences) + "_contactId@" +
                messageActivityuserId + "_chat@yes";
        Timber.e(sendChat);

        application.getWebService()
                .getImageResponse(sendChat, message)
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

    private void checkUserBlock(){

        application.getWebService()
                .block_user_check(messageActivityuserId, MySharedPreferences.getUserId(preferences))
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse response) {

                        if (response.getSuccess()){
                            Toast.makeText(MessageActivity.this, "This user has blocked You", Toast.LENGTH_LONG).show();
                        } else {
                            changeProfileRequest();
                        }
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        Crashlytics.logException(e);
                        progressFrame.setVisibility(View.GONE);
                    }

                });
    }

    private void checkUserBlockText(){

        application.getWebService()
                .block_user_check(messageActivityuserId, MySharedPreferences.getUserId(preferences))
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<SuccessResponse>() {
                    @Override
                    public void onNext(SuccessResponse response) {

                        if (response.getSuccess()){
                            Toast.makeText(MessageActivity.this, "This user has blocked You", Toast.LENGTH_LONG).show();
                        } else {
                            sendTextMessage();
                        }
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
                   //     this.messages = response;
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



    private void uploadFile(Uri fileUri) {
        File file = new File(String.valueOf(fileUri));

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // finally, execute the request
        try {
            application.getWebService()
                    .uploadFile(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<String>() {
                        @Override
                        public void onNext(String response) {
                            Timber.d("file url " + response);
                            chatMessage(response);
                         //   setImageFileUrl(response);
                        //    MySharedPreferences.registerImageUrl(preferences, response);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setImageFileUrl(String imageUrl) {
        this.image_Url = new MessagePojo.Image(imageUrl);
    }

    private void setOnlineString(String onlineString){
        this.onlineString = onlineString;
    }

    protected void checkOnlineIndicator(String userId) {

        application.getWebService()
                .checkOnline(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<OnlineStatusCheck>() {
                    @Override
                    public void onNext(OnlineStatusCheck response) {

                        if (response.getOnlineStatus()){
                            setOnlineString("Online Now");
                        } else {
                            setOnlineString(String.valueOf("Last Seen " + CurrentTimeLong.getCurrentTime(response.getLastTimeOnline(), MessageActivity.this)));
                        }
                    }
                });
    }

    private void chatMessage(String url){

        MessagePojo messagePojo = new MessagePojo(MySharedPreferences.getUserId(preferences), "",
                String.valueOf(System.currentTimeMillis()), new UserPojo(MySharedPreferences.getUserId(preferences),
                MySharedPreferences.getUsername(preferences), "", true));

        messagePojo.setImage(new MessagePojo.Image(url));
        adapter.addToStart(messagePojo, true);
        sendImageMessage(url);
      //  return messagePojo;
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
            case R.id.action_copy:
                adapter.copySelectedMessagesText(this, getMessageStringFormatter(), true);
                break;
        }
        return true;
    }

    private MessagesListAdapter.Formatter<MessagePojo> getMessageStringFormatter() {
        return new MessagesListAdapter.Formatter<MessagePojo>() {
            @Override
            public String format(MessagePojo message) {
          //      String createdAt = new SimpleDateFormat("MMM d, EEE 'at' h:mm a", Locale.getDefault()).format(message.getCreatedAt());

                byte[] data = Base64.decode(message.getText(), Base64.DEFAULT);
                try {
                    messageCopied = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //Set Last message text



                String text = messageCopied;
                if (message.getText().equals("") || message.getText() == null) text = message.getImageUrl();

                return String.format(text);
            }
        };
    }

    @Override
    public void onSelectionChanged(int count) {
        this.selectionCount = count;
        menu.findItem(R.id.action_delete).setVisible(count > 0);
        menu.findItem(R.id.action_copy).setVisible(count > 0);
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

    @Override
    public void onMessageClick(MessagePojo message) {
        if (message.getImageUrl()!=null){
            Intent imageURL = new Intent(this, ImageZoomActivity.class);
            imageURL.putExtra(Constants.IMAGE_URL, message.getImageUrl());
            startActivity(imageURL);
        } else {
            Timber.e("Its Text");
        }
    }
}
