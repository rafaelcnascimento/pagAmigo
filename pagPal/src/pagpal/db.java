package pagpal;
    
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;

public class db {

   public String DB_URL = "jdbc:mysql://localhost/pagpal";
   public String USER = "root";
   public String PASS = "";
   public Connection conn = null;
   public Statement stmt = null;
   
   public db() throws SQLException {
       this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
   }
     
   public ResultSet selectUser(String nome) throws SQLException{
    conn = DriverManager.getConnection(DB_URL, USER, PASS);
    
    ResultSet rs = conn.createStatement().executeQuery("SELECT * from users WHERE nome = '"+nome+"' ");
    
    return rs;
   }
}

/*
db DB = new db(); 
       Connection conn = DB.conn;
       Statement stmt = null;
        try {

            System.out.println("Conectando...");
            
            
            
            

//conn = DriverManager.getConnection(DB_URL, USER, PASS);

            System.out.println("Cria a consulta...");
            //stmt = conn.createStatement();
//           String sql = "CREATE TABLE MyGuests (\n" +
//                        "id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,\n" +
//                        "firstname VARCHAR(30) NOT NULL,\n" +
//                        "cc VARCHAR(30) NOT NULL,\n" +
//                        "email VARCHAR(50)\n" +
//                        ")";
//
//           stmt.executeUpdate(sql);

            ResultSet rs = DB.conn.createStatement().executeQuery("SELECT * from users");

            //precorre o resultset
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cc  = rs.getString("cc");
                String email = rs.getString("email");

                System.out.println("\nID:" + id);
                System.out.println("Nome:" + nome);
                System.out.println("CC:" + cc);
                System.out.println("Email:" + email);
            }
           
        } catch (SQLException se) {
            //errors JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //errors Class.forName
            e.printStackTrace();
        } finally {
            //finally fecha resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nada pode ser feito aqui
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Fim!");

*/


