package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.entity.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    Optional<PostScrap> findByPostIdAndMemberId(Long postId, Long memberId);
}
