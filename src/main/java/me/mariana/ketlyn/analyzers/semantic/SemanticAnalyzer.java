package me.mariana.ketlyn.analyzers.semantic;

import me.mariana.ketlyn.analyzers.TokenMetadata;
import me.mariana.ketlyn.util.FlagLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.mariana.ketlyn.grammar.Symbol.*;
import static me.mariana.ketlyn.grammar.Token.*;

public class SemanticAnalyzer {
    private final List<TokenMetadata> tokens;
    private final Map<String, Boolean> variables;
    private int index;

    public SemanticAnalyzer(List<TokenMetadata> tokens) {
        this.tokens = new ArrayList<>(tokens);
        this.variables = new HashMap<>();
    }

    public void analyze() {
        index = 0;
        while (index < tokens.size()) {
            var currentToken = tokens.get(index);
            if (VAR.equals(currentToken.token())) {
                verifyDeclarationOfVariable();
            }
            else if (IF.equals(currentToken.token()) || LOOP.equals(currentToken.token())) {
                verifyConditionalArgument();
            }
            else if (ID.equals(currentToken.token())) {
                verifyVariable(currentToken);
            }
            else if (currentToken.token().equals(READ)) {
                markReadArgumentAsAssigned();
            }
            index++;
        }

    }

    private void verifyDeclarationOfVariable() {
        var nextToken = tokens.get(index + 1);
        verifyIfAlreadyDeclaredVariable(nextToken);
        variables.put(nextToken.lexeme(), false);
        this.log("The variable '" + nextToken.lexeme() + "' was declared");
    }

    private void verifyVariable(TokenMetadata currentToken) {
        verifyIfNotDeclaredVariable(currentToken);
        var buffer = new StringBuilder();
        if (EQUALS.equals(tokens.get(++index).token())) {
            index++;
            verifyExpression(buffer);
            verifyVariableValue(buffer, currentToken);
        }
    }

    private void markReadArgumentAsAssigned() {
        final var READ_ARGUMENT = this.index + 2;
        var nextToken = tokens.get(READ_ARGUMENT);
        verifyIfNotDeclaredVariable(nextToken);
        variables.put(nextToken.lexeme(), true);
        log("The variable '" + nextToken.lexeme() + "' is an input value");
    }

    private void verifyIfAlreadyDeclaredVariable(TokenMetadata nextToken) {
        if (isAlreadyDeclaredVariable(nextToken)) {
            error("The variable '" + nextToken.lexeme() + "' is already declared", nextToken.line(), nextToken.column());
        }
    }

    private void verifyVariableValue(StringBuilder buffer, TokenMetadata currentToken) {
        if ("0".equals(buffer.toString())) {
            variables.put(currentToken.lexeme(), false);
        }
        else if (buffer.toString().contains("/0")) {
            error("Division by zero", currentToken.line(), currentToken.column());
        }
        else {
            variables.put(currentToken.lexeme(), true);
        }
        log("The variable '%s' receive value: '%s'".formatted(currentToken.lexeme(), buffer));
    }

    private void verifyExpression(StringBuilder buffer) {
        while (!SEMICOLON.equals(tokens.get(index).token())) {
            var lexeme = tokens.get(index).lexeme();
            if (isRawVariable(lexeme)) {
                verifyIfNotDeclaredVariable(tokens.get(index));
                final boolean hasValue = variables.get(lexeme);
                buffer.append(hasValue ? lexeme : "0"); //Se tiver valor concatena o lexema, senão concatena 0.
            }
            else {
                buffer.append(lexeme);
            }
            index++;
        }
    }

    private boolean isRawVariable(String lexeme) {
        if (!Character.isLetter(lexeme.charAt(0))) {
            return false;
        }
        return lexeme.chars().allMatch(Character::isLetterOrDigit);
    }

    private void verifyConditionalArgument() {
        while (!CLOSE_PARENTHESIS.equals(tokens.get(index++).token())) {
            var currentToken = tokens.get(index);
            if (ID.equals(currentToken.token())) {
                verifyIfNotDeclaredVariable(currentToken);
            }
        }
    }

    private void verifyIfNotDeclaredVariable(TokenMetadata currentToken) {
        if (!isAlreadyDeclaredVariable(currentToken)) {
            error(
                    "The variable '" + currentToken.lexeme() + "' wasn't declared",
                    currentToken.line(),
                    currentToken.column()
            );
        }
    }

    private void log(String message) {
        if (FlagLogger.SEMANTIC.isActive()) {
            System.out.println(message);
        }
    }

    private void error(String message, int line, int column) {
        throw new SemanticException(String.format("[%d, %d] %s", line, column, message));
    }

    private boolean isAlreadyDeclaredVariable(TokenMetadata nextToken) {
        return variables.containsKey(nextToken.lexeme());
    }

    public Map<String, Boolean> getVariables() {
        return variables;
    }
}
