# Questions: Exécution des différents tests

### 1. Donner les commandes Maven pour exécuter les différents tests.

Tests fonctions:
```bash
mvn test -Dtest='*Test,!*ComponentTest,!*CucumberTest'
```

Tests composant:
```bash
mvn test -Dtest=*ComponentTest
```

Tests multi-composants (Cucumber):
```bash
mvn test -Dtest=*CucumberTest
```

