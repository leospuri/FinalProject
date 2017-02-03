package in.voiceme.app.voiceme.contactPage;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.redbooth.WelcomeCoordinatorLayout;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.contactPage.animators.ChatAvatarsAnimator;
import in.voiceme.app.voiceme.contactPage.animators.InSyncAnimator;
import in.voiceme.app.voiceme.contactPage.animators.RocketAvatarsAnimator;
import in.voiceme.app.voiceme.contactPage.animators.RocketFlightAwayAnimator;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.userpost.BaseResponse;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import in.voiceme.app.voiceme.utils.ContactsHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class ContactsActivity extends BaseActivity implements View.OnClickListener {
    //   private static final String TWITTER_KEY = "I6Zt8s6wSZzMtnPqun18Raw0T";
    //   private static final String TWITTER_SECRET = "Jb92MdEm2GmK40RMqZvoxmjTFR4aUipanCOYr3BHloy43cvOsA";
    private Button getAllContacts;
    private Button enterButton;
    // private DigitsAuthButton digitsButton;

    //  private AuthCallback callback;
    //  private Button digitsAuthButton;
    // private TextView personalContact;
    private TextView allPersonalContact;
    private boolean givenPersonalContact = false;
    private boolean givenAllPersonalContact = false;
    private ImageView skip;

    private boolean animationReady = false;
    private ValueAnimator backgroundAnimator;

    WelcomeCoordinatorLayout coordinatorLayout;
    private RocketAvatarsAnimator rocketAvatarsAnimator;
    private ChatAvatarsAnimator chatAvatarsAnimator;
    private RocketFlightAwayAnimator rocketFlightAwayAnimator;
    private InSyncAnimator inSyncAnimator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //      TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //      Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());

        setContentView(R.layout.activity_contacts);


        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        initializeListeners();
        initializePages();
        initializeBackgroundTransitions();

        skip = (ImageView) findViewById(R.id.skip);

        getAllContacts = (Button) findViewById(R.id.button_get_all_contacts);
        enterButton = (Button) findViewById(R.id.enter_contacts_main_page);

//        personalContact = (TextView) findViewById(R.id.person_contact_verified);
        allPersonalContact = (TextView) findViewById(R.id.all_person_contact_verified);

        //      personalContact.setVisibility(View.GONE);
        allPersonalContact.setVisibility(View.GONE);

        enterButton.setOnClickListener(this);
        getAllContacts.setOnClickListener(this);
        skip.setOnClickListener(this);
        // Create a digits button and callback


        final AuthCallback callback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Timber.v("DIGITS SUCCESSFUL authentication");
                Timber.v("phone number: " + phoneNumber);
                // application.getAuth().getUser().setPhoneNumber(true);

                givenPersonalContact = true;

                String newPhoneNumber = removeSpecialCharacters(phoneNumber);
                //             personalContact.setVisibility(View.VISIBLE);
                //   phoneNumber.toString().replace("+", "");

                try {
                    sendContact(newPhoneNumber);
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

                AuthConfig.Builder digitsAuthConfigBuilder = new AuthConfig.Builder()
                        .withAuthCallBack(callback)
                        .withPhoneNumber("");

                Digits.authenticate(digitsAuthConfigBuilder.build());
            }
        });

        enterButton.setBackgroundColor(getResources().getColor(R.color.material_red_500));

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
                R.layout.welcome_page_6,
                R.layout.welcome_page_4);
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
                    case 5:
                        if (rocketFlightAwayAnimator == null) {
                            rocketFlightAwayAnimator = new RocketFlightAwayAnimator(coordinatorLayout);
                            rocketFlightAwayAnimator.play();
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
        processLoggedState(view);
        if (view.getId() == R.id.button_get_all_contacts){
            readContacts();

        } else if (view.getId() == R.id.skip){
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        } else if (view.getId() == R.id.enter_contacts_main_page){

            if (givenPersonalContact) {
                if (givenAllPersonalContact){
                    enterButton.setBackgroundColor(getResources().getColor(R.color.contacts_green_button));
                    //  MySharedPreferences.checkContactSent(preferences, "Sent");
                    setAuthToken("token");
                    startActivity(new Intent(ContactsActivity.this, ContactListActivity.class));
                    finish();
                    return;
                } else {
                    Toast.makeText(this, "You have not given your contacts", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "You have not given your phone number", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void readContacts() {
        if (ActivityUtils.isContactsPermission(this)) {
            getContacts();
        }
    }

    private void getContacts() {

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
                    contacts.remove(0);
                    contacts.remove(contacts.size() - 1);
                    try {
                        sendAllContacts(contacts.toString().replace("[", "").replace("]", "").replace(" ", ""));
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // showContactsCompletedDialog(contacts);
                    Timber.d("comma separated contacts array %s", contacts.toString().replace("[", "").replace("]", ""));
                });
    }

    private void sendAllContacts(String contacts) throws Exception {
        application.getWebService()
                .addAllContacts(MySharedPreferences.getUserId(preferences), contacts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AddContactResponse>() {
                    @Override
                    public void onNext(AddContactResponse response) {
                        Timber.e("Got user details " + response.getInsertedRows().toString());
                        application.getAuth().getUser().setAllContacts(true);
                        givenAllPersonalContact = true;
                        allPersonalContact.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == getResources().getInteger(R.integer.contacts_request)) {
                getContacts();
            }
        }
    }

    private void sendContact(String phoneNumber) throws Exception {
        application.getWebService()
                .registerMobile(MySharedPreferences.getUserId(preferences), phoneNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        Timber.e("Successfully logged in" + response.getStatus());

                    }
                });
    }


//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
