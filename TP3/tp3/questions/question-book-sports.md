# Questions: Réserver une activité sportive

### 1. Élaborer la stratégie d'essais pour la fonctionnalité de réservation d'une activité sportive.

Pour faciliter la correction, suivez le gabarit ci-dessous.

**TESTS FONCTIONS:**

Réserver un sport compatible pour un adulte: On teste uniquement la méthode bookSport d’un AdultTraveler, sans dépendance extérieure.

Sport déjà réservé par un adulte: On teste que bookSport lève SportAlreadyBookedException sans dépendance extérieure.

Enfant réservant un sport non compatible: On teste uniquement la logique de compatibilité dans ChildTraveler.

Senior réservant un deuxième sport compatible: On teste la logique interne d’attribution du badge SUPER_SPORTY.

Booking avec voyageur inexistant: On teste la méthode bookSport de Booking isolément.

Service croisière inexistante: On teste TravelerService avec des mocks pour vérifier qu’il lève CruiseNotFoundException.

Service sport inexistant: On teste TravelerService avec des mocks pour vérifier qu’il lève SportNotFoundException.


**TESTS COMPOSANT:**

POST /sports – succès : On teste la ressource REST et son comportement HTTP avec le service mocké.

POST /sports – champ sport manquant : On teste la ressource + validateur + mapper (sans domaine).

POST /sports – croisière inexistante : On teste la traduction de l’exception du service vers la réponse HTTP.

POST /sports – sport inexistant : On teste uniquement le mapping HTTP (SPORT_NOT_FOUND).

POST /sports – sport non réservable : On teste le mapper REST pour SPORT_NOT_BOOKABLE.

POST /sports – sport déjà réservé : On teste le mapper REST pour SPORT_ALREADY_BOOKED.



**TESTS MULTI-COMPOSANTS (Cucumber):**

Adulte réservant un sport compatible : On teste l’intégration API + service + domaine + JSON + repo mémoire.

Senior réservant deux sports compatibles : On valide toute la chaîne jusqu’aux badges SPORTY puis SUPER_SPORTY.

Enfant réservant un sport non compatible : On teste l’intégration complète menant à SPORT_NOT_BOOKABLE.

Voyageur réservant deux fois le même sport : On teste que toute la chaîne conduit à SPORT_ALREADY_BOOKED.

Réserver un sport sans fournir le champ sport: On teste toute la chaîne API + validation de requête + resource + exception mapper en envoyant une requête POST /sports sans champ "sport". Le test vérifie que l’API retourne HTTP 400 avec l’erreur JSON "MISSING_PARAMETER" et la description "Missing parameter: sport".

Réserver un sport pour une croisière inexistante: On teste l’intégration complète API + service + domaine + repo en envoyant une requête POST /sports avec un cruiseId inexistant. Le test vérifie que l’API retourne HTTP 404 avec l’erreur JSON "CRUISE_NOT_FOUND" et la description "Cruise not found".
