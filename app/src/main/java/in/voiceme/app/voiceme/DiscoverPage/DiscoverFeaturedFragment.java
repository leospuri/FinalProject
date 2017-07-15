package in.voiceme.app.voiceme.DiscoverPage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.voiceme.app.voiceme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFeaturedFragment extends Fragment {


    public DiscoverFeaturedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover_featured, container, false);
    }

}
