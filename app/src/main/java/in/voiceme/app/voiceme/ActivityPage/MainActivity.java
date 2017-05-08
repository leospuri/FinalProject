package in.voiceme.app.voiceme.ActivityPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.analytics.HitBuilders;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.userpost.AudioStatus;
import in.voiceme.app.voiceme.userpost.TextStatus;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    FloatingActionButton textStatus;
    FloatingActionButton audioStatus;
    FloatingActionsMenu rightLabels;
    private static ImageView play_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        checkAuthStatus();


        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Activity Page");
        setNavDrawer(new MainNavDrawer(this));

        mTracker = application.getDefaultTracker();

        rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        textStatus = (FloatingActionButton) findViewById(R.id.action_a);
        audioStatus = (FloatingActionButton) findViewById(R.id.action_b);
        play_button = (ImageView) findViewById(R.id.list_item_posts_play_button);

        textStatus.setOnClickListener(this);

        audioStatus.setOnClickListener(this);

        /**
         * Initialize Facebook SDK
         */




        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_main_activity);
        this.addPages(viewPager);

        // Give the PagerSlidingTabStrip the ViewPager
        SmartTabLayout tabsStrip = (SmartTabLayout) findViewById(R.id.tabs_main_activity);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);

    }



    //add all pages
    private void addPages(ViewPager pager) {
        MainActivityFragmentPagerAdapter adapter = new MainActivityFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new ActivityYourFeedFragment());
        adapter.addPage(new ActivityInteractionFragment());

        //set adapter to pager
        pager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_text) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    */





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
    public void onClick(View view) {
        if (view.getId() == R.id.action_a){
            processLoggedState(view);

            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("PostStatus")
                    .setAction("Text Post Clicked")
                    .build());
            // [END custom_event]

            startActivity(new Intent(MainActivity.this, TextStatus.class));
            rightLabels.toggle();
        } else if (view.getId() == R.id.action_b){
            processLoggedState(view);

            // [START custom_event]
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("PostStatus")
                    .setAction("Audio Post Clicked")
                    .build());
            // [END custom_event]


            startActivity(new Intent(MainActivity.this, AudioStatus.class));
            rightLabels.toggle();
        }
    }
}
