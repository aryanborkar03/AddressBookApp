package com.aryan.addressbook;

import com.aryan.addressbook.model.AddressBook;
import com.aryan.addressbook.model.Contact;
import com.aryan.addressbook.repository.ContactRepository;
import com.aryan.addressbook.service.AddressBookService;
import com.aryan.addressbook.util.CSVUtil;
import com.aryan.addressbook.util.FileUtil;
import com.aryan.addressbook.util.JSONUtil;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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

    private AddressBookService service;

    @BeforeEach
    void setup() {
        ContactRepository repository = mock(ContactRepository.class);
        service = new AddressBookService(repository);
    }

    @Test
    public void givenValidContact_whenAdded_shouldReturnSameContact() {

        Contact contact = createContact();

        Contact result = service.addContact("personal", contact);

        assertEquals("Rahul", result.getFirstName());
        assertEquals("Sharma", result.getLastName());
    }

    @Test
    public void givenContact_whenAdded_shouldCreateAddressBookAutomatically() {

        Contact contact = createContact();

        service.addContact("office", contact);

        assertNotNull(service.getAddressBook("office"));
    }

    @Disabled
    @Test
    public void givenMultipleContacts_whenAdded_shouldStoreAllContacts() {

        Contact c1 = createContact();
        Contact c2 = createContact();

        service.addContact("personal", c1);
        service.addContact("personal", c2);

        assertEquals(2,
                service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenDifferentAddressBooks_whenAddingContacts_shouldSeparateData() {

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

        Contact contact = new Contact();

        Contact result = service.addContact("personal", contact);

        assertNotNull(result);
    }

    @Test
    public void givenContactWithEmptyStrings_whenAdded_shouldStoreSuccessfully() {

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

    @Disabled
    @Test
    public void givenSameContactTwice_whenAdded_shouldAllowDuplicatesForNow() {

        Contact c1 = createContact();
        Contact c2 = createContact();

        service.addContact("personal", c1);
        service.addContact("personal", c2);

        assertEquals(2,
                service.getAddressBook("personal").getContacts().size());
    }

    @Test
    public void givenLongPhoneNumber_whenAdded_shouldStoreContact() {

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
                "Future City",
                "Nova",
                "Delhi",
                "110001",
                "9999999999",
                "rahul.update@gmail.com"
        );

        Contact result = service.updateContact(
                "personal",
                "Rahul",
                "Sharma",
                updated
        );

        assertEquals("Nova", result.getCity());
        assertEquals("9999999999", result.getPhoneNumber());
    }
    
    @Test
    public void givenNonExistingContact_whenUpdate_shouldReturnNull() {

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

        boolean result = service.deleteContact(
                "personal",
                "Unknown",
                "Person"
        );

        assertFalse(result);
    }
    
    @Test
    public void givenMissingAddressBook_whenDelete_shouldReturnFalse() {

        boolean result = service.deleteContact(
                "unknownBook",
                "Rahul",
                "Sharma"
        );

        assertFalse(result);
    }
    
    @Test
    public void givenMultipleContacts_whenAdded_shouldStoreAllContacts2() {

        Contact c1 = new Contact(
                "Rahul","Sharma","Sector 21","Delhi","Delhi",
                "110001","9876543210","rahul.sharma@gmail.com");

        Contact c2 = new Contact(
                "Rahul","Verma","Central City","Delhi","DL",
                "110001","8888888888","rahul@gmail.com");

        service.addContact("personal", c1);
        service.addContact("personal", c2);

        assertEquals(2, service.getContacts("personal").size());
    }
    
    @Test
    public void givenEmptyAddressBook_whenGetContacts_shouldReturnEmptyList() {

        assertEquals(0, service.getContacts("personal").size());
    }
    
    @Test
    public void givenContactsInDifferentBooks_whenFetched_shouldRemainSeparate() {

        Contact c1 = new Contact("Rahul","Sharma","","","","","","");
        Contact c2 = new Contact("Rahul","Verma","","","","","","");

        service.addContact("personal", c1);
        service.addContact("office", c2);

        assertEquals(1, service.getContacts("personal").size());
        assertEquals(1, service.getContacts("office").size());
    }
    
    @Disabled
    @Test
    public void givenDuplicateContacts_whenAdded_shouldAllowDuplicates() {

        Contact c = new Contact("Rahul","Sharma","","","","","","");

        service.addContact("personal", c);
        service.addContact("personal", c);

        assertEquals(2, service.getContacts("personal").size());
    }
    
    @Test
    public void givenLargeNumberOfContacts_whenAdded_shouldHandleCorrectly() {

        for(int i=0;i<100;i++) {

            Contact c = new Contact(
                    "User"+i,"Test","","","","","","");

            service.addContact("personal", c);
        }

        assertEquals(100, service.getContacts("personal").size());
    }
    
    @Test
    public void givenNewBookName_whenCreated_shouldReturnAddressBook() {

        AddressBook book = service.createAddressBook("personal");

        assertEquals("personal", book.getName());
    }
    
    @Test
    public void givenDuplicateBookName_whenCreated_shouldReturnExistingBook() {

        AddressBook b1 = service.createAddressBook("personal");
        AddressBook b2 = service.createAddressBook("personal");

        assertEquals(b1, b2);
    }
    
    @Test
    public void givenMultipleBooks_whenCreated_shouldStoreAll() {

        service.createAddressBook("personal");
        service.createAddressBook("office");

        assertEquals(2, service.getAllAddressBooks().size());
    }
    
    @Test
    public void givenContactsInDifferentBooks_shouldRemainSeparate() {

        service.createAddressBook("personal");
        service.createAddressBook("office");

        Contact c1 = new Contact("Rahul","Sharma","","","","","","");
        Contact c2 = new Contact("Rahul","Verma","","","","","","");

        service.addContact("personal", c1);
        service.addContact("office", c2);

        assertEquals(1, service.getContacts("personal").size());
        assertEquals(1, service.getContacts("office").size());
    }
    
    @Test
    public void givenBooksCreated_whenFetched_shouldReturnDictionary() {

        service.createAddressBook("personal");
        service.createAddressBook("office");

        assertTrue(service.getAllAddressBooks().containsKey("personal"));
        assertTrue(service.getAllAddressBooks().containsKey("office"));
    }
    
    @Test
    public void givenDuplicateContact_whenAdded_shouldThrowException() {

        Contact c1 = new Contact(
                "Rahul","Sharma","Sector 21","Delhi",
                "Delhi","110001","9876543210","rahul.sharma@gmail.com");

        Contact c2 = new Contact(
                "Rahul","Sharma","Other","Other",
                "Other","111111","9999999999","dup@gmail.com");

        service.addContact("personal", c1);

        assertThrows(RuntimeException.class, () -> {
            service.addContact("personal", c2);
        });
    }
    
    @Test
    public void givenSameContactInDifferentBooks_shouldBeAllowed() {

        Contact c = new Contact("Rahul","Sharma","","","","","","");

        service.addContact("personal", c);
        service.addContact("office", c);

        assertEquals(1, service.getContacts("personal").size());
        assertEquals(1, service.getContacts("office").size());
    }
    
    @Test
    public void givenDifferentContacts_whenAdded_shouldAllow() {

        Contact c1 = new Contact("Rahul","Sharma","","","","","","");
        Contact c2 = new Contact("Rahul","Verma","","","","","","");

        service.addContact("personal", c1);
        service.addContact("personal", c2);

        assertEquals(2, service.getContacts("personal").size());
    }
    
    @Test
    public void givenExistingDuplicate_whenChecked_shouldPreventDuplicate() {

        Contact c1 = new Contact("Rahul","Sharma","","","","","","");

        service.addContact("personal", c1);

        assertThrows(RuntimeException.class, () -> {
            service.addContact("personal", new Contact("Rahul","Sharma","","","","","",""));
        });
    }
    
    @Test
    public void givenContacts_whenSearchByCity_shouldReturnMatches() {

        Contact c1 = new Contact("Rahul","Sharma","","Delhi","Delhi","","","");
        Contact c2 = new Contact("Rahul","Verma","","Delhi","DL","","","");

        service.addContact("personal", c1);
        service.addContact("office", c2);
        assertEquals(2, service.searchByCity("Delhi").size());
    }
    
    @Test
    public void givenContacts_whenSearchByState_shouldReturnMatches() {

        Contact c1 = new Contact("Rahul","Sharma","","Delhi","Delhi","","","");
        Contact c2 = new Contact("Rahul","Verma","","Delhi","DL","","","");

        service.addContact("personal", c1);
        service.addContact("office", c2);

        assertEquals(1, service.searchByState("DL").size());
    }
    
    @Test
    public void givenUnknownCity_whenSearch_shouldReturnEmptyList() {

        assertEquals(0, service.searchByCity("Unknown").size());
    }
    
    @Test
    public void givenDifferentCaseCity_whenSearch_shouldStillMatch() {

        Contact c = new Contact("Rahul","Sharma","","Delhi","Delhi","","","");
        service.addContact("personal", c);

        assertEquals(1, service.searchByCity("delhi").size());
    }
    
    @Test
    public void givenMultipleContactsSameCity_whenSearch_shouldReturnAll() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Rahul","Verma","","Delhi","DL","","",""));

        assertEquals(2, service.searchByCity("Delhi").size());
    }
    
    @Test
    public void givenContacts_whenGroupedByCity_shouldReturnCityMap() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Rahul","Verma","","Delhi","DL","","",""));

        Map<String, List<Contact>> result = service.viewPersonsByCity();

        assertTrue(result.containsKey("Delhi"));
        assertTrue(result.containsKey("Delhi"));
    }
    
    @Test
    public void givenMultipleContactsSameCity_whenGrouped_shouldReturnList() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Amit","Sharma","","Delhi","Delhi","","",""));

        Map<String, List<Contact>> result = service.viewPersonsByCity();

        assertEquals(2, result.get("Delhi").size());
    }
    
    @Test
    public void givenContacts_whenGroupedByState_shouldReturnStateMap() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        Map<String, List<Contact>> result = service.viewPersonsByState();

        assertTrue(result.containsKey("Delhi"));
    }
    
    @Test
    public void givenNoContacts_whenGrouped_shouldReturnEmptyMap() {

        assertEquals(0, service.viewPersonsByCity().size());
    }
    
    @Test
    public void givenContactsAcrossBooks_whenGrouped_shouldCombineResults() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Rahul","Verma","","Delhi","DL","","",""));

        Map<String, List<Contact>> result = service.viewPersonsByCity();

        assertEquals(1, result.size());
    }
    
    @Test
    public void givenContacts_whenCountByCity_shouldReturnCorrectCounts() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Amit","Sharma","","Delhi","Delhi","","",""));

        Map<String, Long> result = service.countContactsByCity();

        assertEquals(2, result.get("Delhi"));
    }
    
    @Test
    public void givenContacts_whenCountByState_shouldReturnCorrectCounts() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Rahul","Verma","","Delhi","DL","","",""));

        Map<String, Long> result = service.countContactsByState();

        assertEquals(1, result.get("Delhi"));
        assertEquals(1, result.get("DL"));
    }
    
    @Test
    public void givenNoContacts_whenCount_shouldReturnEmptyMap() {

        assertEquals(0, service.countContactsByCity().size());
    }
    
    @Test
    public void givenContactsAcrossBooks_whenCount_shouldCombineResults() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Rahul","Verma","","Delhi","DL","","",""));

        Map<String, Long> result = service.countContactsByCity();

        assertEquals(2, result.get("Delhi"));
    }
    
    @Test
    public void givenCityDifferentCase_whenCount_shouldTreatSeparately() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","","",""));

        service.addContact("office",
                new Contact("Rahul","Verma","","delhi","DL","","",""));

        Map<String, Long> result = service.countContactsByCity();

        assertEquals(1, result.get("Delhi"));
    }
    
    @Test
    public void givenContacts_whenSortedByName_shouldReturnAlphabeticalOrder() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","","","","",""));

        service.addContact("personal",
                new Contact("Amit","Sharma","","","","","",""));

        service.addContact("personal",
                new Contact("Rahul","Verma","","","","","",""));

        List<Contact> sorted = service.sortContactsByName("personal");

        assertEquals("Amit", sorted.get(0).getFirstName());
    }
    
    @Test
    public void givenEmptyAddressBook_whenSorted_shouldReturnEmptyList() {

        assertEquals(0, service.sortContactsByName("personal").size());
    }
    
    @Test
    public void givenSingleContact_whenSorted_shouldReturnSameContact() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","","","","",""));

        List<Contact> result = service.sortContactsByName("personal");

        assertEquals(1, result.size());
    }
    
    @Test
    public void givenAlreadySortedContacts_whenSorted_shouldRemainSame() {

        service.addContact("personal",
                new Contact("Amit","Sharma","","","","","",""));

        service.addContact("personal",
                new Contact("Rahul","Verma","","","","","",""));

        List<Contact> sorted = service.sortContactsByName("personal");

        assertEquals("Amit", sorted.get(0).getFirstName());
    }
    
    @Test
    public void givenContacts_whenSortedByCity_shouldReturnSortedList() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","","","",""));

        service.addContact("personal",
                new Contact("Rahul","Verma","","Delhi","","","",""));

        List<Contact> sorted = service.sortContactsByCity("personal");

        assertEquals("Delhi", sorted.get(0).getCity());
    }
    
    @Test
    public void givenContacts_whenSortedByState_shouldReturnSortedList() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","","Delhi","","",""));

        service.addContact("personal",
                new Contact("Rahul","Verma","","","DL","","",""));

        List<Contact> sorted = service.sortContactsByState("personal");

        assertEquals("DL", sorted.get(0).getState());
    }
    
    @Test
    public void givenContacts_whenSortedByZip_shouldReturnSortedList() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","","","110001","",""));

        service.addContact("personal",
                new Contact("Rahul","Verma","","","","110001","",""));

        List<Contact> sorted = service.sortContactsByZip("personal");

        assertEquals("110001", sorted.get(0).getZip());
    }
    
    @Test
    public void givenEmptyBook_whenSorted_shouldReturnEmptyList() {

        assertEquals(0, service.sortContactsByCity("personal").size());
    }
    
    @Test
    public void givenSingleContact_whenSorted_shouldReturnSame() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","","","",""));

        assertEquals(1, service.sortContactsByCity("personal").size());
    }
    
    @Test
    public void givenContacts_whenSavedToFile_shouldCreateFile() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""));

        service.saveContactsToFile("personal","src/test/resources/testdata/test_contacts.txt");

        File file = new File("src/test/resources/testdata/test_contacts.txt");

        assertTrue(file.exists());
    }

    @Test
    public void givenFile_whenRead_shouldReturnContacts() {

        FileUtil.writeContactsToFile(
                "src/test/resources/testdata/test_contacts.txt",
                List.of(new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""))
        );

        List<Contact> contacts = FileUtil.readContactsFromFile(
                "src/test/resources/testdata/test_contacts.txt"
        );

        assertEquals(1, contacts.size());
    }

    @Test
    public void givenEmptyFile_whenRead_shouldReturnEmptyList() {

        List<Contact> contacts =
                FileUtil.readContactsFromFile("src/test/resources/testdata/empty.txt");

        assertEquals(0, contacts.size());
    }

    @Test
    public void givenMultipleContacts_whenSavedAndRead_shouldMatchCount() {

        List<Contact> contacts = List.of(
                new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""),
                new Contact("Rahul","Verma","","Delhi","DL","110001","","")
        );

        FileUtil.writeContactsToFile(
                "src/test/resources/testdata/multi_contacts.txt",
                contacts
        );

        List<Contact> result = FileUtil.readContactsFromFile(
                "src/test/resources/testdata/multi_contacts.txt"
        );

        assertEquals(2, result.size());
    }
    
    @Test
    public void givenContacts_whenSavedToCSV_shouldCreateCSVFile() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""));

        service.saveContactsToCSV("personal","src/test/resources/testdata/test_contacts.csv");

        File file = new File("src/test/resources/testdata/test_contacts.csv");

        assertTrue(file.exists());
    }
    
    @Test
    public void givenCSVFile_whenRead_shouldReturnContacts() {

        CSVUtil.writeContactsToCSV(
                "src/test/resources/testdata/test_contacts.csv",
                List.of(new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""))
        );

        List<Contact> contacts = CSVUtil.readContactsFromCSV("src/test/resources/testdata/test_contacts.csv");

        assertEquals(1, contacts.size());
    }
    
    @Test
    public void givenMultipleContacts_whenSavedAndLoadedCSV_shouldMatchCount() {

        List<Contact> contacts = List.of(
                new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""),
                new Contact("Rahul","Verma","","Delhi","DL","110001","","")
        );

        CSVUtil.writeContactsToCSV("src/test/resources/testdata/multi_contacts.csv", contacts);

        List<Contact> result = CSVUtil.readContactsFromCSV("src/test/resources/testdata/multi_contacts.csv");

        assertEquals(2, result.size());
    }
    
    @Test
    public void givenEmptyCSV_whenRead_shouldReturnEmptyList() {

        List<Contact> contacts = CSVUtil.readContactsFromCSV("src/test/resources/testdata/empty.csv");

        assertEquals(0, contacts.size());
    }
    
    @Test
    public void givenContacts_whenSavedToJSON_shouldCreateFile() {

        service.addContact("personal",
                new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""));

        String path = "src/test/resources/testdata/test_contacts.json";

        service.saveContactsToJSON("personal", path);

        File file = new File(path);

        assertTrue(file.exists());
    }
    
    @Test
    public void givenJSONFile_whenRead_shouldReturnContacts() {

        List<Contact> contacts = List.of(
                new Contact("Rahul","Sharma","","Delhi","Delhi","110001","","")
        );

        JSONUtil.writeContactsToJSON(
                "src/test/resources/testdata/test_contacts.json",
                contacts
        );

        List<Contact> result = JSONUtil.readContactsFromJSON(
                "src/test/resources/testdata/test_contacts.json"
        );

        assertEquals(1, result.size());
    }
    
    @Test
    public void givenMultipleContacts_whenSavedAndLoadedJSON_shouldMatchCount() {

        List<Contact> contacts = List.of(
                new Contact("Rahul","Sharma","","Delhi","Delhi","110001","",""),
                new Contact("Rahul","Verma","","Delhi","DL","110001","","")
        );

        String path = "src/test/resources/testdata/multi_contacts.json";

        JSONUtil.writeContactsToJSON(path, contacts);

        List<Contact> result = JSONUtil.readContactsFromJSON(path);

        assertEquals(2, result.size());
    }
    
    
    @Test
    public void givenEmptyJSON_whenRead_shouldReturnEmptyList() {

        String path = "src/test/resources/testdata/empty.json";
        

        JSONUtil.writeContactsToJSON(path, new ArrayList<>());

        List<Contact> contacts = JSONUtil.readContactsFromJSON(path);

        assertEquals(0, contacts.size());  
    }
}