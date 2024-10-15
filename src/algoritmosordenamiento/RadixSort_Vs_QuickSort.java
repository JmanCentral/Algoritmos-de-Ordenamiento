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


public class RadixSort_Vs_QuickSort extends Canvas {
    public ArrayList<double[]> dataradix;
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
 *   - copia Quick : Copia de los valores desordenados para ordenarlos con el algoritmo de burbuja .
 *   - copiaRadix: Copia de los valores desordenados para ordenarlos con el algoritmo de burbuja mejorado.
 *   - resultadosQuick : guarda el arreglo ordenado con el metodo Quick
 *   - resultadosRadix : guarda el arreglo con el metodo Rzdix
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
       	 
        	int[] copiaradix = Arrays.copyOf(valoresDesordenados, cantidad );

        	int[] copiaquick = Arrays.copyOf(valoresDesordenados, cantidad );
       	 
       	 

       	 
        	ArrayList<double[]> resultadosRadix = ordenarConRadixsort(copiaradix);
        	ArrayList<double[]> resultadosquick = ordenarConQuickSort(copiaquick);

        	
                Frame frame = new Frame("Gráfico AWT1");
                RadixSort_Vs_QuickSort canvas = new RadixSort_Vs_QuickSort(resultadosRadix, resultadosquick);
                frame.add(canvas);
                frame.setSize(1000, 720);
                frame.setVisible(true);
                
                    frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    frame.dispose(); // Cierra la ventana cuando se hace clic en el botón de cerrar
                }
            });
                      
        	escribirResultados(resultadosRadix, "resultados_Radix.txt");
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
            valorAleatorio = Math.abs(random.nextInt(1000000)); 
        } while (valoresUnicos.contains(valorAleatorio));

        valores[i] = valorAleatorio;
        valoresUnicos.add(valorAleatorio);
    }

    return valores;
}


	/***********************************************************************************************************
* Nombre Método: getDigit
* Propósito: Obtiene un dígito específico de un número dado en una posición determinada.
* Variables utilizadas: 
*       - num: Número del cual se extrae el dígito.
*       - position: Posición del dígito a obtener.
* Precondición: El número debe ser un entero válido y la posición debe ser un número no negativo.
* Postcondición: Devuelve el dígito en la posición indicada.
**********************************************************************************************************/
private static int getDigit(int num, int position) {
    return (num / (int) Math.pow(10, position)) % 10;
}

  
   /***********************************************************************************************************
* Nombre Método: getMaxDigit
* Propósito: Obtiene el número máximo de dígitos presentes en los elementos de un arreglo.
* Variables utilizadas: 
*       - arr: Arreglo de enteros del cual se calcula el máximo número de dígitos.
*       - max: Almacena el número máximo de dígitos encontrado.
*       - numDigits: Número de dígitos en el número actual del arreglo.
* Precondición: El arreglo no debe ser nulo y debe contener al menos un elemento.
* Postcondición: Devuelve el máximo número de dígitos encontrado en los elementos del arreglo.
**********************************************************************************************************/
private static int getMaxDigit(int[] arr) {
    int max = 0;
    for (int num : arr) {
        int numDigits = (int) Math.log10(num) + 1;
        max = Math.max(max, numDigits);
    }
    return max;
}

    
    
