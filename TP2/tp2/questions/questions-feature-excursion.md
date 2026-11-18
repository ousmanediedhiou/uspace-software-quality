# Questions sur la nouvelle fonctionnalité: Ajouter une excursion sur une planète (Seulement pour IFT-6006)

En lien avec l'exercice [Ajouter une excursion](../exercices/exercice-feature-excursion.md).

### 1. Expliquer votre implémentation en vous appuyant sur les principes SOLID+T et les concepts vus en classe. (environ 150-200 mots)

L’implémentation de la fonctionnalité **Ajouter une excursion sur une planète** applique rigoureusement les principes SOLID+T.

Pour respecter le **SRP**, chaque classe a une responsabilité claire. **ExcursionService** gère la logique applicative et la validation des données, tandis que **ExcursionAssembler** et les **DTO** assurent la conversion entre les couches.
Pour respecter l'**OCP**, la fonctionnalité a été ajoutée sans modifier le cœur du domaine, uniquement en introduisant des classes cohérentes comme **ExcursionService**, **ExcursionAssembler**, **NewExcursionDto**, **ExcursionDto**, les différents **ExceptionMappers**, ainsi que l’enrichissement léger de **PlanetAssembler** et **CruiseAssembler**.

Le **LSP** est respecté grâce à une hiérarchie stable d’exceptions et d’interfaces.
Le **ISP** est appliqué via des interfaces fines telles que **CruiseRepository** ou **CabinAllocationPolicy**. Le DIP est mis en œuvre par l’injection de dépendances **(CruiseRepository, LocalDateTimeParser)** favorisant le découplage.
Nous avons des tests unitaires isolés avec Mockito et le format **Arrange Act Assert** valident chaque couche et chaque cas d’erreur, assurant un code robuste, extensible et maintenable.