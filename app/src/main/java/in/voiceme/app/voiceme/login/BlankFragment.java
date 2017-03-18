package in.voiceme.app.voiceme.login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends BaseFragment {
    private Button skip;
    private Button register;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        skip = (Button) view.findViewById(R.id.try_it_btn);
        register = (Button) view.findViewById(R.id.activity_login_register);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryDemoOnClick(view);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IntroActivity.class));
            }
        });

        return view;

    }

    public void tryDemoOnClick(View viewPrm) {

        // [END custom_event]
        SharedPreferences prefsLcl = application.getSharedPreferences("Logged in or not", MODE_PRIVATE);
        prefsLcl.edit().putBoolean("is this demo mode", true).apply();
        startActivity(new Intent(getActivity(), AnonymousLogin.class));
        getActivity().finish();
    }


    @Override
    public boolean processLoggedState(View viewPrm) {
        return false;
    }
}
