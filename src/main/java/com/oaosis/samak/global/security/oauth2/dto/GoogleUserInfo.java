package com.oaosis.samak.global.security.oauth2.dto;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;
import lombok.Getter;

/**
 * Google OAuth2 사용자 정보
 */
@Getter
public class GoogleUserInfo extends OAuth2UserInfo {

    private final String email;
    private final String providerId;
    private final String name;
    private final String imageUrl;

    public GoogleUserInfo(String email, String providerId, String name, String imageUrl) {
        super(OAuth2Provider.GOOGLE);
        this.email = email;
        this.providerId = providerId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static GoogleUserInfo of(GoogleIdToken.Payload payload) {
        String providerId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        return new GoogleUserInfo(email, providerId, name, pictureUrl);
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }
}