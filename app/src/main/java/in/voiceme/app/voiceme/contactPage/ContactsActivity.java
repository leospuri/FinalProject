package in.voiceme.app.voiceme.contactPage;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.redbooth.WelcomeCoordinatorLayout;

import in.voiceme.app.voiceme.DTO.BaseResponse;
import in.voiceme.app.voiceme.DTO.ContactAddResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.contactPage.animators.ChatAvatarsAnimator;
import in.voiceme.app.voiceme.contactPage.animators.InSyncAnimator;
import in.voiceme.app.voiceme.contactPage.animators.RocketAvatarsAnimator;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.RegisterActivity;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.userpost.PrivacyPolicy;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    //   private static final String TWITTER_KEY = "I6Zt8s6wSZzMtnPqun18Raw0T";
    //   private static final String TWITTER_SECRET = "Jb92MdEm2GmK40RMqZvoxmjTFR4aUipanCOYr3BHloy43cvOsA";
    private Button getAllContacts;
  //  private Button enterButton;
    private AlertDialog.Builder builder1;
    // private DigitsAuthButton digitsButton;

    //  private AuthCallback callback;
    //  private Button digitsAuthButton;
    // private TextView personalContact;
    private TextView allPersonalContact;
    private ProgressBar activity_allcontacts_progressbar;
    private TextView verified;
    private TextView singlePrivacyPolicy;
    private boolean givenPersonalContact = false;
    private boolean givenAllPersonalContact = false;
    private ImageView skip;

    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;

    WelcomeCoordinatorLayout coordinatorLayout;
    private RocketAvatarsAnimator rocketAvatarsAnimator;
    private ChatAvatarsAnimator chatAvatarsAnimator;
    private InSyncAnimator inSyncAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //      Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        setContentView(R.layout.activity_contacts);


        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        activity_allcontacts_progressbar = (ProgressBar) findViewById(R.id.activity_allcontacts_progressbar);
        initializeListeners();
        initializePages();
        initializeBackgroundTransitions();

        skip = (ImageView) findViewById(R.id.skip);

        getAllContacts = (Button) findViewById(R.id.button_get_all_contacts);
        singlePrivacyPolicy = (TextView) findViewById(R.id.person_contact_single);
        verified = (TextView) findViewById(R.id.verfied_single);
     //   enterButton = (Button) findViewById(R.id.enter_contacts_main_page);

