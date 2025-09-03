# Github Repository Popularity Calculator API

Dieses Projekt stellt eine REST-API bereit, die die Beliebtheit von GitHub-Repositories auf Basis von bestimmten
Kriterien berechnet.

## Überblick

Mit dem REST-Endpunkt (`/calculatePopularity/{queryString}`) können Repositories gesucht werden, für die dann eine 
"popularity score" berechnet wird.

Die Kriterien für die Suche sind:

| Parameter           | Beschreibung                                                                                 |
|---------------------|----------------------------------------------------------------------------------------------|
| queryString         | eine verpflichtende Pfad-Variable für "best-match"-Suche                                     |
| earliestDate        | optionaler Query-Parameter, um die Suche nach frühsten Erstellungsdatum zu filtern           |
| programmingLanguage | optionaler Query-Parameter, um die Suche nach der hinterlegten Programmiersprache zu filtern |

Mit dem "queryString" wird bei Github eine "best-match"-Suche ausgeführt, wobei die Repositories mit den meisten Sternen
absteigend sortiert ausgegeben werden.

Die Suche ist intern auf die Top 500 Ergebnisse limitiert.

## Verfügbare Endpunkte

### GET /

Gibt eine Beschreibung der API und eine Liste der verfügbaren Endpunkte zurück.

### GET /calculatePopularity/{queryString}?earliestDate={earliestDate}&programmingLanguage={programmingLanguage}

Startet die Suche nach den "best-match"-Repositories und berechnet für die Top 500 Ergebnisse eine "popularity score".

