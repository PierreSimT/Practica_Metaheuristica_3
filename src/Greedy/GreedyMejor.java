/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Greedy;

import Utils.Restricciones;
import static Utils.Utilidades.compruebaTransmisores;
import static Utils.Utilidades.rDiferencia;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static main.main.NUMERO;

/**
 *
 * @author psfue
 */
public class GreedyMejor {
        int numIndividuos = 20;
    
    List<List<Integer>> frecuencias;
    List<List<Integer>> padres;
    List<Integer> transmisores;
    Restricciones restricciones;
    
    List<Integer> resultado;
    
    public GreedyMejor ( List<List<Integer>> _frecuencias, List<Integer> _transmisores,
            Restricciones _restricciones ) {
        
        resultado = new ArrayList<>();
        padres = new ArrayList<>();
        
        frecuencias = _frecuencias;
        transmisores = _transmisores;
        restricciones = _restricciones;
        
        for ( int i = 0; i < numIndividuos; i++ ) {
            padres.add(new ArrayList<> ());
            resultado.add(0);
        }
    }
    
    public void algoritmo () throws FileNotFoundException {
        
        List<Integer> frecuenciasR = new ArrayList<>();

        for ( int k = 0; k < numIndividuos; k++ ){
        for ( int i = 0; i < transmisores.size(); i ++ ) {
            frecuenciasR.add(0);
        }

        Random numero = NUMERO;
        int seleccionado = numero.nextInt(transmisores.size());

        int tamanio = frecuencias.get(transmisores.get(seleccionado)).size();
        int frecuenciaRandom = frecuencias.get(transmisores.get(seleccionado)).get(numero.nextInt(tamanio));
        frecuenciasR.set(seleccionado, frecuenciaRandom);

        List<List<Integer>> listaRestric = new ArrayList<>();
        int transmisor = 0;
        boolean fin = false;
        while( transmisor < transmisores.size() ) {
                listaRestric = restricciones.restriccionesTransmisor(transmisor);
                if ( transmisor != seleccionado && listaRestric.size() > 0 ) {

                    int minimo = Integer.MAX_VALUE;
                    boolean encontrado = false;
                    int frecuenciaR = 0;
                    int frecuencia;
                    int pos = 0;

                    int valor = 0; //Sacado del bucle while

                    while( pos < frecuencias.get(transmisores.get(transmisor)).size() &&  ! encontrado ) {

                        List<Integer> nuevaLista = new ArrayList<>();
                        nuevaLista.addAll(frecuenciasR);

                        frecuencia = frecuencias.get(transmisores.get(transmisor)).get(pos);
                        nuevaLista.set(transmisor, frecuencia);
                        List<List<Integer>> listaRest = compruebaTransmisores(transmisor, restricciones, frecuenciasR);

                        if ( listaRest.size() > 0 ) { // Lista no vacía, se selecciona frecuencia que afecte lo menos posible al resultado

                            valor = rDiferencia(nuevaLista, listaRest);
                            if ( valor < minimo ) {
                                minimo = valor;
                                frecuenciaR = frecuencia;
                                if ( valor == 0 ) // Si la suma de todas las restricciones = 0 entonces es el mejor resultado posible
                                {
                                    encontrado = true;
                                }
                            }
                        } else { // En caso de que la lista este vacía no hay restricciones que se puedan satisfacer -> frecuencia aleatoria

                            tamanio = frecuencias.get(transmisores.get(transmisor)).size();
                            frecuenciaR = frecuencias.get(transmisores.get(transmisor)).get(numero.nextInt(tamanio));
                            valor = 0;
                            encontrado = true;
                        }
                        pos ++;
                    }
                    frecuenciasR.set(transmisor, frecuenciaR);
                }
                transmisor ++;
            }
            resultado.set(k, rDiferencia(frecuenciasR, restricciones));
            padres.get(k).addAll(frecuenciasR);
            frecuenciasR.clear(); // Borra todos los elementos anteriores para nueva solucion
        }
    }
    
    public void resMejorIndividuo () throws FileNotFoundException {
        int minimo = Integer.MAX_VALUE;
        int actual = 0;
        for ( int i = 0; i < numIndividuos; i ++ ) {
            if ( resultado.get(i) < minimo ) {
                minimo = resultado.get(i);
                actual = i;
            }
        }
        List<Integer> mejorIndividuo = padres.get(actual);
        List<List<Integer>> listaRest = new ArrayList<>();

        for ( int i = 0; i < padres.get(actual).size()-1; i ++ ) {
            System.out.println("Transmisor " + (i + 1) + ": " + padres.get(actual).get(i));
        }

        System.out.println(minimo);
    }
    
    public int resultadoFinal() {
        int minimo = Integer.MAX_VALUE;
        int actual = 0;
        for ( int i = 0; i < numIndividuos; i ++ ) {
            if ( resultado.get(i) < minimo ) {
                minimo = resultado.get(i);
                actual = i;
            }
        }
        
        return minimo;
    }

}
