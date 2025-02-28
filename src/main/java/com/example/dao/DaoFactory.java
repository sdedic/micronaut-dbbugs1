package com.example.dao;

import io.micronaut.context.annotation.Factory;
import io.micronaut.data.connection.annotation.Connectable;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;

@Factory
public class DaoFactory {
    @Singleton
    @Connectable
    BooksDao    createBookDao(Jdbi jdbi) {
        return jdbi.onDemand(BooksDao.class);
    }
}
