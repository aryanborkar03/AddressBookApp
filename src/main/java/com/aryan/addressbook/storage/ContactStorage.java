package com.aryan.addressbook.storage;

import com.aryan.addressbook.model.Contact;
import java.util.List;

public interface ContactStorage {

    void save(String filePath, List<Contact> contacts);

    List<Contact> load(String filePath);
}