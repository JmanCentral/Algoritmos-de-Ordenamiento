package algoritmosordenamiento;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;


public class BurbujaMejorado_Vs_Inserccion extends Canvas {
	public ArrayList<double[]> dataBurbujaMejorado;
	public ArrayList<double[]> dataInsercion;

    	/***********************************************************************************************************
 * Nombre Método: main
 * Propósito: La función principal del programa. Solicita al usuario la cantidad de elementos aleatorios,
 *  		  genera esos elementos aleatorios, los ordena utilizando dos algoritmos diferentes, crea un
 *  		  gráfico combinado de los resultados y escribe los resultados en archivos de texto.
 * Variables utilizadas:
 *   - cantidad: Almacena la cantidad de elementos aleatorios ingresados por el usuario.
 *   - valido: Booleano que indica si la cantidad ingresada es válida (no negativa).
 *   - valoresDesordenados: Almacena los elementos aleatorios generados.
 *   - copiaBurbujaMejorado: Copia de los valores desordenados para ordenarlos con el algoritmo de burbuja mejorado.
 *   - copia insercion : Copia de los valores desordenados para ordenarlos con el algoritmo de insercion .
 *   - resultadosburbujamejorado : guarda el arreglo con el metodo burbuja mejorado.
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

   	 int[] copiaBurbujaMejorado = Arrays.copyOf(valoresDesordenados, cantidad);
   	 int[] copiaInsercion = Arrays.copyOf(valoresDesordenados, cantidad);

   	 ArrayList<double[]> resultadosBurbujaMejorado = ordenarConBurbujaMejorado(copiaBurbujaMejorado);
   	 ArrayList<double[]> resultadosInsercion = ordenarConInsercion(copiaInsercion);

   	 Frame frame = new Frame("Gráfico AWT");
    	BurbujaMejorado_Vs_Inserccion canvas = new BurbujaMejorado_Vs_Inserccion(resultadosBurbujaMejorado, resultadosInsercion);
    	frame.add(canvas);
    	frame.setSize(1000, 720);
    	frame.setVisible(true);
   	 
    	frame.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent we) {
            	frame.dispose(); // Cierra la ventana cuando se hace clic en el botón de cerrar
        	}
    	});
   	 
   	 escribirResultados(resultadosBurbujaMejorado, "resultados_burbuja_mejorado1.txt");
   	 escribirResultados(resultadosInsercion, "resultados_insercion.txt");

    }

    
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

   	 
    
    public static ArrayList<double[]> ordenarConBurbujaMejorado(int[] arreglo) {
        	int N = arreglo.length;
        	ArrayList<double[]> resultados = new ArrayList<>();
        	double tiempoInicio = System.nanoTime();
        	int iteraciones = 0;
        	boolean intercambio = true;
    
       	 
       	 
        	while (intercambio) {
            	iteraciones++;
            	intercambio = false;
            	for (int j = 0; j < N - 1; j++) {
                	if (arreglo[j] > arreglo[j + 1]) {
                    	int auxiliar = arreglo[j];
                    	arreglo[j] = arreglo[j + 1];
                    	arreglo[j + 1] = auxiliar;
                    	intercambio = true;
                	}
            	}

            	double tiempoActual;
            	if (N <= 5000) {
                	tiempoActual = System.nanoTime() - tiempoInicio;
            	} else {
                	tiempoActual = (System.nanoTime() - tiempoInicio)/ 1e6;
            	}
            	resultados.add(new double[]{iteraciones, tiempoActual});
        	}

        	return resultados;
    	}

    	public static ArrayList<double[]> ordenarConInsercion(int[] arreglo) {
        	int N = arreglo.length;
        	ArrayList<double[]> resultados = new ArrayList<>();
        	double tiempoInicio = System.nanoTime();
        	int iteraciones = 0;

    
        	for (int i = 1; i < N; i++) {
            	iteraciones++;
            	int auxiliar = arreglo[i];
            	int j = i - 1;

            	while (j >= 0 && arreglo[j] > auxiliar) {
                	arreglo[j + 1] = arreglo[j];
                	j--;
            	}

            	arreglo[j + 1] = auxiliar;

            	double tiempoActual;
            	if (N <= 5000) {
                	tiempoActual = System.nanoTime() - tiempoInicio;
            	} else {
                	tiempoActual = (System.nanoTime() - tiempoInicio)/ 1e6;
            	}
            	resultados.add(new double[]{iteraciones, tiempoActual});
        	}

        	return resultados;
    	}


   	 
    public static void escribirResultados(ArrayList<double[]> resultados, String nombreArchivo) {
   	 try (FileWriter archivo = new FileWriter(nombreArchivo); PrintWriter escritor = new PrintWriter(archivo)) {

   		 escritor.println("Iteraciones\tTiempo(Ns/Ms)");
   		 for (double[] resultado : resultados) {
       		 int iteraciones = (int) resultado[0]; // Convertir la iteración a int
       		 double tiempo = resultado[1];
       		 escritor.printf("%d<------------>%.3f%n", iteraciones, tiempo); // Formatear el tiempo con tres decimales
   		 }
   	 } catch (IOException e) {
   		 e.printStackTrace();
   	 }
    }
   	 
    	public BurbujaMejorado_Vs_Inserccion(ArrayList<double[]> dataBurbujaMejorado, ArrayList<double[]> dataInsercion) {
    	this.dataBurbujaMejorado = dataBurbujaMejorado;
    	this.dataInsercion = dataInsercion;
	}

     	/***********************************************************************************************************
* Nombre del Método: paint
* Propósito: Dibuja un gráfico en nativo  con datos de dos listas de puntos y etiquetas en un contexto de gráficos.
* Variables utilizadas:
*   - dataInsercion: ArrayList de double[] que contiene los datos de la burbuja.
*   - dataBurbujaMejorado: ArrayList de double[] que contiene los datos de la burbuja mejorada.
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
*   - Loss Arrays  dataBurbujaMejorado y dataInsercion deben contener datos válidos para graficar .
*   - El contexto de gráficos g debe estar inicializado y configurado para el dibujo.
* Postcondición:
*   - Dibuja un gráfico en forma nativo  que muestra datos de las listas dataBurbuja y dataBurbujaMejorado con ejes etiquetados y un título.
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
            	for (double[] punto : dataBurbujaMejorado) {
                	maxX = Math.max(maxX, punto[0]);
                	maxY = Math.max(maxY, punto[1]);
            	}
            	for (double[] punto : dataInsercion) {
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
           	 
           	 
            	g.drawRect(offsetX, offsetY, width, height);

          	 
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

            	// Dibuja los ejes después del rectángulo para que estén en la parte superior
            	g.drawLine(offsetX, offsetY + height, offsetX, offsetY);  // Eje Y
            	g.drawLine(offsetX, offsetY + height, offsetX + width, offsetY + height);  // Eje X

            	// Título en la parte superior del gráfico
            	g.setColor(Color.BLACK);
            	g.setFont(new Font("times new roman", Font.BOLD, 32));
            	g.drawString("Burbuja mejorado vs Inserción", offsetX + width / 2 - 200, offsetY - 60);
            	g.setFont(new Font("times new roman", Font.BOLD, 20));  // Vuelve a establecer el tamaño de fuente original
            	// Títulos
            	g.setColor(Color.BLACK);
            	g.drawString("Cantidad de datos", offsetX + width / 2 - 30, offsetY + height + 50);
           	// Rotar y dibujar "Tiempo (ms)"
           	// Condición para cambiar la etiqueta de tiempo
           	 
            	if (dataBurbujaMejorado.size() <= 5000 || dataInsercion.size() <= 5000) {
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
            	} else {
                	String texto = "Tiempo(Mili)";
                	int xTexto = offsetX - 100;  
                	int yTexto = offsetY + height / 2;  

                	AffineTransform transformacionOriginal = ((Graphics2D) g).getTransform();

                	AffineTransform rotacion = AffineTransform.getRotateInstance(-Math.PI / 2, xTexto, yTexto);
                	((Graphics2D) g).setTransform(rotacion);

                	g.drawString(texto, xTexto, yTexto);

                	((Graphics2D) g).setTransform(transformacionOriginal);
            	}
           	 

            	// Datos de Burbuja Mejorado
            	g.setColor(Color.GREEN);
            	BasicStroke solidStroke = new BasicStroke(2.0f);  // Grosor de la línea sólida
            	((Graphics2D) g).setStroke(solidStroke);
            	for (int i = 0; i < dataBurbujaMejorado.size() - 1; i++) {
                	int x1 = offsetX + (int) (dataBurbujaMejorado.get(i)[0] * (width / maxX));
                	int y1 = offsetY + height - (int) (dataBurbujaMejorado.get(i)[1] * (height / maxY));
                	int x2 = offsetX + (int) (dataBurbujaMejorado.get(i + 1)[0] * (width / maxX));
                	int y2 = offsetY + height - (int) (dataBurbujaMejorado.get(i + 1)[1] * (height / maxY));

                	GradientPaint gradient = new GradientPaint(x1, y1, Color.GREEN, x2, y2, Color.WHITE);
                	((Graphics2D) g).setPaint(gradient);
                	g.drawLine(x1, y1, x2, y2);

                 	g.setColor(Color.GREEN);
                 	g.drawOval(x2 - 2, y2 - 2, 4, 4);  // Puntos


            	}

            	// Datos de Inserción
            	g.setColor(Color.RED);
            	BasicStroke dashedStroke = new BasicStroke(2.0f);  // Línea punteada
            	((Graphics2D) g).setStroke(dashedStroke);
            	for (int i = 0; i < dataInsercion.size() - 1; i++) {
                	int x1 = offsetX + (int) (dataInsercion.get(i)[0] * (width / maxX));
                	int y1 = offsetY + height - (int) (dataInsercion.get(i)[1] * (height / maxY));
                	int x2 = offsetX + (int) (dataInsercion.get(i + 1)[0] * (width / maxX));
                	int y2 = offsetY + height - (int) (dataInsercion.get(i + 1)[1] * (height / maxY));

                	GradientPaint gradient = new GradientPaint(x1, y1, Color.RED, x2, y2, Color.WHITE);
                	((Graphics2D) g).setPaint(gradient);
                	g.drawLine(x1, y1, x2, y2);

                	g.setColor(Color.RED);
                	g.drawOval(x2 - 2, y2 - 2, 4, 4);  // Puntos
            	}

                          	 
            	g.setColor(Color.WHITE);

            	g.fillRect(400, 640, 330, 20);

            	g.setColor(Color.BLACK);

            	g.drawRect(400, 635, 305, 30);
 
            	g.setColor(Color.BLACK);

            	drawColoredLabel(g, "Burbuja Mejorado", 410, 656, Color.GREEN);
            	drawColoredLabel(g, "Inserción", 600, 656, Color.RED);

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
