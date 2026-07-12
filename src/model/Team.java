package model;

import exceptions.InvalidStickerIndexException;
import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<Sticker> stickers;

    public Team(String name, int numPlayers) {
        this.name = name;
        this.stickers = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++) {
            this.stickers.add(new Sticker(0));
        }
    }

    public String getName() {
        return name;
    }

    public Sticker getSticker(int index) {
        validateIndex(index);
        return stickers.get(index);
    }

    public void setStickerQuantity(int index, int quantity) {
        validateIndex(index);
        stickers.get(index).setQuantity(quantity);
    }

    public void addStickerQuantity(int index, int amount) {
        validateIndex(index);
        stickers.get(index).addQuantity(amount);
    }

    public void removeStickerQuantity(int index, int amount) {
        validateIndex(index);
        stickers.get(index).removeQuantity(amount);
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= stickers.size()) {
            throw new InvalidStickerIndexException("Índice do jogador inválido: " + index);
        }
    }

    public int getNumPlayers() {
        return stickers.size();
    }

    public int getTotalStickers() {
        return stickers.stream().mapToInt(Sticker::getQuantity).sum();
    }

    public int getMissingStickersCount() {
        return (int) stickers.stream().filter(Sticker::isMissing).count();
    }

    public int getUniqueStickersCount() {
        return (int) stickers.stream().filter(Sticker::isPasted).count();
    }

    public int getRepeatedStickersCount() {
        return stickers.stream().mapToInt(Sticker::getRepeatedCount).sum();
    }
}
