/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos.Memetico;

import Utils.Restricciones;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ptondreau
 */
public class Hibrido {
    
    protected static int numIndividuos = 20;
    protected static int numEvaluaciones = 0;
    protected static int numGeneraciones = 0;
    
    protected static List<Integer> resultado = new ArrayList<>();
    protected static List<List<Integer>> padres = new ArrayList<>();
    protected static List<List<Integer>> hijos = new ArrayList<>();
    
    protected static List<List<Integer>> frecuencias = new ArrayList<>();
    protected static List<Integer> transmisores = new ArrayList<>();
    protected static Restricciones restricciones;
    
    public Hibrido ( List<List<Integer>> _frecuencias, List<Integer> _transmisores,
            Restricciones _restricciones ){
        
        frecuencias = _frecuencias;
        transmisores = _transmisores;
        restricciones = _restricciones;
        
        for ( int i = 0; i < numIndividuos; i++ ) {
            padres.add(new ArrayList<> ());
            resultado.add(0);
        }
        
    }
    
    public void algoritmo () throws FileNotFoundException {
        
        Generacional genetico = new Generacional ();
        while ( numEvaluaciones < 20000 ) {
            
            genetico.algoritmo();
            for ( int i = 0; i < numIndividuos; i++ ){
                BusquedaLocal bl = new BusquedaLocal (i);
                bl.algoritmo();
            }
            
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

    for ( int i = 0; i < mejorIndividuo.size(); i ++ ) {
        if ( mejorIndividuo.get(i) != 0 ) {
            System.out.println("Transmisor " + (i + 1) + ": " + mejorIndividuo.get(i));
        }
    }

    System.out.println(resultado.get(actual));
}
    
}
