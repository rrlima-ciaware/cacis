/**
 * Copyright Ekagra Software Technologies Ltd.
 * Copyright SAIC, Inc
 * Copyright 5AM Solutions
 * Copyright SemanticBits Technologies
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cacis/LICENSE.txt for details.
 */
package gov.nih.nci.cacis.nav;

/**
 * Represents exceptions encountered while receiving notifications
 * 
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @since May 5, 2011
 * 
 */
public class NotificationSendException extends Exception {

    private static final long serialVersionUID = 4878153586628471573L;

    /**
     * Takes the cause
     * 
     * @param cause the cause of the exception
     */
    public NotificationSendException(Throwable cause) {
        super(cause);
    }

    /**
     * Takes message and cause
     * 
     * @param msg a message
     * @param cause the cause
     */
    public NotificationSendException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Default constructor
     */
    public NotificationSendException() { //NOPMD

    }

    /**
     * Takes a message
     * 
     * @param msg a message
     */
    public NotificationSendException(String msg) {
        super(msg);
    }
}