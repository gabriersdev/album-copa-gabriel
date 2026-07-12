package controller;

import model.Album;
import model.Team;
import repository.AlbumRepository;
import service.AlbumService;
import service.TradeService;
import utils.Util;

import java.io.FileNotFoundException;
import java.io.IOException;

// TODO - quebrar este arquivo em outros, para distribuir melhor a responsabilidade dos métodos, reduzir a repetição de código e concentrar aqui regras de negócio
public class Controller {
    static Album album1 = null;
    static Album album2 = null;
    static AlbumService albumService = new AlbumService();
    static TradeService tradeService = new TradeService();
    static AlbumRepository albumRepository = new AlbumRepository();

    public static Album getAlbum(int id) {
        return id == 1 ? album1 : album2;
    }

    public static void setAlbum(int id, Album album) {
        if (id == 1) album1 = album;
        else album2 = album;
    }

    public static void tryReadFile(int id) {
        System.out.print("Insira o diretório do arquivo: ");
        do {
            try {
                Album newAlbum = albumRepository.loadFromFile(id, Util.inputScanner.next());
                setAlbum(id, newAlbum);
                System.out.println("Álbum carregado com sucesso!");
                break;
            } catch (FileNotFoundException e) {
                System.out.printf("%nArquivo não encontrado: %s %nTente de novo: ", e.getMessage());
            } catch (NumberFormatException e) {
                System.out.printf("%nFormato inválido de arquivo: %s %nTente de novo: ", e.getMessage());
            } catch (IOException e) {
                System.out.printf("%nErro de leitura: %s %nTente de novo: ", e.getMessage());
            }
        } while (true);
    }

    public static void inputData(int id) {
        System.out.print("Quantas seleções há? ");
        int teamsCount = Util.readPositiveInteger("\nInsira apenas números, tente novamente: ", "\nInsira valores positivos apenas:\n");

        System.out.print("Quantos jogadores há em cada? ");
        int playersPerTeam = Util.readPositiveInteger("\nInsira apenas números, tente novamente: ", "\nInsira valores positivos apenas, tente novamente:\n");

        Album newAlbum = new Album(id);
        newAlbum.setNumPlayersPerTeam(playersPerTeam);
        System.out.printf("%n");

        for (int i = 0; i < teamsCount; i++) {
            System.out.printf("Qual o nome da seleção #%d? ", (i + 1));
            String teamName = Util.inputScanner.next();

            do {
                if (newAlbum.hasTeam(teamName)) {
                    System.out.printf("%nSeleção já cadastrada. Tente novamente: %n");
                    teamName = Util.inputScanner.next();
                } else break;
            } while (true);

            Team team = new Team(teamName, playersPerTeam);

            for (int j = 0; j < playersPerTeam; j++) {
                System.out.printf("Quantidade de figurinhas do jogador %d da seleção %s? ", j + 1, teamName);
                int quantity = Util.readPositiveInteger(
                        String.format("%nInsira apenas números. Tente novamente: %n"),
                        String.format("%nInsira apenas um valor positivo, tente novamente: %n")
                );
                team.setStickerQuantity(j, quantity);
            }
            newAlbum.addTeam(team);
            System.out.printf("%n");
        }

        setAlbum(id, newAlbum);
        System.out.printf("%nÁlbum carregado com sucesso!");
    }

