package com.oaosis.samak.domain.member.presentation;

import com.oaosis.samak.domain.member.dto.response.MemberResponse;
import com.oaosis.samak.domain.member.application.MemberService;
import com.oaosis.samak.global.security.entity.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "나의 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<MemberResponse> getMyInfo(@AuthenticationPrincipal AuthenticatedUser user) {
        MemberResponse response = memberService.getMember(user.getEmail());
        return ResponseEntity.ok(response);
    }
}