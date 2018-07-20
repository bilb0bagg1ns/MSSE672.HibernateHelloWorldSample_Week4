package com.hibernatesample.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is our persistent class that maps to the database table 'greetings'.
 * This class will be used to create a greeting and save it in the database.
 *
 * Consists of two main class variables:
 *
 * 1. greetingId: an identifier attribute to uniquely identify this message
 * (tying it to the primary key in the 'greetings' table) for persistence and
 * retrieval from the database. Hibernate lets you set this either manually,
 * which means that the developer will create a Greetings class with a unique
 * greetingId OR as in our case we've indicated at the time of table creation
 * that it will be auto incremented.
 *
 * CREATE TABLE `regis`.`greetings` ( `GREETING_ID` int(10) unsigned NOT NULL
 * auto_increment, ....
 *
 * 2. greetingText: Message text that will be persisted.
 */

@Entity
@Table(name="Greetings")
public class Greetings implements java.io.Serializable {

    /**
     * Identifier attribute
     * 
     * We use the @GeneratedValue annotation because the column was set for auto_increment
     */
    @Id
    @Column(name="greeting_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer greetingId;

    /**
     * Message that is persisted
     */
    @Column(name="greeting_text")
    private String greetingText;

    // Constructors
    /**
     * default constructor
     */
    public Greetings() {
    }

    public Greetings(String greetingText) {
        this.greetingText = greetingText;
    }

    // Property accessors
    public Integer getGreetingId() {
        return this.greetingId;
    }

    public void setGreetingId(Integer greetingId) {
        this.greetingId = greetingId;
    }

    public String getGreetingText() {
        return this.greetingText;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
    }

    /**
     *
     * @return @author
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Greetings[");
        stringBuilder.append("greetingText = ").append(greetingText);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}//end Greetings
