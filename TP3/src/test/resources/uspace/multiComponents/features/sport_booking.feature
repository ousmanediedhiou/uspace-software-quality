#language: fr

Fonctionnalité: Réserver une activité sportive

  Contexte:
    Étant donné que le serveur USpace est démarré

  Scénario: Erreur : tentative de réserver un sport sans fournir le champ requis
    Quand j'envoie une requête POST pour réserver un sport sans champ sport
    Alors la réponse a le statut 400
    Et le corps JSON contient l'erreur "MISSING_PARAMETER" et la description "Missing parameter: sport"

  Scénario: Erreur : tentative de réserver un sport pour une croisière inexistante
    Quand j'envoie une requête POST pour réserver le sport "Lunar Soccer" pour une croisière inexistante
    Alors la réponse a le statut 404
    Et le corps JSON contient l'erreur "CRUISE_NOT_FOUND" et la description "Cruise not found"
