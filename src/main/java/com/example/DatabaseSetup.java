package com.example;

import com.example.dao.BooksDao;
import com.example.model.Book;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.sql.SQLException;

@Singleton
public class DatabaseSetup {
    BooksDao booksDao;

    @Inject
    public DatabaseSetup(BooksDao booksDao) {
        this.booksDao = booksDao;
    }

    public void fillInitialRecords() throws SQLException {
        Book b1 = new Book(1, "A");
        Book b2 = new Book(2, "B");
        Book b3 = new Book(3, "C");

        booksDao.create(b1);
        booksDao.create(b2);
        booksDao.create(b3);
    }
}
