package com.code_n_droid.dateme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private List<User> userList;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        goToFragment(new FindFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    goToFragment(new FindFragment());
                    break;
                case R.id.interested:
                    goToFragment(new LikesFragment());
                    break;
                case R.id.likes:
                    goToFragment(new LikeMessageFragment());
                    break;
                case R.id.filter:
                    goToFragment(new FilterFragment().setListener(()->{
                        goToFragment(new FindFragment());
                        feedLoader();
                    }));
                    break;
                case R.id.profile:
                    goToFragment(new ProfileFragment());
                    break;
            }
            return true;
        });

        loadUserData(getIntent().getStringExtra(AddDetailsActivity.EMAIL_KEY));

    }

    private void feedLoader() {
        int start, end;
        if(AppData.genderFilter){
            start = AppData.startingAge;
            end = AppData.endAge;
        }else {
            start = 0;
            end = 100;
        }

        if(AppData.genderFilter){
            feedLoad(start, end, AppData.gender, AppData.user.getHobbies());
        }else{
            feedLoad(start, end, AppData.user.getHobbies());
        }
    }

    private void goToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, fragment)
                .commit();
    }

    public void loadUserData(String email) {

        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        AppData.user = queryDocumentSnapshots.toObjects(User.class).get(0);
                        pd.dismiss();
                        feedLoader();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                                .show();
                        pd.dismiss();
                    }
                });
    }

    public void feedLoad(int startAge, int endAge, String gender, List<String> hobbies)
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String[] myArray = new String[hobbies.size()];
        hobbies.toArray(myArray);

        db.collection("users")
                .whereEqualTo("gender", gender).whereGreaterThan("age",startAge-1)
                .whereLessThan("age", endAge+1).whereArrayContainsAny("hobbies", Arrays.asList(myArray))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        userList = queryDocumentSnapshots.toObjects(User.class);
                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                                .show();
                        pd.dismiss();
                    }
                });
    }

    public void feedLoad(int startAge, int endAge, List<String> hobbies)
    {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String[] myArray = new String[hobbies.size()];
        hobbies.toArray(myArray);

        db.collection("users")
                .whereGreaterThan("age",startAge-1)
                .whereLessThan("age", endAge+1).whereArrayContainsAny("hobbies", Arrays.asList(myArray))
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        userList = queryDocumentSnapshots.toObjects(User.class);
                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG)
                                .show();
                        pd.dismiss();
                    }
                });
    }
}