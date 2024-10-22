/*
 * Copyright (c) 2024, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.healthcare.apim.conformance;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handle OAS2 API definitions.
 */
public class OAS3Handler implements OASHandler {

    private static final Log log = LogFactory.getLog(OAS3Handler.class);
    private OpenAPI openAPI;

    public OAS3Handler(String apiDefinition) {

        openAPI = getOpenAPI(apiDefinition);
    }

    /**
     * get vendor specific extension of the API definition file.
     *
     * @return
     */
    @Override
    public Map<String, Object> getVendorExtentions() {

        return openAPI.getExtensions();
    }

    /**
     * set OAS2,OAS3 definition file.
     *
     * @param apiDefinition
     */
    @Override
    public void setAPIDefinition(String apiDefinition) {

        openAPI = getOpenAPI(apiDefinition);
    }

    /**
     * Get title of API definition.
     *
     * @return
     */
    @Override
    public String getTitle() {

        return openAPI.getInfo().getTitle();
    }

    /**
     * Get searchparameter names.
     *
     * @return
     */
    @Override
    public List<String> getSearchParameters() {

        List<String> searchParamNames = new ArrayList<>();
        PathItem pathItem = openAPI.getPaths().get("/");
        if (pathItem != null && pathItem.getGet() != null && pathItem.getGet().getParameters() != null) {
            for (Parameter parameter : openAPI.getPaths().get("/").getGet().getParameters()) {
                if (parameter.getName() != null) {
                    searchParamNames.add(parameter.getName());
                } else {
                    //referenced parameter, need to extract from components object.
                    //Referenced Param String: #/components/parameters/_<param-name>Param"
                    String referencedParam = "";
                    if (parameter.get$ref() != null) {
                        referencedParam = openAPI.getComponents().getParameters().get(
                                parameter.get$ref().split("/")[3]).getName();
                    }
                    if (!StringUtils.isEmpty(referencedParam)) {
                        searchParamNames.add(referencedParam);
                    }
                }
            }
        }
        return searchParamNames;
    }

    /**
     * Extracted from org.wso2.carbon.apimgt.impl.definitions.OAS3Parser in the base product.
     *
     * @param oasDefinition serialized apiDefinition file.
     * @return
     */
    OpenAPI getOpenAPI(String oasDefinition) {

        OpenAPIV3Parser openAPIV3Parser = new OpenAPIV3Parser();
        SwaggerParseResult parseAttemptForV3 = openAPIV3Parser.readContents(oasDefinition, null, null);
        if (CollectionUtils.isNotEmpty(parseAttemptForV3.getMessages())) {
            log.debug("Errors found when parsing OAS definition");
        }
        return parseAttemptForV3.getOpenAPI();
    }

}
