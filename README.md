
# Exit-Game

Ein Spielprojekt, das im Rahmen unseres Kurses entwickelt wird.

## Projektstruktur

```
Exit-Game/
├── main/          # Hauptprojekt des Spiels
├── prototypes/    # Experimentelle Prototypen und Tests
├── examples/      # Kleine Beispielprogramme und Tutorials
└── .idea/         # IntelliJ IDEA Konfiguration
```

Jeder Ordner ist ein unabhängiges Modul, an dem separat gearbeitet werden kann.

## Voraussetzungen

- **Java 17** (Eclipse Temurin)
- **IntelliJ IDEA** (Community oder Ultimate Edition)
- **Git**

## Einrichtungsanleitung

### 1. Repository klonen

```bash
git clone https://gitlab.lrz.de/00000000014C06C6/exit-game.git
cd exit-game
```

### 2. Java 17 installieren

Falls Java 17 noch nicht installiert ist:

1. IntelliJ IDEA öffnen
2. **Datei → Projektstruktur** (`Strg+Alt+Shift+S`)
3. Unter **Projekteinstellungen → Projekt** auf das **SDK** Dropdown klicken
4. **SDK hinzufügen → JDK herunterladen** auswählen
5. Folgendes auswählen:
    - **Version**: 17
    - **Anbieter**: Eclipse Temurin (AdoptiumOpenJDK)
6. **Herunterladen** klicken und auf Installation warten
7. **OK** klicken

### 3. Projekt öffnen

1. IntelliJ IDEA öffnen
2. **Datei → Öffnen**
3. Den `Exit-Game` Ordner auswählen
4. **OK** klicken
5. Warten, bis IntelliJ das Projekt indiziert hat

### 4. Einrichtung überprüfen

1. Rechts unten sollte **Java 17** angezeigt werden
2. `examples/hello-world/src/HelloWorld.java` öffnen
3. Rechtsklick und **'HelloWorld.main()' ausführen** auswählen
4. Im Output sollte "Hello, World!" erscheinen

## Mit dem Projekt arbeiten

### Code ausführen

- Rechtsklick auf eine beliebige `.java` Datei mit einer `main` Methode
- **'KlassenName.main()' ausführen** auswählen

### Neuen Code hinzufügen

**Im Hauptprojekt:**
```bash
# Spielcode in main/src/ hinzufügen
```

**Für Prototypen:**
```bash
mkdir -p prototypes/mein-prototyp/src
# Prototyp-Code hier hinzufügen
```

**Für Beispiele:**
```bash
mkdir -p examples/mein-beispiel/src
# Beispiel-Code hier hinzufügen
```

Nach dem Hinzufügen neuer Ordner: **Datei → Alles von Festplatte neu laden** in IntelliJ.

## Zusammenarbeit

- **Issues**: [Issue erstellen](https://gitlab.lrz.de/00000000014C06C6/exit-game/-/issues)
- **Merge Requests**: [Merge Request einreichen](https://gitlab.lrz.de/00000000014C06C6/exit-game/-/merge_requests)

### Workflow

1. Branch für dein Feature erstellen: `git checkout -b feature/mein-feature`
2. Änderungen vornehmen
3. Commit: `git commit -m "Feature hinzugefügt"`
4. Push: `git push origin feature/mein-feature`
5. Merge Request auf GitLab erstellen

## Problembehandlung

**"JDK 17 kann nicht gefunden werden"**
- Schritt 2 der Einrichtungsanleitung befolgen, um Java 17 herunterzuladen

**"Quellordner nicht erkannt"**
- **Datei → Alles von Festplatte neu laden** oder IntelliJ neu starten

**Code kompiliert nicht**
- Prüfen, ob das Sprachlevel Java 17 ist: **Datei → Projektstruktur → Projekt → Sprachlevel**

## Fragen?

Ein [Issue](https://gitlab.lrz.de/00000000014C06C6/exit-game/-/issues) eröffnen oder im Team-Chat (WhatsApp oder [Matrix](https://matrix.to/#/!ulsiGDIgpIdvmIgfJA:tum.de?via=tum.de)) fragen.
```