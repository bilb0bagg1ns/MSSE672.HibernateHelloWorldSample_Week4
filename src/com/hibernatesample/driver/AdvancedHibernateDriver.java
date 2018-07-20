package com.hibernatesample.driver;

import com.hibernatesample.model.domain.Greetings;
import com.hibernatesample.model.services.factory.HibernateSessionFactory;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * A slightly advanced driver that shows the process of interacting with
 * Hibernate and executing the task of persisting a greeting, listing all the
 * greetings and modifying a greeting.
 *
 * Note: Actions(save, query, update, and query) performed in this Driver are all 
 * performed as one unit of work, meaning, we open a session, perform these actions
 * and then close the session.
 * 
 * From Hibernate Documentation:
 *
 * A SessionFactory is an expensive-to-create, thread safe object, intended to be
 * shared by all application threads. It is created once, usually on application
 * startup, from a Configuration instance.
 *
 * A Session is an inexpensive, non-thread safe object that should be used once
 * and a Session will not obtain a JDBC Connection, or a Datasource, unless it
 * is then discarded for: a single request, a conversation or a single unit of
 * work. needed. It will not consume any resources until used.
 *
 * In order to reduce lock contention in the database, a database transaction
 * has to be as short as possible. Long database transactions will prevent your
 * application from scaling to a highly concurrent load. It is not recommended
 * that you hold a database transaction open during user think time until the
 * unit of work is complete.
 *
 */
public class AdvancedHibernateDriver {

    /*
     * Category set in config/log4j.properties as
     * log4j.category.com.classexercise=DEBUG, A1
     */
    static Logger log = Logger.getLogger("com.hibernatesample");

    /**
     * @param args
     */
    public static void main(String[] args) {
        Transaction tx = null;
        try {
            // initialize log4j
            Log4JInit.initializeLog4J();

            log.info("About to create a Hibernate Session");
            Session session = HibernateSessionFactory.currentSession();
            tx = session.beginTransaction(); // begin transaction

            Greetings greetings = new Greetings("Hello World!!");
            log.info("---------------------------");
            log.info("About to save the greeting!");
            session.save(greetings);
            // Note: 
            // 1. This closes the Transaction
            // 2. Also, even if you don't do an explicit commit, 
            //    data is still stored in the database					
            tx.commit();
            log.info("Greeting saved. Check database for data!");

            /* Lets retrieve all the greetings */
            log.info("---------------------------");
            log.info("About to retrieve all greetings!");
            /**
             * Note: The reference to Greetings in the query is the object
             * Greetings not the table greetings. We deal in objects here not
             * tables!
             */
            Query q = session.createQuery("from Greetings as g order by g.greetingText asc");
            List greetingsList = q.list();
            for (Object o : greetingsList) {
                log.info((Greetings) o);
            }

            log.info("---------------------------");
            log.info("About to update Greetings with generated Id 1!");
            /* Transaction was closed earlier, reopen it.*/
            tx = session.beginTransaction();

            // Get the message with whose greetingId is 1.
            Greetings greeting = (Greetings) session.load(Greetings.class, new Integer(1));
            greeting.setGreetingText("Hello Homer!");
            // commit the transactions
            tx.commit();

            /* Lets retrieve all the greetings after previous update */
            log.info("---------------------------");
            log.info("About to retrieve all greetings after previous update!");
            q = session.createQuery("from Greetings as g order by g.greetingText asc");
            greetingsList = q.list();
            for (Object o : greetingsList) {
                log.info((Greetings) o);
            }
            log.info("---------------------------");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error(e.getClass() + ": " + e.getMessage(), e);
        } finally {
            // close the Session and the SessionFactory
            HibernateSessionFactory.closeSessionAndFactory();
        }
    }//end main

}
