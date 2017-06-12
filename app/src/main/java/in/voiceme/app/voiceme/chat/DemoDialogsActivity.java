package in.voiceme.app.voiceme.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import in.voiceme.app.voiceme.DTO.ChatDialogPojo;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

/**
 * Created by harishpc on 6/1/2017.
 */
public abstract class DemoDialogsActivity extends BaseActivity {

    protected ImageLoader imageLoader;
    protected DialogsListAdapter<ChatDialogPojo> dialogsListAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                //If you using another library - write here your way to load image
                //   Picasso.with(DialogDetailsActivity.this).load(url).placeholder(getResources().getDrawable(R.drawable.user)).error(getResources().getDrawable(R.drawable.user)).into(imageView);

                if (url != null){
                    if (url.isEmpty()){
                        Picasso.with(imageView.getContext()).load(R.drawable.user).into(imageView);
                    } else {
                        Picasso.with(imageView.getContext()).load(url).into(imageView);
                    }
                } else {
                    Picasso.with(imageView.getContext()).load(url).into(imageView);
                }


            }
        };
    }
}
