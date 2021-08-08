package com.code_n_droid.dateme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.text.MessageFormat;
import java.util.List;

public class FindFeedAdapter extends RecyclerView.Adapter<FindFeedVH> {

    public List<User> users;
    public FindFragment.Listener listener;

    public FindFeedAdapter(List<User> users,@NonNull FindFragment.Listener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FindFeedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_card,parent, false);
        return new FindFeedVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindFeedVH holder, int position) {
        User user = users.get(position);

        holder.remove.setOnClickListener(view -> {
            users.remove(position);
            notifyItemRemoved(position);
        });
        holder.photoClick.setOnClickListener(view -> {

        });
        holder.like.setOnClickListener(view -> {
            listener.onLikeAction(user.getEmail());
            users.remove(position);
            notifyItemRemoved(position);
        });

        holder.nameAge.setText(MessageFormat.format("{0}, {1}", user.getName(), user.getAge()));
        holder.location.setText(user.getLocation());

        StorageReference reference = FirebaseStorage.getInstance().getReference(user.getPhotos().get(0));
            reference.getDownloadUrl().addOnSuccessListener(uri ->
                    Glide.with(holder.itemView.getContext())
                    .load(uri)
                    .into(holder.photo))
            .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}

class FindFeedVH extends RecyclerView.ViewHolder{

    ShapeableImageView photo;
    View photoClick;
    TextView nameAge, location;
    MaterialButton remove, like;


    public FindFeedVH(@NonNull View itemView) {
        super(itemView);

        photo = itemView.findViewById(R.id.photo);
        photoClick = itemView.findViewById(R.id.imageClick);
        nameAge = itemView.findViewById(R.id.name);
        location = itemView.findViewById(R.id.location);
        remove = itemView.findViewById(R.id.remove);
        like = itemView.findViewById(R.id.like);
    }
}
