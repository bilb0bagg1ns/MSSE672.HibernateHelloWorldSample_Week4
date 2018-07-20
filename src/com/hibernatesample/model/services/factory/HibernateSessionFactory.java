package com.hibernatesample.model.services.factory;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * Configures and provides access to Hibernate sessions, tied to the current
 * thread of execution. Follows the Thread Local Session pattern, see
 * {@link http://hibernate.org/42.html}.
 */
public class HibernateSessionFactory {

    static Logger log = Logger.getLogger("com.hibernatesample");
    /**
     * Location of hibernate.cfg.xml file.
     *
     * Should be placed in the 'config' folder. In NetBeans, it is copied to the
     * build\classes folder and hence can be found without having to refer to
     * the config folder. }
     * </pre>
     */

    /**
     * Holds a single instance of Session
     */
    private static final ThreadLocal threadLocal = new ThreadLocal();

    /**
     * The single instance of hibernate SessionFactory
     */
    private static org.hibernate.SessionFactory sessionFactory;

    /**
     * Returns the ThreadLocal Session instance. Lazy initialize the
     * <code>SessionFactory</code> if needed.
     *
     * @return Session
     * @throws HibernateException
     */
    public static Session currentSession() throws HibernateException {
        Session session = (Session) threadLocal.get();

        if (session == null || !session.isOpen()) {
            if (sessionFactory == null) {
                try {
                    sessionFactory = new Configuration()
                            .configure() // configures settings from hibernate.cfg.xml
                            .buildSessionFactory();

                } catch (Exception e) {
                    log.error("%%%% Error Creating SessionFactory %%%%", e);
                }
            }
            session = (sessionFactory != null) ? sessionFactory.openSession() : null;
            threadLocal.set(session);
        }
        return session;
    }

    /**
     * Close the single hibernate session. Factory is still open and available 
     * to return a new session.
     * 
     * One would use this method if you have multiple interactions with the 
     * database via Hibernate.
     * 
     * For example: Say we have two methods that we want to call in sequence:
     * 
     *   1. Method 1: saveData
     *        open session
     *        save data
     *        close Session
     *   2. Method 2: retrieveData
     *        open session
     *        save data
     *        close Session
     *
     * Who closes the SessionFactory? Ans: Study the Driver classes!
     * 
     * @throws HibernateException
     */
    public static void closeSession() throws HibernateException {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);

        if (session != null) {
            session.close();
        }
    }

    
    /**
     * Close the factory. 
     * 
     * Turns around and calls closeSessionAndFactory, because as defensive coding
     * we want to ensure that the session is also closed.
     * 
     * @throws HibernateException 
     */
    public static void closeFactory() throws HibernateException {
        closeSessionAndFactory();
    }
    /**
     * Close the session and the factory. One would call this when all database
     * interactions via Hibernate are completed - not doing so will cause the
     * app to keep running!
     * 
     * @throws HibernateException 
     */
    public static void closeSessionAndFactory() throws HibernateException {
        Session session = (Session) threadLocal.get();
        threadLocal.set(null);

        if (session != null) {
            session.close();
        }

        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    /**
     * Default constructor.
     */
    private HibernateSessionFactory() {
    }
}
