package com.webapp.ytb.webappytp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.ytb.webappytp.modele.Message;

import jakarta.transaction.Transactional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional
    List<Message> findAllByOrderByTimestampAsc();

    List<Message> findAllByFicheInterventionIdOrderByTimestampAsc(long id);



}