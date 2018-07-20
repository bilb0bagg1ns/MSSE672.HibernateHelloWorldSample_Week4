package com.hibernatesample.driver;

import java.io.FileInputStream;
import org.apache.log4j.PropertyConfigurator;

/**
 * Log4JInit.java
 *
 * Initializes Log4j
 *
 * @author Mike.Prasad
 */
public class Log4JInit {

    /**
     * Creates a new instance of Log4JInit
     */
    public Log4JInit() {
    }

    public static void initializeLog4J() {
        FileInputStream sf = null;

        try {

            String log4jConfigFileLocation = System.getProperty("log4j_prop_location");

            if (log4jConfigFileLocation != null) {
                // Initialize Properties for components
                PropertyConfigurator.configure(log4jConfigFileLocation);
                System.out.println("Successfully configured log4j");
            } else {
                System.out.println("log4j configuration file location not set. Passed in value is: " + log4jConfigFileLocation + ".");
                throw new RuntimeException("log4j configuration file location not set.");
            }
        } catch (RuntimeException rte) {
            System.out.println("Failed to configure log4j");
        }

    } //end initializeLog4J

} //end Log4JInit
