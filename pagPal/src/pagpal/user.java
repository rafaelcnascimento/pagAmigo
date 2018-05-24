package pagpal;

import java.sql.SQLException;
import java.util.Scanner;

 import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.sql.PreparedStatement;  

public class user {
    
    public static int id;
    public static String nome;
    public static String email;
    public static String cc;
    public static float balanco;
    public static String senha;
    
    public static void cadastro() throws SQLException {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Nome:");
        nome = scan.nextLine();
        System.out.println("Email:");
        email = scan.nextLine();
        System.out.println("Numero do Cartão de Crédito:");
        cc = scan.nextLine();
        System.out.println("Senha:");
        senha = scan.nextLine();
        
        db DB = new db();
         
        Connection conn = DB.conn;
        
            String sql = "INSERT INTO users(nome,email,cc,senha) VALUES(?,?,?,?)";  
            try {  
                PreparedStatement stmt = conn.prepareStatement(sql);  
                stmt.setString(1, nome);  
                stmt.setString(2, email);  
                stmt.setString(3, cc);  
                stmt.setString(4, senha);  
                stmt.execute();  
                stmt.close();  
                
                System.out.println("\nCadastro efetuado com sucesso!");
            } catch (SQLException u) {  
                throw new RuntimeException(u);  
        }  
    }
    
}
