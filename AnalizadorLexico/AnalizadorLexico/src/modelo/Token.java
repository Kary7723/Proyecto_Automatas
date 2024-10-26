package modelo;

public class Token {
    private final String tipo;
    private final String valor;
    private final int linea;
    private final int columna;  // Agregamos columna para mejor precisión

    public Token(String tipo, String valor, int linea) {
        this(tipo, valor, linea, 0);
    }

    public Token(String tipo, String valor, int linea, int columna) {
        this.tipo = tipo;
        this.valor = valor;
        this.linea = linea;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return String.format("Línea %d, Col %d: %s -> %s", 
                           linea, columna, tipo, valor);
    }

    // Método para obtener una descripción más amigable del tipo de token
    public String getTipoDescriptivo() {
        return switch (tipo) {
            case "ENTERO_CORTO" -> "Entero Corto";
            case "DINERO" -> "Valor Monetario";
            case "HORA" -> "Hora";
            case "HEXADECIMAL" -> "Número Hexadecimal";
            case "REAL" -> "Número Real";
            case "REAL_CIENTIFICO" -> "Número en Notación Científica";
            case "FECHA" -> "Fecha";
            case "CADENA" -> "Cadena de Texto";
            case "OPERADOR_ADITIVO" -> "Operador Aditivo";
            case "OPERADOR_MULTIPLICATIVO" -> "Operador Multiplicativo";
            case "OPERADOR_RELACIONAL" -> "Operador Relacional";
            case "OPERADOR_LOGICO" -> "Operador Lógico";
            case "OPERADOR_ASIGNACION" -> "Operador de Asignación";
            case "IDENTIFICADOR" -> "Identificador";
            case "DELIMITADOR" -> "Delimitador";
            case "NO_RECONOCIDO" -> "Token No Reconocido";
            default -> tipo;
        };
    }
}