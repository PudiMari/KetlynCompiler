package me.mariana.ketlyn.grammar;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Symbol implements Terminal {
    OPEN_PARENTHESIS(15,"("),
    CLOSE_PARENTHESIS(16,")"),
    SEMICOLON(17,";"),
    EQUALS(18,"="),
    PLUS(19,"+"),
    MINUS(20,"-"),
    ASTERISK(21,"*"),
    FORWARD_SLASH(22,"/"),
    GREATER_THAN(24,">"),
    LESS_THAN(23,"<");

    private final int index;
    private final String symbol;

    Symbol(int index, String symbol) {
        this.index = index;
        this.symbol = symbol;
    }

    public static boolean contains(Character symbol) {
        return getValues().contains(symbol.toString());
    }

    @Override
    public String getName() {
        return symbol;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public static Symbol toEnum(String str) {
        for (Symbol symbol : Symbol.values()) {
            if (symbol.getName().equals(str)) {
                return symbol;
            }
        }
        throw new IllegalArgumentException("Este simbolo '" + str + "' nao pode ser convertido.");
    }

    public static Symbol toEnum(Character str) {
        for (Symbol symbol : Symbol.values()) {
            if (symbol.getName().equals(str.toString())) {
                return symbol;
            }
        }
        throw new IllegalArgumentException("Este simbolo '" + str + "' nao pode ser convertido.");
    }

    public static List<String> getValues() {
        return Arrays.stream(Symbol.values())
                .map(Symbol::getName)
                .collect(Collectors.toList());
    }
}
