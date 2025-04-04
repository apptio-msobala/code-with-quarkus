package org.acme;

import io.quarkus.security.PermissionsAllowed;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class GreetingResource {

    @GET
    @Path("/write")
    @Produces(MediaType.TEXT_PLAIN)
    @PermissionsAllowed("write")
    public String write() {
        return "Write from Quarkus REST";
    }

    @GET
    @Path("/read")
    @Produces(MediaType.TEXT_PLAIN)
    @PermissionsAllowed("read")
    public String read() {
        return "Read from Quarkus REST";
    }
}
