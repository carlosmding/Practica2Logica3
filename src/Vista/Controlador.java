/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vista;

import Modelo.Diccionario;

/**
 *
 * @author Usuario
 */
public class Controlador {
    private Lienzo objLienzo; 
    private Diccionario objArbol; 

    public Controlador(Lienzo objLienzo, Diccionario objArbol) {
        this.objLienzo = objLienzo;
        this.objArbol = objArbol;
    }
    
    public void iniciar() {
        objLienzo.setObjArbol(objArbol);
    }
}
