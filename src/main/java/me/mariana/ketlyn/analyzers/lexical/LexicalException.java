package me.mariana.ketlyn.analyzers.lexical;

public class LexicalException extends RuntimeException {
    public LexicalException(String message, int line, int column) {
        super(String.format("[%d, %d] - %s", line, column, message));
    }
}
