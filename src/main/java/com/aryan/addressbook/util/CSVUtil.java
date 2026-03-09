package com.aryan.addressbook.util;

import com.aryan.addressbook.model.Contact;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter; 
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    public static void writeContactsToCSV(String filePath, List<Contact> contacts) {

        try(CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {

            for(Contact contact : contacts) {

                String[] data = {
                        contact.getFirstName(),
                        contact.getLastName(),
                        contact.getCity(),
                        contact.getState(),
                        contact.getZip()
                };

                writer.writeNext(data);
            }

        } catch(Exception e) {
            throw new RuntimeException("CSV write error", e);
        }
    }

    public static List<Contact> readContactsFromCSV(String filePath) {

        List<Contact> contacts = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {

            String[] data;

            while ((data = reader.readNext()) != null) {

                // skip empty rows
                if (data.length == 0 || data[0].trim().isEmpty())
                    continue;

                // ensure minimum columns exist
                if (data.length < 5)
                    continue;

                Contact contact = new Contact(
                        data[0],  // first name
                        data[1],  // last name
                        "",
                        data[2],  // city
                        data[3],  // state
                        data[4],  // zip
                        "",
                        ""
                );

                contacts.add(contact);
            }

        } catch (Exception e) {
            throw new RuntimeException("CSV read error", e);
        }

        return contacts;
    }
}