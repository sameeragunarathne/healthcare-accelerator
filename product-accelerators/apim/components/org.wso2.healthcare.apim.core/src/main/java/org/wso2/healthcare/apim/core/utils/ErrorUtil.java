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

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Utility class containing utility functions related to Error handling
 */
public class ErrorUtil {

    /**
     * Returns the stacktrace from the throwable. If the throwable is null, it will return the stacktrace of the
     * current thread
     *
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable != null) {
            return ExceptionUtils.getFullStackTrace(throwable);
        }
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement element : stackTraceElements) {
            stringBuilder.append(element.toString()).append('\n');
        }
        return stringBuilder.toString();
    }

}
