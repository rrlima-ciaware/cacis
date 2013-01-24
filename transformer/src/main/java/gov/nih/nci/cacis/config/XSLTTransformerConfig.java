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
package gov.nih.nci.cacis.config;

import gov.nih.nci.cacis.common.util.AnyBasePathURIResolver;
import gov.nih.nci.cacis.transform.GenerateNarrativeTransformer;
import gov.nih.nci.cacis.transform.HL7V2Transformer;
import gov.nih.nci.cacis.transform.RIMITSTransformer;
import gov.nih.nci.cacis.transform.SourceTransformer;
import gov.nih.nci.cacis.transform.Trim2CCDTransformer;
import gov.nih.nci.cacis.transform.XmlToRdfTransformer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author kherm manav.kher@semanticbits.com
 */
@Configuration
public class XSLTTransformerConfig {

    @Value("${cacis-pco.transformer.xml2rdf.xsl}")
    private String rdfToXmlXsl;

    @Value("${cacis-pco.transformer.hl7v2.xsl}")
    private String hl7v2Xsl;

    @Value("${cacis-pco.transformer.rimits.xsl}")
    private String rimITSXsl;

    @Value("${cacis-pco.transformer.xsl.baseClassPath}")
    private String xslBaseClassPath;

    @Value("${cacis-pco.transformer.trim2ccd.xsl}")
    private String trim2CCDXsl;

    @Value("${cacis-pco.transformer.generate-narrative.xsl}")
    private String generateNarrativeXsl;

    @Value("${cacis-pco.source.system.baseClassPath}")
    private String sourceSystemBaseClassPath;
    
    @Value("${cacis-pco.source.system.xslt.properties.file}")
    private String sourceSystemXSLTProperties;

    private static final String PROTOTYPE = "prototype";

    /**
     * {@inheritDoc}
     */
    public URIResolver xslUriResolver() {
        return new AnyBasePathURIResolver(xslBaseClassPath);
    }

    /**
     * {@inheritDoc}
     */
    @Bean
    public TransformerFactory xslTransformerFactory() {
        final TransformerFactory tf = TransformerFactory.newInstance();
        tf.setURIResolver(xslUriResolver());
        return tf;
    }

    /**
     * XML To RDF Transformer
     * 
     * @return XmlToRdfTransformer transformer
     * @throws TransformerException exception
     */
    @Bean
    @Scope(PROTOTYPE)
    public XmlToRdfTransformer xmlToRdfTransformer() throws TransformerException {
        final Transformer transformer = xslTransformerFactory().newTransformer(
                xslUriResolver().resolve(rdfToXmlXsl, xslBaseClassPath));
        return new XmlToRdfTransformer(transformer);
    }

    /**
     * 
     * @return HL7V2Transformer transformer
     * @throws TransformerException exception
     */
    @Bean
    @Scope(PROTOTYPE)
    public HL7V2Transformer hL7V2Transformer() throws TransformerException {
        final Transformer transformer = xslTransformerFactory().newTransformer(
                xslUriResolver().resolve(hl7v2Xsl, xslBaseClassPath));
        return new HL7V2Transformer(transformer);
    }

    /**
     * 
     * @return HL7V2Transformer transformer
     * @throws TransformerException exception
     */
    @Bean
    @Scope(PROTOTYPE)
    public RIMITSTransformer rIMITSTransformer() throws TransformerException {
        final Transformer transformer = xslTransformerFactory().newTransformer(
                xslUriResolver().resolve(rimITSXsl, xslBaseClassPath));
        return new RIMITSTransformer(transformer);
    }

    /**
     * Trim to CCD Transformer
     * 
     * @return HL7V2Transformer transformer
     * @throws TransformerException exception
     */
    @Bean
    @Scope(PROTOTYPE)
    public Trim2CCDTransformer trim2CCDTransformer() throws TransformerException {
        final Transformer transformer = xslTransformerFactory().newTransformer(
                xslUriResolver().resolve(trim2CCDXsl, xslBaseClassPath));
        return new Trim2CCDTransformer(transformer);
    }

    @Bean
    @Scope(PROTOTYPE)
    public SourceTransformer sourceTransformer(String sourceSystemIdentifier) throws TransformerException {
        try {
            PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(sourceSystemXSLTProperties);
            String propertyValue = propertiesConfiguration.getString(sourceSystemIdentifier);
            if (propertyValue == null) {
                throw new TransformerException(String.format("Source system '%s' is not configured in [%s]!",
                        sourceSystemIdentifier, sourceSystemXSLTProperties));
            }
            final Transformer transformer = xslTransformerFactory().newTransformer(
                    xslUriResolver().resolve(propertyValue, sourceSystemBaseClassPath));
            return new SourceTransformer(transformer);
        } catch (ConfigurationException e) {
            throw new TransformerException(String.format("Property '%s' cannot be read from [%s]. Either the "
                    + "property / properties file does not exist or the properties file is not readable!",
                    sourceSystemIdentifier, sourceSystemXSLTProperties)
                    + e);
        }
    }

    /**
     * Generate Narrative Transformer
     * 
     * @return GenerateNarrativeTransformer transformer
     * @throws TransformerException exception
     */
    @Bean
    @Scope(PROTOTYPE)
    public GenerateNarrativeTransformer generateNarrativeTransformer() throws TransformerException {
        final Transformer transformer = xslTransformerFactory().newTransformer(
                xslUriResolver().resolve(generateNarrativeXsl, xslBaseClassPath));
        return new GenerateNarrativeTransformer(transformer);
    }

}
