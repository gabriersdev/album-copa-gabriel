package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Album;
import model.Team;
import model.Sticker;
import repository.AlbumRepository;
import utils.JsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

// TODO - quebrar este arquivo em outros, para distribuir melhor a responsabilidade dos métodos
public class ApiHandler implements HttpHandler {
    private AlbumRepository repository = new AlbumRepository();
    private String file1 = "database/album-database-1.txt";
    private String file2 = "database/album-database-2.txt";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // CORS Headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            Album album1 = repository.loadFromFile(1, file1);
            Album album2 = repository.loadFromFile(2, file2);

            String response = "";

            if ("GET".equalsIgnoreCase(method)) {
                if ("/api/initial-data".equals(path)) {
                    response = getInitialData(album1, album2);
                } else if (path.startsWith("/api/selection/")) {
                    String selectionName = path.substring("/api/selection/".length());
                    response = getSelectionData(album1, album2, selectionName);
                } else if ("/api/comparison".equals(path)) {
                    response = getComparisonData(album1, album2);
                } else {
                    sendError(exchange, 404, "Endpoint não encontrado.");
                    return;
                }
            } else if ("POST".equalsIgnoreCase(method) && "/api/trade".equals(path)) {
                InputStream is = exchange.getRequestBody();
                String body = new String(is.readAllBytes());
                response = handleTrade(album1, album2, body);
            } else {
                sendError(exchange, 405, "Método não permitido.");
                return;
            }

