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
             
            DB.updateBalanco(quantidade, destinatario, 1);

            usuario.balanco = usuario.balanco - quantidade;
            
            DB.updateBalanco(quantidade, usuario.email, 0);
            
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
        
        DB.updateBalanco(quantidade, usuario.email, 1);
        
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
        
        Scanner scan = new Scanner(System.in);
        
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
                System.out.println("Data: "+rs.getString("data"));
                System.out.println("Valor: R$"+rs.getString("valor"));
            } else if (rs.getString("sender").equals(usuario.nome)){
                System.out.println("Tipo: Transferencia");
                System.out.println("Data: "+rs.getString("data"));
                System.out.println("Destinatário: "+rs.getString("receiver"));
                System.out.println("Valor: -R$"+rs.getString("valor"));
            } else {
                System.out.println("Tipo: Transferencia");
                System.out.println("Data: "+rs.getString("data"));
                System.out.println("Remetente: "+rs.getString("sender"));
                System.out.println("Valor: R$"+rs.getString("valor"));
            }
        }
        System.out.println("------------------------------");
        
        System.out.println("Pressione enter para continuar...");
        scan.nextLine();
    }
    
    public static void solicitacoes(user usuario, db DB) throws SQLException {
        
        Scanner scan = new Scanner(System.in);
        
        int opcao;
        boolean ordem_feita = false;
        boolean ordem_recebida = false;
        
        Connection conn = DB.conn;
        
        String sql = "SELECT o.id,u1.id as requisitante_id,u1.nome as requisitante,u2.id as requisitado_id,u2.nome as requisitado ,o.valor,o.data,o.mensagem     " +
                     "FROM ordens o\n" +
                     "INNER JOIN users u1 on u1.id = o.requisitante_id\n" +
                     "INNER JOIN users u2 on u2.id = o.requisitado_id\n" +
                     "WHERE requisitante_id = ? or requisitado_id = ?";  
          
        PreparedStatement stmt = conn.prepareStatement(sql);  
        stmt.setInt(1, usuario.id);  
        stmt.setInt(2, usuario.id); 
        ResultSet rs  = stmt.executeQuery();
        
        
        if (!rs.next()) {
            System.err.println("Nenhuma solicitação");
        } else {
            System.out.println("Solicitações feitas:");
            while (rs.next()) {
                System.out.println("------------------------------");
                if (rs.getInt("requisitante_id") == usuario.id) {
                    System.out.println("ID= "+ rs.getInt("id"));
                    System.out.println("Destinatário "+rs.getString("requisitado"));
                    System.out.println("Data: "+rs.getString("data"));
                    System.out.println("Valor: R$"+rs.getString("valor"));
                    System.out.println("Mensagem: "+rs.getString("mensagem"));
                    
                    ordem_feita = true;
                }
            }
            System.out.println("------------------------------");
            
            if (ordem_feita) {
                System.out.println("Se deseja cancelar uma solicitação digite o ID respectivo ou 0 para continuar");

                opcao = scan.nextInt();
            
                if (opcao != 0) {
                    DB.deleteOrdem(opcao);
                }
            }
            
            rs.beforeFirst();
            
            System.out.println("Solicitações recebidas:");
            while (rs.next()) {
                System.out.println("------------------------------");
                if (rs.getInt("requisitado_id") == usuario.id) {
                    System.out.println("ID= "+ rs.getInt("id"));
                    System.out.println("Pedida por: "+rs.getString("requisitado"));
                    System.out.println("Data: "+rs.getString("data"));
                    System.out.println("Valor: R$"+rs.getString("valor"));
                    System.out.println("Mensagem: "+rs.getString("mensagem"));
                    
                    ordem_recebida = true;
                }
                
                 if (ordem_recebida) {
                    System.out.println("Para aceitar ou recusar uma ordem digite o seu ID ou 0 para continuar");

                    opcao = scan.nextInt();
            
                    if (opcao != 0) {
                        DB.aceitarOrdem(opcao,DB,usuario);
                    }
                }
            }
            System.out.println("------------------------------");
       }
    }
}



