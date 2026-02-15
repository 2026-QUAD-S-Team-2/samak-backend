package com.oaosis.samak.global.security.oauth2.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.oaosis.samak.global.security.oauth2.dto.GoogleUserInfo;
import com.oaosis.samak.domain.auth.exception.AuthErrorCode;
import com.oaosis.samak.domain.auth.exception.CustomAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class GoogleAuthService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleAuthService(@Value("${google.oauth.client-id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleUserInfo verifyIdToken(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new CustomAuthException(AuthErrorCode.TOKEN_NOT_FOUND);
            }
            
            GoogleIdToken.Payload payload = idToken.getPayload();
            return GoogleUserInfo.of(payload);

        } catch (Exception e) {
            throw new CustomAuthException(AuthErrorCode.OAUTH_AUTHENTICATION_FAILED);
        }
    }
}