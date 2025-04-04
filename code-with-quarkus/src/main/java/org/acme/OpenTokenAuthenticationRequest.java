package org.acme;

import io.quarkus.security.identity.request.BaseAuthenticationRequest;
import java.util.UUID;

public class OpenTokenAuthenticationRequest extends BaseAuthenticationRequest {

  private final String openToken;
  private final UUID environmentId;

  public OpenTokenAuthenticationRequest(String openToken, UUID environmentId) {
    this.openToken = openToken;
    this.environmentId = environmentId;
  }

  public String getOpenToken() {
    return openToken;
  }

  public UUID getEnvironmentId() {
    return environmentId;
  }
}
