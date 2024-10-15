package algoritmosordenamiento;



import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

public class HeapSort_Vs_MergeSort extends Canvas {
 
    public ArrayList<double[]> dataHeap;
    public ArrayList<double[]> dataMerge;


        /***********************************************************************************************************
    	* Nombre Método: main
    	* Propósito: La función principal del programa. Solicita al usuario la cantidad de elementos aleatorios,
    	*   		 genera esos elementos aleatorios, los ordena utilizando dos algoritmos diferentes, crea un
    	*   		 gráfico combinado en forma nativa  de los resultados y escribe los resultados en archivos de texto.
    	* Variables utilizadas:
    	*   - cantidad: Almacena la cantidad de elementos aleatorios ingresados por el usuario.
    	*   - valido: Booleano que indica si la cantidad ingresada es válida (no negativa).
    	*   - valoresDesordenados: Almacena los elementos aleatorios generados.
    	*   - copiaHeap: Copia de los valores desordenados para ordenarlos con el algoritmo de Heapsrot.
    	*   - copiaMerge : Copia de los valores desordenados para ordenarlos con el algoritmo de Mergesort.
    	*   - resultadosintercalacion: guarda el arreglo con el metodo intercalacion.
    	*   - resultadosinsercion : guarda el arreglo ordenado con el metodo insercion.
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

    	int[] copiaHeap = Arrays.copyOf(valoresDesordenados, cantidad);
    	int[] copiaMerge = Arrays.copyOf(valoresDesordenados, cantidad);

        ArrayList<double[]> resultadosHeap = ordenarConHeapsort(copiaHeap);
    	ArrayList<double[]> resultadosMerge = ordenarConMergesort(copiaMerge);
        
       

    	Frame frame = new Frame("Gráfico AWT2");
        HeapSort_Vs_MergeSort canvas = new HeapSort_Vs_MergeSort(resultadosHeap , resultadosMerge);
        frame.add(canvas);
        frame.setSize(1000, 720);
        frame.setVisible(true); 
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                frame.dispose(); // Cierra la ventana cuando se hace clic en el botón de cerrar
            }
        });

    	escribirResultados(resultadosHeap, "resultados_Heap.txt");
    	escribirResultados(resultadosMerge, "resultados_Merge.txt");
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
* Nombre Método: ordenarConHeapsort
* Propósito: Ordenar un arreglo de enteros utilizando el algoritmo de ordenamiento HeapSort. El método 
*            registra el número de iteraciones y el tiempo transcurrido durante el proceso.
* Variables utilizadas: 
*   - arr: Arreglo de enteros a ordenar.
*   - N: Longitud del arreglo.
*   - iteraciones: Contador de intercambios realizados durante el proceso de ordenamiento.
*   - resultados: ArrayList que almacena información sobre el número de intercambios y el tiempo transcurrido
*                 en cada iteración.
*   - tiempoInicio: Tiempo en nanosegundos al inicio del método.
* Precondición: El arreglo no debe ser nulo.
* Postcondición: El arreglo estará ordenado y se habrán registrado los intercambios y el tiempo transcurrido.
***********************************************************************************************************/
public static ArrayList<double[]> ordenarConHeapsort(int[] arr) {
    ArrayList<double[]> resultados = new ArrayList<>();
    int N = arr.length;
    int iteraciones = 0; 

    // Build heap (rearrange array)
    for (int i = N / 2 - 1; i >= 0; i--) {
        heapify(arr, N, i);
    }
    double tiempoInicio = System.nanoTime(); 

    // One by one extract an element from heap
    for (int i = N - 1; i > 0; i--) {
        // Move current root to end
        int temp = arr[0];
        arr[0] = arr[i];
        arr[i] = temp;
        iteraciones++;  // Incrementa el contador de intercambios

        // Call max heapify on the reduced heap
        heapify(arr, i, 0);

        double tiempoActual;

        tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; // Segundos
        resultados.add(new double[]{iteraciones, tiempoActual});
    } 

    return resultados;
}

/***********************************************************************************************************
* Nombre Método: heapify
* Propósito: Realizar el proceso de heapify en un subárbol de un arreglo de enteros para el algoritmo HeapSort.
* Variables utilizadas: 
*   - arr: Arreglo de enteros a heapify.
*   - N: Tamaño del subárbol.
*   - i: Índice del nodo raíz del subárbol.
* Precondición: El arreglo no debe ser nulo.
* Postcondición: El subárbol estará heapificado.
***********************************************************************************************************/
public static void heapify(int arr[], int N, int i) {
    int largest = i; // Initialize largest as root
    int l = 2 * i + 1; // left = 2*i + 1
    int r = 2 * i + 2; // right = 2*i + 2

    // If left child is larger than root
    if (l < N && arr[l] > arr[largest]) {
        largest = l;
    }

    // If right child is larger than largest so far
    if (r < N && arr[r] > arr[largest]) {
        largest = r;
    }

    // If largest is not root
    if (largest != i) {
        int swap = arr[i];
        arr[i] = arr[largest];
        arr[largest] = swap;

        // Recursively heapify the affected sub-tree
        heapify(arr, N, largest);
    }
}
/***********************************************************************************************************/

        
        /****************************************************
 	* Nombre Método: ordenarConMergesort
 	* Propósito: Ordena un arreglo utilizando el método de Mergesort y devuelve información sobre las iteraciones y el tiempo.
 	* Variables utilizadas:
 	* - mitad : se usa para divir el arreglo desordenado
 	* - mitad izquierda : se usa para guarda la mitad del arreglo de la iazquiera
 	* - mitad derecha : se usa para guardar la mitad del arreglo de la derecha
 	* - arreglo: Arreglo de enteros que se va a ordenar.
 	* - N: Entero que indica la longitud del arreglo.  
 	* - resultados: ArrayList que almacena pares de valores (iteraciones, tiempo) en cada iteración.
 	* - tiempoInicio : Tiempo en nanosegundos en el que comienza la ordenación.
 	* - tiempoActual : Tiempo final convertido en miliegundos con 1e6
 	* - iteraciones : cuenta el número de iteraciones realizadas en el proceso de ordenación.
 	* - i, j , k : indices utilizados en los bucles de ordenación.
 	* Precondición: El arreglo debe contener valores enteros.
 	* Postcondición: El arreglo se ordena y se devuelve una lista de pares (iteraciones, tiempo) que registra el
 	* número de iteraciones y el tiempo transcurrido en cada una de ellas.*/
        
