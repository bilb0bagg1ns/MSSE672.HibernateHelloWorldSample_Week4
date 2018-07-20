package com.hibernatesample.driver;

import com.hibernatesample.model.domain.Greetings;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * A very simplistic example that shows the process of interacting with Hibernate 
 * and executing the task of persisting a greeting.
 *
 *  
 * A more robust app would effectively place the code in a try/catch/finally blocks with the
 * catch block doing any rollbacks and the finally blocks releasing resources as
 * shown below.
 *
 * NOTE: THIS EXAMPLE DOES NOT USE THE HibernateSessionFactory.java
 * 
 * <pre>
 * {@code
 *      Session session = factory.openSession();
 *      Transaction tx;
 *      try {
 *           tx = session.beginTransaction(); //do some work
 *           ...
 *           tx.commit();
 *      } catch (Exception e) {
 *          if (tx!=null) tx.rollback(); throw e;
 *      } finally {
 *          session.close();
 *          sessionFactory.close();
 *      }
 *}
 * </pre>
 */

public class SimpleHibernateDriver {
    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        Session session = null;
        try {
            sessionFactory = new Configuration()
                    .configure() // configures settings from hibernate.cfg.xml
                    .buildSessionFactory();
            session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Greetings greetings = new Greetings("Hello World!!");
            /*
             To use the non annotated version of the domain class:
              1. Comment out line above.
              2. Uncomment line below.
              3. In hibernate.cfg.xml file, uncomment line below             
                 <!-- <mapping resource="GreetingsNonAnnotated.hbm.xml"/> -->
            */
            //GreetingsNonAnnotated greetings = new GreetingsNonAnnotated("Hello World!!");
            session.save(greetings);
            System.out.println ("-------------------------------------------");
            System.out.println ("Greetings saved. Manually check greetings table to confirm");
            System.out.println ("-------------------------------------------");
            tx.commit();
        } finally {
            if (session != null) {session.close();}
            if (sessionFactory != null) {sessionFactory.close();}
        }
    }
}
