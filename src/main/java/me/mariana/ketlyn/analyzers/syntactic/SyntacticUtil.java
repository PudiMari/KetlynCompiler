package me.mariana.ketlyn.analyzers.syntactic;

import me.mariana.ketlyn.grammar.Grammar;
import me.mariana.ketlyn.grammar.NonTerminal;
import me.mariana.ketlyn.grammar.Token;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static me.mariana.ketlyn.grammar.NonTerminal.*;
import static me.mariana.ketlyn.grammar.Symbol.*;
import static me.mariana.ketlyn.grammar.Token.*;

public class SyntacticUtil {
    public static List<List<Grammar>> commandTable() {
        return asList(
                asList(INIT, RECURSIVE_STATEMENT, CLOSE),
                of(),
                asList(STATEMENT, RECURSIVE_STATEMENT),
                of(),
                of(NonTerminal.WRITE),
                of(NonTerminal.READ),
                of(CONDITIONAL),
                of(NonTerminal.LOOP),
                of(DECLARATION),
                of(ASSIGNMENT),
                asList(Token.WRITE, OPEN_PARENTHESIS, ANY_EXPRESSION, CLOSE_PARENTHESIS, SEMICOLON),
                asList(Token.READ, OPEN_PARENTHESIS, ID, CLOSE_PARENTHESIS, SEMICOLON),
                asList(NonTerminal.IF, NonTerminal.ELSE, ENDIF),
                asList(Token.IF, OPEN_PARENTHESIS, LOGICAL_EXPRESSION, CLOSE_PARENTHESIS, RECURSIVE_STATEMENT),
                asList(Token.ELSE, RECURSIVE_STATEMENT),
                of(),
                asList(WHILE, END_LOOP),
                asList(Token.LOOP, OPEN_PARENTHESIS, LOGICAL_EXPRESSION, CLOSE_PARENTHESIS, RECURSIVE_STATEMENT),
                asList(VAR, ID, SEMICOLON),
                asList(ID, EQUALS, BASIC_EXPRESSION, SEMICOLON),
                asList(ID, BASIC_OPERATOR),
                asList(NUMBER, BASIC_OPERATOR),
                asList(PLUS, BASIC_EXPRESSION),
                asList(MINUS, BASIC_EXPRESSION),
                asList(OPEN_PARENTHESIS, BASIC_EXPRESSION, CLOSE_PARENTHESIS, BASIC_OPERATOR),
                asList(PLUS, BASIC_EXPRESSION),
                asList(MINUS, BASIC_EXPRESSION),
                asList(ASTERISK, BASIC_EXPRESSION),
                asList(FORWARD_SLASH, BASIC_EXPRESSION),
                of(),
                of(STRING),
                of(ID),
                of(NUMBER),
                of(BASIC_EXPRESSION),
                of(STRING_EXPRESSION),
                of(LESS_THAN),
                of(GREATER_THAN),
                asList(ID_OR_NUMBER, LOGICAL_OPERATOR, ID_OR_NUMBER)
        );
    }

    public static List<List<Integer>> parseTable() {
        return asList(
                asList(1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, 2, -1, -1, -1, -1, 3, 2, 3, 3, 2, 3, 2, 2, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, 9, -1, -1, -1, -1, -1, 6, -1, -1, 7, -1, 4, 5, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, 13, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, 14, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 18, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, 19, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, 20, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 24, -1, -1, -1, 22, 23, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 29, 29, -1, 25, 26, 27, 28, -1, -1),
                asList(-1, -1, -1, -1, 30, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, 33, -1, 33, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, 33, 33, -1, -1, -1, -1),
                asList(-1, 37, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, 31, -1, 32, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1),
                asList(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, 36)
        );
    }
}
