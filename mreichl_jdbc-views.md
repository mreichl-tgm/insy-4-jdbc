## JDBC Views

### 1. Kundendaten
Erstellt eine View welche alle öffentlichen Kundendaten bereitstellt und gibt der Benutzergruppe *kunde* alle Rechte über diese.
```sql
CREATE VIEW kundendaten AS (SELECT knr, email FROM kunde NATURAL JOIN bestellung);
GRANT ALL PRIVILEGES ON kundendaten TO GROUP kunde;
```

### 2. Häufige Artikel
Erstellt eine View welche alle Artikel auflistet, welche überdurchschnittlich oft bestellt wurden.
```sql
CREATE VIEW ahaeufig AS (SELECT anr, count(anr) AS "canr" FROM bestellartikel GROUP BY(anr) HAVING (count(anr) > avg("canr")));
```

### 3. Materialized
Sammlung der häufigsten Artikel zum Zeitpunkt der Ausführung
```sql
CREATE MATERIALIZED VIEW mahaeufig AS (SELECT anr, count(anr) AS "canr" FROM bestellartikel GROUP BY(anr) HAVING (count(anr) > avg("canr")));
```

### Vergleich
Die Materialized View ist natürlich schneller, da diese die Daten bereits gespeichert hat.
Ein großer Nachteil ist dagegen, dass sie nicht aktuell sein muss.
