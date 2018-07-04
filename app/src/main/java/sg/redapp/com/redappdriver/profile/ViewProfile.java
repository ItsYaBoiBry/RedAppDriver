package sg.redapp.com.redappdriver.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.redapp.com.redappdriver.Classes.User;
import sg.redapp.com.redappdriver.R;
import sg.redapp.com.redappdriver.login.SignUp;

public class ViewProfile extends AppCompatActivity {
    Toolbar toolbar;
    EditText firstName, carPlate, email,phone;
    TextView countryCode, serviceType, name, rating, vehicleNum;
    Button save, edit;
    CircleImageView  editImage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    CheckBox cbTowAccident, cbTowBreakdown, cbTyreManding, cbSpareTyreReplacement,cbBatteryJumpStart,cbBatteryReplacement, cbOthers;

    ArrayList arrayList = new ArrayList<String>();

    CircleImageView civProfile;
    private Uri resultUri;
    private Bitmap resultBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        SetupToolbar();

        firstName = findViewById(R.id.firstName);
        carPlate = findViewById(R.id.carPlate);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        countryCode = findViewById(R.id.countryCode);
        serviceType = findViewById(R.id.serviceType);
        name = findViewById(R.id.name);
        rating = findViewById(R.id.rating);
        vehicleNum = findViewById(R.id.vehicleNum);
        save = findViewById(R.id.save);
        edit = findViewById(R.id.edit);
        civProfile = findViewById(R.id.userimage);
        editImage = findViewById(R.id.editImage);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

    }

    public void SetupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Init();
        tempSetup();

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit();
            }
        });
        countryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"+65"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProfile.this);
                builder.setItems(items, (dialog, item) -> {
                    countryCode.setText(items[item]);
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        serviceType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ViewProfile.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.drop_down_type_of_service);
                Button okButton = dialog.findViewById(R.id.buttonOk);
                cbTowAccident = dialog.findViewById(R.id.serviceType_tow_accident);
                cbTowBreakdown = dialog.findViewById(R.id.serviceType_tow_breakdown);
                cbTyreManding = dialog.findViewById(R.id.serviceType_tyre_mending);
                cbSpareTyreReplacement = dialog.findViewById(R.id.serviceType_spare_tyre_replacement);
                cbBatteryJumpStart = dialog.findViewById(R.id.serviceType_battery_jump_start);
                cbBatteryReplacement = dialog.findViewById(R.id.serviceType_battery_replacement);
                cbOthers = dialog.findViewById(R.id.serviceType_other);
                dialog.show();
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver");
                        arrayList.clear();
                        if(cbTowAccident.isChecked()){
                            arrayList.add(cbTowAccident.getText().toString());
                        }
                        if(cbTowBreakdown.isChecked()){
                            arrayList.add(cbTowBreakdown.getText().toString());
                        }
                        if(cbTyreManding.isChecked()){
                            arrayList.add(cbTyreManding.getText().toString());
                        }
                        if(cbSpareTyreReplacement.isChecked()){
                            arrayList.add(cbSpareTyreReplacement.getText().toString());
                        }
                        if(cbBatteryJumpStart.isChecked()){
                            arrayList.add(cbBatteryJumpStart.getText().toString());
                        }
                        if(cbBatteryReplacement.isChecked()){
                            arrayList.add(cbBatteryReplacement.getText().toString());
                        }
                        if(cbOthers.isChecked()){
                            arrayList.add(cbOthers.getText().toString());
                        }
                        String completeString = "";
                        for(int i = 0; i<arrayList.size(); i++){
                            Log.i("arraylist",arrayList.size()+"");
                            completeString = completeString + arrayList.get(i);
                        }
                        serviceType.setText(completeString);
                        dialog.dismiss();
                    }
                });
            }
        });

