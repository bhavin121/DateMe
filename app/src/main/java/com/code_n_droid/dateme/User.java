package com.code_n_droid.dateme;

import java.util.HashMap;
import java.util.List;

public class User {

    private String name;
    private String email;
    private String password;
    private String bio;
    private int age;
    private String gender;
    private List<String> photos;
    private float latitude, longitude;
    private String location;
    private List<String> hobbies;

    public User() {
    }

    public User(String name, String email, String password, String bio, int age, String gender, List<String> photos, float latitude, float longitude, String location, List<String> hobbies) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.age = age;
        this.gender = gender;
        this.photos = photos;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.hobbies = hobbies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }
}

abstract class Users{
    // Gender Constants
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    // Hobbies List
    public static final String MAGIC = "magic";
    public static final String DANCE = "dance";
    public static final String SINGING = "singing";
    public static final String PAINTING = "painting";
    public static final String ACTING = "acting";
    public static final String SPORTS = "sports";
    public static final String ADVENTURE = "adventure";
    public static final String WRITING = "writing";
    public static final String TRAVEL = "travel";
    public static final String READING = "reading";
    public static final String BLOGGING = "blogging";
    public static final String MUSIC = "music";

    public static HashMap<Integer, String> map = new HashMap<>();

    // static block
    static {
        map.put(R.id.magic, Users.MAGIC);
        map.put(R.id.dance, Users.DANCE);
        map.put(R.id.singing, Users.SINGING);
        map.put(R.id.painting, Users.PAINTING);
        map.put(R.id.acting, Users.ACTING);
        map.put(R.id.sports, Users.SPORTS);
        map.put(R.id.adventure, Users.ADVENTURE);
        map.put(R.id.writing, Users.WRITING);
        map.put(R.id.travel, Users.TRAVEL);
        map.put(R.id.reading, Users.READING);
        map.put(R.id.blogging, Users.BLOGGING);
        map.put(R.id.music, Users.MUSIC);
    }
}