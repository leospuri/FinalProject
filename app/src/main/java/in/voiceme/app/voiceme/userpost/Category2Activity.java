package in.voiceme.app.voiceme.userpost;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.DTO.ReportResponse;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import rx.android.schedulers.AndroidSchedulers;

public class Category2Activity extends BaseActivity implements View.OnClickListener {
    private TagView tagGroup;

    private AlertDialog.Builder builder1;
    private EditText editText;
    private TextView createNewHashTag;
    private List<AllPopularTagsPojo> categoryTags;
    private RecyclerView rv;
    private ScrollView scrollView;
    private String current_category;


    /**
     * sample country list
     */
    private ArrayList<TagClass> tagList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category2);
        getSupportActionBar().setTitle("Choose Category");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLoggedState(v);
                finish();
            }
        });

        scrollView = (ScrollView) findViewById(R.id.tags_laoyut);

        rv=(RecyclerView)findViewById(R.id.category_tag_rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        getPopularHashTags();

        tagGroup = (TagView) findViewById(R.id.tag_group);
        editText = (EditText) findViewById(R.id.editText);
        createNewHashTag = (TextView) findViewById(R.id.create_new_hashtag);

        if (createNewHashTag.getVisibility()==View.VISIBLE){
            createNewHashTag.setVisibility(View.GONE);
        }

        createNewHashTag.setOnClickListener(this);
        if (isNetworkConnected()){
            getAllHashTags();
        } else {
            Toast.makeText(this, "You are not connected to internet", Toast.LENGTH_SHORT).show();
        }


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollView.setVisibility(View.VISIBLE);
                createNewHashTag.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isNetworkConnected()){
                    setTags(s);
                } else {
                    Toast.makeText(Category2Activity.this, "You are not connected to internet", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {
                Toast.makeText(Category2Activity.this, "Long Click: " + tag.text, Toast.LENGTH_SHORT).show();
            }
        });

        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                editText.setText(tag.text);
                editText.setSelection(tag.text.length());//to set cursor position
                checkId(tag.text);
            }
        });



        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {

            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Category2Activity.this);
                builder.setMessage("\"" + tag.text + "\" will be delete. Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.remove(position);
                        Toast.makeText(Category2Activity.this, "\"" + tag.text + "\" deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();

            }
        });
    }

    private void checkId(String text){
        for (int i=0; i < tagList.size(); i++){
            if (text.equals(tagList.get(i).getName())){
                String id = tagList.get(i).getId();
                setCategory(id);
                Toast.makeText(this, "The Category ID is: " + id, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initializeAdapter(List<AllPopularTagsPojo> categoryTags){
        CategoryTagAdapter adapter = new CategoryTagAdapter(categoryTags);

        adapter.setOnItemClickListener(new PopularCategoryClickListner() {

            @Override
            public void popularCategoryName(AllPopularTagsPojo model, View v) {
                String name = model.getId();
                setCategory(name);
                Toast.makeText(Category2Activity.this, "name of the category: " + name, Toast.LENGTH_SHORT).show();

            }
        });

        rv.setAdapter(adapter);
    }

    private void prepareTags(List<AllCategoryPojo> allCategories) {
        Gson gson = new Gson();
        tagList = new ArrayList<>();
        String simpleTags = gson.toJson(
                allCategories,
                new TypeToken<ArrayList<AllCategoryPojo>>() {}.getType());
        JSONArray jsonArray;
        JSONObject temp;
        try {
            // Todo result from network call
            jsonArray = new JSONArray(simpleTags);


            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getJSONObject(i);
                tagList.add(new TagClass(temp.getString("id"), temp.getString("name")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getPopularHashTags() {
        // network call from retrofit
        try {
            application.getWebService()
                    .getPopularHashTags()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<List<AllPopularTagsPojo>>() {
                        @Override
                        public void onNext(List<AllPopularTagsPojo> userResponse) {
                            initializeAdapter(userResponse);
                          //  categoryTags = userResponse;
                            Toast.makeText(Category2Activity.this, "current response = " + userResponse.get(0).getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllHashTags() {
        // network call from retrofit
        try {
            application.getWebService()
                    .getAllHashTags()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<List<AllCategoryPojo>>() {
                        @Override
                        public void onNext(List<AllCategoryPojo> userResponse) {
                            prepareTags(userResponse);
                            Toast.makeText(Category2Activity.this, "current response = " + userResponse.get(0).getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void setTags(CharSequence cs) {
        /**
         * for empty edittext
         */
        if (cs.toString().equals("")) {
            tagGroup.addTags(new ArrayList<Tag>());
            return;
        }

        String text = cs.toString();
        ArrayList<Tag> tags = new ArrayList<>();
        Tag tag;


        // Todo: Error when i closed the app inside the category activity
        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getName().toLowerCase().startsWith(text.toLowerCase())) {
                tag = new Tag(tagList.get(i).getName());
                tag.radius = 10f;
                tag.layoutColor = Color.parseColor(tagList.get(i).getColor());
                if (i % 2 == 0) // you can set deletable or not
                    tag.isDeletable = true;
                tags.add(tag);
            }
        }
        tagGroup.addTags(tags);

    }

    public void setCategory(String current_category) {
        this.current_category = current_category;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categorymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.category_menu) {
            Intent returnIntent = new Intent();
            if (current_category.isEmpty() || current_category == null){
                Toast.makeText(this, "You have not selected any Hashtags", Toast.LENGTH_SHORT).show();
            } else {
                returnIntent.putExtra("resultFromCategory", current_category);
            }
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.create_new_hashtag){
            dialogBox();
        }
    }

    public void dialogBox(){
        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to Create a New HashTag ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Category2Activity.this, "Clicked OK", Toast.LENGTH_SHORT).show();
                        insertCategory(editText.getText().toString());
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Category2Activity.this, "Clicked No", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void insertCategory(String category) {
        // network call from retrofit
        try {
            application.getWebService()
                    .insertCategory(category)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<ReportResponse>() {
                        @Override
                        public void onNext(ReportResponse userResponse) {

                            Toast.makeText(Category2Activity.this, "current response = " + userResponse.getSuccess().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
