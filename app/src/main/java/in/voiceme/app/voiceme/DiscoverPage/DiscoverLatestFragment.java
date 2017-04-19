package in.voiceme.app.voiceme.DiscoverPage;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;

import java.util.List;
import java.util.concurrent.TimeoutException;

import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.WasLoggedInInterface;
import in.voiceme.app.voiceme.infrastructure.BaseFragment;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.RetryWithDelay;
import in.voiceme.app.voiceme.utils.PaginationAdapterCallback;
import in.voiceme.app.voiceme.utils.PaginationScrollListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.facebook.GraphRequest.TAG;


public class DiscoverLatestFragment extends BaseFragment implements WasLoggedInInterface, PaginationAdapterCallback {
    public static final String ARG_LATEST_PAGE = "ARG_LATEST_PAGE";

    private int mPage;

   // private LatestListAdapter latestListAdapter;
    private RecyclerView recyclerView;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private View progressFrame;

    ProgressBar progressBar;
    Button error_btn_retry;
    LinearLayout errorLayout;
    LinearLayout no_post_layout;
    TextView txtError;
    TextView no_post_textview;
    private LatestListAdapter latestListAdapter;
    PullRefreshLayout layout;


    public DiscoverLatestFragment() {
        // Required empty public constructor
    }

    public static DiscoverLatestFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_LATEST_PAGE, page);
        DiscoverLatestFragment fragment = new DiscoverLatestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_LATEST_PAGE);
        latestListAdapter = new LatestListAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover_latest, container, false);
        progressFrame = view.findViewById(R.id.activity_discover_latest);
        error_btn_retry = (Button) view.findViewById(R.id.error_btn_retry);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        no_post_layout = (LinearLayout) view.findViewById(R.id.no_post_layout);
        no_post_textview = (TextView) view.findViewById(R.id.no_post_textview);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        layout = (PullRefreshLayout) view.findViewById(R.id.discover_latest_swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_SMARTISAN);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                        currentPage = PAGE_START;
                        try {
                            loadFirstPage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 4000);
            }
        });

        try {
            initUiView(view);
            loadFirstPage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        error_btn_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    loadFirstPage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //recyclerview
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_discover_recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        recyclerView.setAdapter(new MyRecyclerAdapter(this.getActivity(), getDiscoverLatest()));
        return view;
    }

    private void initUiView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_discover_recyclerview);
      //  LinearLayoutManager linearLayout = new LinearLayoutManager(this.getActivity());
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                int totalItemCount = linearLayoutManager.getItemCount();
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void showEmptyView() {
        if (no_post_layout.getVisibility() == View.GONE) {
            no_post_layout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            no_post_textview.setText("There are no Latest Posts");
        }
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
                .getLatestFeed(MySharedPreferences.getUserId(preferences), currentPage)
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        progressBar.setVisibility(View.GONE);
                        progressFrame.setVisibility(View.GONE);

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
                        } else if (currentPage <= TOTAL_PAGES ) latestListAdapter.addLoadingFooter();
                        else isLastPage = true;

                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        progressFrame.setVisibility(View.GONE);
                        showErrorView(e);
                    }
                });
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        hideErrorView();

        application.getWebService()
                .getLatestFeed(MySharedPreferences.getUserId(preferences), currentPage)
                .retryWhen(new RetryWithDelay(3,2000))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        hideErrorView();
                        latestListAdapter.addAll(response);
                        latestListAdapter.removeLoadingFooter();
                        isLoading = false;

                        if(response.size() < 25){
                            isLastPage = true;
                        }

                        Log.e("RESPONSE:::", "Size===" + response.size());

                       // showRecycleWithDataFilled(response);
                        if (currentPage != TOTAL_PAGES) latestListAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        progressFrame.setVisibility(View.GONE);
                        Timber.e("error is: " + e);
                        latestListAdapter.showRetry(true, fetchErrorMessage(e));
                    }
                });

    }

    @Override
    public String toString() {
        return "documentary";
    }

    private void showRecycleWithDataFilled(final List<PostsModel> myList) {
        latestListAdapter = new LatestListAdapter(myList, getActivity());
        recyclerView.setAdapter(latestListAdapter);
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
