package com.code_n_droid.dateme;

import java.util.List;

public class LikesList {
    private List<String> likers;

    public LikesList() {
    }

    public LikesList(List<String> likers) {
        this.likers = likers;
    }

    public List<String> getLikers() {
        return likers;
    }

    public void setLikers(List<String> likers) {
        this.likers = likers;
    }
}
