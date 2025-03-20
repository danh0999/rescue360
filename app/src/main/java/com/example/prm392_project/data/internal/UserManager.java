package com.example.prm392_project.data.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;


import com.example.prm392_project.data.models.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class UserManager {
    private static final String PREF_NAME = "rescue_secure_prefs";
    private static final String IS_ADMIN_KEY = "is_admin";
    private static final String USER_INFO_KEY = "user_info";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public UserManager(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            gson = new Gson();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    // Save isAdmin status
    public void saveIsAdmin(boolean isAdmin) {
        Log.i("UserManager", "Saving isAdmin status: " + isAdmin);
        sharedPreferences.edit().putBoolean(IS_ADMIN_KEY, isAdmin).apply();
    }

    // Retrieve isAdmin status
    public boolean isAdmin() {
        return sharedPreferences.getBoolean(IS_ADMIN_KEY, false);
    }

    // Save user info
    public void saveUser(User user) {
        Log.i("UserManager", "Saving user info: " + user);
        String userJson = gson.toJson(user);
        sharedPreferences.edit().putString(USER_INFO_KEY, userJson).apply();
    }

    // Retrieve user info
    public User getUser() {
        String userJson = sharedPreferences.getString(USER_INFO_KEY, null);
        return userJson != null ? gson.fromJson(userJson, User.class) : null;
    }

    // Clear user data
    public void clearUser() {
        sharedPreferences.edit().remove(IS_ADMIN_KEY).remove(USER_INFO_KEY).apply();
    }
}
