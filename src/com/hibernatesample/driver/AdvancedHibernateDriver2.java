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
 * A variation of the other advanced driver which demonstrates orchestration calls
 * to two different database interactions (save and retrieve).
 *
 * From Hibernate Documentation:
 *
 * A SessionFactory is an expensive-to-create, threadsafe object, intended to be
 * shared by all application threads. It is created once, usually on application
 * startup, from a Configuration instance.
 *
 * A Session is an inexpensive, non-threadsafe object that should be used once
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
public class AdvancedHibernateDriver2 {

    /*
     * Category set in config/log4j.properties as
     * log4j.category.com.classexercise=DEBUG, A1
     */
    static Logger log = Logger.getLogger("com.hibernatesample");

    public void saveData() {
        Transaction tx = null;
        try {
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
            log.info("---------------------------");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            log.error(e.getClass() + ": " + e.getMessage(), e);
        } finally {
            // close session only
            HibernateSessionFactory.closeSession();
            log.info("Sucessfully closed session after saving data");
        }
    }

    public void retrieveData() {
        Transaction tx = null;
        try {
            log.info("About to create a Hibernate Session");
            Session session = HibernateSessionFactory.currentSession();
            /* Transaction was closed earlier, reopen it.*/            
            tx = session.beginTransaction(); // begin transaction

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
            // close session only
            HibernateSessionFactory.closeSession();
            log.info("Sucessfully closed session after retrieving data");            
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        /**
         * Envision these orchestration calls below located in a service 
         * (example: GreetingPersistenceService.java)
         * 
         */
        // initialize log4j
        Log4JInit.initializeLog4J();
        AdvancedHibernateDriver2 advancedHibernateDriver2 = new AdvancedHibernateDriver2();
        try {
            advancedHibernateDriver2.saveData();
            advancedHibernateDriver2.retrieveData();
       } finally {
            // close factory
          HibernateSessionFactory.closeFactory();
        }

    }//end main

}
