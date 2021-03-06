/**
 * Copyright 5AM Solutions Inc
 * Copyright SemanticBits LLC
 * Copyright AgileX Technologies, Inc
 * Copyright Ekagra Software Technologies Ltd
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cacis/LICENSE.txt for details.
 */
package gov.nih.nci.cacisweb.action;

import gov.nih.nci.cacisweb.dao.DAOFactory;
import gov.nih.nci.cacisweb.dao.ICDWUserPermissionDAO;
import gov.nih.nci.cacisweb.model.CdwPermissionModel;
import gov.nih.nci.cacisweb.model.CdwUserModel;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class CdwUserSearchAction extends ActionSupport {

    private static final Logger log = Logger.getLogger(CdwUserSearchAction.class);

    private static final long serialVersionUID = 1L;

    private CdwUserModel cdwUserBean;

    private CdwPermissionModel cdwPermissionBean;

    @Override
    public String input() throws Exception {
        log.debug("input() - START");
        return INPUT;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String execute() throws Exception {
        log.debug("execute() - START");

        DAOFactory daoFactory = DAOFactory.getDAOFactory();
        ICDWUserPermissionDAO cdwUserPermissionDAO = daoFactory.getCDWUserPermissionDAO();
        log.debug("Username: " + getCdwUserBean().getUsername());
        cdwUserBean.setUserPermission((ArrayList) cdwUserPermissionDAO.searchUserPermissions(getCdwUserBean()));
        if (cdwUserPermissionDAO.isUserExists(getCdwUserBean())) {
            if (cdwUserBean.getUserPermission().size() == 0) {
                addActionError(getText("cdwUserBean.noPermissions"));
            }
        } else {
            addActionError(getText("cdwUserBean.usernameDoesNotExist"));
        }
        cdwUserBean.setPassword("");

        log.debug("execute() - END");
        return INPUT;
    }

    public CdwUserModel getCdwUserBean() {
        return cdwUserBean;
    }

    public void setCdwUserBean(CdwUserModel cdwUserBean) {
        this.cdwUserBean = cdwUserBean;
    }

    public CdwPermissionModel getCdwPermissionBean() {
        return cdwPermissionBean;
    }

    public void setCdwPermissionBean(CdwPermissionModel cdwPermissionBean) {
        this.cdwPermissionBean = cdwPermissionBean;
    }

}
