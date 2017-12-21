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
import java.util.Collections;
import java.util.List;
import static main.main.NUMERO;

/**
 *
 * @author ptondreau
 */
public class Hibrido {
    
    public static boolean AM1 = false;
    public static boolean AM2 = false;
    public static boolean AM3 = false;
    
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
            if ( AM1 ) {
                for ( int i = 0; i < numIndividuos; i++ ){
                    System.out.print(i+" ");
                    BusquedaLocal bl = new BusquedaLocal (i);
                    bl.algoritmo();
                    System.out.println();
                }
            } else if ( AM2 ) {
                for ( int i = 0; i < numIndividuos*0.1; i++ ){
                    int numAleatorio = NUMERO.nextInt(numIndividuos);
                    BusquedaLocal bl = new BusquedaLocal (numAleatorio);
                    bl.algoritmo();
                }
            } else {
                List<Integer> auxR = new ArrayList<>();
                List<Integer> auxR2 = new ArrayList<>();
                auxR2.addAll(resultado);
                auxR.addAll(resultado);
                Collections.sort(auxR);

                for ( int i = 0; i < numIndividuos*0.1; i++ ){                 
                    int indice = auxR2.indexOf(auxR.get(i));
                    auxR2.remove(indice);
                    BusquedaLocal bl = new BusquedaLocal (indice);
                    bl.algoritmo();
                }
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
