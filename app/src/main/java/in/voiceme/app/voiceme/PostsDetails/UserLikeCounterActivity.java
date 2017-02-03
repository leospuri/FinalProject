package in.voiceme.app.voiceme.PostsDetails;

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

public class UserLikeCounterActivity extends BaseActivity {
    private static final int REQUEST_VIEW_MESSAGE = 1;
    private RecyclerView rv;
    private String likeCounter;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_user_like_counter);
        getSupportActionBar().setTitle("Like user");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCounter = null;
                finish();
            }
        });

        likeCounter = getIntent().getStringExtra(Constants.LIKE_FEELING);

        rv = (RecyclerView) findViewById(R.id.counter_like_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();

    }


    private void initializeData() {

        application.getWebService()
                .getInteractionPosts(likeCounter)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<UserSuperList>() {
                    @Override
                    public void onNext(UserSuperList response) {
                        showRecycleWithDataFilled(response);
                    }
                });
    }

    private void showRecycleWithDataFilled(final UserSuperList myList) {
        RVAdapter adapter = new RVAdapter(myList.getLikes());
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
