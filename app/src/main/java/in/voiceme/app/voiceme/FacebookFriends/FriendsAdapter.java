package in.voiceme.app.voiceme.FacebookFriends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import in.voiceme.app.voiceme.R;

public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public final int ITEM_TYPE_FRIEND = 0;
    public final int ITEM_TYPE_LOAD = 1;

    private ArrayList<FriendItemData> friendsList;
    private Context context;

    /**
     * @param friendsList
     */
    public FriendsAdapter(ArrayList<FriendItemData> friendsList) {
        this.friendsList = friendsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_FRIEND) {
            return new FriendViewHolder(inflater.inflate(R.layout.item_friend, parent, false));
        } else {
            return new LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(getItemViewType(position) == ITEM_TYPE_FRIEND) {
            FriendViewHolder friendViewHolder = ((FriendViewHolder)viewHolder);
            String friendName = friendsList.get(position).getName();
            friendViewHolder.tvUserName.setText(position + "  " + friendName);

            Animation anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (friendsList.get(position) != null) {
            return ITEM_TYPE_FRIEND;
        } else {
            return ITEM_TYPE_LOAD;
        }
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {

        public TextView tvUserName;

        public FriendViewHolder(View v) {
            super(v);
            tvUserName = (TextView) v.findViewById(R.id.tv_UserName);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar pbLoading;

        public LoadingViewHolder(View v) {
            super(v);
            pbLoading = (ProgressBar) v.findViewById(R.id.pb_loading);
        }
    }
}
