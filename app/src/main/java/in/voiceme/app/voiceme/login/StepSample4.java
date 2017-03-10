package in.voiceme.app.voiceme.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.userpost.AllCategoryPojo;
import in.voiceme.app.voiceme.userpost.AllPopularTagsPojo;
import in.voiceme.app.voiceme.userpost.CategoryTagAdapter;
import in.voiceme.app.voiceme.userpost.NewCategoryAdded;
import in.voiceme.app.voiceme.userpost.PopularCategoryClickListner;
import in.voiceme.app.voiceme.userpost.TagClass;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample4 extends AbstractStep implements View.OnClickListener {

    private TagView tagGroup;

    private AlertDialog.Builder builder1;
    private EditText editText;
    private TextView createNewHashTag;
    private RecyclerView rv;
    private ScrollView scrollView;
    private String current_category;
    private TextView selected_hashtag;

    /**
     * sample country list
     */
    private ArrayList<TagClass> tagList;
    private String current_feeling = null;
    private boolean yes = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.intro_step_four, container, false);

        scrollView = (ScrollView) v.findViewById(R.id.intro_tags_laoyut);
        selected_hashtag = (TextView) v.findViewById(R.id.intro_selected_hashtag);

        if (selected_hashtag.getVisibility() == View.VISIBLE){
            selected_hashtag.setVisibility(View.GONE);
        }
        rv=(RecyclerView)v.findViewById(R.id.intro_category_tag_rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        getPopularHashTags();

        tagGroup = (TagView) v.findViewById(R.id.intro_tag_group);
        editText = (EditText) v.findViewById(R.id.intro_editText);
        createNewHashTag = (TextView) v.findViewById(R.id.intro_create_new_hashtag);

        if (createNewHashTag.getVisibility()==View.VISIBLE){
            createNewHashTag.setVisibility(View.GONE);
        }

        createNewHashTag.setOnClickListener(this);
        getAllHashTags();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollView.setVisibility(View.VISIBLE);
                createNewHashTag.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    setTags(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {
                Toast.makeText(getActivity(), "Long Click: " + tag.text, Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("\"" + tag.text + "\" will be delete. Are you sure?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view.remove(position);
                        Toast.makeText(getActivity(), "\"" + tag.text + "\" deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();

            }
        });

        return v;
    }

    private void checkId(String text){
        for (int i=0; i < tagList.size(); i++){
            if (text.equals(tagList.get(i).getName())){
                String id = tagList.get(i).getId();
                setCategory(id);
                Toast.makeText(getActivity(), "The Category ID is: " + id, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initializeAdapter(List<AllPopularTagsPojo> categoryTags){
        CategoryTagAdapter adapter = new CategoryTagAdapter(categoryTags);

        adapter.setOnItemClickListener(new PopularCategoryClickListner() {

            @Override
            public void popularCategoryName(AllPopularTagsPojo model, View v) {
                // Todo add category text to the edittext
                editText.setText(model.getName());
                String name = model.getId();
                setCategory(name);
                Toast.makeText(getActivity(), "name of the category: " + name, Toast.LENGTH_SHORT).show();

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
            ((VoicemeApplication)getActivity().getApplication()).getWebService()
                    .getPopularHashTags()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<List<AllPopularTagsPojo>>() {
                        @Override
                        public void onNext(List<AllPopularTagsPojo> userResponse) {
                            initializeAdapter(userResponse);
                            //  categoryTags = userResponse;
                            Toast.makeText(getActivity(), "current response = " + userResponse.get(0).getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllHashTags() {
        // network call from retrofit
        try {
            ((VoicemeApplication)getActivity().getApplication()).getWebService()
                    .getAllHashTags()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<List<AllCategoryPojo>>() {
                        @Override
                        public void onNext(List<AllCategoryPojo> userResponse) {
                            prepareTags(userResponse);
                            Toast.makeText(getActivity(), "current response = " + userResponse.get(0).getName(), Toast.LENGTH_SHORT).show();
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
        selected_hashtag.setVisibility(View.VISIBLE);
        yes = true;
        selected_hashtag.setText(String.valueOf("You have selected : " + editText.getText().toString()));
        this.current_category = current_category;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.create_new_hashtag){
            dialogBox();
        }
    }

    public void dialogBox(){
        builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Do you want to Create a New HashTag ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Clicked OK", Toast.LENGTH_SHORT).show();
                        insertCategory(editText.getText().toString());
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Clicked No", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void insertCategory(String category) {
        // network call from retrofit
        try {
            ((VoicemeApplication)getActivity().getApplication()).getWebService()
                    .insertCategory(category)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<NewCategoryAdded>() {
                        @Override
                        public void onNext(NewCategoryAdded userResponse) {
                            setCategory(userResponse.getId());

                            Toast.makeText(getActivity(), "current response = " + userResponse.getSuccess().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
        if (yes){
            return true;
        } else {
            Toast.makeText(getActivity(), "IsOptional is false- step 01", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    @Override
    public void onStepVisible() {
    }

    @Override
    public void onNext() {
        StepThreeInterface stepOneInterface = (StepThreeInterface) getActivity();
        stepOneInterface.setCategory(this.current_category);
    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {;
        if (yes){
            return true;
        } else {
            Toast.makeText(getActivity(), "nextIf is false- step 01", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must click!</b> <small>this is the condition!</small>";
    }


}