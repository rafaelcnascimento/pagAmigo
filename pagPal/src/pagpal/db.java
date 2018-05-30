    package pagpal;
    
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;


public class db {

   public String DB_URL = "jdbc:mysql://localhost/pagpal";
   public String USER = "root";
   public String PASS = "";
   public Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
   public PreparedStatement stmt;
   
   public db() throws SQLException {
       this.conn = DriverManager.getConnection(DB_URL, USER, PASS);
   }
     
   public int getId(String email) throws SQLException{
        
       int id = 0;
       
       conn = DriverManager.getConnection(DB_URL, USER, PASS);
    
       String sql = "SELECT id FROM users WHERE email = ?";
        
       PreparedStatement stmt = conn.prepareStatement(sql);  
       stmt.setString(1, email);  
        
       ResultSet rs = stmt.executeQuery();
       while (rs.next()) {
          id = rs.getInt("id");
       }
       return id;
   }
   
   public void updateBalanco(Float quantidade,String destinatario, String email,int tipo) throws SQLException{
          
       if (tipo == 1) {
            String sql = "UPDATE users SET balanco = balanco + ? WHERE email = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, quantidade); 
            stmt.setString(2, email); 
            stmt.execute();
            stmt.close();
       } else {
           String sql = "UPDATE users SET balanco = balanco - ? WHERE email = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, quantidade); 
            stmt.setString(2, email); 
            stmt.execute();
            stmt.close();
       }
   }
   
   public void setPagamentos(int sender_id, int receiver_id, float quantidade) throws SQLException {
            String sql = "INSERT INTO pagamentos(sender_id,receiver_id,valor) VALUES(?,?,?)";
        
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sender_id); 
            stmt.setInt(2, receiver_id); 
            stmt.setFloat(3, quantidade);
            stmt.executeUpdate();
   }
           
   public  void setOrdens(int requisitante_id, int requisitado_id, float quantidade, String mensagem, int tipo) throws SQLException{
       if (tipo == 1) {
            String sql = "INSERT INTO ordens(requisitante_id,requisitado_id,valor,mensagem) VALUES(?,?,?,?)";
        
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requisitante_id); 
            stmt.setInt(2, requisitado_id); 
            stmt.setFloat(3, quantidade);
            stmt.setString(4, mensagem);
            stmt.executeUpdate();
       }
   }
   
   public ResultSet checkDestinatario(String email) throws SQLException{
        
        String sql = "SELECT id FROM users WHERE email = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email); 

        ResultSet rs = stmt.executeQuery();
        
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


