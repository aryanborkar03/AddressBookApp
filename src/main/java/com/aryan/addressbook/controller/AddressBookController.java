package com.aryan.addressbook.controller;

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
}