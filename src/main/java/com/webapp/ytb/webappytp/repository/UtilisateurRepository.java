package com.webapp.ytb.webappytp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.webapp.ytb.webappytp.repository.UtilisateurRepository;

import com.webapp.ytb.webappytp.modele.UserRole;
import com.webapp.ytb.webappytp.modele.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT e FROM Utilisateur e WHERE e.login = :login")
    Utilisateur findUserByLogin(@Param("login") String login);

    @Query("SELECT MAX(u.id) FROM Utilisateur u")
    Long findMaxId();

    List<Utilisateur> findByRole(UserRole role);

    List<Utilisateur> findByFormation_Id(Long formationId);
}
