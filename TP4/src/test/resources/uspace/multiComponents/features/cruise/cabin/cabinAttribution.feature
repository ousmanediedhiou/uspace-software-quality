#language: fr

Fonctionnalité: Attribution des cabines pour une croisière

  Scénario: Réponse 200 avec un critère valide
    Quand on demande l'attribution des cabines avec le critère "bookingDateTime"
    Alors le code de statut 200 est obtenu

  Scénario: Critère d'attribution invalide
    Quand on demande l'attribution des cabines avec le critère "invalidCriteria"
    Alors le code de statut 400 est obtenu
    Et l'erreur "INVALID_CABIN_ATTRIBUTION_CRITERIA" est obtenue
