package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.entity.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);

    Optional<PostScrap> findByPostIdAndMemberId(Long postId, Long memberId);

    long countByPostId(Long postId);

    @Query("""
            SELECT ps.post.id
            FROM PostScrap ps
            WHERE ps.member.id = :memberId AND ps.post.id IN :postIds
            """)
    List<Long> findScrappedPostIds(@Param("memberId") Long memberId, @Param("postIds") List<Long> postIds);
}
