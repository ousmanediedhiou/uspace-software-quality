# Questions sur la nouvelle fonctionnalité: Voir le coût de la réservation (Seulement pour IFT-6006)

En lien avec l'exercice [Voir le coût de la réservation](../exercices/exercice-feature-cost.md).

### 1. Expliquer votre implémentation en vous appuyant sur les principes SOLID+T et les concepts vus en classe. (environ 150-200 mots)

Les fonctionnalités ont été implémentées en appliquant les principes SOLID + T, afin de garantir une architecture claire, extensible et testable.
Le principe **SRP** est respecté grâce à la classe **BookingCostCalculator**, isolée du reste du domaine, elle a pour seule responsabilité le calcul du coût selon la cabine, la date et les voyageurs.
Le principe **OCP** est appliqué via l’interface **CruisePricingProvider**, permettant de modifier facilement les tarifs ou d’ajouter de nouvelles sources de prix sans changer le calculateur.
Grâce à **LSP** et **ISP**, les abstractions (**CostCalculator**, **CruisePricingProvider**) restent simples et remplaçables par d’autres implémentations comme les tests et les nouveaux barèmes.
Le principe **DIP** est respecté puisque **BookingService** dépend d’interfaces injectées, et non de classes concrètes.
Toutes les dépendances sont injectées, ce qui permet d’écrire des tests unitaires isolés avec des mocks, sans couplage fort.
Le coût d’une réservation est enregistré de façon immuable au moment de la création, garantissant qu’un changement ultérieur des tarifs n’affecte pas les réservations déjà effectuées.