package in.voiceme.app.voiceme.ActivityPage;

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

import java.util.List;
import java.util.concurrent.TimeoutException;

import in.voiceme.app.voiceme.DTO.PostsModel;
import in.voiceme.app.voiceme.DiscoverPage.LatestListAdapter;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseFragment;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.utils.PaginationAdapterCallback;
import in.voiceme.app.voiceme.utils.PaginationScrollListener;
import rx.android.schedulers.AndroidSchedulers;

import static com.facebook.GraphRequest.TAG;

public class ActivityYourFeedFragment extends BaseFragment implements PaginationAdapterCallback {

    public static final String ARG_YOUR_FEED_PAGE = "ARG_INTERACTION_PAGE";

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private int currentResults;

    private int mPage;
    private RecyclerView recyclerView;
    private LatestListAdapter latestListAdapter;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    TextView txtError;



    public ActivityYourFeedFragment() {
        // Required empty public constructor
    }

    public static ActivityYourFeedFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_YOUR_FEED_PAGE, page);
        ActivityYourFeedFragment fragment2 = new ActivityYourFeedFragment();
        fragment2.setArguments(args);
        return fragment2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_YOUR_FEED_PAGE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_your_feed, container, false);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_main_feed_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.addOnScrollListener(new PaginationScrollListener(new LinearLayoutManager(this.getActivity())) {
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

    private void loadFirstPage() throws Exception {
        Log.d(TAG, "loadFirstPage: ");
        hideErrorView();

        application.getWebService()
                .getUserFollowerPost(MySharedPreferences.getUserId(preferences),
                        MySharedPreferences.getUserId(preferences),
                        currentPage)
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
                        } else if (currentPage <= TOTAL_PAGES ) latestListAdapter.addLoadingFooter();
                        else isLastPage = true;

                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        showErrorView(e);
                    }
                });
    }

    /*
    private List<PostsModel> fetchResults(List<PostsModel> response) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<PostsModel>>(){}.getType();
        //    List<PostsModel> posts = (List<PostsModel>) gson.fromJson(response, listType);
        List<PostsModel> myModelList = gson.fromJson(response.toString(), listType);

        return myModelList;
    } */

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        hideErrorView();

        application.getWebService()
                .getUserFollowerPost(MySharedPreferences.getUserId(preferences),
                        MySharedPreferences.getUserId(preferences),
                        currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<PostsModel>>() {
                    @Override
                    public void onNext(List<PostsModel> response) {
                        hideErrorView();
                        latestListAdapter.removeLoadingFooter();
                        isLoading = false;

                     //   List<PostsModel> body = (List<PostsModel>) response.get(0).body();

                   //     Type listType = new TypeToken<List<PostsModel>>(){}.getType();
                        //    List<PostsModel> posts = (List<PostsModel>) gson.fromJson(response, listType);
                     //   List<PostsModel> myModelList = gson.fromJson(response.toString(), listType);


                       // List<PostsModel> model = fetchResults(response);
                       //    showRecycleWithDataFilled(response);

                        latestListAdapter.addAll(response);

                      //  showRecycleWithDataFilled(response);
                        if (response.size() < 25){
                            isLastPage = true;
                        } else if (currentPage <= TOTAL_PAGES ) latestListAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }
                    @Override
                    public void onError(Throwable e){
                        e.printStackTrace();
                        latestListAdapter.showRetry(true, fetchErrorMessage(e));
                    }
                });

    }



    @Override
    public String toString() {
        return "documentary";
    }


    private void showRecycleWithDataFilled(final List<PostsModel> myList) {
        //latestListAdapter = new LatestListAdapter(getActivity());
        //latestListAdapter.addAll(myList);

        latestListAdapter = new LatestListAdapter(myList, getActivity());

    /*    latestListAdapter.setOnItemClickListener(new LikeUnlikeClickListener() {
            @Override
            public void onItemClick(PostsModel model, View v) {
                String name = model.getIdUserName();
            }

            @Override
            public void onLikeUnlikeClick(PostsModel model, LikeButton v) {

            }
        }); */
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