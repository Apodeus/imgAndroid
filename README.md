# Projet Android - Semestre 6
*Réalisation d'une application Android de traitement d'images.*

## Sujet

[Lien](http://dept-info.labri.fr/~vialard/ANDROID/references/cahierDesCharges.pdf).

Deadline premier rendu : **Vendredi 3 Mars**

Deadline rendu final : **Vendredi 14 Avril**
## Cahier des charges

Schéma fonctionnel : [Mindomo](https://www.mindomo.com/mindmap/b9565ab1eb794d20a15267735e7b041d).

## Membres

**04348** : Emile Barjou

**Apodeus** : Romain Ordonez

**Echoffee** : Adrien Halnaut

## Récupération du dépôt sous IntelliJ IDEA ou Android Studio

- Créer un projet Android vierge avec comme nom de projet `My Application` et en domaine de compagnie `newEra`.
- Ajuster si besoin les paramètres Gradle ou autre pour compiler l'application vierge correctement.
- Supprimer le dossier `MyApplication\app\src`.
- Dans la console, se placer dans le dossier `MyApplication\app` et cloner le dépôt :

```git clone https://github.com/Apodeus/imgAndroid```
- Placer *tout* le contenu du dossier `MyApplication\app\imgAndroid` dans le dossier `MyApplication\app`.
- Ajouter la racine VCS dans l'IDE (une pop-up apparaît pour cela).

L'architecture finale devrait ressembler à cela : 

![pic](https://a.pomf.cat/ogjuoo.png)

## Theme colors
["Examined Living"](http://flatcolors.net/palette/615-examined-living)

## Gradle
```
apply plugin: 'com.android.application'

android {
    compileSdkVersion 24
    buildToolsVersion "24.0.2"
    defaultConfig {
        applicationId "newera.myapplication"
        minSdkVersion 19
        targetSdkVersion 24
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:24.2.1'
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:design:24.2.1'
}
```
