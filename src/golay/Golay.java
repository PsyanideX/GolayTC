/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package golay;

/**
 *
 * @author marti
 */
public class Golay {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int[][] generadora = 
        {{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1},
        {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0},
        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 0, 1, 0, 1, 1},
        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1},
        {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1, 1},
        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 1},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1}};
        int m1=12;
        int m2=24;
        
        int[][] vector = {{1,0,0,1,1,0,1,1,1,0,1,1}};
        int v1=1;
        int v2=12;
        
        System.out.println("La palabra fuente es:");
        displayProduct(vector);
        
        System.out.println("La palabra codificada es:");
        //Codificar
        int[][] res = codificar(vector,generadora,v1,m1,m2);
        displayProduct(res);
        
        System.out.println("El resultado de la descodificación es:");
        //Descodificar
        int[][] s = descodificar(res,generadora);
        displayProduct(s);
        
    }
    
    public static int[][] codificar(int[][] vector, int[][] generadora,int v1,int m1,int m2){
        int[][] res = multiplyMatrices(vector,generadora,v1,m1,m2);
        return res;
    }
    public static int[][] descodificar(int[][] palabra, int[][] generadora){
        int[][] transpuesta = transposeMatrix(generadora);
        
        //Primero calculamos el síndrome de la palabra recibida, s=r*G transpuesta
        int[][] s = multiplyMatrices(palabra, transpuesta, palabra.length, transpuesta.length, transpuesta[0].length);
        
        int numUnos = contarUnos(s);
        int[][] e = new int[1][s[0].length*2];
        boolean seteado = false;

        //Si w(s)<=3, entonces el vector es e=(s,0)
        if(numUnos <= 3){
            
            for(int i = 0; i<s[0].length;i++){
                e[0][i] = s[0][i];
            }
            for(int j = 12; j < 24;j++){
                e[0][j] = 0;
            }
            seteado = true;
        } else if(algo3(s,extraerA(generadora))<13){ //Si w(s+ai) <= 2 para alguna fila de la matriz A, entonces el vector error es e = (ui,s*A + ai)
           
            for(int i = 0; i<s[0].length;i++){
                e[0][i] = s[0][i] + generadora[algo3(s,extraerA(generadora))][i];
            }
            for(int j = 12; j < 24;j++){
                if(j-12==algo3(s,extraerA(generadora))){
                    e[0][j] = 1;
                }
                else e[0][j] = 0;
            }
            seteado = true;
        } else if(contarUnos(multiplyMatrices(s,extraerA(generadora),s.length,extraerA(generadora).length,extraerA(generadora)[0].length))<=3){
           
            for(int i=0; i < 24;i++){
                if(i < 12){
                    e[0][i] = 0;
                }else{
                    e[0][i] = multiplyMatrices(s,extraerA(generadora),s.length,extraerA(generadora).length,extraerA(generadora)[0].length)[0][i-12];
                }
            } 
            seteado = true;
        } else if(algo3(multiplyMatrices(s,extraerA(generadora),s.length,extraerA(generadora).length,extraerA(generadora)[0].length),extraerA(generadora))<=2){
           
            for(int i=0; i<24; i++){
                if(i < 12){
                    if(i==algo3(multiplyMatrices(s,extraerA(generadora),s.length,extraerA(generadora).length,extraerA(generadora)[0].length),extraerA(generadora))){
                        e[0][i] = 1;
                    }
                    else{
                        e[0][i] = 0;
                    }
                } else {
                    e[0][i] = multiplyMatrices(s,extraerA(generadora),s.length,extraerA(generadora).length,extraerA(generadora)[0].length)[algo3(multiplyMatrices(s,extraerA(generadora),s.length,extraerA(generadora).length,extraerA(generadora)[0].length),extraerA(generadora))][i-12];
                }
            }
            seteado = true;
        }
        
        if(seteado){
            int[][] toret = new int[1][12];
            for(int i = 0;i < 12;i++){
                toret[0][i] = e[0][i] + palabra[0][i]; 
            }
            return toret;
        } else {
            System.out.println("La palabra recibida contiene más de tres errores.");
            return palabra;
        }
        
        
    }
    
    //Suma la palabra con cada fila de la matriz A
    private static int algo3(int[][] palabra, int[][] matriz){
        int fila = 0;
        boolean end = false;
        int[][] aux = new int[1][matriz[0].length];
        while(!end || fila < 12){
            for(int i = 0; i < matriz.length; i++){
                for(int j = 0; j < matriz[0].length; j++){
                    aux[0][j] = palabra[0][j] + matriz[i][j];
                }
                if(contarUnos(aux)<=2){
                    end = true;
                } else {
                    fila++;
                }
            }
        }
        return fila;
    }
    
    //G = (I12|A). Esta función extrae A de G
    private static int[][] extraerA(int[][] generadora){
        int[][] A = new int[generadora.length][generadora[0].length/2];
        for(int i=0;i<generadora.length;i++){
            for(int j=12;j<generadora[0].length; j++){
                A[i][j-12] = generadora[i][j];
            }
        }
        return A;
    }
    
    //Función que calcula el peso de un vector (contar numero de 1)
    private static int contarUnos(int[][] vector){
        int num = 0;
        for(int i=0;i<vector.length;i++){
            for(int j=0;j<vector[0].length;j++){
                if(vector[i][j] == 1){
                    num++;
                }
            }
        }
        return num;
    }
    
    //Función que multiplica matrices
    public static int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix, int r1, int c1, int c2) {
        int aux;
        int[][] product = new int[r1][c2];
        for(int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    if(firstMatrix[i][k] == 1 && secondMatrix[k][j]==1){
                        aux = 1;
                        if(aux == 1 && product[i][j] == 1){
                            product[i][j] = 0;
                        } else {
                            product[i][j] = aux + product[i][j];
                        }
                    } else {
                        aux = 0;
                        product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                    }
                }
            }
        }

        return product;
    }
    
    //Funcion que printea una matriz
    public static void displayProduct(int[][] product) {
        for(int[] row : product) {
            for (int column : row) {
                System.out.print(column + "    ");
            }
            System.out.println();
        }
    }
   
    //Funcion que transpone matriz
    public static int[][] transposeMatrix(int [][] m){
        int[][] aux = new int [m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                aux[j][i] = m[i][j];
            }
        }

        return aux;
    }
    
}
