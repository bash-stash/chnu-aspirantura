package com.chnu.aspirantura;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fibs on 04.02.2018.
 */

public class Settings implements Serializable {
    private static HashMap<String,String> settings = new HashMap<String,String>();

    public static void load() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("settings.dat"));
            settings = (HashMap<String, String>) in.readObject();
            System.out.println("Loading..");
            for(Map.Entry<String, String> s: settings.entrySet()) System.out.println(s.getKey()+" - " + s.getValue());
            System.out.println("Loaded!");

        }catch (Exception e){
            System.out.println(e);
            setDefault();
        }
    }

    public static void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("settings.dat"));
            out.writeObject(settings);
            out.close();

            System.out.println("Saving..");
            for(Map.Entry<String, String> s: settings.entrySet()) System.out.println(s.getKey()+" - " + s.getValue());
            System.out.println("Saved!");
        } catch (Exception e) {
            System.out.println("ERROR in saveing: " + e);
        }
    }



    public static String getSettings(String key) {
        if (settings.containsKey(key)) return settings.get(key);
        return "";
    }

    public static void setSettings(String key, String value) {
        if (settings.containsKey(key)) settings.put(key,value);
        else System.out.println("ERROR!");
    }

    public static void setDefault()  {
    //AspiranturaMenuView
    settings.put("menu_item_name_aspirantura","checked");
    settings.put("menu_item_status_aspirantura","checked");
    settings.put("menu_item_birthday_aspirantura","checked");
    settings.put("menu_item_passport_aspirantura","checked");
    settings.put("menu_item_contacts_aspirantura","checked");
    settings.put("menu_item_identkod_aspirantura","checked");
    settings.put("menu_item_speciality_aspirantura","checked");
    settings.put("menu_item_directir_aspirantura","checked");
    settings.put("menu_item_scienceactivity_aspirantura","checked");

    //KerivnikiMenuView
    settings.put("menu_item_name_kerivniki","checked");
    settings.put("menu_item_status_kerivniki","checked");
    settings.put("menu_item_speciality_kerivniki","checked");

    System.out.println("Set to default!");
    save();
    }
}
