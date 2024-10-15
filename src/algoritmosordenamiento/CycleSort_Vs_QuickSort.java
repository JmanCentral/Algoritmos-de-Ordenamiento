package algoritmosordenamiento;



import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;


public class CycleSort_Vs_QuickSort extends Canvas {
	public ArrayList<double[]> datacycle;
	public ArrayList<double[]> dataquick;
    

/***********************************************************************************************************
 * Nombre Método: main
 * Propósito: La función principal del programa. Solicita al usuario la cantidad de elementos aleatorios,
 *        	genera esos elementos aleatorios, los ordena utilizando dos algoritmos diferentes, crea un
 *        	gráfico combinado en forma nativa de los resultados y escribe los resultados en archivos de texto.
 * Variables utilizadas:
 *   - cantidad: Almacena la cantidad de elementos aleatorios ingresados por el usuario.
 *   - valido: Booleano que indica si la cantidad ingresada es válida (no negativa).
 *   - valoresDesordenados: Almacena los elementos aleatorios generados.
 *   - copiaCycle : Copia de los valores desordenados para ordenarlos con el algoritmo cyclesort .
 *   - copiaQuick: Copia de los valores desordenados para ordenarlos con el algoritmo quicksort.
 *   - resultadosCycle : guarda el arreglo ordenado con el metodo Cyclesort
 *   - resultadosQuick : guarda el arreglo con el metodo Quicksort
 * Precondicion : la cantidad debe ser un numero positivo sin comas ni otro caracteres especial , aparte los arreglos 
 * de los valores aleatorios , y la copia para los dos metodos a usar deben estar definidos
 * Poscondicion : Los resultados de los algoritmos de ordenación se muestran en un gráfico combinado 
 * y se guardan los datos  en archivos de texto 
**********************************************************************************************************/  
    
public static void main(String[] args) {
  	 
		 int cantidad = 0;

  		 boolean valido = false;
  	 
       		 while (!valido) {
           		 try {
               		 cantidad = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de elementos aleatorios (N):"));

               		 if (cantidad >= 0) {
                   		 valido = true;
               		 } else {
                   		 JOptionPane.showMessageDialog(null, "Ingrese un número entero no negativo.");
               		 }
           		 } catch (NumberFormatException e) {
               		 JOptionPane.showMessageDialog(null, "Ingrese un número entero válido.");
           		 }
       		 }


   		 int[] valoresDesordenados = generarValoresAleatorios(cantidad);
  		 
   		 int[] copiacyle = Arrays.copyOf(valoresDesordenados, cantidad );
   		 int[] copiaquick = Arrays.copyOf(valoresDesordenados, cantidad );
  		 
  		 

  		 
   		 ArrayList<double[]> resultadoscycle = ordenarConCyclesort(copiacyle);
   		 ArrayList<double[]> resultadosquick = ordenarConQuickSort(copiaquick);

   		 
            	Frame frame = new Frame("Gráfico AWT1");
            	CycleSort_Vs_QuickSort canvas = new CycleSort_Vs_QuickSort(resultadoscycle, resultadosquick);
            	frame.add(canvas);
            	frame.setSize(1000, 720);
            	frame.setVisible(true);
           	 
                	frame.addWindowListener(new WindowAdapter() {
            	public void windowClosing(WindowEvent we) {
                	frame.dispose(); // Cierra la ventana cuando se hace clic en el botón de cerrar
            	}
        	});
                 	 
   		 escribirResultados(resultadoscycle, "resultados_Cycle.txt");
   		 escribirResultados(resultadosquick, "resultados_Quick.txt");
  		 
    }

