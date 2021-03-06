package in.voiceme.app.voiceme.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import in.voiceme.app.voiceme.DTO.AllCategoryPojo;
import in.voiceme.app.voiceme.DTO.AllPopularTagsPojo;
import in.voiceme.app.voiceme.DTO.NewCategoryAdded;
import in.voiceme.app.voiceme.DTO.TagClass;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.Constants;
import in.voiceme.app.voiceme.infrastructure.VoicemeApplication;
import in.voiceme.app.voiceme.userpost.CategoryTagAdapter;
import in.voiceme.app.voiceme.userpost.PopularCategoryClickListner;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Francesco Cannizzaro (fcannizzaro).
 */
public class StepSample4 extends AbstractStep implements View.OnClickListener {

    private TagView tagGroup;

    private AlertDialog.Builder builder1;
    private EditText editText;
    private ImageView createNewHashTag;
    private RecyclerView rv;
    private ScrollView scrollView;
    private String current_category;
    private TextView selected_hashtag;
    private TextView intro_step_four_popular;
    private LinearLayout mLinearLayout;
    private View mView;
    private EditText userInputDialogEditText;
    private String category_name;

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
        intro_step_four_popular = (TextView) v.findViewById(R.id.intro_step_four_popular);

        userInputDialogEditText = (EditText) v.findViewById(R.id.userInputDialog);

        if (selected_hashtag.getVisibility() == View.VISIBLE){
            selected_hashtag.setVisibility(View.GONE);
        }
        rv=(RecyclerView)v.findViewById(R.id.intro_category_tag_rv);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        getPopularHashTags();

        mLinearLayout = (LinearLayout) v.findViewById(R.id.stepFourLinear);
        tagGroup = (TagView) v.findViewById(R.id.intro_tag_group);
        editText = (EditText) v.findViewById(R.id.intro_editText);
        createNewHashTag = (ImageView) v.findViewById(R.id.intro_create_new_hashtag);

        mLinearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (editText.isFocused()){
                    rv.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        createNewHashTag.setOnClickListener(this);
        getAllHashTags();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                scrollView.setVisibility(View.VISIBLE);
                intro_step_four_popular.setVisibility(View.GONE);
                rv.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    setTags(s);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().toString().isEmpty()){
                    scrollView.setVisibility(View.GONE);
                    intro_step_four_popular.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.VISIBLE);
                }
            }
        });

        tagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {
              //  Toast.makeText(getActivity(), "Long Click: " + tag.text, Toast.LENGTH_SHORT).show();
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
                     //   Toast.makeText(getActivity(), "\"" + tag.text + "\" deleted", Toast.LENGTH_SHORT).show();
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
                String name = tagList.get(i).getName();
                setCategory(id, name);
         //       Toast.makeText(getActivity(), "The Category ID is: " + id, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initializeAdapter(List<AllPopularTagsPojo> categoryTags){
        CategoryTagAdapter adapter = new CategoryTagAdapter(categoryTags);

        adapter.setOnItemClickListener(new PopularCategoryClickListner() {

            @Override
            public void popularCategoryName(AllPopularTagsPojo model, View v) {
                // Todo add category text to the edittext
                String name = model.getId();
                String categoryname = model.getName();
                setCategory(name, categoryname);
                editText.setText(categoryname);
           //     Toast.makeText(getActivity(), "name of the category: " + name, Toast.LENGTH_SHORT).show();

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
                    .subscribeOn(Schedulers.io())
                    .subscribe(new BaseSubscriber<List<AllPopularTagsPojo>>() {
                        @Override
                        public void onNext(List<AllPopularTagsPojo> userResponse) {
                            initializeAdapter(userResponse);
                            //  categoryTags = userResponse;
                    //        Toast.makeText(getActivity(), "current response = " + userResponse.get(0).getName(), Toast.LENGTH_SHORT).show();
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
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<List<AllCategoryPojo>>() {
                        @Override
                        public void onNext(List<AllCategoryPojo> userResponse) {
                            prepareTags(userResponse);
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

    public void setCategory(String current_category, String categoryName) {
        this.category_name = categoryName;
        selected_hashtag.setVisibility(View.VISIBLE);
        if (categoryName.trim().length() > 1500){
            Toast.makeText(getActivity(), "Please Enter Status less than 1500 words", Toast.LENGTH_SHORT).show();
            yes = false;
        } else {
            yes = true;
        }
        selected_hashtag.setText(String.valueOf("You have selected : " + categoryName));
        this.current_category = current_category;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.intro_create_new_hashtag){
            if (!editText.getText().toString().trim().isEmpty()){
                dialogBox(editText.getText().toString().trim());
            } else {
                dialogBox();
            }


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.CATEGORY, current_category);
        outState.putString(Constants.IDUSERNAME, category_name);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            current_category = savedInstanceState.getString(Constants.CATEGORY);
            category_name = savedInstanceState.getString(Constants.IDUSERNAME);
            setCategory(current_category, category_name);
        }

    }

    public void dialogBox(){

        mView = getActivity().getLayoutInflater().inflate(R.layout.user_input_dialog_box, null);

        builder1 = new AlertDialog.Builder(getActivity());
        builder1.setView(mView);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Created New Interest", Toast.LENGTH_SHORT).show();
                        insertCategory(editText.getText().toString());
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
                            setCategory(userResponse.getId(), editText.getText().toString());
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
        if (!editText.getText().toString().trim().isEmpty() && selected_hashtag.getText().toString().isEmpty()){
            dialogBox(editText.getText().toString());
        }

     //   yes = true;
    }

    public void dialogBox(String message){


        builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Do you want to Create a New Interest: " + message + " ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), "Creating New Interest", Toast.LENGTH_SHORT).show();
                        insertCategory(editText.getText().toString().trim());
                        yes = true;
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

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {
        if (yes){
            return true;
        } else {
           // Toast.makeText(getActivity(), "nextIf is false- step 01", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    @Override
    public String error() {
        return "<b>You must click one category!</b>";
    }


}
