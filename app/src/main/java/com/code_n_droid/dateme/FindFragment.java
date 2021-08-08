package com.code_n_droid.dateme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FindFragment extends Fragment {

    public FindFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 pager2 = view.findViewById(R.id.pager);
        FindFeedAdapter adapter = new FindFeedAdapter(null, email -> uploadData(email));

        pager2.setAdapter(adapter);
        pager2.setClipToPadding(false);
        pager2.setClipChildren(true);
        pager2.setOffscreenPageLimit(2);
    }

    private void uploadData(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("likes")
                .document(email);
        ref.update("likers", FieldValue.arrayUnion(AppData.user.getEmail()))
        .addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Something Went wrong", Toast.LENGTH_SHORT).show();
        });
    }

    public interface Listener{
        void onLikeAction(String email);
    }
}