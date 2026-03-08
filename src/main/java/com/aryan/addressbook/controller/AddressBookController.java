package com.aryan.addressbook.controller;

import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.service.AddressBookService;
import org.springframework.web.bind.annotation.*;

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
}