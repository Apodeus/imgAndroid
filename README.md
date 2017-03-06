# EliJ
**Projet Android - Semestre 6**

*Réalisation d'une application Android de traitement d'images.*

## Sujet

[Lien](http://dept-info.labri.fr/~vialard/ANDROID/references/cahierDesCharges.pdf).

Deadline premier rendu - *Version Akuma*: **Vendredi 3 Mars**

Deadline rendu final - *Version Buzzard*: **Vendredi 14 Avril**
## Cahier des charges
Cahier : [Overleaf](https://www.overleaf.com/8419408rzkrzspxynkf#/29873234/).

Schéma fonctionnel : [Mindomo](https://www.mindomo.com/mindmap/b9565ab1eb794d20a15267735e7b041d).

## ToDo List

### Système
- ~~Charger une image depuis la Galerie~~
- ~~Charger une image depuis la caméra~~
- ~~Afficher une image sur une ImageView custom~~
- ~~Zoomer~~ 
- ~~Scroller~~
- ~~Réinitialiser l'image d'origine~~
- ~~Sauvegarder l'image modifiée~~

### Filtres
- ~~Régler la luminosité~~
- ~~Régler le contraste~~
- ~~Egalisation d'histogramme~~
- ~~Filtrage de couleur~~
- ~~Convolution~~

### Bonus
- Effet dessin au crayon
- Effet cartoon
- Incruster des objets (reconnaissance faciale ?)
- "Schtroumpfer" un visage
- Restreindre la zone d'application d'un filtre

### Problèmes
- ~~Problème de hitbox du menu circulaire~~
- ~~Resteindre le dépassement de l'image à l'écran~~
- Meilleure orientation de l'image lorsqu'elle est chargée
- Certaines images ne se chargent pas. (Problème en fonction de l'appareil utilisé ?)
- Améliorer le Zoom, le zoom doit s'opérer depuis le point entre les deux doigts.


## Membres

**04348** : Emile Barjou

**Apodeus** : Romain Ordonez

**Echoffee** : Adrien Halnaut

## Récupération du dépôt sous IntelliJ IDEA ou Android Studio

L'application est développée à la fois sur IntelliJ IDEA et Android Studio, et ce dernier masquant quelques fichiers incompatibles avec IDEA, une petite manipulation est nécessaire avant de pouvoir récupérer le projet et l'intégrer avec Git dans l'IDE.

- Créer un projet Android vierge avec comme nom de projet `My Application` et en domaine de compagnie `newEra`, sélectionner l'API 19.
- Ajuster si besoin les paramètres Gradle ou autre pour compiler l'application vierge correctement.
- Supprimer le dossier `MyApplication\app\src`.
- Dans la console, se placer dans le dossier `MyApplication\app` et cloner le dépôt :

```git clone https://github.com/Apodeus/imgAndroid```
- Placer *tout* le contenu du dossier `MyApplication\app\imgAndroid` dans le dossier `MyApplication\app`.
- Ajouter la racine VCS dans l'IDE (une pop-up apparaît pour cela).

L'architecture finale devrait ressembler à cela : 

```
MyApplication
	+ app
		+ out
			- elij-akuma.apk
		+ src
			+ main
				+ java
					+ NewEra.EliJ
						+ image
							+ processing
								+ shaders
									- ...
								+ tools
									- ...
								- ...
							- ...
						+ ui
							+ system
								- ...
							+ view
								+ inputs
									+ components
										- ...
									- ...
								- ...
							Clickable.java
						MainActivity.java
				+ res
					.
					. //fichiers ressource
					.
				+ rs 
					.
					. //fichiers RenderScript
					.
		 - build.gradle
```


## Theme colors
["Examined Living"](http://flatcolors.net/palette/615-examined-living)
