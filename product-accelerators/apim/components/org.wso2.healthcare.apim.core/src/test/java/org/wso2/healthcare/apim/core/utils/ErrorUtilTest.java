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

import org.junit.Test;
import org.wso2.healthcare.apim.core.OpenHealthcareException;

import static org.junit.Assert.assertTrue;

public class ErrorUtilTest {

    @Test
    public void getStackTrace() {

        OpenHealthcareException e = new OpenHealthcareException("Test Exp");
        String trace = ErrorUtil.getStackTrace(e);
        assertTrue(trace.contains("Test Exp"));
    }

    @Test
    public void getStackTraceNull() {

        String trace = ErrorUtil.getStackTrace(null);
        assertTrue(trace.contains("java.lang.Thread.getStackTrace(Thread.java:"));
    }
}
