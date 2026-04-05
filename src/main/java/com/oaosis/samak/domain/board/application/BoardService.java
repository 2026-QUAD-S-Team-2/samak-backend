package com.oaosis.samak.domain.board.application;

import com.oaosis.samak.domain.board.dto.request.FraudVoteRequest;
import com.oaosis.samak.domain.board.dto.request.PostCreateRequest;
import com.oaosis.samak.domain.board.dto.response.FraudVoteResponse;
import com.oaosis.samak.domain.board.dto.response.PostCreateResponse;
import com.oaosis.samak.domain.board.dto.response.PostDetailResponse;
import com.oaosis.samak.domain.board.dto.response.PostListResponse;
import com.oaosis.samak.domain.board.entity.FraudVote;
import com.oaosis.samak.domain.board.entity.Post;
import com.oaosis.samak.domain.board.entity.PostImage;
import com.oaosis.samak.domain.board.entity.PostScrap;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import com.oaosis.samak.domain.board.entity.enums.VoteType;
import com.oaosis.samak.global.response.CursorResponse;
import com.oaosis.samak.domain.board.exception.BoardErrorCode;
import com.oaosis.samak.domain.board.exception.BoardException;
import com.oaosis.samak.domain.board.repository.CommentRepository;
import com.oaosis.samak.domain.board.repository.FraudVoteRepository;
import com.oaosis.samak.domain.board.repository.PostImageRepository;
import com.oaosis.samak.domain.board.repository.PostLikeRepository;
import com.oaosis.samak.domain.board.repository.PostRepository;
import com.oaosis.samak.domain.board.repository.PostScrapRepository;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.exception.MemberErrorCode;
import com.oaosis.samak.domain.member.exception.MemberException;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.infra.s3.service.S3UrlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final FraudVoteRepository fraudVoteRepository;
    private final PostScrapRepository postScrapRepository;
    private final MemberRepository memberRepository;
    private final S3UrlBuilder s3UrlBuilder;

    @Transactional
    public PostCreateResponse createPost(String email, PostCreateRequest request) {
        Member author = getMember(email);

        Post post = Post.builder()
                .author(author)
                .category(request.category())
                .title(request.title())
                .content(request.content())
                .build();
        postRepository.save(post);

        if (request.imageNames() != null && !request.imageNames().isEmpty()) {
            List<PostImage> images = request.imageNames().stream()
                    .map(name -> PostImage.builder()
                            .post(post)
                            .imageName(name)
                            .build())
                    .toList();
            postImageRepository.saveAll(images);
        }

        return PostCreateResponse.from(post);
    }

    public CursorResponse<PostListResponse> getPostList(PostCategory category, Long cursor, int size) {
        PageRequest pageable = PageRequest.of(0, size + 1);

        List<PostListResponse> items;
        if (category == null) {
            items = cursor == null
                    ? postRepository.findPostsFirst(pageable)
                    : postRepository.findPostsAfterCursor(cursor, pageable);
        } else {
            items = cursor == null
                    ? postRepository.findPostsByCategoryFirst(category, pageable)
                    : postRepository.findPostsByCategoryAfterCursor(category, cursor, pageable);
        }

        return CursorResponse.of(items, size, PostListResponse::id);
    }

    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.POST_NOT_FOUND));

        long likeCount = postLikeRepository.countByPostId(postId);
        long commentCount = commentRepository.countByPostId(postId);
        List<String> imageUrls = postImageRepository.findAllByPost(post).stream()
                .map(img -> s3UrlBuilder.buildImageUrl(img.getImageName()))
                .toList();

        return PostDetailResponse.of(post, likeCount, commentCount, imageUrls);
    }

    @Transactional
    public void castFraudVote(Long postId, String email, FraudVoteRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.POST_NOT_FOUND));

        if (post.getCategory() != PostCategory.FRAUD_VOTE) {
            throw new BoardException(BoardErrorCode.INVALID_VOTE_CATEGORY);
        }

        Member voter = getMember(email);

        if (fraudVoteRepository.existsByPostIdAndVoterId(postId, voter.getId())) {
            throw new BoardException(BoardErrorCode.DUPLICATE_VOTE);
        }

        fraudVoteRepository.save(FraudVote.builder()
                .post(post)
                .voter(voter)
                .voteType(request.voteType())
                .build());
    }

    public FraudVoteResponse getFraudVoteResult(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new BoardException(BoardErrorCode.POST_NOT_FOUND);
        }

        long fraudCount = fraudVoteRepository.countByPostIdAndVoteType(postId, VoteType.FRAUD);
        long notFraudCount = fraudVoteRepository.countByPostIdAndVoteType(postId, VoteType.NOT_FRAUD);

        return FraudVoteResponse.of(postId, fraudCount, notFraudCount);
    }

    @Transactional
    public void addScrap(Long postId, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BoardException(BoardErrorCode.POST_NOT_FOUND));

        Member member = getMember(email);

        if (postScrapRepository.existsByPostIdAndMemberId(postId, member.getId())) {
            throw new BoardException(BoardErrorCode.DUPLICATE_SCRAP);
        }

        postScrapRepository.save(PostScrap.builder()
                .post(post)
                .member(member)
                .build());
    }

    @Transactional
    public void removeScrap(Long postId, String email) {
        Member member = getMember(email);

        PostScrap scrap = postScrapRepository.findByPostIdAndMemberId(postId, member.getId())
                .orElseThrow(() -> new BoardException(BoardErrorCode.SCRAP_NOT_FOUND));

        postScrapRepository.delete(scrap);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
