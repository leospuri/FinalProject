package in.voiceme.app.voiceme.ProfilePage;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import in.voiceme.app.voiceme.DTO.FollowerUserModel;
import in.voiceme.app.voiceme.R;
import timber.log.Timber;

/**
 * Created by harish on 1/15/2017.
 */

public class FollowerCardViewHolder extends RecyclerView.ViewHolder {
    protected FollowerUserModel dataItem;
    TextView personName;
    SimpleDraweeView personPhoto;

    public FollowerCardViewHolder(View itemView) {
        super(itemView);
        personName = (TextView) itemView.findViewById(R.id.person_name);
        personPhoto = (SimpleDraweeView) itemView.findViewById(R.id.person_photo);

        personName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameClicked(view);
            }
        });

        personPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userProfileClicked(view);
            }
        });

    }

    protected void userNameClicked(View view) {

    }

    protected void userProfileClicked(View view) {

    }


    public void bind(FollowerUserModel dataItem) {
        this.dataItem = dataItem;

        if (dataItem.getName() == null){
            Timber.d("*********************** data ITEM is null ******************");
        }
        personName.setText(dataItem.getName());

        personPhoto.setImageURI(dataItem.getAvatarPics());
    }
}