	public static ArrayList<double[]> ordenarConMergesort(int[] arreglo) {
    	int N = arreglo.length;
    	ArrayList<double[]> resultados = new ArrayList<>();
    	
    	int iteraciones = 0;

   	 
    	if (N < 2) {
        	return resultados;
    	}

   	 
    	int mitad = N / 2;
    	int[] mitadIzquierda = Arrays.copyOfRange(arreglo, 0, mitad);
    	int[] mitadDerecha = Arrays.copyOfRange(arreglo, mitad, N);

    	// Llamar recursivamente para ordenar las dos mitades
    	ArrayList<double[]> resultadosIzquierda = ordenarConMergesort(mitadIzquierda);
    	ArrayList<double[]> resultadosDerecha = ordenarConMergesort(mitadDerecha);

   	double tiempoInicio = System.nanoTime();
    	int i = 0, j = 0, k = 0;
    	while (i < mitadIzquierda.length && j < mitadDerecha.length) {
        	if (mitadIzquierda[i] <= mitadDerecha[j]) {
            	arreglo[k++] = mitadIzquierda[i++];
        	} else {
            	arreglo[k++] = mitadDerecha[j++];
        	}
        	iteraciones++;
        	double tiempoActual;

                tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; 
        	resultados.add(new double[]{iteraciones, tiempoActual});
    	}

   	 
    	while (i < mitadIzquierda.length) {
        	arreglo[k++] = mitadIzquierda[i++];
        	iteraciones++;
        	double tiempoActual;
                tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; 
        	resultados.add(new double[]{iteraciones, tiempoActual});
    	}

   	 
    	while (j < mitadDerecha.length) {
        	arreglo[k++] = mitadDerecha[j++];
        	iteraciones++;
        	double tiempoActual;
               
                tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; 
        	resultados.add(new double[]{iteraciones, tiempoActual});
    	}

    	return resultados;
	}

	
	public static void escribirResultados(ArrayList<double[]> resultados, String nombreArchivo) {
    	try (FileWriter archivo = new FileWriter(nombreArchivo); PrintWriter escritor = new PrintWriter(archivo)) {

        	escritor.println("Iteraciones\tTiempo (ns/ms)");
        	for (double[] resultado : resultados) {
            	int iteraciones = (int) resultado[0]; // Convertir la iteración a int
            	double tiempo = resultado[1];
            	escritor.printf("%d<------------>%.3f%n", iteraciones, tiempo); // Formatear el tiempo con tres decimales
        	}
    	} catch (IOException e) {
        	e.printStackTrace();
    	}
	}
        
