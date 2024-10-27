package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JComponent;

public class EstilosUI {
    public static final Color COLOR_FONDO = new Color(240, 248, 255);
    public static final Color COLOR_CABECERA = new Color(70, 130, 180);
    public static final Color COLOR_BOTON = new Color(0, 191, 255);
    public static final Color COLOR_TEXTO = new Color(25, 25, 112);
    
    public static void aplicarEstiloBoton(JButton boton, Color colorFondo) {
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(120, 40));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo.brighter());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorFondo);
            }
        });
    }
    
    public static Font getFuenteNormal() {
        return new Font("Consolas", Font.PLAIN, 14);
    }
    
    public static Font getFuenteTitulo() {
        return new Font("Arial", Font.BOLD, 24);
    }
}