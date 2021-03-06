/**
 * Copyright 5AM Solutions Inc
 * Copyright SemanticBits LLC
 * Copyright AgileX Technologies, Inc
 * Copyright Ekagra Software Technologies Ltd
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cacis/LICENSE.txt for details.
 */
package gov.nih.nci.cacisweb.dao;

import gov.nih.nci.cacisweb.exception.DAOException;

import java.sql.Connection;

/**
 * @author Ajay Nalamala
 * 
 */
public interface ICommonUtilityDAO {

	public Connection manageCaCISConnection() throws DAOException;

	public void closeCaCISConnection() throws DAOException;

}
