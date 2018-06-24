package sg.redapp.com.redappdriver.functions;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.Set;

import sg.redapp.com.redappdriver.R;

public class SharedPreferenceStorage {
    private Context context;
    private SharedPreferences sharedPreferences;


    public SharedPreferenceStorage(Context context){
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void StoreString(String key, String value){
        sharedPreferences = getContext().getSharedPreferences(getContext().getResources().getString(R.string.sharedpreference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public void StoreInt(String key, int value){
        sharedPreferences = getContext().getSharedPreferences(getContext().getResources().getString(R.string.sharedpreference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public String getString(String key){
        sharedPreferences = getContext().getSharedPreferences(getContext().getResources().getString(R.string.sharedpreference), Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
    public int getInt(String key){
        sharedPreferences = getContext().getSharedPreferences(getContext().getResources().getString(R.string.sharedpreference), Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,-1);
    }
}
