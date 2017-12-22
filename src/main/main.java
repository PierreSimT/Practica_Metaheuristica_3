/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

//import Algoritmos.P2.*;
import Algoritmos.Memetico.Hibrido;
import Greedy.Greedy;
import Greedy.GreedyMejor;
import Utils.Restricciones;
import Utils.listaTransmisores;
import Utils.rangoFrec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author alumno
 */
public class main {

    public static String DIRECTORIO;
    public static String TRABAJO;
    public static Integer LINEAS;
    public static Random NUMERO;
    public static String DIRECTORIOS [] = { "scen06", "scen07", "scen08", "scen09", "scen10", "graph05", "graph06", "graph07", "graph11", "graph12" };
    public static Integer SEMILLAS[] = {3181827, 1818273, 8182731, 1827318, 8273181};
    public static String archivos [] =  {"Greedy.xlsx" };//"AM_10_1.xlsx", "AM_10_01.xlsx", "AM_10_01_mej.xlsx" };
    
    //Variables para el menu
    static Scanner scanner = new Scanner(System.in);
    static int select = -1;

    
    
    /**
     * @param args the command line arguments
     */
    public static void main ( String[] args ) throws FileNotFoundException, IOException {
        
        int lineaInicial = 2;
        int columnaInicial = 1;
        int cuentaDirectorios = 0;
        
        NUMERO = new Random();

        TRABAJO = System.getProperty("user.dir");

        System.out.println("Conjunto de archivos que quiere usar: ");
        Scanner reader = new Scanner(System.in);
        DIRECTORIO =  DIRECTORIOS[cuentaDirectorios]; //reader.nextLine(); 
        LINEAS = countLines(DIRECTORIO)+1;

        rangoFrec frecuencias = new rangoFrec();
        listaTransmisores transmisores = new listaTransmisores();
        Restricciones rest = new Restricciones();

        int cuentaArchivos = 0;
        float startTime;
        float endTime;
        float duration;
        int contador = 0;
        int semilla = 0;
        select = 4;
        while( select != 0 ) {

            if ( semilla == 0 ) {
                NUMERO.setSeed(SEMILLAS[contador]);
            } else {
                NUMERO.setSeed(semilla);
            }

//            try {
                System.out.print("Elige opción:\n"
                        + "1.- AM (10,1.0)\n"
                        + "2.- AM (10,0.1)\n"
                        + "3.- AM (10,0.1mej)\n"
                        + "4.- Greedy\n"
                        + "5.- Greedy Mejorado\n"
                        + "6.- Cambiar conjunto de archivos\n"
                        + "7.- Cambiar semilla\n "
                        + "0.- Salir"
                        + "\n: ");

                //select = 1;//Integer.parseInt(scanner.nextLine());

                switch( select ) {
                    case 1:
                        System.out.println("Ejecucion "+contador+" de "+DIRECTORIO);
                        startTime = System.nanoTime();
                        Hibrido.AM1 = true;
                        Hibrido memetico = new Hibrido(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        memetico.algoritmo();                        
                        endTime = System.nanoTime();
//                        memetico.resMejorIndividuo();
                        int resultado = memetico.resultadoFinal();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos ; resultado : "+resultado+" en archivo "+archivos[cuentaArchivos]+" algoritmo "+select);
                        escribirExcel(duration, resultado, lineaInicial, columnaInicial, archivos[cuentaArchivos]);
                        lineaInicial++;
                        Hibrido.AM1 = false;
                        break;
                    case 2:
                        Hibrido.AM2 = true;
                        System.out.println("Ejecucion "+contador+" de "+DIRECTORIO);
                        startTime = System.nanoTime();
                        memetico = new Hibrido(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        memetico.algoritmo();                        
                        endTime = System.nanoTime();
//                        memetico.resMejorIndividuo();
                        resultado = memetico.resultadoFinal();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos ; resultado : "+resultado+" en archivo "+archivos[cuentaArchivos]+" algoritmo "+select);
                        escribirExcel(duration, resultado, lineaInicial, columnaInicial, archivos[cuentaArchivos]);
                        lineaInicial++;
                        Hibrido.AM2 = false;
                        break;
                    case 3:
                        Hibrido.AM3 = true;
                        System.out.println("Ejecucion "+contador+" de "+DIRECTORIO);
                        startTime = System.nanoTime();
                        memetico = new Hibrido(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        memetico.algoritmo();                        
                        endTime = System.nanoTime();
//                        memetico.resMejorIndividuo();
                        resultado = memetico.resultadoFinal();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos ; resultado : "+resultado+" en archivo "+archivos[cuentaArchivos]+" algoritmo "+select);
                        escribirExcel(duration, resultado, lineaInicial, columnaInicial, archivos[cuentaArchivos]);
                        lineaInicial++;
                        Hibrido.AM3 = false;
                        break;
                    case 4:
                        System.out.println("Ejecucion "+contador+" de "+DIRECTORIO);
                        startTime = System.nanoTime();
                        Greedy greedy = new Greedy (frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        greedy.algoritmo();                        
                        endTime = System.nanoTime();
//                        greedy.resMejorIndividuo();
                        resultado = greedy.resultadoFinal();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos ; resultado : "+resultado+" en archivo "+archivos[cuentaArchivos]+" algoritmo "+select);
                        escribirExcel(duration, resultado, lineaInicial, columnaInicial, archivos[cuentaArchivos]);
                        lineaInicial++; 
                        break;
                    case 5:
                        System.out.println("Ejecucion "+contador+" de "+DIRECTORIO);
                        startTime = System.nanoTime();
                        GreedyMejor greedyM = new GreedyMejor(frecuencias.rangoFrecuencias, transmisores.transmisores, rest);
                        greedyM.algoritmo();                        
                        endTime = System.nanoTime();
//                        memetico.resMejorIndividuo();
                        resultado = greedyM.resultadoFinal();
                        
                        duration = (endTime - startTime) / 1000000000;
                        System.out.println("Tiempo de ejecucion: " + duration + " segundos ; resultado : "+resultado+" en archivo "+archivos[cuentaArchivos]+" algoritmo "+select);
                        escribirExcel(duration, resultado, lineaInicial, columnaInicial, archivos[cuentaArchivos]);
                        lineaInicial++;
                        break;
                    case 6:
                        System.out.println("Conjunto de archivos que quiere usar: ");

                        DIRECTORIO = reader.next();
                        LINEAS = countLines(DIRECTORIO) + 1;

                        frecuencias = new rangoFrec();
                        transmisores = new listaTransmisores();
                        rest = new Restricciones();
                        break;
                    case 7:
                        System.out.print("Nueva semilla: ");
                        semilla = reader.nextInt();
                        NUMERO.setSeed(semilla);
                        break;
                    case 0:
                        System.out.println("Fin");
                        break;
                    default:
                        System.out.println("Número no reconocido");
                        break;
                }

                System.out.println("\n"); //Mostrar un salto de línea en Java

//            } catch( Exception e ) {
//                System.out.println("Uoop! Error! " + e.toString());
//            }
            
            if ( contador == 4 ) {
                cuentaDirectorios = Math.floorMod(cuentaDirectorios + 1, 10);
                DIRECTORIO = DIRECTORIOS[cuentaDirectorios];
                LINEAS = countLines(DIRECTORIO) + 1;
                frecuencias = new rangoFrec();
                transmisores = new listaTransmisores();
                rest = new Restricciones ();
                
                lineaInicial = 2;
                columnaInicial = columnaInicial + 2;
                
                if ( cuentaDirectorios == 0 ) {
                    columnaInicial = 1;
                    cuentaArchivos += 1;
                    select += 1;
                }
                
                if ( select == 5 ) {
                    select = 0;
                }
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

    private static void escribirExcel (float duration, int resultado, int linea, int columna, String archivo) throws FileNotFoundException, IOException {
        
        FileInputStream archivoXLS = new FileInputStream (new File(System.getProperty("user.dir")+"/"+archivo));
        
        XSSFWorkbook libro = new XSSFWorkbook(archivoXLS);

        XSSFSheet hoja = libro.getSheetAt(0);

        Cell cell = null;
        
        cell = hoja.getRow(linea).getCell(columna);
        cell.setCellValue(resultado);
        cell = hoja.getRow(linea).getCell(columna+1);
        cell.setCellValue(duration);

        archivoXLS.close();

        FileOutputStream archivoOutXLS = new FileOutputStream (new File(System.getProperty("user.dir")+"/"+archivo));
        
        libro.write(archivoOutXLS);
        
        archivoOutXLS.close();
    }
}
