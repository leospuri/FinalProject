package in.voiceme.app.voiceme.ActivityPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jetradar.desertplaceholder.DesertPlaceholder;

import in.voiceme.app.voiceme.DiscoverPage.DiscoverActivity;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class OfflineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        DesertPlaceholder desertPlaceholder = (DesertPlaceholder) findViewById(R.id.placeholder);
        desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkConnected()){
                    startActivity(new Intent(OfflineActivity.this, DiscoverActivity.class));
                } else {
                    Toast.makeText(OfflineActivity.this, "No Connected Detected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
