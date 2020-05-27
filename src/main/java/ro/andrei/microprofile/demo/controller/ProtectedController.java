package ro.andrei.microprofile.demo.controller;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 *
 */
@Path("/protected")
@RequestScoped
@Slf4j
public class ProtectedController {

    @Inject
    @Claim("custom-value")
    private ClaimValue<String> custom;

    @Inject
    private JsonWebToken jwt;

    @GET
    @RolesAllowed("protected")
    public String getJWTBasedValue() {
        log.info(jwt.toString());
        return "Protected Resource; Custom value : " + custom.getValue();
    }
}
