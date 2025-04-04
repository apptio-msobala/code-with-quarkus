package org.acme;

import static io.quarkus.vertx.http.runtime.security.HttpCredentialTransport.Type.OTHER_HEADER;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.security.identity.IdentityProviderManager;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.request.AuthenticationRequest;
import io.quarkus.vertx.http.runtime.security.ChallengeData;
import io.quarkus.vertx.http.runtime.security.HttpAuthenticationMechanism;
import io.quarkus.vertx.http.runtime.security.HttpCredentialTransport;
import io.quarkus.vertx.http.runtime.security.HttpSecurityUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Alternative
@Priority(1)
@ApplicationScoped
public class OpenTokenHttpAuth implements HttpAuthenticationMechanism {

  private static final Logger LOG = LoggerFactory.getLogger(OpenTokenHttpAuth.class);
  private static final String APPTIO_OPENTOKEN = "apptio-opentoken";
  private static final String APPTIO_ENVIRONMENT = "apptio-current-environment";

  @Override
  public Uni<SecurityIdentity> authenticate(RoutingContext context,
      IdentityProviderManager identityProviderManager) {
    var opentoken = context.request().headers().get(APPTIO_OPENTOKEN);
    var environmentId = context.request().headers().get(APPTIO_ENVIRONMENT);
    if (opentoken == null || environmentId == null) {
      return Uni.createFrom().failure(new AuthenticationFailedException());
    }

    var authCredentials = new OpenTokenAuthenticationRequest(opentoken,
        UUID.fromString(environmentId));
    HttpSecurityUtils.setRoutingContextAttribute(authCredentials, context);
    context.put(HttpAuthenticationMechanism.class.getName(), this);
    return identityProviderManager.authenticate(authCredentials);
  }

  @Override
  public Uni<ChallengeData> getChallenge(RoutingContext context) {
    var challengeData = new ChallengeData(HttpResponseStatus.UNAUTHORIZED.code(),
        HttpHeaderNames.WWW_AUTHENTICATE, APPTIO_OPENTOKEN);
    return Uni.createFrom().item(challengeData);
  }

  @Override
  public Set<Class<? extends AuthenticationRequest>> getCredentialTypes() {
    return Collections.singleton(OpenTokenAuthenticationRequest.class);
  }

  @Override
  public Uni<HttpCredentialTransport> getCredentialTransport(RoutingContext context) {
    return Uni.createFrom().item(new HttpCredentialTransport(OTHER_HEADER, APPTIO_OPENTOKEN));
  }

}