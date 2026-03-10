package com.aryan.addressbook.storage;

import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.util.CSVUtil;

import java.util.List;

public class CSVStorage implements ContactStorage {

    @Override
    public void save(String filePath, List<Contact> contacts) {
        CSVUtil.writeContactsToCSV(filePath, contacts);
    }

    @Override
    public List<Contact> load(String filePath) {
        return CSVUtil.readContactsFromCSV(filePath);
    }
}