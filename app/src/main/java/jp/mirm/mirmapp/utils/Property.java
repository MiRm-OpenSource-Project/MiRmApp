package jp.mirm.mirmapp.utils;

import android.os.Environment;
import jp.mirm.mirmapp.MainApplication;
import jp.mirm.mirmapp.R;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class Property {

    private Properties properties;
    private final String file;

    public Property(String serverName) {
        this.file = Environment.getExternalStorageDirectory().getPath() + "/servers/" + serverName + "/setting.properties";
        this.properties = new Properties();

        try {
            if (new File(file).exists()) properties.load(new FileInputStream(new File(file)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void putAll(Map<String, Object> data) {
        properties.putAll(data);
    }

    public void setPassword(String password) {
        properties.setProperty("password", password);
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public void setServerId(String serverId) {
        properties.setProperty("serverId", serverId);
    }

    public String getServerId() {
        return properties.getProperty("serverId");
    }

    public void setAutoLogin(boolean bool) {
        properties.setProperty("autoLogin", String.valueOf(bool));
    }

    public boolean getAutoLogin() {
        return Boolean.parseBoolean(properties.getProperty("autoLogin"));
    }

    public void setNextReward(long unixTime) {
        properties.setProperty("nextReward", String.valueOf(unixTime));
    }

    public long getNextReward() {
        return Long.valueOf(properties.getProperty("nextReward"));
    }

    public void setSaveLog(boolean bool) {
        properties.setProperty("saveLog", String.valueOf(bool));
    }

    public boolean getSaveLog() {
        return Boolean.parseBoolean(properties.getProperty("saveLog"));
    }

    public void setConsoleTick(int tick) {
        properties.setProperty("consoleTick", String.valueOf(tick));
    }

    public int getConsoleTick() {
        return Integer.valueOf(properties.getProperty("consoleTick"));
    }

    public void setBackupDirectory(String path) {
        properties.setProperty("backupDirectory", path);
    }

    public String getBackupDirectory() {
        return properties.containsKey("backupDirectory") ? properties.getProperty("backupDirectory") : null;
    }

    public String getDate() {
        return properties.getProperty("date");
    }

    public void save() {
        try {
            properties.setProperty("date", new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPANESE).format(new Date()));
            FileWriter writer = new FileWriter(new File(file));
            properties.store(writer, MainApplication.getInstance().getResources().getString(R.string.data_property_comment));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Property fromFile(File file) {
        return new Property(file.getName().replaceAll(".properties", ""));
    }

    @Override
    public String toString() {
        if (properties != null) {
            return properties.toString();
        } else {
            return null;
        }
    }
}
