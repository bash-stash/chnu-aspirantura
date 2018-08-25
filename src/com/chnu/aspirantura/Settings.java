package com.chnu.aspirantura;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Properties properties = new Properties();
    private static Properties propertiesDatabase = new Properties();


    private static final String APP_COMMON = "common.properties";
    private static final String DB_CFG = "database.properties";



    public static void loadDatabaseProperties() {
        try (FileInputStream fis = new FileInputStream(DB_CFG)) {
            propertiesDatabase.load(fis);
        } catch (IOException e) {
            setDefaultDatabaseProperties();
        }
    }


    public static String getDatabaseProperty(String key) {
        if (propertiesDatabase.containsKey(key)) return (String) propertiesDatabase.get(key);
        return "";
    }


    public static void savePropertiesToFile() {
        try (FileOutputStream out = new FileOutputStream(APP_COMMON)) {
            properties.store(out, null);
        } catch (Exception e) {
            Utils.logger.error("Error saving properties to file: "+e.getMessage());
        }
    }



    public static void savePropertiesToFile(Properties p, final String name) {
        try (FileOutputStream out = new FileOutputStream(name)) {
            p.store(out, null);
        } catch (Exception e) {
           Utils.logger.error("Error saving properties to file: "+e.getMessage());
        }
    }


    public static void loadProperties() {
        try (FileInputStream fis = new FileInputStream(APP_COMMON)) {
            properties.load(fis);
        } catch (IOException e) {
            setDefault();
        }
    }


    public static String getProperty(String key) {
        if (properties.containsKey(key)) return (String) properties.get(key);
        return "";
    }

    public static void setSettings(String key, String value) {
        properties.put(key, value);
        savePropertiesToFile(properties,APP_COMMON);
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

        savePropertiesToFile(properties,APP_COMMON);
    }

    public static void setDefaultDatabaseProperties() {
        propertiesDatabase.setProperty("database.host", "localhost");
        propertiesDatabase.setProperty("database.port", "3306");
        propertiesDatabase.setProperty("database.name", "mydb");
        propertiesDatabase.setProperty("database.user", "root");
        propertiesDatabase.setProperty("database.pass", "");

        savePropertiesToFile(propertiesDatabase,DB_CFG);
    }
}
