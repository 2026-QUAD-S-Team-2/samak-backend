package com.oaosis.samak.domain.member.application;

import com.oaosis.samak.domain.member.dto.response.MemberResponse;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.infra.gcs.service.GcsUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final GcsUrlBuilder gcsUrlBuilder;

    public MemberResponse getMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        String profileImageUrl = gcsUrlBuilder.buildImageUrl(member.getProfileImageName());
        return MemberResponse.from(member, profileImageUrl);
    }
}