  /***********************************************************************************************************
	* Nombre Método: generarValoresAleatorios
	* Propósito: Genera un arreglo de enteros de longitud N con valores únicos aleatorios.
	* Variables utilizadas:
	*   - N: Entero que indica la cantidad de valores aleatorios a generar.
	*   - valores: Arreglo de enteros que almacena los valores aleatorios únicos generados.
	*   - valoresUnicos: Conjunto que almacena los valores aleatorios únicos generados.
	*   - random: Objeto Random utilizado para generar números aleatorios.
	*   - valorAleatorio: Entero que almacena el valor aleatorio generado en cada iteración.
	* Precondición: El valor de N debe ser un número positivo para generar un array de valores aleatorios únicos.
	* Postcondición: Se devuelve un arreglo de enteros de longitud N con valores únicos aleatorios.
	**********************************************************************************************************/  

public static int[] generarValoresAleatorios(int N) {

    int[] valores = new int[N];
    Set<Integer> valoresUnicos = new HashSet<>();
    Random random = new Random();

    for (int i = 0; i < N; i++) {
   	 int valorAleatorio;
   	 do {
   		 valorAleatorio = random.nextInt();
   	 } while (valoresUnicos.contains(valorAleatorio)); // Verifica si el valor ya existe en el conjunto

   	 valores[i] = valorAleatorio;
   	 valoresUnicos.add(valorAleatorio); // Agrega el valor único al conjunto
    }

    return valores;
}
    /***********************************************************************************************************
* Nombre Método: ordenarConCyclesort
* Propósito: Ordenar un arreglo de enteros utilizando el algoritmo de Cycle Sort y registrar el número de 
* iteraciones realizadas junto con el tiempo transcurrido en cada iteración.
* Variables utilizadas:
*   - arr: Arreglo de enteros a ordenar.
*   - resultados: ArrayList que almacena los datos de cada iteración (número de iteración, tiempo transcurrido).
*   - tiempoInicio: Tiempo en nanosegundos al inicio del método.
*   - iteraciones: Contador de iteraciones realizadas en el algoritmo.
*   - n: Longitud del arreglo de entrada.
*   - ciclo: Variable utilizada para recorrer el arreglo en el algoritmo Cycle Sort.
*   - elemento: Elemento actual que se está ordenando.
*   - pos: Posición actual del elemento en el arreglo.
* Precondición: El arreglo 'arr' no debe ser nulo y debe contener elementos.
* Postcondición: 'resultados' contendrá pares de valores [iteración, tiempo] que representan el número de
* iteración y el tiempo transcurrido al ordenar el arreglo en cada iteración.
**********************************************************************************************************/

    public static ArrayList<double[]> ordenarConCyclesort(int[] arr) {
    	int n = arr.length;
    	ArrayList<double[]> resultados = new ArrayList<>();
    	double tiempoInicio = System.nanoTime();
    	int iteraciones = 0;

    	for (int ciclo = 0; ciclo <= n - 2; ciclo++) {
        	int elemento = arr[ciclo];
        	int pos = ciclo;
       	 
        	for (int i = ciclo + 1; i < n; i++) {
            	if (arr[i] < elemento) {
                	pos++;
            	}
        	}

        	if (pos == ciclo) {
            	continue;
        	}

        	while (elemento == arr[pos]) {
            	pos++;
        	}

        	if (pos != ciclo) {
            	int temp = arr[pos];
            	arr[pos] = elemento;
            	elemento = temp;
            	iteraciones++;
            	double tiempoActual;
            	if (n <= 10000) {
                	tiempoActual = System.nanoTime() - tiempoInicio;
            	} 
            	else {
             	tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6;
            	}
            	resultados.add(new double[]{iteraciones, tiempoActual});
        	}

        	while (pos != ciclo) {
            	pos = ciclo;

            	for (int i = ciclo + 1; i < n; i++) {
                	if (arr[i] < elemento) {
                    	pos++;
                	}
            	}

            	while (elemento == arr[pos]) {
                	pos++;
            	}

            	if (elemento != arr[pos]) {
                	int temp = arr[pos];
                	arr[pos] = elemento;
                	elemento = temp;
                	iteraciones++;
                	double tiempoActual;
            	if (n <= 10000) {
                	tiempoActual = System.nanoTime() - tiempoInicio;
            	} 
            	else {
             	tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6;
            	}
                	resultados.add(new double[]{iteraciones, tiempoActual});
            	}
        	}  
    	}
   	 
    	return resultados;
	}    

   	private static int Iteraciones = 0;

