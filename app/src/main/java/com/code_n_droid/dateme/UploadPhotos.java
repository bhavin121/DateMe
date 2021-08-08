package com.code_n_droid.dateme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.UUID;

public class UploadPhotos extends AppCompatActivity {

    public static final int PICK_IMAGE = 100;
    private TextView noItem;
    private LinearLayout layout;
    private int uploadCount = 0;
    private final ArrayList<String> photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photos);

        noItem = findViewById(R.id.noImageChosen);
        layout = findViewById(R.id.chosenImageContainer);

        findViewById(R.id.pickImageButton).setOnClickListener(view -> {
            if(uploadCount > 6){
                Toast.makeText(this, "You can add max 6 photos ", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        findViewById(R.id.nextButton).setOnClickListener(view -> {
            if(uploadCount < 2){
                Toast.makeText(this, "You have to add at least 2 photos ", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putStringArrayListExtra(AddDetailsActivity.PHOTOS_KEY, photos);
            intent.putExtra(AddDetailsActivity.PASSWORD_KEY, getIntent().getStringExtra(AddDetailsActivity.PASSWORD_KEY));
            intent.putExtra(AddDetailsActivity.EMAIL_KEY, getIntent().getStringExtra(AddDetailsActivity.EMAIL_KEY));
            startActivity(intent);
        });
    }

    private void uploadPhoto(Uri photo) {

        if(photo == null){
            Toast.makeText(this, "Pick A Photo", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference reference = firebaseStorage.getReference();

        StorageReference photoReference = reference.child("images/"+ UUID.randomUUID().toString());

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Photo...");
        dialog.setCancelable(false);
        dialog.show();

        photoReference.putFile(photo)
                .addOnSuccessListener(taskSnapshot -> {
                    dialog.dismiss();
                    uploadCount++;
                    photos.add(photoReference.getPath());
                    Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    layout.removeViewAt(layout.getChildCount()-1);
                    Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri photo = data.getData();
            addPhoto(photo);
            uploadPhoto(photo);
            Toast.makeText(this, photo.getPath(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void addPhoto(Uri photo) {
        noItem.setVisibility(View.GONE);
        View view = getLayoutInflater().inflate(R.layout.image_item, layout, false);
        ((AppCompatImageView)view.findViewById(R.id.photo)).setImageURI(photo);
        layout.addView(view);
    }
}