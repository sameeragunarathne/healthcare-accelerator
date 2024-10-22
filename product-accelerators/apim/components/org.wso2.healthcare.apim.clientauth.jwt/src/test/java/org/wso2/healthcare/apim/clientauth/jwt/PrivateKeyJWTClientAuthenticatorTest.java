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

package org.wso2.healthcare.apim.clientauth.jwt;

import org.mockito.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.base.CarbonBaseConstants;
import org.wso2.carbon.identity.base.IdentityConstants;
import org.wso2.carbon.identity.common.testng.WithAxisConfiguration;
import org.wso2.carbon.identity.common.testng.WithCarbonHome;
import org.wso2.carbon.identity.common.testng.WithH2Database;
import org.wso2.carbon.identity.common.testng.WithKeyStore;
import org.wso2.carbon.identity.common.testng.WithRealmService;
import org.wso2.carbon.identity.core.util.IdentityUtil;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.bean.OAuthClientAuthnContext;
import org.wso2.healthcare.apim.clientauth.jwt.internal.JWTServiceComponent;
import org.wso2.healthcare.apim.clientauth.jwt.validator.JWTValidatorTest;

import java.security.Key;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.wso2.healthcare.apim.clientauth.jwt.Constants.OAUTH_JWT_ASSERTION;
import static org.wso2.healthcare.apim.clientauth.jwt.Constants.OAUTH_JWT_ASSERTION_TYPE;
import static org.wso2.healthcare.apim.clientauth.jwt.Constants.OAUTH_JWT_BEARER_GRANT_TYPE;
import static org.wso2.healthcare.apim.clientauth.jwt.Constants.SCOPE;
import static org.wso2.healthcare.apim.clientauth.jwt.util.JWTTestUtil.buildJWT;
import static org.wso2.healthcare.apim.clientauth.jwt.util.JWTTestUtil.getKeyStoreFromFile;
import static org.wso2.carbon.utils.multitenancy.MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
import static org.wso2.carbon.utils.multitenancy.MultitenantConstants.SUPER_TENANT_ID;

@WithCarbonHome
@WithAxisConfiguration
@WithH2Database(jndiName = "jdbc/WSO2CarbonDB", files = {"dbscripts/identity.sql"})
@WithRealmService(tenantId = SUPER_TENANT_ID, tenantDomain = SUPER_TENANT_DOMAIN_NAME,
        injectToSingletons = {JWTServiceComponent.class})
@WithKeyStore
public class PrivateKeyJWTClientAuthenticatorTest {

    PrivateKeyJWTClientAuthenticator privateKeyJWTClientAuthenticator = new PrivateKeyJWTClientAuthenticator();
    @Mock
    HttpServletRequest httpServletRequest;

    OAuthClientAuthnContext oAuthClientAuthnContext =new OAuthClientAuthnContext();

    KeyStore clientKeyStore;
    Key key1;
    String audience;

    @BeforeClass
    public void setUp() throws Exception {

        clientKeyStore = getKeyStoreFromFile("testkeystore.jks", "wso2carbon",
                System.getProperty(CarbonBaseConstants.CARBON_HOME));
        key1 = clientKeyStore.getKey("wso2carbon", "wso2carbon".toCharArray());
        audience = IdentityUtil.getServerURL(IdentityConstants.OAuth.TOKEN, true, false);
    }

    @Test
    public void testGetClientId() throws Exception {

        Map<String, List> bodyContent = new HashMap<>();
        List<String> assertion = new ArrayList<>();
        assertion.add(buildJWT(JWTValidatorTest.TEST_CLIENT_ID_1, JWTValidatorTest.TEST_CLIENT_ID_1, "3000", audience,
                300000, key1));
        bodyContent.put(OAUTH_JWT_ASSERTION, assertion);
        String clientId = privateKeyJWTClientAuthenticator.getClientId(httpServletRequest, bodyContent,
                oAuthClientAuthnContext);
        assertEquals(clientId, "KrVLov4Bl3natUksF2HmWsdw684a", "The expected client id is the jwt subject.");

    }

    @Test
    public void testcanAuthenticateWithoutScope() throws IdentityOAuth2Exception {

        Map<String, List> bodyContent = new HashMap<>();
        List<String> assertion = new ArrayList<>();
        List<String> assertionType = new ArrayList<>();
        assertion.add(
                buildJWT(JWTValidatorTest.TEST_CLIENT_ID_1, JWTValidatorTest.TEST_CLIENT_ID_1, "3000", audience, 30000,
                        key1));
        assertionType.add(OAUTH_JWT_BEARER_GRANT_TYPE);
        bodyContent.put(OAUTH_JWT_ASSERTION, assertion);

        bodyContent.put(OAUTH_JWT_ASSERTION_TYPE, assertionType);
        boolean received = privateKeyJWTClientAuthenticator.canAuthenticate(httpServletRequest, bodyContent,
                oAuthClientAuthnContext);
        assertFalse(received, "Invalid request authenticated.");

    }

    @Test
    public void testcanAuthenticateWithScope() throws IdentityOAuth2Exception {

        Map<String, List> bodyContent = new HashMap<>();
        List<String> assertion = new ArrayList<>();
        List<String> assertionType = new ArrayList<>();
        List<String> scopes = new ArrayList<>();
        assertion.add(
                buildJWT(JWTValidatorTest.TEST_CLIENT_ID_1, JWTValidatorTest.TEST_CLIENT_ID_1, "3000", audience, 30000,
                        key1));
        assertionType.add(OAUTH_JWT_BEARER_GRANT_TYPE);
        scopes.add("default");
        bodyContent.put(OAUTH_JWT_ASSERTION, assertion);

        bodyContent.put(OAUTH_JWT_ASSERTION_TYPE, assertionType);
        bodyContent.put(SCOPE, scopes);
        boolean received = privateKeyJWTClientAuthenticator.canAuthenticate(httpServletRequest, bodyContent,
                oAuthClientAuthnContext);
        assertTrue(received, "Valid request failed.");

    }
}
