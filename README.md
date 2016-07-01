# BeaconsApp

Ce projet est une application android native permettant de découvrir les beacons présent à proximité et d'ajouter ceux qui concerne notre environnement.
La deuxième fonctionnalité de cette application est de transmettre la position du devise à un serveur node JS. Ceci peut permettre de savoir ou est une personne dans la maison [Lien vers le serveur](https://github.com/elerion/beacons-node).

Cette application utilise deux librairies principal :
- Retrofit pour l'utiliser de requêtes http
- android-beacon-library pour l'interaction avec les beacons

## Installation

Builder l'app grâce à android studio, et modifier l'url du server dans `MainActivity`
