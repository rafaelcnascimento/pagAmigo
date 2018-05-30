package pagpal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class dinheiro {
    
    @SuppressWarnings("static-access")
    public static void enviar(user usuario, db DB) throws SQLException{
        
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

        ResultSet rs = DB.checkDestinatario(destinatario);

        if (!rs.next()) {
            System.out.println("Destinatário inexistente");
        } else {
             
            DB.updateBalanco(quantidade, null,destinatario, 1);

            usuario.balanco = usuario.balanco - quantidade;
            
            DB.updateBalanco(quantidade, null, usuario.email, 0);
            
            DB.setPagamentos(usuario.id, DB.getId(destinatario), quantidade);
            
            System.out.println("\nTransação efetuada com sucesso");
            System.out.println("O seu novo balanço é de: R$"+usuario.balanco);
        }
    }
    
    public static void depositar(user usuario,db DB) throws SQLException {
        
        float quantidade;
        
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Informe o valor a ser adicionado:");
        quantidade = scan.nextFloat();
        
        DB.updateBalanco(quantidade, null, usuario.email, 1);
        
        DB.setPagamentos(usuario.id, usuario.id, quantidade);
        
        usuario.balanco = usuario.balanco + quantidade;
        
        System.out.println("\nTransação efetuada com sucesso");
        System.out.println("O seu novo balanço é de: R$"+usuario.balanco);
    }
    
    public static void solicitar(user usuario,db DB) throws SQLException {
        
        float quantidade;
        String  destinatario;
        String mensagem;

        Scanner scan = new Scanner(System.in);

        System.out.println("\nSeu balanço: R$"+usuario.balanco);
        System.out.println("Digite a quantidade a ser solicitada:");
        quantidade = scan.nextFloat();

        System.out.println("Informe o email do destinatário:");
        scan.nextLine();
        destinatario = scan.nextLine();

        ResultSet rs = DB.checkDestinatario(destinatario);

        if (!rs.next()) {
            System.out.println("Destinatário inexistente");
        } else {
            System.out.println("Digite uma mensagem para o destinatário:");
            mensagem = scan.nextLine();
            
            DB.setOrdens(usuario.id, DB.getId(destinatario), quantidade, mensagem, 1);

            System.out.println("\nSolicitação efetuada com sucesso");
        }
    }
    
    public static void historico(user usuario, db DB) throws SQLException {
        
        Connection conn = DB.conn;
        
        String sql = "SELECT p.id,u1.nome as sender,u2.nome as receiver ,p.valor,p.data \n" +
                     "FROM pagamentos p\n" +
                     "INNER JOIN users u1 on u1.id = p.sender_id\n" +
                     "INNER JOIN users u2 on u2.id = p.receiver_id\n" +
                     "WHERE sender_id = ? OR receiver_id = ?";  
          
        PreparedStatement stmt = conn.prepareStatement(sql);  
        stmt.setInt(1, usuario.id);  
        stmt.setInt(2, usuario.id); 
        ResultSet rs  = stmt.executeQuery();
        
        while (rs.next()) {
            System.out.println("------------------------------");
            System.out.println("ID= "+ rs.getInt("id"));
            if (rs.getString("sender").equals(rs.getString("receiver"))) {
                System.out.println("Tipo: Depósito");
            } else {
                System.out.println("Tipo: Transferencia");
            }
        }
        
        System.out.println("------------------------------");
    }
}



