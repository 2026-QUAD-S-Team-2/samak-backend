package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByPostId(Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.post.id = :postId AND c.parentComment IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findTopLevelCommentsByPostId(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.parentComment.id IN :parentIds ORDER BY c.createdAt ASC")
    List<Comment> findRepliesByParentIdIn(@Param("parentIds") List<Long> parentIds);
}