//        getTypeOfService.setOnClickListener(new View.OnClickListener() {
//
//        });
    }

    public void tempSetup(){
        DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver");
//        name.setText(""+ userName);
        driverRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User userList = dataSnapshot.getValue(User.class);
                String userKey = dataSnapshot.getKey();
                assert userKey != null;
                if(userKey.equals(user.getUid())){
                    Log.d("user", "onChildAdded: User " + userList.toString());
                    firstName.setText(userList.getName());
                    carPlate.setText(userList.getUserCarPlate());
                    email.setText(userList.getEmail());
                    countryCode.setText("+65");
                    phone.setText(userList.getPhone_number());
                    serviceType.setText(userList.getType_of_service());
                    name.setText(String.format("%s", firstName.getText().toString()));
                    rating.setText("4.2");
                    vehicleNum.setText(userList.getUserCarPlate());
                    if(dataSnapshot.hasChild("profileImageUrl")){
                        Log.i("profileImageUrl","Url:" + userList.getProfileImageUrl());
                        if(!userList.getProfileImageUrl().equalsIgnoreCase("No Image")){
                            Log.i("status called","set image");
                            Glide.with(ViewProfile.this).load(userList.getProfileImageUrl()).into(civProfile);
                        }
                    }
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Glide.with(ViewProfile.this).load("https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&h=350").into(civProfile);
    }
    public void Edit(){
        editImage.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        firstName.setEnabled(true);
        carPlate.setEnabled(true);
        email.setEnabled(false);
        countryCode.setEnabled(true);
        countryCode.setClickable(true);
        phone.setEnabled(true);
        serviceType.setClickable(true);
        serviceType.setEnabled(true);
    }
    public void Save(){
        editImage.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        firstName.setEnabled(false);
        carPlate.setEnabled(false);
        email.setEnabled(false);
        countryCode.setEnabled(false);
        countryCode.setClickable(false);
        phone.setEnabled(false);
        serviceType.setClickable(false);
        serviceType.setEnabled(false);
        name.setText(String.format("%s", firstName.getText().toString()));

        String updateName = name.getText().toString();
        String updateEmail = email.getText().toString();
        String updatePhoneNumber = phone.getText().toString();
        String updateTypeOfService = serviceType.getText().toString();
        String updateCarPlate = carPlate.getText().toString();
        DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver");
        DatabaseReference name = driverRef.child(user.getUid()).child("name");

        User updateUser = new User(updateEmail,updateName,updatePhoneNumber,updateTypeOfService,updateCarPlate);
        driverRef.child(user.getUid()).setValue(updateUser);
    }
    public void Init(){
        editImage.setVisibility(View.GONE);
        edit.setVisibility(View.VISIBLE);
        save.setVisibility(View.GONE);
        firstName.setEnabled(false);
        carPlate.setEnabled(false);
        email.setEnabled(false);
        countryCode.setEnabled(false);
        countryCode.setClickable(false);
        phone.setEnabled(false);
        serviceType.setClickable(false);
        serviceType.setEnabled(false);
    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }

//    Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//    startActivityForResult(intent,1007);

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1007);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1008);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1007 &&resultCode == Activity.RESULT_OK){
            final Uri imageGalleryUri = data.getData();
            resultUri = imageGalleryUri;
            resultBitmap = null;
            //civProfile.setImageURI(imageGalleryUri);

            DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver").child((user.getUid()));
            Map<String, Object> userUpdates = new HashMap<>();

            if(resultUri != null || resultBitmap != null){
                Log.i("status called","resultUri is not null");
                StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_image").child(user.getUid());
                Bitmap bitmap = null;

                try{
                    if(resultUri != null){
                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                    }else if(resultBitmap != null){
                        bitmap = resultBitmap;
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] baosData = baos.toByteArray();
                UploadTask uploadTask = filePath.putBytes(baosData);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("status called","success added");
                        StorageMetadata smd = taskSnapshot.getMetadata();
                        StorageReference sRef = smd.getReference();
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                //Map newImage = new HashMap();
                                Glide.with(ViewProfile.this).load(downloadUrl.toString()).into(civProfile);
                                userUpdates.put("profileImageUrl",downloadUrl.toString());
                                driverRef.updateChildren(userUpdates);
                            }
                        });

                    }
                });

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_SHORT).show();
                    }
                });
            }


            Log.i("status called","1007");
        } else if(requestCode == 1008 && resultCode == Activity.RESULT_OK){
            Log.i("status called","1008");
            Bundle extras = data.getExtras();
            resultBitmap = (Bitmap) extras.get("data");
            resultUri = null;
            civProfile.setImageBitmap(resultBitmap);
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);



            DatabaseReference driverRef = firebaseDatabase.getReference().child("user").child("driver").child((user.getUid()));
            Map<String, Object> userUpdates = new HashMap<>();

            if(resultUri != null || resultBitmap != null){
                Log.i("status called","resultUri is not null");
                StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_image").child(user.getUid());
                Bitmap bitmap = null;

                try{
                    if(resultUri != null){
                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                    }else if(resultBitmap != null){
                        bitmap = resultBitmap;
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] baosData = baos.toByteArray();
                UploadTask uploadTask = filePath.putBytes(baosData);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i("status called","success added");
                        StorageMetadata smd = taskSnapshot.getMetadata();
                        StorageReference sRef = smd.getReference();
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUrl = uri;
                                //Map newImage = new HashMap();
                                Glide.with(ViewProfile.this).load(downloadUrl.toString()).into(civProfile);
                                userUpdates.put("profileImageUrl",downloadUrl.toString());
                                driverRef.updateChildren(userUpdates);
                            }
                        });

                    }
                });

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
