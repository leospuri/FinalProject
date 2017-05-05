package in.voiceme.app.voiceme.NotificationsPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.voiceme.app.voiceme.DTO.PostSuperUserListModel;
import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.PostsDetails.RVAdapter;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class NotificationsActivity extends BaseActivity {
    private static final int REQUEST_VIEW_MESSAGE = 1;
    private RecyclerView rv;
    private View progressFrame;

    private ArrayList data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_home);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Notifications");
        setNavDrawer(new MainNavDrawer(this));

        progressFrame = findViewById(R.id.activity_notification_progress);

        rv = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
    }

    private void initializeData() {

        application.getWebService()
                .getNotificationPosts(MySharedPreferences.getUserId(preferences), "1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<List<NotificationPojo>>() {
                    @Override
                    public void onNext(List<NotificationPojo> response) {
                        showRecycleWithDataFilled(response);
                        progressFrame.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressFrame.setVisibility(View.GONE);
                        try {
                            Timber.e(e.getMessage());
                            //      Toast.makeText(UserHugCounterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
    }

    private void showRecycleWithDataFilled(final List<NotificationPojo> myList) {
        NotificationAdapter adapter = new NotificationAdapter(myList);
        rv.setAdapter(adapter);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent myActivityIntent = new Intent(NotificationsActivity.this, DiscoverActivity.class);
        startActivity(myActivityIntent);
        finish();
    }
}
