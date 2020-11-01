/*
 * Copyright 2019 Carlos Alejandro Escobar Marulanda ealejandro101@gmail.com
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation 
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions:
 * The above copyright notice and this permission notice shall 
 * be included in all copies or substantial portions of the 
 * Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package Modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author @param <T>
 */
public class Diccionario<T extends Comparable> {

    NodoEntrada root;
    NodoEntrada ult;
    NodoEntrada x;
    int cantidad;

    public Diccionario() {
    }

    public NodoEntrada insertarEntrada(NodoEntrada nodoAInsertar) {

        if (root == null) {
            root = nodoAInsertar;
            return root;
        }
        NodoEntrada nodoRecorrido = root; // Registro que uso para buscar
        NodoEntrada padreNodoRecorridoX = null;
        NodoEntrada pivote = root; // Nodo que se puede desvalancear
        NodoEntrada padrePivote = null;
        NodoEntrada nodoRecorridoParaFB;
        NodoEntrada q;

        /**
         * Busqueda donde insertar también va dejando el rastro de donde
         * posiblemente se puede desbalancear
         *
         */
        while (nodoRecorrido != null) {
            // Validar si el nodo x esta en riesgo de desbalanceo
            if (nodoRecorrido.getfB() != 0) {
                pivote = nodoRecorrido;
                padrePivote = padreNodoRecorridoX;
            }
            // 
            int comparacion = nodoAInsertar.getDato().compareTo(nodoRecorrido.getDato());
            if (comparacion == 0) {
                //es un dato existente
                return nodoRecorrido;
            } else if (comparacion < 0) {
                // n es menor
                padreNodoRecorridoX = nodoRecorrido;
                nodoRecorrido = (NodoEntrada) nodoRecorrido.getLi();
            } else {
                //n es mayor
                padreNodoRecorridoX = nodoRecorrido;
                nodoRecorrido = (NodoEntrada) nodoRecorrido.getLd();
            }
        }

        /**
         * Insertar el dato
         */
        if (nodoAInsertar.getDato().compareTo(padreNodoRecorridoX.getDato()) > 0) {
            padreNodoRecorridoX.setLd(nodoAInsertar);
        } else if (nodoAInsertar.getDato().compareTo(padreNodoRecorridoX.getDato()) < 0) {
            padreNodoRecorridoX.setLi(nodoAInsertar);
        }

        /**
         * Calcular los nuevos factores de balance de todos los ancestros del
         * nodo insertado
         */
        if (nodoAInsertar.getDato().compareTo(pivote.getDato()) > 0) {
            pivote.setfB(pivote.getfB() - 1);
            nodoRecorridoParaFB = (NodoEntrada) pivote.getLd();
        } else {
            pivote.setfB(pivote.getfB() + 1);
            nodoRecorridoParaFB = (NodoEntrada) pivote.getLi();
        }
        q = nodoRecorridoParaFB;

        while (nodoRecorridoParaFB != nodoAInsertar) {
            if (nodoAInsertar.getDato().compareTo(nodoRecorridoParaFB.getDato()) > 0) {
                nodoRecorridoParaFB.setfB(nodoRecorridoParaFB.getfB() - 1);
                nodoRecorridoParaFB = (NodoEntrada) nodoRecorridoParaFB.getLd();
            } else {
                nodoRecorridoParaFB.setfB(nodoRecorridoParaFB.getfB() + 1);
                nodoRecorridoParaFB = (NodoEntrada) nodoRecorridoParaFB.getLi();
            }
        }

        /**
         * Rebalancear
         */
        if (!((pivote.getfB() == -2) || (pivote.getfB() == 2))) {
            return nodoAInsertar;
        }

        // Estamos tentados a cambiar de raiz
        NodoEntrada nuevaRaizSubArbol = null;
        if (pivote.getfB() == +2) {
            if (q.getfB() == +1) {
                System.out.println("rotacionDerecha");
                nuevaRaizSubArbol = rotacionDerecha(pivote, q);
            } else {
                System.out.println("dobleRotacionDerecha");
                nuevaRaizSubArbol = dobleRotacionDerecha(pivote, q);
            }
        } else if (pivote.getfB() == -2) {
            if (q.getfB() == -1) {
                System.out.println("rotacionIzquierda");
                nuevaRaizSubArbol = rotacionIzquierda(pivote, q);

            } else {
                System.out.println("dobleRotacionIzquierda");
                nuevaRaizSubArbol = dobleRotacionIzquierda(pivote, q);
            }
        }

        /**
         * Consecuencias de rebalancear, validación de que pivote no fuera la
         * raíz
         */
        if (padrePivote == null) {
            root = nuevaRaizSubArbol;
            return nodoAInsertar;
        }

        /**
         * Liga el padre del pivote con la nueva raiz
         */
        if (padrePivote.getLi() == pivote) {
            padrePivote.setLi(nuevaRaizSubArbol);
        } else {
            padrePivote.setLd(nuevaRaizSubArbol);
        }
        return nodoAInsertar;
    }

    /**
     * 1. void unaRotacionALaDerecha(NodoEntrada p, NodoEntrada q) 2.
     * p.asignaLi(q.retornaLd()) 3. q.asignaLd(p) 4. p.asignaFb(0) 5.
     * q.asignaFb(0) 6. fin(unaRotacionALaDerecha)
     *
     * @param pivote
     * @param q
     */
    private NodoEntrada rotacionDerecha(NodoEntrada p, NodoEntrada q) {
        p.setLi(q.getLd());
        q.setLd(p);
        p.setfB(0);
        q.setfB(0);
        return q;
    }

