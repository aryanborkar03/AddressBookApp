package com.aryan.addressbook;

import com.aryan.addressbook.model.AddressBook;
import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.service.AddressBookService;
import com.aryan.addressbook.util.FileUtil;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

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
        assertEquals(2, service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenDifferentAddressBooks_whenAddingContacts_shouldSeparateData() {
        AddressBookService service = new AddressBookService();
        Contact c1 = createContact();
        Contact c2 = createContact();
        service.addContact("personal", c1);
        service.addContact("office", c2);
        assertEquals(1, service.getAddressBook("personal").getContacts().size());
        assertEquals(1, service.getAddressBook("office").getContacts().size());
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
        Contact contact = new Contact("", "", "", "", "", "", "", "");
        service.addContact("personal", contact);
        assertEquals(1, service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenSameContactTwice_whenAdded_shouldAllowDuplicatesForNow() {
        AddressBookService service = new AddressBookService();
        Contact c1 = createContact();
        Contact c2 = createContact();
        service.addContact("personal", c1);
        service.addContact("personal", c2);
        assertEquals(2, service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenLongPhoneNumber_whenAdded_shouldStoreContact() {
        AddressBookService service = new AddressBookService();
        Contact contact = new Contact(
                "Rahul", "Sharma", "Sector 21", "Delhi", "Delhi",
                "110001", "999999999999999", "rahul.sharma@gmail.com"
        );
        service.addContact("personal", contact);
        assertEquals(1, service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenInvalidEmail_whenAdded_shouldStillStoreContact() {
        AddressBookService service = new AddressBookService();
        Contact contact = new Contact(
                "Rahul", "Sharma", "Sector 21", "Delhi", "Delhi",
                "110001", "9876543210", "invalid-email"
        );
        service.addContact("personal", contact);
        assertEquals(1, service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenMultipleAddressBooks_whenContactsAdded_shouldMaintainSeparateLists() {
        AddressBookService service = new AddressBookService();
        Contact c1 = createContact();
        Contact c2 = createContact();
        service.addContact("family", c1);
        service.addContact("friends", c2);
        assertEquals(1, service.getAddressBook("family").getContacts().size());
        assertEquals(1, service.getAddressBook("friends").getContacts().size());
    }

    @Test
    public void givenExistingContact_whenUpdated_shouldReturnUpdatedContact() {
        AddressBookService service = new AddressBookService();
        Contact original = new Contact(
                "Rahul", "Sharma", "Sector 21", "Delhi", "Delhi",
                "110001", "9876543210", "rahul.sharma@gmail.com"
        );
        service.addContact("personal", original);
        Contact updated = new Contact(
                "Rahul", "Sharma", "Future City", "Nova", "Delhi",
                "110001", "9999999999", "Rahul@update.com"
        );
        Contact result = service.updateContact("personal", "Rahul", "Sharma", updated);
        assertEquals("Nova", result.getCity());
        assertEquals("9999999999", result.getPhoneNumber());
    }

    @Test
    public void givenNonExistingContact_whenUpdate_shouldReturnNull() {
        AddressBookService service = new AddressBookService();
        Contact updated = new Contact();
        Contact result = service.updateContact("personal", "Unknown", "Person", updated);
        assertNull(result);
    }

    @Test
    public void givenMissingAddressBook_whenUpdate_shouldReturnNull() {
        AddressBookService service = new AddressBookService();
        Contact updated = new Contact();
        Contact result = service.updateContact("unknownBook", "Rahul", "Sharma", updated);
        assertNull(result);
    }

    @Test
    public void givenExistingContact_whenDeleted_shouldReturnTrue() {
        AddressBookService service = new AddressBookService();
        Contact contact = new Contact(
                "Rahul", "Sharma", "Sector 21", "Delhi", "Delhi",
                "110001", "9876543210", "rahul.sharma@gmail.com"
        );
        service.addContact("personal", contact);
        boolean result = service.deleteContact("personal", "Rahul", "Sharma");
        assertTrue(result);
    }

    @Test
    public void givenMissingContact_whenDelete_shouldReturnFalse() {
        AddressBookService service = new AddressBookService();
        boolean result = service.deleteContact("personal", "Unknown", "Person");
        assertFalse(result);
    }

    @Test
    public void givenMissingAddressBook_whenDelete_shouldReturnFalse() {
        AddressBookService service = new AddressBookService();
        boolean result = service.deleteContact("unknownBook", "Rahul", "Sharma");
        assertFalse(result);
    }

    @Test
    public void givenMultipleContacts_whenAdded_shouldStoreAllContacts2() {
        AddressBookService service = new AddressBookService();
        Contact c1 = new Contact("Rahul", "Sharma", "Sector 21", "Delhi", "Delhi",
                "110001", "9876543210", "rahul.sharma@gmail.com");
        Contact c2 = new Contact("Rahul", "Verma", "Central City", "Delhi", "DL",
                "110001", "8888888888", "rahul@gmail.com");
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
        Contact c1 = new Contact("Rahul", "Sharma", "", "", "", "", "", "");
        Contact c2 = new Contact("Rahul", "Verma", "", "", "", "", "", "");
        service.addContact("personal", c1);
        service.addContact("office", c2);
        assertEquals(1, service.getContacts("personal").size());
        assertEquals(1, service.getContacts("office").size());
    }

    @Test
    public void givenDuplicateContacts_whenAdded_shouldAllowDuplicates() {
        AddressBookService service = new AddressBookService();
        Contact c = new Contact("Rahul", "Sharma", "", "", "", "", "", "");
        service.addContact("personal", c);
        service.addContact("personal", c);
        assertEquals(2, service.getContacts("personal").size());
    }

    @Test
    public void givenLargeNumberOfContacts_whenAdded_shouldHandleCorrectly() {
        AddressBookService service = new AddressBookService();
        for (int i = 0; i < 100; i++) {
            Contact c = new Contact("User" + i, "Test", "", "", "", "", "", "");
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
        assertSame(b1, b2);
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
        Contact c1 = new Contact("Rahul", "Sharma", "", "", "", "", "", "");
        Contact c2 = new Contact("Rahul", "Verma", "", "", "", "", "", "");
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

    @Test
    public void givenDuplicateContact_whenAdded_shouldThrowException() {
        AddressBookService service = new AddressBookService();
        Contact c1 = new Contact("Rahul", "Sharma", "Sector 21", "Delhi",
                "Delhi", "110001", "9876543210", "rahul.sharma@gmail.com");
        Contact c2 = new Contact("Rahul", "Sharma", "Other", "Other",
                "Other", "111111", "9999999999", "dup@gmail.com");
        service.addContact("personal", c1);
        assertThrows(RuntimeException.class, () -> {
            service.addContact("personal", c2);
        });
    }

    @Test
    public void givenSameContactInDifferentBooks_shouldBeAllowed() {
        AddressBookService service = new AddressBookService();
        Contact c = new Contact("Rahul", "Sharma", "", "", "", "", "", "");
        service.addContact("personal", c);
        service.addContact("office", c);
        assertEquals(1, service.getContacts("personal").size());
        assertEquals(1, service.getContacts("office").size());
    }

    @Test
    public void givenDifferentContacts_whenAdded_shouldAllow() {
        AddressBookService service = new AddressBookService();
        Contact c1 = new Contact("Rahul", "Sharma", "", "", "", "", "", "");
        Contact c2 = new Contact("Rahul", "Verma", "", "", "", "", "", "");
        service.addContact("personal", c1);
        service.addContact("personal", c2);
        assertEquals(2, service.getContacts("personal").size());
    }

    @Test
    public void givenExistingDuplicate_whenChecked_shouldPreventDuplicate() {
        AddressBookService service = new AddressBookService();
        Contact c1 = new Contact("Rahul", "Sharma", "", "", "", "", "", "");
        service.addContact("personal", c1);
        assertThrows(RuntimeException.class, () -> {
            service.addContact("personal", new Contact("Rahul", "Sharma", "", "", "", "", "", ""));
        });
    }

    @Test
    public void givenContacts_whenSearchByCity_shouldReturnMatches() {
        AddressBookService service = new AddressBookService();
        Contact c1 = new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", "");
        Contact c2 = new Contact("Rahul", "Verma", "", "Delhi", "DL", "", "", "");
        service.addContact("personal", c1);
        service.addContact("office", c2);
        assertEquals(1, service.searchByCity("Delhi").size());
    }

    @Test
    public void givenContacts_whenSearchByState_shouldReturnMatches() {
        AddressBookService service = new AddressBookService();
        Contact c1 = new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", "");
        Contact c2 = new Contact("Rahul", "Verma", "", "Delhi", "DL", "", "", "");
        service.addContact("personal", c1);
        service.addContact("office", c2);
        assertEquals(1, service.searchByState("DL").size());
    }

    @Test
    public void givenUnknownCity_whenSearch_shouldReturnEmptyList() {
        AddressBookService service = new AddressBookService();
        assertEquals(0, service.searchByCity("Unknown").size());
    }

    @Test
    public void givenDifferentCaseCity_whenSearch_shouldStillMatch() {
        AddressBookService service = new AddressBookService();
        Contact c = new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", "");
        service.addContact("personal", c);
        assertEquals(1, service.searchByCity("delhi").size());
    }

    @Test
    public void givenMultipleContactsSameCity_whenSearch_shouldReturnAll() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        service.addContact("office", new Contact("Rahul", "Verma", "", "Delhi", "DL", "", "", ""));
        assertEquals(2, service.searchByCity("Delhi").size());
    }

    @Test
    public void givenContacts_whenGroupedByCity_shouldReturnCityMap() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        service.addContact("office", new Contact("Rahul", "Verma", "", "Mumbai", "MH", "", "", ""));
        Map<String, List<Contact>> result = service.viewPersonsByCity();
        assertTrue(result.containsKey("Delhi"));
        assertTrue(result.containsKey("Mumbai"));
    }

    @Test
    public void givenMultipleContactsSameCity_whenGrouped_shouldReturnList() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        service.addContact("office", new Contact("Amit", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        Map<String, List<Contact>> result = service.viewPersonsByCity();
        assertEquals(2, result.get("Delhi").size());
    }

    @Test
    public void givenContacts_whenGroupedByState_shouldReturnStateMap() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        Map<String, List<Contact>> result = service.viewPersonsByState();
        assertTrue(result.containsKey("Delhi"));
    }

    @Test
    public void givenNoContacts_whenGrouped_shouldReturnEmptyMap() {
        AddressBookService service = new AddressBookService();
        assertEquals(0, service.viewPersonsByCity().size());
    }

    @Test
    public void givenContactsAcrossBooks_whenGrouped_shouldCombineResults() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        service.addContact("office", new Contact("Rahul", "Verma", "", "Mumbai", "MH", "", "", ""));
        Map<String, List<Contact>> result = service.viewPersonsByCity();
        assertEquals(2, result.size());
    }

    @Test
    public void givenContacts_whenCountByCity_shouldReturnCorrectCounts() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        service.addContact("office", new Contact("Amit", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        Map<String, Long> result = service.countContactsByCity();
        assertEquals(2L, result.get("Delhi"));
        
    }

    @Test
    public void givenContacts_whenCountByState_shouldReturnCorrectCounts() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        service.addContact("office", new Contact("Rahul", "Verma", "", "Delhi", "DL", "", "", ""));
        Map<String, Long> result = service.countContactsByState();
        assertEquals(1L, result.get("Delhi"));
        assertEquals(1L, result.get("DL"));
    }

    @Test
    public void givenNoContacts_whenCount_shouldReturnEmptyMap() {
        AddressBookService service = new AddressBookService();
        assertEquals(0, service.countContactsByCity().size());
    }

    @Test
    public void givenContactsAcrossBooks_whenCount_shouldCombineResults() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "", "", ""));
        service.addContact("office", new Contact("Rahul", "Verma", "", "Mumbai", "MH", "", "", ""));
        Map<String, Long> result = service.countContactsByCity();
        assertEquals(1L, result.get("Delhi"));
        assertEquals(1L, result.get("Mumbai"));
    }

    @Test
    public void givenCityDifferentCase_whenCount_shouldTreatSeparately() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul","Verma","","delhi","DL","","",""));
        service.addContact("office", new Contact("Rahul", "Verma", "", "delhi", "DL", "", "", ""));
        Map<String, Long> result = service.countContactsByCity();
        assertEquals(1L, result.get("Delhi"));
        assertEquals(1L, result.get("delhi"));
    }

    @Test
    public void givenContacts_whenSortedByName_shouldReturnAlphabeticalOrder() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "", "", "", "", ""));
        service.addContact("personal", new Contact("Amit", "Sharma", "", "", "", "", "", ""));
        service.addContact("personal", new Contact("Rahul", "Verma", "", "", "", "", "", ""));
        List<Contact> sorted = service.sortContactsByName("personal");
        assertEquals("Amit", sorted.get(0).getFirstName());
    }

    @Test
    public void givenEmptyAddressBook_whenSorted_shouldReturnEmptyList() {
        AddressBookService service = new AddressBookService();
        assertEquals(0, service.sortContactsByName("personal").size());
    }

    @Test
    public void givenSingleContact_whenSorted_shouldReturnSameContact() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "", "", "", "", ""));
        List<Contact> result = service.sortContactsByName("personal");
        assertEquals(1, result.size());
    }

    @Test
    public void givenAlreadySortedContacts_whenSorted_shouldRemainSame() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Amit", "Sharma", "", "", "", "", "", ""));
        service.addContact("personal", new Contact("Rahul", "Verma", "", "", "", "", "", ""));
        List<Contact> sorted = service.sortContactsByName("personal");
        assertEquals("Amit", sorted.get(0).getFirstName());
    }

    @Test
    public void givenContacts_whenSortedByCity_shouldReturnSortedList() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "", "", "", ""));
        service.addContact("personal", new Contact("Rahul", "Verma", "", "Bangalore", "", "", "", ""));
        List<Contact> sorted = service.sortContactsByCity("personal");
        assertEquals("Bangalore", sorted.get(0).getCity());
    }

    @Test
    public void givenContacts_whenSortedByState_shouldReturnSortedList() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "", "DL", "", "", ""));
        service.addContact("personal", new Contact("Rahul", "Verma", "", "", "KA", "", "", ""));
        List<Contact> sorted = service.sortContactsByState("personal");
        assertEquals("DL", sorted.get(0).getState());
    }

    @Test
    public void givenContacts_whenSortedByZip_shouldReturnSortedList() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "", "", "110001", "", ""));
        service.addContact("personal", new Contact("Rahul", "Verma", "", "", "", "560001", "", ""));
        List<Contact> sorted = service.sortContactsByZip("personal");
        assertEquals("110001", sorted.get(0).getZip());
    }

    @Test
    public void givenEmptyBook_whenSorted_shouldReturnEmptyList() {
        AddressBookService service = new AddressBookService();
        assertEquals(0, service.sortContactsByCity("personal").size());
    }

    @Test
    public void givenSingleContact_whenSorted_shouldReturnSame() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "", "", "", ""));
        assertEquals(1, service.sortContactsByCity("personal").size());
    }

    @Test
    public void givenContacts_whenSavedToFile_shouldCreateFile() {
        AddressBookService service = new AddressBookService();
        service.addContact("personal", new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "110001", "", ""));
        String filePath = "src/test/resources/testdata/test_contacts.txt";
        service.saveContactsToFile("personal", filePath);
        File file = new File(filePath);
        assertTrue(file.exists());
        file.deleteOnExit(); // Cleanup
    }

    @Test
    public void givenFile_whenRead_shouldReturnContacts() {
        String filePath = "src/test/resources/testdata/test_contacts.txt";
        FileUtil.writeContactsToFile(filePath, List.of(new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "110001", "", "")));
        List<Contact> contacts = FileUtil.readContactsFromFile(filePath);
        assertEquals(1, contacts.size());
    }

    @Test
    public void givenEmptyFile_whenRead_shouldReturnEmptyList() {
        String filePath = "src/test/resources/testdata/empty.txt";
        new File(filePath).getParentFile().mkdirs(); // Ensure directory exists
        List<Contact> contacts = FileUtil.readContactsFromFile(filePath);
        assertEquals(0, contacts.size());
    }

    @Test
    public void givenMultipleContacts_whenSavedAndRead_shouldMatchCount() {
        String filePath = "src/test/resources/testdata/multi_contacts.txt";
        List<Contact> contacts = List.of(
                new Contact("Rahul", "Sharma", "", "Delhi", "Delhi", "110001", "", ""),
                new Contact("Rahul", "Verma", "", "Delhi", "DL", "110001", "", "")
        );
        FileUtil.writeContactsToFile(filePath, contacts);
        List<Contact> result = FileUtil.readContactsFromFile(filePath);
        assertEquals(2, result.size());
    }
}
