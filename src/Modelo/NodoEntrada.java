/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author win
 */
public class NodoEntrada<T extends Comparable> extends NodoAVL {
    
    private String significado;
    private String referencias;
    private NodoAVL sinonimos;
    private NodoAVL antonimos;
    
    public NodoEntrada(T dato, String ref, String sig, NodoAVL sin, NodoAVL ant ) {
        super(dato);
        this.referencias = ref;
        this.significado = sig;
        this.sinonimos = sin;
        this.antonimos = ant;
    }

    public NodoEntrada(Comparable dato) {
        super(dato);
    }
    
    public NodoEntrada(Comparable dato,String ref){
        super(dato);
        this.referencias = ref;
    }
    
   
    public String getSignificado() {
        return significado;
    }

    public void setSignificado(String significado) {
        this.significado = significado;
    }

    public NodoAVL getSinonimos() {
        return sinonimos;
    }

    public void setSinonimos(NodoAVL sinonimos) {
        this.sinonimos = sinonimos;
    }

    public NodoAVL getAntonimos() {
        return antonimos;
    }

    public void setAntonimos(NodoAVL antonimos) {
        this.antonimos = antonimos;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }
    
    
}
