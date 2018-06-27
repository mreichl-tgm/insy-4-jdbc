## JDBCClient
Der JDBCClient ist eine Klasse welche mit der Webshop Datenbank interagiert.

Die Verbindung selbst wird im *Konstruktor* übernommen.

Artikel werden mittels *addArticle()* hinzugefügt und mittels saveArticle gespeichert falls diese geändert wurden, 
sollte *saveArticle()* einen neuen Artikel erhalten wird stattdessen addArticle() aufgerufen.

Die Funktion *deleteArticle()* löscht einen Artikel anhand seiner Artikelbezeichnung.
```java
class JDBCClient {
    private Connection con;
	JDBCClient() {
        try {
            con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/insy_webshop","postgres","");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    List<Artikel> getAllArticles() {
        ArrayList<Artikel> artikelListe = new ArrayList<>();
    
        try {
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM artikel");
    
            while (res.next()) {
                artikelListe.add(new Artikel(
                        res.getInt("anr"),
                        res.getString("abez"),
                        res.getString("ainfo"),
                        res.getFloat("npreis"),
                        res.getInt("vstueckz")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return artikelListe;
    }
    
	void addArticle(Artikel a) {
        PreparedStatement pstmt;

        try {
            pstmt = con.prepareStatement("INSERT INTO artikel (anr, abez, ainfo, npreis, vstueckz) VALUES (?,?,?,?,?)");
            pstmt.setInt(1, a.getAnr());
            pstmt.setString(2, a.getAbez());
            pstmt.setString(3, a.getAinfo());
            pstmt.setDouble(4, a.getPreis());
            pstmt.setInt(5, a.getVstueckz());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	void saveArticle(Artikel a) {
        Statement stmt;
        PreparedStatement pstmt;
        ArrayList<Integer> anrList = new ArrayList<>();

        try {
            stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM artikel");

            while (res.next()) {
                anrList.add(res.getInt("anr"));
            }

            if (anrList.contains(a.getAnr())) {
                pstmt = con.prepareStatement("UPDATE artikel SET abez=?, ainfo=?, npreis=?, vstueckz=? WHERE anr=?");
                pstmt.setString(1, a.getAbez());
                pstmt.setString(2, a.getAinfo());
                pstmt.setDouble(3, a.getPreis());
                pstmt.setInt(4, a.getVstueckz());
                pstmt.setInt(5, a.getAnr());
                pstmt.execute();
            } else {
                addArticle(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	void deleteArticle(String abez) {
        PreparedStatement pstmt;

        try {
            pstmt = con.prepareStatement("DELETE FROM artikel WHERE abez = ?");
            pstmt.setString(1, abez);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```
### Prepared Statements
Wie man sieht wurden bei allen Daten, welche vom Nutzer selbst kommen, preparedStatements verwendet.
Der sinn dieser besteht darin sogenannte SQL Injections zu verhindern.

Angenommen eine Funktion nimmt einen Benutzernamen und fügt diesen in eine Tabelle ein:
```java
String username = Schueler'); DROP TABLE students; --
ResultSet res = stmt.executeQuery("INSERT INTO user VALUES(" + username + ")");
```
Ein solcher Angriff wird durch ein reguläres Statement nicht verhindert!
Ein Prepared Statement überprüft jedoch die Werte.
Hat der User keine Möglichkeit das Statement zu beeinflussen, 
ist jedoch ein normaler Query zu bevorzugen um Laufzeit zu sparen.
