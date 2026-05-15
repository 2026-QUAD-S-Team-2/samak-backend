package com.oaosis.samak.domain.board.entity;

import com.oaosis.samak.domain.analysis.entity.AnalysisItem;
import com.oaosis.samak.domain.board.entity.enums.PostCategory;
import com.oaosis.samak.domain.member.entity.Member;
import com.oaosis.samak.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PostCategory category;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_item_id")
    private AnalysisItem analysisItem;

    @Builder
    public Post(Member author, PostCategory category, String title, String content, AnalysisItem analysisItem) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.content = content;
        this.analysisItem = analysisItem;
    }
}
