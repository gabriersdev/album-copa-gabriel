package model;

import exceptions.InvalidStickerIndexException;

public class Sticker {
    public static final int STICKER_MISSING = 0;
    public static final int STICKER_OWNED = 1;

    private int quantity;

    public Sticker(int initialQuantity) {
        if (initialQuantity < 0) throw new IllegalArgumentException("Quantidade inicial não pode ser negativa.");
        this.quantity = initialQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Quantidade não pode ser negativa.");
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Quantidade a adicionar não pode ser negativa.");
        this.quantity += amount;
    }

    public void removeQuantity(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Quantidade a remover não pode ser negativa.");
        if (this.quantity - amount < 0) throw new InvalidStickerIndexException("Quantidade de figurinhas não pode ser negativa.");
        this.quantity -= amount;
    }

    public boolean isMissing() {
        return quantity == STICKER_MISSING;
    }

    public boolean isPasted() {
        return quantity >= STICKER_OWNED;
    }

    public boolean hasRepeated() {
        return quantity > STICKER_OWNED;
    }

    public int getRepeatedCount() {
        return quantity > STICKER_OWNED ? quantity - STICKER_OWNED : 0;
    }
}
