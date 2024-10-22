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

package org.wso2.healthcare.apim.core.utils;

import org.wso2.healthcare.apim.core.OpenHealthcareEnvironment;
import org.wso2.healthcare.apim.core.OpenHealthcareException;
import org.wso2.healthcare.apim.core.config.CaptchaConfig;

/**
 * Utility class containing configuration related util functions
 */
public class ConfigUtil {

    /**
     * Function to create captcha config (JS object format) for UI containing configurations required for UI
     *
     * @return
     */
    public static String getCaptchaConfigForUI() throws OpenHealthcareException {

        CaptchaConfig captchaConfig = OpenHealthcareEnvironment.getInstance().getConfig().getCaptchaConfig();
        return String.format("{enable : %s, siteKey : \"%s\"}", captchaConfig.isEnable(), captchaConfig.getSiteKey());
    }

}
