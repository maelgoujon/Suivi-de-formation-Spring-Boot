package com.webapp.ytb.webappytp.service;

import com.webapp.ytb.webappytp.modele.Session;
import com.webapp.ytb.webappytp.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session creer(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> lire() {
        return sessionRepository.findAll();
    }
}