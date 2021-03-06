/**
 * Copyright 5AM Solutions Inc
 * Copyright SemanticBits LLC
 * Copyright AgileX Technologies, Inc
 * Copyright Ekagra Software Technologies Ltd
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cacis/LICENSE.txt for details.
 */
package gov.nih.nci.cacis.transform;

import javax.xml.transform.Transformer;

/**
 * @author bpickeral
 * @since Sep 2, 2011
 */
public class Trim2CCDTransformer extends XSLTTransformer {
    /**
     * Constructor
     *
     * @param transformer xslt transformer
     */
    public Trim2CCDTransformer(Transformer transformer) {
        super(transformer);
    }
}
