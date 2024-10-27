package modelo;

public class Token {
    private String tipo;
    private String valor;
    private int linea;

    public Token(String tipo, String valor, int linea) {
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
    }

    // Getters y Setters
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    @Override
    public String toString() {
        return "Línea " + linea + ": " + tipo + " -> " + valor;
    }
}