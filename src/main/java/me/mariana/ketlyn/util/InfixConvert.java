package me.mariana.ketlyn.util;

import java.util.*;

import static me.mariana.ketlyn.util.LineSeparator.splitBy;

public class InfixConvert {
    private static int precedence(String c) {
        switch (c) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return -1;
        }
    }

    private static boolean isOperator(String str) {
        return Arrays.asList("+", "-", "*", "/", ")", "(").contains(str);
    }

    public static List<String> infixToPostFix(String inputExpression) {
        var parsedExpression = splitBy("\\*")
                .andThen(splitBy("\\+"))
                .andThen(splitBy("\\-"))
                .andThen(splitBy("\\/"))
                .andThen(splitBy("\\)"))
                .andThen(splitBy("\\("))
                .andThen(splitBy("\\s"))
                .apply(Collections.singletonList(inputExpression));

        Stack<String> operators = new Stack<>();
        LinkedList<String> result = new LinkedList<>();
        var it = parsedExpression.listIterator();
        String popped;

        while (it.hasNext()) {
            var curr = it.next();

            if (curr.chars().allMatch(Character::isWhitespace)) {
                continue;
            } else if (!isOperator(curr)) {
                result.addLast(curr);
            } else if (curr.equals(")")) {
                while (!(popped = operators.pop()).equals("("))
                    result.addLast(popped);
            } else {
                while (!operators.isEmpty() && !curr.equals("(") && precedence(operators.peek()) >= precedence(curr))
                    result.addLast(operators.pop());
                operators.push(curr);
            }
        }

        while (!operators.isEmpty())
            result.addLast(operators.pop());

        return result;
    }
}
