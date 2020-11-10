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

import java.io.*;
import java.util.*;

/**
 *
 * @author @param <T>
 */
public class Diccionario<T extends Comparable> {

    private NodoEntrada root;
    private NodoEntrada ult;
    private NodoEntrada x;
    private int cantidad;

    public Diccionario() {
    }

    public NodoEntrada insertarEntrada(NodoEntrada nodoAInsertar) {
        System.out.println("Palabra de la entrada: " + nodoAInsertar.getDato());
        if (root == null) {
            root = nodoAInsertar;
            return root;
        }
        NodoEntrada nodoRecorrido = root; // Registro que uso para buscar
        NodoEntrada padreNodoRecorridoX = null;
        NodoEntrada pivote = root; // Nodo que se puede desbalancear
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

        while ((linea = in.readLine()) != null) {
            NodoAVL raizSinonimos = new NodoAVL(null);
            NodoAVL raizAntonimos = new NodoAVL(null);
            String palabra, otros, referencias = "", significado = "", sinonimos = "", antonimos = "";
            try {
                palabra = (linea.split("\\/"))[0];
                otros = (linea.split("\\/"))[1];
                try {
                    if (otros != "") {
                        referencias = (otros.split("\""))[0];
                        significado = (otros.split("\""))[1];
                        sinonimos = (otros.split("\""))[3];
                        antonimos = (otros.split("\""))[5];

                        //arreglo de sinonimos
                        String sin[] = sinonimos.split("\\,");
                        ArbolAVL arbolSinonimos = new ArbolAVL();
                        for (int i = 0; i < sin.length; i++) {
                            arbolSinonimos.insertarDato(sin[i]);
                            //pendiente quitar los espacios y las comillas    
                        }
                        raizSinonimos = arbolSinonimos.getRoot();

                        //arreglo de antonimos
                        String ant[] = antonimos.split("\\,");
                        ArbolAVL arbolAntonimos = new ArbolAVL();
                        for (int i = 0; i < ant.length; i++) {
                            arbolAntonimos.insertarDato(ant[i]);
                            //pendiente quitar los espacios y las comillas    
                        }
                        raizAntonimos = arbolAntonimos.getRoot();
                    }
                } catch (Exception e) {
                };
            } catch (Exception e) {
                palabra = linea;
            }
            NodoEntrada nuevo = new NodoEntrada(palabra, referencias, significado, raizSinonimos, raizAntonimos);
            insertar(nuevo);
        }
    }

    public void insertar(NodoEntrada nuevo) throws Exception {
        if (buscar(nuevo.getDato()) == null) {
            if (insertarEntrada(nuevo) == null) {
                throw new Exception("Error al insertar la entrada de la palabra " + nuevo.getDato());
            }
            cantidad++;
        }
        // si ya esta repetida que imprime o muestra
    }

    public String modificarEntrada(Comparable dato, Comparable nuevoDato) {
        String resultado = "No se encontro la palabra para modificar";
        NodoEntrada nodoAModificar = buscar(dato);
        if (nodoAModificar == null) {
            return resultado;
        } else {
            NodoEntrada nuevoNodo = new NodoEntrada(nuevoDato, nodoAModificar.getReferencias(), nodoAModificar.getSignificado(), nodoAModificar.getSinonimos(), nodoAModificar.getAntonimos());
            NodoEntrada raizListaNodos = crearLista(nodoAModificar); // creo una lista ligada con todos los nodos excepto el nodo a modificar
            insertarLista(raizListaNodos);
            insertarEntrada(nuevoNodo);
            resultado = "Palabra modificada correctamente";
        }
        return resultado;
    }


    public NodoEntrada crearLista(NodoEntrada busqueda) {
        Stack<NodoEntrada> migas = new Stack<>();
        NodoEntrada recorrido = root;
        NodoEntrada cabezaLista = new NodoEntrada("");
        NodoEntrada ultimoLista = cabezaLista;
        migas.add(recorrido);
        recorrido = (NodoEntrada) recorrido.getLi();
        while (!migas.isEmpty() || recorrido != null) {
            if (recorrido != null) {
                migas.add(recorrido);
                recorrido = (NodoEntrada) recorrido.getLi();
            } else {
                recorrido = migas.pop();
                if (recorrido != busqueda) {
                    NodoEntrada nuevoNodo = new NodoEntrada(recorrido.getDato(), recorrido.getReferencias(), recorrido.getSignificado(), recorrido.getSinonimos(), recorrido.getAntonimos());
                    ultimoLista.setLd(nuevoNodo);
                    ultimoLista = (NodoEntrada) ultimoLista.getLd();
                }
                recorrido = (NodoEntrada) recorrido.getLd();
            }
        }
        return cabezaLista;
    }

