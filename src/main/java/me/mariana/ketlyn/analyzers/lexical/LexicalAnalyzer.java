package me.mariana.ketlyn.analyzers.lexical;


import me.mariana.ketlyn.util.FlagLogger;
import me.mariana.ketlyn.grammar.Symbol;
import me.mariana.ketlyn.grammar.Token;
import me.mariana.ketlyn.analyzers.TokenMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LexicalAnalyzer {
    private final List<String> sourceCode;
    private int line = 1;
    private int column = 1;
    private List<TokenMetadata> tokens = new ArrayList<>();

    public LexicalAnalyzer(List<String> sourceCode) {
        this.sourceCode = sourceCode;
    }

    public void analyze() {
        for (var line : sourceCode) { //Percorre as linhas do sourceCode
            this.analyzeLine(line);
            this.line++;
            this.column = 1;
        }
        this.tokens.add(new TokenMetadata(
                        Token.$,
                        "$",
                        this.line,
                        this.column
                )
        );
    }

    private void analyzeLine(String line) {
        if (line.isEmpty() || line.isBlank()) return;
        var index = new AtomicInteger(0);
        var buffer = new StringBuilder();
        while (index.get() < line.length()) {
            Character currentCharacter = line.charAt(index.getAndIncrement()); //Marcando o caractere atual
            /*
                    char nextCharacter;
                    if (index.get() < line.length()){
                        nextCharacter = line.charAt(index.get());
                    }
                    else{
                        nextCharacter = 0;
                    }
             */
            var nextCharacter = index.get() < line.length() ? line.charAt(index.get()) : 0;
            if (isCommentary(currentCharacter, nextCharacter))
                return; //Se o atual e o próximo caractere for '/' é comentário
            if (Character.isWhitespace(currentCharacter)) {
                this.column++;
            } else if (currentCharacter == '"') {
                analyzeQuote(line, index);
                buffer = new StringBuilder();
            } else if (Symbol.contains(currentCharacter)) {
                var token = new TokenMetadata(
                        Symbol.toEnum(currentCharacter), //Converte de caractere para enumerado
                        currentCharacter.toString(), //Converte o caractere para string
                        this.line,
                        this.column
                );
                this.tokens.add(token);
                log(token);
                this.column++; //Por ser sempre um caractere, soma a coluna
                buffer = new StringBuilder();
            } else if (!Character.isLetterOrDigit(nextCharacter) && nextCharacter != ':') {
                buffer.append(currentCharacter);
                analyzeBuffer(buffer.toString());
                this.column += buffer.length();
                buffer = new StringBuilder();
            } else {
                buffer.append(currentCharacter);
            }
        }
    }

    private void log(TokenMetadata token) {
        if (FlagLogger.LEXICAL.isActive()) {
            System.out.println(token);
        }
    }

    private void analyzeBuffer(String buffer) {
        TokenMetadata token = null;

        if (isKeyword(buffer)) {
            token = new TokenMetadata(Token.ofKeyword(buffer), buffer, this.line, this.column);
        } else if (isIdentifier(buffer)) {
            token = new TokenMetadata(Token.ID, buffer, this.line, this.column);
        } else if (isNumber(buffer)) {
            token = new TokenMetadata(Token.NUMBER, buffer, this.line, this.column);
        }

        if (token != null) {
            tokens.add(token);
            log(token);
        } else {
            throw new LexicalException(
                    "O lexema '" + buffer + "' não foi reconhecido",
                    this.line,
                    this.column);
        }
    }

    private boolean isKeyword(String buffer) { //Checa se o buffer é uma keyword
        return Token.containsKeyword(buffer);
    }

    private boolean isIdentifier(String buffer) { //Checa se o buffer é uma variável
        if (Character.isDigit(buffer.charAt(0))) { //Se começar com número, não pode ser variável
            return false;
        }
        return buffer.chars().allMatch(Character::isLetterOrDigit);
    }

    private boolean isNumber(String buffer) { //Checa se o buffer é um número
        return buffer.chars().allMatch(Character::isDigit);
    }

    public List<TokenMetadata> getTokens() {
        return tokens;
    }

    private void analyzeQuote(String line, AtomicInteger index) {
        var buffer = new StringBuilder();
        buffer.append('"');
        Character currentCharacter;
        do {
            currentCharacter = line.charAt(index.getAndIncrement()); //Caractere atual
            buffer.append(currentCharacter); //Concatenando o buffer com o caractere atual
            if (index.get() == line.length()) { //Mostra mensagem de erro se chegar ao fim da linha e não encontrar '"'
                throw new LexicalException("Está faltando fechar aspas duplas", this.line, this.column);
            }
        } while (currentCharacter != '"');
        var token = new TokenMetadata(Token.STRING, buffer.toString(), this.line, this.column);
        log(token);
        this.tokens.add(token); // Adiciona token de string na lista de tokens
        this.column += buffer.length(); //Soma o tamanho do buffer na variavel coluna
    }

    private boolean isCommentary(char currentCharacter, char nextCharacter) {
        return currentCharacter == '/' && nextCharacter == '/';
    }
}
