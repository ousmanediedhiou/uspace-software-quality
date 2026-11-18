#language: fr

Fonctionnalité: Obtention du manifeste des navettes d'urgence

  Scénario: Réponse 200 avec l'identifiant d'une croisière existante
    Quand on demande le manifeste des navettes d'urgences pour la croisière "JUPITER_MOONS_EXPLORATION_2085"
    Alors le code de statut 200 est obtenu
    
  Scénario: Croisière inexistante
    Quand on demande le manifeste des navettes d'urgences pour la croisière "CROISIERE-INEXISTANTE"
    Alors le code de statut 404 est obtenu
    Et l'erreur "CRUISE_NOT_FOUND" est obtenue
    