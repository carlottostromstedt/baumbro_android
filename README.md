# baumbro_android

An Android Java project for the course UeK 335

## Planung

### Storyboard
![Storyboard](assets/baumbro_storyboard.png)

## Technischer Entwurf

### Anforderungsanalyse

#### Spezifikation der Anforderungen
Um meine Anwendungsfälle zu definieren, werde ich die Anforderungen in funktionale und nicht funktionale Anforderungen gliedern.

| Typ | Beschreibung |
| --- | --- |
| FR | Anforderungen an das System um die Bedürfnisse des Benutzers zu erfüllen. |
| NFR | Anforderungen an Qualitäten, Eigenschaften und Einschränkungen des Systems. |

#### Akteur
Der Anwender dieser Applikation ist alltäglicher Anwender von Mobiltelefonen, aber kein IT-Spezialist. Er möchte Informationen zu Bäumen in seiner Umgebung aufrufen.

#### Funktionale Anforderungen

| Titel                                    | ID   | Akteur                           | Anforderung                                                          | Akzeptanzkriterien                                                                                       |
|------------------------------------------|------|----------------------------------|----------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| Abfrage und Darstellung des Gerätestandorts | FR-1 | Anwender der Baumbro-Applikation | Der Gerätestandort wird abgefragt und dargestellt.                  | Der Gerätestandort wird in der View der zweiten Aktivität in der Kartenansicht am richtigen Ort dargestellt. |
| Kartendarstellung von naheliegenden Bäumen | FR-2 | Anwender der Baumbro-Applikation | Bäume, die sich in der Nähe des Anwenders befinden, werden auf der Karte angezeigt. | Bäume, die sich innerhalb einer festgelegten Distanz vom Gerätestandort befinden, werden in der Kartenansicht als "Pins" dargestellt. |
| Auflistung von naheliegenden Bäumen     | FR-3 | Anwender der Baumbro-Applikation | Bäume, die sich in der Nähe des Anwenders befinden, werden angezeigt. | Bäume, die sich innerhalb einer festgelegten Distanz vom Gerätestandort befinden, werden unterhalb der Kartenansicht aufgelistet. |
| Darstellung eines einzelnen Baumes      | FR-4 | Anwender der Baumbro-Applikation | In einer Show-View werden Details zu einem Baum gezeigt.             | In einer dritten View wird der ausgewählte Baum mit detaillierten Informationen angezeigt.                |
| Schnappschuss eines Baumes erstellen    | FR-5 | Anwender der Baumbro-Applikation | Der "Schnappschuss"-Knopf auf der Show-Seite öffnet die Kamera.      | Beim Klick auf den "Schnappschuss"-Knopf auf der Show-Seite wird ein Kamera-Intent ausgelöst.             |
| Schnappschuss in Galerie dargestellt    | FR-6 | Anwender der Baumbro-Applikation | Der Schnappschuss des Baumes wird in einer Galerieansicht gezeigt.   | In einer View für einen einzelnen Baum wird der getätigte Schnappschuss in einer Galerieansicht dargestellt. |

#### Nicht-Funktionale Anforderungen

| Titel                                    | ID   | Anforderung                           | Akzeptanzkriterien                                                                           |
|------------------------------------------|------|---------------------------------------|----------------------------------------------------------------------------------------------|
| UI im Storyboard-Design                  | NFR-1| Das UI entspricht der Designsprache, die im Storyboard erfasst wurde.                     | Das UI entspricht der Designsprache des Storyboards: Grün und minimalistisch.                |
| Performante Datenbankabfragen            | NFR-2| Datenbankabfragen sollten performant sein.                                               | Die App sollte die Datenbankabfragen für die Kartenansicht innerhalb von 0.5 Sekunden tätigen. |
| Schneller Fluss zwischen Views           | NFR-3| Der Wechsel zwischen den Views sollte schnell sein.                                       | Das Laden einer View sollte nie länger als eine Sekunde dauern.                                |

### Anwendungsfalldiagramm

![Anwendungsfalldiagramm](assets/usecase_diagramm.png)

### Testen

Aus Zeit- und Komplexitätsgründen, beschränken wir uns auf manuelle Tests und es werden keine Unit-Tests geschrieben. 
Die Tests werden auf einem emulierten Gerät im IDE Android Studio ausgeführt.

#### Testumgebung
| Eigenschaft | Technische Spezifikation |
| --- | --- |
| Gerät | Pixel 4 (emuliert) |
| Bildschirmauflösung | 1020x2280 440dpi |
| Android-Version | Android 14.0 arm64-v8a |
| Entwicklungsumgebung | Android Studio Iguana | 2023.2.1 |

#### Testfälle

| Titel                                        | ID   | Voraussetzungen                                             | Durchführung                                                                                         | Erwartetes Resultat                                                                                               |
|----------------------------------------------|------|-------------------------------------------------------------|------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| Knopf *Find a Tree* ruft Kartenasicht-View auf | T-1  | App gestartet und Berechtigungen für Standort gegeben.      | Es wird auf den *Find a Tree*-Knopf in der ersten View geklickt.                                      | Kartenansicht wird in einer zweiten View gezeigt.                                                                  |
| Kartenansicht zeigt korrekter Standort an.  | T-2  | App gestartet, Berechtigungen für Standort gegeben.          | Es wird auf den *Find a Tree*-Knopf in der ersten View geklickt.                                      | Kartenansicht in der zweiten View zeigt mit einem Pin den korrekten Standort an.                                  |
| Kartenansicht zeigt umliegende Bäume mit Pins. | T-3  | App gestartet, Berechtigungen für Standort gegeben.          | Es wird auf den *Find a Tree*-Knopf in der ersten View geklickt.                                      | Kartenansicht in der zweiten View zeigt umliegende Bäume mit Pins markiert.                                        |
| Kartenansicht zeigt umliegende Bäume aufgelistet. | T-4  | App gestartet, Berechtigungen für Standort gegeben.          | Es wird auf den *Find a Tree*-Knopf in der ersten View geklickt.                                      | Unterhalb der Kartenansicht in der ersten View werden die umliegenden Bäume mit ihren Eigenschaften in einer Listenansicht angezeigt. |
| Listeneinträge führen zu Show-Seite.       | T-5  | App gestartet, Berechtigungen für Standort gegeben, *Find a Tree*-Knopf geklickt. | Es wird auf einen Listeneintrag eines Baumes geklickt.                                               | Wir werden zu einer View geführt, die Details zum angeklickten Baum sowie einen "Schnappschuss"-Knopf anzeigt.     |
| "Schnappschuss"-Knopf öffnet Kamera-App.   | T-6  | App gestartet, Berechtigungen für Standort gegeben, *Find a Tree*-Knopf geklickt, Listeneintrag eines Baumes geklickt. | Es wird auf den "Schnappschuss"-Knopf in der Show-View geklickt.                                      | Eine Kamera-Intent wird ausgeführt und die Kamera wird geöffnet.                                                  |
| Schnappschuss wird in Galerie gezeigt.     | T-7  | App gestartet, Berechtigungen für Standort gegeben, *Find a Tree*-Knopf geklickt, Listeneintrag eines Baumes geklickt, "Schnappschuss"-Knopf geklickt. | Der Schnappschuss wird getätigt und das Bild akzeptiert.                                              | Der Schnappschuss wird in einer View in einer Galerieansicht für den ausgewählten Baum angezeigt.                 |


### Systemaufbau

#### Klassendiagramm
![Klassendiagramm Baumbro](assets/class_diagram.png)

