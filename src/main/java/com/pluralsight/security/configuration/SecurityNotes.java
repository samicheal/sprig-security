package com.pluralsight.security.configuration;

public class SecurityNotes {


    /**
     * Hacking Digest Authentication with Spring Security
     *
     * The following lines of code were added to the Admin security configuration class to allow the hacking of the
     * digest authentication filter
     *
     * filter.setPasswordAlreadyEncoded(true); // prevents filter from generating HA1 but uses one obtained from userdetails service
     * filter.setCreateAuthenticatedToken(true); //forces digest to use the encrypted token from the userdetails service
     *
     * in the configure(AuthenticationManagerBuilder auth) method:
     * .password("18d94adb9db016d4aed2502f88ca6e56") //password already encrypted using MD5
     *
     * To improve the security of the digest authentication type, Spring security added a stale property to the authenticate
     * Header returned to the browser during digest authentication.
     * This propety ensures that the nonce value, which is a central theme of digest, cannot be used in a replay attack.
     * Hence the nonce becomes a one-way, one-time use value, thereby adding some extra security to the scheme.
     *
     * I broke this by intercepting the authenticateHeader and resetting the value of the stale property to false.
     *
     *
     * Used postman to simulate the request
     *
     * Set the Authorization value of postman to {
     *      Digest username="admin", realm="admin-digest-realm",
     *      nonce="MTU5NTExNjM3Mzg1NTo2OWVjNzQyNjBkNWUxZmUwNjAwYTFiY2M4ZTIxYmI1Mg==",
     *      uri="/support/admin", response="24504b4ee62229e51f460498b28b4c1a"
     * }
     *
     * Set URL = http://localhost:8087/support/admin
     *
     */
}
