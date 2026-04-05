package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.dto.response.PostListResponse;
import com.oaosis.samak.domain.board.entity.Post;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
    Optional<Post> findByIdWithAuthor(@Param("id") Long id);

    @Query("""
            SELECT new com.oaosis.samak.domain.board.dto.response.PostListResponse(
                p.id, p.category, p.title,
                (SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = p),
                (SELECT COUNT(c) FROM Comment c WHERE c.post = p),
                p.createdAt)
            FROM Post p
            ORDER BY p.id DESC
            """)
    List<PostListResponse> findPostsFirst(Pageable pageable);

    @Query("""
            SELECT new com.oaosis.samak.domain.board.dto.response.PostListResponse(
                p.id, p.category, p.title,
                (SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = p),
                (SELECT COUNT(c) FROM Comment c WHERE c.post = p),
                p.createdAt)
            FROM Post p
            WHERE p.id < :cursorId
            ORDER BY p.id DESC
            """)
    List<PostListResponse> findPostsAfterCursor(@Param("cursorId") Long cursorId, Pageable pageable);

    @Query("""
            SELECT new com.oaosis.samak.domain.board.dto.response.PostListResponse(
                p.id, p.category, p.title,
                (SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = p),
                (SELECT COUNT(c) FROM Comment c WHERE c.post = p),
                p.createdAt)
            FROM Post p
            WHERE p.category = :category
            ORDER BY p.id DESC
            """)
    List<PostListResponse> findPostsByCategoryFirst(@Param("category") PostCategory category, Pageable pageable);

    @Query("""
            SELECT new com.oaosis.samak.domain.board.dto.response.PostListResponse(
                p.id, p.category, p.title,
                (SELECT COUNT(pl) FROM PostLike pl WHERE pl.post = p),
                (SELECT COUNT(c) FROM Comment c WHERE c.post = p),
                p.createdAt)
            FROM Post p
            WHERE p.category = :category AND p.id < :cursorId
            ORDER BY p.id DESC
            """)
    List<PostListResponse> findPostsByCategoryAfterCursor(@Param("category") PostCategory category, @Param("cursorId") Long cursorId, Pageable pageable);
}
