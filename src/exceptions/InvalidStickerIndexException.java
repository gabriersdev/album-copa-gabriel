package exceptions;

public class InvalidStickerIndexException extends RuntimeException {
    public InvalidStickerIndexException(String message) {
        super(message);
    }
}
