package in.voiceme.app.voiceme.PostsDetails;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import in.voiceme.app.voiceme.ProfilePage.SecondProfile;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.Constants;

/**
 * Created by Harish on 9/1/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public List<UserListModel> dataSet;

    public RVAdapter(List<UserListModel> persons) {
        this.dataSet = persons;
    }

    public void animateTo(List<UserListModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }


    private void applyAndAnimateRemovals(List<UserListModel> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final UserListModel model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<UserListModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final UserListModel model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }


    private void applyAndAnimateMovedItems(List<UserListModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final UserListModel model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(UserListModel item) {
        if (!dataSet.contains(item)) {
            dataSet.add(item);
            notifyItemInserted(dataSet.size() - 1);
        }
    }

    public void addItem(int position, UserListModel model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void removeItem(UserListModel item) {
        int indexOfItem = dataSet.indexOf(item);
        if (indexOfItem != -1) {
            this.dataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public UserListModel removeItem(int position) {
        final UserListModel model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void clearItem() {
        if (dataSet != null)
            dataSet.clear();
    }

    public void moveItem(int fromPosition, int toPosition) {
        final UserListModel model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public UserListModel getItem(int index) {
        if (dataSet != null && dataSet.get(index) != null) {
            return dataSet.get(index);
        } else {
            throw new IllegalArgumentException("Item with index " + index + " doesn't exist, dataSet is " + dataSet);
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh = null;
        if (viewType == VIEW_ITEM) {
            View itemView = LayoutInflater.
                    from(parent.getContext()).
                    inflate(R.layout.item, parent, false);
            vh = new RVAdapter.PersonViewHolder(itemView);
        } else if (viewType == VIEW_PROG) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.progress_item, parent, false);
            vh = new RVAdapter.ProgressViewHolder(v);
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder personViewHolder, int position) {

        if (personViewHolder instanceof RVAdapter.PersonViewHolder) {
            UserListModel dataItem = dataSet.get(position);
            ((RVAdapter.PersonViewHolder) personViewHolder).bind(dataItem);
        } else {
            ((RVAdapter.ProgressViewHolder) personViewHolder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (dataSet != null)
            return dataSet.size();
        else
            return 0;
    }

    public static class PersonViewHolder extends UserCardViewHolder {

        PersonViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        protected void userNameClicked(View view) {
            Intent intent = new Intent(view.getContext(), SecondProfile.class);
            Toast.makeText(view.getContext(), "Post ID is " + dataItem.getIdUserName(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.SECOND_PROFILE_ID, dataItem.getIdUserName());
            view.getContext().startActivity(intent);
        }

        @Override
        protected void userProfileClicked(View view) {
            Intent intent = new Intent(view.getContext(), SecondProfile.class);
            Toast.makeText(view.getContext(), "Post ID is " + dataItem.getIdUserName(), Toast.LENGTH_SHORT).show();
            intent.putExtra(Constants.SECOND_PROFILE_ID, dataItem.getIdUserName());
            view.getContext().startActivity(intent);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}
