package com.pluralsight.security.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

public class CustomDigestAuthenticationEntryPointClass extends DigestAuthenticationEntryPoint {

    private static final Log logger = LogFactory.getLog(DigestAuthenticationEntryPoint.class);
    private String key;
    private String realmName;
    private int nonceValiditySeconds = 300;
    private int order = 2147483647;

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        long expiryTime = System.currentTimeMillis() + (long)(this.nonceValiditySeconds * 1000);
        String signatureValue = DigestAuthUtils.md5Hex(expiryTime + ":" + this.key);
        String nonceValue = expiryTime + ":" + signatureValue;
        String nonceValueBase64 = new String(Base64.getEncoder().encode(nonceValue.getBytes()));
        String authenticateHeader = "Digest realm=\"" + this.realmName + "\", qop=\"auth\", nonce=\"" + nonceValueBase64 + "\"";
        if (authException instanceof NonceExpiredException) {
            authenticateHeader = authenticateHeader + ", stale=\"false\"";
        }

        if (logger.isDebugEnabled()) {
            logger.debug("WWW-Authenticate header sent to user agent: " + authenticateHeader);
        }

        response.addHeader("WWW-Authenticate", authenticateHeader);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }



}
