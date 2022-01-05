package me.mariana.ketlyn.analyzers.syntactic;

import me.mariana.ketlyn.analyzers.TokenMetadata;
import me.mariana.ketlyn.grammar.Grammar;
import me.mariana.ketlyn.grammar.NonTerminal;
import me.mariana.ketlyn.grammar.Terminal;
import me.mariana.ketlyn.grammar.Token;
import me.mariana.ketlyn.util.FlagLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.UnaryOperator;

import static me.mariana.ketlyn.analyzers.syntactic.SyntacticUtil.commandTable;
import static me.mariana.ketlyn.analyzers.syntactic.SyntacticUtil.parseTable;

public class SyntacticAnalyzer {
    private final Stack<Grammar> stack;
    private final LinkedList<TokenMetadata> tokens;

    public SyntacticAnalyzer(List<TokenMetadata> tokens) {
        //inicia a fila
        this.tokens = new LinkedList<>(tokens);
        //inicializa a pilha
        this.stack = new Stack<Grammar>();
        this.stack.push(Token.$);
        this.stack.push(NonTerminal.KETLYN);
    }

    public void analyze() {
        while (!stack.isEmpty()) {
            // recupera os dados do primeiro elemento da fila de tokens
            var token = tokens.getFirst().token();
            var line = tokens.getFirst().line();
            var column = tokens.getFirst().column();

            log("token " + "'" + token.getName() + "'" + " was selected from tokens.");

            if (stack.peek() instanceof Terminal) {
                terminalOnPeek(tokens, token, line, column);
            } else {
                nonTerminalOnPeek(token, line, column, grammar -> {
                    //Com uma cópia da lista recebida, inverte a sequência
                    var reverse = new LinkedList<Grammar>();
                    new LinkedList<>(grammar)
                            .descendingIterator()
                            .forEachRemaining(reverse::add);
                    return reverse;
                });
            }
        }
    }

    private void nonTerminalOnPeek(Terminal token, Integer line,
                                   Integer column,
                                   UnaryOperator<List<Grammar>> reverse) {
        var nonTerminal = stack.peek();
        /* Combinando o índice do nonTerminal com o de tokens forma uma matriz
         * Essa matriz guarda a sequência de comandos que deve ser inserida na pilha de NonTerminal
         */
        var index = parseTable().get(nonTerminal.getIndex())
                .get(token.getIndex());
        // Se o índice retornar negativo, não é uma combinação válida
        if (index < 0)
            error(nonTerminal, token, line, column);

        // Uma lista com a sequência de comandos
        var grammar = commandTable().get(index);
        log((stack.peek() instanceof NonTerminal ? "non terminal " : "token ")
                + "'" + stack.peek() + "'" + " was poped of the stack.");
        // Retira o NonTerminal anterior que gerou a nova sequencia de comandos
        stack.pop();
        // Inverte a lista
        reverse.apply(grammar).forEach(lang -> {
            log((stack.peek() instanceof NonTerminal ? "non terminal " : "token ") +
                    "'" + stack.peek() + "'" + " was pushed to the stack.");
            stack.push(lang);
        });
    }

    private void log(String message) {
        if (FlagLogger.SYNTACTIC.isActive()) {
            System.out.println(message);
        }
    }

    private void terminalOnPeek(LinkedList<TokenMetadata> tokens, Terminal token, Integer line,
                                Integer column) {
        //Se o indice for igual, os dois objetos são removidos ambos objetos do topo da pila / primeiro da fila
        if (stack.peek().getIndex() == token.getIndex()) {
            log("token " + "'" + token.getName() + "'" + " was erased from tokens.");
            tokens.removeFirst();
            log("token " + "'" + token.getName() + "'" + " was poped of the stack.");
            stack.pop();
        } else {
            /* Se a igualdade não for verdadeira finaliza o programa,
             * pois o código fonte analisado não está correto
             */
            error(stack.peek(), token, line, column);
        }
    }

    //Mensagens de erro sintático
    private void error(Grammar obj, Terminal token, Integer line, Integer column) {
        var builder = new StringBuilder();
        builder.append("at [").append(line).append(", ").append(column).append("]").append("\n");
        builder.append("\t").append("'").append(obj.getName()).append("'").append(
                        " was expected, but ")
                .append("'").append(token.getName()).append("'").append(" was found.").append("\n");
        builder.append("\t").append("compilation terminated\n");
        throw new SyntacticException(builder.toString());
    }
}
