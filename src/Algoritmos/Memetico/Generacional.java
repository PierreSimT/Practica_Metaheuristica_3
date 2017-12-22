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

    public static boolean mejorado = false;

    final int numParejas = 7;
    final int numIndividuos = Hibrido.numIndividuos;

    int numEstancamiento = 0;
    int idMejorResult;
    int mejorResult = Integer.MAX_VALUE;
    int idMutado;

    /**
     * Constructor del algoritmo
     * @throws FileNotFoundException 
     */
    public Generacional() throws FileNotFoundException {

        for (int i = 0; i < numIndividuos; i++) {
            construccionInicial(i);
        }

    }

    /**
     * Codigo que ejecuta el algoritmo.
     * @throws FileNotFoundException 
     */
    void algoritmo() throws FileNotFoundException {

        numGeneraciones = 0;
        //Loop hasta 20000 evaluaciones
        while (numGeneraciones < 10) {
            generarHijos();
            cruzarIndividuos();
            mutarIndividuos();
            nuevaGeneracion();
            numGeneraciones++;
        }
    }

    /**
     * La construccion inicial se puede hacer de dos formas dependiendo de cual
     * se encuentra activa. Para nuestro grupo de practicas se nos ha indicado
     * de implementar un algoritmo Greedy que no busca minimizar el coste, solo 
     * usar valores aleatorios, mientras que en la practica se usa el Greedy
     * usado en la practica 2. Por lo tanto, damos la habilidad de poder alternar
     * entre ambas implementaciones desde el menu.
     * @param id Indice del padre que se esta construyendo
     * @throws FileNotFoundException 
     */
    void construccionInicial(int id) throws FileNotFoundException {

        if (!mejorado) {
            List<Integer> frecuenciasR = new ArrayList<>();
            List<List<Integer>> listaRest = new ArrayList();
            Random numero = NUMERO;

            for (int i = 0; i < transmisores.size(); i++) {
                listaRest = restricciones.restriccionesTransmisor(i);
                if (listaRest.size() > 0) {
                    int indiceFrecAleatorio = numero.nextInt(frecuencias.get(transmisores.get(i)).size());
                    int frecAleatoria = frecuencias.get(transmisores.get(i)).get(indiceFrecAleatorio);
                    frecuenciasR.add(frecAleatoria);
                } else {
                    frecuenciasR.add(0);
                }
            }

            int diferencia = rDiferencia(frecuenciasR, restricciones);
            resultado.set(id, diferencia);
            padres.get(id).addAll(frecuenciasR);
            frecuenciasR.clear();

            BusquedaLocal bl = new BusquedaLocal(id);
            bl.algoritmo();
        } else {
            List<Integer> frecuenciasR = new ArrayList<>();
            for (int i = 0; i < transmisores.size(); i++) {
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
            while (transmisor < transmisores.size()) {
                listaRestric = restricciones.restriccionesTransmisor(transmisor);
                if (transmisor != seleccionado && listaRestric.size() > 0) {

                    int minimo = Integer.MAX_VALUE;
                    boolean encontrado = false;
                    int frecuenciaR = 0;
                    int frecuencia;
                    int pos = 0;

                    int valor = 0; //Sacado del bucle while

                    while (pos < frecuencias.get(transmisores.get(transmisor)).size() && !encontrado) {

                        List<Integer> nuevaLista = new ArrayList<>();
                        nuevaLista.addAll(frecuenciasR);

                        frecuencia = frecuencias.get(transmisores.get(transmisor)).get(pos);
                        nuevaLista.set(transmisor, frecuencia);
                        List<List<Integer>> listaRest = compruebaTransmisores(transmisor, restricciones, frecuenciasR);

                        if (listaRest.size() > 0) { // Lista no vacía, se selecciona frecuencia que afecte lo menos posible al resultado

                            valor = rDiferencia(nuevaLista, listaRest);
                            if (valor < minimo) {
                                minimo = valor;
                                frecuenciaR = frecuencia;
                                if (valor == 0) // Si la suma de todas las restricciones = 0 entonces es el mejor resultado posible
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
                        pos++;
                    }
                    frecuenciasR.set(transmisor, frecuenciaR);
                }
                transmisor++;
            }
            resultado.set(id, rDiferencia(frecuenciasR, restricciones));
            padres.get(id).addAll(frecuenciasR);
            frecuenciasR.clear(); // Borra todos los elementos anteriores para nueva solucion
            BusquedaLocal bl = new BusquedaLocal(id);
            bl.algoritmo();

        }

    }

    /**
     * Se generan los hijos, al finalizar esta funcion los hijos son copias
     * exactas de los padres.
     */
    void generarHijos() {
        for (int i = 0; i < numIndividuos; i++) {
            Random numero = NUMERO;
            int seleccionado = numero.nextInt(numIndividuos);
            Random numero2 = NUMERO;
            int seleccionado2 = numero2.nextInt(numIndividuos);

            if (resultado.get(seleccionado) < resultado.get(seleccionado2)) {
                hijos.add(i, padres.get(seleccionado));

            } else {
                hijos.add(i, padres.get(seleccionado2));
            }
        }

    }

    /**
     * Cruza dos hijos, pasamos estos indices a la funcion que se encargara de 
     * hacer el cruce entre ambos hijos.
     */
    void cruzarIndividuos() {
        int cont = 0;
        while (cont < numParejas) {
            int individuo1 = cont;
            int individuo2 = cont + 1;
            algBX(individuo1, individuo2);
            cont++;
        }

    }

    // No estoy seguro de si habría que hacerla así.
    final double alfa = 0.5;

    /**
     * Funcion que muta un gen de un cromosoma aleatorio.
     */
    void mutarIndividuos() {
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

    /**
     * Cruce BLX de los hijos que se pasaron anteriormente desde cruzarIndividuos
     * @param individuo1
     * @param individuo2 
     */
    void algBX(int individuo1, int individuo2) {

        List<Integer> solucion1 = new ArrayList<>();
        List<Integer> solucion2 = new ArrayList<>();

        for (int i = 0; i < transmisores.size(); i++) {
            int d = Math.abs(hijos.get(individuo1).get(i) - hijos.get(individuo2).get(i));
            int cmin = Integer.MAX_VALUE;
            int cmax = Integer.MIN_VALUE;

            if (hijos.get(individuo1).get(i) < hijos.get(individuo2).get(i)) {
                cmin = hijos.get(individuo1).get(i);
            } else {
                cmin = hijos.get(individuo2).get(i);
            }

            if (hijos.get(individuo1).get(i) > hijos.get(individuo2).get(i)) {
                cmax = hijos.get(individuo1).get(i);
            } else {
                cmax = hijos.get(individuo2).get(i);
            }

            int vmin = (int) (cmin - d * alfa);
            int vmax = (int) (cmax + d * alfa);

            int frecAsociada = transmisores.get(i);

            //Para la solución 1
            Random numero = NUMERO;
            int valorObtenido = numero.nextInt(vmax + 1) + vmin;
            int minimaDiferencia = Integer.MAX_VALUE;
            int frecuenciaFinal = 0;

            for (int j = 0; j < frecuencias.get(frecAsociada).size(); j++) {
                if (Math.abs(valorObtenido - frecuencias.get(frecAsociada).get(j)) < minimaDiferencia) {
                    minimaDiferencia = Math.abs(valorObtenido - frecuencias.get(frecAsociada).get(j));
                    frecuenciaFinal = frecuencias.get(frecAsociada).get(j);
                }
            }

            solucion1.add(i, frecuenciaFinal);

            //Para la solución 2
            int valorObtenido2 = numero.nextInt(vmax + 1) + vmin;
            int minimaDiferencia2 = Integer.MAX_VALUE;
            int frecuenciaFinal2 = 0;

            for (int j = 0; j < frecuencias.get(frecAsociada).size(); j++) {
                if (Math.abs(valorObtenido2 - frecuencias.get(frecAsociada).get(j)) < minimaDiferencia2) {
                    minimaDiferencia2 = Math.abs(valorObtenido2 - frecuencias.get(frecAsociada).get(j));
                    frecuenciaFinal2 = frecuencias.get(frecAsociada).get(j);
                }
            }

            solucion2.add(i, frecuenciaFinal2);
        }
        hijos.set(individuo1, solucion1);
        hijos.set(individuo2, solucion2);
    }

    /** 
     * Funcion que realiza las ultimas operaciones para pasar la generacion de
     * hijos a la siguiente generacion de padres. Aqui tambien se comprueba si
     * se debe de realizar la reinicializacion.
     * @throws FileNotFoundException 
     */
    public void nuevaGeneracion() throws FileNotFoundException {
        //Elitismo

        int aux = mejorResult;
        int resultadoHijos[] = new int[numIndividuos];
        //Buscamos el mejor individuo de la generación de padres
        int minimo = Integer.MAX_VALUE;
        int actual = 0;
        for (int i = 0; i < numIndividuos; i++) {
            if (resultado.get(i) < minimo) {
                minimo = resultado.get(i);
                actual = i;
            }
        }

        List<Integer> mejorIndividuo = padres.get(actual);

        // Evaluamos los hijos
        if (idMutado <= 14) {
            resultadoHijos = evaluar(hijos, idMutado);
            numEvaluaciones += 14;
        } else {
            resultadoHijos = evaluar(hijos, idMutado);
            numEvaluaciones += 15;
        }

        for (int i = 14; i < numIndividuos; i++) {
            if (i != idMutado) {
                resultadoHijos[i] = resultado.get(i);
            }
        }

        //Buscamos el hijo con el mayor coste
        int maximo = Integer.MIN_VALUE;
        int actual2 = 0;

        for (int i = 0; i < numIndividuos; i++) {
            if (resultadoHijos[i] > maximo) {
                maximo = resultadoHijos[i];
                actual2 = i;
            }
        }

        //Si el menor de los padres tiene menor coste que el mayor de los hijos se reemplaza
        if (minimo < maximo) {
            hijos.set(actual2, mejorIndividuo);
            resultadoHijos[actual2] = minimo;

        }

        //Los hijos serán los padres para la siguiente generación
        padres.clear();
        padres.addAll(hijos);
        hijos.clear();

        for (int i = 0; i < resultado.size(); i++) {
            if (resultadoHijos[i] != 0) {
                resultado.set(i, resultadoHijos[i]);
                if (resultado.get(i) < mejorResult) {
                    mejorResult = resultado.get(i);
                    idMejorResult = i;
                }
            } else {
                resultado.set(i, rDiferencia(padres.get(i), restricciones));
            }
        }

        if (aux == mejorResult) {
            numEstancamiento++;
        } else {
            numEstancamiento = 0;
        }

        if (numEstancamiento >= 20 || comprobarConvergencia()) {
            reinicializacion();
            numEstancamiento = 0;
        }
    }

    /**
     * Evaluamos los hijos y obtenemos sus resultados.
     * @param individuos
     * @param mutado
     * @return
     * @throws FileNotFoundException 
     */
    public int[] evaluar(List<List<Integer>> individuos, int mutado) throws FileNotFoundException {
        int[] result = new int[numIndividuos];

        for (int i = 0; i < 14; i++) {
            result[i] = rDiferencia(individuos.get(i), restricciones);
        }
        if (idMutado >= 14) {
            result[mutado] = rDiferencia(individuos.get(mutado), restricciones);
        }

        return result;
    }

    /**
     * Si existe convergencia o estancamiento se realiza una reinicializacion
     * desde aqui se llama a la construccion Inicial para reconstruir una solucion.
     * @throws FileNotFoundException 
     */
    private void reinicializacion() throws FileNotFoundException {
        List<Integer> mejorSolucion = new ArrayList();
        mejorSolucion.addAll(padres.get(idMejorResult));
        padres.clear();
        hijos.clear();

        for (int i = 0; i < numIndividuos; i++) {
            padres.add(new ArrayList<>());
        }

        padres.set(0, mejorSolucion);
        resultado.set(0, mejorResult);

        for (int i = 1; i < numIndividuos; i++) {
            construccionInicial(i);
        }

    }

    /**
     * Esta funciona comprueba si los individuos convergen a una misma solucion
     * @return 
     */
    private boolean comprobarConvergencia() {

        List<Integer> auxiliar = new ArrayList<>();
        auxiliar.addAll(resultado);
        Collections.sort(auxiliar);

        int contador = 1;
        boolean convergencia = false;

        for (int i = 1; i < auxiliar.size(); i++) {
            if (contador >= 16) {
                convergencia = true;
                break;
            }
            if (auxiliar.get(i) == (auxiliar.get(i - 1))) {
                contador++;
            } else {
                contador = 1;
            }
        }

        // Preguntar al profesor puesto que añade complejidad
        if (convergencia) {

            List<List<Integer>> auxiliarP = new ArrayList<>();

            for (int i = 0; i < padres.size(); i++) {
                auxiliarP.add(new ArrayList<>());
                auxiliarP.get(i).addAll(padres.get(i));
                Collections.sort(auxiliarP.get(i));
            }

            contador = 1;
            convergencia = false;
            for (int i = 1; i < auxiliarP.size(); i++) {
                if (contador >= 16) {
                    convergencia = true;
                    break;
                }
                if (auxiliarP.get(i).equals(auxiliarP.get(i - 1))) {
                    contador++;
                } else {
                    contador = 1;
                }
            }
        }

        return convergencia;
    }

    /**
     * Funcion que devuelve por pantalla los transmisores del mejor resultado
     * y su coste
     * @throws FileNotFoundException 
     */
    public void resMejorIndividuo() throws FileNotFoundException {
        int minimo = Integer.MAX_VALUE;
        int actual = 0;
        for (int i = 0; i < numIndividuos; i++) {
            if (resultado.get(i) < minimo) {
                minimo = resultado.get(i);
                actual = i;
            }
        }
        List<Integer> mejorIndividuo = padres.get(actual);

        for (int i = 0; i < mejorIndividuo.size(); i++) {
            if (mejorIndividuo.get(i) != 0) {
                System.out.println("Transmisor " + (i + 1) + ": " + mejorIndividuo.get(i));
            }
        }

        System.out.println("Resultado: "+resultado.get(actual));
    }

    /**
     * Solo se devuelve como entero el resultado
     * @return 
     */
    public int resultadoFinal() {
        int minimo = Integer.MAX_VALUE;
        int actual = 0;
        for (int i = 0; i < numIndividuos; i++) {
            if (resultado.get(i) < minimo) {
                minimo = resultado.get(i);
                actual = i;
            }
        }
        List<Integer> mejorIndividuo = padres.get(actual);

        return resultado.get(actual);
    }
}
