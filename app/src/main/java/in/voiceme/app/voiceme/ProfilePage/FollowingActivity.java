package in.voiceme.app.voiceme.ProfilePage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import in.voiceme.app.voiceme.DTO.ProfileFollowerUserList;
import in.voiceme.app.voiceme.NotificationsPage.SimpleDividerItemDecoration;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class FollowingActivity extends BaseActivity {
    private static final int REQUEST_VIEW_MESSAGE = 1;
    private String userId;
    private RecyclerView rv;
    private View progressFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        userId = getIntent().getStringExtra(Constants.USER_FOLLOWING);

        getSupportActionBar().setTitle("Following users");
        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressFrame = findViewById(R.id.activity_following_progress);

        rv = (RecyclerView) findViewById(R.id.user_following_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new SimpleDividerItemDecoration(this));
        rv.setHasFixedSize(true);

        initializeData();
    }

    private void initializeData() {

        //Todo work on followers user ID
        application.getWebService()
                .getUserFollowing(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<ProfileFollowerUserList>() {
                    @Override
                    public void onNext(ProfileFollowerUserList response) {
                        showRecycleWithDataFilled(response);
                        progressFrame.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressFrame.setVisibility(View.GONE);
                        try {
                            Timber.e(e.getMessage());
                        //    Toast.makeText(FollowingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void showRecycleWithDataFilled(final ProfileFollowerUserList myList) {
        FollowerDataAdapter adapter = new FollowerDataAdapter(myList.getFollower());
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
