package guru.pmvungu.example.com.guru;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;

import java.util.HashMap;

public class SessionManager {


	 // Shared Preferences
    SharedPreferences pref;
     
    // Editor for Shared preferences
    Editor editor;
     
    // Context
    Context _context;
     
    // Shared pref mode
    int PRIVATE_MODE = 0;
     
    // Sharedpref file name
    private static final String PREF_NAME = "africell_simreg";
     
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_FINGER = "Isfinger";
     
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public static final String KEY_FINGER = "finger";
    public static String fg = "0";
    public static Bitmap bitmap;
    public  static byte[] bytedata1;

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
   // public static final String KEY_FINGER = "finger";
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
     
    /**
     * Create login session
     * */
    public void createLoginSession(String us_login){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
         
        // Storing name in pref
        editor.putString(KEY_NAME, us_login);
         
        // Storing email in pref
     //   editor.putString(KEY_EMAIL, email);
         
        // commit changes
        editor.commit();
    }


    public void createEmpreinteSession(String us_finger){
        // Storing login value as TRUE
        editor.putBoolean(IS_FINGER, true);
        // Storing name in pref
        editor.putString(KEY_FINGER, us_finger);

        // Storing email in pref
        //   editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }
    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, _context.getClass());
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             
            // Staring Login Activity
            _context.startActivity(i);
        }
         
    }
     
     
     
    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
         
        // user email id
    //    user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
         
        // return user
        return user;
    }

    public HashMap<String, String> getFingerDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_FINGER, pref.getString(KEY_FINGER, null));

        // user email id
        //    user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
         
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, _context.getClass());
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         
        // Staring Login Activity
        _context.startActivity(i);
    }
     
    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
