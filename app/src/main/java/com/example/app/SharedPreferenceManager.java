package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {

    private static final String SHARED_PREF_NAME = "user_prefs";
    private static final String KEY_FARMER_ID = "farmerID";
    private static final String KEY_RSBSA_NUM = "rsbsa_num";

    private static SharedPreferenceManager instance;
    private static Context ctx;

    private SharedPreferenceManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    // Save farmerID
    public void saveFarmerID(int farmerID) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_FARMER_ID, farmerID);
        editor.apply();
    }

    // Retrieve farmerID
    public int getFarmerID() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_FARMER_ID, -1); // Default is -1 if not found
    }

    // Clear farmerID
    public void clearFarmerID() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_FARMER_ID);
        editor.apply();
    }

    // Save rsbsa_num
    public void saveRSBSANum(String rsbsaNum) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_RSBSA_NUM, rsbsaNum); // Save the rsbsa_num as a String
        editor.apply();
    }

    // Retrieve rsbsa_num
    public String getRSBSANum() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_RSBSA_NUM, null); // Default is null if not found
    }

    // Clear rsbsa_num
    public void clearRSBSANum() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_RSBSA_NUM); // Remove the rsbsa_num key
        editor.apply();
    }
}
