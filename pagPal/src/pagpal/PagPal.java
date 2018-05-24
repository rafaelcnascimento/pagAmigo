package pagpal;

import java.sql.SQLException;
import java.util.Scanner;

public class PagPal {

    public static void main(String[] args) throws SQLException {

       int escolha;
       boolean loop = true;
       
       Scanner scan = new Scanner(System.in);
       
        while (loop) {            
            System.out.println("\nEscolha uma opção:");
            System.out.println("1-Cadastrar");
            System.out.println("2-Logar");
            System.out.println("3-Sair");
            
            escolha = scan.nextInt();
            
            switch (escolha) {
                case 1:
                    user.cadastro();
                    break;
                case 2:
                    System.out.println("Login");
                    break;
                case 3:
                    loop = false;
                    break;
                default:
                    System.out.println("Opção invalida");
                    break;
            }
        }
        
        System.out.println("Adeus!");
    }
}
