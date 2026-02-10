package com.oaosis.samak.global.security.oauth2.dto;

import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;

public abstract class OAuth2UserInfo {

    protected final OAuth2Provider provider;

    protected OAuth2UserInfo(OAuth2Provider provider) {
        this.provider = provider;
    }

    public abstract String getProviderId();

    public abstract String getEmail();

    public abstract String getName();

    public abstract String getImageUrl();

    public OAuth2Provider getProvider() {
        return provider;
    }
}