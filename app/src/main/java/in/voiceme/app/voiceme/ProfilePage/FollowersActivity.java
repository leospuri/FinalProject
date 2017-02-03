package in.voiceme.app.voiceme.ProfilePage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.l;
import rx.android.schedulers.AndroidSchedulers;

public class FollowersActivity extends BaseActivity {
    private RecyclerView rv;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);
        userId = getIntent().getStringExtra(Constants.USER_FOLLOW);

        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        getSupportActionBar().setTitle("Followers Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        rv = (RecyclerView) findViewById(R.id.user_follower_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
    }

    private void initializeData() {

        //Todo work on followers user ID
        application.getWebService()
                .getUserFollow(userId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<FollowerUserList>() {
                    @Override
                    public void onNext(FollowerUserList response) {
                        showRecycleWithDataFilled(response);
                    }
                });
    }

    private void showRecycleWithDataFilled(final FollowerUserList myList) {
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
