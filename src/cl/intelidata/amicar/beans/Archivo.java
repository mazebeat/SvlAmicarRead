package cl.intelidata.amicar.beans;

import org.jam.superutils.FastFileTextReader;

/**
 * Archivo, facilita la lectura y escritura de los archivos, dentro de sus
 * metodos utilizando sobrecarga de metodos podemos encontrar funcionalidades
 * similares que reciben distintos parametros, dandonos versatilidad al momento
 * de utilizarla
 * 
 * @author Daniel C�sped Villanueva
 * 
 */
public class Archivo {
	private String strPathEntrada;
	private String strPathSalida;
	// Extension archivo modificado a csv
	public final String CSV = "_pro.csv";

	public Archivo(String strDirectorioEntrada, String strDirectorioSalida) {
		this.printSeparador();
		setPath(strDirectorioEntrada.trim(), strDirectorioSalida.trim());
	}

	/**
	 * Lee un archivo en la ruta dentro de la ruta de origen, devolviendo una
	 * lista con los datos
	 * 
	 * @param strArchivo
	 *            nombre del archivo a leer
	 * @param strCabecera
	 *            cabecera o primera linea a insertar
	 * @return Lista con los datos del archivo <B>List<String></b>
	 */

	public java.util.List<String> leer(String strArchivo, String strCabecera) {
		// Atributos y variables locales
		String strLinea = "";
		java.util.List<String> lista = new java.util.ArrayList<String>();
		try {
			/********************************************************************/
			System.out.println("leyendo del archivo...");
			// Crea el objeto que leera el archivo
			FastFileTextReader fastFileTextReader = new FastFileTextReader(this.getStrPathEntrada() + strArchivo.trim(), FastFileTextReader.UTF_8, 1024 * 40);
			/********************************************************************/
			// Comienza la lectura del archivo linea por linea
			while ((strLinea = fastFileTextReader.readLine()) != null) {
				System.out.println("leyendo la linea: " + strLinea); //
				// Eliminar
				// insertar lectura de los archivos
				if (!strLinea.startsWith(strCabecera)) {
					lista.add(strLinea);
				}

			}
			/********************************************************************/
			// Intenta el cierre del objeto que lee el archivo
			try {
				fastFileTextReader.close();
			} catch (Exception e) {
				this.error(e, "Problemas al cerrar el archivo");
			}
			// Confirma la lectura exitosa
			System.out.println("Proceso del archivo teminado con exito");
		} catch (java.io.FileNotFoundException fe) {
			System.out.println("El archivo " + strArchivo + " no fue encontrado");
			System.exit(1);
		} catch (Exception e) {
			this.error(e, "Problemas al leer el archivo");
		}
		return lista;
	}

	/************************************************************************************************************/

	public void guardarLista(java.util.List<String> lista, String strRuta, String strExtension) {
		try {
			this.guardarLista(lista, strRuta, null, strExtension);
		} catch (Exception e) {
			this.error(e, "Problemas al guardar el archivo");
		}
	}

	/************************************************************************************************************/

	public void guardarLista(java.util.List<String> lista, String strRuta, String strCabecera, String strExtension) {
		this.printSeparador();
		// Atributos y variables locales
		java.io.BufferedWriter bufferedWriter = null;
		java.io.BufferedWriter bufferedWriterArchivo = null;
		/********************************************************************/
		// Comienza la escritura del archivo
		String archivo = System.currentTimeMillis() + "." + strExtension;
		String archivoFinal = strRuta.trim() + archivo;
		strRuta = this.getStrPathSalida() + strRuta.trim();

		String strArchivo = strRuta.trim() + archivo;
		try {
			bufferedWriter = new java.io.BufferedWriter(new java.io.FileWriter(strArchivo));
			System.out.println("Creando el archivo " + strArchivo);
			if (strCabecera != null) {
				// Inserta la cabecera del archivo
				System.out.println("Insertando Cabecera...");
				bufferedWriter.append(strCabecera);
				bufferedWriter.newLine();
			}
			// Lee la lista y escribe el archivo
			System.out.println("Escribiendo los datos...");
			for (String strDatos : lista) {
				bufferedWriter.append(strDatos);
				bufferedWriter.newLine();
			}
			bufferedWriterArchivo = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(strRuta + "Generado.txt"), "UTF-8"));
			bufferedWriterArchivo.append(archivoFinal);
			bufferedWriterArchivo.newLine();
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			this.error(e, "");
		} finally {
			/********************************************************************/
			// Cierra el buffer de escritura
			try {
				bufferedWriter.close();
				bufferedWriterArchivo.close();
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				this.error(e, "");
			}
			/********************************************************************/
		}
		/********************************************************************/
	}

	/************************************************************************************************************/

	public void guardarListaCSV(java.util.List<String> lista, String strNombreArchivo, String strCabecera) {
		this.printSeparador();
		// Atributos y variables locales
		java.io.BufferedWriter bufferedWriter = null;
		/********************************************************************/
		// Comienza la escritura del archivo
		strNombreArchivo = this.getStrPathSalida() + strNombreArchivo.trim();
		String strArchivo = strNombreArchivo.trim() + System.currentTimeMillis() + this.CSV;
		try {
			bufferedWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(strArchivo), "UTF-8"));
			System.out.println("Creando el archivo " + strArchivo);
			if (strCabecera != null) {
				// Inserta la cabecera del archivo
				System.out.println("Insertando Cabecera...");
				bufferedWriter.append(strCabecera);
				bufferedWriter.newLine();
			}
			// Lee la lista y escribe el archivo
			System.out.println("Escribiendo los datos...");
			for (String strDatos : lista) {
				bufferedWriter.append(strDatos);
				bufferedWriter.newLine();
			}

		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			this.error(e, "");
		} finally {
			/********************************************************************/
			// Cierra el buffer de escritura
			try {
				bufferedWriter.close();
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				this.error(e, "");
			}
			/********************************************************************/
		}
		/********************************************************************/
	}

	/************************************************************************************************************/

	public void guardarListaCSV(java.util.List<String> lista, String strNombreArchivo) {
		try {
			this.guardarListaCSV(lista, strNombreArchivo, null);
		} catch (Exception e) {
			this.error(e, "Problemas la guardar el archivo");
		}
	}

	/************************************************************************************************************/
	/**
	 * Setea las rutas y las carpetas donde se moveran los archivos
	 * 
	 * @param strDirectorioEntrada
	 *            Ruta donde se alojan los archivos
	 */
	private void setPath(String strDirectorioEntrada, String strDirectorioSalida) {

		System.out.println("Seteando los directorios de entrada y salida...");
		setStrPathEntrada(strDirectorioEntrada);
		setStrPathSalida(strDirectorioSalida);
		System.out.println("Directorios verificados y listo para la indexacion...");
	}

	// Impresor de separador
	private void printSeparador() {
		System.out.println("\n**********************************************************************\n");
	}

	/************************************************************************************************************/

	/******************************************* Getters y Setters **********************************************/
	private void error(Exception e, String strMensaje) {
		e.printStackTrace();
		System.out.println(strMensaje);
		System.exit(1);
	}

	public String getStrPathEntrada() {
		return strPathEntrada;
	}

	public void setStrPathEntrada(String strPathEntrada) {
		this.strPathEntrada = strPathEntrada;
	}

	public String getStrPathSalida() {
		return strPathSalida;
	}

	public void setStrPathSalida(String strPathSalida) {
		this.strPathSalida = strPathSalida;
	}

}
