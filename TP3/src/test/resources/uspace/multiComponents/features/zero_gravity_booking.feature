#language: fr

Fonctionnalité: Réservation d'une expérience zéro gravité

  Contexte:
    Étant donné que le serveur USpace est démarré

  Scénario: Réservation réussie d'une expérience zéro gravité pour un adulte
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour l'adulte à la date "2085-01-01T10:00"
    Alors la réponse HTTP pour zéro gravité est 200

  Scénario: Réservation réussie d'une expérience zéro gravité pour un senior
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour le senior à la date "2085-01-01T10:00"
    Alors la réponse HTTP pour zéro gravité est 200

  Scénario: Réservation réussie d'une expérience zéro gravité pour un enfant accompagné
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour l'adulte à la date "2085-01-01T10:00"
    Et je réserve une expérience zéro gravité pour l'enfant à la date "2085-01-01T11:00"
    Alors la réponse HTTP pour zéro gravité est 200

  Scénario: Échec de réservation zéro gravité pour un enfant non accompagné
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour l'enfant à la date "2085-01-01T10:00"
    Alors la réponse HTTP pour zéro gravité est 400
    Et le code d'erreur zéro gravité est "ZERO_GRAVITY_EXPERIENCE_CHILD_CRITERIA"

  Scénario: Échec de réservation zéro gravité dans une réservation inexistante
    Étant donné qu'aucune réservation n'existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour un voyageur dans une réservation inexistante
    Alors la réponse HTTP pour zéro gravité est 404
    Et le code d'erreur zéro gravité est "TRAVELER_NOT_FOUND"

  Scénario: Échec de réservation zéro gravité pour un voyageur inexistant dans la réservation
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour un voyageur inexistant de cette réservation
    Alors la réponse HTTP pour zéro gravité est 404
    Et le code d'erreur zéro gravité est "TRAVELER_NOT_FOUND"

  Scénario: Échec de réservation zéro gravité après le départ de la croisière
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour l'adulte à une date après le départ de la croisière
    Alors la réponse HTTP pour zéro gravité est 400
    Et le code d'erreur zéro gravité est "ZERO_GRAVITY_EXPERIENCE_BOOKING_TIME"

  Scénario: Échec de réservation zéro gravité déjà réservée pour le même adulte
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand je réserve une expérience zéro gravité pour l'adulte à la date "2085-01-01T10:00"
    Et je réserve à nouveau une expérience zéro gravité pour le même adulte à la date "2085-01-01T11:00"
    Alors la réponse HTTP pour zéro gravité est 400
    Et le code d'erreur zéro gravité est "ZERO_GRAVITY_EXPERIENCE_ALREADY_BOOKED"

  Scénario: Échec de réservation zéro gravité lorsque la capacité maximale est atteinte
    Étant donné que la capacité maximale de l'expérience zéro gravité est atteinte pour la croisière par défaut
    Quand je réserve une expérience zéro gravité pour l'adulte à la date "2085-01-01T10:00"
    Alors la réponse HTTP pour zéro gravité est 400
    Et le code d'erreur zéro gravité est "ZERO_GRAVITY_EXPERIENCE_FULL"

  Scénario: Requête invalide sans champ obligatoire
    Étant donné qu'une réservation avec un adulte, un senior et un enfant existe pour la croisière par défaut pour les tests zéro gravité
    Quand j'envoie une requête POST zéro gravité sans champ obligatoire pour l'adulte
    Alors la réponse HTTP pour zéro gravité est 400
    Et le code d'erreur zéro gravité est "MISSING_PARAMETER" et la description "Missing parameter: experienceBookingDateTime"
