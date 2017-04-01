package in.voiceme.app.voiceme.userpost;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;

import in.voiceme.app.voiceme.DTO.UserResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.RegisterActivity;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class TextStatus extends BaseActivity implements View.OnClickListener {
    private TextView textView_category;
    private TextView textView_feeling;
    private LinearLayout chosen_feeling_text;
    private TextView textView_status;
    //private TextModel textModel;
    private LinearLayout selected_text_select;
    private LinearLayout text_chosen_status;
    private LinearLayout text_category_back;
    private LinearLayout text_feeling;
    private LinearLayout text_text_status;
    private ImageView category_id;
    private ImageView feeling_image;
    private ImageView id_status;
    private TextView choosen_text_category;
    private TextView choosen_feeling_text;
    private TextView choosen_text_written;

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

        selected_text_select = (LinearLayout) findViewById(R.id.selected_text_select);
        text_category_back = (LinearLayout) findViewById(R.id.text_category_back);
        text_feeling = (LinearLayout) findViewById(R.id.text_feeling);
        text_chosen_status = (LinearLayout) findViewById(R.id.text_chosen_status);
        text_text_status = (LinearLayout) findViewById(R.id.text_text_status);

        category_id = (ImageView) findViewById(R.id.category_id);
        feeling_image = (ImageView) findViewById(R.id.feeling_image);
        id_status = (ImageView) findViewById(R.id.id_status);


        chosen_feeling_text = (LinearLayout) findViewById(R.id.chosen_feeling_text);
        choosen_feeling_text = (TextView) findViewById(R.id.choosen_feeling_text);
        choosen_text_category = (TextView) findViewById(R.id.choosen_text_category);
        textView_category = (TextView) findViewById(R.id.textView_category);
        textView_feeling = (TextView) findViewById(R.id.textView_feeling);
        choosen_text_written = (TextView) findViewById(R.id.choosen_text_written);
        textView_status = (TextView) findViewById(R.id.textView_status);
        button_post_text_status = (Button) findViewById(R.id.button_post_text_status);

        button_post_text_status.setOnClickListener(this);
        text_feeling.setOnClickListener(this);
        textView_feeling.setOnClickListener(this);
        textView_status.setOnClickListener(this);
        textView_category.setOnClickListener(this);
        id_status.setOnClickListener(this);
        category_id.setOnClickListener(this);
        feeling_image.setOnClickListener(this);
        text_text_status.setOnClickListener(this);
        text_category_back.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromCategory");
                String result2 = data.getStringExtra("resultFromCategory2");
                choosen_text_category.setText(result2);
                text_chosen_status.setVisibility(View.VISIBLE);
            //    Toast.makeText(this, "Category returned: " + result, Toast.LENGTH_SHORT).show();
                category = result;
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromFeeling");
                String result2 = data.getStringExtra("resultFromFeeling2");
                chosen_feeling_text.setVisibility(View.VISIBLE);
                choosen_feeling_text.setText(result2);
         //       Toast.makeText(this, "Feeling returned: " + result, Toast.LENGTH_SHORT).show();
                feeling = result;

            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("resultFromStatus");
                selected_text_select.setVisibility(View.VISIBLE);
                choosen_text_written.setText(result);
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
                    startActivity(new Intent(TextStatus.this, RegisterActivity.class));
                }
            });
            alertDialog.show();
            return true;
        }
        return false;

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.text_category_back ||
                view.getId() == R.id.category_id ||
                view.getId() == R.id.textView_category){
          //  processLoggedState(view);
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("TextStatusPage")
                    .setAction("Enter Category Page")
                    .build());
            // [END custom_event]
            //   Intent categoryIntent = new Intent(TextStatus.this, CategoryActivity.class);
            Intent categoryIntent = new Intent(TextStatus.this, CategoryActivity.class);
            startActivityForResult(categoryIntent, 1);

        } else if(view.getId() == R.id.text_feeling ||
                view.getId() == R.id.feeling_image ||
                view.getId() == R.id.textView_feeling){
         //   processLoggedState(view);
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("TextStatusPage")
                    .setAction("Enter Feeling Page")
                    .build());
            // [END custom_event]
            Intent feelingIntent = new Intent(TextStatus.this, FeelingActivity.class);
            startActivityForResult(feelingIntent, 2);

        } else if(view.getId() == R.id.text_text_status ||
                view.getId() == R.id.id_status ||
                view.getId() == R.id.textView_status){
         //   processLoggedState(view);
            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("TextStatusPage")
                    .setAction("Enter Status Page")
                    .build());
            // [END custom_event]
            Intent statusIntent = new Intent(TextStatus.this, StatusActivity.class);
            startActivityForResult(statusIntent, 3);

        } else if(view.getId() == R.id.button_post_text_status){
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
                                            startActivity(new Intent(TextStatus.this, DiscoverActivity.class));
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
    }





}
