package com.oaosis.samak.global.security.oauth2.service.verifier;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.oaosis.samak.global.security.oauth2.dto.GoogleUserInfo;
import com.oaosis.samak.global.security.oauth2.dto.OAuth2UserInfo;
import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;
import com.oaosis.samak.domain.auth.exception.AuthErrorCode;
import com.oaosis.samak.domain.auth.exception.CustomAuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Slf4j
@Component
public class GoogleIdTokenVerifierImpl implements OAuth2IdTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleIdTokenVerifierImpl(@Value("${google.oauth.client-id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Override
    public OAuth2UserInfo verifyAndExtract(String idToken) {
        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new CustomAuthException(AuthErrorCode.INVALID_TOKEN);
            }
            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            return GoogleUserInfo.of(payload);
        } catch (CustomAuthException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomAuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }
}