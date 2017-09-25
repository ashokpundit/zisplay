package vun.zisplaymerchant.models;

//import com.activeandroid.Model;

/**
 * Created by ashok on 2/6/15.
 */
public class SettingsData {
    private static SettingsData ourInstance = new SettingsData();

    public static SettingsData getInstance() {
        return ourInstance;
    }

    private SettingsData() {
    }
}
