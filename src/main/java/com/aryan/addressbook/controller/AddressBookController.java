package com.aryan.addressbook.controller;

import com.aryan.addressbook.model.AddressBook;
import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.service.AddressBookService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/addressbooks")
public class AddressBookController {

    private final AddressBookService service;

    public AddressBookController(AddressBookService service) {
        this.service = service;
    }

    @PostMapping("/{name}/contacts")
    public Contact addContact(
            @PathVariable String name,
            @RequestBody Contact contact) {

        return service.addContact(name, contact);
    }

    @PutMapping("/{bookName}/contacts")
    public Contact updateContact(
            @PathVariable String bookName,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestBody Contact contact) {

        return service.updateContact(bookName, firstName, lastName, contact);
    }

    @DeleteMapping("/{bookName}/contacts")
    public String deleteContact(
            @PathVariable String bookName,
            @RequestParam String firstName,
            @RequestParam String lastName) {

        boolean deleted = service.deleteContact(bookName, firstName, lastName);

        if (deleted) {
            return "Contact deleted successfully";
        }

        return "Contact not found";
    }
    
    @GetMapping("/{bookName}/contacts")
    public List<Contact> getContacts(@PathVariable String bookName) {

        return service.getContacts(bookName);
    }
    
    @PostMapping("/{name}")
    public AddressBook createAddressBook(@PathVariable String name) {

        return service.createAddressBook(name);
    }
    
    @GetMapping
    public Map<String, AddressBook> getAllAddressBooks() {

        return service.getAllAddressBooks();
    }
    
    @GetMapping("/search/city/{city}")
    public List<Contact> searchByCity(@PathVariable String city) {

        return service.searchByCity(city);
    }
    
    @GetMapping("/search/state/{state}")
    public List<Contact> searchByState(@PathVariable String state) {

        return service.searchByState(state);
    }
    
    @GetMapping("/view/city")
    public Map<String, List<Contact>> viewByCity() {

        return service.viewPersonsByCity();
    }
    
    @GetMapping("/view/state")
    public Map<String, List<Contact>> viewByState() {

        return service.viewPersonsByState();
    }
    
    @GetMapping("/count/city")
    public Map<String, Long> countByCity() {

        return service.countContactsByCity();
    }
    
    @GetMapping("/count/state")
    public Map<String, Long> countByState() {

        return service.countContactsByState();
    }
    
    @GetMapping("/{bookName}/sort/name")
    public List<Contact> sortByName(@PathVariable String bookName) {

        return service.sortContactsByName(bookName);
    }
    
    @GetMapping("/{bookName}/sort/city")
    public List<Contact> sortByCity(@PathVariable String bookName) {

        return service.sortContactsByCity(bookName);
    }
    
    @GetMapping("/{bookName}/sort/state")
    public List<Contact> sortByState(@PathVariable String bookName) {

        return service.sortContactsByState(bookName);
    }
    
    @GetMapping("/{bookName}/sort/zip")
    public List<Contact> sortByZip(@PathVariable String bookName) {

        return service.sortContactsByZip(bookName);
    }
    
    @PostMapping("/{bookName}/save")
    public String saveContacts(
            @PathVariable String bookName,
            @RequestParam String filePath) {

        service.saveContactsToFile(bookName, filePath);

        return "Contacts saved to file";
    }
    
    @GetMapping("/load")
    public List<Contact> loadContacts(@RequestParam String filePath) {

        return service.loadContactsFromFile(filePath);
    }
    
    @PostMapping("/{bookName}/save-csv")
    public String saveCSV(
            @PathVariable String bookName,
            @RequestParam String filePath) {

        service.saveContactsToCSV(bookName, filePath);

        return "Contacts saved to CSV";
    }
    
    @GetMapping("/load-csv")
    public List<Contact> loadCSV(@RequestParam String filePath) {

        return service.loadContactsFromCSV(filePath);
    }
    
    @PostMapping("/{bookName}/save-json")
    public String saveJSON(
            @PathVariable String bookName,
            @RequestParam String filePath) {

        service.saveContactsToJSON(bookName, filePath);

        return "Contacts saved to JSON";
    }
    
    @GetMapping("/load-json")
    public List<Contact> loadJSON(@RequestParam String filePath) {

        return service.loadContactsFromJSON(filePath);
    }
    
    @GetMapping("/db/contacts")
    public List<Contact> getContactsFromDB() {

        return service.getContactsFromDatabase();
    }
}