        /***********************************************************************************************************
* Nombre Método: ordenarConQuickSort
* Propósito: Ordenar un arreglo de enteros utilizando el algoritmo de ordenamiento QuickSort. El método 
*            registra el número de iteraciones y el tiempo transcurrido durante el proceso.
* Variables utilizadas: 
*   - arreglo: Arreglo de enteros a ordenar.
*   - n: Longitud del arreglo.
*   - Iteraciones: Contador de iteraciones globales.
*   - resultados: ArrayList que almacena información sobre el número de iteraciones y el tiempo transcurrido
*                 en cada iteración.
*   - tiempoInicio: Tiempo en nanosegundos al inicio del método.
* Precondición: El arreglo no debe ser nulo.
* Postcondición: El arreglo estará ordenado y se habrán registrado las iteraciones y el tiempo transcurrido.
***********************************************************************************************************/

public static ArrayList<double[]> ordenarConQuickSort(int[] arreglo) {
	int n =  arreglo.length;
	
	ArrayList<double[]> resultados = new ArrayList<>();
	double tiempoInicio = System.nanoTime();
        Iteraciones++;
        double tiempoActual;
        if (n <= 10000) {
            tiempoActual = (System.nanoTime() - tiempoInicio);	
        } else {
            tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; // Segundos
        }
        resultados.add(new double[]{Iteraciones, tiempoActual});
	quickSort(arreglo, 0, arreglo.length - 1, resultados, tiempoInicio, n);

	return resultados;
}

/***********************************************************************************************************
* Nombre Método: quickSort
* Propósito: Implementación recursiva del algoritmo QuickSort para ordenar un subarreglo de enteros. 
*            Registra el número de iteraciones y el tiempo transcurrido durante el proceso.
* Variables utilizadas: 
*   - arreglo: Arreglo de enteros a ordenar.
*   - inicio: Índice de inicio del subarreglo.
*   - fin: Índice de fin del subarreglo.
*   - resultados: ArrayList que almacena información sobre el número de iteraciones y el tiempo transcurrido
*                 en cada iteración.
*   - tiempoInicio: Tiempo en nanosegundos al inicio del método.
*   - n: Longitud del arreglo.
* Precondición: El arreglo no debe ser nulo.
* Postcondición: El subarreglo estará ordenado y se habrán registrado las iteraciones y el tiempo transcurrido.
***********************************************************************************************************/

public static void quickSort(int[] arreglo, int inicio, int fin, ArrayList<double[]> resultados, double tiempoInicio, int n) {
	if (inicio < fin) {
    	if (Iteraciones >= arreglo.length - 1) {
        	return; // Detener el Quicksort si el contador de iteraciones es igual o mayor que la longitud del arreglo
    	}
    	int indiceParticion = particionar(arreglo, inicio, fin, resultados, tiempoInicio, n);
    	quickSort(arreglo, inicio, indiceParticion - 1, resultados, tiempoInicio, n);
    	quickSort(arreglo, indiceParticion + 1, fin, resultados, tiempoInicio, n);
	}
}

/***********************************************************************************************************
* Nombre Método: particionar
* Propósito: Particionar un subarreglo de enteros para el algoritmo QuickSort. Registra el número de iteraciones
*            y el tiempo transcurrido durante el proceso.
* Variables utilizadas: 
*   - arreglo: Arreglo de enteros a particionar.
*   - inicio: Índice de inicio del subarreglo.
*   - fin: Índice de fin del subarreglo.
*   - resultados: ArrayList que almacena información sobre el número de iteraciones y el tiempo transcurrido
*                 en cada iteración.
*   - tiempoInicio: Tiempo en nanosegundos al inicio del método.
*   - n: Longitud del arreglo.
* Precondición: El arreglo no debe ser nulo.
* Postcondición: El subarreglo estará particionado y se habrán registrado las iteraciones y el tiempo transcurrido.
***********************************************************************************************************/


public static int particionar(int[] arreglo, int inicio, int fin, ArrayList<double[]> resultados, double tiempoInicio, int n) {
	int pivote = arreglo[fin];
	int i = inicio - 1;

	for (int j = inicio; j < fin; j++) {
    	if (arreglo[j] < pivote) {
        	i++;
        	int temp = arreglo[i];
        	arreglo[i] = arreglo[j];
        	arreglo[j] = temp;
    	}
        
        Iteraciones++;
	double tiempoActual;
	if (n <= 10000) {
    	tiempoActual = (System.nanoTime() - tiempoInicio);
        }else {
    	tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; // Segundos
	}
	resultados.add(new double[]{Iteraciones, tiempoActual});
	}

	int temp = arreglo[i + 1];
	arreglo[i + 1] = arreglo[fin];
	arreglo[fin] = temp;

	// Incrementa el contador de iteraciones global y registra el tiempo
	

	return i + 1;
}


/***********************************************************************************************************
 * Nombre Método: escribirResultados
 * Propósito: Escribe los resultados de ordenación en un archivo de texto en el formato "Iteraciones\tTiempo (ms)".
 * Variables utilizadas:
 *   - resultados: ArrayList que contiene los resultados de ordenación en forma de pares (iteraciones, tiempo).
 *   - nombreArchivo: Nombre del archivo en el que se escribirán los resultados.
 *   - iteraciones : se convierte las iteraciones del arreglo del metodo double a entero
 *   - tiempo : se mantiene double
 * Precondición: El ArrayList resultados debe contener datos válidos.
 * Postcondición: Los resultados de ordenación se escriben en el archivo especificado en el formato deseado.
 **********************************************************************************************************/
  		 
  	 
    public static void escribirResultados(ArrayList<double[]> resultados, String nombreArchivo) {
    try (FileWriter archivo = new FileWriter(nombreArchivo);
		 PrintWriter escritor = new PrintWriter(archivo)) {

   	 escritor.println("Iteraciones\tTiempo (ns/ms)");
   	 for (double[] resultado : resultados) {
   		 int iteraciones = (int) resultado[0]; // Convertir la iteración a int
   		 double tiempo = resultado[1];
  		 
   		 escritor.printf("%d<------------>%.3f%n", iteraciones, tiempo);
   	 }
    } catch (IOException e) {
   	 e.printStackTrace();
    }
}
   	 
   	 
    	public CycleSort_Vs_QuickSort(ArrayList<double[]> datacycle, ArrayList<double[]> dataquick) {
    	this.datacycle = datacycle;
    	this.dataquick = dataquick;
	}


