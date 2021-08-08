package com.code_n_droid.dateme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.MessageFormat;
import java.util.List;

public class PersonListAdapter extends RecyclerView.Adapter<PersonVH> {

    private List<User> users;

    public PersonListAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public PersonVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_card, parent, false);
        return new PersonVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonVH holder, int position) {
//        User user = users.get(position);
//        // Add Image Loading
//        holder.nameAge.setText(MessageFormat.format("{0}, {1}", user.getName(), user.getAge()));
//        holder.location.setText(user.getLocation());
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}

class PersonVH extends RecyclerView.ViewHolder{

    AppCompatImageView photo;
    TextView nameAge, location;

    public PersonVH(@NonNull View itemView) {
        super(itemView);
        photo = itemView.findViewById(R.id.photo);
        nameAge = itemView.findViewById(R.id.name);
        location = itemView.findViewById(R.id.location);
    }
}
