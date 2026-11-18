#language: fr

Fonctionnalité: Règles de réservation d'activités sportives

  Contexte:
    Étant donné que le serveur USpace est démarré
    Et qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut

  Scénario: Adulte réservant un sport compatible
    Quand je réserve le sport "Lunar Soccer" pour l'adulte
    Alors la réponse a le statut 200

  Scénario: Senior réservant deux sports compatibles
    Quand je réserve le sport "Lunar Soccer" pour le senior
    Et je réserve le sport "Galactic Tennis" pour le senior
    Alors la réponse a le statut 200

  Scénario: Enfant réservant un sport non compatible
    Quand je réserve le sport "Zero-G Boxing" pour l'enfant
    Alors la réponse a le statut 400
    Et le corps JSON contient l'erreur "SPORT_NOT_BOOKABLE" et la description "Sport not bookable for traveler"

  Scénario: Voyageur réservant deux fois le même sport
    Quand je réserve une première fois le sport "Lunar Soccer" pour l'adulte
    Et je réserve une deuxième fois le sport "Lunar Soccer" pour l'adulte
    Alors la réponse a le statut 400
    Et le corps JSON contient l'erreur "SPORT_ALREADY_BOOKED" et la description "Sport already booked by traveler"