    public void insertarLista(NodoEntrada raiz) {
        root = null;
        NodoEntrada nodoAIngresar = (NodoEntrada) raiz.getLd();
        while (nodoAIngresar != null) {
            insertarEntrada(nodoAIngresar);
            nodoAIngresar = (NodoEntrada) nodoAIngresar.getLd();
        }
    }

    public String imprimirInOrden(NodoEntrada raiz) {
        String cadena = "";
        Stack<NodoEntrada> migas = new Stack<>();
        migas.add(raiz);
        raiz = (NodoEntrada) raiz.getLi();
        while (!migas.isEmpty() || raiz != null) {
            if (raiz != null) {
                migas.add(raiz);
                raiz = (NodoEntrada) raiz.getLi();
            } else {
                raiz = migas.pop();
                cadena += (raiz.getDato() + " ");
                raiz = (NodoEntrada) raiz.getLd();
            }
        }
        return cadena;
    }

    public String obtenerResultadosArbolesAVL(NodoAVL root) {
        String resultadosSinAnt = "";
        if (root == null) {
            return resultadosSinAnt;
        } else {
            NodoBinarioGenerico recorrido = (NodoBinarioGenerico) root;
            Stack<NodoBinarioGenerico> migas = new Stack<>();
            migas.add(recorrido);
            recorrido = recorrido.getLi();
            boolean primero = true;
            while (!migas.isEmpty() || recorrido != null) {
                if (recorrido != null) {
                    migas.add(recorrido);
                    recorrido = recorrido.getLi();
                } else {
                    recorrido = migas.pop();
                    if (primero) {
                        resultadosSinAnt += recorrido.getDato();
                        primero = false;
                    } else {
                        resultadosSinAnt += "," + recorrido.getDato();
                    }
                    recorrido = recorrido.getLd();
                }
            }
            return resultadosSinAnt;
        }

    }

