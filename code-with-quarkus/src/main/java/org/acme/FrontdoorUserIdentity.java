package org.acme;

import io.quarkus.security.StringPermission;
import io.quarkus.security.credential.Credential;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;
import java.security.Permission;
import java.security.Principal;
import java.util.Map;
import java.util.Set;

public class FrontdoorUserIdentity implements SecurityIdentity {

  Map<String, Set<Permission>> rolePermissions = Map.of("Apptio Admin",
      Set.of(new StringPermission("read"), new StringPermission("write")),
      "Apptio User",
      Set.of(new StringPermission("read")));

  @Override
  public Principal getPrincipal() {
    return null;
  }

  @Override
  public boolean isAnonymous() {
    return false;
  }

  @Override
  public Set<String> getRoles() {
    return Set.of("Apptio User");
  }

  @Override
  public boolean hasRole(String s) {
    return getRoles().contains(s);
  }

  @Override
  public <T extends Credential> T getCredential(Class<T> aClass) {
    return null;
  }

  @Override
  public Set<Credential> getCredentials() {
    return null;
  }

  @Override
  public <T> T getAttribute(String s) {
    return null;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return null;
  }

  @Override
  public Uni<Boolean> checkPermission(Permission permission) {
    return Uni.createFrom().item(getRoles().stream()
        .flatMap(role -> rolePermissions.get(role).stream())
        .anyMatch(permission::equals));
  }
}
