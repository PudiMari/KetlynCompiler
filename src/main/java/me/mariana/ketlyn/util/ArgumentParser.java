package me.mariana.ketlyn.util;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class ArgumentParser {
    private static final String NÃO_FOI_ENCONTRADO_O_ARQUIVO_FONTE_COM_EXTENSÃO_KTL = "Não foi encontrado o arquivo fonte com extensão .ktl";
    private final List<String> arguments;

    private Path filePath;

    public ArgumentParser(String[] args) {
        this.arguments = Arrays.asList(args);
    }

    public void parse() {
        verifyAndActiveFlagLoggers();
        verifyAndSetFilePath();

    }

    public Path getFilePath() {
        if (filePath == null) throw new IllegalStateException("Não há um arquivo fonte analisado");
        return filePath;
    }

    private void verifyAndSetFilePath() {
        this.filePath = arguments.stream() //Lista de argumentos
                .filter(arg -> arg.endsWith(".ktl")) //Filtra pelo final .ktl
                .findFirst() //Recupera o primeiro valor encontrado
                .map(Path::of) //Mapeia para o objeto Path que representa caminho em java
                .orElseThrow(() -> new IllegalStateException(NÃO_FOI_ENCONTRADO_O_ARQUIVO_FONTE_COM_EXTENSÃO_KTL)); //Exceção
    }

    private void verifyAndActiveFlagLoggers() {
        for (var argument : arguments) { //para cada argumento na lista de argumentos
            if (argument.startsWith("-")) {
                var flag = FlagLogger.of(argument);
                flag.setActive(true);
            }
        }
    }
}
