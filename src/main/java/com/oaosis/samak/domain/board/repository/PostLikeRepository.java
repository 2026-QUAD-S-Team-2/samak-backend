package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    long countByPostId(Long postId);
}
