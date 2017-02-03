package in.voiceme.app.voiceme.DiscoverPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;
import in.voiceme.app.voiceme.userpost.AudioStatus;
import in.voiceme.app.voiceme.userpost.TextStatus;

public class DiscoverActivity extends BaseActivity {

    private static final int REQUEST_VIEW_MESSAGE = 1;
    FloatingActionButton textStatus;
    FloatingActionButton audioStatus;
    FloatingActionsMenu rightLabels;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_discover);
        getSupportActionBar().setTitle("Discover");
        setNavDrawer(new MainNavDrawer(this));

        textStatus = (FloatingActionButton) findViewById(R.id.action_a);
        audioStatus = (FloatingActionButton) findViewById(R.id.action_b);
        rightLabels = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        // ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        // viewPager.setAdapter(new DiscoverActivityFragmentPagerAdapter(getSupportFragmentManager()));

        textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Toast.makeText(DiscoverActivity.this, "Button 01", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DiscoverActivity.this, TextStatus.class));
                rightLabels.toggle();

            }
        });


        audioStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLoggedState(view);
                Toast.makeText(DiscoverActivity.this, "button 02", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DiscoverActivity.this, AudioStatus.class));
                rightLabels.toggle();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_discover_viewpager);
        this.addPages(viewPager);

        // Give the PagerSlidingTabStrip the ViewPager
        SmartTabLayout tabsStrip = (SmartTabLayout) findViewById(R.id.activity_discover_tab_layout);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);
    }

    //add all pages
    private void addPages(ViewPager pager) {
        DiscoverActivityFragmentPagerAdapter adapter =
                new DiscoverActivityFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addPage(new DiscoverLatestFragment());
        adapter.addPage(new DiscoverPopularFragment());
        adapter.addPage(new DiscoverTrendingFragment());

        //set adapter to pager
        pager.setAdapter(adapter);
    }

//  @Override
//    public boolean processLoggedState(View viewPrm) {          if(this.mBaseLoginClass.isDemoMode(viewPrm))                      {Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show(); return true;} return false;
//
//  }
}
