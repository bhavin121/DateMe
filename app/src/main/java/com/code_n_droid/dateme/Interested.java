package com.code_n_droid.dateme;

import java.util.List;

public class Interested {
    private List<String> interestedPeoples;

    public Interested() {
    }

    public Interested(List<String> interestedPeoples) {
        this.interestedPeoples = interestedPeoples;
    }

    public List<String> getInterestedPeoples() {
        return interestedPeoples;
    }

    public void setInterestedPeoples(List<String> interestedPeoples) {
        this.interestedPeoples = interestedPeoples;
    }
}
