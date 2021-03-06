package in.voiceme.app.voiceme.userpost;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import in.voiceme.app.voiceme.DTO.AllPopularTagsPojo;
import in.voiceme.app.voiceme.DTO.TagClass;
import in.voiceme.app.voiceme.R;
import timber.log.Timber;

/**
 * Created by Harish on 9/1/2016.
 */
public class CategoryTagAdapter extends RecyclerView.Adapter<CategoryTagAdapter.TrendingHashTagsViewHolder> {
    private TagClass tagList;
    private static PopularCategoryClickListner myClickListener;

    public static class TrendingHashTagsViewHolder extends RecyclerView.ViewHolder {
        protected AllPopularTagsPojo dataItem;

        TextView tagName;
        TextView numberOfTags;

        TrendingHashTagsViewHolder(View itemView) {
            super(itemView);

            tagName = (TextView) itemView.findViewById(R.id.category_popular_tag);
            numberOfTags = (TextView) itemView.findViewById(R.id.category_tag_count);


            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.KITKAT) {
                // only for gingerbread and newer versions
                Timber.e("lower android version");
            } else {
                int[] androidColors = itemView.getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                tagName.setTextColor(randomAndroidColor);
            }



        }

        public void bind(AllPopularTagsPojo dataItem) {
            this.dataItem = dataItem;
            tagName.setText(String.valueOf("#" + dataItem.getName()));
            numberOfTags.setText(String.valueOf("used by " + dataItem.getCount() + " users"));

        }
    }

    public void setOnItemClickListener(PopularCategoryClickListner myClickListener) {
        this.myClickListener = myClickListener;
    }

    List<AllPopularTagsPojo> categoryTags;

    public CategoryTagAdapter(List<AllPopularTagsPojo> categoryTags){
        this.categoryTags = categoryTags;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public TrendingHashTagsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hastag_card, viewGroup, false);
        TrendingHashTagsViewHolder pvh = new TrendingHashTagsViewHolder(v);

        pvh.tagName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (myClickListener != null) {
                        myClickListener.popularCategoryName(pvh.dataItem, view);

                    } else {
                        Toast.makeText(view.getContext(), "Click Event Null", Toast.LENGTH_SHORT).show();
                    }
                } catch (NullPointerException e) {
                    Toast.makeText(view.getContext(), "Click Event Null Ex", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return pvh;
    }

    @Override
    public void onBindViewHolder(TrendingHashTagsViewHolder trendingHashTagsViewHolder, int i) {

            AllPopularTagsPojo dataItem = categoryTags.get(i);
            ((CategoryTagAdapter.TrendingHashTagsViewHolder) trendingHashTagsViewHolder).bind(dataItem);



    //    trendingHashTagsViewHolder.tagName.setText(categoryTags.get(i).getName());
    //    trendingHashTagsViewHolder.numberOfTags.setText(categoryTags.get(i).getCount());
    }

    @Override
    public int getItemCount() {
        return categoryTags.size();
    }
}
