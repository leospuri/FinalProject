package in.voiceme.app.voiceme.contactPage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.like.LikeButton;

import java.util.List;

import in.voiceme.app.voiceme.DiscoverPage.LikeUnlikeClickListener;
import in.voiceme.app.voiceme.ProfilePage.TotalPostsAdapter;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.PostsModel;
import rx.android.schedulers.AndroidSchedulers;

public class ContactListActivity extends BaseContact {


    private int mPage;
    private RecyclerView recyclerView;
    private TotalPostsAdapter activityInteractionAdapter;
    private String check;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setTitle("Contacts Posts");
        setNavDrawer(new MainNavDrawer(this));

        recyclerView = (RecyclerView) findViewById(R.id.personal_contact_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "documentary";
    }

    private void getData() throws Exception {
        ((VoicemeApplication) getApplication()).getWebService()
                .getContactPosts(MySharedPreferences.getUserId(preferences), MySharedPreferences.getUserId(preferences), "true")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        Log.e("RESPONSE:::", "Size===" + response.size());
                        showRecycleWithDataFilled(response);
                    }
                });
    }

    private void showRecycleWithDataFilled(final List<PostsModel> myList) {
        activityInteractionAdapter = new TotalPostsAdapter(myList, this);
        activityInteractionAdapter.setOnItemClickListener(new LikeUnlikeClickListener() {
            @Override
            public void onItemClick(PostsModel model, View v) {
                String name = model.getIdUserName();
            }

            @Override
            public void onLikeUnlikeClick(PostsModel model, LikeButton v) {

            }
        });
        recyclerView.setAdapter(activityInteractionAdapter);
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

//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
