package project.example;

import java.io.*; //Utilização das classes de leitura de arquivos

public class Album {
    static int linhas; //declarando variáveis globais 
    static int colunas;
    static String[] selecoes;
    static int[][] matriz;

    //=======================
    // Método de leitura do arquivo
    //=======================
    static void carregarAlbum(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {

            // 1ª linha: dimensões
            //Lê uma linha do arquivo com o método da classe bufferedReader br e divide strings diferentes utilizando o split
            String[] dimensoes = br.readLine().trim().split("\\s+");
            linhas = Integer.parseInt(dimensoes[0]); //converte a string para inteiro
            colunas = Integer.parseInt(dimensoes[1]); //converte a string para inteiro

            selecoes = new String[linhas];
            matriz = new int[linhas][colunas];

            // próximas M linhas: nomes das seleções
            for (int i = 0; i < linhas; i++) {
                selecoes[i] = br.readLine().trim(); //Lê os nomes de cada seleção.
                //o método trim serve para remover espaços em branco antes e depois da string lida
            }

            // próximas M linhas: valores da matriz que representam as figrinhas
            for (int i = 0; i < linhas; i++) {
                String[] valores = br.readLine().trim().split("\\s+");
                //Leitura similar à linha 14
                for (int j = 0; j < colunas; j++) {
                    matriz[i][j] = Integer.parseInt(valores[j]);//Converte valores para inteiro
                }
            }

            System.out.println("Álbum carregado com sucesso!");
            System.out.printf("Seleções: %d | Jogadores por seleção: %d%n", linhas, colunas);
        }
    }

    //=======================
    // Método para imprimir o álbum atual
    //=======================
    static void exibirMatriz() {
        if (matriz == null) {
            System.out.println("Nenhum álbum carregado.");
            return;
        }
        // cabeçalho com números dos jogadores
        System.out.println("ALBUM ATUAL");
        System.out.printf("%-15s", "Seleção");
        //Imprime a palavra e reserva 15 espaços para impressão, alinhando à esquerda
        for (int j = 1; j <= colunas; j++) {
            System.out.printf(" J%-3d", j);
        }
        System.out.println();

        // linha separadora
        //Imprime 15 hifens e depois mais 5 hifens por coluna da matriz
        System.out.println("-".repeat(15 + colunas * 5));

        // linhas da matriz
        for (int i = 0; i < linhas; i++) {
            System.out.printf("%-15s", selecoes[i]);
            for (int j = 0; j < colunas; j++) {
                System.out.printf(" %-4d", matriz[i][j]);
            }
            System.out.println();
        }
    }

    //=======================
    // Método main
    //=======================
    public static void main(String[] args) {

        try {
            carregarAlbum("C:/Users/bruno/Desktop/album.txt");
            exibirMatriz();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido no arquivo: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erro de leitura: " + e.getMessage());
        }

    }
}