package com.aryan.addressbook.util;

import com.aryan.addressbook.model.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONUtil {

    private static final Gson gson = new Gson();

    public static void writeContactsToJSON(String filePath, List<Contact> contacts) {

        try(FileWriter writer = new FileWriter(filePath)) {

            gson.toJson(contacts, writer);

        } catch(Exception e) {
            throw new RuntimeException("JSON write error", e);
        }
    }

    public static List<Contact> readContactsFromJSON(String filePath) {

        try(FileReader reader = new FileReader(filePath)) {

            Type contactListType = new TypeToken<List<Contact>>(){}.getType();

            List<Contact> contacts = gson.fromJson(reader, contactListType);

            return contacts != null ? contacts : new ArrayList<>();

        } catch(Exception e) {
            throw new RuntimeException("JSON read error", e);
        }
    }
}