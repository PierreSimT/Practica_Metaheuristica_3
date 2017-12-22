/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algoritmos.Memetico;

import Utils.Restricciones;
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
    
    protected static List<Integer> resultado;
    protected static List<List<Integer>> padres;
    protected static List<List<Integer>> hijos;
    
    protected static List<List<Integer>> frecuencias = new ArrayList<>();
    protected static List<Integer> transmisores = new ArrayList<>();
    protected static Restricciones restricciones;
    
    public Hibrido ( List<List<Integer>> _frecuencias, List<Integer> _transmisores,
            Restricciones _restricciones ){
        
        resultado = new ArrayList<>();
        padres = new ArrayList<>();
        hijos = new ArrayList();

        ejecucion = 0;
        numGeneraciones = 0;
        numEvaluaciones = 0;
        
        frecuencias = _frecuencias;
        transmisores = _transmisores;
        restricciones = _restricciones;
        
        for ( int i = 0; i < numIndividuos; i++ ) {
            padres.add(new ArrayList<> ());
            resultado.add(0);
        }
        
    }
    
    /**
     * Algoritmo a seguir del AM.
     * Tenemos tres casos que puede seguir el algoritmo, cada uno haciendo 
     * referencia a la eleccion que se hace desde el menu al ejecutar el programa.
     * @throws FileNotFoundException 
     */
    public void algoritmo () throws FileNotFoundException {
        
        Generacional genetico = new Generacional ();
        while ( numEvaluaciones < 20000 ) {
            genetico.algoritmo();
            if ( AM1 ) {
                for ( int i = 0; i < numIndividuos; i++ ){
                    BusquedaLocal bl = new BusquedaLocal (i);
                    bl.algoritmo();
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
    
    /**
     * Funcion que devuelve por pantalla los transmisores del mejor resultado
     * y su coste
     * @throws FileNotFoundException 
     */
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
    
    /**
     * Solo se devuelve como entero el resultado
     * @return resultado con menor coste
     */
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
