package in.voiceme.app.voiceme.ProfilePage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.emmasuzuki.easyform.EasyTextInputLayout;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.BaseSubscriber;
import in.voiceme.app.voiceme.infrastructure.MySharedPreferences;
import in.voiceme.app.voiceme.l;
import in.voiceme.app.voiceme.login.LoginResponse;
import in.voiceme.app.voiceme.utils.ActivityUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class ChangeProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_SELECT_IMAGE = 100;

    private EasyTextInputLayout username;
    private EasyTextInputLayout aboutme;
    private EasyTextInputLayout userAge;
    private TextView genderSelection;
    private TextView genderSelectionTitle;
    private Button submitButton;
    private String imageUrl;
    private boolean changedImage = false;
    AlertDialog alertDialog1;
    private String currentGender;
    CharSequence[] values = {" Male "," Female "," Transgender "};

    private CircleImageView avatarView;
    private View avatarProgressFrame;
    private File tempOutputFile; //storing the profile_image temporarily while we crop it.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        getSupportActionBar().setTitle("Edit Profile Page");
        toolbar.setNavigationIcon(R.mipmap.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitButton = (Button) findViewById(R.id.submit_button_profile);
        username = (EasyTextInputLayout) findViewById(R.id.edittext_profile_username);
        aboutme = (EasyTextInputLayout) findViewById(R.id.edittext_profile_aboutme);
        userAge = (EasyTextInputLayout) findViewById(R.id.edittext_profile_age);
        genderSelection = (TextView) findViewById(R.id.user_gender_text_box);
        genderSelectionTitle = (TextView) findViewById(R.id.user_gender);

        avatarView = (CircleImageView) findViewById(R.id.changeimage);
        avatarProgressFrame = findViewById(R.id.activity_profilechange_avatarProgressFrame);
        tempOutputFile = new File(getExternalCacheDir(), "temp-profile_image.jpg");

        avatarView.setOnClickListener(this);
        submitButton.setOnClickListener(this);
        genderSelection.setOnClickListener(this);
        genderSelectionTitle.setOnClickListener(this);

        try {
            getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        avatarProgressFrame.setVisibility(View.GONE);
    }

    private void changeProfileRequest() {
      //  ActivityUtils.openGallery(this);
        ActivityUtils.cameraPermissionGranted(this);
        changeAvatar();
    }

    private void changeAvatar() {
        List<Intent> otherImageCaptureIntent = new ArrayList<>();
        List<ResolveInfo> otherImageCaptureActivities =
                getPackageManager().queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                        0); // finding all intents in apps which can handle capture image
        // loop through all these intents and for each of these activities we need to store an intent
        for (ResolveInfo info : otherImageCaptureActivities) { // Resolve info represents an activity on the system that does our work
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName,
                    info.activityInfo.name); // declaring explicitly the class where we will go
            // where the picture activity dump the image
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutputFile));
            otherImageCaptureIntent.add(captureIntent);
        }

        // above code is only for taking picture and letting it go through another app for cropping before setting to imageview
        // now below is for choosing the image from device

        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");

        Intent chooser = Intent.createChooser(selectImageIntent, "Choose Avatar");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, otherImageCaptureIntent.toArray(
                new Parcelable[otherImageCaptureActivities.size()]));  // add 2nd para as intent of parcelables.

        startActivityForResult(chooser, REQUEST_SELECT_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            tempOutputFile.delete();
            return;
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_IMAGE) {
                // user selected an image off their device. other condition they took the image and that image is in our tempoutput file
                Uri outputFile;
                Uri tempFileUri = Uri.fromFile(tempOutputFile);
                // if statement will detect if the user selected an image from the device or took an image
                if (data != null && (data.getAction() == null || !data.getAction()
                        .equals(MediaStore.ACTION_IMAGE_CAPTURE))) {
                    //then it means user selected an image off the device
                    // so we can get the Uri of that image using data.getData
                    outputFile = data.getData();
                    // Now we need to do the crop
                } else {
                    // image was out temp file. user took an image using camera
                    outputFile = tempFileUri;
                    // Now we need to do the crop
                }
                startCropActivity(outputFile);
            } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {


                uploadFile(Uri.parse(tempOutputFile.getPath()));
                changedImage = true;

                Timber.e(String.valueOf(Uri.fromFile(tempOutputFile)));
                avatarView.setImageResource(0);

                avatarView.setImageURI(Uri.fromFile(tempOutputFile));
                Toast.makeText(this, String.valueOf(Uri.fromFile(tempOutputFile)), Toast.LENGTH_SHORT).show();
                Toast.makeText(this, String.valueOf(tempOutputFile.getAbsolutePath()), Toast.LENGTH_SHORT).show();

                // avatarProgressFrame.setVisibility(View.VISIBLE);
                // bus.post(new Account.ChangeAvatarRequest(Uri.fromFile(tempOutputFile)));
            }
        }
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(50);
        return uCrop.withOptions(options);
    }

    private void startCropActivity(@NonNull Uri uri) {

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(tempOutputFile));

        uCrop = advancedConfig(uCrop);
        uCrop.start(ChangeProfileActivity.this);
    }


    @Override
    public void onClick(View view) {
        processLoggedState(view);
        int viewId = view.getId();

        if (viewId == R.id.changeimage) {
            changeProfileRequest();
        } else if (viewId == R.id.submit_button_profile) {

            if (changedImage){
                try {
                    submitData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    submitDataWithoutProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        } else if(viewId == R.id.user_gender_text_box || viewId == R.id.user_gender){
            CreateAlertDialogWithRadioButtonGroup();
        }
    }

    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(ChangeProfileActivity.this);

        builder.setTitle("Choose Your Gender");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:

                        Toast.makeText(ChangeProfileActivity.this, "Male clicked", Toast.LENGTH_LONG).show();
                        setCurrentGender("Male");
                        genderSelection.setText("Male");
                        break;
                    case 1:

                        Toast.makeText(ChangeProfileActivity.this, "Female Clicked", Toast.LENGTH_LONG).show();
                        setCurrentGender("Female");
                        genderSelection.setText("Female");
                        break;
                    case 2:

                        Toast.makeText(ChangeProfileActivity.this, "Transgender Clicked", Toast.LENGTH_LONG).show();
                        setCurrentGender("Transgender");
                        genderSelection.setText("Transgender");
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

    private void setCurrentGender(String gender){
        this.currentGender = gender;
    }



    private void submitData() throws Exception {
        application.getWebService()
                .login("", MySharedPreferences.getEmail(preferences), "",
                        userAge.getEditText().getText().toString(), MySharedPreferences.getSocialID(preferences),
                        Uri.parse(imageUrl), this.currentGender, aboutme.getEditText().getText().toString(),
                        username.getEditText().getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LoginResponse>() {
                    @Override
                    public void onNext(LoginResponse response) {

                        Toast.makeText(ChangeProfileActivity.this,
                                "result from update profile " + response.status, Toast.LENGTH_SHORT).show();
                        MySharedPreferences.registerUsername(preferences, username.getEditText().getText().toString());
                        changedImage = false;
                        //Todo add network call for uploading profile_image file
                        startActivity(new Intent(ChangeProfileActivity.this, ProfileActivity.class));
                    }
                });
    }

    private void submitDataWithoutProfile() throws Exception {
        application.getWebService()
                .loginWithoutProfile("", MySharedPreferences.getEmail(preferences), "",
                        userAge.getEditText().getText().toString(), MySharedPreferences.getSocialID(preferences),
                        this.currentGender, aboutme.getEditText().getText().toString(),
                        username.getEditText().getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LoginResponse>() {
                    @Override
                    public void onNext(LoginResponse response) {

                        Toast.makeText(ChangeProfileActivity.this,
                                "result from update profile " + response.status, Toast.LENGTH_SHORT).show();
                        MySharedPreferences.registerUsername(preferences, username.getEditText().getText().toString());
                        //Todo add network call for uploading profile_image file
                        startActivity(new Intent(ChangeProfileActivity.this, ProfileActivity.class));
                    }
                });
    }

    private void getData() throws Exception {
        application.getWebService()
                .getUserProfile(MySharedPreferences.getUserId(preferences))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ProfileUserList>() {
                    @Override
                    public void onNext(ProfileUserList response) {
                        Timber.e("Got user details");
                        //     followers.setText(String.valueOf(response.size()));
                        profileData(response);
                    }
                });
    }

    private void profileData(ProfileUserList response) {
        username.getEditText().setText(response.getData().getUserNickName());
        aboutme.getEditText().setText(response.getData().getAboutMe());
        userAge.getEditText().setText(response.getData().getUserDateOfBirth());
        genderSelection.setText(response.getData().getGender());

        if (!response.getData().getAvatarPics().equals("")) {
            Picasso.with(this)
                    .load(response.getData().getAvatarPics())
                    .resize(75, 75)
                    .centerInside()
                    .into(avatarView);
        }

    }

    private void uploadFile(Uri fileUri) {
        File file = new File(String.valueOf(fileUri));

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // finally, execute the request
        try {
            application.getWebService()
                    .uploadFile(body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<String>() {
                        @Override
                        public void onNext(String response) {
                            Timber.d("file url " + response);
                            Toast.makeText(ChangeProfileActivity.this, "file url " + response, Toast.LENGTH_SHORT).show();
                            setImageFileUrl(response);
                            MySharedPreferences.registerImageUrl(preferences, response);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setImageFileUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean processLoggedState(View viewPrm) {
        if (this.mBaseLoginClass.isDemoMode(viewPrm)) {
            l.a(666);
            Toast.makeText(viewPrm.getContext(), "You aren't logged in", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
