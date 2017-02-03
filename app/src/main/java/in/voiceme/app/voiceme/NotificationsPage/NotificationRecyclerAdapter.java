package in.voiceme.app.voiceme.NotificationsPage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.voiceme.app.voiceme.R;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<NotificationPost> data;
  private static final int ITEM = 1;

  public NotificationRecyclerAdapter(ArrayList<NotificationPost> data) {
    this.data = data;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View view = layoutInflater.inflate(R.layout.cell_notification_item, parent, false);
      return new VHItem(view);
    }

    throw new RuntimeException("NO VIEW TYPE FOUND");
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder.getItemViewType() == ITEM) {
      VHItem item = (VHItem) holder;

      item.notificationMessage.setText(data.get(position).initWithNotification());

      item.notificationTime.setText(((VHItem) holder).notificationMessage.getContext()
          .getString(R.string.time_stamp, android.text.format.DateUtils.getRelativeTimeSpanString(
              ((VHItem) holder).notificationMessage.getContext(), data.get(position).getSentTime(),
              true)));

      Picasso.with(item.itemView.getContext())
          .load(data.get(position).getSenderAvatar())
          .into(item.imgProfilePicture);
    }
  }

  @Override public int getItemCount() {
    return data.size();
  }

  @Override public int getItemViewType(int position) {
    return ITEM;
  }

  public void swap(ArrayList<NotificationPost> data) {
    this.data.clear();
    this.data = data;
    notifyDataSetChanged();
  }

  private class VHItem extends RecyclerView.ViewHolder {

    private View itemView;
    private TextView notificationMessage;
    private TextView notificationTime;
    private CircleImageView imgProfilePicture;

    VHItem(View itemView) {
      super(itemView);
      this.itemView = itemView;
      notificationMessage = (TextView) itemView.findViewById(R.id.txt_notification_message);
      notificationTime = (TextView) itemView.findViewById(R.id.txt_notification_time);
      imgProfilePicture = (CircleImageView) itemView.findViewById(R.id.img_profile_pic);
    }
  }
}


