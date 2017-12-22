/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

//import Algoritmos.P2.*;
import Algoritmos.Memetico.Generacional;
import Algoritmos.Memetico.Hibrido;
import Greedy.Greedy;
import Greedy.GreedyMejor;
import Utils.Restricciones;
import Utils.listaTransmisores;
import Utils.rangoFrec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author alumno
 */
public class main {

    public static String DIRECTORIO;
    public static String TRABAJO;
    public static Integer LINEAS;
    public static Random NUMERO;
    public static Integer SEMILLAS[] = {3181827, 1818273, 8182731, 1827318, 8273181};
    
    //Variables para el menu
    static Scanner scanner = new Scanner(System.in);
    static int select = -1;

    
    
    /**
     * @param args the command line arguments
     */
    public static void main ( String[] args ) throws FileNotFoundException, IOException {
        
        NUMERO = new Random();

        TRABAJO = System.getProperty("user.dir");

        System.out.println("Conjunto de archivos que quiere usar: ");
        Scanner reader = new Scanner(System.in);
        DIRECTORIO =  reader.nextLine(); 
        LINEAS = countLines(DIRECTORIO)+1;

        rangoFrec frecuencias = new rangoFrec();
        listaTransmisores transmisores = new listaTransmisores();
        Restricciones rest = new Restricciones();

        float startTime;
        float endTime;
        float duration;
        int contador = 0;
        int semilla = 0;
        select = -1;
        while( select != 0 ) {

            if ( semilla == 0 ) {
                NUMERO.setSeed(SEMILLAS[contador]);
            } else {
                NUMERO.setSeed(semilla);
            }

            try {
                System.out.print("Elige opción:\n"
                        + "1.- AM (10,1.0)\n"
                        + "2.- AM (10,0.1)\n"
                        + "3.- AM (10,0.1mej)\n"
                        + "4.- Greedy (PROFESOR)\n"
                        + "5.- Greedy Mejorado (PRACTICA)\n"
                        + "6.- Cambiar uso de Greedy para AM\n"
                        + "7.- Cambiar conjunto de archivos\n"
                        + "8.- Cambiar semilla\n "
                        + "0.- Salir"
                        + "\n: ");

                select = Integer.parseInt(scanner.nextLine());

                switch( select ) {
                    case 1:
                        System.out.print("Ejecutando AM(10,1.0) en "+DIRECTORIO);
                        if ( Generacional.mejorado )
                            System.out.println(" con Greedy Mejorado");
                        else
                            System.out.println(" con Greedy");
                        startTime = System.nanoTime();
                        Hibrido.AM1 = true;
                        Hibrido memetico = new Hibrido(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        memetico.algoritmo();                        
                        endTime = System.nanoTime();
                        memetico.resMejorIndividuo();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos");
                        Hibrido.AM1 = false;
                        break;
                    case 2:
                        Hibrido.AM2 = true;
                        System.out.println("Ejecutando AM(10,0.1) en "+DIRECTORIO);
                        if ( Generacional.mejorado )
                            System.out.println(" con Greedy Mejorado");
                        else
                            System.out.println(" con Greedy");
                        startTime = System.nanoTime();
                        memetico = new Hibrido(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        memetico.algoritmo();                        
                        endTime = System.nanoTime();
                        memetico.resMejorIndividuo();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos");
                        Hibrido.AM2 = false;
                        break;
                    case 3:
                        Hibrido.AM3 = true;
                        System.out.println("Ejecutando AM(10,0.1mej) en "+DIRECTORIO);
                        if ( Generacional.mejorado )
                            System.out.println(" con Greedy Mejorado");
                        else
                            System.out.println(" con Greedy");
                        startTime = System.nanoTime();
                        memetico = new Hibrido(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        memetico.algoritmo();                        
                        endTime = System.nanoTime();
                        memetico.resMejorIndividuo();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos");
                        Hibrido.AM3 = false;
                        break;
                    case 4:
                        System.out.println("Ejecutando Greddy en "+DIRECTORIO);
                        startTime = System.nanoTime();
                        Greedy greedy = new Greedy (frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        greedy.algoritmo();                        
                        endTime = System.nanoTime();
                        greedy.resMejorIndividuo();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos");
                        break;
                    case 5:
                        System.out.print("Ejecutando Greddy Mejorado en "+DIRECTORIO);
                        startTime = System.nanoTime();
                        GreedyMejor greedyM = new GreedyMejor(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        greedyM.algoritmo();                        
                        endTime = System.nanoTime();
                        greedyM.resMejorIndividuo();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos");
                        break;
                    case 6:
                        if ( Generacional.mejorado ) {
                            Generacional.mejorado = false;
                            System.out.println("Establecido Greedy (PROFESOR)");
                        }
                        else {
                            Generacional.mejorado = true;
                            System.out.println("Establecido Greedy Mejorado (PRACTICA)");
                        }
                        contador = Math.floorMod(contador-1, 5);
                        break;
                    case 7:
                        System.out.println("Conjunto de archivos que quiere usar: ");

                        DIRECTORIO = reader.next();
                        LINEAS = countLines(DIRECTORIO) + 1;

                        frecuencias = new rangoFrec();
                        transmisores = new listaTransmisores();
                        rest = new Restricciones();
                        contador = Math.floorMod(contador-1, 5);
                        break;
                    case 8:
                        System.out.print("Nueva semilla: ");
                        semilla = reader.nextInt();
                        NUMERO.setSeed(semilla);
                        contador = Math.floorMod(contador-1, 5);
                        break;
                    case 0:
                        System.out.println("Fin");
                        break;
                    default:
                        System.out.println("Número no reconocido");
                        contador = Math.floorMod(contador-1, 5);
                        break;
                }

                System.out.println("\n"); //Mostrar un salto de línea en Java

            } catch( Exception e ) {
                System.out.println("Uoop! Error! " + e.toString());
            }
            
            contador = Math.floorMod(contador + 1, 5);
        }
    }

    public static int countLines ( String filename ) throws IOException {
        String archivo = "var.txt";
        if ( filename.matches("scen.*") ) {
            archivo = archivo.toUpperCase();
        }
        File file = new File(main.TRABAJO + "/conjuntos/" + main.DIRECTORIO + "/" + archivo);
        Scanner lineas = new Scanner(file);
        int ultimoTransmisor = 0;
        while( lineas.hasNextLine() ) {
            ultimoTransmisor = lineas.nextInt();
            String ultimaLinea = lineas.nextLine();
        }
        return ultimoTransmisor;
    }
}
