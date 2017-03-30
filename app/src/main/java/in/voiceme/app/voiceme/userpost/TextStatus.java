package in.voiceme.app.voiceme.userpost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;

import in.voiceme.app.voiceme.ActivityPage.MainActivity;
import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.SecondBeforeLoginActivity;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class TextStatus extends BaseActivity {
    private TextView textView_category;
    private TextView textView_feeling;
    private TextView textView_status;
    //private TextModel textModel;

    private String category;
    private String feeling;
    private String textStatus;
    private TextView choosen_category;

    private Button button_post_text_status;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_text_status);
        getSupportActionBar().setTitle("Text Status");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        textView_category = (TextView) findViewById(R.id.textView_category);
        textView_feeling = (TextView) findViewById(R.id.textView_feeling);
        textView_status = (TextView) findViewById(R.id.textView_status);
        button_post_text_status = (Button) findViewById(R.id.button_post_text_status);

        textView_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("TextStatusPage")
                        .setAction("Enter Category Page")
                        .build());
                // [END custom_event]
             //   Intent categoryIntent = new Intent(TextStatus.this, CategoryActivity.class);
                Intent categoryIntent = new Intent(TextStatus.this, CategoryActivity.class);
                startActivityForResult(categoryIntent, 1);
            }
        });
        textView_feeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("TextStatusPage")
                        .setAction("Enter Feeling Page")
                        .build());
                // [END custom_event]
                Intent feelingIntent = new Intent(TextStatus.this, FeelingActivity.class);
                startActivityForResult(feelingIntent, 2);
            }
        });
        textView_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                // [START custom_event]
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("TextStatusPage")
                        .setAction("Enter Status Page")
                        .build());
                // [END custom_event]
                Intent statusIntent = new Intent(TextStatus.this, StatusActivity.class);
                startActivityForResult(statusIntent, 3);
            }
        });

        button_post_text_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (processLoggedState(view)){
                    return;
                } else {
                    if (category == null || feeling == null || textStatus == null) {
                        Toast.makeText(TextStatus.this, "Please select all categories to Post Status", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            application.getWebService().postStatus(MySharedPreferences.getUserId(preferences),
                                    textStatus, category, feeling, "", "")
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .retryWhen(new RetryWithDelay(3,2000))
                                    .subscribe(new BaseSubscriber<UserResponse>() {
                                        @Override
                                        public void onNext(UserResponse userResponse) {
                                            Timber.e("UserResponse " + userResponse.getStatus() + "===" + userResponse.getMsg());
                                            if (userResponse.getStatus() == 1) {
                                             //   Toast.makeText(TextStatus.this, "Successfully posted status", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(TextStatus.this, MainActivity.class));
                                            }
                                        }
                                        @Override
                                        public void onError(Throwable e) {
                                            try {
                                                Toast.makeText(TextStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }catch (Exception ex){
                                                ex.printStackTrace();
                                            }
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    // [START custom_event]
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("TextStatusPage")
                            .setAction("Post Status")
                            .build());
                    // [END custom_event]
                    // network call from retrofit

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromCategory");
            //    Toast.makeText(this, "Category returned: " + result, Toast.LENGTH_SHORT).show();
                category = result;
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromFeeling");
         //       Toast.makeText(this, "Feeling returned: " + result, Toast.LENGTH_SHORT).show();
                feeling = result;

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromStatus");
        //        Toast.makeText(this, "text Status returned: " + result, Toast.LENGTH_SHORT).show();
                textStatus = result;
            }
        }

    }


    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Reminder");
            alertDialog.setMessage("You cannot interact\nunless you logged in");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(TextStatus.this, SecondBeforeLoginActivity.class));
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }

}
