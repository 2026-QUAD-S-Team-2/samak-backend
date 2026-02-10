package com.oaosis.samak.global.security.oauth2.service.verifier;

import com.oaosis.samak.global.security.oauth2.dto.OAuth2UserInfo;
import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;

public interface OAuth2IdTokenVerifier {

    OAuth2UserInfo verifyAndExtract(String idToken);
    OAuth2Provider getProvider();

    default boolean supports(OAuth2Provider provider) {
        return getProvider().equals(provider);
    }
}