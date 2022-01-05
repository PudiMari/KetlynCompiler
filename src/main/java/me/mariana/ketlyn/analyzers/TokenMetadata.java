package me.mariana.ketlyn.analyzers;

import me.mariana.ketlyn.grammar.Terminal;

public record TokenMetadata(
        Terminal token,
        String lexeme,
        int line,
        int column
) {
}
