/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Greedy;

import Utils.Restricciones;
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
public class Greedy {
    int numIndividuos = 20;
    
    List<List<Integer>> padres;
    List<List<Integer>> frecuencias;
    List<Integer> transmisores;
    Restricciones restricciones;
    
    List<Integer> resultado;
    
    public Greedy (List<List<Integer>> _frecuencias, List<Integer> _transmisores,
            Restricciones _restricciones ){
        
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
        List <Integer> frecuenciasR = new ArrayList<>();
        List<List<Integer>> listaRest = new ArrayList();
        Random numero = NUMERO;
        
        for ( int k = 0; k < numIndividuos; k++) {
            for ( int i = 0; i < transmisores.size(); i++ ) {
                listaRest = restricciones.restriccionesTransmisor(i);
                if ( listaRest.size() > 0 ) {
                    int indiceFrecAleatorio = numero.nextInt(frecuencias.get(transmisores.get(i)).size());
                    int frecAleatoria = frecuencias.get(transmisores.get(i)).get(indiceFrecAleatorio);
                    frecuenciasR.add(frecAleatoria);
                } else 
                    frecuenciasR.add(0);
            }

            int diferencia = rDiferencia(frecuenciasR, restricciones);
            resultado.set(k, diferencia);
            padres.get(k).addAll(frecuenciasR);
            frecuenciasR.clear();
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

        System.out.println("Resultado: "+minimo);
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
