package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.entity.Post;
import com.oaosis.samak.domain.board.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findAllByPost(Post post);
}
