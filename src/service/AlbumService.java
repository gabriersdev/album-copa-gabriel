package service;

import model.Album;
import model.Team;
import utils.Util;

public class AlbumService {
    public void printSummary(Album album) {
        int total = 0;
        int numTeams = album.getNumTeams();
        int[] totals = new int[numTeams];

        for (int i = 0; i < numTeams; i++) {
            Team team = album.getTeam(i);
            total += team.getTotalStickers();
            totals[i] = team.getTotalStickers();
        }

        System.out.printf("%nÁlbum %d: %d figurinhas %n", album.getId(), total);

        // Impressão agradável
        for (int i = 0; i < numTeams; i++) System.out.printf("+ %d da seleção do(a) %s%n", totals[i], album.getTeam(i).getName());
        System.out.println();
    }

    public void printDetails(Album album) {
        int total = 0, missing = 0, unique = 0, repeated = 0, percentage = 0;
        int numTeams = album.getNumTeams();
        int cols = album.getNumPlayersPerTeam();

        for (int i = 0; i < numTeams; i++) {
            Team team = album.getTeam(i);
            total += team.getTotalStickers();
            missing += team.getMissingStickersCount();
            unique += team.getUniqueStickersCount();
            repeated += team.getRepeatedStickersCount();
        }

        // Define a percentagem
        if (unique + repeated > 0) percentage = (int) ((double) (unique * 100) / (double) (unique + repeated));

        Util.printStringMatrix(new String[][]{
                {"%nÁlbum %s%% concluído:%n", percentage + ""},
                {"- Quantidade total de figurinhas: %s.%n", total + ""},
                {"- Você completou as figurinhas de %s jogadores.%n", unique + ""},
                {"- Você tem %s figurinhas repetidas.%n", repeated + ""},
                {"- Faltam %s jogadores para completar o álbum.%n%n", missing + ""}
        }, 3);

        System.out.printf("%-15s", "Seleção");
        for (int j = 1; j <= cols; j++) System.out.printf("%sJ%-3d", " ", j);

        System.out.println();
        System.out.println("-".repeat(15 + cols * 5));

        // Impressão agradável
        for (int i = 0; i < numTeams; i++) {
            Team team = album.getTeam(i);
            System.out.printf("%-15s", team.getName());
            for (int j = 0; j < cols; j++) System.out.printf("%s%-4d", " ", team.getSticker(j).getQuantity());
            System.out.println();
        }

        System.out.printf("%nFigurinhas repetidas: %n");
        for (int i = 0; i < numTeams; i++) {
            Team team = album.getTeam(i);
            for (int j = 0; j < cols; j++) {
                if (team.getSticker(j).hasRepeated()) System.out.printf("- %d figurinhas do jogador #%d (%s) %n", team.getSticker(j).getRepeatedCount(), j + 1, team.getName());
            }
        }

        System.out.printf("%nFigurinhas que faltam: %n");
        for (int i = 0; i < numTeams; i++) {
            Team team = album.getTeam(i);
            for (int j = 0; j < cols; j++) {
                if (team.getSticker(j).isMissing()) System.out.printf("- Jogador #%d (%s) %n", j + 1, team.getName());
            }
        }

        // System.out.printf("%n%n%n");
    }
}