/***********************************************************************************************************
* Nombre Método: ordenarConRadixsort
* Propósito: Implementación del algoritmo de ordenación Radix Sort para ordenar un arreglo de enteros.
* Variables utilizadas: 
*       - arreglo: Arreglo de enteros a ordenar.
*       - resultados: ArrayList que almacena los datos de iteraciones y tiempo para análisis.
*       - iteraciones: Contador de iteraciones en el algoritmo.
*       - maxDigit: Máximo número de dígitos en el arreglo.
*       - tiempoInicio: Tiempo de inicio del algoritmo para cálculos de tiempo.
* Precondición: El arreglo no debe ser nulo y debe contener enteros.
* Postcondición: Devuelve un ArrayList con datos de iteraciones y tiempo hasta que se complete el algoritmo o alcance un límite.
**********************************************************************************************************/
public static ArrayList<double[]> ordenarConRadixsort(int[] arreglo) {
    ArrayList<double[]> resultados = new ArrayList<>();
    int iteraciones = 0;
    int maxDigit = getMaxDigit(arreglo);
    double tiempoInicio = System.nanoTime();
    int n = arreglo.length; // Tamaño del arreglo

    for (int position = 0; position < maxDigit; position++) {
        int[] output = new int[n];
        int[] count = new int[10];
        Arrays.fill(count, 0);

        for (int num : arreglo) {
            int digit = getDigit(num, position);
            count[digit]++;
            iteraciones++;
            double tiempoActual;
            
           
            tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; 
            resultados.add(new double[]{iteraciones, tiempoActual});

            if (iteraciones >= n) {
                return resultados; // Detiene el proceso si las iteraciones alcanzan el tamaño del arreglo
            }
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            int digit = getDigit(arreglo[i], position);
            output[count[digit] - 1] = arreglo[i];
            count[digit]--;
            iteraciones++;
            double tiempoActual;

          
            tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; 
            resultados.add(new double[]{iteraciones, tiempoActual});

            if (iteraciones >= n) {
                return resultados; // Detiene el proceso si las iteraciones alcanzan el tamaño del arreglo
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
    
    int n = arreglo.length; 
    ArrayList<double[]> resultados = new ArrayList<>();
    double tiempoInicio = System.nanoTime();
    Iteraciones++;
        double tiempoActual;
         
        tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; // Segundos
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
        
        if (Iteraciones >= arreglo.length) {
            return; // Detener el Quicksort si el contador de iteraciones es igual o mayor que la longitud del arreglo
        }
        int indiceParticion = particionar(arreglo, inicio, fin, resultados, tiempoInicio, n);
        quickSort(arreglo, inicio, indiceParticion - 1, resultados, tiempoInicio, n );
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
        tiempoActual = (System.nanoTime() - tiempoInicio) / 1e6; // Segundos
        resultados.add(new double[]{Iteraciones, tiempoActual});
    }

    int temp = arreglo[i + 1];
    arreglo[i + 1] = arreglo[fin];
    arreglo[fin] = temp;

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
        
        
        public RadixSort_Vs_QuickSort(ArrayList<double[]> dataradix, ArrayList<double[]> dataquick) {
        this.dataradix = dataradix;
        this.dataquick = dataquick;
    }

/***********************************************************************************************************
* Nombre del Método: paint
* Propósito: Dibuja un gráfico en nativo  con datos de dos listas de puntos y etiquetas en un contexto de gráficos.
* Variables utilizadas:
*   - dataRadix: ArrayList de double[] que contiene los datos del ordenamiento Radixsort.
*   - dataQuick: ArrayList de double[] que contiene los datos del ordenamiento Quicksort.
*   - g: El contexto de gráficos en el que se dibujará el gráfico.
*   - int offsetX :  Margen izquierdo
*   -  int offsetY :  Margen derecho
*   -  int width : Ancho del gráfico
*   -  int height : Alto del gráfico
*   -  double maxX : Calcular valores máximos
*   -  double maxY : Calcular valores máximos
*   -  int x : lineas horizontales
*   -  int y : lineas verticales
*   -  double xValue : hallar los maximos segun los datos en el eje x
*   -  double yValue : hallar los maximos segun los datos en el eje y
*   -  int xTexto :  Posición x original
*   -  int yTexto :  Posición y original
*   -  int x1 : graficar los datos
*   -  int y1 : graficar los datos
*   -  int x2 : graficar los datos
*   -  int y2 : graficar los datos
* Precondición:
*   - Loss Arrays  dataRadix y dataQuick deben contener datos válidos para graficar .
*   - El contexto de gráficos g debe estar inicializado y configurado para el dibujo.
* Postcondición:
*   - Dibuja un gráfico en forma nativo  que muestra datos de las listas  dataRadix y dataQuick con ejes etiquetados y un título.
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
                for (double[] punto : dataradix) {
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
                g.drawString("Radixsort VS Quicksort", offsetX + width / 2 - 200, offsetY - 60);
                g.setFont(new Font("times new roman", Font.BOLD, 20));
                // Títulos
                g.setColor(Color.BLACK);
                g.drawString("Cantidad de datos", offsetX + width / 2 - 30, offsetY + height + 50);
               // Rotar y dibujar "Tiempo (ms)"

                	String texto = "Tiempo(Mili)";
                	int xTexto = offsetX - 100;  
                	int yTexto = offsetY + height / 2;  

                	AffineTransform transformacionOriginal = ((Graphics2D) g).getTransform();

                	AffineTransform rotacion = AffineTransform.getRotateInstance(-Math.PI / 2, xTexto, yTexto);
                	((Graphics2D) g).setTransform(rotacion);

                	g.drawString(texto, xTexto, yTexto);

                	((Graphics2D) g).setTransform(transformacionOriginal);
               
   

                
                g.setColor(Color.BLUE);
                BasicStroke solidStroke = new BasicStroke(2.0f);  
                ((Graphics2D) g).setStroke(solidStroke);
                for (int i = 0; i < dataradix.size() - 1; i++) {
                    int x1 = offsetX + (int) (dataradix.get(i)[0] * (width / maxX));
                    int y1 = offsetY + height - (int) (dataradix.get(i)[1] * (height / maxY));
                    int x2 = offsetX + (int) (dataradix.get(i + 1)[0] * (width / maxX));
                    int y2 = offsetY + height - (int) (dataradix.get(i + 1)[1] * (height / maxY));

                    GradientPaint gradient = new GradientPaint(x1, y1, Color.BLUE, x2, y2, Color.WHITE);
                    ((Graphics2D) g).setPaint(gradient);
                    g.drawLine(x1, y1, x2, y2);

                     g.setColor(Color.BLUE); 
                    g.drawOval(x2 - 2, y2 - 2, 4, 4);  // Puntos


                }

                // Datos de Inserción
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

                g.drawRect(400, 635, 230, 30); 

                g.setColor(Color.BLACK);

                // Dibujar el texto
                drawColoredLabel(g, "radix", 410, 656, Color.BLUE);
                drawColoredLabel(g, "Quick", 550, 656, Color.YELLOW);

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
