# Questions: Création de compte utilisateur

### 1. Élaborer la stratégie d'essais pour la fonctionnalité de création de compte utilisateur.

Pour faciliter la correction, suivez le gabarit ci-dessous.

**TESTS FONCTIONS:**

Valider qu’un compte avec un username déjà existant ne peut pas être créé : On teste uniquement la logique du service de création, avec un repository simulé qui contient déjà ce username, sans utiliser d’autres couches du système.

Valider qu’un compte valide est sauvegardé dans le repository : On teste uniquement la logique du service de création, avec un repository simulé, et on vérifie que la méthode save est appelée, sans faire intervenir l’API ni la base de données.

Valider qu’un compte utilisateur existant est bien retourné par son username : On teste uniquement la méthode de recherche du service, avec un repository simulé qui retourne un compte, et un assembler simulé qui produit le DTO.

Valider qu’un compte inexistant génère une erreur lors de la recherche par username : On teste uniquement la méthode de recherche du service, avec un repository simulé qui ne trouve aucun compte, sans utiliser l’API ni la base de données.

Valider que la création échoue si le champ username est manquant ou vide : On teste uniquement le validateur de DTO de compte, avec un objet simulé sans username valide, sans utiliser le service ni le repository.

Valider que la création échoue si le champ homePlanetName est manquant ou vide : On teste uniquement le validateur de DTO de compte, avec un objet simulé sans homePlanetName valide, sans utiliser l’API ni la persistance.

Valider que le repository InMemory permet de retrouver un compte par son username : On teste uniquement le repository InMemory en sauvegardant un compte puis en le recherchant avec le même username, sans faire intervenir le service ni l’API.


**TESTS COMPOSANT:**

Valider que la création d’un compte valide retourne HTTP 201 : On appelle l’API POST /accounts avec un corps JSON valide et on vérifie que la réponse HTTP est 201, en testant uniquement la ressource API avec un service mocké, sans utiliser la vraie base de données.

Valider que la création d’un compte sans username retourne HTTP 400 avec l’erreur MISSING_PARAMETER pour username : On appelle l’API POST /accounts avec un JSON sans username et on vérifie que la réponse HTTP est 400 avec le bon code d’erreur, en testant la ressource et le mapper d’erreur, sans impliquer la persistance réelle.

Valider que la création d’un compte sans homePlanetName retourne HTTP 400 avec l’erreur MISSING_PARAMETER pour homePlanetName : On appelle l’API POST /accounts avec un JSON sans homePlanetName et on vérifie que la réponse HTTP est 400 avec le bon message d’erreur, en testant uniquement l’API et les mappers, sans toucher aux autres couches.

Valider que la création d’un compte avec un username déjà existant retourne HTTP 400 avec l’erreur USERNAME_ALREADY_EXISTS : On appelle l’API POST /accounts avec un username déjà utilisé, simulé par le service mocké, et on vérifie que l’API retourne 400 avec le bon code d’erreur, ce qui en fait un test composant de la ressource et du mapper.

Valider que la récupération d’un compte existant retourne HTTP 200 avec le corps du compte : On appelle l’API GET /accounts/{username} avec un service mocké qui retourne un DTO et on vérifie que la réponse est 200 avec les bonnes données, en testant la ressource API sans impliquer la vraie base de données.

Valider que la récupération d’un compte inexistant retourne HTTP 404 avec l’erreur ACCOUNT_NOT_FOUND : On appelle l’API GET /accounts/{username} avec un service mocké qui lance l’exception de compte inexistant, et on vérifie que la réponse est 404 avec le bon code d’erreur, ce qui en fait un test composant de l’API et de son mapper d’erreur.

**TESTS MULTI-COMPOSANTS (Cucumber):**

Valider qu’un compte utilisateur valide peut être créé dans un environnement réel :
C’est un test multi-composant parce qu’il exécute une vraie requête HTTP sur un serveur Jetty réel et utilise plusieurs composants réels ensemble (API, validation, service, repository Hibernate, base H2) sans mocks.

Valider que la création échoue si le champ username est manquant :
C’est un test multi-composant car il implique la collaboration réelle de plusieurs composants : la ressource API, le validateur réel, le mapper d’erreurs et le serveur HTTP, tous exécutés ensemble.

Valider que la création échoue si le champ homePlanetName est manquant :
C’est un test multi-composant parce que le test traverse plusieurs couches réelles (API, validation, mappers, infrastructure HTTP), ce qui dépasse largement le périmètre d’un test unitaire ou composant isolé.

Valider qu’un compte ne peut pas être créé si le username existe déjà :
C’est un test multi-composant car il exécute plusieurs requêtes réelles (création d’un premier compte, tentative de création d’un second), et valide l’interaction réelle entre API, service, repository et base H2, sans aucun mock.