    public void exportar() throws FileNotFoundException, IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter("Exportar.dic"));
        PrintWriter ingresarLinea = new PrintWriter(out);
        String linea;
        Stack<NodoBinarioGenerico> migas = new Stack<>();
        NodoBinarioGenerico recorrido = (NodoBinarioGenerico) root;
        migas.add(recorrido);
        recorrido = recorrido.getLi();
        while (!migas.isEmpty() || recorrido != null) {
            if (recorrido != null) {
                migas.add(recorrido);
                recorrido = recorrido.getLi();
            } else {
                recorrido = migas.pop();
                NodoEntrada nodoEvaluado = (NodoEntrada) recorrido;
                if (nodoEvaluado.getReferencias() == "") {
                    linea = (String) nodoEvaluado.getDato();
                    ingresarLinea.println(linea);
                } else if (nodoEvaluado.getSignificado() == "") {
                    linea = (String) nodoEvaluado.getDato() + "/" + (String) nodoEvaluado.getReferencias();
                    ingresarLinea.println(linea);
                } else {
                    String sin = obtenerResultadosArbolesAVL(nodoEvaluado.getSinonimos());
                    String ant = obtenerResultadosArbolesAVL(nodoEvaluado.getAntonimos());
                    linea = (String) nodoEvaluado.getDato() + "/" + nodoEvaluado.getReferencias() + "\"" + nodoEvaluado.getSignificado() + "\":\"" + sin + "\":\"" + ant + "\"";
                    ingresarLinea.println(linea);
                }
                recorrido = recorrido.getLd();
            }
        }
        out.close();
    }

    public int getCantidad() {
        return cantidad;
    }

    public NodoEntrada buscarPadre(NodoEntrada hijo) {
        NodoEntrada recorrido = root;
        if (buscar(hijo.getDato()) == null) {
            return null;
        } else {
            Stack<NodoEntrada> ancestros = new Stack<>();
            while (recorrido != null) {
                if (recorrido.getDato().compareTo(hijo.getDato()) == 0) {
                    NodoEntrada padre = ancestros.pop();
                    return padre;
                } else if (recorrido.getDato().compareTo(hijo.getDato()) < 0) {
                    if (recorrido.getLd() != null) {
                        ancestros.push(recorrido);
                        recorrido = (NodoEntrada) recorrido.getLd();
                    } else {
                        return null;
                    }
                } else {
                    if (recorrido.getLi() != null) {
                        ancestros.push(recorrido);
                        recorrido = (NodoEntrada) recorrido.getLi();
                    } else {
                        return null;
                    }
                }
            }
        }
        return recorrido;
    }

    public String ingresarNuevaPalabra(Comparable dato, String datos) {
        String resultado = "Palabra ya ingresada";
        if (buscar(dato) == null) {
            String palabra, otros, referencias = "", significado = "", sinonimos = "", antonimos = "";
            palabra = (datos.split("\\/"))[0];
            otros = (datos.split("\\/"))[1];
            referencias = (otros.split("\""))[0];
            significado = (otros.split("\""))[1];
            sinonimos = (otros.split("\""))[3];
            antonimos = (otros.split("\""))[5];

            //arreglo de sinonimos
            NodoAVL raizSinonimos = new NodoAVL(null);
            NodoAVL raizAntonimos = new NodoAVL(null);

            String sin[] = sinonimos.split("\\,");
            ArbolAVL arbolSinonimos = new ArbolAVL();
            for (int i = 0; i < sin.length; i++) {
                arbolSinonimos.insertarDato(sin[i]);
                if (buscar(sin[i]) == null) {
                    NodoEntrada nuevoNodo = new NodoEntrada(sin[i]);
                    nuevoNodo.setSignificado(significado);
                    NodoAVL rootsinonimos;
                    ArbolAVL arbolitosin = new ArbolAVL();
                    arbolitosin.insertarDato(palabra);
                    rootsinonimos = arbolitosin.getRoot();
                    nuevoNodo.setSinonimos(rootsinonimos);
                    insertarEntrada(nuevoNodo);
                } else {
                    NodoAVL raizsin = (buscar(sin[i])).getSinonimos();
                    if (raizsin != null) {
                        NodoAVL nodoEncontrado = existeNodoAVL(raizsin, palabra);
                        if (nodoEncontrado == null) {
                            NodoAVL raiznueva;
                            String listadosinonimos = obtenerResultadosArbolesAVL(raizsin);
                            listadosinonimos += "," + palabra;
                            String listado[] = listadosinonimos.split("\\,");
                            ArbolAVL arbolSin = new ArbolAVL();
                            for (int j = 0; j < listado.length; j++) {
                                arbolSin.insertarDato(listado[j]);
                                //pendiente quitar los espacios    
                            }
                            raiznueva = arbolSin.getRoot();
                            buscar(sin[i]).setSinonimos(raiznueva);
                        }
                    }
                }
                //pendiente quitar los espacios   
            }
            raizSinonimos = arbolSinonimos.getRoot();

            //arreglo de antonimos
            String ant[] = antonimos.split("\\,");
            ArbolAVL arbolAntonimos = new ArbolAVL();
            for (int i = 0; i < ant.length; i++) {
                arbolAntonimos.insertarDato(ant[i]);
                if (buscar(ant[i]) == null) {
                    NodoEntrada nuevoNodo = new NodoEntrada(ant[i]);
                    insertarEntrada(nuevoNodo);
                }
                //pendiente quitar los espacios  
            }
            raizAntonimos = arbolAntonimos.getRoot();

            NodoEntrada nuevaEntrada = new NodoEntrada(palabra, referencias, significado, raizSinonimos, raizAntonimos);
            insertarEntrada(nuevaEntrada);
            resultado = "Palabra ingresada correctamente";
        }
        return resultado;
    }

    public NodoAVL existeNodoAVL(NodoAVL raiz, Comparable dato) {
        Stack<NodoAVL> migas = new Stack<>();
        migas.add(raiz);
        raiz = (NodoAVL) raiz.getLi();
        while (!migas.isEmpty() || raiz != null) {
            if (raiz != null) {
                migas.add(raiz);
                raiz = (NodoAVL) raiz.getLi();
            } else {
                raiz = migas.pop();
                if (raiz.getDato() == dato) {
                    return raiz;
                }
                raiz = (NodoAVL) raiz.getLd();
            }
        }
        return null;
    }
}
