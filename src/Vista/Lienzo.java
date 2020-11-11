/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Diccionario;
import Modelo.NodoEntrada;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Usuario
 */
public class Lienzo extends JPanel {
    private Diccionario objArbol;
    public static final int DIAMETRO = 30;
    public static final int RADIO = DIAMETRO / 2;
    public static final int ANCHO = 50;

    public void setObjArbol(Diccionario objArbol) {
        this.objArbol = objArbol;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); 
        pintar(g, getWidth() / 2, 20, objArbol.getRoot());
    }
    
    private void pintar(Graphics g, int x, int y, NodoEntrada n) {
        if (n == null)
        {}
        else {
            int EXTRA = n.nodosCompletos(n);
            g.drawOval(x, y, DIAMETRO, DIAMETRO);
            g.drawString(n.getDato().toString(), x + 12, y + 18);
            if (n.getLi()!= null)
                g.drawLine(x + RADIO, y + RADIO, x - ANCHO - EXTRA + RADIO, y + ANCHO + RADIO);
            if (n.getLd() != null)
                g.drawLine(x + RADIO, y + RADIO, x + ANCHO + EXTRA + RADIO, y + ANCHO + RADIO);
            pintar(g, x - ANCHO - EXTRA, y + ANCHO , (NodoEntrada) n.getLi());
            pintar(g, x + ANCHO + EXTRA, y + ANCHO , (NodoEntrada) n.getLd());
        }
    }
}
