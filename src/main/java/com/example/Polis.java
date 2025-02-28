package com.example;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;

@Singleton
public class Polis {

    Jdbi jdbi;

    @Inject
    public Polis(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public String s() {
        return "ahoj";
    }
}
