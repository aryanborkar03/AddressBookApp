package com.aryan.addressbook.util;

import com.aryan.addressbook.model.Contact;

import java.io.*;
import java.util.*;

public class FileUtil {

    public static void writeContactsToFile(String filePath, List<Contact> contacts) {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for(Contact contact : contacts) {

                writer.write(
                        contact.getFirstName() + "," +
                        contact.getLastName() + "," +
                        contact.getCity() + "," +
                        contact.getState() + "," +
                        contact.getZip()
                );

                writer.newLine();
            }

        } catch(IOException e) {
            throw new RuntimeException("Error writing file", e);
        }
    }


    public static List<Contact> readContactsFromFile(String filePath) {

        List<Contact> contacts = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                Contact contact = new Contact(
                        data[0],
                        data[1],
                        "",
                        data[2],
                        data[3],
                        data[4],
                        "",
                        ""
                );

                contacts.add(contact);
            }

        } catch(IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        return contacts;
    }
}