package in.voiceme.app.voiceme.chat;

import android.os.Bundle;
import android.view.View;

import in.voiceme.app.voiceme.ImageZoomLib.FrescoZoomImageView;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.Constants;

public class ImageZoomActivity extends BaseActivity {
    private String mImgUrl = "https://avatars1.githubusercontent.com/u/8758713?v=3&s=460";
    private String imageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        imageURL = getIntent().getStringExtra(Constants.IMAGE_URL);

        toolbar.setNavigationIcon(R.drawable.ic_close_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final FrescoZoomImageView frescoImageView = (FrescoZoomImageView)findViewById(R.id.fiv);
        frescoImageView.loadView(imageURL,R.drawable.background_start);
        // Below just adds new image after clicking
        /*
        frescoImageView.setOnDraweeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frescoImageView.asCircle();
                frescoImageView.loadView("https://avatars0.githubusercontent.com/u/66577?v=3&s=460",R.mipmap.ic_launcher);
            }
        }); */
    }
}
