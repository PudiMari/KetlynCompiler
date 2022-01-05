package me.mariana.ketlyn.analyzers.assembly;

import me.mariana.ketlyn.grammar.Token;

import java.util.Arrays;
import java.util.List;

public class AssemblyMaker {
    public StringBuffer codigo;
    public StringBuffer textSection;
    public StringBuffer dataSection;
    public StringBuffer bssSection;

    public AssemblyMaker() {
        this.codigo = new StringBuffer();
        this.dataSection = new StringBuffer();
        this.textSection = new StringBuffer();
        this.bssSection = new StringBuffer();

        this.bssSection.append("\n section .bss\n");

        this.dataSection.append(" section .data\n");
        this.dataSection.append("\tfmtin:\tdb \"%d\",  0x0\n ");
        this.dataSection.append("\tfmtout:\tdb \"%d\", 0xA, 0x0\n");

        this.textSection.append("\n section .text\n");
        this.textSection.append("\tglobal _main\n\textern _printf\n\textern _scanf\n");
        this.textSection.append("\n_main:\n ");

    }

    public String codFinal(List<String> intermediario) {

        int strCount = 1;

        this.textSection.append("\tpush ebp\n \tmov ebp,esp\n");

        for (int i = 0; i < intermediario.size(); i++) {

            if (intermediario.get(i).equals("WRITE")) {

                var str = intermediario.get(++i);
                var write = "";
                if (Token.isId(str)) {
                    write += "\tpush dword [" + str + "]\n" +
                            "\tpush dword fmtout\n" +
                            "\tcall _printf\n" +
                            "\tadd esp, 8\n";
                } else {
                    var constStr = "str_" + (strCount++);
                    write += "\tpush dword " + constStr + "\n" +
                            "\tcall _printf\n" +
                            "\tadd esp, 4\n";
                    var declaracao = "\t" + constStr + ": db " + str + ", 10,0\n";
                    dataSection.append(declaracao);

                }
                textSection.append(write);

            } else if (intermediario.get(i).equals("INT")) {
                bssSection.append("\t").append(intermediario.get(++i)).append(": resd 1\n");

            } else if (intermediario.get(i).equals("READ")) {
                textSection.append("\tpush ").append(intermediario.get(++i));
                textSection.append("\n\tpush dword fmtin\n");
                textSection.append("\tcall _scanf\n");
                textSection.append("\tadd esp, 8\n");

            } else if (intermediario.get(i).equals("IF")) {


                var exp = intermediario.get(++i).split(" ");

                if (Token.isId(exp[0])) {
                    exp[0] = "[" + exp[0] + "]";
                }
                if (Token.isId(exp[2])) {
                    exp[2] = "[" + exp[2] + "]";
                }
                i += 2;
                var label = intermediario.get(i);
                var jmp = "";
                if (exp[1].equals("<=")) {
                    jmp = "\tjle " + label + "\n";
                } else {
                    jmp = "\tjge " + label + "\n";
                }
                textSection.append("\tmov eax, " + exp[0] + "\n");
                textSection.append("\tcmp eax, " + exp[2] + "\n");
                textSection.append(jmp);

            } else if (intermediario.get(i).contains("_L") && !textSection.toString().contains(intermediario.get(i))) {
                textSection.append(intermediario.get(i) + "\n");

            } else if (intermediario.get(i).contains("GOTO")) {
                textSection.append("\tjmp " + intermediario.get(++i) + "\n");

            } else {
                var expr = intermediario.get(i).split(" ");
                System.out.println(Arrays.toString(expr));
                var first = toRegister(expr[0]);
                var second = toRegister(expr[2]);


                if (expr.length == 3) {
                    textSection.append("\tmov eax," + second + "\n");
                    textSection.append("\tmov " + first + ", eax\n");
                } else {
                    var third = toRegister(expr[3]);
                    var opr = switch (expr[4]) {
                        case "+" -> "add";
                        case "-" -> "sub";
                        case "*" -> "mul";
                        case "/" -> "div";
                        default -> " ";
                    };
                    textSection.append("\tmov eax, " + second + "\n");
                    textSection.append("\tmov ebx, " + third + "\n");

                    switch (opr) {
                        case "mul":
                            textSection.append("\t" + opr + " ebx\n");
                            break;
                        case "div":
                            textSection.append("\txor edx, edx\n");
                            textSection.append("\t").append(opr).append(" ebx\n");
                            break;
                        default:
                            textSection.append("\t" + opr + " eax, ebx\n");
                            break;
                    }
                    textSection.append("\tmov " + first + ", eax\n");
                }
            }
        }

        codigo.append(dataSection);
        codigo.append(bssSection);
        codigo.append(textSection);
        codigo.append("\n\tmov esp,ebp\n \tpop ebp\n \tret");
        return codigo.toString();
    }

    private String toRegister(String strb) {
        switch (strb) {
            case "_t0":
                return "ecx";
            case "_t1":
                return "edx";
            default:
                if (Token.isId(strb)) {
                    return "[" + strb + "]";
                }
                return strb;
        }
    }
}
