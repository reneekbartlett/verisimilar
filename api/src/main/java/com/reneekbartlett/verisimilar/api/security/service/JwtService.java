package com.reneekbartlett.verisimilar.api.security.service;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;

import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.reneekbartlett.verisimilar.api.exception.TokenVerificationException;
import com.reneekbartlett.verisimilar.api.security.ApiKeyProperties;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
//import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final SecretKey secretKey;

    /**
     * Initializes the service with a 128-bit (16-byte) master key string.
     * In a production app, fetch this securely via environment variables or a vault.
     */
    public JwtService(ApiKeyProperties properties) {
        byte[] keyBytes = properties.getSharedSecret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 32) {
            throw new IllegalArgumentException("Key must be exactly 256 bits (32 bytes) long for AES-256.");
        }
        // Direct encryption requires standard SecretKeySpec mapping
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * Decrypts and parses the JWT token.
     * Parse the compact token string, Decrypt using the direct AES-256 key
     * @throws ParseException 
     */
    public JWTClaimsSet extractClaims(String encryptedToken) throws JOSEException, ParseException { 
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(encryptedToken);
        encryptedJWT.decrypt(new DirectDecrypter(this.secretKey));

        // Validate Expiration
        JWTClaimsSet claimsSet = encryptedJWT.getJWTClaimsSet();
        if (claimsSet.getExpirationTime().before(new Date())) {
            throw new JOSEException("Token expired");
        }

        return claimsSet;
    }

    public boolean verifyTokenSignature(SignedJWT signedJWT, JWSVerifier verifier) {
        try {
            // This method throws a checked JOSEException
            return signedJWT.verify(verifier);
        } catch (JOSEException e) {
            // Translate it immediately. The checked exception is buried here.
            throw new TokenVerificationException("Cryptographic token verification failed", e);
        }
    }

    public String extractUsername(JWTClaimsSet claims) throws JOSEException, ParseException {
        return claims.getSubject();
    }

    public List<String> extractRoles(JWTClaimsSet claims) throws JOSEException, ParseException {
        List<String> roles;
        try {
            roles = claims.getStringListClaim("roles");
        } catch (ParseException e) {
            roles = List.of();
        }
        return roles;
    }

    public String getUsernameFromToken(String encryptedToken) throws JOSEException, ParseException {
        JWTClaimsSet claims = extractClaims(encryptedToken);
        return extractUsername(claims);
    }

    public List<String> getRolesFromToken(String encryptedToken) throws JOSEException, ParseException {
        JWTClaimsSet claims = extractClaims(encryptedToken);
        return extractRoles(claims);
    }

    /**
     * Generates an encrypted JWE token containing username and roles.
     */
    public String generateToken(String username, List<String> roles) throws JOSEException {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("roles", roles)
                .expirationTime(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiry
                .issueTime(new Date())
                .build();

        // Define JWEHeader for encryption via AES-256 GCM, create JWE
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);

        EncryptedJWT encryptedJWT = new EncryptedJWT(header, claimsSet);

        // Encrypt with secret Key
        encryptedJWT.encrypt(new DirectEncrypter(secretKey));

        return encryptedJWT.serialize();
    }

    /**
     * Encrypts a string payload into a compact JWE string.
     */
    public String encrypt(String payload) throws JOSEException {
        // Define your payload
        Payload jwePayload = new Payload(payload);

        // Define JWEHeader for encryption via AES-256 GCM and create JWEObject
        JWEHeader header = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM); 

        JWEObject jweObject = new JWEObject(header, jwePayload);

        // Encrypt with secret Key
        jweObject.encrypt(new DirectEncrypter(this.secretKey));

        return jweObject.serialize();
    }

    /**
     * Decrypt a compact JWE string back into its original payload string.
     * Parse incoming token string, bind Direct Decrypter, then extract
     */
    public String decrypt(String encryptedToken) throws Exception {
        JWEObject jweObject = JWEObject.parse(encryptedToken);
        jweObject.decrypt(new DirectDecrypter(this.secretKey));
        return jweObject.getPayload().toString();
    }
}
