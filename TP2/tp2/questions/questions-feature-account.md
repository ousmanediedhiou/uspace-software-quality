# Questions sur la nouvelle fonctionnalité: Ajouter un compte utilisateur (Seulement pour IFT-6006)

En lien avec l'exercice [Ajouter un compte utilisateur](../exercices/exercice-feature-account.md).

### 1. Expliquer pourquoi votre implémentation respecte le DIP.

Mon implémentation respecte le principe d’inversion des dépendances (DIP),
car les services de haut niveau comme AccountService et BookingService dépendent d’abstractions (AccountRepository, CruiseRepository) plutôt que des implémentations concrètes (InMemoryAccountRepository, HibernateAccountRepository).
Le choix de l’implémentation est inversé et confié au conteneur d’injection (HK2/Guice) via la classe ConfigurationServerRest.
Ainsi, je peux remplacer la couche de persistance (InMemory á Hibernate) sans modifier la logique métier.
Cela rend le système plus flexible, testable et conforme aux principes SOLID.