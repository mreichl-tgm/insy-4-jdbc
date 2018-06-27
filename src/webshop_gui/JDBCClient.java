package webshop_gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class JDBCClient {
    private static int version = 0;
    private static int last_v1 = 0;
    private static int last_v2 = 0;
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
                setFalcon("artikel", a.getAnr());

                pstmt = con.prepareStatement("UPDATE artikel SET abez=?, ainfo=?, npreis=?, vstueckz=? WHERE anr=?");
                pstmt.setString(1, a.getAbez());
                pstmt.setString(2, a.getAinfo());
                pstmt.setDouble(3, a.getPreis());
                pstmt.setInt(4, a.getVstueckz());
                pstmt.setInt(5, a.getAnr());
                pstmt.execute();

                if (!endFalcon()) {
                    System.out.println("Update failed due to a lost update!");
                }
            } else {
                addArticle(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setFalcon(String source, int identifier) {
        try {
            con.setAutoCommit(false);

            PreparedStatement pstmt_v1 = con.prepareStatement("SELECT version as v1 FROM ? WHERE ?");
            pstmt_v1.setString(1, source);
            pstmt_v1.setInt(2, identifier);

            ResultSet res_v1 = pstmt_v1.executeQuery();
            last_v1 = res_v1.getInt("version");

            PreparedStatement pstmt_v2 = con.prepareStatement("SELECT version as v2 FROM ? WHERE ?");
            pstmt_v2.setString(1, source);
            pstmt_v2.setInt(2, identifier);

            ResultSet res_v2 = pstmt_v2.executeQuery();
            last_v2 = res_v2.getInt("version");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setFalcon(String source, String identifier) {
        try {
            con.setAutoCommit(false);

            PreparedStatement pstmt_v1 = con.prepareStatement("SELECT version as v1 FROM ? WHERE ?");
            pstmt_v1.setString(1, source);
            pstmt_v1.setString(2, identifier);

            ResultSet res_v1 = pstmt_v1.executeQuery();
            last_v1 = res_v1.getInt("version");

            PreparedStatement pstmt_v2 = con.prepareStatement("SELECT version as v2 FROM ? WHERE ?");
            pstmt_v2.setString(1, source);
            pstmt_v2.setString(2, identifier);

            ResultSet res_v2 = pstmt_v2.executeQuery();
            last_v2 = res_v2.getInt("version");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean endFalcon() {
        try {
            con.setAutoCommit(true);

            if (last_v1 == last_v2) {
                version = version + 1;
            } else {
                con.rollback();
                return false;
            }

            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    void deleteArticle(String abez) {
        PreparedStatement pstmt;

        try {
            setFalcon("artikel", abez);

            pstmt = con.prepareStatement("DELETE FROM artikel WHERE abez = ?");
            pstmt.setString(1, abez);
            pstmt.execute();

            if (!endFalcon()) {
                System.out.println("Update failed due to a lost update!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void falcon() throws SQLException {
	    Statement stmt = con.createStatement();
        stmt.executeQuery("BEGIN");
    }
}
