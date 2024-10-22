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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.base.CarbonBaseConstants;
import org.wso2.carbon.identity.common.testng.WithCarbonHome;
import org.wso2.carbon.identity.common.testng.WithKeyStore;

import java.security.Key;
import java.security.KeyStore;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.wso2.healthcare.apim.clientauth.jwt.util.JWTTestUtil.buildJWT;
import static org.wso2.healthcare.apim.clientauth.jwt.util.JWTTestUtil.getKeyStoreFromFile;

@WithCarbonHome
@WithKeyStore
public class JWTCacheTest {

    private JWTCache jwtCache;
    private JWTCacheEntry cacheEntry;
    private SignedJWT signedJWT;

    @BeforeClass
    public void setUp() throws Exception {
        KeyStore clientKeyStore = getKeyStoreFromFile("testkeystore.jks", "wso2carbon",
                System.getProperty(CarbonBaseConstants.CARBON_HOME));
        jwtCache = JWTCache.getInstance();
        Key key1 = clientKeyStore.getKey("wso2carbon", "wso2carbon".toCharArray());

        String privateKeyJWT1 = buildJWT("test-issuer", "test-subject", "test-jti", "test-audience", 150000, key1);
        signedJWT = SignedJWT.parse(privateKeyJWT1);
        cacheEntry = new JWTCacheEntry(signedJWT);
    }

    @Test()
    public void testAddToCache() throws Exception {
        jwtCache.addToCache("test-key:test-issuer",cacheEntry);

    }

    @Test(dependsOnMethods = {"testAddToCache"})
    public void testGetValueFromCache() throws Exception {
        assertEquals(jwtCache.getValueFromCache("test-key:test-issuer"), cacheEntry);
    }

    @Test(dependsOnMethods = {"testGetValueFromCache"})
    public void testClearCacheEntry() throws Exception {
        jwtCache.clearCacheEntry("test-key:test-issuer");
        assertNull(jwtCache.getValueFromCache("test-key:test-issuer"));

    }
}
