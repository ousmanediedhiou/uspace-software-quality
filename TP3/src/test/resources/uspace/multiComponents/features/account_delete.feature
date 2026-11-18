#language: fr

Fonctionnalité: Suppression de compte utilisateur

  Contexte:
    Étant donné le serveur USpace est démarré

  Scénario: Supprimer un compte sans réservation et planète non Earth
    Étant donné un compte sans réservation existe
    Quand je supprime ce compte
    Alors la suppression retourne le statut 200
    Et une requête pour consulter ce compte retourne le statut 404 et l'erreur "ACCOUNT_NOT_FOUND"

  Scénario: Supprimer un compte inexistant
    Étant donné aucun compte n'existe pour ce nom d'utilisateur
    Quand je supprime ce compte
    Alors la suppression retourne le statut 404 et l'erreur "ACCOUNT_NOT_FOUND" avec la description "Account not found."

  Scénario: Supprimer un compte avec la planète Earth
    Étant donné un compte avec la planète Earth existe
    Quand je supprime ce compte
    Alors la suppression retourne le statut 400 et l'erreur "ACCOUNT_CANNOT_BE_DELETED" avec la description "Account cannot be deleted."

  Scénario: Supprimer un compte avec des réservations
    Étant donné un compte avec des réservations existe
    Quand je supprime ce compte
    Alors la suppression retourne le statut 400 et l'erreur "ACCOUNT_CANNOT_BE_DELETED" avec la description "Account cannot be deleted."
