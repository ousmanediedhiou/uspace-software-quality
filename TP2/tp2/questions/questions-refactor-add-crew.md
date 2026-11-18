# Questions sur le réusinage de l'ajout de membres d'équipage (Seulement pour IFT-6006)

En lien avec l'exercice [Réusiner l'ajout de membres d'équipage](../exercices/exercice-refactor-add-crew.md).

### 1. Expliquer les modifications apportées en vous appuyant sur les principes SOLID+T et les concepts vus en classe. (environ 150-200 mots)

L’ajout d’un membre d’équipage était initialement géré directement dans Cruise, mélangeant validation, allocation de cabine, création et persistance.
Ce code violait plusieurs principes SOLID (forte dépendance aux implémentations concrètes, faible testabilité et responsabilités multiples).
Nous avons réusiné en introduisant un service d’orchestration CrewMemberService qui centralise les règles : vérification de la croisière, validation du format employeeId, détection de doublon, allocation d’une cabine STANDARD, création du CrewMember, ajout à la croisière, puis sauvegarde via le CruiseRepository.
Cruise a été allégée, elle ne conserve que la cohérence de son état (ajout et réservation). L’allocation est confiée à une stratégie injectée (CabinAllocationPolicy → StandardCrewCabinPolicy) pour respecter le DIP et l'OCP : la logique est interchangeable sans modifier le service. Les interfaces (CruiseRepository, CabinAllocationPolicy) respectent ISP par leur spécialisation. L’héritage entre Traveler et CrewMember a été découplé pour préserver LSP et la cohérence métier. Toutes les dépendances sont injectées et moquables, assurant la testabilité (TDA), des tests unitaires isolés valident chaque règle et comportement, garantissant un code robuste, évolutif et conforme.

### 2. Expliquer pourquoi vous avez choisi de garder l'héritage entre CrewMember et Traveler (ou de ne pas le garder). (environ 100-150 mots)

Nous avons choisi de ne pas conserver l’héritage entre CrewMember et Traveler, car leurs rôles métier et responsabilités diffèrent fondamentalement.
Un Traveler participe aux réservations, détient des badges et peut souscrire à des expériences de gravité zéro, tandis qu’un CrewMember appartient au personnel d’équipage : il n’a aucune activité touristique et se voit simplement attribuer une cabine STANDARD obligatoire.
Conserver l’héritage impliquerait d’hériter de comportements non pertinents, nécessitant des méthodes vides ou des exceptions (UnsupportedOperationException), ce qui violerait le principe de substitution de Liskov (LSP).
En supprimant cet héritage, nous garantissons le principe de responsabilité unique (SRP) : chaque classe ne gère que ses propres invariants métier.
Le CrewMember conserve les données utiles (nom, identifiant employé) via composition plutôt que par héritage.
Ce découplage améliore la cohérence du domaine, réduit le risque d’effets de bord, et permet de faire évoluer séparément les règles métier du personnel et celles des voyageurs, tout en restant conforme à OCP et à la philosophie du DDD + SOLID+T.