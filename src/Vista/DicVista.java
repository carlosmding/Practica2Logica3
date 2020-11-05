/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Diccionario;
import Modelo.NodoEntrada;

/**
 *
 * @author win
 */
public class DicVista {
    
    public static void main(String[] args) throws Exception {
        Diccionario nuevo = new Diccionario();
        nuevo.load();
        
        System.out.println("Raiz: "+nuevo.getRoot().getDato());
        System.out.print("Diccionario: ");
        nuevo.imprimirInOrden(nuevo.getRoot());
        
        nuevo.exportar();
        System.out.println("");
        System.out.println("Hay "+nuevo.getCantidad()+" palabras");

        nuevo.modificarEntrada("carro", "jet");
        NodoEntrada nodoAInsertar = new NodoEntrada("ab");
        nuevo.insertarEntrada(nodoAInsertar);
        
        NodoEntrada nodoAInsertar2 = new NodoEntrada("abcs");
        nuevo.insertarEntrada(nodoAInsertar2);
        
        NodoEntrada nodoAInsertar3 = new NodoEntrada("abc");
        nuevo.insertarEntrada(nodoAInsertar3);
        
        NodoEntrada nodoAInsertar4 = new NodoEntrada("abcd2");
        nuevo.insertarEntrada(nodoAInsertar4);
        
        System.out.println("Raiz: "+nuevo.getRoot().getDato());
        System.out.print("Diccionario: ");
        nuevo.imprimirInOrden(nuevo.getRoot());
        

    }
}
