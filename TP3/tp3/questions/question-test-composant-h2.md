# Questions: Tests composant de l'accès aux données avec H2 (Seulement IFT-6006)

### 1. Expliquer pourquoi ces tests sont des tests "composant" et pas des tests "fonctions".

Les tests réalisés pour HibernateAccountRepository et HibernateCruiseRepository sont des tests composant parce qu’ils vérifient le fonctionnement d’un composant technique complet, et non d’une simple fonction isolée.

Un test fonction (unitaire) testerait uniquement la logique d’une méthode, en isolant la classe avec des doublures (mocks).
Par exemple, un test fonction testerait une validation, un calcul, ou un comportement métier sans dépendance externe.

Ici, nous faisons l’inverse :

Les tests utilisent la vraie configuration Hibernate (HibernateUtil, hibernate.cfg.xml)

Les tests utilisent la vraie base de données H2 en mémoire

Les tests déclenchent des sessions, des transactions, le mapping objet–relationnel, etc.

Les tests vérifient save, findBy…, et la capacité du repository à persister et relire des entités réelles (Account, Cruise)

Donc, au lieu de tester une seule fonction isolée :

On teste le composant complet “repository + Hibernate + H2”
L'objectif est de valider le fonctionnement réel du composant d'accès aux données

Ce périmètre est beaucoup plus large que celui d’un test fonction, ce qui correspond exactement à la définition d'un test composant