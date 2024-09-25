/*
 * Copyright (c) 2024, WSO2 LLC. (http://www.wso2.org).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.healthcare.consentmgt.core;

import org.wso2.healthcare.consentmgt.core.dao.ConsentPIICategoryMappingDAO;
import org.wso2.healthcare.consentmgt.core.dao.impl.ConsentPIICategoryMappingDAOImpl;
import org.wso2.healthcare.consentmgt.core.exception.FHIRConsentMgtException;
import org.wso2.healthcare.consentmgt.core.model.PIICategoryMapping;

import java.util.List;

public class PIICategoryMapperImpl implements PIICategoryMapper {

    private ConsentPIICategoryMappingDAO mappingDAO;

    public PIICategoryMapperImpl() {
        this.mappingDAO = new ConsentPIICategoryMappingDAOImpl();
    }

    @Override
    public PIICategoryMapping addPIICategoryMapping(PIICategoryMapping mapping) throws FHIRConsentMgtException {
        return mappingDAO.addPIICategoryMapping(mapping);
    }

    @Override
    public PIICategoryMapping getPIICategoryFromId(Integer id) throws FHIRConsentMgtException {
        return mappingDAO.getPIICategoryMappingFromId(id);
    }

    @Override
    public PIICategoryMapping getPIICategoryFromCategoryId(Integer categoryId)
            throws FHIRConsentMgtException {
        List<PIICategoryMapping> piiCategoryMappings = mappingDAO.listPIICategoryMapping(null, null, categoryId, null);
        return piiCategoryMappings.get(0);
    }
    
    @Override
    public List<PIICategoryMapping> searchPIICategoryMapping(Integer limit, Integer offset,
            Integer categoryId, String type) throws FHIRConsentMgtException {
        return mappingDAO.listPIICategoryMapping(limit,offset,categoryId,type);
    }
}