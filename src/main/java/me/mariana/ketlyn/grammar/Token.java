package me.mariana.ketlyn.grammar;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Token implements Terminal {
    $(0,"$", null),
    ID(1,"id", null),
    NUMBER(3,"number", null),
    STRING(4,"string_literal", null),

    INIT(5,"init_program", "ketlyn::init"),
    CLOSE(6,"close_program", "ketlyn::close"),
    IF(7,"if", "ktl::if"),
    ELSE(8,"else", "ktl::else"),
    ENDIF(9,"endif", "ktl::endif"),
    LOOP(10,"loop", "ktl::loop"),
    END_LOOP(11,"endloop", "ktl::endloop"),
    WRITE(12,"write", "ktl::write"),
    READ(13,"read", "ktl::read"),
    VAR(14,"var", "ktl::var");

    private final int index;
    private final String token;
    private final String keyword;

    Token(int index, String token, String keyword) {
        this.index = index;
        this.token = token;
        this.keyword = keyword;
    }

    public static boolean containsKeyword(String keyword) {
        return keywords().contains(keyword);
    }

    public static Token ofKeyword(String word) {
        for (Token token : Token.values()) {
            if (word.equals(token.getKeyword())) {
                return token;
            }
        }
        throw new IllegalArgumentException("Este simbolo '" + word + "' nao pode ser convertido.");
    }

    public static List<String> keywords() {
        return Arrays.stream(Token.values())
                .map(Token::getKeyword)
                .collect(Collectors.toList());
    }

    public static boolean isId(String s) {
        if (!Character.isLetter(s.charAt(0))) {
            return false;
        }
        return s.chars().allMatch(Character::isLetterOrDigit);
    }

    @Override
    public String getName() {
        return token;
    }

    @Override
    public int getIndex() {
        return index;
    }

    public String getKeyword() {
        return keyword;
    }

    public static Token toEnum(String str) {
        for (Token token : Token.values()) {
            if (token.getName().equals(str)) {
                return token;
            }
        }
        throw new IllegalArgumentException("Este simbolo '" + str + "' nao pode ser convertido.");
    }

    public static List<String> getValues() {
        return Arrays.stream(Token.values())
                .map(Token::getName)
                .collect(Collectors.toList());
    }
}
