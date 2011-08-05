/**
 * The software subject to this notice and license includes both human readable source code form and machine readable,
 * binary, object code form. The caEHR Software was developed in conjunction with the National Cancer Institute (NCI) by
 * NCI employees and 5AM Solutions Inc, SemanticBits LLC, and AgileX Technologies, Inc (collectively 'SubContractors').
 * To the extent government employees are authors, any rights in such works shall be subject to Title 17 of the United
 * States Code, section 105.
 *
 * This caEHR Software License (the License) is between NCI and You. You (or Your) shall mean a person or an entity, and
 * all other entities that control, are controlled by, or are under common control with the entity. Control for purposes
 * of this definition means (i) the direct or indirect power to cause the direction or management of such entity,
 * whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or
 * (iii) beneficial ownership of such entity.
 *
 * This License is granted provided that You agree to the conditions described below. NCI grants You a non-exclusive,
 * worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its
 * rights in the caEHR Software to (i) use, install, access, operate, execute, copy, modify, translate, market, publicly
 * display, publicly perform, and prepare derivative works of the caEHR Software; (ii) distribute and have distributed
 * to and by third parties the caEHR Software and any modifications and derivative works thereof; and (iii) sublicense
 * the foregoing rights set out in (i) and (ii) to third parties, including the right to license such rights to further
 * third parties. For sake of clarity, and not by way of limitation, NCI shall have no right of accounting or right of
 * payment from You or Your sub-licensees for the rights granted under this License. This License is granted at no
 * charge to You.
 *
 * Your redistributions of the source code for the Software must retain the above copyright notice, this list of
 * conditions and the disclaimer and limitation of liability of Article 6, below. Your redistributions in object code
 * form must reproduce the above copyright notice, this list of conditions and the disclaimer of Article 6 in the
 * documentation and/or other materials provided with the distribution, if any.
 *
 * Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: This
 * product includes software developed by the National Cancer Institute and SubContractor parties. If You do not include
 * such end-user documentation, You shall include this acknowledgment in the Software itself, wherever such third-party
 * acknowledgments normally appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", or any SubContractor party to endorse or promote
 * products derived from this Software. This License does not authorize You to use any trademarks, service marks, trade
 * names, logos or product names of either NCI or any of the subcontracted parties, except as required to comply with
 * the terms of this License.
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this Software into Your proprietary programs
 * and into any third party proprietary programs. However, if You incorporate the Software into third party proprietary
 * programs, You agree that You are solely responsible for obtaining any permission from such third parties required to
 * incorporate the Software into such third party proprietary programs and for informing Your sub-licensees, including
 * without limitation Your end-users, of their obligation to secure any required permissions from such third parties
 * before incorporating the Software into such third party proprietary software programs. In the event that You fail to
 * obtain such permissions, You agree to indemnify NCI for any claims against NCI by such third parties, except to the
 * extent prohibited by law, resulting from Your failure to obtain such permissions.
 *
 * For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and
 * to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses
 * of modifications of the Software, or any derivative works of the Software as a whole, provided Your use,
 * reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, ANY OF ITS SUBCONTRACTED PARTIES OR THEIR AFFILIATES BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.cacis.nav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.icegreen.greenmail.util.GreenMail;

/**
 * Class used by MC to send notification via NotificationSender.
 * @author bpickeral
 * @since Aug 4, 2011
 */
public class DocRouterNotificationSender {

    @Autowired
    private GreenMail server;

    @Autowired
    private NotificationSenderImpl notificationSender;

    @Value("${nav.keystore.type}")
    private String keyStoreType;

    @Value("${nav.keystore.location}")
    private String keyStoreLocation;

    @Value("${nav.keystore.password}")
    private String keyStorePassword;

    @Value("${nav.keystore.key}")
    private String keyStoreKey;

    @Value("${nav.message.instructions}")
    private String instructions;

    @Value("${nav.message.subject}")
    private String subject;

    @Value("${nav.message.to}")
    private String mailTo;

    @Value("${nav.message.from}")
    private String mailFrom;


    /**
     * Used by MC to send an email Notification using the Document Router.
     * @param docId Document ID
     * @param regId Reg Id
     * @param document Clinical Document exchange CCD
     * @throws IOException on error
     * @throws NotificationSendException on send error
     */
    public void sendNotification(String docId, String regId, String document) throws IOException,
            NotificationSendException {
        try {
            server.start();

            final XDSNotificationSignatureBuilder sigBuilder = new DefaultXDSNotificationSignatureBuilder(
                    getDocumentResolver(docId, regId, document), SignatureMethod.RSA_SHA1, DigestMethod.SHA1, keyStoreType,
                    keyStoreLocation, keyStorePassword, keyStoreKey);

            // props.setProperty("mail.debug", TRUE);

            notificationSender.setSignatureBuilder(sigBuilder);
            notificationSender.setHost(server.getSmtp().getBindTo());
            notificationSender.setPort(server.getSmtp().getPort());
            notificationSender.setProtocol(server.getSmtp().getProtocol());
            notificationSender.setInstructions(instructions);
            notificationSender.setSubject(subject);
            notificationSender.setTo(mailTo);
            notificationSender.setFrom(mailFrom);
            notificationSender.setMailProperties(new Properties());
            notificationSender.setUserName("");


            final String[] keys = { docId };
            notificationSender.send(new ArrayList<String>(Arrays.asList(keys)));
        } finally {
            server.stop();
        }

    }

    private XDSDocumentResolver getDocumentResolver(String docId, String regId, String document) throws IOException {
        final InMemoryCacheDocumentHolder h = new InMemoryCacheDocumentHolder();
        h.putDocument(docId, document.getBytes());
        return new InMemoryCacheXDSDocumentResolver(regId, h);
    }

    /**
     * Get the server.
     * @return server
     */
    public GreenMail getServer() {
        return server;
    }
}