    /**
     * 1. void dobleRotacionALaDerecha(NodoEntrada p, NodoEntrada q) 2. r =
     * q.retornaLd() 3. q.asignaLd(r.retornaLi()) 4. r.asignaLi(q) 5.
     * p.asignaLi(r.retornaLd() 6. r.asignaLd(p) 7. casos de r.retornaFb() 8. 0:
     * p.asignaFb(0) 9. q.asignaFb(0) 10. break 11. 1: p.asignaFb(–1) 12.
     * q.asignaFb(0) 13. break 14. –1: p.asignaFb(0) 15. q.asignaFb(1) 16.
     * fin(casos) 17. r.asignaFb(0) 18. q = r 19. fin(dobleRotacionALaDerecha)
     *
     * @param pivote
     * @param q
     */
    private NodoEntrada dobleRotacionDerecha(NodoEntrada p, NodoEntrada q) {
        NodoEntrada r = (NodoEntrada) q.getLd();
        q.setLd(r.getLi());
        r.setLi(q);
        p.setLi(r.getLd());
        r.setLd(p);
        switch (r.getfB()) {
            case 0:
                p.setfB(0);
                q.setfB(0);
                break;
            case 1:
                p.setfB(-1);
                q.setfB(0);
                break;
            case -1:
                p.setfB(0);
                q.setfB(1);
                break;
        }
        r.setfB(0);
        return r;
    }

    /**
     * 1. void unaRotacionALaIzquierda(NodoEntrada p, NodoEntrada q) 2.
     * p.asignaLd(q.retornaLi()) 3. q.asignaLi(p) 4. p.asignaFb(0) 5.
     * q.asignaFb(0) 6. fin(unaRotacionALaIzquierda)
     *
     * @param pivote
     * @param q
     */
    private NodoEntrada rotacionIzquierda(NodoEntrada p, NodoEntrada q) {
        p.setLd(q.getLi());
        q.setLi(p);
        p.setfB(0);
        q.setfB(0);
        return q;
    }

    /**
     *
     * @param p
     * @param q
     */
    private NodoEntrada dobleRotacionIzquierda(NodoEntrada p, NodoEntrada q) {
        NodoEntrada r = (NodoEntrada) q.getLi();
        q.setLi(r.getLd());
        r.setLd(q);

        p.setLd(r.getLi());
        r.setLi(p);
        switch (r.getfB()) {
            case 0:
                p.setfB(0);
                q.setfB(0);
                break;
            case -1:
                p.setfB(1);
                q.setfB(0);
                break;
            case 1:
                p.setfB(0);
                q.setfB(-1);
                break;
        }
        r.setfB(0);
        return r;
    }

    /**
     * Retorna el nodo si se encuentra o null en caso de no encontrarse
     *
     * @param dato
     * @return
     */
    public NodoEntrada buscar(Comparable dato) {
        NodoEntrada aux = root;
        while (aux != null) {

            if (aux.getDato().compareTo(dato) == 0) {
                return aux;
            } else if (aux.getDato().compareTo(dato) < 0) {
                if (aux.getLd() != null) {
                    aux = (NodoEntrada) aux.getLd();
                } else {
                    return null;
                }
            } else {
                if (aux.getLi() != null) {
                    aux = (NodoEntrada) aux.getLi();
                } else {
                    return null;
                }
            }
        }
        return aux;
    }

    public NodoEntrada getRoot() {
        return root;
    }

    public void load() throws FileNotFoundException, IOException, Exception {
        BufferedReader in = new BufferedReader(new FileReader("Palabras.dic"));
        String linea;
        NodoAVL raizSinonimos = new NodoAVL (null);
        NodoAVL raizAntonimos = new NodoAVL (null);
        
        while ((linea = in.readLine()) != null) {
            String palabra, otros, significado="", sinonimos="", antonimos="";
            try {
                palabra = (linea.split("\\/"))[0];
                System.out.println("Palabra: " + palabra);
                otros = (linea.split("\\/"))[1];
                System.out.println("Sobrante> " + otros);

                try {
                    if (otros != "") {
                        significado = (otros.split("\\:"))[1];
                        sinonimos = (otros.split("\\:"))[2];
                        antonimos = (otros.split("\\:"))[3];
                        System.out.println(significado + "" + sinonimos + "" + antonimos);
                        
                        //arreglo de sinonimos
                        String sin [] = sinonimos.split("\\,");
                        ArbolAVL arbolSinonimos = new ArbolAVL();
                        for(int i=0; i<sin.length; i++){
                            arbolSinonimos.insertarDato(sin[i]);
                            //pendiente quitar los espacios y las comillas    
                        }
                        raizSinonimos = arbolSinonimos.getRoot();

                        //arreglo de antonimos
                        String ant [] = antonimos.split("\\,");
                        ArbolAVL arbolAntonimos = new ArbolAVL();
                        for(int i=0; i<ant.length; i++){
                            arbolAntonimos.insertarDato(ant[i]);
                            //pendiente quitar los espacios y las comillas    
                        }
                        raizAntonimos = arbolAntonimos.getRoot();
                    }
                } catch (Exception e) {
                    System.out.println("Palabra sola");
                };

            } catch (Exception e) {
                palabra = linea;
                System.out.println(palabra);
            }
            
            NodoEntrada nuevo = new NodoEntrada(palabra, significado, raizSinonimos, raizAntonimos);
            insertar(nuevo);
            System.out.println(cantidad);
        }
    }

    public void insertar(NodoEntrada nuevo) throws Exception {
        if (buscar(nuevo.getDato()) == null) {
            if (insertarEntrada(nuevo) == null) {
                throw new Exception("Error al insertar la entrada de la palabra " + nuevo.getDato());
            }
            cantidad++;
        }
    }

}
