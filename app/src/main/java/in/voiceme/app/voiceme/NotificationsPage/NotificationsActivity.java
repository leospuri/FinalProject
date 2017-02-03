package in.voiceme.app.voiceme.NotificationsPage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import io.realm.Realm;
import io.realm.RealmChangeListener;


public class NotificationsActivity extends BaseActivity {
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    public static final int REQUEST_IMAGE_DELETED = 100;
    public static final String RESULT_EXTRA_MESSAGE_ID = "RESULT_EXTRA_MESSAGE_ID";

    private NotificationRecyclerAdapter recyclerAdapter;
    private ArrayList data = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_home);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Notifications");
        setNavDrawer(new MainNavDrawer(this));

        initUI();

        this.data = getData();

        recyclerAdapter.swap(data);

    }

//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }

    private void initUI() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerAdapter = new NotificationRecyclerAdapter(data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private ArrayList getData() {

        realm = Realm.getDefaultInstance();

        RealmChangeListener realmListener = element -> refreshData();

        realm.addChangeListener(realmListener);

        ArrayList posts = new ArrayList<>(realm.where(NotificationPost.class).findAll());
        Collections.reverse(posts);
        return posts;
    }

    private void refreshData() {
        this.data.clear();
        this.data = new ArrayList<>(realm.where(NotificationPost.class).findAll());
        Collections.reverse(data);
        recyclerAdapter.swap(data);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        realm.removeAllChangeListeners();
    }
}
