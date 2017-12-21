/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos.Memetico;

import Utils.Restricciones;
import static Utils.Utilidades.rDiferencia;
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
    protected static int ejecucion = 0;
    
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
            System.out.println("Ejecucion de generacional");
            genetico.algoritmo();
            for ( int i = 0; i < numIndividuos; i++ ){
                System.out.print(i+" ");
                BusquedaLocal bl = new BusquedaLocal (i);
                bl.algoritmo();
                System.out.println();
            }
            ejecucion++;
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
    
    for ( int i = 0; i < mejorIndividuo.size(); i ++ ) {
        listaRest = restricciones.restriccionesTransmisor(i);
        if ( listaRest.size() > 0 ) {
            System.out.println("Transmisor " + (i + 1) + ": " + mejorIndividuo.get(i));
        }
    }

    System.out.println(minimo);
}
    
}
