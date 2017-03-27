package in.voiceme.app.voiceme.PostsDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;
import java.util.concurrent.TimeoutException;

import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.ProfilePage.TotalPostsAdapter;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.userpost.AudioStatus;
import in.voiceme.app.voiceme.userpost.TextStatus;
import in.voiceme.app.voiceme.utils.PaginationAdapterCallback;
import in.voiceme.app.voiceme.utils.PaginationScrollListener;
import rx.android.schedulers.AndroidSchedulers;

import static com.facebook.GraphRequest.TAG;

public class UserCategoryActivity extends BaseActivity implements PaginationAdapterCallback, View.OnClickListener {

    private int mPage;
    private RecyclerView recyclerView;
    private TotalPostsAdapter activityInteractionAdapter;

    private String categoryId;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    ProgressBar progressBar;
    LinearLayout errorLayout;
    TextView txtError;


    FloatingActionButton textStatus;
    FloatingActionButton audioStatus;
    FloatingActionsMenu rightLabels;
    private View progressFrame;

    PullRefreshLayout layout;

    private String currentCategoryID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);

        getSupportActionBar().setTitle("Category Posts");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressFrame = findViewById(R.id.activity_user_category_progress);
        recyclerView = (RecyclerView) findViewById(R.id.user_category_recyclerview);

        categoryId = getIntent().getStringExtra(Constants.CATEGORY);
        setFeeling(categoryId);

  /*      if (categoryId.equals(family)) {
            setFeeling("1");
        } else if (categoryId.equals(health)) {
            setFeeling("2");
        } else if (categoryId.equals(work)) {
            setFeeling("3");
        } else if (categoryId.equals(social)) {
            setFeeling("4");
        } else if (categoryId.equals(others)) {
            setFeeling("5");
        } */

        rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        textStatus = (FloatingActionButton) findViewById(R.id.action_a);
        audioStatus = (FloatingActionButton) findViewById(R.id.action_b);

        textStatus.setOnClickListener(this);
        audioStatus.setOnClickListener(this);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        layout = (PullRefreshLayout) findViewById(R.id.category_swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                    }
                }, 4000);
            }
        });


        try {
            initUiView();
            loadFirstPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUiView() {
        recyclerView = (RecyclerView) findViewById(R.id.user_category_recyclerview);

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

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
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
      //      startActivity(new Intent(this, OfflineActivity.class));
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private void loadFirstPage() throws Exception {
        Log.d(TAG, "loadFirstPage: ");
        hideErrorView();

        application.getWebService()
                .getCategoryPosts(currentCategoryID, MySharedPreferences.getUserId(preferences), currentPage)
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
                        progressFrame.setVisibility(View.GONE);


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
                .getCategoryPosts(currentCategoryID, MySharedPreferences.getUserId(preferences), currentPage)
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


    private void setFeeling(String feelingID) {
        this.currentCategoryID = feelingID;
    }

    @Override
    public String toString() {
        return "documentary";
    }


    private void showRecycleWithDataFilled(final List<PostsModel> myList) {
        activityInteractionAdapter = new TotalPostsAdapter(myList, this);
    /*    activityInteractionAdapter.setOnItemClickListener(new LikeUnlikeClickListener() {
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
            return true;
        }
        return false;

    }

    @Override
    public void retryPageLoad() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.action_a) {
            processLoggedState(view);
            startActivity(new Intent(UserCategoryActivity.this, TextStatus.class));
            rightLabels.toggle();
        } else if (view.getId() == R.id.action_b) {
            processLoggedState(view);
            startActivity(new Intent(UserCategoryActivity.this, AudioStatus.class));
            rightLabels.toggle();
        }
    }
}
