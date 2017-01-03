package com.rond.hsoub.API;

/**
 * Created by Nullsky on 12/23/2016.
 */

public enum ConnectionMethod {
    GET ("GET"),
    POST ("POST"),
    PUT ("PUT");

    private final String name;

    ConnectionMethod(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return otherName != null && name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
