package gov.nih.nci.cacisweb.action;

import gov.nih.nci.cacisweb.CaCISWebConstants;
import gov.nih.nci.cacisweb.exception.KeystoreInstantiationException;
import gov.nih.nci.cacisweb.exception.PropFileAndKeystoreOutOfSyncException;
import gov.nih.nci.cacisweb.model.SecureFTPModel;
import gov.nih.nci.cacisweb.util.CaCISUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;

public class SecureFTPAction extends ActionSupport {

    private static final Logger log = Logger.getLogger(SecureFTPAction.class);

    private static final long serialVersionUID = 1L;

    private List<SecureFTPModel> secureFTPRecepientList;

    private SecureFTPModel secureFTPBean;

    @Override
    public String input() throws Exception {
        log.debug("input() - START");
        secureFTPRecepientList = new ArrayList<SecureFTPModel>();
        String secureFTPKeystoreLocation = CaCISUtil
                .getProperty(CaCISWebConstants.COM_PROPERTY_NAME_SECFTP_RECEPIENT_KEYSTORE_LOCATION);
        String secureFTPKeystorePassword = CaCISUtil
                .getProperty(CaCISWebConstants.COM_PROPERTY_NAME_SECFTP_RECEPIENT_KEYSTORE_PASSWORD);

        CaCISUtil caCISUtil = new CaCISUtil();

        try {
            caCISUtil.isPropertyFileAndKeystoreInSync(CaCISUtil
                    .getProperty(CaCISWebConstants.COM_PROPERTY_NAME_SECFTP_RECEPIENT_CONFIG_FILE_LOCATION),
                    secureFTPKeystoreLocation, CaCISWebConstants.COM_KEYSTORE_TYPE_JKS, secureFTPKeystorePassword);
        } catch (PropFileAndKeystoreOutOfSyncException e) {
            log.error(e.getMessage());
            addActionError(e.getMessage());
        }

        try {
            KeyStore keystore = caCISUtil.getKeystore(secureFTPKeystoreLocation,
                    CaCISWebConstants.COM_KEYSTORE_TYPE_JKS, secureFTPKeystorePassword);
            // List the aliases
            Enumeration<String> enumeration = keystore.aliases();
            while (enumeration.hasMoreElements()) {
                String alias = (String) enumeration.nextElement();
                X509Certificate x509Certificate = (X509Certificate) keystore.getCertificate(alias);
                SecureFTPModel secureFTPModel = new SecureFTPModel();
                secureFTPModel.setCertificateAlias(alias);
                secureFTPModel.setCertificateDN(x509Certificate.getSubjectDN().toString());
                secureFTPRecepientList.add(secureFTPModel);
                log.debug("Alias: " + alias + " DN: " + x509Certificate.getSubjectDN().getName());
            }
            caCISUtil.releaseKeystore();
        } catch (KeystoreInstantiationException kie) {
            log.error(kie.getMessage());
            addActionError(getText("exception.keystoreInstantiation"));
            return ERROR;
        }
        log.debug("input() - END");
        return INPUT;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String delete() throws Exception {
        log.debug("delete() - START");
        String secureFTPKeystoreLocation = CaCISUtil
                .getProperty(CaCISWebConstants.COM_PROPERTY_NAME_SECFTP_RECEPIENT_KEYSTORE_LOCATION);
        try {
            CaCISUtil caCISUtil = new CaCISUtil();
            KeyStore keystore = caCISUtil.getKeystore(secureFTPKeystoreLocation,
                    CaCISWebConstants.COM_KEYSTORE_TYPE_JKS, CaCISUtil
                            .getProperty(CaCISWebConstants.COM_PROPERTY_NAME_SECFTP_RECEPIENT_KEYSTORE_PASSWORD));
            caCISUtil.releaseKeystore();
            // Delete the certificate
            keystore.deleteEntry(secureFTPBean.getCertificateAlias());

            // Save the new keystore contents
            FileOutputStream out = new FileOutputStream(new File(secureFTPKeystoreLocation));
            keystore.store(out, CaCISUtil.getProperty(
                    CaCISWebConstants.COM_PROPERTY_NAME_SECFTP_RECEPIENT_KEYSTORE_PASSWORD).toCharArray());
            out.close();

            // delete the entry from FTP configuration properties file
            PropertiesConfiguration config = new PropertiesConfiguration(CaCISUtil
                    .getProperty(CaCISWebConstants.COM_PROPERTY_NAME_SECFTP_RECEPIENT_CONFIG_FILE_LOCATION));
            config.clearProperty(secureFTPBean.getCertificateAlias());
            config.save();
        } catch (KeystoreInstantiationException kie) {
            log.error(kie.getMessage());
            addActionError(getText("exception.keystoreInstantiation"));
            return ERROR;
        }
        addActionMessage(getText("secureFTPBean.deleteCertificateSuccessful"));
        log.debug("delete() - END");
        return SUCCESS;
    }

    public List<SecureFTPModel> getSecureFTPRecepientList() {
        return secureFTPRecepientList;
    }

    public void setSecureFTPRecepientList(List<SecureFTPModel> secureFTPRecepientList) {
        this.secureFTPRecepientList = secureFTPRecepientList;
    }

    public SecureFTPModel getSecureFTPBean() {
        return secureFTPBean;
    }

    public void setSecureFTPBean(SecureFTPModel secureFTPBean) {
        this.secureFTPBean = secureFTPBean;
    };

}