# biblio-api

API REST de gestion d'une petite bibliothèque (Spring Boot 3.5, Java 25, architecture MVC).

## Démarrer

```bash
./mvnw spring-boot:run
```

- Swagger UI : http://localhost:8080/swagger-ui.html
- Les données sont gardées **en mémoire** (perdues à l'arrêt).

## Structure

| Package      | Rôle                                                        |
| ------------ | ----------------------------------------------------------- |
| `model`      | Le modèle : `Livre`                                         |
| `repository` | Accès aux données (en mémoire)                              |
| `service`    | Logique métier                                              |
| `controller` | Contrôleur REST (+ `LivreRequest`, corps JSON des requêtes) |
| `exception`  | Exceptions et traduction en codes HTTP                      |

## Endpoints

| Méthode | URL                                           | Description          |
| ------- | --------------------------------------------- | -------------------- |
| GET     | `/api/livres`                                 | Lister les livres    |
| GET     | `/api/livres/{id}`                            | Obtenir un livre     |
| POST    | `/api/livres`                                 | Ajouter un livre     |
| PUT     | `/api/livres/{id}`                            | Modifier un livre    |
| DELETE  | `/api/livres/{id}`                            | Supprimer un livre   |
| POST    | `/api/livres/{id}/emprunter`                  | Emprunter un livre   |
| POST    | `/api/livres/{id}/retourner`                  | Retourner un livre   |
| GET     | `/api/livres/{id}/frais-retard?joursRetard=N` | Frais de retard en $ |

---

## Réponses aux questions du TP :

### A.1 Lecture de code

Parcourez les packages `model`, `repository`, `service`, `controller`, `exception`. Répondez par écrit
(2-3 lignes chacune) dans votre `README.md` :

1. Quelle classe joue le rôle du **Modèle**, du **Contrôleur** ? Où se trouve la **logique métier** ? La classe Livre joue le rôle du Modèle et la classe LivreController joue le rôle du Contrôleur. La logique métier se trouve dans le Service, dans la classe LivreService.

2. Que fait `GestionnaireErreurs` et quel code HTTP obtient-on pour un livre introuvable ? Et pour un double emprunt ? La classe GestionnaireErreurs transforme les exceptions que le service renvoit en code d'erreur HTTP lisibles et clairs. Pour un livre introuvable on obtient le code d'erreur 404 et pour un double emprunt c'est l'erreur 409.

3. Où les données sont-elles stockées ? Que se passe-t-il quand l'application redémarre ? Les données sont stockées en mémoire, lorsque l'application redémarre, elles sont perdues.

4. Suivez le trajet complet d'un appel `POST /api/livres/1/emprunter` : quelles classes/méthodes sont appelées, dans quel ordre ?

        En premier, le controlleur est appelé (classe LivreController)
        Ensuite, c'est le service qui est appelé (classe LivreService, méthode emprunter(long id))
        Après, dans la méthode emprunter, on appel la méthode trouver de LivreService qui elle appelle le repository (LivreRepository) avec la méthode findById pour vérifier si le livre existe.
        Puis, encore dans la méthode emprunter, on appel la classe Livre pour vérifier la disponibilité du livre et la changé s'il est disponible.
        Enfin ça appel la fonction save du repository LivreRepository pour enregistrer le changement de disponibilité du livre.



Exécutez ces scénarios et notez le code HTTP obtenu ainsi que le résultat et explique que veut dire ce code:
| # | Appel | Résultat attendu à vérifier |
|---|---|---|
| 1 | `GET /api/livres` | Liste de 3 livres |
| 2 | `POST /api/livres` avec un nouveau livre | Code 201, id attribué |
| 3 | `POST /api/livres` avec un titre vide | Code 400 |
| 4 | `POST /api/livres/1/emprunter` deux fois de suite | 200 puis 409 |
| 5 | `GET /api/livres/2/frais-retard?joursRetard=100` | Notez le montant : il vous servira pour la partie C |

Ces réponses vont dans votre `README.md` : branche `feature/lecture-code`, puis Pull Request (voir les règles de la partie 0).

        1 : Obtenue une liste de 3 livres et un code HTTP 200. Ce code veut dire Success (OK)
        2 : Le livre a été créé avec succès (Code 201 - Created), ça retourne l'entité sous format JSON. 
        3 : Ça retourne une Erreur 400, soit une requête invalide. Dans notre cas, le response body nous avertis qu'il faut obligatoirement avoir un titre. 
        4 : La première fois ça retourne le code 200, soit success(OK), ce qui veut dire que nous avons bel et bien emprunter le livre avec l'id 1. La seconde fois, ça retourne le code 409, soit le code d'erreur d'un conflit, puisque le livre est déjà emprunter.  
        5 : Ça retourne le code d'erreur 200 soit success (OK), ainsi que le montant des frais pour 100 jours de retard, soit 20$.

