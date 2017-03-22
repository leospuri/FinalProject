package in.voiceme.app.voiceme.userpost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.voiceme.app.voiceme.DTO.ReportResponse;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class EditPost extends BaseActivity implements View.OnClickListener {
    TextView previous_status;
    EditText edit_for_status;
    Button update_status;
    Button delete_status;
    Button delete_audio;
    private String postID;
    private String postText;
    private String audioLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Edit Post");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        postID = getIntent().getStringExtra(Constants.IDPOST);
        postText = getIntent().getStringExtra(Constants.STATUS_POST);
        audioLink = getIntent().getStringExtra(Constants.AUDIO);

        previous_status = (TextView) findViewById(R.id.previous_status);
        edit_for_status = (EditText) findViewById(R.id.edit_for_status);
        delete_audio = (Button) findViewById(R.id.delete_audio);
        update_status = (Button) findViewById(R.id.update_status);
        delete_status = (Button) findViewById(R.id.delete_status);


        delete_audio.setOnClickListener(this);
        update_status.setOnClickListener(this);
        delete_status.setOnClickListener(this);

        previous_status.setText(postText);
        edit_for_status.setText(postText);
        if (audioLink == null){
                delete_audio.setVisibility(View.GONE);
        } else {
            delete_audio.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delete_audio){
            deleteAudio(postID, "remove_audio");
            delete_audio.setVisibility(View.GONE);
            Toast.makeText(this, "Audio has been removed successfully", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.update_status){
            updateStatus(postID, "text_status", edit_for_status.getText().toString());
            Toast.makeText(this, "Status has been updated successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DiscoverActivity.class));
        } else if (view.getId() == R.id.delete_status){
            deleteAudio(postID, "delete");
            Toast.makeText(this, "Status has been deleted successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DiscoverActivity.class));
        }
    }

    protected void deleteAudio(String postID, String action) {
        application.getWebService()
                .deletePost(postID, action)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<ReportResponse>() {
                    @Override
                    public void onNext(ReportResponse response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Timber.d("Message from server" + response);
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Toast.makeText(EditPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    protected void updateStatus(String postID, String action, String text_status) {
        application.getWebService()
                .updatePost(postID, action, text_status)
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ReportResponse>() {
                    @Override
                    public void onNext(ReportResponse response) {
                        Timber.d("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        // Toast.makeText(ChangeProfileActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                        Timber.d("Message from server" + response);
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Toast.makeText(EditPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }
}
