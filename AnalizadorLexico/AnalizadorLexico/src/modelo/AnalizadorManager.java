package modelo;

import java.util.*;
import java.util.regex.Pattern;

public class AnalizadorManager {
    // Patrones básicos según las convenciones
    private static final String L = "[a-zA-Z]";
    private static final String M = "[A-Z]";
    private static final String m = "[a-z]";
    private static final String N = "[0-9]";
    private static final String H = "[0-9A-Fa-f]";
    
    
    // Patrones de expresiones regulares para cada tipo de token
    private static final Pattern PATRON_ENTERO = Pattern.compile(N + "+");
private static final Pattern PATRON_BOOLEANO = Pattern.compile("true|false");
    private static final Pattern PATRON_ENTERO_CORTO = Pattern.compile(N + "+_c");
    private static final Pattern PATRON_ENTERO_LARGO = Pattern.compile(N + "+_l");
    private static final Pattern PATRON_DINERO = Pattern.compile("Q" + N + "+(?:," + N + "{3})*(?:\\." + N + "{2})?");
    private static final Pattern PATRON_HORA = Pattern.compile("(?:[01]" + N + "|2[0-3]):[0-5]" + N);
    private static final Pattern PATRON_HEXADECIMAL = Pattern.compile("0[xX]" + H + "+");
    private static final Pattern PATRON_REAL = Pattern.compile("[0-9]+\\.[0-9]+");
    private static final Pattern PATRON_REAL_CIENTIFICO = Pattern.compile("[0-9]+\\.[0-9]+[Ee][+-][0-9]+");
    private static final Pattern PATRON_FECHA = Pattern.compile("'(0?[1-9]|[12][0-9]|3[01])\\\\(0?[1-9]|1[012])\\\\[0-9]{4}'");    private static final Pattern PATRON_IDENTIFICADOR = Pattern.compile("(" + L + "|_)(" + L + "|" + N + "|_)*");
    private static final Pattern PATRON_CADENA = Pattern.compile("\"[^\"]*\"");
    private static final Pattern PATRON_INICIO = Pattern.compile("inicio");
    private static final Pattern PATRON_FIN = Pattern.compile("fin");

    // Conjuntos de operadores
    private static final Set<String> OPERADORES_ADITIVOS = Set.of("+", "-");
    private static final Set<String> OPERADORES_MULTIPLICATIVOS = Set.of("*", "/", "%");
    private static final Set<String> OPERADORES_RELACIONALES = Set.of("<", ">", "<=", ">=", "==", "!=");
    private static final Set<String> OPERADORES_LOGICOS = Set.of("&&", "||", "!");
    private static final Set<String> OPERADORES_ASIGNACION = Set.of("=", "+=", "-=", "*=", "/=", "%=");
    private static final Set<String> DELIMITADORES = Set.of("{", "}", "[", "]", ",", ";", ".");

    public List<Token> analizar(String codigo) {
        List<Token> tokens = new ArrayList<>();
        String[] lineas = codigo.split("\n");
        
        for (int numLinea = 0; numLinea < lineas.length; numLinea++) {
            String linea = lineas[numLinea].trim();
            if (!linea.isEmpty()) {
                analizarLinea(linea, numLinea + 1, tokens);
            }
        }
        
        return tokens;
    }

    private void analizarLinea(String linea, int numeroLinea, List<Token> tokens) {
    StringBuilder tokenActual = new StringBuilder();
    boolean enCadena = false;
    int columnaActual = 0;

    for (int i = 0; i < linea.length(); i++) {
        char c = linea.charAt(i);
        columnaActual++;

        // Manejo de cadenas
        if (c == '"') {
            if (!enCadena) {
                procesarTokenPendiente(tokenActual, numeroLinea, columnaActual - tokenActual.length(), tokens);
                enCadena = true;
            } else {
                tokenActual.append(c);
                procesarToken(tokenActual.toString(), numeroLinea, columnaActual - tokenActual.length(), tokens);
                tokenActual.setLength(0);
                enCadena = false;
                continue;
            }
        }

        if (enCadena) {
            tokenActual.append(c);
            continue;
        }

        // Procesar otros caracteres
        if (Character.isWhitespace(c)) {
            procesarTokenPendiente(tokenActual, numeroLinea, columnaActual - tokenActual.length(), tokens);
        } else if (esCaracterEspecial(c)) {
            procesarTokenPendiente(tokenActual, numeroLinea, columnaActual - tokenActual.length(), tokens);
            procesarOperadorCompuesto(c, linea, i, numeroLinea, columnaActual, tokens);
        } else {
            tokenActual.append(c);
        }
    }

    procesarTokenPendiente(tokenActual, numeroLinea, columnaActual - tokenActual.length(), tokens);
}

    private void procesarTokenPendiente(StringBuilder tokenActual, int numeroLinea, int columna, List<Token> tokens) {
        if (tokenActual.length() > 0) {
            procesarToken(tokenActual.toString(), numeroLinea, columna, tokens);
            tokenActual.setLength(0);
        }
    }

