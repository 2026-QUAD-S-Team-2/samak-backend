package com.oaosis.samak.global.security.oauth2.service;

import com.oaosis.samak.global.security.oauth2.dto.OAuth2UserInfo;
import com.oaosis.samak.global.security.oauth2.entity.enums.OAuth2Provider;
import com.oaosis.samak.global.security.oauth2.service.verifier.OAuth2IdTokenVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OAuth2AuthenticationService {

    private final Map<OAuth2Provider, OAuth2IdTokenVerifier> verifiers;

    public OAuth2AuthenticationService(List<OAuth2IdTokenVerifier> verifierList) {
        this.verifiers = verifierList.stream()
                .collect(Collectors.toMap(
                        OAuth2IdTokenVerifier::getProvider,
                        Function.identity()
                ));
    }

    public OAuth2UserInfo verifyIdToken(OAuth2Provider provider, String idToken) {
        OAuth2IdTokenVerifier verifier = verifiers.get(provider);
        return verifier.verifyAndExtract(idToken);
    }
}