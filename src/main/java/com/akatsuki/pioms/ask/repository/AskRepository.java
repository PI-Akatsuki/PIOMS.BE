package com.akatsuki.pioms.ask.repository;

import com.akatsuki.pioms.ask.aggregate.Ask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AskRepository extends JpaRepository<Ask, Integer> {
    @Query("SELECT a FROM Ask a WHERE a.askStatus = '답변대기'")
    List<Ask> findAllByStatusWaitingForReply();

    @Query("SELECT a FROM Ask a ORDER BY a.askEnrollDate DESC")
    List<Ask> findAllOrderByEnrollDateDesc();

    @Query("SELECT a FROM Ask a WHERE a.franchiseOwner.franchiseOwnerCode = :franchiseOwnerId ORDER BY a.askEnrollDate DESC")
    List<Ask> findByFranchiseOwner_FranchiseOwnerCodeOrderByAskEnrollDateDesc(Integer franchiseOwnerId);
}

