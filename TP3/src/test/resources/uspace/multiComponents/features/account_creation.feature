#language: fr

Fonctionnalité: Création de compte utilisateur

Contexte:
  Étant donné que le serveur USpace est démarré

Scénario: Créer un compte utilisateur valide
Étant donné un compte utilisateur valide à créer
Quand j'envoie la requête de création du compte
Alors la création retourne le statut 201
Et le compte créé peut être consulté avec succès

Scénario: Créer un compte sans username
Étant donné un compte utilisateur sans username
Quand j'envoie la requête de création du compte
Alors la création retourne le statut 400 et l'erreur "MISSING_PARAMETER" pour le paramètre "username"

Scénario: Créer un compte sans homePlanetName
Étant donné un compte utilisateur sans homePlanetName
Quand j'envoie la requête de création du compte
Alors la création retourne le statut 400 et l'erreur "MISSING_PARAMETER" pour le paramètre "homePlanetName"

Scénario: Créer un compte avec un username déjà utilisé
Étant donné un compte utilisateur avec un username déjà utilisé
Quand j'envoie la requête de création du compte
Alors la création retourne le statut 400 et l'erreur "USERNAME_ALREADY_EXISTS" avec la description "Username already exists."
