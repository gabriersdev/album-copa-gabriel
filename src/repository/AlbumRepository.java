package repository;

import model.Album;
import model.Team;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AlbumRepository {

    public Album loadFromFile(int id, String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String[] dimensions = br.readLine().trim().split("\\s+");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);

            Album album = new Album(id);
            album.setNumPlayersPerTeam(cols);

            String[] teamNames = new String[rows];
            for (int i = 0; i < rows; i++) {
                teamNames[i] = br.readLine().trim();
            }

            for (int i = 0; i < rows; i++) {
                Team team = new Team(teamNames[i], cols);
                String[] values = br.readLine().trim().split("\\s+");
                for (int j = 0; j < cols; j++) {
                    team.setStickerQuantity(j, Integer.parseInt(values[j]));
                }
                album.addTeam(team);
            }

            return album;
        }
    }

    public void saveToFile(Album album, String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            int rows = album.getNumTeams();
            int cols = album.getNumPlayersPerTeam();
            
            bw.write(rows + " " + cols);
            bw.newLine();
            
            for (int i = 0; i < rows; i++) {
                bw.write(album.getTeam(i).getName());
                bw.newLine();
            }
            
            for (int i = 0; i < rows; i++) {
                Team team = album.getTeam(i);
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < cols; j++) {
                    sb.append(team.getSticker(j).getQuantity());
                    if (j < cols - 1) {
                        sb.append(" ");
                    }
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }
}
