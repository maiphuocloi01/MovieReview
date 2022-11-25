package com.cnjava.moviereview.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("phone")
    private String phone;
    @SerializedName("gender")
    private String gender;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("username")
    private String username;
    @SerializedName("locked")
    private boolean locked;
    @SerializedName("enabled")
    private boolean enabled;
    @SerializedName("reviews")
    private List<Review> reviews;

    public User(String name, String avatar, String phone, String gender, String birthday) {
        this.name = name;
        this.avatar = avatar;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
    }

    public User(String name, String phone, String gender, String birthday) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password) {
        this.password = password;
        this.email = email;
    }

    public User(String email, String name, boolean enabled) {
        this.name = name;
        this.email = email;
        this.enabled = enabled;
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
