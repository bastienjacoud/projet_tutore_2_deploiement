# README projet_tutore_2
Projet Tutoré 2 Polytech 5A Apprentissage

## Déploiement de l'application

Le déploiement à été automatisé pour se déployer sur Heroku. 
Pour cela, il faut se placer sur la branche master, merger les modifications de la branche de développement sur master.
Il suffit ensuite de se placer dans le dossier front-polymeetup puis de taper la commande suivante :
 - npm run build
 
Le projet angular est alors compilé en mode production, placé dans le dossier static du serveur. Le serveur va ensuite remplacer ce qui existait dans le projet de déploiement. 
Il ne reste plus qu'à push le projet de déploiement et les modifications seront en ligne. 

ATTENTION TOUTEFOIS : Pour que le déploiement s'effectue correctement, il faut que les deux repository GIT se situent (développement et déploiement) se situent l'un à côté de l'autre au niveau de l'arborescence de l'ordinateur. 

## Equipe

* Anthony Chaput
* Thomas Gille
* Bastien Jacoud
* Mark Kpamy

## A faire (vrac)

```
Créer model étudiant
Créer un modèle ent 
Faire upload CV

Faire modèle souhait candidat
Faire modèle choix entreprise

Penser algo liaison ent-étudiant

Actualisation du planning 

Export des rendez vous entreprise & étudiant
Exports des demandes

Pages :

Login
Création de profil
	- Candidat (pdf)
	- Entreprise (title, image, desc, desc complete)
Selection des entreprise / candididats .

Page affichage des planning (étu, entreprise).
Bouton export de toutes les formations.

Endpoints

POST /login => Return JWT or ErrorCode

POST /student => return 200 or ErrorCode
POST /company => //
POST /admin => //

GET /company
GET /company/{id}

GET /student
GET /student/{id}

POST /choice    body = {selected: ['id1', 'id2"} // identification par JWT
GET /choice/{id}
```

## Questions en suspens

* Tactique planification : planning créé eau fur et à mesure (premier arrivé, premier servi) ou mise en place d'une date butoir (fermture des inscriptions, planning créé à ce moment là)
* Nombre entretiens à passer en parallèle (système de stand, de salle, de nombre de recruteurs ...)
* Contraintes temporelles de l'entreprsie pour le planning ?
