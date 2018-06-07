package pagpal;

import java.sql.SQLException;
import java.util.Scanner;

public class PagPal {
    
    public static void menuInicial() throws SQLException {
    
        int escolha;
        boolean loop = true;
        String email;

        db DB = new db();   
        Scanner scan = new Scanner(System.in);

        while (loop) {            
                System.out.println("\nEscolha uma opção:");
                System.out.println("1-Cadastrar");
                System.out.println("2-Logar");
                System.out.println("3-Sair");

                escolha = scan.nextInt();

                switch (escolha) {
                    case 1:
                        user.cadastro(DB);
                        break;
                    case 2:
                        email = user.login(DB);
                        if (email != null) {
                            user usuario = new user(email);
                            menuLogado(usuario, DB);
                        }
                        break;
                    case 3:
                        loop = false;
                        break;
                    default:
                        System.err.println("Opção invalida");
                        break;
                }
        }
}
    
public static void menuLogado(user usuario, db DB) throws SQLException {
    
    int escolha;
    boolean loop = true;
    
    Scanner scan = new Scanner(System.in);

    while (loop) {            
            System.out.println("\nEscolha uma opção:");
            System.out.println("1-Enviar dinheiro");
            System.out.println("2-Adicionar créditos");
            System.out.println("3-Solicitar dinheiro");
            System.out.println("4-Histórico");
            System.out.println("5-Solicitações");
            System.out.println("6-Editar cadastro");
            System.out.println("7-Sair");
            
            escolha = scan.nextInt();
            
            switch (escolha) {
                case 1:
                    dinheiro.enviar(usuario,DB);
                    break;
                case 2:
                    dinheiro.depositar(usuario,DB);
                    break;
                case 3:
                    dinheiro.solicitar(usuario,DB);
                    break;
                case 4:
                    dinheiro.historico(usuario,DB);
                    break;
                case 5:
                    dinheiro.solicitacoes(usuario,DB);
                    break;    
                case 6:
                    user.editar(usuario,DB);
                    break;    
                case 7:
                    loop = false;
                    break;
                default:
                    System.out.println("Opção invalida");
                    break;
            }
        }
}

    public static void main(String[] args) throws SQLException{
        
        menuInicial();
        
        System.out.println("Adeus!");
    }
}
