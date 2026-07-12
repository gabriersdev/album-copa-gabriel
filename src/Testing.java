import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;
import utils.Util;

public class Testing {
    public static void printMSGVermelho(String mensagem) {
        String reset = "\u001B[0m";
        String fundoVermelho = "\u001B[41m";
        String textoBranco = "\u001B[97m";

        System.out.println(fundoVermelho + textoBranco + mensagem + reset);
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println(" Executando testes... ");
        System.out.println(" Iniciando testes de Main.java...  ");
        System.out.println();

        Scanner in = new Scanner(System.in);

        printMSGVermelho("Vamos iniciar o teste. Existe um delay visual entre cada interação do script de teste e o script principal.");
        System.out.println();
        printMSGVermelho("Não se assuste. Para para a execução digite no terminal CTRL + C (para execução no Windows, exceto via substema Linux).");
        System.out.println();

        System.out.print("Digite 1 para iniciar a execução dos testes: ");
        if (in.nextInt() == 1) testarComDelayVisual();
        else System.out.println("Script de chamada para execução de teste finalizado.");
    }

    private static void testarComDelayVisual() {
        // A lista exata de interações que faremos, item por item
        String[] simulacaoDeInputs = {
                "0", // Menu inicial: Digitar informações (Álbum 1)
                "2", // Quantas seleções?
                "3", // Jogadores em cada?

                // Seleção A
                "A", // Qual o nome?
                "1", // J1=1
                "0", // J2=0 (Falta)
                "2", // J3=2 (Repetida)

                // Seleção B
                "B", // Qual o nome?
                "0", // J1=0 (Falta)
                "1", // J2=1
                "1", // J3=1

                // Menu: Adicionar 2º álbum
                "3",
                "0", // Digitar informações
                "2", // Quantas seleções?
                "3", // Jogadores em cada?

                // Seleção A (Álbum 2)
                "A",
                "1", // J1=1
                "2", // J2=2 (Tem repetida)
                "0", // J3=0 (Falta)

                // Seleção B (Álbum 2)
                "B",
                "1", // J1=1
                "1", // J2=1
                "0", // J3=0

                // Menu: Comparar álbuns
                "4",

                // Menu: Adicionar figurinhas
                "1",
                "1", // Qual álbum? -> 1
                "A", // Seleção -> A
                "2", // Número do jogador -> 2
                "1", // Quanto adicionar -> 1

                // Menu: Imprimir álbum
                "0",
                "1", // Álbum 1

                // Menu: Sair
                "5"
        };

        try {
            // Cria um "cano" para conectar nossa Thread simuladora com o Scanner do Java
            PipedOutputStream outputParaScanner = new PipedOutputStream();
            PipedInputStream inputDoScanner = new PipedInputStream(outputParaScanner);

            // Sobrescreve o System.in do sistema para usar o nosso "cano"
            System.setIn(inputDoScanner);
            Util.inputScanner = new java.util.Scanner(System.in);

            // Essa Thread vai funcionar como um "fantasma" digitando as coisas com intervalo
            Thread digitadorFantasma = new Thread(() -> {
                try {
                    // Dar um pequeno delay inicial para a tela renderizar
                    Thread.sleep(1000);

                    for (String input : simulacaoDeInputs) {
                        // Espera 1000 milissegundos antes de digitar a próxima coisa
                        // para que você consiga ler e acompanhar
                        Thread.sleep(1000);

                        // Imprime visualmente na tela para você saber o que a automação digitou
                        // O \u001B[32m deixa a letra VERDE nos terminais suportados para diferenciar
                        System.out.print("\u001B[32m" + input + "\u001B[0m\n");

                        // Envia para o Scanner do Main ler
                        outputParaScanner.write((input + "\n").getBytes());
                        outputParaScanner.flush();
                    }

                    outputParaScanner.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Inicia a digitação automatizada em segundo plano
            digitadorFantasma.start();

            // Roda o código original do sistema na Thread principal
            Main.main(new String[]{});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
