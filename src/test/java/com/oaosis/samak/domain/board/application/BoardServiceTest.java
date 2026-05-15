package com.oaosis.samak.domain.board.application;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.analysis.enums.AnalysisStatus;
import com.oaosis.samak.domain.analysis.repository.AnalysisItemRepository;
import com.oaosis.samak.domain.board.dto.request.PostCreateRequest;
import com.oaosis.samak.domain.board.dto.response.PostDetailResponse;
import com.oaosis.samak.domain.board.entity.Post;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import com.oaosis.samak.domain.board.exception.BoardErrorCode;
import com.oaosis.samak.domain.board.exception.BoardException;
import com.oaosis.samak.domain.board.repository.CommentRepository;
import com.oaosis.samak.domain.board.repository.FraudVoteRepository;
import com.oaosis.samak.domain.board.repository.PostImageRepository;
import com.oaosis.samak.domain.board.repository.PostLikeRepository;
import com.oaosis.samak.domain.board.repository.PostRepository;
import com.oaosis.samak.domain.board.repository.PostScrapRepository;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.domain.member.repository.MemberRepository;
import com.oaosis.samak.infra.gcs.service.GcsUrlBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock PostRepository postRepository;
    @Mock PostImageRepository postImageRepository;
    @Mock PostLikeRepository postLikeRepository;
    @Mock CommentRepository commentRepository;
    @Mock FraudVoteRepository fraudVoteRepository;
    @Mock PostScrapRepository postScrapRepository;
    @Mock MemberRepository memberRepository;
    @Mock AnalysisItemRepository analysisItemRepository;
    @Mock GcsUrlBuilder gcsUrlBuilder;

    @InjectMocks BoardService boardService;

    @Test
    void createPost_aiAnalysis_withCompletedItem_savesPostSuccessfully() {
        Member member = mock(Member.class);
        AnalysisItem item = mock(AnalysisItem.class);
        when(item.getStatus()).thenReturn(AnalysisStatus.COMPLETED);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));
        when(analysisItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        PostCreateRequest request = new PostCreateRequest(
                PostCategory.AI_ANALYSIS, "AI 분석 공유", "이 공고 위험합니다", null, 1L
        );

        boardService.createPost("test@example.com", request);

        verify(analysisItemRepository).findById(1L);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void createPost_aiAnalysis_withPendingItem_throwsAnalysisItemNotCompleted() {
        Member member = mock(Member.class);
        AnalysisItem item = mock(AnalysisItem.class);
        when(item.getStatus()).thenReturn(AnalysisStatus.PENDING);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));
        when(analysisItemRepository.findById(1L)).thenReturn(Optional.of(item));

        PostCreateRequest request = new PostCreateRequest(
                PostCategory.AI_ANALYSIS, "제목", "내용", null, 1L
        );

        assertThatThrownBy(() -> boardService.createPost("test@example.com", request))
                .isInstanceOf(BoardException.class)
                .satisfies(ex -> assertThat(((BoardException) ex).getErrorCode())
                        .isEqualTo(BoardErrorCode.ANALYSIS_ITEM_NOT_COMPLETED));
    }

    @Test
    void createPost_aiAnalysis_withoutItemId_throwsAnalysisItemRequired() {
        Member member = mock(Member.class);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));

        PostCreateRequest request = new PostCreateRequest(
                PostCategory.AI_ANALYSIS, "제목", "내용", null, null
        );

        assertThatThrownBy(() -> boardService.createPost("test@example.com", request))
                .isInstanceOf(BoardException.class)
                .satisfies(ex -> assertThat(((BoardException) ex).getErrorCode())
                        .isEqualTo(BoardErrorCode.ANALYSIS_ITEM_REQUIRED));
    }

    @Test
    void createPost_aiAnalysis_withNonExistentItemId_throwsAnalysisItemNotFound() {
        Member member = mock(Member.class);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));
        when(analysisItemRepository.findById(999L)).thenReturn(Optional.empty());

        PostCreateRequest request = new PostCreateRequest(
                PostCategory.AI_ANALYSIS, "제목", "내용", null, 999L
        );

        assertThatThrownBy(() -> boardService.createPost("test@example.com", request))
                .isInstanceOf(BoardException.class)
                .satisfies(ex -> assertThat(((BoardException) ex).getErrorCode())
                        .isEqualTo(BoardErrorCode.ANALYSIS_ITEM_NOT_FOUND));
    }

    @Test
    void createPost_nonAiAnalysisCategory_withItemId_throwsAnalysisItemNotAllowed() {
        Member member = mock(Member.class);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));

        PostCreateRequest request = new PostCreateRequest(
                PostCategory.EXPERIENCE, "제목", "내용", null, 1L
        );

        assertThatThrownBy(() -> boardService.createPost("test@example.com", request))
                .isInstanceOf(BoardException.class)
                .satisfies(ex -> assertThat(((BoardException) ex).getErrorCode())
                        .isEqualTo(BoardErrorCode.ANALYSIS_ITEM_NOT_ALLOWED));
    }

    @Test
    void createPost_experience_withoutItemId_savesPostSuccessfully() {
        Member member = mock(Member.class);
        when(memberRepository.findByEmail("test@example.com")).thenReturn(Optional.of(member));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        PostCreateRequest request = new PostCreateRequest(
                PostCategory.EXPERIENCE, "피해 경험담", "급여 미지급", List.of("img.jpg"), null
        );

        boardService.createPost("test@example.com", request);

        verify(analysisItemRepository, never()).findById(any());
        verify(postRepository).save(any(Post.class));
        verify(postImageRepository).saveAll(any());
    }

    @Test
    void getPostDetail_aiAnalysisPost_includesAnalysisItemId() {
        AnalysisItem item = mock(AnalysisItem.class);
        when(item.getId()).thenReturn(5L);

        Member member = mock(Member.class);
        when(member.getNickname()).thenReturn("홍길동");

        Post post = mock(Post.class);
        when(post.getCategory()).thenReturn(PostCategory.AI_ANALYSIS);
        when(post.getAnalysisItem()).thenReturn(item);
        when(post.getAuthor()).thenReturn(member);

        when(postRepository.findByIdWithAuthor(1L)).thenReturn(Optional.of(post));
        when(postLikeRepository.countByPostId(1L)).thenReturn(3L);
        when(commentRepository.countByPostId(1L)).thenReturn(2L);
        when(postImageRepository.findAllByPost(post)).thenReturn(List.of());

        PostDetailResponse response = boardService.getPostDetail(1L);

        assertThat(response.analysisItemId()).isEqualTo(5L);
    }

    @Test
    void getPostDetail_experiencePost_hasNullAnalysisItemId() {
        Member member = mock(Member.class);
        when(member.getNickname()).thenReturn("홍길동");

        Post post = mock(Post.class);
        when(post.getCategory()).thenReturn(PostCategory.EXPERIENCE);
        when(post.getAnalysisItem()).thenReturn(null);
        when(post.getAuthor()).thenReturn(member);

        when(postRepository.findByIdWithAuthor(1L)).thenReturn(Optional.of(post));
        when(postLikeRepository.countByPostId(1L)).thenReturn(0L);
        when(commentRepository.countByPostId(1L)).thenReturn(0L);
        when(postImageRepository.findAllByPost(post)).thenReturn(List.of());

        PostDetailResponse response = boardService.getPostDetail(1L);

        assertThat(response.analysisItemId()).isNull();
    }
}
