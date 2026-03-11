package com.aryan.addressbook.repository;

import com.aryan.addressbook.model.Contact;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContactRepository {

    private final DataSource dataSource;

    public ContactRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Contact> getAllContacts() {

        List<Contact> contacts = new ArrayList<>();

        String sql = "SELECT first_name, last_name, city, state, zip, phone, email FROM contacts";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Contact contact = new Contact(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        "",
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zip"),
                        resultSet.getString("phone"),
                        resultSet.getString("email")
                );

                contacts.add(contact);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch contacts from database", e);
        }

        return contacts;
    }
    public int save(Contact contact) {

        String query = "INSERT INTO contacts " +
                "(first_name, last_name, city, state, zip, phone, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {

            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getCity());
            statement.setString(4, contact.getState());
            statement.setString(5, contact.getZip());
            statement.setString(6, contact.getPhoneNumber());
            statement.setString(7, contact.getEmail());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save contact", e);
        }
    }
    public int updateContactCity(String firstName, String lastName, String city) {

        String query =
                "UPDATE contacts SET city=? WHERE first_name=? AND last_name=?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, city);
            statement.setString(2, firstName);
            statement.setString(3, lastName);

            return statement.executeUpdate();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}