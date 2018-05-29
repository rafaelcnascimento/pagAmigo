package pagpal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class dinheiro {
    
    @SuppressWarnings("static-access")
    public static void enviar(user usuario) throws SQLException{
        
        float quantidade;
        String  destinatario;

        Scanner scan = new Scanner(System.in);

        System.out.println("\nSeu balanço: R$"+usuario.balanco);
        System.out.println("Digite a quantidade a ser enviada:");
        quantidade = scan.nextFloat();

        if (quantidade > usuario.balanco) {
            System.err.println("Fundos insuficientes");
        }

        System.out.println("Informe o email do destinatário:");
        scan.nextLine();
        destinatario = scan.nextLine();

        db DB = new db();
        Connection conn = DB.conn;

        String sql = "SELECT id FROM users WHERE email = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, destinatario); 

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            System.out.println("Destinatário inexistente");
        } else {
            sql = "UPDATE users SET balanco = balanco + ? WHERE email = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, quantidade); 
            stmt.setString(2, destinatario); 
            stmt.execute();

            usuario.balanco = usuario.balanco - quantidade;

            sql = "UPDATE users SET balanco = balanco - ? WHERE email = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setFloat(1, quantidade); 
            stmt.setString(2, usuario.email); 
            stmt.execute();
            
            sql = "INSERT INTO pagamentos(sender_id,receiver_id,valor) VALUES(?,?,?)";
        
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuario.id); 
            stmt.setInt(2, DB.getId(destinatario)); 
            stmt.setFloat(3, quantidade);
            stmt.executeUpdate();

            System.out.println("\nTransação efetuada com sucesso");
            System.out.println("O seu novo balanço é de: R$"+usuario.balanco);

        }
    }
    
    public static void depositar(user usuario) throws SQLException {
        
        float quantidade;
        
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Informe o valor a ser adicionado:");
        quantidade = scan.nextFloat();
        
        db DB = new db();
        Connection conn = DB.conn;

        String sql = "UPDATE users SET balanco = balanco + ? WHERE email = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        
        stmt = conn.prepareStatement(sql);
        stmt.setFloat(1, quantidade); 
        stmt.setString(2, usuario.email); 
        stmt.execute();
        stmt.close();
        
        sql = "INSERT INTO pagamentos(sender_id,receiver_id,valor) VALUES(?,?,?)";
        
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, usuario.id); 
        stmt.setInt(2, usuario.id); 
        stmt.setFloat(3, quantidade);
        stmt.executeUpdate();
        
        usuario.balanco = usuario.balanco + quantidade;
        
        System.out.println("\nTransação efetuada com sucesso");
        System.out.println("O seu novo balanço é de: R$"+usuario.balanco);
    }
    
    public static void solicitar(user usuario) throws SQLException {
        
        
    }
}

