package com.rond.hsoub.Models;

/**
 * Created by Nullsky on 11/23/2016.
 */

public class community {
    public String name;
    public String ID;
    public String description;
    public boolean isFollowing;
    public String followers;
    public String number;

    public community(String name, String ID, String description, boolean isFollowing, String followers, String number) {
        this.name = name;
        this.ID = ID;
        this.description = description;
        this.isFollowing = isFollowing;
        this.followers = followers;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

}
