# Questions: Suppression de compte utilisateur

### 1. Élaborer la stratégie d'essais pour la fonctionnalité de suppression de compte utilisateur.

Pour faciliter la correction, suivez le gabarit ci-dessous.

**TESTS FONCTIONS:**

Valider qu’un compte inexistant ne peut pas être supprimé :On teste uniquement la règle métier : le service reçoit un nom d’utilisateur qui ne correspond à aucun compte, avec un repository simulé, sans utiliser d’autres couches du système.

Valider qu’un compte avec des réservations ne peut pas être supprimé : On teste uniquement la règle métier, avec un compte simulé contenant des réservations, sans utiliser d’autres couches du système.

Valider qu’un compte dont la HomePlanet est “Earth” ne peut pas être supprimé : On teste uniquement la règle métier, un compte simulé a une planète “Earth” (peu importe les majuscules), sans faire intervenir l’API ni la base de données.

Valider qu’un compte valide sans réservations et dont la planète n’est pas “Earth” peut être supprimé : On teste uniquement la logique métier : le compte simulé n’a aucune réservation et sa planète n’est pas “Earth”, sans dépendre d’aucune autre couche du système.


**TESTS COMPOSANT:**

Valider que la suppression d’un compte existant et supprimable retourne HTTP 200 : C’est un test composant car on teste la ressource API, la configuration Jersey et les exception mappers, sans utiliser la vraie logique métier ni la base de données.

Valider que la suppression d’un compte inexistant retourne HTTP 404 : C’est un test composant car on teste le comportement de l’API et du mapper d’exception, sans impliquer les repositories réels.

Valider que la suppression d’un compte non supprimable retourne HTTP 400 : C’est un test composant car on teste la transformation d’une erreur métier en réponse HTTP via la ressource et le mapper, sans utiliser la persistance réelle.

Valider que la ressource appelle le service de suppression avec le bon username : C’est un test composant car on teste l’intégration entre la ressource API et le service, tout en contrôlant le service par un mock.



**TESTS MULTI-COMPOSANTS (Cucumber):**

Supprimer un compte sans réservation et planète non Earth : C’est un test multi-composant car on appelle la vraie API REST, le vrai service de compte et le repository en mémoire, puis on vérifie aussi via un GET que le compte n’existe plus.

Supprimer un compte inexistant : C’est un test multi-composant car la requête DELETE passe par l’API, la logique métier et la persistance en mémoire, et on vérifie la réponse 404 retournée par tout ce flux complet.

Supprimer un compte avec la planète “Earth” : C’est un test multi-composant car on crée réellement un compte avec la planète “Earth”, puis on passe par l’API, le service et le repository pour vérifier que la règle métier bloque la suppression avec un HTTP 400.

Supprimer un compte avec des réservations : C’est un test multi-composant car on crée un compte et une réservation via les vraies routes de booking, puis on appelle l’API de suppression et on vérifie que l’ensemble API + service + persistance refuse la suppression avec un HTTP 400.
