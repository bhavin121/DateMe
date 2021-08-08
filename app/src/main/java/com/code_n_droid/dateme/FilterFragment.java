package com.code_n_droid.dateme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FilterFragment extends Fragment {

    private Listener listener;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CheckBox age, gender;
        EditText startVal, endVal;
        RadioGroup genderFilter = view.findViewById(R.id.genderFilter);
        age = view.findViewById(R.id.age);
        gender = view.findViewById(R.id.gender);
        startVal = view.findViewById(R.id.ageStartVal);
        endVal = view.findViewById(R.id.ageEndVal);

        view.findViewById(R.id.applyFilter).setOnClickListener(view1 -> {
            if(age.isChecked()){
                Toast toast = Toast.makeText(view.getContext(),"Invalid Age Filter", Toast.LENGTH_LONG);
                if(startVal.getText().toString().isEmpty() || endVal.getText().toString().isEmpty()){
                    toast.show();
                    return;
                }
                try{
                    int start = Integer.parseInt(startVal.getText().toString());
                    int end = Integer.parseInt(endVal.getText().toString());

                    if(start <= end){
                        AppData.ageFilter = true;
                        AppData.startingAge = start;
                        AppData.endAge = end;
                    }else{
                        toast.show();
                        return;
                    }
                }catch (Exception e){
                    toast.show();
                    return;
                }
            }

            if(gender.isChecked()){
                AppData.genderFilter = true;
                AppData.gender = (genderFilter.getCheckedRadioButtonId() == R.id.male)?Users.MALE:Users.FEMALE;
            }

            if(listener != null){
                listener.onApplyFilter();
            }
        });

    }

    public FilterFragment setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public interface Listener{
        void onApplyFilter();
    }
}