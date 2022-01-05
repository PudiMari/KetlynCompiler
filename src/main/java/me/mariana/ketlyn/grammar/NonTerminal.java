package me.mariana.ketlyn.grammar;

import me.mariana.ketlyn.grammar.Grammar;

public enum NonTerminal implements Grammar {
    KETLYN(0, "KETLYN"),
    RECURSIVE_STATEMENT(1, "RECURSIVE_STATEMENT"),
    STATEMENT(2, "STATEMENT"),
    WRITE(3, "WRITE"),
    READ(4, "READ"),
    CONDITIONAL(5, "CONDITIONAL"),
    IF(6, "IF"),
    ELSE(7, "ELSE"),
    LOOP(8, "LOOP"),
    WHILE(9, "WHILE"),
    DECLARATION(10, "DECLARATION"),
    ASSIGNMENT(11, "ASSIGNMENT"),
    BASIC_EXPRESSION(12, "BASIC_EXPRESSION"),
    BASIC_OPERATOR(13, "BASIC_OPERATOR"),
    STRING_EXPRESSION(14, "STRING_EXPRESSION"),
    ANY_EXPRESSION(15, "ANY_EXPRESSION"),
    LOGICAL_EXPRESSION(16, "LOGICAL_EXPRESSION"),
    ID_OR_NUMBER(17, "ID_OR_NUMBER"),
    LOGICAL_OPERATOR(18, "LOGICAL_OPERATOR");

    private final int index;
    private final String name;

    NonTerminal(int index, String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
