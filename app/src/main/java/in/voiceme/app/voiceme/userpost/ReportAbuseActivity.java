package in.voiceme.app.voiceme.userpost;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.voiceme.app.voiceme.DTO.ReportResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class ReportAbuseActivity extends BaseActivity implements View.OnClickListener {
    private EditText report_abuse_reason;
    private Button submitProblem;
    private TextView abuse_previous_status;
    private String id_username;
    private String post_text;
    private String current_problem = null;
    private String id_posts;
    private RadioGroup radio_group_report;
    private RadioButton report_other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_abuse);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Report Abuse Page");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        Intent intent = getIntent();
        id_username = intent.getStringExtra(Constants.IDUSERNAME);
        id_posts = intent.getStringExtra(Constants.IDPOST);
        post_text = intent.getStringExtra(Constants.STATUS_POST);

        report_abuse_reason = (EditText) findViewById(R.id.report_abuse_reason);
        abuse_previous_status = (TextView) findViewById(R.id.abuse_previous_status);
        submitProblem = (Button) findViewById(R.id.submit_report);
        if (report_abuse_reason.getVisibility() == View.VISIBLE){
            report_abuse_reason.setVisibility(View.GONE);
        }

        radio_group_report = (RadioGroup) findViewById(R.id.radio_group_report);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });


        report_other = (RadioButton) findViewById(R.id.report_other);
        radio_group_report.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id=group.getCheckedRadioButtonId();
                RadioButton rb=(RadioButton) findViewById(id);

                String radioText=rb.getText().toString();
                current_problem = radioText;
                if (rb == report_other){
                    report_abuse_reason.setVisibility(View.VISIBLE);
                }

            }
        });

        report_abuse_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                current_problem = report_abuse_reason.getText().toString();
            }
        });
        abuse_previous_status.setText(String.valueOf("**" + " " + post_text + " " + "**"));
        submitProblem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_report){
            processLoggedState(view);
                try {
                    application.getWebService()
                            .reportAbuse(id_username, MySharedPreferences.getUserId(preferences), id_posts, current_problem)
                            .observeOn(AndroidSchedulers.mainThread())
                            .retryWhen(new RetryWithDelay(3,2000))
                            .subscribe(new BaseSubscriber<ReportResponse>() {
                                @Override
                                public void onNext(ReportResponse userResponse) {
                                    Timber.e("UserResponse " + userResponse.getSuccess());
                                    if (userResponse.getSuccess()) {
                                        Toast.makeText(ReportAbuseActivity.this, "Successfully posted message to our team", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ReportAbuseActivity.this, DiscoverActivity.class));
                                    }
                                }
                                @Override
                                public void onError(Throwable e) {
                                    try {
                                        Toast.makeText(ReportAbuseActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;

    }
}
