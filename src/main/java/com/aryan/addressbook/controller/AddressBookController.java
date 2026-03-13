package com.aryan.addressbook.controller;

import com.aryan.addressbook.model.AddressBook;
import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.dto.ContactDTO;
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
    
    private List<ContactDTO> toDTOList(List<Contact> contacts) {

        return contacts.stream()
                .map(service::convertToDTO)
                .toList();
    }

    @PostMapping("/{name}/contacts")
    public ContactDTO addContact(
            @PathVariable String name,
            @RequestBody ContactDTO dto) {

        Contact contact = service.convertToModel(dto);

        Contact saved = service.addContact(name, contact);

        return service.convertToDTO(saved);
    }

    @PutMapping("/{bookName}/contacts")
    public ContactDTO updateContact(
            @PathVariable String bookName,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestBody ContactDTO dto) {

        Contact updatedContact = service.convertToModel(dto);

        Contact result = service.updateContact(bookName, firstName, lastName, updatedContact);

        if(result == null) {
            return null;
        }

        return service.convertToDTO(result);
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
    public List<ContactDTO> getContacts(@PathVariable String bookName) {

        return toDTOList(service.getContacts(bookName));
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
    public List<ContactDTO> searchByCity(@PathVariable String city) {

        return toDTOList(service.searchByCity(city));
    }
    
    @GetMapping("/search/state/{state}")
    public List<ContactDTO> searchByState(@PathVariable String state) {

        return toDTOList(service.searchByState(state));
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
    public List<ContactDTO> sortByName(@PathVariable String bookName) {

        return toDTOList(service.sortContactsByName(bookName));
    }
    
    @GetMapping("/{bookName}/sort/city")
    public List<ContactDTO> sortByCity(@PathVariable String bookName) {

        return toDTOList(service.sortContactsByCity(bookName));
    }
    
    @GetMapping("/{bookName}/sort/state")
    public List<ContactDTO> sortByState(@PathVariable String bookName) {

        return toDTOList(service.sortContactsByState(bookName));
    }
    
    @GetMapping("/{bookName}/sort/zip")
    public List<ContactDTO> sortByZip(@PathVariable String bookName) {

        return toDTOList(service.sortContactsByZip(bookName));
    }
    
    @PostMapping("/{bookName}/save")
    public String saveContacts(
            @PathVariable String bookName,
            @RequestParam String filePath) {

        service.saveContactsToFile(bookName, filePath);

        return "Contacts saved to file";
    }
    
    @GetMapping("/load")
    public List<ContactDTO> loadContacts(@RequestParam String filePath) {

        return toDTOList(service.loadContactsFromFile(filePath));
    }
    
    @PostMapping("/{bookName}/save-csv")
    public String saveCSV(
            @PathVariable String bookName,
            @RequestParam String filePath) {

        service.saveContactsToCSV(bookName, filePath);

        return "Contacts saved to CSV";
    }
    
    @GetMapping("/load-csv")
    public List<ContactDTO> loadCSV(@RequestParam String filePath) {

        return toDTOList(service.loadContactsFromCSV(filePath));
    }
    
    @PostMapping("/{bookName}/save-json")
    public String saveJSON(
            @PathVariable String bookName,
            @RequestParam String filePath) {

        service.saveContactsToJSON(bookName, filePath);

        return "Contacts saved to JSON";
    }
    
    @GetMapping("/load-json")
    public List<ContactDTO> loadJSON(@RequestParam String filePath) {

        return toDTOList(service.loadContactsFromJSON(filePath));
    }
    
    @GetMapping("/db/contacts")
    public List<ContactDTO> getContactsFromDB() {

        return toDTOList(service.getContactsFromDatabase());
    }
    
    @PutMapping("/db/update-city")
    public String updateContactCity(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String city) {

        int updated = service.updateContactCity(firstName, lastName, city);

        if(updated > 0) {
            return "Contact updated successfully";
        }

        return "Contact not found";
    }
    
    @GetMapping("/db/contacts-by-date")
    public List<ContactDTO> getContactsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return toDTOList(service.getContactsByDateRange(startDate, endDate));
    }
    
    @GetMapping("/db/count/city")
    public Map<String, Long> countContactsByCityDB() {

        return service.countContactsByCityFromDB();
    }
    
    @GetMapping("/db/count/state")
    public Map<String, Long> countContactsByStateDB() {

        return service.countContactsByStateFromDB();
    }
    
    @PostMapping("/db/add-contact")
    public String addContactToDB(@RequestBody ContactDTO dto) {

        Contact contact = service.convertToModel(dto);

        int inserted = service.addContactToDatabase(contact);

        if(inserted > 0) {
            return "Contact added successfully";
        }

        return "Failed to add contact";
    }
}