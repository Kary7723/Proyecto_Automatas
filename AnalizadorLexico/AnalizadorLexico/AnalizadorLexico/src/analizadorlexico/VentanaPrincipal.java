package analizadorlexico;

import modelo.AnalizadorManager;
import modelo.ResultadosTableModel;
import modelo.Token;
import util.EstilosUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private JTextArea areaEntrada;
    private JTable tablaResultados;
    private JButton btnAnalizar;
    private JButton btnLimpiar;
    private final AnalizadorManager analizador;

    public VentanaPrincipal() {
        this.analizador = new AnalizadorManager();
        initComponents();
    }

    private void initComponents() {
        setTitle("Analizador Léxico");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(EstilosUI.COLOR_FONDO);

        // Panel superior
        add(createTopPanel(), BorderLayout.NORTH);

        // Panel central
        add(createCenterPanel(), BorderLayout.CENTER);

        // Panel inferior
        add(createBottomPanel(), BorderLayout.SOUTH);

        ((JComponent) getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(EstilosUI.COLOR_CABECERA);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Analizador Léxico");
        titleLabel.setFont(EstilosUI.getFuenteTitulo());
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(EstilosUI.COLOR_FONDO);

        // Panel de entrada
        areaEntrada = new JTextArea();
        areaEntrada.setFont(EstilosUI.getFuenteNormal());
        JScrollPane scrollEntrada = new JScrollPane(areaEntrada);
        scrollEntrada.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(EstilosUI.COLOR_CABECERA),
            "Código de Entrada"
        ));

        // Tabla de resultados
        tablaResultados = new JTable();
        tablaResultados.setFont(EstilosUI.getFuenteNormal());
        tablaResultados.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(EstilosUI.COLOR_CABECERA),
            "Resultados del Análisis"
        ));

        panel.add(scrollEntrada);
        panel.add(scrollTabla);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(EstilosUI.COLOR_FONDO);

        btnAnalizar = new JButton("Analizar");
        EstilosUI.aplicarEstiloBoton(btnAnalizar, EstilosUI.COLOR_BOTON);
        btnAnalizar.addActionListener(e -> analizarCodigo());

        btnLimpiar = new JButton("Limpiar");
        EstilosUI.aplicarEstiloBoton(btnLimpiar, new Color(188, 93, 88));
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panel.add(btnAnalizar);
        panel.add(btnLimpiar);

        return panel;
    }

    private void analizarCodigo() {
        String codigo = areaEntrada.getText();
        if (codigo.trim().isEmpty()) {
            mostrarError("Por favor, ingrese código para analizar.");
            return;
        }

        try {
            List<Token> tokens = analizador.analizar(codigo);
            tablaResultados.setModel(new ResultadosTableModel(tokens));
            ajustarColumnasTabla();
        } catch (Exception ex) {
            mostrarError("Error al analizar el código: " + ex.getMessage());
        }
    }

        private void ajustarColumnasTabla() {
        for (int i = 0; i < tablaResultados.getColumnCount(); i++) {
            tablaResultados.getColumnModel().getColumn(i).setPreferredWidth(
                switch (i) {
                    case 0 -> 100;  // Línea
                    case 1 -> 200;  // Tipo
                    case 2 -> 300;  // Valor
                    default -> 150;
                }
            );
        }
    }

    private void limpiarCampos() {
        areaEntrada.setText("");
        tablaResultados.setModel(new ResultadosTableModel(List.of()));
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
        });
    }
}
