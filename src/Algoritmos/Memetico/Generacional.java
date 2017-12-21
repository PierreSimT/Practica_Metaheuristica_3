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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import static main.main.NUMERO;
/**
 *
 * @author ptondreau
 */
public class Generacional {

    final int numParejas = 7;
    final int numIndividuos = Hibrido.numIndividuos;
    
    int numEstancamiento = 0;
    int idMejorResult;
    int mejorResult = Integer.MAX_VALUE;
    int idMutado;

    public Generacional () throws FileNotFoundException {
        
       for ( int i = 0; i < numIndividuos; i ++ ) {
            construccionInicial(i);
        }
       
    }

    void algoritmo () throws FileNotFoundException {
        
        numGeneraciones = 0;
        //Loop hasta 20000 evaluaciones
        while( numGeneraciones < 10 ) {
            generarHijos();
            cruzarIndividuos();
            mutarIndividuos();
            nuevaGeneracion();
            numGeneraciones++;
        }
    }
    
    void construccionInicial ( int id ) throws FileNotFoundException {
        
        List <Integer> frecuenciasR = new ArrayList<>();
        List<List<Integer>> listaRest = new ArrayList();
        Random numero = NUMERO;
        
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
        resultado.set(id, diferencia);
        padres.get(id).addAll(frecuenciasR);
        frecuenciasR.clear();
        
        BusquedaLocal bl = new BusquedaLocal(id);
        bl.algoritmo();
        
    }

    void generarHijos () {
        for ( int i = 0; i < numIndividuos; i ++ ) {
            Random numero = NUMERO;
            int seleccionado = numero.nextInt(numIndividuos);
            Random numero2 = NUMERO;
            int seleccionado2 = numero2.nextInt(numIndividuos);
            
            while ( seleccionado == seleccionado2 ) {
                seleccionado2 = numero2.nextInt(numIndividuos);
            }
            if ( resultado.get(seleccionado) < resultado.get(seleccionado2) ) {
                hijos.add(i, padres.get(seleccionado));

            } else {
                hijos.add(i, padres.get(seleccionado2));
            }
        }
        
    }

    void cruzarIndividuos () {
        int cont = 0;
        int numEmparejado = 0;
        while( numEmparejado < numParejas ) {
            int individuo1 = cont;
            int individuo2 = cont + 1;
            algBX(individuo1, individuo2);
            cont += 2;
            numEmparejado++;
        }

    }

    // No estoy seguro de si habría que hacerla así.
    final double alfa = 0.5;
    void mutarIndividuos () {
        //Mutamos solo un individuo
        
        //Seleccionamos el individuo a mutar
        Random numero = NUMERO;
        int seleccionado = numero.nextInt(numIndividuos);

        int transmisorMut = numero.nextInt(transmisores.size());
        int frecAsociada = transmisores.get(transmisorMut);

        int frecuenciaMut = numero.nextInt(frecuencias.get(frecAsociada).size());
        hijos.get(seleccionado).set(transmisorMut, frecuencias.get(frecAsociada).get(frecuenciaMut));

        idMutado = seleccionado;

    }

    void algBX ( int individuo1, int individuo2 ) {
        
        List<Integer> solucion1 = new ArrayList<>();
        List<Integer> solucion2 = new ArrayList<>();

        for ( int i = 0; i < transmisores.size(); i ++ ) {
                int d = Math.abs(hijos.get(individuo1).get(i) - hijos.get(individuo2).get(i));
                int cmin = Integer.MAX_VALUE;
                int cmax = Integer.MIN_VALUE;

                if ( hijos.get(individuo1).get(i) < hijos.get(individuo2).get(i) ) {
                    cmin = hijos.get(individuo1).get(i);
                } else {
                    cmin = hijos.get(individuo2).get(i);
                }

                if ( hijos.get(individuo1).get(i) > hijos.get(individuo2).get(i) ) {
                    cmax = hijos.get(individuo1).get(i);
                } else {
                    cmax = hijos.get(individuo2).get(i);
                }

                int vmin = (int) (cmin - d * alfa);
                int vmax = (int) (cmax + d * alfa);

                int frecAsociada = transmisores.get(i);

                //Para la solución 1
                Random numero = NUMERO;
                int valorObtenido = numero.nextInt(vmax+1)+vmin;
                int minimaDiferencia = Integer.MAX_VALUE;
                int frecuenciaFinal = 0;

                for ( int j = 0; j < frecuencias.get(frecAsociada).size(); j ++ ) {
                    if ( Math.abs(valorObtenido - frecuencias.get(frecAsociada).get(j)) < minimaDiferencia ) {
                        minimaDiferencia = Math.abs(valorObtenido - frecuencias.get(frecAsociada).get(j));
                        frecuenciaFinal = frecuencias.get(frecAsociada).get(j);
                    }
                }

                solucion1.add(i, frecuenciaFinal);

                //Para la solución 2
                int valorObtenido2 = numero.nextInt(vmax+1)+vmin;
                int minimaDiferencia2 = Integer.MAX_VALUE;
                int frecuenciaFinal2 = 0;

                for ( int j = 0; j < frecuencias.get(frecAsociada).size(); j ++ ) {
                    if ( Math.abs(valorObtenido2 - frecuencias.get(frecAsociada).get(j)) < minimaDiferencia2 ) {
                        minimaDiferencia2 = Math.abs(valorObtenido2 - frecuencias.get(frecAsociada).get(j));
                        frecuenciaFinal2 = frecuencias.get(frecAsociada).get(j);
                    }
                }

                solucion2.add(i, frecuenciaFinal2);
        }
            hijos.set(individuo1, solucion1);
            hijos.set(individuo2, solucion2);
    }
    
