package com.chnu.aspirantura;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Properties properties = new Properties();
    private static final String FILE_NAME = "common.properties";


    public static void loadProperties() {
        try (FileInputStream fis = new FileInputStream(FILE_NAME)) {
            properties.load(fis);
        } catch (IOException e) {
            setDefault();
        }
    }


    public static void savePropertiesToFile() {
        try (FileOutputStream out = new FileOutputStream(FILE_NAME);) {
            properties.store(out, null);
        } catch (Exception e) {
            System.out.println("ERROR in saving: " + e);
        }
    }


    public static String getSettings(String key) {
        if (properties.containsKey(key)) return (String) properties.get(key);
        return "";
    }

    public static void setSettings(String key, String value) {
        properties.put(key, value);
        savePropertiesToFile();
    }

    public static void setDefault() {
        properties.setProperty("item.name.aspirantura.checked", "true");
        properties.setProperty("item.birthday.aspirantura.checked", "true");
        properties.setProperty("item.status.aspirantura.checked", "true");
        properties.setProperty("item.speciality.aspirantura.checked", "true");
        properties.setProperty("item.kerivnik.aspirantura.checked", "true");
        properties.setProperty("item.note.aspirantura.checked", "true");
        properties.setProperty("item.year.aspirantura.checked", "true");
        properties.setProperty("item.form.aspirantura.checked", "true");


        properties.setProperty("item.name.kerivniki.checked", "true");
        properties.setProperty("item.status.kerivniki.checked", "true");
        properties.setProperty("item.speciality.kerivniki.checked", "true");

        savePropertiesToFile();
    }
}
