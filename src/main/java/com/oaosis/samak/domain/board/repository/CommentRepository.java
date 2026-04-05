package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByPostId(Long postId);
}
