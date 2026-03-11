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
    
    public List<Contact> getContactsByDateRange(String startDate, String endDate) {

        List<Contact> contacts = new ArrayList<>();

        String query =
                "SELECT * FROM contacts WHERE date_added BETWEEN ? AND ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, startDate);
            statement.setString(2, endDate);

            ResultSet rs = statement.executeQuery();

            while(rs.next()) {

                Contact contact = new Contact(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        "",
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("zip"),
                        rs.getString("phone"),
                        rs.getString("email")
                );

                contacts.add(contact);
            }

        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return contacts;
    }
}