package utils;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Util {
    public static Scanner inputScanner = new Scanner(System.in);

    public static String repeatString(String letter, int count) {
        return String.valueOf(letter).repeat(Math.max(0, count));
    }

    public static void printStringMatrix(String[][] matrix, int printType) {
        for (String[] v : matrix) {
            if (printType == 3) {
                System.out.printf(v[0], v[1]);
            } else {
                System.out.println("Parâmetro não implementado.");
            }
        }
    }

    public static void printStringArray(String[] array, int printType) {
        for (String v : array) {
            switch (printType) {
                case 1 -> System.out.print(v);
                case 2 -> System.out.println(v);
                default -> System.out.println("Parâmetro não implementado.");
            }
        }
    }

    public static int readInteger(String errorMessageLetter) {
        do {
            try {
                return inputScanner.nextInt();
            } catch (InputMismatchException err) {
                System.out.print(errorMessageLetter);
                inputScanner.next();
            }
        } while (true);
    }

    public static int readPositiveInteger(String errorMessageLetter, String errorMessageNegative) {
        do {
            int value = readInteger(errorMessageLetter);
            if (value < 0) {
                System.out.print(errorMessageNegative);
            } else {
                return value;
            }
        } while (true);
    }

    public static int readIntegerInRange(String errorMessageLetter, String errorMessageInvalid, int min, int max) {
        do {
            int value = readInteger(errorMessageLetter);
            if (value < min || value > max) {
                System.out.print(errorMessageInvalid);
            } else {
                return value;
            }
        } while (true);
    }
}
