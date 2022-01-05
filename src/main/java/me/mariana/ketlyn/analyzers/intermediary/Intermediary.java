package me.mariana.ketlyn.analyzers.intermediary;

import me.mariana.ketlyn.analyzers.TokenMetadata;
import me.mariana.ketlyn.util.FlagLogger;
import me.mariana.ketlyn.util.PostfixNotation;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static me.mariana.ketlyn.grammar.Token.*;

public class Intermediary {
    private LinkedList<String> code;
    private List<TokenMetadata> tokens;
    private List<String> variables;
    private Stack<String> labelStack;
    private int labelCount;
    private int varCount;

    public Intermediary(List<TokenMetadata> tokens, Set<String> keySet) {
        this.code = new LinkedList<>();
        this.labelStack = new Stack<>();
        this.variables = new ArrayList<>(keySet);
        this.code = new LinkedList<>();
        this.labelStack = new Stack<>();
        this.tokens = tokens;
    }

    public List<String> generate() {
        variables.forEach(var -> {
            appendCode("INT");
            appendCode(" ");
            appendCode(var);
            appendCode("\n");
        });

        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i).token();
            String command;
            var condition = new LinkedList<String>();

            if (IF.equals(token)) {
                appendCode("IF");
                appendCode(" ");
                i += 2;
                condition = new LinkedList<>();
                while (!tokens.get(i).lexeme().equals(")")) {
                    condition.add(tokens.get(i++).lexeme());
                    condition.add(" ");
                }
                inverseCondition(condition);
                appendCode(listToString(condition));
                labelStack.push("_L" + (++labelCount) + ":");
                appendCode("GOTO");
                appendCode(" ");
                appendCode("_L" + (labelCount));
                appendCode("\n");
            }
            else if (ELSE.equals(token)) {
                appendCode("GOTO");
                appendCode(" ");
                appendCode("_L" + (++labelCount));
                appendCode("\n");
                appendCode(labelStack.peek());
                appendCode(" ");
                appendCode("\n");

                labelStack.pop();
                labelStack.push("_L" + labelCount + ":");
            }
            else if (LOOP.equals(token)) {
                appendCode("_L" + (++labelCount) + ":");
                appendCode("\n");
                appendCode("IF");
                appendCode(" ");
                condition = new LinkedList<>();

                i += 2;

                while (!tokens.get(i).lexeme().equals(")")) {
                    condition.add(tokens.get(i++).lexeme());
                    condition.add(" ");
                }
                inverseCondition(condition);
                appendCode(listToString(condition));
                appendCode("GOTO");
                appendCode(" ");

                appendCode("_L" + (labelCount + 1));
                appendCode("\n");

                labelStack.push("_L" + (labelCount + 1) + ":");
                labelStack.push("_L" + (labelCount++));
                labelStack.push("GOTO");
            }
            else if (ENDIF.equals(token)) {
                appendCode(labelStack.peek());
                labelStack.pop();
                appendCode("\n");
            }
            else if (END_LOOP.equals(token)) {
                appendCode(labelStack.peek());
                labelStack.pop();
                appendCode(labelStack.peek());
                labelStack.pop();
                appendCode("\n");

                appendCode(labelStack.peek());
                labelStack.pop();
                appendCode("\n");
            }
            else if (VAR.equals(token)) {
                while (!tokens.get(++i).lexeme().equals(";")) ;
            }
            else if (ID.equals(token)) {
                command = tokens.get(i++).lexeme();
                if (tokens.get(i).lexeme().equals("=")) {
                    command += " = ";
                    var expr = new StringBuilder();
                    while (!tokens.get(++i).lexeme().equals(";")) {
                        expr.append(tokens.get(i).lexeme());
                    }
                    var postfix = PostfixNotation.infixToPostFix(expr.toString());
                    var arr = threeAddressCode(postfix);

                    for (int j = 0; j < arr.size() - 1; j++) {
                        appendCode("_t" + ((varCount++) % 2) + " = " + arr.get(j));
                    }
                    appendCode(command + arr.get(arr.size() - 1));
                }
                appendCode("\n");
            }
            else if (WRITE.equals(token)) {
                appendCode("WRITE");
                appendCode(" ");
                i += 2;
                appendCode(tokens.get(i).lexeme());
                appendCode("\n");
            }
            else if (READ.equals(token)) {
                appendCode("READ");
                appendCode(" ");
                i += 2;
                appendCode(tokens.get(i).lexeme());
                appendCode("\n");
            }
        }

        var finalCode = code.stream()
                .filter(str -> !str.equals(" "))
                .filter(str -> !str.equals("\n"))
                .collect(Collectors.toList());
        if (FlagLogger.INTERMEDIARY.isActive()){
            finalCode.forEach(f -> System.out.println("\t" + f));
        }
        return finalCode;
    }

    private List<String> threeAddressCode(List<String> postfix) {
        LinkedList<String> result = new LinkedList<>();
        Stack<String> stack = new Stack<>();

        Predicate<String> isOperator = (str) -> asList("+", "-", "*", "/").contains(str);

        var temp = this.varCount;

        for (String token : postfix) {
            if (isOperator.test(token)) {
                var b = stack.peek();
                stack.pop();
                var a = stack.peek();
                stack.pop();
                result.addLast(a + " " + b + " " + token);
                stack.push("_t" + ((varCount++) % 2));
            }
            else {
                stack.push(token);
            }
        }

        if (result.isEmpty()) {
            result.addLast(stack.peek());
        }

        varCount = temp;
        return result;
    }

    private void appendCode(String line) {
        code.addLast(line);
    }

    private String listToString(List<String> condition) {
        var builder = new StringBuilder();
        condition.forEach(builder::append);
        return builder.toString();
    }

    private void inverseCondition(List<String> condition) {
        for (int i = 0; i < condition.size(); i++) {
            var temp = condition.get(i);
            condition.set(i, switch (temp) {
                case ">" -> "<=";
                case "<" -> ">=";
                default -> temp;
            });
        }
    }
}
