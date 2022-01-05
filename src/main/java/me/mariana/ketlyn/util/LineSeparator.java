package me.mariana.ketlyn.util;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface LineSeparator extends UnaryOperator<List<String>> {
    static LineSeparator splitBy(String delimiter) {
        final String formatter = "((?<=%1$s)|(?=%1$s))";
        final String formattedDelimiter = String.format(formatter, delimiter);
        return line -> line.stream()
                .map(str -> str.split(formattedDelimiter))
                .flatMap(Stream::of)
                .collect(Collectors.toList());
    }

    /*
     * funciona como um encadeador de metodos
     * */
    default LineSeparator andThen(LineSeparator other) {
        return line -> {
            List<String> otherResult = other.apply(line);
            return this.apply(otherResult);
        };
    }
}
