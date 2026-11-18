# Questions sur la mise en place de Hibernate et H2 (Seulement pour IFT-6006)

En lien avec l'exercice [Mettre en place Hibernate avec une base de données H2](../exercices/exercice-hibernate-h2.md).

### 1. Expliquer pourquoi votre implémentation de l'accès aux données avec Hibernate respecte le DIP.

Mon implémentation de l’accès aux données avec Hibernate respecte le principe DIP car les classes du domaine et de l’application dépendent uniquement d’une abstraction (CruiseRepository), et non d’un détail technique comme Hibernate.
L’implémentation concrète HibernateCruiseRepository se trouve dans la couche infrastructure et est injectée au moment de l’exécution via le mécanisme d’injection de dépendances (HK2). Ainsi, la logique métier reste indépendante du choix de la technologie de persistance.