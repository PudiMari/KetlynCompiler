package me.mariana.ketlyn.util;

public enum FlagLogger {
    LEXICAL(false, "-lexical"),
    SYNTACTIC(false, "-syntactic"),
    SEMANTIC(false, "-semantic"),
    INTERMEDIARY(false,"-intermediary");

    private boolean active;
    private final String command;

    FlagLogger(boolean active, String command) {

        this.active = active;
        this.command = command;
    }

    /**
     * Compara o comando recebido no formato de string com a lista de comandos existentes
     *
     * @param command
     * @return Comando no formato de FlagLogger
     */
    public static FlagLogger of(String command) {
        for (var flag : FlagLogger.values()) {
            if (flag.getCommand().equals(command)) return flag;
        }
        throw new IllegalArgumentException("O comando " + command + " não foi reconhecido");
    }


    public void setActive(boolean active) {
        System.out.println("Alterando o valor de " + this.name() + " para " + active);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public String getCommand() {
        return command;
    }
}
