package me.mariana.ketlyn.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileManager {
    private final Path filePath;

    public FileManager(Path filePath) {
        this.filePath = filePath;
    }

    public List<String> read() {
        //Faz a leitura do arquivo e converte para um objeto BufferedReader
        //BufferedReader é uma representação para informações do arquivo
        try (var buffer = Files.newBufferedReader(filePath)) {
            return buffer.lines() //Pega todas as linhas do arquivo .ktl
                    .collect(Collectors.toList()); //Coletando as linhas do arquivo e formando uma lista
        } catch (IOException e) { //IOException é uma exceção específica para leitura e escrita de arquivo
            e.printStackTrace(); //Pilha de erros
            throw new IllegalStateException("Erro ao ler o arquivo " + filePath + " motivo: " + e.getMessage());
        }
    }
    public Path create() {
        try {
            var asm = new File(getDir(filePath)
                    + "/"
                    + getAsmName(extractFileName(filePath))
                    + ".asm");
            boolean asmExists = asmExists().and(deleteAsmFile()).test(asm);
            if(asmExists) {
                System.out.println("O arquivo " + asm.getAbsoluteFile().getName() + " ja existe, " +
                        "um novo arquivo .asm sera criado");
            }
            else {
                System.out.println("Criado arquivo " + asm.getAbsoluteFile().getName());
            }
            isAsmFileCreated(asm);
            return asm.toPath();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void isAsmFileCreated(File asm) throws IOException {
        boolean fileCreated = asm.createNewFile();

        if(fileCreated) {
            System.out.println("Arquivo " + asm.getAbsoluteFile().getName() + " criado com sucesso");
        }
        else {
            throw new IOException("O arquivo .asm nao foi criado.");
        }
    }

    private Predicate<File> asmExists() {
        return File::exists;
    }

    private Predicate<File> deleteAsmFile() {
        return File::delete;
    }

    private String getAsmName(String inputName) {
        // remove a extensao do arquivo para ser utilizado na criacao do .asm
        return inputName.substring(0,                           // percorre do inicio da string
                inputName.indexOf(".")
        );     // ate a localizacao do ponto ( nao incluido )

    }

    private String getDir(Path path) {
        // obtem o caminho absoluto do arquivo ate o diretorio ignorando o arquivo
        return path.toFile()
                .getAbsoluteFile()
                .getParent();
    }

    private String extractFileName(Path path) {
        // obtem o caminho absoluto, porem so devolve o nome do arquivo com a extensao
        return path.toFile()
                .getAbsoluteFile()
                .getName();
    }

    public void writeDataOnAsmFile(String asmCode) {
        final var assemblyPath = this.create();
        try(var buffer = Files.newBufferedWriter(assemblyPath)) {
            buffer.write(asmCode);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
