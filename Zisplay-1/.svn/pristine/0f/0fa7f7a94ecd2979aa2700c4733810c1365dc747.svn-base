package vun.zisplay.managers;

import android.content.Context;
import android.content.SharedPreferences;


import vun.zisplay.Zisplay;
import zisplay.vun.zisplay.R;

/**
 * Created by ashok on 2/12/15.
 */
public class LocalData {

    static private LocalData localData=null;
    private SharedPreferences sharedPref=null;
    private Context context;
    public static LocalData getInstance()
    {
        if(localData==null)
        {
            localData=new LocalData();
        }
        return localData;
    }

    LocalData()
    {
        context = Zisplay.getInstance().getApplicationContext();;
        sharedPref = context.getSharedPreferences(
        context.getString(R.string.local_data_file_name), Context.MODE_PRIVATE);
    }

    public void saveData(String name, String value)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public String getData(String key)
    {
        return sharedPref.getString(key, "");
    }

    public void saveData(String name, boolean value)
    {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public Boolean getBooleanData(String key)
    {
        return sharedPref.getBoolean(key, false);
    }


}
