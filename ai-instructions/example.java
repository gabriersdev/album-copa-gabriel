package exercise07;

import java.util.Scanner;

public class Gabriel16 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        /* Elabore um algoritmo que faça a conversão de um número binário de N bits digitados pelo usuário para o
        número na base octal, base decimal e base hexadecimal. Valide se o valor informado na entrada só
        possui 0’s ou 1’s, ou seja, se é mesmo um número binário. Não utilize funções pré-definidas no
        JAVA. */

        long bin, hex, dec = 0, octal = 0, dig, bit, pos = 0, mult = 1;
        String hexString = "", hexStringReverse = "";

        System.out.println("Digite o número binário: ");
        bin = in.nextLong();

        while (bin != 0) {
            bit = bin % 10;

            if (bit != 0 && bit != 1) {
                System.out.println("Não é binário:");
                return;
            }

            bin = bin / 10;
            dec += (long) (bit * Math.pow(2, pos));
            pos += 1;
        }

        System.out.printf("%nO valor convertido em DECIMAL é %d%n", dec);
        hex = dec;

        // Para converter DECIMAL para OCTAL
        while (dec != 0) {
            dig = dec % 8;

            dec = dec / 8;
            octal = octal + mult * dig;
            mult *= 10;
        }

        System.out.printf("%nO valor convertido em OCTAL é %d%n", octal);

        while (hex != 0) {
            dig = hex % 16;

            if (dig < 10) hexString += String.valueOf(dig);

            else {
                if (dig == 10) hexString += "A";
                else if (dig == 11) hexString += "B";
                else if (dig == 12) hexString += "C";
                else if (dig == 13) hexString += "D";
                else if (dig == 14) hexString += "E";
                else hexString += "F";
            }

            hex = hex / 16;
        }

        for (int i = (hexString.length() - 1); i >= 0; i--) hexStringReverse += hexString.split("")[i];
        System.out.printf("%nO valor convertido em HEXADECIMAL é %s%n", hexStringReverse);
    }
}
