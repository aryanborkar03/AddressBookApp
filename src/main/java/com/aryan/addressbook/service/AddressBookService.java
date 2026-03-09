package com.aryan.addressbook.service;

import com.aryan.addressbook.model.AddressBook;
import com.aryan.addressbook.model.Contact;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AddressBookService {

    private Map<String, AddressBook> addressBooks = new HashMap<>();

    public Contact addContact(String bookName, Contact contact) {

        AddressBook book = addressBooks.get(bookName);

        if(book == null) {
            book = new AddressBook(bookName);
            addressBooks.put(bookName, book);
        }

        boolean duplicate = book.getContacts()
                .stream()
                .anyMatch(existing ->
                        existing.getFirstName().equals(contact.getFirstName()) &&
                        existing.getLastName().equals(contact.getLastName())
                );

        if(duplicate) {
            throw new RuntimeException("Duplicate contact not allowed");
        }

        book.addContact(contact);

        return contact;
    }

    public Contact updateContact(String bookName,
            String firstName,
            String lastName,
            Contact updatedContact) {

        AddressBook book = addressBooks.get(bookName);

        if (book == null) {
            return null;
        }

        for (Contact contact : book.getContacts()) {

            if (contact.getFirstName().equals(firstName) &&
                    contact.getLastName().equals(lastName)) {

                contact.setAddress(updatedContact.getAddress());
                contact.setCity(updatedContact.getCity());
                contact.setState(updatedContact.getState());
                contact.setZip(updatedContact.getZip());
                contact.setPhoneNumber(updatedContact.getPhoneNumber());
                contact.setEmail(updatedContact.getEmail());

                return contact;
            }
        }

        return null;
    }

    public boolean deleteContact(String bookName,
            String firstName,
            String lastName) {

        AddressBook book = addressBooks.get(bookName);

        if (book == null) {
            return false;
        }

        return book.getContacts().removeIf(contact -> contact.getFirstName().equals(firstName) &&
                contact.getLastName().equals(lastName));
    }
    
    public List<Contact> getContacts(String bookName) {

        AddressBook book = addressBooks.get(bookName);

        if (book == null) {
            return new ArrayList<>();
        }

        return book.getContacts();
    }

    public AddressBook createAddressBook(String name) {

        if(addressBooks.containsKey(name)) {
            return addressBooks.get(name);
        }

        AddressBook book = new AddressBook(name);
        addressBooks.put(name, book);

        return book;
    }

    public Map<String, AddressBook> getAllAddressBooks() {
        return addressBooks;
    }
    
    public AddressBook getAddressBook(String name) {
        return addressBooks.get(name);
    }
}