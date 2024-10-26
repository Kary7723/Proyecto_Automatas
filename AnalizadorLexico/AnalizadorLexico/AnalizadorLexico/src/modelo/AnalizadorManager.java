package modelo;

import java.util.*;

public class AnalizadorManager {
    private static final Set<String> PALABRAS_RESERVADAS = new HashSet<>(Arrays.asList(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch",
        "char", "class", "const", "continue", "default", "do", "double",
        "else", "enum", "extends", "final", "finally", "float", "for",
        "if", "implements", "import", "instanceof", "int", "interface",
        "long", "native", "new", "package", "private", "protected",
        "public", "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws", "transient",
        "try", "void", "volatile", "while"
    ));

    private static final Set<String> OPERADORES = new HashSet<>(Arrays.asList(
        "+", "-", "*", "/", "=", "==", "!=", "<", ">", "<=", ">=",
        "&&", "||", "!", "++", "--", "+=", "-=", "*=", "/="
    ));

    private static final Set<String> DELIMITADORES = new HashSet<>(Arrays.asList(
        "(", ")", "{", "}", "[", "]", ";", ",", "."
    ));

    public List<Token> analizar(String codigo) {
        List<Token> tokens = new ArrayList<>();
        String[] lineas = codigo.split("\n");
        
        for (int numLinea = 0; numLinea < lineas.length; numLinea++) {
            analizarLinea(lineas[numLinea], numLinea + 1, tokens);
        }
        
        return tokens;
    }

    private void analizarLinea(String linea, int numeroLinea, List<Token> tokens) {
        StringTokenizer tokenizer = new StringTokenizer(linea, " \t\n\r\f+-*/(){};,=<>!&|[].", true);
        StringBuilder tokenActual = new StringBuilder();
        
        while (tokenizer.hasMoreTokens()) {
            String caracterActual = tokenizer.nextToken();
            
            if (caracterActual.trim().isEmpty()) {
                continue;
            }

            if (esParteDeOperador(caracterActual)) {
                if (tokenActual.length() > 0) {
                    procesarToken(tokenActual.toString(), numeroLinea, tokens);
                    tokenActual.setLength(0);
                }
                tokenActual.append(caracterActual);
                
                if (tokenizer.hasMoreTokens()) {
                    String siguienteCaracter = tokenizer.nextToken();
                    if (esOperadorCompuesto(caracterActual + siguienteCaracter)) {
                        tokenActual.append(siguienteCaracter);
                    } else {
                        if (!siguienteCaracter.trim().isEmpty()) {
                            procesarToken(tokenActual.toString(), numeroLinea, tokens);
                            tokenActual.setLength(0);
                            tokenActual.append(siguienteCaracter);
                        }
                    }
                }
                
                if (tokenActual.length() > 0) {
                    procesarToken(tokenActual.toString(), numeroLinea, tokens);
                    tokenActual.setLength(0);
                }
            } else {
                if (esDelimitador(caracterActual)) {
                    if (tokenActual.length() > 0) {
                        procesarToken(tokenActual.toString(), numeroLinea, tokens);
                        tokenActual.setLength(0);
                    }
                    tokens.add(new Token("DELIMITADOR", caracterActual, numeroLinea));
                } else {
                    tokenActual.append(caracterActual);
                }
            }
        }
        
        if (tokenActual.length() > 0) {
            procesarToken(tokenActual.toString(), numeroLinea, tokens);
        }
    }

    private void procesarToken(String token, int numeroLinea, List<Token> tokens) {
        if (token.trim().isEmpty()) {
            return;
        }

        if (esNumero(token)) {
            tokens.add(new Token("NÚMERO", token, numeroLinea));
        } else if (esOperador(token)) {
            tokens.add(new Token("OPERADOR", token, numeroLinea));
        } else if (esPalabraReservada(token)) {
            tokens.add(new Token("PALABRA_RESERVADA", token, numeroLinea));
        } else if (esIdentificador(token)) {
            tokens.add(new Token("IDENTIFICADOR", token, numeroLinea));
        } else {
            tokens.add(new Token("NO_RECONOCIDO", token, numeroLinea));
        }
    }

    private boolean esNumero(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esOperador(String token) {
        return OPERADORES.contains(token);
    }

    private boolean esPalabraReservada(String token) {
        return PALABRAS_RESERVADAS.contains(token.toLowerCase());
    }

    private boolean esIdentificador(String token) {
        return token.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$");
    }

    private boolean esDelimitador(String token) {
        return DELIMITADORES.contains(token);
    }

    private boolean esParteDeOperador(String token) {
        return "+-*/=<>!&|".contains(token);
    }

    private boolean esOperadorCompuesto(String token) {
        return OPERADORES.contains(token);
    }
}