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

package org.wso2.healthcare.apim.clientauth.jwt.cache;

import com.nimbusds.jwt.SignedJWT;
import org.wso2.carbon.identity.application.common.cache.CacheEntry;

import java.text.ParseException;

/**
 * Cache Entry for JWT Cache
 */
public class JWTCacheEntry extends CacheEntry {
    private String encodedJWT;

    public JWTCacheEntry(SignedJWT jwt) {
        this.encodedJWT = jwt.serialize();
    }

    public SignedJWT getJwt() throws ParseException {
        return SignedJWT.parse(this.encodedJWT);
    }

    public String getEncodedJWt() {
        return this.encodedJWT;
    }
}
