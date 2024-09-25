/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.com).
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
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.healthcare.apim.conformance;

import java.util.List;
import java.util.Map;

/**
 * Interface to specify API definition related functions.
 */
public interface OASHandler {

    /**
     * get vendor specific extension of the API definition file.
     * @return
     */
    Map<String, Object> getVendorExtentions();

    /**
     * set OAS2,OAS3 definition file.
     * @param apiDefinition
     */
    void setAPIDefinition(String apiDefinition);

    /**
     * Get title of API definition.
     * @return
     */
    String getTitle();

    /**
     * Get searchparameter names.
     * @return
     */
    List<String> getSearchParameters();

}
