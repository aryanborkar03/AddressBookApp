package com.aryan.addressbook;

import com.aryan.addressbook.model.AddressBook;
import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.service.AddressBookService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressBookServiceTest {

    private Contact createContact() {

        return new Contact(
                "Rahul",
                "Sharma",
                "Sector 21",
                "Delhi",
                "Delhi",
                "110001",
                "9876543210",
                "rahul.sharma@gmail.com"
        );
    }

    @Test
    public void givenValidContact_whenAdded_shouldReturnSameContact() {

        AddressBookService service = new AddressBookService();

        Contact contact = createContact();

        Contact result = service.addContact("personal", contact);

        assertEquals("Rahul", result.getFirstName());
        assertEquals("Sharma", result.getLastName());
    }

    @Test
    public void givenContact_whenAdded_shouldCreateAddressBookAutomatically() {

        AddressBookService service = new AddressBookService();

        Contact contact = createContact();

        service.addContact("office", contact);

        assertNotNull(service.getAddressBook("office"));
    }

    @Test
    public void givenMultipleContacts_whenAdded_shouldStoreAllContacts() {

        AddressBookService service = new AddressBookService();

        Contact c1 = createContact();
        Contact c2 = createContact();

        service.addContact("personal", c1);
        service.addContact("personal", c2);

        assertEquals(2,
                service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenDifferentAddressBooks_whenAddingContacts_shouldSeparateData() {

        AddressBookService service = new AddressBookService();

        Contact c1 = createContact();
        Contact c2 = createContact();

        service.addContact("personal", c1);
        service.addContact("office", c2);

        assertEquals(1,
                service.getAddressBook("personal").getContacts().size());

        assertEquals(1,
                service.getAddressBook("office").getContacts().size());
    }

    @Test
    public void givenContactWithNullValues_whenAdded_shouldNotCrash() {

        AddressBookService service = new AddressBookService();

        Contact contact = new Contact();

        Contact result = service.addContact("personal", contact);

        assertNotNull(result);
    }

    @Test
    public void givenContactWithEmptyStrings_whenAdded_shouldStoreSuccessfully() {

        AddressBookService service = new AddressBookService();

        Contact contact = new Contact(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );

        service.addContact("personal", contact);

        assertEquals(1,
                service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenSameContactTwice_whenAdded_shouldAllowDuplicatesForNow() {

        AddressBookService service = new AddressBookService();

        Contact c1 = createContact();
        Contact c2 = createContact();

        service.addContact("personal", c1);
        service.addContact("personal", c2);

        assertEquals(2,
                service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenLongPhoneNumber_whenAdded_shouldStoreContact() {

        AddressBookService service = new AddressBookService();

        Contact contact = new Contact(
                "Rahul",
                "Sharma",
                "Sector 21",
                "Delhi",
                "Delhi",
                "110001",
                "999999999999999",
                "rahul.sharma@gmail.com"
        );

        service.addContact("personal", contact);

        assertEquals(1,
                service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenInvalidEmail_whenAdded_shouldStillStoreContact() {

        AddressBookService service = new AddressBookService();

        Contact contact = new Contact(
                "Rahul",
                "Sharma",
                "Sector 21",
                "Delhi",
                "Delhi",
                "110001",
                "9876543210",
                "invalid-email"
        );

        service.addContact("personal", contact);

        assertEquals(1,
                service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenMultipleAddressBooks_whenContactsAdded_shouldMaintainSeparateLists() {

        AddressBookService service = new AddressBookService();

        Contact c1 = createContact();
        Contact c2 = createContact();

        service.addContact("family", c1);
        service.addContact("friends", c2);

        assertEquals(1,
                service.getAddressBook("family").getContacts().size());

        assertEquals(1,
                service.getAddressBook("friends").getContacts().size());
    }

    @Test
    public void givenExistingContact_whenUpdated_shouldReturnUpdatedContact() {

        AddressBookService service = new AddressBookService();

        Contact original = new Contact(
                "Rahul",
                "Sharma",
                "Sector 21",
                "Delhi",
                "Delhi",
                "110001",
                "9876543210",
                "rahul.sharma@gmail.com"
        );

        service.addContact("personal", original);

        Contact updated = new Contact(
                "Rahul",
                "Sharma",
                "Sector 45",
                "Gurgaon",
                "Haryana",
                "122001",
                "9999999999",
                "rahul.update@gmail.com"
        );

        Contact result = service.updateContact(
                "personal",
                "Rahul",
                "Sharma",
                updated
        );

        assertEquals("Gurgaon", result.getCity());
        assertEquals("9999999999", result.getPhoneNumber());
    }

    @Test
    public void givenNonExistingContact_whenUpdate_shouldReturnNull() {

        AddressBookService service = new AddressBookService();

        Contact updated = new Contact();

        Contact result = service.updateContact(
                "personal",
                "Unknown",
                "Person",
                updated
        );

        assertNull(result);
    }

    @Test
    public void givenMissingAddressBook_whenUpdate_shouldReturnNull() {

        AddressBookService service = new AddressBookService();

        Contact updated = new Contact();

        Contact result = service.updateContact(
                "unknownBook",
                "Rahul",
                "Sharma",
                updated
        );

        assertNull(result);
    }

    @Test
    public void givenExistingContact_whenDeleted_shouldReturnTrue() {

        AddressBookService service = new AddressBookService();

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

        service.addContact("personal", contact);

        boolean result = service.deleteContact(
                "personal",
                "Rahul",
                "Sharma"
        );

        assertTrue(result);
    }

    @Test
    public void givenMissingContact_whenDelete_shouldReturnFalse() {

        AddressBookService service = new AddressBookService();

        boolean result = service.deleteContact(
                "personal",
                "Unknown",
                "Person"
        );

        assertFalse(result);
    }

    @Test
    public void givenMissingAddressBook_whenDelete_shouldReturnFalse() {

        AddressBookService service = new AddressBookService();

        boolean result = service.deleteContact(
                "unknownBook",
                "Rahul",
                "Sharma"
        );

        assertFalse(result);
    }

    @Test
    public void givenMultipleContacts_whenAdded_shouldStoreAllContacts2() {

        AddressBookService service = new AddressBookService();

        Contact c1 = new Contact(
                "Rahul","Sharma","Sector 21","Delhi","Delhi",
                "110001","9876543210","rahul@gmail.com");

        Contact c2 = new Contact(
                "Amit","Verma","MG Road","Mumbai","Maharashtra",
                "400001","8888888888","amit@gmail.com");

        service.addContact("personal", c1);
        service.addContact("personal", c2);

        assertEquals(2, service.getContacts("personal").size());
    }

    @Test
    public void givenEmptyAddressBook_whenGetContacts_shouldReturnEmptyList() {

        AddressBookService service = new AddressBookService();

        assertEquals(0, service.getContacts("personal").size());
    }

    @Test
    public void givenContactsInDifferentBooks_whenFetched_shouldRemainSeparate() {

        AddressBookService service = new AddressBookService();

        Contact c1 = new Contact("Rahul","Sharma","","","","","","");
        Contact c2 = new Contact("Amit","Verma","","","","","","");

        service.addContact("personal", c1);
        service.addContact("office", c2);

        assertEquals(1, service.getContacts("personal").size());
        assertEquals(1, service.getContacts("office").size());
    }

    @Test
    public void givenDuplicateContacts_whenAdded_shouldAllowDuplicates() {

        AddressBookService service = new AddressBookService();

        Contact c = new Contact("Rahul","Sharma","","","","","","");

        service.addContact("personal", c);
        service.addContact("personal", c);

        assertEquals(2, service.getContacts("personal").size());
    }

    @Test
    public void givenLargeNumberOfContacts_whenAdded_shouldHandleCorrectly() {

        AddressBookService service = new AddressBookService();

        for(int i=0;i<100;i++) {

            Contact c = new Contact(
                    "User"+i,"Test","","","","","","");

            service.addContact("personal", c);
        }

        assertEquals(100, service.getContacts("personal").size());
    }

    @Test
    public void givenNewBookName_whenCreated_shouldReturnAddressBook() {

        AddressBookService service = new AddressBookService();

        AddressBook book = service.createAddressBook("personal");

        assertEquals("personal", book.getName());
    }

    @Test
    public void givenDuplicateBookName_whenCreated_shouldReturnExistingBook() {

        AddressBookService service = new AddressBookService();

        AddressBook b1 = service.createAddressBook("personal");
        AddressBook b2 = service.createAddressBook("personal");

        assertEquals(b1, b2);
    }

    @Test
    public void givenMultipleBooks_whenCreated_shouldStoreAll() {

        AddressBookService service = new AddressBookService();

        service.createAddressBook("personal");
        service.createAddressBook("office");

        assertEquals(2, service.getAllAddressBooks().size());
    }

    @Test
    public void givenContactsInDifferentBooks_shouldRemainSeparate() {

        AddressBookService service = new AddressBookService();

        service.createAddressBook("personal");
        service.createAddressBook("office");

        Contact c1 = new Contact("Rahul","Sharma","","","","","","");
        Contact c2 = new Contact("Amit","Verma","","","","","","");

        service.addContact("personal", c1);
        service.addContact("office", c2);

        assertEquals(1, service.getContacts("personal").size());
        assertEquals(1, service.getContacts("office").size());
    }

    @Test
    public void givenBooksCreated_whenFetched_shouldReturnDictionary() {

        AddressBookService service = new AddressBookService();

        service.createAddressBook("personal");
        service.createAddressBook("office");

        assertTrue(service.getAllAddressBooks().containsKey("personal"));
        assertTrue(service.getAllAddressBooks().containsKey("office"));
    }
}