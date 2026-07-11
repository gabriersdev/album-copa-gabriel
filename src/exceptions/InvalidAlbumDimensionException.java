package exceptions;

public class InvalidAlbumDimensionException extends RuntimeException {
    public InvalidAlbumDimensionException(String message) {
        super(message);
    }
}
