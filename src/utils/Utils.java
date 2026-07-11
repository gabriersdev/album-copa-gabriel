package utils;

public class Utils {
    // Funciona como o operador "" * N do Python, para multiplicar letras ou itens e retornar numa string concatenada
    public static String mult(String letter, int count) {
        return String.valueOf(letter).repeat(Math.max(0, count));
    }

    // Percorre uma matriz formada de strings e mostra os itens dele
    public static void showVetorItems(String[][] vetor, int tipoImpressao) {
        for (String[] v : vetor) {
            if (tipoImpressao == 3) System.out.printf(v[0], v[1]);
            else System.out.println("Parâmetro não implementado.");
        }
    }

    // Percorre um vetor formado de strings e mostra os itens dele
    public static void showVetorItems(String[] vetor, int tipoImpressao) {
        for (String v : vetor) {
            switch (tipoImpressao) {
                case 1 -> System.out.print(v);
                case 2 -> System.out.println(v);
                default -> System.out.println("Parâmetro não implementado.");
            }
        }
    }
}
