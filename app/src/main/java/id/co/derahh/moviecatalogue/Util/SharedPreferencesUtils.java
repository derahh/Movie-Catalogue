package id.co.derahh.moviecatalogue.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    public static void setInsertState(Context context, String id ,boolean isAlreadyLoved){
        SharedPreferences sharedpreferences = context.getSharedPreferences("insert_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(id, isAlreadyLoved);
        editor.apply();
    }

    public static boolean getInsertState(Context context, String id){
        SharedPreferences sharedpreferences = context.getSharedPreferences("insert_data", Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(id, false);
    }

    public static void clearSharedPreferences(Context context){
        context.getSharedPreferences("insert_data", Context.MODE_PRIVATE).edit().clear().apply();
    }
}