        /***********************************************************************************************************
* Nombre del Método: paint
* Propósito: Dibuja un gráfico en nativo  con datos de dos listas de puntos y etiquetas en un contexto de gráficos.
* Variables utilizadas:
*   - dataCyle: ArrayList de double[] que contiene los datos del metodo Cyclesort.
*   - dataQuick: ArrayList de double[] que contiene los datos del metodo Quicksort.
*   - g: El contexto de gráficos en el que se dibujará el gráfico.
*   - int offsetX :  Margen izquierdo
*     int offsetY :  Margen derecho
*     int width : Ancho del gráfico
*     int height : Alto del gráfico
*     double maxX : Calcular valores máximos
*     double maxY : Calcular valores máximos
*     int x : lineas horizontales 
*     int y : lineas verticales 
*     double xValue : hallar los maximos segun los datos en el eje x
*     double yValue : hallar los maximos segun los datos en el eje y
*     int xTexto :  Posición x original
*     int yTexto :  Posición y original
*     int x1 : graficar los datos
*     int y1 : graficar los datos
*     int x2 : graficar los datos
*     int y2 : graficar los datos
* Precondición:
*   - Loss Arrays  dataInserción y dataIntercalación deben contener datos válidos para graficar .
*   - El contexto de gráficos g debe estar inicializado y configurado para el dibujo.
* Postcondición: 
*   - Dibuja un gráfico en forma nativo  que muestra datos de las listas dataInsercion y dataIntercalacion con ejes etiquetados y un título.
**********************************************************************************************************/ 
    