//        personalContact = (TextView) findViewById(R.id.person_contact_verified);
        allPersonalContact = (TextView) findViewById(R.id.all_person_contact_verified);

        //      personalContact.setVisibility(View.GONE);
        allPersonalContact.setVisibility(View.GONE);

      //  enterButton.setOnClickListener(this);
        getAllContacts.setOnClickListener(this);
        skip.setOnClickListener(this);
        // Create a digits button and callback


        singlePrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivity.this, PrivacyPolicy.class));
            }
        });

        final AuthCallback callback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Timber.v("DIGITS SUCCESSFUL authentication");
                Timber.v("phone number: " + phoneNumber);
                // application.getAuth().getUser().setPhoneNumber(true);

                verified.setVisibility(View.VISIBLE);
                givenPersonalContact = true;

                MySharedPreferences.registerContact(preferences, phoneNumber);
                String newPhoneNumber = removeSpecialCharacters(phoneNumber);
                //             personalContact.setVisibility(View.VISIBLE);
                //   phoneNumber.toString().replace("+", "");

                try {
                    sendContact(newPhoneNumber);
                    coordinatorLayout.setCurrentPage(4, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(DigitsException error) {
                Timber.d("Oops Digits issue");
            }
        };


        final Button digitsAuthButton = (Button) findViewById(R.id.auth_button);

        digitsAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (processLoggedState(v)) {
                    return;
                } else {
                    AuthConfig.Builder digitsAuthConfigBuilder = new AuthConfig.Builder()
                            .withAuthCallBack(callback)
                            .withPhoneNumber("");

                    Digits.authenticate(digitsAuthConfigBuilder.build());
                }


            }
        });

       // enterButton.setBackgroundColor(getResources().getColor(R.color.material_red_500));

    }

    public String removeSpecialCharacters(String string) {
        string = string.replaceAll("[^\\dA-Za-z ]", "").replaceAll("\\s+", "+");
        string = string.replaceAll("[-+^)(]*", "");
        string = string.replaceAll(" ", "");
        return string;
    }

    private void initializePages() {
        final WelcomeCoordinatorLayout coordinatorLayout
                = (WelcomeCoordinatorLayout)findViewById(R.id.coordinator);
        coordinatorLayout.addPage(R.layout.welcome_page_1,
                R.layout.welcome_page_2,
                R.layout.welcome_page_3,
                R.layout.welcome_page_5,
                R.layout.welcome_page_6);
    }

    private void initializeListeners() {
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {
                switch (pageSelected) {
                    case 0:
                        if (rocketAvatarsAnimator == null) {
                            rocketAvatarsAnimator = new RocketAvatarsAnimator(coordinatorLayout);
                            rocketAvatarsAnimator.play();
                        }
                        break;
                    case 1:
                        if (chatAvatarsAnimator == null) {
                            chatAvatarsAnimator = new ChatAvatarsAnimator(coordinatorLayout);
                            chatAvatarsAnimator.play();
                        }
                        break;
                    case 2:
                        if (inSyncAnimator == null) {
                            inSyncAnimator = new InSyncAnimator(coordinatorLayout);
                            inSyncAnimator.play();
                        }
                    case 3:
                        if (inSyncAnimator == null) {
                            return;
                        }
                    case 4:
                        if (inSyncAnimator == null) {
                            return;
                        }

                        break;
                }
            }
        });
    }

    private void initializeBackgroundTransitions() {
        final Resources resources = getResources();
        final int colorPage1 = ResourcesCompat.getColor(resources, R.color.page1, getTheme());
        final int colorPage2 = ResourcesCompat.getColor(resources, R.color.page2, getTheme());
        final int colorPage3 = ResourcesCompat.getColor(resources, R.color.page3, getTheme());
        final int colorPage4 = ResourcesCompat.getColor(resources, R.color.page4, getTheme());
        backgroundAnimator = ValueAnimator
                .ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3, colorPage4);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }
        });
    }

    //  @OnClick(R.id.skip)
    void skip() {
        coordinatorLayout.setCurrentPage(coordinatorLayout.getNumOfPages() - 1, true);
    }

    @Override
    public void onClick(View view) {

            if (view.getId() == R.id.button_get_all_contacts){
                if (MySharedPreferences.getContact(preferences) == null){
                    Toast.makeText(ContactsActivity.this, "You need to enter your number first", Toast.LENGTH_LONG).show();
                    coordinatorLayout.setCurrentPage(3, true);
                } else {

                    activity_allcontacts_progressbar.setVisibility(View.VISIBLE);
                    getAllContacts.setVisibility(View.GONE);

                    readContacts();
                }

            } else if (view.getId() == R.id.skip){
            startActivity(new Intent(this, DiscoverActivity.class));
            finish();
            return;
        }

        /* else if (view.getId() == R.id.enter_contacts_main_page){

            if (givenPersonalContact) {
                if (givenAllPersonalContact){
                    enterButton.setBackgroundColor(getResources().getColor(R.color.contacts_green_button));
                    //  MySharedPreferences.checkContactSent(preferences, "Sent");

                    startActivity(new Intent(ContactsActivity.this, ContactListActivity.class));
                    finish();
                    return;
                } else {
                    Toast.makeText(this, "You have not given your contacts", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "You have not given your phone number", Toast.LENGTH_SHORT).show();
            }

        } */
    }

    private void readContacts() {
        ActivityUtils.isContactsPermission(this);
    }

    public void contactMethod(){
        startService(new Intent(this, ContactService.class));
      //  dialogBox();
        application.getAuth().getUser().setAllContacts(true);
        setAuthToken("token");
        startActivity(new Intent(ContactsActivity.this, ContactListActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter=new IntentFilter(ContactService.BROADCAST);

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }

    private BroadcastReceiver onNotice=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            showNotification();
        }
    };



  /*  private void getContacts() {

        ProgressDialog dialog = new ProgressDialog(ContactsActivity.this);
        dialog.setCancelable(true);
        dialog.setMessage("Reading contacts..");
        dialog.show();

        Observable.fromCallable(
                () -> ContactsHelper.readContacts(ContactsActivity.this.getContentResolver()))
                .doOnError(throwable -> Timber.d(throwable.getMessage()))
                .onErrorResumeNext(throwable -> {
                    Timber.d(throwable.getMessage());
                    return Observable.empty();
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contacts -> {
                    try {
                        sendAllContacts(contacts.toString().replace("[", "").replace("]", "").replace(" ", ""));
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // showContactsCompletedDialog(contacts);
                    Timber.d("comma separated contacts array %s", contacts.toString().replace("[", "").replace("]", ""));
                });
    } */

    public void showNotification() {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, ContactListActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Your Anonymous Page is ready")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Your Page is ready")
                .setContentText("Click to enter")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    private void sendAllContacts(String contacts) throws Exception {
        application.getWebService()
                .addAllContacts(MySharedPreferences.getUserId(preferences), contacts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<ContactAddResponse>() {
                    @Override
                    public void onNext(ContactAddResponse response) {
                        Timber.e("Got user details " + response.getInsertedRows().toString());
                        application.getAuth().getUser().setAllContacts(true);
                        givenAllPersonalContact = true;
                        allPersonalContact.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void dialogBox(){
        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Welcome to anonymous page.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        application.getAuth().getUser().setAllContacts(true);
                        setAuthToken("token");
                        startActivity(new Intent(ContactsActivity.this, ContactListActivity.class));
                        dialog.cancel();
                    }
                });

     /*   builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }); */

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == getResources().getInteger(R.integer.contacts_request)) {
                contactMethod();
            }
        }
    }

    private void sendContact(String phoneNumber) throws Exception {
        application.getWebService()
                .registerMobile(MySharedPreferences.getUserId(preferences), phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        Timber.e("Successfully logged in" + response.getStatus());

                        coordinatorLayout.setCurrentPage(R.layout.welcome_page_6, true);

                    }
                });
    }


    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
            alertDialog.setTitle("Reminder");
            alertDialog.setMessage("You cannot interact\nunless you logged in");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ContactsActivity.this, RegisterActivity.class));
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }
}