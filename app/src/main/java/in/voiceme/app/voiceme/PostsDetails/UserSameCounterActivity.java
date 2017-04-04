package in.voiceme.app.voiceme.PostsDetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import in.voiceme.app.voiceme.DTO.PostSuperUserListModel;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class UserSameCounterActivity extends BaseActivity {
    private static final int REQUEST_VIEW_MESSAGE = 1;
    private RecyclerView rv;
    private View progressFrame;
    private String likeCounter;


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_user_same_counter);
        getSupportActionBar().setTitle("User Clicked Same");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressFrame = findViewById(R.id.activity_same_progress);
        likeCounter = getIntent().getStringExtra(Constants.SAME_FEELING);

        rv = (RecyclerView) findViewById(R.id.counter_same_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
    }

    private void initializeData() {

        application.getWebService()
                .getInteractionPosts(likeCounter)
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<PostSuperUserListModel>() {
                    @Override
                    public void onNext(PostSuperUserListModel response) {
                        showRecycleWithDataFilled(response);
                        progressFrame.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressFrame.setVisibility(View.GONE);
                        try {
                            Timber.e(e.getMessage());
                     //       Toast.makeText(UserSameCounterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void showRecycleWithDataFilled(final PostSuperUserListModel myList) {
        RVAdapter adapter = new RVAdapter(myList.getSame());
        rv.setAdapter(adapter);
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
