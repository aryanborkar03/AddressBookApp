package com.aryan.addressbook.service;

import com.aryan.addressbook.model.AddressBook;
import com.aryan.addressbook.model.Contact;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.*;

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
    
    public List<Contact> searchByCity(String city) {

        return addressBooks.values()
                .stream()
                .flatMap(book -> book.getContacts().stream())
                .filter(contact -> contact.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }
    
    public List<Contact> searchByState(String state) {

        return addressBooks.values()
                .stream()
                .flatMap(book -> book.getContacts().stream())
                .filter(contact -> contact.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }
    
    public Map<String, List<Contact>> viewPersonsByCity() {

        return addressBooks.values()
                .stream()
                .flatMap(book -> book.getContacts().stream())
                .collect(Collectors.groupingBy(Contact::getCity));
    }
    
    public Map<String, List<Contact>> viewPersonsByState() {

        return addressBooks.values()
                .stream()
                .flatMap(book -> book.getContacts().stream())
                .collect(Collectors.groupingBy(Contact::getState));
    }
    
    public Map<String, Long> countContactsByCity() {

        return addressBooks.values()
                .stream()
                .flatMap(book -> book.getContacts().stream())
                .collect(Collectors.groupingBy(
                        Contact::getCity,
                        Collectors.counting()
                ));
    }
    
    public Map<String, Long> countContactsByState() {

        return addressBooks.values()
                .stream()
                .flatMap(book -> book.getContacts().stream())
                .collect(Collectors.groupingBy(
                        Contact::getState,
                        Collectors.counting()
                ));
    }
    
    public List<Contact> sortContactsByName(String bookName) {

        AddressBook book = addressBooks.get(bookName);

        if(book == null) {
            return new ArrayList<>();
        }

        return book.getContacts()
                .stream()
                .sorted(Comparator.comparing(Contact::getFirstName))
                .collect(Collectors.toList());
    }
    
    public List<Contact> sortContactsByCity(String bookName) {

        AddressBook book = addressBooks.get(bookName);

        if(book == null) {
            return new ArrayList<>();
        }

        return book.getContacts()
                .stream()
                .sorted(Comparator.comparing(Contact::getCity))
                .collect(Collectors.toList());
    }
    
    public List<Contact> sortContactsByState(String bookName) {

        AddressBook book = addressBooks.get(bookName);

        if(book == null) {
            return new ArrayList<>();
        }

        return book.getContacts()
                .stream()
                .sorted(Comparator.comparing(Contact::getState))
                .collect(Collectors.toList());
    }
    
    public List<Contact> sortContactsByZip(String bookName) {

        AddressBook book = addressBooks.get(bookName);

        if(book == null) {
            return new ArrayList<>();
        }

        return book.getContacts()
                .stream()
                .sorted(Comparator.comparing(Contact::getZip))
                .collect(Collectors.toList());
    }
}