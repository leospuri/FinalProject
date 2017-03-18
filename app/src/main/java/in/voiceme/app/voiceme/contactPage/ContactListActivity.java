package in.voiceme.app.voiceme.contactPage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.List;
import java.util.concurrent.TimeoutException;

import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.ProfilePage.TotalPostsAdapter;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import in.voiceme.app.voiceme.utils.PaginationAdapterCallback;
import in.voiceme.app.voiceme.utils.PaginationScrollListener;
import rx.android.schedulers.AndroidSchedulers;

import static com.facebook.GraphRequest.TAG;

public class ContactListActivity extends BaseContact implements PaginationAdapterCallback {


    private int mPage;
    private RecyclerView recyclerView;
    private TotalPostsAdapter activityInteractionAdapter;
    private String check;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    ProgressBar progressBar;
    LinearLayout errorLayout;
    LinearLayout noPostLayout;
    TextView txtError;
    private AlertDialog.Builder builder1;
    PullRefreshLayout layout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setTitle("Contacts Posts");
        setNavDrawer(new MainNavDrawer(this));

        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        noPostLayout = (LinearLayout) findViewById(R.id.no_post_layout);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        layout = (PullRefreshLayout) findViewById(R.id.contact_swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                        loadNextPage();
                    }
                }, 4000);
            }
        });

  //      if (!secondPage()){
    //        noPostLayout.setVisibility(View.VISIBLE);
      //  } else {
            loadDesign();
//        }


    }

    void loadDesign(){
        noPostLayout.setVisibility(View.INVISIBLE);
        try {
            initUiView();
            loadFirstPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUiView() {
        recyclerView = (RecyclerView) findViewById(R.id.personal_contact_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new PaginationScrollListener(llm) {
            @Override
            protected void loadMoreItems() {
                int totalItemCount = llm.getItemCount();
                isLoading = true;
                currentPage += 1;

                loadNextPage();

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }

    private BroadcastReceiver onNotice=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
         //   showNotification(); Remove still syncing
            setSecondPage("Contact Page Added");
            loadDesign();

        }
    };

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter=new IntentFilter(ContactService.BROADCAST);

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, filter);
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void showNoPost() {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchNoMessage());
        }
    }

    private String fetchNoMessage() {
        String errorMsg;

        if (isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.msg_no_contacts);
        } else  {
           errorMsg = getResources().getString(R.string.error_msg_timeout);
          //  startActivity(new Intent(this, OfflineActivity.class));

        }

        return errorMsg;
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        //    startActivity(new Intent(this, OfflineActivity.class));
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private void loadFirstPage() throws Exception {
        Log.d(TAG, "loadFirstPage: ");
        hideErrorView();

        application.getWebService()
                .getContactPosts(MySharedPreferences.getUserId(preferences), MySharedPreferences.getUserId(preferences), "true", currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        progressBar.setVisibility(View.GONE);

                        hideErrorView();
                        Log.e("RESPONSE:::", "Size===" + response.size());
                        //         List<PostsModel> body = (List<PostsModel>) response.get(0).body();

                        //   List<PostsModel> model = fetchResults(response);
                        //   showRecycleWithDataFilled(response);
                        showRecycleWithDataFilled(response);


                        //   showRecycleWithDataFilled(response);
                        //   latestListAdapter.addAll(myModelList);
                        if (response.size() < 25){
                            isLastPage = true;
                        } else if (currentPage <= TOTAL_PAGES ) activityInteractionAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        showErrorView(e);
                    }
                });
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        hideErrorView();

        application.getWebService()
                .getContactPosts(MySharedPreferences.getUserId(preferences), MySharedPreferences.getUserId(preferences), "true", currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3,2000))
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        hideErrorView();
                        activityInteractionAdapter.removeLoadingFooter();
                        isLoading = false;

                        if(response.size() < 25){
                            isLastPage = true;
                        }

                        Log.e("RESPONSE:::", "Size===" + response.size());
                        activityInteractionAdapter.addAll(response);
                        // showRecycleWithDataFilled(response);
                        if (currentPage != TOTAL_PAGES) activityInteractionAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        activityInteractionAdapter.showRetry(true, fetchErrorMessage(e));
                    }
                });

    }


    private void showRecycleWithDataFilled(final List<PostsModel> myList) {
        activityInteractionAdapter = new TotalPostsAdapter(myList, this);
   /*     activityInteractionAdapter.setOnItemClickListener(new LikeUnlikeClickListener() {
            @Override
            public void onItemClick(PostsModel model, View v) {
                String name = model.getIdUserName();
            }

            @Override
            public void onLikeUnlikeClick(PostsModel model, LikeButton v) {

            }
        }); */
        recyclerView.setAdapter(activityInteractionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.refresh_new_contact) {
         //   startActivity(new Intent(this, ChangeProfileActivity.class));
            Toast.makeText(this, "clicked refresh new contacts", Toast.LENGTH_SHORT).show();
            readContacts();
            return true;
        } else if (itemId == R.id.more_info){
            dialogBox();
            return true;
        }

        return false;
    }

    private void readContacts() {
        ActivityUtils.isContactsPermission(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == getResources().getInteger(R.integer.contacts_request)) {
                contactMethod();
            }
        }
    }

    public void contactMethod(){
        startService(new Intent(this, ContactService.class));
        Toast.makeText(this, "Contacts are getting refreshed.", Toast.LENGTH_SHORT).show();
    }

    public void dialogBox(){
        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("All your contacts are used only for getting anonymous posts from your friends. " +
                "We donot share you numbers with third party.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

     /*   builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }); */

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
    public void retryPageLoad() {
        try {
            loadFirstPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
