/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos.Memetico;

import static Algoritmos.Memetico.Hibrido.*;
import static Utils.Utilidades.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static main.main.NUMERO;

/**
 *
 * @author pierrest
 */
public class BusquedaLocal {

    public static int numIteraciones = 200;
    int id;
    //List<Integer> frecuenciasR; // Cada posicion es la frecuencia asignada a dicho transmisor

    public BusquedaLocal ( int _id ) {
//        for ( int i = 0; i < numIndividuos; i++ ) {
//            System.out.println("Resultado "+i+" "+resultado.get(i));
//            if ( resultado.get(i) < 0 ) 
//                System.out.println("Ejecucion "+ejecucion);
//        }
        //frecuenciasR = padres.get(id);
        id = _id;
        
    }
    
//    public BusquedaLocal ( listaTransmisores _transmisores, rangoFrec _frecuencias, Restricciones _restricciones ) throws FileNotFoundException {
//        frecuencias = _frecuencias.rangoFrecuencias;
//        transmisores = _transmisores.transmisores;
//        restricciones = _restricciones;
//        
//        if ( numIteraciones == 0 )
//            numIteraciones = 200;
//        
//        for ( int i = 0; i < transmisores.size(); i ++ ) {
//            int indiceFrecAleatoria = NUMERO.nextInt(frecuencias.get(transmisores.get(i)).size());
//            int frecAleatoria = frecuencias.get(transmisores.get(i)).get(indiceFrecAleatoria);
//            frecuenciasR.add(frecAleatoria);
//        }
//        
//        resultado = rDiferencia(frecuenciasR, rest);
//        
//    }
    
    /**
     * Algoritmo greedy: Asignar un valor al transmisor de forma iterativa e ir
     * calculando uno por uno. Si el resultado mejora sustituir la lista de
     * solución
     *
     */
    public void algoritmo () throws FileNotFoundException {    
        //System.out.println("PADRE "+id);
        Random numero = NUMERO;
        int token = numero.nextInt(transmisores.size());
        for ( int i = 0; i < numIteraciones; i ++ ) {
            List<Integer> nuevaSolucion = new ArrayList<>();
            double sentido = numero.nextDouble();
            int valorInicial = padres.get(id).get(token); // Se obtiene la frecuencia del token
            int indiceInicial;
            int nuevoCoste = Integer.MAX_VALUE;

            indiceInicial = frecuencias.get(transmisores.get(token)).indexOf(valorInicial); // Mas corto que codigo de abajo
            int indiceOriginal = indiceInicial;
            
            List<List<Integer>> listaRest = new ArrayList<>();
            listaRest = restricciones.restriccionesTransmisor(token);
            if ( listaRest.size() > 0 ) {
                if ( sentido < 0.5 ) {
                    boolean encontrado = false;
                    indiceInicial = Math.floorMod(indiceInicial-1, frecuencias.get(transmisores.get(token)).size());
                    while( ! encontrado && indiceInicial != indiceOriginal ) {
                        int fact1 = rDiferencia(padres.get(id), token, restricciones);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        nuevaSolucion.addAll(padres.get(id));
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token, restricciones);

                        if ( fact2 < fact1 ) {
                            padres.get(id).set(token, valorInicial);
                            resultado.set(id, rDiferencia(nuevaSolucion, restricciones));
                            encontrado = true;
                        }
                        indiceInicial = Math.floorMod(indiceInicial-1, frecuencias.get(transmisores.get(token)).size());
                        nuevaSolucion.clear();
                    }
                } else {
                    boolean encontrado = false;
                    indiceInicial = Math.floorMod(indiceInicial-1, frecuencias.get(transmisores.get(token)).size());
                    while( ! encontrado && indiceInicial != indiceOriginal  ) {
                        int fact1 = rDiferencia(padres.get(id), token, restricciones);
                        valorInicial = frecuencias.get(transmisores.get(token)).get(indiceInicial);
                        nuevaSolucion.addAll(padres.get(id));
                        nuevaSolucion.set(token, valorInicial);
                        int fact2 = rDiferencia(nuevaSolucion, token, restricciones);

                        if ( fact2 < fact1 ) {
                            padres.get(id).set(token, valorInicial);
                            resultado.set(id, rDiferencia(nuevaSolucion, restricciones));
                            encontrado = true;
                        }
                        indiceInicial = Math.floorMod(indiceInicial+1, frecuencias.get(transmisores.get(token)).size());
                        nuevaSolucion.clear();
                    }
                }
                token = Math.floorMod(token + 1, transmisores.size());
                numEvaluaciones++;
            }
        }    
    }

    /**
     * Función que devuelve por pantalla los resultados obtenidos
     */
    public void resultados () throws FileNotFoundException {

        List<List<Integer>> listaTrans = new ArrayList<>();
        for ( int i = 0; i < transmisores.size(); i ++ ) {
            listaTrans = restricciones.restriccionesTransmisor(i);
            if ( listaTrans.size() > 0 ) {
                System.out.println("Transmisor " + (i + 1) + ": " + padres.get(id).get(i));
            }
        }
        System.out.println("Coste: " + rDiferencia(padres.get(id), restricciones));
    }

}
