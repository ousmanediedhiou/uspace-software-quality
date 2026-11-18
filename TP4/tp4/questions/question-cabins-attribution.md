# Scénarios de tests pour l'attribution de cabines

### 1. Élaborer la stratégie d'essais pour la fonctionnalité d'attribution de cabines

Pour faciliter la correction, suivez le gabarit ci-dessous.

- Expliquer pourquoi chaque test est dans sa catégorie.
- Expliquer pourquoi une catégorie est vide si c'est le cas.

**TESTS FONCTIONS:**

*TESTS FONCTIONS – BACKEND*


Attribution par date de réservation, Vérifier que les cabines sont attribuées selon l’ordre de bookingDateTime : il valide une règle métier sans dépendre de l’API, de la BD ou de l’interface.

Attribution par nombre de voyageurs, Vérifier que les cabines sont attribuées selon le nombre de voyageurs puis la date : il teste un comportement métier défini dans les critères d’acceptation.

Croisière inexistante, Vérifier que le service lève une erreur lorsqu’une croisière n’existe pas :  il couvre un cas d’erreur métier attendu sans impliquer l’infrastructure.

Croisière sans réservations, Vérifier que le service retourne une liste vide lorsqu’il n’y a aucune réservation : il valide le comportement métier « aucune cabine attribuée ».

*TESTS FONCTIONS – FRONTEND*

Affichage du tableau de cabines,Vérifier que l’interface affiche un tableau lorsque des cabines sont retournées par l’API mockée : c’est un test fonction car il valide un comportement fonctionnel de l’UI selon les critères d’acceptation.

Aucun tableau si aucune cabine, Vérifier que l’interface n’affiche pas de tableau lorsque l’API retourne une liste vide : c’est un test fonction car il valide le critère métier « aucune cabine attribuée = aucun tableau ».



**TESTS COMPOSANT:**

*TESTS COMPOSANT - FRONTEND*

Il n’y a pas de tests composant frontend, car les tests composant concernent exclusivement l’API backend (JerseyTest). Le frontend est testé avec Vitest ou Cypress, mais n’a pas de concept de “test composant”

*TESTS COMPOSANT - BACKEND*

Réponse 200 avec des cabines attribuées, Vérifier que l’appel à GET /cruises/{cruiseId}/cabins?criteria=bookingDateTime retourne un HTTP 200 avec un JSON contenant la liste cabins conforme à l’énoncé : c’est un test composant qui valide le contrat de la ressource REST (statut + format de réponse) en isolant le service par un mock.

Erreur 404 CRUISE_NOT_FOUND, Vérifier que lorsque le service lève une CruiseNotFoundException, la route retourne un HTTP 404 avec error = "CRUISE_NOT_FOUND" et la bonne description : c’est un test composant qui vérifie le comportement de la ressource et de l’exception mapper du point de vue HTTP.

Erreur 400 INVALID_CABIN_ATTRIBUTION_CRITERIA, Vérifier que lorsque le service signale un critère d’attribution invalide, la route retourne un HTTP 400 avec error = "INVALID_CABIN_ATTRIBUTION_CRITERIA" et la bonne description : c’est un test composant qui teste la gestion des erreurs métier par l’API sans exécuter la logique interne.

**TESTS MULTI-COMPOSANTS (Cucumber):**
*TESTS MULTI-COMPOSANTS - FRONTEND*

Ici, le test multi-composant fait intervenir plusieurs couches techniques du backend qui collaborent :

Jetty / Jersey (serveur + ressource REST),
services métiers,
repositories Hibernate,
base de données H2 en mémoire.

Si on faisait intervenir aussi le frontend (navigateur + SPA + backend + DB), là on serait dans du E2E (bout-en-bout), pas seulement du multi-composant backend.

*TESTS MULTI-COMPOSANTS - BACKEND*

Cabines attribuées avec un critère valide, Vérifier que GET /cruises/{cruiseId}/cabins?criteria=bookingDateTime retourne un code HTTP 200 et qu’au moins une cabine est attribuée : il fait collaborer plusieurs composants réels du backend : serveur Jetty, ressource REST, service métier d’attribution, repositories Hibernate et base H2. Aucune couche n’est mockée, l’appel HTTP traverse toute la stack backend.

Critère d’attribution invalide, Vérifier qu’un appel à GET /cruises/{cruiseId}/cabins?criteria=invalidCriteria retourne un code HTTP 400 et une erreur INVALID_CABIN_ATTRIBUTION_CRITERIA : il exécute la vraie pile backend : API REST, validation métier, exception métier, ExceptionMapper, sérialisation JSON, et serveur Jetty. Le test ne mocke rien et valide la collaboration de ces composants via une requête HTTP réelle.

**TESTS END-TO-END (Cypress):**

*TESTS END-TO-END - FRONTEND* 

Vérifier que lorsqu’on clique sur Get Cabin Attributions et que le backend retourne des cabines, celles-ci s’affichent dans le tableau : Parce qu’il exécute le vrai frontend dans un navigateur, appelle le vrai backend sans mock et valide le résultat final rendu dans l’interface.

Vérifier que lorsqu’on clique sur Get Cabin Attributions et qu’aucune cabine n’est attribuée, le message Aucune cabine n’a encore été attribuée est affiché : Parce qu’il teste l’application complète du navigateur au backend réel, en validant l’affichage final du frontend.