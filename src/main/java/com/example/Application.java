package com.example;

import com.example.jdbitransaction.ConcurrentTransactionsBug;
import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import picocli.CommandLine;

@CommandLine.Command(name = "dbbug", description = "Exhibits micronaut DB bugs",
        mixinStandardHelpOptions = true)
public class Application implements Runnable {
    static final Logger  LOG = LoggerFactory.getLogger(Application.class);

    @CommandLine.Option(names = {"-1", "--connection-lost"}, description = "Demonstrates stealing of transaction between concurrent threads")
    private boolean connections;

    @CommandLine.Option(names = {"-2", "--steal-transactions"}, description = "Demonstrates lost connection on explicit transaction")
    private boolean transactions;

    @CommandLine.Option(names = {"-3", "--nested-transaction"}, description = "Demonstrates lost connection on explicit transaction")
    private boolean nested;

    @CommandLine.Option(names = {"-4", "--default-transactions"}, description = "Demonstrates lack of transaction in @Transaction default methods")
    private boolean defaultTransactions;

    @Inject
    ConcurrentTransactionsBug concurrentTransactionsBug;

    @Inject
    DatabaseSetup databaseSetup;

    public void setConnections(boolean connections) {
        this.connections = connections;
    }

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
            if (connections) {
                concurrentTransactionsBug.connectionStatusLostDuringExecution();
            }
            if (transactions) {
                databaseSetup.fillInitialRecords();
                concurrentTransactionsBug.transactionStealedFromOtherThread();
            }
            if (nested) {
                databaseSetup.fillInitialRecords();
                concurrentTransactionsBug.nestedTransaction();
            }
            if (defaultTransactions) {
                concurrentTransactionsBug.noTransactionOrConnectionInDefaultMethod();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}