package ameerhamza6733.championtrophy2017schedule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Set;

/**
 * Created by AmeerHamza on 3/4/2017.
 */

public class myPraf {

    public static void saveObjectToSharedPreference(Context context,  String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = mySinglton.getInStance(context);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();

    }
    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = mySinglton.getInStance(context);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }
    public static Set getAllPraf(SharedPreferences prefs)
    {
        Map<String,?> entries = prefs.getAll();
        Set<String> keys = entries.keySet();
        for (String key : keys) {

            Log.d("CustomAdapter",key);

        }
        Log.d("CustomAdapter",""+keys.size());
        return keys;
    }

    public static void deletePraf(Context context  , String mPrafKey)
    {
        SharedPreferences settings = mySinglton.getInStance(context);
        settings.edit().remove(mPrafKey).apply();
    }
    public static int getPrafSize(Context context)
    {
        Map<String,?> entries = mySinglton.getInStance(context).getAll();
        Set<String> keys = entries.keySet();
        return keys.size();
    }
}