    private void procesarToken(String token, int numeroLinea, int columna, List<Token> tokens) {
    if (token.isEmpty()) return;

     if (PATRON_REAL_CIENTIFICO.matcher(token).matches()) {
        tokens.add(new Token("REAL_CIENTIFICO", token, numeroLinea, columna));
    }
     else if (PATRON_ENTERO.matcher(token).matches()) {
        tokens.add(new Token("ENTERO", token, numeroLinea, columna));
    } else if (PATRON_ENTERO_CORTO.matcher(token).matches()) {
        tokens.add(new Token("ENTERO_CORTO", token, numeroLinea, columna));
    } else if (PATRON_ENTERO_LARGO.matcher(token).matches()) {
        tokens.add(new Token("ENTERO_LARGO", token, numeroLinea, columna));
    } else if (PATRON_DINERO.matcher(token).matches()) {
        tokens.add(new Token("DINERO", token, numeroLinea, columna));
    } else if (PATRON_HORA.matcher(token).matches()) {
        tokens.add(new Token("HORA", token, numeroLinea, columna));
    } else if (PATRON_HEXADECIMAL.matcher(token).matches()) {
        tokens.add(new Token("HEXADECIMAL", token, numeroLinea, columna));
    }  else if (PATRON_REAL.matcher(token).matches()) {
        tokens.add(new Token("REAL", token, numeroLinea, columna));
    } else if (PATRON_FECHA.matcher(token).matches()) {
        tokens.add(new Token("FECHA", token, numeroLinea, columna));
    } else if (PATRON_CADENA.matcher(token).matches()) {
        tokens.add(new Token("CADENA", token, numeroLinea, columna));
    } else if (PATRON_BOOLEANO.matcher(token).matches()) {
        tokens.add(new Token("BOOLEANO", token, numeroLinea, columna));
    } else if (token.equals("if")) {
        tokens.add(new Token("PALABRA_RESERVADA", token, numeroLinea, columna));
    } else if (token.equals("else")) {
        tokens.add(new Token("PALABRA_RESERVADA", token, numeroLinea, columna));
    } else if (PATRON_IDENTIFICADOR.matcher(token).matches()) {
        if (PATRON_INICIO.matcher(token).matches()) {
            tokens.add(new Token("INICIO", token, numeroLinea, columna));
        } else if (PATRON_FIN.matcher(token).matches()) {
            tokens.add(new Token("FIN", token, numeroLinea, columna));
        }else if (PATRON_FIN.matcher(token).matches()) {
            tokens.add(new Token("Parentesis_Inicio", token, numeroLinea, columna));
        }else if (PATRON_FIN.matcher(token).matches()) {
            tokens.add(new Token("Parentesis_Final", token, numeroLinea, columna));
        } else {
            tokens.add(new Token("IDENTIFICADOR", token, numeroLinea, columna));
        }
    } else {
        tokens.add(new Token("NO_RECONOCIDO", token, numeroLinea, columna));
    }
}

  private void procesarOperadorCompuesto(char c, String linea, int pos, int numeroLinea, int columna, List<Token> tokens) {
    String operador = String.valueOf(c);
    
    // Verificar primero si es un paréntesis
    if (operador.equals("(")) {
        tokens.add(new Token("PARENTESIS_INICIO", operador, numeroLinea, columna));
        return;
    }
    if (operador.equals(")")) {
        tokens.add(new Token("PARENTESIS_FINAL", operador, numeroLinea, columna));
        return;
    }
    
    if (pos + 1 < linea.length()) {
        char nextChar = linea.charAt(pos + 1);
        String posibleOperador = operador + nextChar;
        
        if (OPERADORES_RELACIONALES.contains(posibleOperador) || 
            OPERADORES_LOGICOS.contains(posibleOperador) || 
            OPERADORES_ASIGNACION.contains(posibleOperador)) {
            tokens.add(new Token(getTipoOperador(posibleOperador), posibleOperador, numeroLinea, columna));
            return;
        }
    }
    
    if (OPERADORES_ADITIVOS.contains(operador) || 
        OPERADORES_MULTIPLICATIVOS.contains(operador) || 
        OPERADORES_RELACIONALES.contains(operador) || 
        DELIMITADORES.contains(operador)) {
        tokens.add(new Token(getTipoOperador(operador), operador, numeroLinea, columna));
    }
}
private String getTipoOperador(String operador) {
    if (operador.equals('(')) return "PARENTESIS_INICIO";
    if (operador.equals(')')) return "PARENTESIS_FINAL";
    if (OPERADORES_ADITIVOS.contains(operador)) return "OPERADOR_ADITIVO";
    if (OPERADORES_MULTIPLICATIVOS.contains(operador)) return "OPERADOR_MULTIPLICATIVO";
    if (OPERADORES_RELACIONALES.contains(operador)) return "OPERADOR_RELACIONAL";
    if (OPERADORES_LOGICOS.contains(operador)) return "OPERADOR_LOGICO";
    if (OPERADORES_ASIGNACION.contains(operador)) return "OPERADOR_ASIGNACION";
    if (DELIMITADORES.contains(operador)) return "DELIMITADOR";
    return "NO_RECONOCIDO";
}
   private boolean esCaracterEspecial(char c) {
        return "+-*/<>=!&|(){}[],.;%".indexOf(c) != -1;
    }

  

  
}