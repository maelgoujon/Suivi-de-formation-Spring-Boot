package com.webapp.ytb.webappytp.service;

import com.webapp.ytb.webappytp.modele.Formation;
import java.util.List;

public interface FormationService {
    Formation creer(Formation formation);

    List<Formation> lire();

    Formation modifier(Long id, Formation formation);

    String supprimer(Long id);

    Formation findById(Long id);
}
