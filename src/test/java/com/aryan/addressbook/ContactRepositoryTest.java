package com.aryan.addressbook;
import com.aryan.addressbook.model.Contact;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aryan.addressbook.repository.ContactRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactRepositoryTest {

    @Autowired
    private ContactRepository repository;

    @Test
    public void givenDatabase_whenContactsFetched_shouldReturnRecords() {
         assertNotNull(repository.getAllContacts());
        
    }

    @Test
    public void givenContact_whenCityUpdated_shouldReturnUpdatedRows() {

        repository.save(new Contact(
                "Rahul",
                "Sharma",
                "Sector 10",
                "Delhi",
                "Delhi",
                "110001",
                "9876543210",
                "rahul@gmail.com"
        ));

        int rows = repository.updateContactCity(
                "Rahul",
                "Sharma",
                "Sector 21"
        );

        assertTrue(rows > 0);
    }
}