          public HeapSort_Vs_MergeSort(ArrayList<double[]> dataHeap, ArrayList<double[]> dataMerge) {
           this.dataHeap = dataHeap;
           this.dataMerge = dataMerge;
    }

 /***********************************************************************************************************
* Nombre del Método: paint
* Propósito: Dibuja un gráfico en nativo  con datos de dos listas de puntos y etiquetas en un contexto de gráficos.
* Variables utilizadas:
*   - dataHeap: ArrayList de double[] que contiene los datos del metodo heapsort.
*   - dataMerge: ArrayList de double[] que contiene los datos del metodo mergesort.
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
                
                double maxX = 0;
                double maxY = 0;
                for (double[] punto : dataHeap) {
                    maxX = Math.max(maxX, punto[0]);
                    maxY = Math.max(maxY, punto[1]);
                }
                for (double[] punto : dataMerge) {
                    maxX = Math.max(maxX, punto[0]);
                    maxY = Math.max(maxY, punto[1]);
                }
                maxX = Math.ceil(maxX / 10) * 10;
                
                g.drawRect(offsetX, offsetY, width, height);
                // Ejes X e Y escalados en función de los valores máximos
                g.drawLine(offsetX, offsetY + height, offsetX, offsetY);  // Eje Y
                g.drawLine(offsetX, offsetY + height, offsetX + width, offsetY + height);  // Eje X

                                // Cuadrícula
                g.setColor(Color.WHITE);
                for (int i = 0; i <= 10; i++) {
                    double xValue = i * maxX / 10.0;
                    int x = offsetX + (int) (width - (xValue * width / maxX));
                    g.drawLine(x, offsetY, x, offsetY + height);  // Líneas verticales
                }

                for (int i = 1; i < 16; i++) {
                    double yValue = i * maxY / 15.0;
                    int y = offsetY + (int) (height - (yValue * height / maxY));
                    g.drawLine(offsetX, y, offsetX + width, y);  // Líneas horizontales
                }
                
                // Encierra toda la cuadrícula con un rectángulo
                g.drawRect(offsetX, offsetY, width, height);

                // Etiquetas en ejes escalados
                g.setColor(Color.BLACK);
                Font labelFont = new Font("Arial", Font.BOLD, 14);

                for (int i = 0; i < 11; i++) {
                    double xValue = i * maxX / 10.0;
                    int x = offsetX + (int) (xValue * width / maxX);
                    g.setFont(labelFont);
                    g.setColor(Color.BLACK);
                    g.drawString(Integer.toString((int) xValue), x - 10, offsetY + height + 20);  // Etiquetas en el eje X
                }

                for (int i = 0; i < 16; i++) {
                    double yValue = i * maxY / 15.0;
                    int y = offsetY + (int) (height - (yValue * height / maxY));
                    g.setFont(labelFont);
                    g.setColor(Color.BLACK);
                    String formattedLabel = String.format("%.3e", yValue);
                    g.drawString(formattedLabel, offsetX - 80, y + 5);  // Etiquetas en el eje Y
                }

                            // Encerrar toda la cuadrícula con un rectángulo
                g.drawRect(offsetX, offsetY, width, height);

                
                g.drawLine(offsetX, offsetY + height, offsetX, offsetY);  // Eje Y
                g.drawLine(offsetX, offsetY + height, offsetX + width, offsetY + height);  // Eje X

                // Título en la parte superior del gráfico
                g.setColor(Color.BLACK);
                g.setFont(new Font("times new roman", Font.BOLD, 32));
                g.drawString("Heapsort vs Mergesort", offsetX + width / 2 - 200, offsetY - 60);
                g.setFont(new Font("times new roman", Font.BOLD, 20));        
                g.setColor(Color.BLACK);
                g.drawString("Cantidad de datos", offsetX + width / 2 - 50 , offsetY + height + 50);
                   
               
                
                	String texto = "Tiempo(Mili)";
                	int xTexto = offsetX - 100;  
                	int yTexto = offsetY + height / 2;  

                	AffineTransform transformacionOriginal = ((Graphics2D) g).getTransform();

                	AffineTransform rotacion = AffineTransform.getRotateInstance(-Math.PI / 2, xTexto, yTexto);
                	((Graphics2D) g).setTransform(rotacion);

                	g.drawString(texto, xTexto, yTexto);

                	((Graphics2D) g).setTransform(transformacionOriginal);
                   
               

                Color colorVerdeOscuro = new Color(0, 128, 0);
                g.setColor(colorVerdeOscuro);
                BasicStroke solidStroke = new BasicStroke(2.0f);  // Grosor de la línea sólida
                ((Graphics2D) g).setStroke(solidStroke);
                for (int i = 0; i < dataHeap.size() - 1; i++) {
                    int x1 = offsetX + (int) (dataHeap.get(i)[0] * (width / maxX));
                    int y1 = offsetY + height - (int) (dataHeap.get(i)[1] * (height / maxY));
                    int x2 = offsetX + (int) (dataHeap.get(i + 1)[0] * (width / maxX));
                    int y2 = offsetY + height - (int) (dataHeap.get(i + 1)[1] * (height / maxY));

                    GradientPaint gradient = new GradientPaint(x1, y1, colorVerdeOscuro, x2, y2, Color.WHITE);
                    ((Graphics2D) g).setPaint(gradient);
                    g.drawLine(x1, y1, x2, y2);

                    g.setColor(colorVerdeOscuro); 
                    g.drawOval(x2 - 2, y2 - 2, 4, 4);  // Puntos


                }

                
                g.setColor(Color.ORANGE);
                BasicStroke dashedStroke = new BasicStroke(2.0f);  // Línea punteada
                ((Graphics2D) g).setStroke(dashedStroke);
                for (int i = 0; i < dataMerge.size() - 1; i++) {
                    int x1 = offsetX + (int) (dataMerge.get(i)[0] * (width / maxX));
                    int y1 = offsetY + height - (int) (dataMerge.get(i)[1] * (height / maxY));
                    int x2 = offsetX + (int) (dataMerge.get(i + 1)[0] * (width / maxX));
                    int y2 = offsetY + height - (int) (dataMerge.get(i + 1)[1] * (height / maxY));

                    GradientPaint gradient = new GradientPaint(x1, y1, Color.ORANGE, x2, y2, Color.WHITE);
                    ((Graphics2D) g).setPaint(gradient);
                    g.drawLine(x1, y1, x2, y2);

                    g.setColor(Color.ORANGE); 
                    g.drawOval(x2 - 2, y2 - 2, 4, 4);  // Puntos
                }

                                // Configurar el color de fondo del rectángulo             
                g.setColor(Color.WHITE);

                
                g.fillRect(400, 640, 330, 20); 

                
                g.setColor(Color.BLACK);

                
                g.drawRect(400, 635, 265, 30); 

                
                g.setColor(Color.BLACK);

                drawColoredLabel(g, "Heapsort", 410, 656, colorVerdeOscuro);
                drawColoredLabel(g, "Mergesort", 550, 656, Color.ORANGE);

            }
            
            /***********************************************************************************************************
            * Nombre del Método: drawColoredLabel
            * Propósito: Dibuja un etiqueta de texto con un círculo de color al lado en un contexto de gráficos.
            * Variables utilizadas:
            *   - g: El contexto de gráficos en el que se dibujará la etiqueta y el círculo.
            *   - label: El texto que se mostrará en la etiqueta.
            *   - x: La coordenada x donde se ubicará la etiqueta.
            *   - y: La coordenada y donde se ubicará la etiqueta.
            *   - color: El color del círculo que acompaña a la etiqueta.
            *   - circleX : pone el lugar del circulo en el eje x
            *   - circleY : pone el lugar del circulo en el eje y 
            * Precondición:
            *   - El contexto de gráficos g debe estar inicializado y configurado para el dibujo.
            *   - Los valores de x e y deben ser válidos en el contexto de gráficos g.
            *   - El color debe ser una instancia válida de la clase Color.
            * Postcondición: 
            *   - Dibuja una etiqueta de texto con el texto "label" en las coordenadas (x, y) y un círculo de "color" al lado de la etiqueta.
            **********************************************************************************************************/

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
