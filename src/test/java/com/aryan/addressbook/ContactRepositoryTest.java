package com.aryan.addressbook;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aryan.addressbook.repository.ContactRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ContactRepositoryTest {

    @Autowired
    ContactRepository repository;

    @Test
    public void givenDatabase_whenContactsFetched_shouldReturnRecords() {

    	assertNotNull(repository.getAllContacts());
    }
}