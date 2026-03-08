package com.aryan.addressbook;

import com.aryan.addressbook.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {

    @Test
    public void givenValidContactDetails_whenObjectCreated_shouldReturnCorrectValues() {

        Contact contact = new Contact(
                "Rahul",
                "Sharma",
                "Sector 21",
                "Delhi",
                "Delhi",
                "110001",
                "9876543210",
                "rahul.sharma@gmail.com"
        );

        assertEquals("Rahul", contact.getFirstName());
        assertEquals("Sharma", contact.getLastName());
        assertEquals("Delhi", contact.getCity());
        assertEquals("Delhi", contact.getState());
        assertEquals("9876543210", contact.getPhoneNumber());
    }


    @Test
    public void givenEmptyConstructor_whenSettersUsed_shouldReturnCorrectValues() {

        Contact contact = new Contact();

        contact.setFirstName("Amit");
        contact.setLastName("Verma");
        contact.setCity("Mumbai");

        assertEquals("Amit", contact.getFirstName());
        assertEquals("Verma", contact.getLastName());
        assertEquals("Mumbai", contact.getCity());
    }


    @Test
    public void givenTwoContactsWithSameName_whenCompared_shouldReturnEqual() {

        Contact c1 = new Contact(
                "Neha", "Patel",
                "Street 10", "Ahmedabad", "Gujarat",
                "380001", "9123456789", "neha1@mail.com"
        );

        Contact c2 = new Contact(
                "Neha", "Patel",
                "Street 20", "Surat", "Gujarat",
                "395001", "9988776655", "neha2@mail.com"
        );

        assertEquals(c1, c2);
    }


    @Test
    public void givenTwoContactsWithDifferentNames_whenCompared_shouldReturnNotEqual() {

        Contact c1 = new Contact("Arjun", "Mehta", "", "", "", "", "", "");
        Contact c2 = new Contact("Karan", "Kapoor", "", "", "", "", "", "");

        assertNotEquals(c1, c2);
    }


    @Test
    public void givenSameContactObjects_whenHashCodeCalled_shouldReturnSameHash() {

        Contact c1 = new Contact("Priya", "Singh", "", "", "", "", "", "");
        Contact c2 = new Contact("Priya", "Singh", "", "", "", "", "", "");

        assertEquals(c1.hashCode(), c2.hashCode());
    }


    @Test
    public void givenContactWithNullValues_shouldHandleGracefully() {

        Contact contact = new Contact();

        assertNull(contact.getFirstName());
        assertNull(contact.getLastName());
        assertNull(contact.getCity());
    }


    @Test
    public void givenContact_whenUpdatingPhoneNumber_shouldReturnUpdatedValue() {

        Contact contact = new Contact();

        contact.setPhoneNumber("9000000000");
        contact.setPhoneNumber("9111111111");

        assertEquals("9111111111", contact.getPhoneNumber());
    }


    @Test
    public void givenContact_whenUpdatingEmail_shouldReturnUpdatedEmail() {

        Contact contact = new Contact();

        contact.setEmail("old.email@example.com");
        contact.setEmail("new.email@example.com");

        assertEquals("new.email@example.com", contact.getEmail());
    }

}