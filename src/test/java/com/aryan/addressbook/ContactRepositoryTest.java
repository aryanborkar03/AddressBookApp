package com.aryan.addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.repository.ContactRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class ContactRepositoryTest {

    @Autowired
    ContactRepository repository;

    @Test
    public void givenDatabase_whenContactsFetched_shouldReturnRecords() {

    	assertNotNull(repository.getAllContacts());
    }
    
    @Test
    public void givenContact_whenCityUpdated_shouldReturnUpdatedRows() {

        int rows = repository.updateContactCity(
                "Rahul",
                "Sharma",
                "Delhi"
        );

        assertTrue(rows > 0);
    }
    
    @Test
    public void givenDateRange_whenContactsFetched_shouldReturnRecords() {

        List<Contact> contacts =
                repository.getContactsByDateRange(
                        "2026-03-01",
                        "2026-03-10"
                );

        assertTrue(contacts.size() > 0);
    }
    
    @Test
    public void givenDatabase_whenCountByCityRequested_shouldReturnCounts() {

        Map<String, Long> result = repository.countContactsByCity();

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
    
    @Test
    public void givenDatabase_whenCountByStateRequested_shouldReturnCounts() {

        Map<String, Long> result = repository.countContactsByState();

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
}