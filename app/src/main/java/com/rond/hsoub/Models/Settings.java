package com.rond.hsoub.Models;

/**
 * Created by Nullsky on 6/24/2017.
 */

public class Settings {
    public String website;
    public String details;
    public boolean allow_messages;
    public boolean email_messages;
    public boolean interests_email;
    public boolean hide_last_sign_in_at;
    public boolean private_favorites;
    public boolean embed_images;

    public Settings(String website, String details, boolean allow_messages, boolean email_messages, boolean interests_email, boolean hide_last_sign_in_at, boolean private_favorites, boolean embed_images) {
        this.website = website;
        this.details = details;
        this.allow_messages = allow_messages;
        this.email_messages = email_messages;
        this.interests_email = interests_email;
        this.hide_last_sign_in_at = hide_last_sign_in_at;
        this.private_favorites = private_favorites;
        this.embed_images = embed_images;
    }
}
