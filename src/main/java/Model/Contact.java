package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class Contact {
    private String name;
    private String phone;

    public Contact() {}

    public Contact(String name, String phone){
        this.name = name;
        this.phone = phone;
    }
    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }

    public void setName(String val){
        name = val;
    }

    public void setPhone(String val){
        phone = val;
    }

    private static ArrayList<Contact> contacts = null;

    public static ArrayList<Contact> getContacts(){
        if(contacts == null){
            contacts = new ArrayList<Contact>();
            loadContacts(null);
        }
        return contacts;
    }

    public static void loadContacts(String path){
        String _path = "./contacts.json";
        if(path == null)
            path = _path;
        contacts  = new ArrayList<Contact>();

        try{
            FileReader fr = new FileReader(path);
            Gson gson = new Gson();
            Contact[] loadedContacts = gson.fromJson(new JsonReader(fr), Contact[].class);
            contacts.addAll(Arrays.asList(loadedContacts));

        }catch(Exception ex){
            ex.printStackTrace();

        }
    }

    public static void saveContacts(){
        saveContacts(null, null);
    }

    public static void saveContacts(Object contacts, String path){
        String _path = "./contacts.json";
        ArrayList<Contact> _contacts = Contact.getContacts();
        if(path == null)
            path = _path;

        if(contacts == null)
            contacts = _contacts;

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        FileWriter fw = null;
        try {
        fw = new FileWriter(_path);
            gson.toJson(contacts, fw);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
