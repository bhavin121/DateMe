package com.code_n_droid.dateme;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class PhotoLoadHelper {

    public static void getImageUrl(String path){
        StorageReference reference = FirebaseStorage.getInstance().getReference(path);
        reference.getDownloadUrl();
    }

}
