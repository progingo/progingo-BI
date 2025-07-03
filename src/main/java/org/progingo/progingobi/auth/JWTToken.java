package org.progingo.progingobi.auth;

import lombok.Data;
import lombok.ToString;
import org.apache.shiro.authc.AuthenticationToken;

@Data
@ToString
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }
}
