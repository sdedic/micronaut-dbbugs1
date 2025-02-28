package com.example.dao;

import com.example.model.Book;
import org.jdbi.v3.core.transaction.TransactionIsolationLevel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface BooksDao {

    @SqlQuery("SELECT * FROM Books")
    @UseRowMapper(BookMapper.class)
    @Transaction(TransactionIsolationLevel.READ_COMMITTED)
    public List<Book> listAll();


    @SqlUpdate("""
        INSERT INTO Books (ID, NAME) VALUES (:id, :name)
""")
    public void create(@BindBean  Book book);
}
