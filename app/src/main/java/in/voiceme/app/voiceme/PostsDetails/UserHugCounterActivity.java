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
import in.voiceme.app.voiceme.userpost.AudioStatus;
import rx.android.schedulers.AndroidSchedulers;

public class UserHugCounterActivity extends BaseActivity {
    private static final int REQUEST_VIEW_MESSAGE = 1;
    private RecyclerView rv;
    private String likeCounter;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_user_hug_counter);
        getSupportActionBar().setTitle("User Hugs");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        likeCounter = getIntent().getStringExtra(Constants.HUG_FEELING);

        rv = (RecyclerView) findViewById(R.id.counter_hug_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
    }

    private void initializeData() {

        application.getWebService()
                .getInteractionPosts(likeCounter)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<PostSuperUserListModel>() {
                    @Override
                    public void onNext(PostSuperUserListModel response) {
                        showRecycleWithDataFilled(response);
                    }
                    @Override
                    public void onError(Throwable e) {
                        try {
                            Toast.makeText(UserHugCounterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void showRecycleWithDataFilled(final PostSuperUserListModel myList) {
        RVAdapter adapter = new RVAdapter(myList.getHug());
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
