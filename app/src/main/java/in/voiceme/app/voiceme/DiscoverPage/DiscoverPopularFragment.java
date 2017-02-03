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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;

import java.util.List;
import java.util.concurrent.TimeoutException;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseFragment;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.services.PostsModel;
import in.voiceme.app.voiceme.utils.PaginationAdapterCallback;
import in.voiceme.app.voiceme.utils.PaginationScrollListener;
import rx.android.schedulers.AndroidSchedulers;

import static com.facebook.GraphRequest.TAG;


/**
 * A simple {@link BaseFragment} subclass.
 */
public class DiscoverPopularFragment extends BaseFragment implements PaginationAdapterCallback {
    public static final String ARG_POPULAR_PAGE = "ARG_YOUR_FEED_PAGE";

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    TextView txtError;

    private int mPage;
    private RecyclerView recyclerView;
    private LatestListAdapter latestListAdapter;

    public DiscoverPopularFragment() {
        // Required empty public constructor
    }

    public static DiscoverPopularFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_POPULAR_PAGE, page);
        DiscoverPopularFragment fragment2 = new DiscoverPopularFragment();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_POPULAR_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover_popular, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);
        try {
            initUiView(view);
            loadFirstPage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void initUiView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_discover_popular_recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
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

    @Override
    public String toString() {
        return "documentary";
    }

    private void loadFirstPage() throws Exception {
        Log.d(TAG, "loadFirstPage: ");
        hideErrorView();

        application.getWebService()
                .getPopulars(MySharedPreferences.getUserId(preferences),"true", currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        progressBar.setVisibility(View.GONE);
                        hideErrorView();
                        Log.e("RESPONSE:::", "Size===" + response.size());
                        showRecycleWithDataFilled(response);
                        if (currentPage <= TOTAL_PAGES) latestListAdapter.addLoadingFooter();
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
                .getPopulars(MySharedPreferences.getUserId(preferences),"true", currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        hideErrorView();
                        latestListAdapter.removeLoadingFooter();
                        isLoading = false;


                        Log.e("RESPONSE:::", "Size===" + response.size());
                        showRecycleWithDataFilled(response);
                        if (currentPage != TOTAL_PAGES) latestListAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        latestListAdapter.showRetry(true, fetchErrorMessage(e));
                    }
                });

    }


    private void showRecycleWithDataFilled(final List<PostsModel> myList) {
        latestListAdapter = new LatestListAdapter(myList, getActivity());
        latestListAdapter.setOnItemClickListener(new LikeUnlikeClickListener() {
            @Override
            public void onItemClick(PostsModel model, View v) {
                String name = model.getIdUserName();
            }

            @Override
            public void onLikeUnlikeClick(PostsModel model, LikeButton v) {

            }
        });
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
        loadNextPage();
    }
}
