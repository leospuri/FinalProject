package in.voiceme.app.voiceme.DiscoverPage;

import android.view.View;

import in.voiceme.app.voiceme.DTO.PostsModel;

public interface LikeUnlikeClickListener {
    void onItemClick(PostsModel model, View v);

    void onLikeUnlikeClick(PostsModel model, boolean v);
}
