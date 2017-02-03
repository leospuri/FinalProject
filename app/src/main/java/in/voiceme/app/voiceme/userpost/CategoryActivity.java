package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;

public class CategoryActivity extends BaseActivity {
    private GridView gridView = null;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_category);
        getSupportActionBar().setTitle("Choose Category");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(new GridAdapter(getBaseContext()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                Intent returnIntent = new Intent();
                if (gridView.getPositionForView(view) == 0) {
                    returnIntent.putExtra("resultFromCategory", "1");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (gridView.getPositionForView(view) == 1) {
                    returnIntent.putExtra("resultFromCategory", "2");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (gridView.getPositionForView(view) == 2) {
                    returnIntent.putExtra("resultFromCategory", "3");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (gridView.getPositionForView(view) == 3) {
                    returnIntent.putExtra("resultFromCategory", "4");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else if (gridView.getPositionForView(view) == 4) {
                    returnIntent.putExtra("resultFromCategory", "5");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }

                // Toast.makeText(TextActivity.this, "item clicked" + gridView.getPositionForView(v), Toast.LENGTH_SHORT).show();
                // Send intent to SingleViewActivity
                // Intent i = new Intent(getApplicationContext(), SecondActivity.class);
                // Pass profile_image index
                //  i.putExtra("id", position);
                // startActivity(i);
            }
        });

    }

//    @Override
//     public boolean processLoggedState(View viewPrm) {
//
//    }
}
