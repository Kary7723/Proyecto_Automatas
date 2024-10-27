package analizadorlexico;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class AnalizadorLexico extends JFrame {
    private JTextArea areaEntrada;
    private JTextArea areaSalida;
    private JButton btnAnalizar;
    private JButton btnLimpiar;
    
    private static final Color BACKGROUND_COLOR = new Color(240, 248, 255);
    private static final Color HEADER_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_COLOR = new Color(0, 191, 255);
    private static final Color TEXT_COLOR = new Color(25, 25, 112);

    public AnalizadorLexico() {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Analizador Léxico");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Panel superior
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(HEADER_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Analizador Léxico");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);

        areaEntrada = new JTextArea();
        areaEntrada.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane scrollEntrada = new JScrollPane(areaEntrada);
        scrollEntrada.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HEADER_COLOR),
            "Código de Entrada"
        ));

        areaSalida = new JTextArea();
        areaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));
        areaSalida.setEditable(false);
        JScrollPane scrollSalida = new JScrollPane(areaSalida);
        scrollSalida.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(HEADER_COLOR),
            "Resultados del Análisis"
        ));

        panel.add(scrollEntrada);
        panel.add(scrollSalida);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(BACKGROUND_COLOR);

        btnAnalizar = createStyledButton("Analizar", BUTTON_COLOR);
        btnAnalizar.addActionListener(e -> analizarCodigo());

        btnLimpiar = createStyledButton("Limpiar", new Color(188, 93, 88));
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panel.add(btnAnalizar);
        panel.add(btnLimpiar);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void analizarCodigo() {
        String codigo = areaEntrada.getText();
        if (codigo.trim().isEmpty()) {
            mostrarError("Por favor, ingrese código para analizar.");
            return;
        }

        try {
            Analizador analizador = new Analizador();
            String resultado = analizador.analizar(codigo);
            areaSalida.setText(resultado);
        } catch (Exception ex) {
            mostrarError("Error al analizar el código: " + ex.getMessage());
        }
    }

    private void limpiarCampos() {
        areaEntrada.setText("");
        areaSalida.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private class Analizador {
        public String analizar(String codigo) {
            StringBuilder resultado = new StringBuilder();
            String[] lineas = codigo.split("\n");
            int numeroLinea = 1;

            for (String linea : lineas) {
                resultado.append("Línea ").append(numeroLinea).append(": \n");
                analizarLinea(linea, resultado);
                resultado.append("\n");
                numeroLinea++;
            }

            return resultado.toString();
        }

        private void analizarLinea(String linea, StringBuilder resultado) {
            StringTokenizer tokenizer = new StringTokenizer(linea, " \t\n\r\f+-*/(){};,=", true);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().trim();
                if (token.isEmpty()) continue;

                if (esNumero(token)) {
                    resultado.append("NÚMERO: ").append(token).append("\n");
                } else if (esOperador(token)) {
                    resultado.append("OPERADOR: ").append(token).append("\n");
                } else if (esPalabraReservada(token)) {
                    resultado.append("PALABRA RESERVADA: ").append(token).append("\n");
                } else if (esIdentificador(token)) {
                    resultado.append("IDENTIFICADOR: ").append(token).append("\n");
                } else if (esDelimitador(token)) {
                    resultado.append("DELIMITADOR: ").append(token).append("\n");
                } else {
                    resultado.append("TOKEN NO RECONOCIDO: ").append(token).append("\n");
                }
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
            return switch (token) {
                case "+", "-", "*", "/", "=", "==", "!=", "<", ">", "<=", ">=" -> true;
                default -> false;
            };
        }

        private boolean esPalabraReservada(String token) {
            Set<String> palabrasReservadas = new HashSet<>(Arrays.asList(
                "abstract", "assert", "boolean", "break", "byte", "case", "catch",
                "char", "class", "const", "continue", "default", "do", "double",
                "else", "enum", "extends", "final", "finally", "float", "for",
                "if", "implements", "import", "instanceof", "int", "interface",
                "long", "native", "new", "package", "private", "protected",
                "public", "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this", "throw", "throws", "transient",
                "try", "void", "volatile", "while"
            ));
            return palabrasReservadas.contains(token.toLowerCase());
        }

        private boolean esIdentificador(String token) {
            return token.matches("^[a-zA-Z_$][a-zA-Z0-9_$]*$");
        }

        private boolean esDelimitador(String token) {
            return switch (token) {
                case "(", ")", "{", "}", "[", "]", ";", ",", "." -> true;
                default -> false;
            };
        }
    }

    public static void main(String args[]) {
    
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    java.util.logging.Logger.getLogger(AnalizadorLexico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                new AnalizadorLexico().setVisible(true);
            }
        });
    }
}
