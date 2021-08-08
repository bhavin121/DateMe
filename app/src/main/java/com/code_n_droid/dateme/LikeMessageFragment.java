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

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.system.Os.remove;

public class LikeMessageFragment extends Fragment {

    public LikeMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager2 pager2 = view.findViewById(R.id.pager);
        LikeMessageAdapter adapter = new LikeMessageAdapter(null, new Listener() {
            @Override
            public void onAccept(String email) {
                accept(email);
            }

            @Override
            public void onRemoveAction(String email) {
                delete(email);
            }
        });

        pager2.setAdapter(adapter);
        pager2.setClipToPadding(false);
        pager2.setClipChildren(true);
        pager2.setOffscreenPageLimit(2);
    }

    private void delete(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("likes")
                .document(AppData.user.getEmail());
        ref.update("likers", FieldValue.arrayRemove(email))
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Something Went wrong", Toast.LENGTH_SHORT).show();
                });
    }

    private void accept(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("likes")
                .document(email);
        ref.update("likers", FieldValue.arrayUnion(AppData.user.getEmail()))
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Something Went wrong", Toast.LENGTH_SHORT).show();
                });

        DocumentReference ref1 = db.collection("likes")
                .document(AppData.user.getEmail());
        ref1.update("likers", FieldValue.arrayUnion(email))
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Something Went wrong", Toast.LENGTH_SHORT).show();
                });
    }

    public interface Listener{
        void onAccept(String email);
        void onRemoveAction(String email);
    }
}