        	public void paint(Graphics g) {
            	int offsetX = 120;  
            	int offsetY = 110;  
            	int width = getWidth() - 2 * offsetX;  
            	int height = getHeight() - 2 * offsetY;  

            	Color grisClaro = new Color(218, 222, 222);
            	g.setColor(grisClaro);
            	g.fillRect(offsetX, offsetY, width, height);

            	// Calcular valores máximos
            	double maxX = 0;
            	double maxY = 0;
            	for (double[] punto : datacycle) {
                	maxX = Math.max(maxX, punto[0]);
                	maxY = Math.max(maxY, punto[1]);
            	}
            	for (double[] punto : dataquick) {
                	maxX = Math.max(maxX, punto[0]);
                	maxY = Math.max(maxY, punto[1]);
            	}
            	maxX = Math.ceil(maxX / 10) * 10;
           	 
            	g.drawRect(offsetX, offsetY, width, height); 	 
            	g.drawLine(offsetX, offsetY + height, offsetX, offsetY);  // Eje Y
            	g.drawLine(offsetX, offsetY + height, offsetX + width, offsetY + height);  // Eje X

                            	// Cuadrícula
            	g.setColor(Color.WHITE);
            	for (int i = 0; i <= 10; i++) {
                	double xValue = i * maxX / 10.0;
                	int x = offsetX + (int) (width - (xValue * width / maxX));
                	g.drawLine(x, offsetY, x, offsetY + height);  // Líneas verticales
            	}
              	// Encerrar toda la cuadrícula con un rectángulo

            	for (int i = 1; i < 16; i++) {
                	double yValue = i * maxY / 15.0;
                	int y = offsetY + (int) (height - (yValue * height / maxY));
                	g.drawLine(offsetX, y, offsetX + width, y);  // Líneas horizontales
            	}
            	g.drawRect(offsetX, offsetY, width, height);

            	// Etiquetas en ejes escalados
            	g.setColor(Color.BLACK);
            	Font labelFont = new Font("Arial", Font.BOLD, 14);

            	for (int i = 0; i < 11; i++) {
                	double xValue = i * maxX / 10.0;
                	int x = offsetX + (int) (xValue * width / maxX );
                	g.setFont(labelFont);
                	g.setColor(Color.BLACK);
                	g.drawString(Integer.toString((int) xValue), x - 10, offsetY + height + 20);  // Etiquetas en el eje X
            	}

            	for (int i = 0; i < 16; i++) {
                	double yValue = i * maxY / 15.0;
                	int y = offsetY + (int) (height - (yValue * height / maxY ));
                	g.setFont(labelFont);
                	g.setColor(Color.BLACK);
                	String formattedLabel = String.format("%.3e", yValue);
                	g.drawString(formattedLabel, offsetX - 80, y + 5);  // Etiquetas en el eje Y
            	}

                        	// Encerrar toda la cuadrícula con un rectángulo
            	g.drawRect(offsetX, offsetY, width, height);

            	// Dibuja los ejes después del rectángulo para que estén en la parte superior
            	g.drawLine(offsetX, offsetY + height, offsetX, offsetY);  // Eje Y
            	g.drawLine(offsetX, offsetY + height, offsetX + width, offsetY + height);  // Eje X

            	// Título en la parte superior del gráfico
            	g.setColor(Color.BLACK);
            	g.setFont(new Font("times new roman", Font.BOLD, 32));
            	g.drawString("Cyclesort VS Quicksort", offsetX + width / 2 - 200, offsetY - 60);
            	g.setFont(new Font("times new roman", Font.BOLD, 20));
            	// Títulos
            	g.setColor(Color.BLACK);
            	g.drawString("Cantidad de datos", offsetX + width / 2 - 30, offsetY + height + 50);
           	// Rotar y dibujar "Tiempo (ms)"
           	 
            	if (datacycle.size() <= 10000 || dataquick.size() <= 10000) {
           		 String texto = "Tiempo (Nano)";
           		 int xTexto = offsetX - 100;  // Posición x original
           		 int yTexto = offsetY + height / 2;  // Posición y original

           		 // Guarda la transformación actual

           		 AffineTransform transformacionOriginal = ((Graphics2D) g).getTransform();

           		 // Rota el texto en 90 grados en sentido contrario a las agujas del reloj
           		 AffineTransform rotacion = AffineTransform.getRotateInstance(-Math.PI / 2, xTexto, yTexto);
           		 ((Graphics2D) g).setTransform(rotacion);

           		 // Dibuja el texto rotado
           		 g.drawString(texto, xTexto, yTexto);

           		 // Restaura la transformación original
           		 ((Graphics2D) g).setTransform(transformacionOriginal);
       		 } 
            	else  {
           		 String texto = "Tiempo(Mili)";
           		 int xTexto = offsetX - 100;  
           		 int yTexto = offsetY + height / 2;  

           		 AffineTransform transformacionOriginal = ((Graphics2D) g).getTransform();

           		 AffineTransform rotacion = AffineTransform.getRotateInstance(-Math.PI / 2, xTexto, yTexto);
           		 ((Graphics2D) g).setTransform(rotacion);

           		 g.drawString(texto, xTexto, yTexto);

           		 ((Graphics2D) g).setTransform(transformacionOriginal);
            	}

           	 

            	Color colorMorado = new Color(128, 0, 128);
           	 
            	g.setColor(colorMorado);
            	BasicStroke solidStroke = new BasicStroke(2.0f);  
            	((Graphics2D) g).setStroke(solidStroke);
            	for (int i = 0; i < datacycle.size() - 1; i++) {
                	int x1 = offsetX + (int) (datacycle.get(i)[0] * (width / maxX));
                	int y1 = offsetY + height - (int) (datacycle.get(i)[1] * (height / maxY));
                	int x2 = offsetX + (int) (datacycle.get(i + 1)[0] * (width / maxX));
                	int y2 = offsetY + height - (int) (datacycle.get(i + 1)[1] * (height / maxY));

                	GradientPaint gradient = new GradientPaint(x1, y1, colorMorado, x2, y2, Color.WHITE);
                	((Graphics2D) g).setPaint(gradient);
                	g.drawLine(x1, y1, x2, y2);

                 	g.setColor(colorMorado);
                	g.drawOval(x2 - 2, y2 - 2, 4, 4);  // Puntos


            	}

           	 
            	g.setColor(Color.YELLOW);
            	BasicStroke dashedStroke = new BasicStroke(2.0f);  // Línea punteada
            	((Graphics2D) g).setStroke(dashedStroke);
            	for (int i = 0; i < dataquick.size() - 1; i++) {
                	int x1 = offsetX + (int) (dataquick.get(i)[0] * (width / maxX));
                	int y1 = offsetY + height - (int) (dataquick.get(i)[1] * (height / maxY));
                	int x2 = offsetX + (int) (dataquick.get(i + 1)[0] * (width / maxX));
                	int y2 = offsetY + height - (int) (dataquick.get(i + 1)[1] * (height / maxY));

                	GradientPaint gradient = new GradientPaint(x1, y1, Color.YELLOW, x2, y2, Color.WHITE);
                	((Graphics2D) g).setPaint(gradient);
                	g.drawLine(x1, y1, x2, y2);

                	g.setColor(Color.YELLOW);
                	g.drawOval(x2 - 2, y2 - 2, 4, 4);  // Puntos
            	}

                           	 
            	g.setColor(Color.WHITE);

            	g.fillRect(400, 640, 330, 20);

            	g.setColor(Color.BLACK);

            	g.drawRect(400, 635, 225, 30);

            	g.setColor(Color.BLACK);

            	// Dibujar el texto
            	drawColoredLabel(g, "Cycle ", 410, 656, colorMorado);
            	drawColoredLabel(g, "Quick", 550, 656, Color.YELLOW);

        	}

       	 
        	private void drawColoredLabel(Graphics g, String label, int x, int y, Color color) {
            	g.setColor(Color.BLACK);
            	g.drawString(label, x, y);
            	int labelWidth = g.getFontMetrics().stringWidth(label);
            	int circleX = x + labelWidth + 5;
            	int circleY = y - 10;
            	g.setColor(color);
            	g.fillOval(circleX, circleY, 10, 10);
        	}

   	 
}


