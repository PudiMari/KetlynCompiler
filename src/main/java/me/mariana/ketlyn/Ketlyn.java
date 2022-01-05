package me.mariana.ketlyn;

import me.mariana.ketlyn.analyzers.assembly.AssemblyMaker;
import me.mariana.ketlyn.analyzers.intermediary.Intermediary;
import me.mariana.ketlyn.analyzers.lexical.LexicalAnalyzer;
import me.mariana.ketlyn.analyzers.semantic.SemanticAnalyzer;
import me.mariana.ketlyn.analyzers.syntactic.SyntacticAnalyzer;
import me.mariana.ketlyn.util.ArgumentParser;
import me.mariana.ketlyn.util.FileManager;

public class Ketlyn {
    public static void main(String[] args) {
        var argumentParser = new ArgumentParser(args);

        argumentParser.parse();
        var fileManager = new FileManager(argumentParser.getFilePath());
        var sourceCode = fileManager.read();
        var lexicalAnalyzer = new LexicalAnalyzer(sourceCode);
        lexicalAnalyzer.analyze();
        var tokens = lexicalAnalyzer.getTokens();
        //System.out.println(tokens);
        var syntacticAnalyzer = new SyntacticAnalyzer(tokens);
        syntacticAnalyzer.analyze();
        var semanticAnalyzer = new SemanticAnalyzer(tokens);
        semanticAnalyzer.analyze();
        var intermediary = new Intermediary(tokens,semanticAnalyzer.getVariables().keySet());
        final var intermediaryCode = intermediary.generate();
        var assemblyMaker = new AssemblyMaker();
        final var assembly = assemblyMaker.codFinal(intermediaryCode);
        fileManager.writeDataOnAsmFile(assembly);
    }
}
