package in.voiceme.app.voiceme.contactPage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeoutException;

import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.ProfilePage.TotalPostsAdapter;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
        getSupportActionBar().setTitle("Contacts Posts");
        setNavDrawer(new MainNavDrawer(this));

        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        noPostLayout = (LinearLayout) findViewById(R.id.no_post_layout);
        txtError = (TextView) findViewById(R.id.error_txt_cause);


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
        }

        return errorMsg;
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
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
