package in.voiceme.app.voiceme.userpost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import in.voiceme.app.voiceme.R;

public class GridAdapter extends BaseAdapter {
    public Integer[] mThumbIds = {
            R.drawable.family, R.drawable.health,
            R.drawable.social, R.drawable.work,
            R.drawable.other
    };
    private Context mContext;

    public GridAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.squareitem,
                    null);
        }
        ImageView image = (ImageView) convertView.findViewById(R.id.icon);

        image.setImageResource(mThumbIds[position]);
        return convertView;
    }
}
