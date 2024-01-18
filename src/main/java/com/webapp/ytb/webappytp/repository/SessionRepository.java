package com.webapp.ytb.webappytp.repository;

import com.webapp.ytb.webappytp.modele.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}