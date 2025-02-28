package com.example.jdbitransaction;

import com.example.dao.BookMapper;
import com.example.dao.BooksDao;
import com.example.model.Book;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

@Singleton
public class ConcurrentTransactionsBug {
    BooksDao booksDao;

    @Inject
    public ConcurrentTransactionsBug(BooksDao booksDao) {
        this.booksDao = booksDao;
    }

    Semaphore semaphore = new Semaphore(0);

    public void stealingThread() {
        // do some transactional query
        try {
            booksDao.listAll();
            // release the other thread working with a transaction
        } catch (Exception e) {
            LoggerFactory.getLogger(ConcurrentTransactionsBug.class).error("Error", e);
        } finally {
            semaphore.release(100);
        }
    }

    public void makeConcurrentTransactions() throws Exception {
//        CompletableFuture<?> f = CompletableFuture.runAsync(this::stealingThread);
//        BookMapper.locker.set(semaphore);

        List<Book> books = booksDao.listAll();
//        f.get();
    }

    /**
     * Will throw io.micronaut.data.connection.exceptions.NoConnectionException
     * The reason is that in {@link io.micronaut.data.connection.support.AbstractConnectionOperations}
     * {@link io.micronaut.data.connection.support.AbstractConnectionOperations#suspendOpenConnection}
     * FIRST modifies {@link io.micronaut.core.propagation.PropagatedContext} so it ("A") one lists NO
     * connection, then during newSupplier.get(), that restore of that reduced context "A" is pushed into
     * connection's synchronizations by the newSupplier.
     * AFTER the supplied returns, the Scope "B" that could restore suspended connection is added to
     * synchronizations; it will be ordered AFTER "A", and therefore rolled back first on connection
     * complete. So instead restoring PropagatedContext in the order "A", "B", the states will be
     * restored in the order "B", "A", leaving incorrect state after closing the connection returned
     * by suspendOpenConnection().
     *
     * In addition, if obtaining a new connection fails (i.e. pool exhausted etc), suspendOpenConnection
     * FAILS to restore the previous context, as Scope is not rolled back in finally, it is not added
     * to connection synchronizations.
     */
    public void connectionStatusLostDuringExecution() {
        booksDao.listAll();
    }
}
