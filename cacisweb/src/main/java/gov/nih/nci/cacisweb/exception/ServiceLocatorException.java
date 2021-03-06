/**
 * Copyright 5AM Solutions Inc
 * Copyright SemanticBits LLC
 * Copyright AgileX Technologies, Inc
 * Copyright Ekagra Software Technologies Ltd
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cacis/LICENSE.txt for details.
 */
package gov.nih.nci.cacisweb.exception;

/**
 * @author Ajay Nalamala
 */
public class ServiceLocatorException extends CaCISWebException {

	private static final long serialVersionUID = 678094176725986311L;

    /**
	 * Constructor for ServiceLocatorException.
	 */
	public ServiceLocatorException() {
		super();
	}

	/**
	 * Constructor for ServiceLocatorException.
	 * 
	 * @param s
	 */
	public ServiceLocatorException(String s) {
		super(s);
	}

}
