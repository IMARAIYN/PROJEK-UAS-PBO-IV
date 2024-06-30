package toko;

//ConnectDB.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectDB {
    private String url, usr, pwd, db;
    connectDB(){
        db = "toko";
        url = "jdbc:mysql://localhost/" + db;
        usr = "root";
        pwd = "";
    }
    
    Connection getConnect(){
        Connection cn = null;
        try {
            //load driver database
            Class.forName("com.mysql.cj.jdbc.Driver");
            cn = DriverManager.getConnection(url,usr, pwd);
            System.out.println("Koneksi Berhasil");
        } catch (ClassNotFoundException er) {
            System.out.println("Error #1: " + er.getMessage());
            System.exit(0);
        } catch (SQLException er) {
            System.out.println("Error #2: " + er.getMessage());
        }
        return cn;
    }
    public static void main(String [] args) {
        new connectDB().getConnect();
    }
}