package com.aryan.addressbook.storage;

import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.util.JSONUtil;

import java.util.List;

public class JSONStorage implements ContactStorage {

    @Override
    public void save(String filePath, List<Contact> contacts) {
        JSONUtil.writeContactsToJSON(filePath, contacts);
    }

    @Override
    public List<Contact> load(String filePath) {
        return JSONUtil.readContactsFromJSON(filePath);
    }
}