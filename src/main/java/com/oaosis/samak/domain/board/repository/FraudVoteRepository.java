package com.oaosis.samak.domain.board.repository;

import com.oaosis.samak.domain.board.entity.FraudVote;
import com.oaosis.samak.domain.board.entity.enums.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FraudVoteRepository extends JpaRepository<FraudVote, Long> {

    boolean existsByPostIdAndVoterId(Long postId, Long voterId);

    long countByPostIdAndVoteType(Long postId, VoteType voteType);
}
