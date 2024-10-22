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
package org.wso2.healthcare.apim.clientauth.jwt.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.identity.core.util.IdentityCoreInitializedEvent;
import org.wso2.carbon.identity.oauth2.client.authentication.OAuthClientAuthenticator;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.healthcare.apim.clientauth.jwt.PrivateKeyJWTClientAuthenticator;

/**
 * JwtService osgi Component.*
 */
@Component(
        name = "org.wso2.healthcare.apim.clientauth.jwt",
        immediate = true
)
public class JWTServiceComponent {

    private static final Log log = LogFactory.getLog(JWTServiceComponent.class);
    private BundleContext bundleContext;

    public static RealmService getRealmService() {

        return JWTServiceDataHolder.getInstance().getRealmService();
    }

    @Reference(
            name = "user.realmservice.default",
            service = RealmService.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetRealmService"
    )
    protected void setRealmService(RealmService realmService) {

        JWTServiceDataHolder.getInstance().setRealmService(realmService);
        if (log.isDebugEnabled()) {
            log.debug("RealmService is set in the custom token builder bundle.");
        }
    }

    @Activate
    protected void activate(ComponentContext ctxt) {

        try {
            PrivateKeyJWTClientAuthenticator privateKeyJWTClientAuthenticator = new PrivateKeyJWTClientAuthenticator();
            bundleContext = ctxt.getBundleContext();
            bundleContext.registerService(OAuthClientAuthenticator.class.getName(), privateKeyJWTClientAuthenticator,
                    null);
            if (log.isDebugEnabled()) {
                log.debug("Private Key JWT client handler is activated");
            }
        } catch (Throwable e) {
            log.fatal("Error while activating the Private Key JWT client handler. ", e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {

        if (log.isDebugEnabled()) {
            log.debug("Private Key JWT client handler is deactivated.");
        }
    }

    protected void unsetRealmService(RealmService realmService) {

        JWTServiceDataHolder.getInstance().setRealmService(null);
        if (log.isDebugEnabled()) {
            log.debug("RealmService is unset in the custom token builder bundle.");
        }
    }

    protected void unsetIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
        if (log.isDebugEnabled()) {
            log.debug("Unset Identity core Intialized Event Service.");
        }
    }

    @Reference(
            name = "identityCoreInitializedEventService",
            service = IdentityCoreInitializedEvent.class,
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetIdentityCoreInitializedEventService"
    )
    protected void setIdentityCoreInitializedEventService(IdentityCoreInitializedEvent identityCoreInitializedEvent) {
        /* reference IdentityCoreInitializedEvent service to guarantee that this component will wait until identity core
         is started */
        if (log.isDebugEnabled()) {
            log.debug("Set Identity core Intialized Event Service.");
        }
    }
}
