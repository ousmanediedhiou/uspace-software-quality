# Scénarios de tests pour obtenir le manifeste de navettes d'urgence

### 1. Élaborer la stratégie d'essais pour la fonctionnalité d'obtention du manifeste de navettes d'urgence

Pour faciliter la correction, suivez le gabarit ci-dessous.

- Expliquer pourquoi chaque test est dans sa catégorie.
- Expliquer pourquoi une catégorie est vide si c'est le cas.

**TESTS FONCTIONS:**

*TESTS FONCTIONS - BACKEND*

- Croisière avec membres d'équipages et voyageurs, vérifier tous les passagers et membres d'équipage sont attribués dans le manifeste : il valide une règle métier sans dépendre de l’API, de la BD ou de l’interface.

- Croisière inexistante, vérifier que le service lève une erreur lorsque la croisière n'existe pas : il couvre un cas d’erreur métier attendu sans impliquer l’infrastructure.

- Croisière sans passager ni membre d'équipage, vérifier que le service renvoie un manifeste vide : il valide le comportement métier "les navettes vides ne sont pas obtenues"

*TESTS FONCTIONS - FRONTEND*

- Affichage du tableau du manifeste des navettes d'urgence, vérifier que l’interface affiche un tableau lorsque des navettes d'urgence sont retournées par l’API mockée : c’est un test fonction car il valide un comportement fonctionnel de l’UI selon les critères d’acceptation.

- Aucun tableau si aucune navette d'urgence, vérifier que l’interface n’affiche pas de tableau lorsque l’API retourne un manifeste sans navettes : c’est un test fonction car il valide le critère métier « ne pas afficher les navettes vides » puisque toutes les navettes sont vides

- Bouton "Get Manifest" si manifeste vide, vérifier que l'interface affiche le bouton en rouge lorsque l'API retourne un manifeste vide : c'est un test fonction car il valide un comportement fonctionnel de l’UI selon les critères d’acceptation.


**TESTS COMPOSANT:**

*TESTS COMPOSANT - FRONTEND*

Il n’y a pas de tests composant frontend, car les tests composant concernent exclusivement l’API backend (JerseyTest). Le frontend est testé avec Vitest ou Cypress, mais n’a pas de concept de "test composant"

*TESTS COMPOSANT - BACKEND*

- Réponse 200 avec des navettes d'urgence attribuées, Vérifier que l’appel à GET /cruises/{cruiseId}/emergencyShuttles retourne un HTTP 200 avec un JSON contenant la liste emergencyShuttles conforme à l’énoncé : c’est un test composant qui valide le contrat de la ressource REST (statut + format de réponse) en isolant le service par un mock.

- Erreur 404 CRUISE_NOT_FOUND, Vérifier que lorsque le service lève une CruiseNotFoundException, la route retourne un HTTP 404 avec error = "CRUISE_NOT_FOUND" et la bonne description : c’est un test composant qui vérifie le comportement de la ressource et de l’exception mapper du point de vue HTTP.

**TESTS MULTI-COMPOSANTS (Cucumber):**

*TESTS MULTI-COMPOSANTS - FRONTEND*

Ici, le test multi-composant fait intervenir plusieurs couches techniques du backend qui collaborent :

Jetty / Jersey (serveur + ressource REST),
services métiers,
repositories Hibernate,
base de données H2 en mémoire.

Si on faisait intervenir aussi le frontend (navigateur + SPA + backend + DB), là on serait dans du E2E (bout-en-bout), pas seulement du multi-composant backend.

*TESTS MULTI-COMPOSANTS - BACKEND*

- Manifeste obtenu avec navettes d'ugence attribuées, Vérifier que GET /cruises/{cruiseId}/emergencyShuttles retourne un code HTTP 200 et qu’au moins une navette est attribuée : il fait collaborer plusieurs composants réels du backend : serveur Jetty, ressource REST, service métier d’attribution, repositories Hibernate et base H2. Aucune couche n’est mockée, l’appel HTTP traverse toute la stack backend.

- Croisière inexistante, Vérifier qu’un appel à GET /cruises/{cruiseId}/emergencyShuttles retourne un code HTTP 404 et une erreur CRUISE_NOT_FOUND : il exécute la vraie pile backend : API REST, validation métier, exception métier, ExceptionMapper, sérialisation JSON, et serveur Jetty. Le test ne mocke rien et valide la collaboration de ces composants via une requête HTTP réelle.


**TESTS END-TO-END (Cypress):**

- Vérifier que lorsqu’on clique sur "Get Manifest" et que le backend retourne un manifeste non vide, l'interface utilisateur affiche les détails des navettes et un coût total : Parce qu’il exécute le vrai frontend dans un navigateur, appelle le vrai backend sans mock et valide le résultat final rendu dans l’interface.

- Vérifier que lorsqu’on clique sur "Get Manifest" et qu’aucune navette d'urgence n’est attribuée, le bouton devient rouge et aucun tableau n'est affiché : Parce qu’il teste l’application complète du navigateur au backend réel, en validant l’affichage final du frontend.