package com.example;

import com.example.dao.BooksDao;
import com.example.jdbitransaction.ConcurrentTransactionsBug;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import io.micronaut.runtime.Micronaut;
import picocli.CommandLine;

@CommandLine.Command(name = "dbbug", description = "Exhibits micronaut DB bugs",
        mixinStandardHelpOptions = true)
public class Application implements Runnable {
    static final Logger  LOG = LoggerFactory.getLogger(Application.class);

    @CommandLine.Option(names = {"-1", "--connection-lost"}, description = "Demonstrates lost connection on explicit transaction")
    private boolean transactions;
    @Inject
    ConcurrentTransactionsBug concurrentTransactionsBug;

    @Inject
    DatabaseSetup databaseSetup;

    public void setTransactions(boolean transactions) {
        this.transactions = transactions;
    }

    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        PicocliRunner.run(Application.class, args);
    }

    @Inject
    Polis p;

    @Override
    public void run() {
        try {
            if (transactions) {
//                databaseSetup.fillInitialRecords();
                concurrentTransactionsBug.connectionStatusLostDuringExecution();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}