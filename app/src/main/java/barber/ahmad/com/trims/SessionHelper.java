package barber.ahmad.com.trims;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionHelper {

    public static void createLoginSession(Context context, JSONObject userObject){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs.edit()
                .putBoolean("is_user_logged_in",true)
                .putString("logged_user",userObject.toString())
                .apply();
    }
    public static void createLoginSession(Context context, String userObject){

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs.edit()
                .putBoolean("is_user_logged_in",true)
                .putString("logged_user",userObject)
                .apply();
    }
    public static void logout(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs.edit()
                .remove("is_user_logged_in")
                .remove("logged_user")
                .apply();
    }

    public static Boolean isUserLoggedIn(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean  isUserLoggedIn = sharedPrefs.getBoolean("is_user_logged_in",false);
        return isUserLoggedIn;
    }


    public static JSONObject getCurrentUser(Context context){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String userJson = sharedPrefs.getString("logged_user" ,null);
        try {
            JSONObject userObject = new JSONObject(userJson);
            return  userObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
