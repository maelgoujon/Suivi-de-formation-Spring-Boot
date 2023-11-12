package com.webapp.ytb.webappytp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.webapp.ytb.webappytp.modele.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
    
    @Query("SELECT e FROM Utilisateur e WHERE e.login = :login")
    Utilisateur findUserByLogin(@Param("login") String login);

}