            byte[] bytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();

        } catch (Exception e) {
            e.printStackTrace();
            sendError(exchange, 500, "Erro interno do servidor.");
        }
    }

    private void sendError(HttpExchange exchange, int code, String msg) throws IOException {
        String json = "{\"error\": " + JsonBuilder.escape(msg) + "}";
        byte[] bytes = json.getBytes("UTF-8");
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private String getInitialData(Album a1, Album a2) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        List<String> teamNames = new ArrayList<>();
        for (int i = 0; i < a1.getNumTeams(); i++) {
            teamNames.add(a1.getTeam(i).getName());
        }
        sb.append("\"selections\": ").append(JsonBuilder.stringListToJson(teamNames)).append(",");

        String firstTeam = teamNames.isEmpty() ? "" : teamNames.get(0);
        sb.append("\"currentSelection\": ").append(JsonBuilder.escape(firstTeam)).append(",");

        sb.append("\"stickers\": ").append(getSelectionData(a1, a2, firstTeam));

        sb.append("}");
        return sb.toString();
    }

    private String getSelectionData(Album a1, Album a2, String teamName) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Team t1 = a1.getTeam(teamName);
        Team t2 = a2.getTeam(teamName);

        if (t1 != null && t2 != null) {
            int numPlayers = a1.getNumPlayersPerTeam();
            for (int i = 0; i < numPlayers; i++) {
                Sticker s1 = t1.getSticker(i);
                Sticker s2 = t2.getSticker(i);

                sb.append("{");
                sb.append("\"number\": \"").append(i + 1).append("\",");
                sb.append("\"team\": ").append(JsonBuilder.escape(t1.getName())).append(",");
                sb.append("\"album1Has\": ").append(s1.isPasted()).append(",");
                sb.append("\"album1IsDuplicate\": ").append(s1.hasRepeated()).append(",");
                sb.append("\"album2Has\": ").append(s2.isPasted()).append(",");
                sb.append("\"album2IsDuplicate\": ").append(s2.hasRepeated());
                sb.append("}");

                if (i < numPlayers - 1) sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String getComparisonData(Album a1, Album a2) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"album1\": ").append(buildAlbumStats(a1, "Álbum #1")).append(",");
        sb.append("\"album2\": ").append(buildAlbumStats(a2, "Álbum #2")).append(",");

        sb.append("\"commonStickers\": [");
        boolean firstCommon = true;
        List<String> trades = new ArrayList<>();

        int numTeams = a1.getNumTeams();
        int numPlayers = a1.getNumPlayersPerTeam();

        for (int i = 0; i < numTeams; i++) {
            Team t1 = a1.getTeam(i);
            Team t2 = a2.getTeam(t1.getName());
            if (t2 == null) continue;

            for (int k = 0; k < numPlayers; k++) {
                Sticker s1 = t1.getSticker(k);
                Sticker s2 = t2.getSticker(k);

                if (s1.isPasted() && s2.isPasted()) {
                    if (!firstCommon) sb.append(",");
                    sb.append("{ \"number\": ").append(k + 1).append(", \"team\": ").append(JsonBuilder.escape(t1.getName())).append("}");
                    firstCommon = false;
                }

                if (s1.hasRepeated() && s2.isMissing()) {
                    trades.add("{ \"number\": " + (k + 1) + ", \"team\": " + JsonBuilder.escape(t1.getName()) + ", \"from\": \"Álbum #1\", \"to\": \"Álbum #2\" }");
                } else if (s2.hasRepeated() && s1.isMissing()) {
                    trades.add("{ \"number\": " + (k + 1) + ", \"team\": " + JsonBuilder.escape(t2.getName()) + ", \"from\": \"Álbum #2\", \"to\": \"Álbum #1\" }");
                }
            }
        }
        sb.append("],");

        sb.append("\"possibleTrades\": [");
        for (int i = 0; i < trades.size(); i++) {
            sb.append(trades.get(i));
            if (i < trades.size() - 1) sb.append(",");
        }
        sb.append("]");

        sb.append("}");
        return sb.toString();
    }

    private String buildAlbumStats(Album album, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"name\": ").append(JsonBuilder.escape(name)).append(",");

        int totalStickers = 0;
        int missingStickers = 0;
        int uniqueStickers = 0;

        for (int i = 0; i < album.getNumTeams(); i++) {
            Team t = album.getTeam(i);
            missingStickers += t.getMissingStickersCount();
            uniqueStickers += t.getUniqueStickersCount();
            totalStickers += t.getNumPlayers();
        }

        int progress = 0;
        if (totalStickers > 0) progress = (int) (((double) uniqueStickers / totalStickers) * 100);

        sb.append("\"missing\": ").append(missingStickers).append(",");
        sb.append("\"progress\": ").append(progress).append(",");

        sb.append("\"teams\": [");
        for (int i = 0; i < album.getNumTeams(); i++) {
            Team t = album.getTeam(i);
            int tProgress = 0;
            if (t.getNumPlayers() > 0) {
                tProgress = (int) (((double) t.getUniqueStickersCount() / t.getNumPlayers()) * 100);
            }
            sb.append("{");
            sb.append("\"name\": ").append(JsonBuilder.escape(t.getName())).append(",");
            sb.append("\"progress\": ").append(tProgress).append(",");
            sb.append("\"missing\": ").append(t.getMissingStickersCount());
            sb.append("}");
            if (i < album.getNumTeams() - 1) sb.append(",");
        }
        sb.append("]");

        sb.append("}");
        return sb.toString();
    }

    private String handleTrade(Album a1, Album a2, String body) throws IOException {
        String stickerNumStr = extractJsonValue(body, "stickerNumber");
        String teamName = extractJsonValue(body, "team");
        String fromAlbum = extractJsonValue(body, "fromAlbum");
        String toAlbum = extractJsonValue(body, "toAlbum");

        if (stickerNumStr == null || teamName == null || fromAlbum == null || toAlbum == null) {
            return "{\"success\": false, \"message\": \"Dados incompletos.\"}";
        }

        int stickerNumber;
        try {
            stickerNumber = Integer.parseInt(stickerNumStr);
        } catch (NumberFormatException e) {
            return "{\"success\": false, \"message\": \"Número de figurinha inválido.\"}";
        }

        int playerIndex = stickerNumber - 1;

        Album source = fromAlbum.equals("Álbum #1") ? a1 : a2;
        Album dest = toAlbum.equals("Álbum #2") ? a2 : a1;

        if (source == dest) {
            return "{\"success\": false, \"message\": \"Álbuns de origem e destino devem ser diferentes.\"}";
        }

        Team sourceTeam = source.getTeam(teamName);
        Team destTeam = dest.getTeam(teamName);

        if (sourceTeam == null || destTeam == null) {
            return "{\"success\": false, \"message\": \"Seleção não encontrada.\"}";
        }

        Sticker sourceSticker = sourceTeam.getSticker(playerIndex);
        Sticker destSticker = destTeam.getSticker(playerIndex);

        if (!sourceSticker.hasRepeated()) {
            return "{\"success\": false, \"message\": \"Erro: A troca não pôde ser realizada. O álbum de origem não possui esta figurinha como duplicata.\"}";
        }

        sourceTeam.removeStickerQuantity(playerIndex, 1);
        destTeam.addStickerQuantity(playerIndex, 1);

        repository.saveToFile(a1, file1);
        repository.saveToFile(a2, file2);

        return "{\"success\": true, \"message\": \"Troca realizada com sucesso!\"}";
    }

    private String extractJsonValue(String json, String key) {
        String keyPattern = "\"" + key + "\"";
        int keyIdx = json.indexOf(keyPattern);
        if (keyIdx == -1) return null;

        int colonIdx = json.indexOf(":", keyIdx);
        if (colonIdx == -1) return null;

        int commaIdx = json.indexOf(",", colonIdx);
        int braceIdx = json.indexOf("}", colonIdx);

        int endIdx = -1;
        if (commaIdx != -1 && braceIdx != -1) endIdx = Math.min(commaIdx, braceIdx);
        else if (commaIdx != -1) endIdx = commaIdx;
        else if (braceIdx != -1) endIdx = braceIdx;
        else endIdx = json.length();

        String val = json.substring(colonIdx + 1, endIdx).trim();
        if (val.startsWith("\"") && val.endsWith("\"") && val.length() >= 2) {
            val = val.substring(1, val.length() - 1);
        }
        return val;
    }
}
