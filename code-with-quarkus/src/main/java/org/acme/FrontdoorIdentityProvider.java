package org.acme;

import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.IdentityProvider;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FrontdoorIdentityProvider implements IdentityProvider<OpenTokenAuthenticationRequest> {

  @Override
  public Class<OpenTokenAuthenticationRequest> getRequestType() {
    return OpenTokenAuthenticationRequest.class;
  }

  @Override
  public Uni<SecurityIdentity> authenticate(
      OpenTokenAuthenticationRequest openTokenAuthenticationRequest,
      AuthenticationRequestContext authenticationRequestContext) {
    return Uni.createFrom().item(new FrontdoorUserIdentity());
  }
}