    public void nuevaGeneracion () throws FileNotFoundException {
        //Elitismo

        int aux = mejorResult;
        int resultadoHijos[] = new int[ numIndividuos ];
        //Buscamos el mejor individuo de la generación de padres
        int minimo = Integer.MAX_VALUE;
        int actual = 0;
        for ( int i = 0; i < numIndividuos; i ++ ) {
            if ( resultado.get(i) < minimo ) {
                minimo = resultado.get(i);
                actual = i;
            }
        }

        List<Integer> mejorIndividuo = padres.get(actual);
        
        /* Modificar todo esto, hay menos individuos a evaluar a cada iteracion */
        // Evaluamos los hijos
        // Evaluamos los hijos
        if ( idMutado <= 14 ) {
            resultadoHijos = evaluar(hijos, idMutado);
            numEvaluaciones += 14;
        } else {
            resultadoHijos = evaluar(hijos, idMutado);
            numEvaluaciones += 15;
        }

        for ( int i = 14; i < numIndividuos; i ++ ) {
            if ( i != idMutado ) {
                resultadoHijos[ i ] = resultado.get(i);
            }
        }

        
        //Buscamos el hijo con el mayor coste
        int maximo = Integer.MIN_VALUE;
        int actual2 = 0;

        for ( int i = 0; i < numIndividuos; i ++ ) {
            if ( resultadoHijos[ i ] > maximo ) {
                maximo = resultadoHijos[ i ];
                actual2 = i;
            }
        }

        //Si el menor de los padres tiene menor coste que el mayor de los hijos se reemplaza
        if ( minimo < maximo ) {
            hijos.set(actual2, mejorIndividuo);
            resultadoHijos[ actual2 ] = minimo;

        }

        //Los hijos serán los padres para la siguiente generación
        padres.clear();
        padres.addAll(hijos);
        hijos.clear();

        for ( int i = 0; i < resultado.size(); i ++ ) {
            if ( resultadoHijos[i] != 0 ) {
                resultado.set(i, resultadoHijos[ i ]); //rDiferencia(padres.get(i), restricciones);
                if ( resultado.get(i) < mejorResult ) {
                    mejorResult = resultado.get(i);
                    idMejorResult = i;
                }
            } else
                resultado.set(i, rDiferencia(padres.get(i), restricciones));
        }
        
        if ( aux == mejorResult ) {
            numEstancamiento ++;
        } else {
            numEstancamiento = 0;
        }

        if ( numEstancamiento >= 20 || comprobarConvergencia() ) {
            System.out.println("Reinicializacion\n\n");
            reinicializacion();
            numEstancamiento = 0;
        }
    }

    public int[] evaluar ( List<List<Integer>> individuos, int mutado ) throws FileNotFoundException {
        int[] result = new int[ numIndividuos ];

        for ( int i = 0; i < 14; i ++ ) {
            result[ i ] = rDiferencia(individuos.get(i), restricciones);
        }
        if ( idMutado >= 14 ) {
            result[ mutado ] = rDiferencia(individuos.get(mutado), restricciones);
        }

        return result;
    }

    private void reinicializacion () throws FileNotFoundException {
        List<Integer> mejorSolucion = new ArrayList();
        mejorSolucion.addAll(padres.get(idMejorResult));
        padres.clear();
        hijos.clear();

        for ( int i = 0; i < numIndividuos; i ++ ) {
            padres.add(new ArrayList<>());
        }

        padres.set(0, mejorSolucion);
        resultado.set(0, mejorResult);

        for ( int i = 1; i < numIndividuos; i ++ ) {
            construccionInicial(i);
        }

    }

    private boolean comprobarConvergencia () {

        List<Integer> auxiliar = new ArrayList<>();
        auxiliar.addAll(resultado);
        Collections.sort(auxiliar);

        int contador = 1;
        boolean convergencia = false;

        for ( int i = 1; i < auxiliar.size(); i ++ ) {
            if ( contador >= 16 ) {
                convergencia = true;
                break;
            }
            if ( auxiliar.get(i) == (auxiliar.get(i - 1)) ) {
                contador ++;
            } else {
                contador = 1;
            }
        }

        // Preguntar al profesor puesto que añade complejidad
        if ( convergencia ) {

            List<List<Integer>> auxiliarP = new ArrayList<>();

            for ( int i = 0; i < padres.size(); i ++ ) {
                auxiliarP.add(new ArrayList<>());
                auxiliarP.get(i).addAll(padres.get(i));
                Collections.sort(auxiliarP.get(i));
            }

            contador = 1;
            convergencia = false;
            for ( int i = 1; i < auxiliarP.size(); i ++ ) {
                if ( contador >= 16 ) {
                    convergencia = true;
                    break;
                }
                if ( auxiliarP.get(i).equals(auxiliarP.get(i - 1)) ) {
                    contador ++;
                } else {
                    contador = 1;
                }
            }
        }

        return convergencia;
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
    
    public int resultadoFinal () {
        int minimo = Integer.MAX_VALUE;
        int actual = 0;
        for ( int i = 0; i < numIndividuos; i ++ ) {
            if ( resultado.get(i) < minimo ) {
                minimo = resultado.get(i);
                actual = i;
            }
        }
        List<Integer> mejorIndividuo = padres.get(actual);
        
        return resultado.get(actual);
    }
}
