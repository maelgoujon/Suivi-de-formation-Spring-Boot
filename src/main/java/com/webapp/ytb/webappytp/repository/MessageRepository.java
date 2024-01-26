package com.webapp.ytb.webappytp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.ytb.webappytp.modele.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {



}