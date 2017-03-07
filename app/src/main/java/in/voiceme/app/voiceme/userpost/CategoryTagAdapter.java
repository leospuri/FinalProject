package in.voiceme.app.voiceme.userpost;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import in.voiceme.app.voiceme.R;

/**
 * Created by Harish on 9/1/2016.
 */
public class CategoryTagAdapter extends RecyclerView.Adapter<CategoryTagAdapter.PersonViewHolder> {
    private TagClass tagList;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        Button tagName;
        TextView numberOfTags;



        PersonViewHolder(View itemView) {
            super(itemView);
            tagName = (Button) itemView.findViewById(R.id.category_popular_tag);
            numberOfTags = (TextView) itemView.findViewById(R.id.category_tag_count);

            int[] androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            tagName.setBackgroundColor(randomAndroidColor);
        }
    }

    List<AllPopularTagsPojo> categoryTags;

    CategoryTagAdapter(List<AllPopularTagsPojo> categoryTags){
        this.categoryTags = categoryTags;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hastag_card, viewGroup, false);

        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.tagName.setText(categoryTags.get(i).getName());
        personViewHolder.numberOfTags.setText(categoryTags.get(i).getCount());
    }

    @Override
    public int getItemCount() {
        return categoryTags.size();
    }
}
