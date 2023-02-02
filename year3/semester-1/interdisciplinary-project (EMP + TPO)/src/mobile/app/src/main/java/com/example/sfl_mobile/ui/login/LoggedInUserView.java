package com.example.sfl_mobile.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String username;
    private String password;
    private String role;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String username, String password, String role) {
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    String getDisplayName() {
        return displayName;
    }

    String getUserName() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getRole() {
        return role;
    }
}