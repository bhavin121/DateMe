package com.code_n_droid.dateme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class AddDetailsActivity extends AppCompatActivity implements LocationListener {

    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String PHOTOS_KEY = "photos";

    private RadioGroup gender;
    private ChipGroup hobbies;
    private EditText nameField, ageField, bioField;
    private String email, password;
    private final List<String> photos = new LinkedList<>();

    private double lat, longitude;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        // Find Views
        gender = findViewById(R.id.gender);
        hobbies = findViewById(R.id.hobbies);
        nameField = findViewById(R.id.nameField);
        ageField = findViewById(R.id.ageField);
        bioField = findViewById(R.id.bioField);

        email = getIntent().getStringExtra(EMAIL_KEY);
        password = getIntent().getStringExtra(PASSWORD_KEY);
//        photos.addAll(getIntent().getStringArrayListExtra(PHOTOS_KEY));

        findViewById(R.id.submitButton).setOnClickListener(view -> {
            uploadData();
        });

        firestore = FirebaseFirestore.getInstance();

        getLocation();
    }

    private void uploadData() {
        User user = readInputs();

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Data...");
        dialog.show();

        firestore.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    dialog.dismiss();
                    goToHome();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                });
    }

    private void goToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(AddDetailsActivity.EMAIL_KEY, email);
        startActivity(intent);
    }

    private User readInputs() {
        User user = new User();

        // Read gender
        if(gender.getCheckedRadioButtonId() == R.id.male){
            user.setGender(Users.MALE);
        }else{
            user.setGender(Users.FEMALE);
        }
        // Read name age and bio
        try{
            user.setAge(Integer.parseInt(ageField.getText().toString()));
        }catch(Exception e){
            Toast.makeText(this, "Invalid Age", Toast.LENGTH_SHORT).show();
        }
        if(!(user.getAge()>18 && user.getAge()<100)){
            Toast.makeText(this, "Invalid Age", Toast.LENGTH_SHORT).show();
        }
        user.setBio(bioField.getText().toString());
        user.setName(nameField.getText().toString());

        // read hobbies

        List<Integer> ids = hobbies.getCheckedChipIds();
        List<String> hobbiesList = new LinkedList<>();
        for(int i : ids){
            hobbiesList.add(Users.map.get(i));
        }
        user.setHobbies(hobbiesList);

        // Email & Password
        user.setEmail(email);
        user.setPassword(password);
        user.setPhotos(photos);

        return user;
    }

    /* Retrieve Location */

    public void getLocation(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            retrieveLocation();
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);

        }
    }

    @SuppressLint("MissingPermission")
    private void retrieveLocation() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Getting location");
        pd.show();

        LocationManager manager=(LocationManager)getSystemService(LOCATION_SERVICE);

        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000, 5, this);
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(location!=null) {
            lat = location.getLatitude();
            longitude = location.getLongitude();

            Toast.makeText(this,longitude+""+lat, Toast.LENGTH_SHORT).show();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try {

                List<Address> addressList = geocoder.getFromLocation(lat, longitude, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                pd.dismiss();
            }
        }
        pd.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==200 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            retrieveLocation();
        }else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}