package com.code_n_droid.dateme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LikesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> userList = new LinkedList<>();

    public LikesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.likesListRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        PersonListAdapter adapter = new PersonListAdapter(userList);
        recyclerView.setAdapter(adapter);
    }

    public void getLikers(String email) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("likes")
                .whereEqualTo("email", "email")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        queryDocumentSnapshots.toObjects(LikesList.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG)
                                .show();
                    }
                });

    }
}