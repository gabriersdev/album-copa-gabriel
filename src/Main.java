import controller.Controller;
import utils.Util;

public class Main {
    public static void main(String[] args) {
        Util.printStringArray(new String[]{
                "Você deseja:",
                "- [0] Digitar as informações",
                "- [1] Ler um arquivo"
        }, 2);

        switch (Controller.getDesiredOption()) {
            case 0 -> Controller.inputData(1);
            case 1 -> Controller.tryReadFile(1);
        }

        Controller.nextStep();
    }
}
