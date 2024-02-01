# Projet Spring Boot Java avec Thymeleaf - Suivi de formation

## Description du Projet

Le projet vise à créer une application web pour une association accompagnant des handicapés.

## Fonctionnalités implémentées:
Personnalisation des éléments des fiches (icone, texte, couleur et police)
Évaluation du travail par le formatteur et l'apprenti
Canal de discussion (messages : texte, audio et images)
Connexion personnelle avec mot de passe
Gestion des utilisateurs (creation, modification, suppression et archivage)
Evolution des élements des fiches (ajout de nouveaux materiaux/images, modification des ancien(ne)s et suppression)

## Objectifs du Projet

1. **Personnalisation Visuelle des Fiches d'Intervention**
   - Niveau 1 : Pas d'affichage
   - Niveau 2 : Facile (Icone)
   - Niveau 3 : Aidé (Icone + Texte)
   - Niveau 4 : Pro (Version finale à l'examen)

2. **Système d'Évaluation du Travail Réalisé**
   - Éducateurs techniques peuvent laisser une trace d'évaluation formative (textuelle ou audio).
   - Possibilité pour les jeunes de saisir une trace liée à leur ressenti.

3. **Système d'Authentification**
   - Authentification des jeunes avec choix de profil et mot de passe.
   - Authentification sécurisée pour les éducateurs techniques.
   - Super-administrateur peut gérer les comptes utilisateurs.

4. **Système d'Analyse de la Progression (Optionnel)**
   - Historique des modifications de l'accessibilité pour chaque jeune.
   - Analyse temporelle de l'évolution de l'interface.

5. **Utilisation Hors Ligne de l'Application Web (Optionnel)**
   - Accès hors ligne à certaines pages.
   - Mise à jour des données locales sur le serveur central une fois connecté.

## Prérequis

- Java Development Kit (JDK 17)
- Maven
- Base de données (config dans application.properties).

## Installation et Exécution

1. Cloner le dépôt : `git clone https://github.com/maelgoujon/sae_apeaj.git`
2. Compiler avec maven : `mvn clean package`
3. Executer le war : `java -jar webapp.war`
4. Application visible sur : `http://localhost:8081`

## Contributeurs

- https://github.com/maelgoujon / https://github.com/goujonmael
- https://github.com/Cemailla
- https://github.com/dardeeet
- https://github.com/TanyXxx