    public static void addOrRemoveStickers(int id, boolean isAddAction) {
        Album album = getAlbum(id);
        if (album == null) return;

        System.out.print("Em qual seleção? ");
        String teamName = Util.inputScanner.next();
        Team team = album.getTeam(teamName);

        do {
            if (team != null) break;
            else {
                System.out.print("O time não existe no álbum, tente de novo: ");
                teamName = Util.inputScanner.next();
                team = album.getTeam(teamName);
            }
        } while (true);

        System.out.print("Qual o número do jogador? ");
        int playerInput = Util.readIntegerInRange(
                "Insira apenas números, tente de novo: ",
                String.format("O numero máximo de jogador é %d, tente de novo: ", album.getNumPlayersPerTeam()),
                1, album.getNumPlayersPerTeam()
        );

        int playerIndex = playerInput - 1;

        if (isAddAction) {
            System.out.print("Quanto adicionar? ");
            int amountToAdd = Util.readPositiveInteger("Insira apenas números: ", "Insira apenas números positivos: ");
            team.addStickerQuantity(playerIndex, amountToAdd);
        } else {
            System.out.print("Quanto retirar? ");
            do {
                int amountToRemove = Util.readPositiveInteger("Insira apenas números: ", "Insira apenas números positivos: ");
                try {
                    team.removeStickerQuantity(playerIndex, amountToRemove);
                    break;
                } catch (RuntimeException e) {
                    System.out.print("A quantidade de figurinhas não pode ser negativa. Tente novamente: ");
                }
            } while (true);
        }

        nextStep();
    }

    public static int getDesiredOption() {
        System.out.print("Informe: ");
        return Util.readIntegerInRange(
                "Erro! Insira apenas números, tente de novo: ",
                "Valor digitado não está nas opções, tente de novo: ",
                0, 1
        );
    }

    public static void handleSecondAlbum() {
        Util.printStringArray(new String[]{
                "Como adicionar um álbum?",
                "- [0] Digitar as informações",
                "- [1] Ler um arquivo"
        }, 2);

        switch (getDesiredOption()) {
            case 0:
                inputData(2);
                break;
            case 1:
                tryReadFile(2);
                break;
        }
        nextStep();
    }

    public static void nextStep() {
        System.out.printf("%n%s%n", Util.repeatString(".", 30));
        if (album1 != null) albumService.printSummary(album1);

        if (album2 != null) {
            System.out.printf("%n%n");
            albumService.printSummary(album2);
        }

        System.out.printf("%n%s%n", Util.repeatString(".", 30));
        System.out.printf("%nO que deseja fazer agora?%n");

        Util.printStringArray(new String[]{
                "- [0] Imprimir álbuns",
                "- [1] Adicionar figurinhas",
                "- [2] Remover figurinhas",
                "- [3] Adicionar segundo álbum",
                "- [4] Comparar álbuns",
                "- [5] Sair do Programa",
        }, 2);

        System.out.printf("%nDigite a opção: ");

        int option = Util.readIntegerInRange(
                "Erro! Insira apenas números. Tente novamente: ",
                "Valor digitado não está nas opções, tente novamente: ",
                0, 5
        );

        switch (option) {
            case 0 -> selectAlbum(0);
            case 1 -> selectAlbum(1);
            case 2 -> selectAlbum(2);
            case 3 -> {
                if (album2 != null) {
                    System.out.printf("%n >>> Já existe um segundo álbum <<< %n%n");
                    nextStep();
                } else handleSecondAlbum();
            }
            case 4 -> {
                if (album2 == null) {
                    System.out.printf("%nO segundo álbum não foi cadastrado%n");
                    nextStep();
                } else if (album1.getNumPlayersPerTeam() != album2.getNumPlayersPerTeam()) {
                    System.out.printf("%nNúmero de jogadores é diferentes em cada álbum%n");
                    nextStep();
                } else {
                    tradeService.compareAlbums(album1, album2);
                    nextStep();
                }
            }
            case 5 -> System.exit(0);
        }
    }

    public static void selectAlbum(int actionId) {
        Util.printStringArray(new String[]{
                "Qual álbum você deseja selecionar para continuar a ação?",
                "- [1] Álbum 1",
                "- [2] Álbum 2"
        }, 2);

        int albumId = Util.readIntegerInRange("\nErro! Insira apenas números, tente novamente: ", "\nO valor digitado não está nas opções. Tente novamente: ", 1, 2);

        if (albumId == 2 && album2 == null) {
            System.out.printf("%nO Segundo álbum não foi cadastrado.%n");
            nextStep();
            return;
        }

        switch (actionId) {
            case 0 -> albumService.printDetails(getAlbum(albumId));
            case 1 -> addOrRemoveStickers(albumId, true);
            case 2 -> addOrRemoveStickers(albumId, false);
        }

        if (actionId == 0) nextStep();
    }
}
