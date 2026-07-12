package service;

import exceptions.InvalidAlbumDimensionException;
import model.Album;
import model.Team;

public class TradeService {
    public void compareAlbums(Album album1, Album album2) {
        if (album1.getNumPlayersPerTeam() != album2.getNumPlayersPerTeam()) {
            throw new InvalidAlbumDimensionException("Número de jogadores é diferente em cada álbum.");
        }

        System.out.printf("%n >>> Comparação <<< %n");
        int count = 0;
        int numTeams = album1.getNumTeams();

        for (int i = 0; i < numTeams; i++) {
            boolean newline = false;
            Team team1 = album1.getTeam(i);
            Team team2 = album2.getTeam(team1.getName());

            if (team2 != null) {
                System.out.println(team1.getName().toUpperCase());

                int numPlayers = album1.getNumPlayersPerTeam();
                for (int k = 0; k < numPlayers; k++) {
                    if (team1.getSticker(k).isMissing() && team2.getSticker(k).hasRepeated()) {
                        System.out.printf("Você pode receber a figurinha do JOGADOR %d do outro álbum%n", k + 1);
                        count++;
                        newline = true;
                    }

                    //
                    else if (team1.getSticker(k).hasRepeated() && team2.getSticker(k).isMissing()) {
                        System.out.printf("Você pode dar a figurinha do JOGADOR %d pro outro álbum%n", k + 1);
                        count++;
                        newline = true;
                    }

                    //
                    else if (k == numPlayers - 1 && !newline) {
                        System.out.printf("Sem possibilidade de troca...%n");
                        count++;
                        newline = true;
                    }
                }
            }

            if (newline) System.out.printf("%n");
        }

        if (count == 0) System.out.printf("Não há o que comparar...%n%n%n");
    }
}
