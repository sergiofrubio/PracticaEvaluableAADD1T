package main;

// Importar otras clases necesarias
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Principal {
	private static Scanner sca = new Scanner(System.in);
	private static Libro libro = new Libro ("", "", 0,0);
	private static Autor autor = new Autor ("", "", 0,0);
	private static Prestamo prestamo = new Prestamo(0,0,0,0,0,false);
	public static File archivoLibros, archivoAutores, archivoPrestamos;
	private static ArrayList<Libro> libros = new ArrayList<>();
	private static ArrayList<Autor> autores = new ArrayList<>();

	public static void main(String[] args) {
		crearArchivos();
		boolean salir = false;
		while (!salir) {
			mostrarMenu();
			int opcion = sca.nextInt();
			switch (opcion) {
				case 1:
					// Gestionar libros
					gestionarLibros(archivoLibros);
					break;
				case 2:// Gestionar autores
					gestionarAutores(archivoAutores);
					break;
				case 3:
					// Gestionar préstamos
					gestionarPrestamos(archivoPrestamos);
					break;
				case 4:
					salir = true;
					break;
				default:
					System.out.println("Opción no válida. Por favor,intente de nuevo.");
			}
		}
	}

	public static void crearArchivos() {
		String ficheroLibros = "libros.bin";
		String ficheroAutores = "autores.bin";
		String ficheroPrestamos = "prestamos.txt";

		archivoLibros = new File(ficheroLibros);
		archivoAutores = new File(ficheroAutores);
		archivoPrestamos = new File(ficheroPrestamos);

		if (!archivoLibros.exists() && !archivoAutores.exists() && !archivoPrestamos.exists()) {
			try {
				archivoLibros.createNewFile();
				archivoAutores.createNewFile();
				archivoPrestamos.createNewFile();
			} catch (IOException e) {
				System.out.println("Los ficheros no se han creado");
			}
			System.out.println("Los ficheros se han creado correctamente");
		}
	}

	public static void mostrarMenu() {
		System.out.println("Bienvenido al Sistema de Gestión de Biblioteca");
		System.out.println("1. Gestionar Libros");
		System.out.println("2. Gestionar Autores");
		System.out.println("3. Gestionar Préstamos");
		System.out.println("4. Salir");
		System.out.print("Seleccione una opción: ");
	}

	@SuppressWarnings("unchecked")
	private static void gestionarLibros(File archivoLibros) {
		System.out.println("Elige una de las opciones");
		System.out.println("1. Añadir un Libro");
		System.out.println("2. Mostrar lista de Libros registrados");
		System.out.println("3. Modificar un Libro");
		System.out.println("4. Eliminar un Libro");
		System.out.println("5. Exportar libros");
		System.out.println("6. Importar libros");
		System.out.println("7. Salir al menú principal");
		int opcion = sca.nextInt();

		switch (opcion) {
			case 1:
				System.out.println("Introduzca el ID del libro:");
				int idLibro = sca.nextInt();
				sca.nextLine(); // Consumir nueva línea
				System.out.println("Introduzca el título del libro:");
				String titulo = sca.nextLine();
				System.out.println("Introduzca el género del libro:");
				String genero = sca.nextLine();
				System.out.println("Introduzca el año de publicación del libro:");
				int anioPublicacion = sca.nextInt();
				libro = new Libro(titulo, genero, anioPublicacion, idLibro);
				libros.add(libro);
				libro.agregarLibro(archivoLibros, libros);
				libros.clear();
				break;
			case 2:

				libro.mostrarLibros(archivoLibros);
				break;
			case 3:
				System.out.println("Introduzca el id del libro a modificar: ");
				int idLibroModificar = sca.nextInt();
				sca.nextLine(); // Consumir nueva línea
				System.out.println("Introduzca el nuevo título del libro: ");
				String nuevoTitulo = sca.nextLine();
				System.out.println("Introduzca el nuevo género: ");
				String nuevoGenero = sca.nextLine();
				System.out.println("Introduzca el nuevo año de publicación: ");
				int nuevoAnioPublicacion = sca.nextInt();
				libro.modificarLibro(archivoLibros, idLibroModificar, nuevoTitulo, nuevoGenero, nuevoAnioPublicacion);
				break;
			case 4:
				System.out.println("Introduzca el id del libro a eliminar: ");
				idLibro = sca.nextInt();
				libro.borrarLibro(archivoLibros, idLibro);
				break;
			case 5:
				try {
					// Cargar la lista de libros desde el archivo binario
					if (archivoLibros.exists()) {
						try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoLibros))) {
							libros = (ArrayList<Libro>) ois.readObject();
						}
					}

					// Crear un documento XML
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
					Document doc = docBuilder.newDocument();

					// Crear el elemento raíz
					Element rootElement = doc.createElement("libros");
					doc.appendChild(rootElement);

					// Crear elementos para cada libro
					for (Libro libro : libros) {
						Element libroElement = doc.createElement("libro");
						rootElement.appendChild(libroElement);

						Element idElement = doc.createElement("id");
						idElement.appendChild(doc.createTextNode(String.valueOf(libro.getId_libro())));
						libroElement.appendChild(idElement);

						Element tituloElement = doc.createElement("titulo");
						tituloElement.appendChild(doc.createTextNode(libro.getTitulo()));
						libroElement.appendChild(tituloElement);

						Element generoElement = doc.createElement("genero");
						generoElement.appendChild(doc.createTextNode(libro.getGenero()));
						libroElement.appendChild(generoElement);

						Element anioPublicacionElement = doc.createElement("anioPublicacion");
						anioPublicacionElement
								.appendChild(doc.createTextNode(String.valueOf(libro.getAnio_publicacion())));
						libroElement.appendChild(anioPublicacionElement);
					}

					// Escribir el contenido del documento XML en un archivo
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File("libros.xml"));
					transformer.transform(source, result);

					System.out.println("Libros exportados a XML correctamente.");
					libros.clear();
				} catch (IOException | ClassNotFoundException | ParserConfigurationException | TransformerException e) {
					e.printStackTrace();
				}
				break;
			case 6:
				try {
					// Parsear el documento XML
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(new File("libros.xml"));

					// Obtener el nodo raíz "libros"
					// Element rootElement = document.getDocumentElement();

					// Obtener la lista de nodos de libros
					NodeList nodeList = document.getElementsByTagName("libro");

					// Recorrer la lista de nodos y crear objetos Libro
					for (int i = 0; i < nodeList.getLength(); i++) {
						libro = new Libro("","",0,0);
						Node node = nodeList.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							libro.setId_libro(Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent()));
							libro.setTitulo(element.getElementsByTagName("titulo").item(0).getTextContent());
							libro.setGenero(element.getElementsByTagName("genero").item(0).getTextContent());
							libro.setAnio_publicacion(Integer.parseInt(element.getElementsByTagName("anioPublicacion").item(0).getTextContent()));
							libros.add(libro);
						}
					}

					// Escribir el ArrayList de objetos Libro en un archivo binario
					try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoLibros))) {
						oos.writeObject(libros);
						System.out.println("Libros importados correctamente.");
						libros.clear();

					}
				} catch (ParserConfigurationException | IOException e) {
					e.printStackTrace();
				} catch (org.xml.sax.SAXException e) {
					e.printStackTrace();
				}

				break;
			case 7:
				mostrarMenu();
				break;
			default:
				System.err.println("Elige una opción del menú");
		}
	}

	@SuppressWarnings("unchecked")
	private static void gestionarAutores(File archivoAutores) {
		System.out.println("Elige una de las opciones");
		System.out.println("1. Añadir un/a autor/a");
		System.out.println("2. Mostrar lista de Autores registrados");
		System.out.println("3. Modificar un/a autor/a");
		System.out.println("4. Eliminar un/a autor/a");
		System.out.println("5. Exportar autores");
		System.out.println("6. Importar autores");
		System.out.println("7. Salir al menú principal");
		int opcion = sca.nextInt();

		switch (opcion) {
			case 1:
				System.out.println("Introduzca el ID del autor:");
				int idAutor = sca.nextInt();
				sca.nextLine(); // Consumir nueva línea
				System.out.println("Introduzca el nombre del autor:");
				String nombre_autor = sca.nextLine();
				System.out.println("Introduzca nacionalidad del autor:");
				String nacionalidad = sca.nextLine();
				System.out.println("Introduzca el año de nacimiento del autor:");
				int anioNacimiento = sca.nextInt();
				autor = new Autor(nombre_autor, nacionalidad, anioNacimiento, idAutor);
				autores.add(autor);
				autor.agregarAutor(archivoAutores, autores);
				autores.clear();
				break;
			case 2:
				autor.mostrarAutores(archivoAutores);
				break;
			case 3:
				System.out.println("Introduzca el id del autor a modificar: ");
				int idAutorModificar = sca.nextInt();
				sca.nextLine(); // Consumir nueva línea
				System.out.println("Introduzca el nuevo nombre del autor: ");
				String nuevoNombre = sca.nextLine();
				System.out.println("Introduzca la nueva nacionalidad: ");
				String nuevaNacinalidad = sca.nextLine();
				System.out.println("Introduzca el nuevo año de nacimiento: ");
				int nuevoAnioNacimiento = sca.nextInt();
				autor.modificarAutor(archivoAutores, idAutorModificar, nuevoNombre, nuevaNacinalidad,
						nuevoAnioNacimiento);
				break;
			case 4:
				System.out.println("Introduzca el id del autor a eliminar: ");
				idAutor = sca.nextInt();
				autor.borrarAutor(archivoAutores, idAutor);
				break;
			case 5:
				try {
					// Cargar la lista de autores desde el archivo binario
					if (archivoAutores.exists()) {
						try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivoAutores))) {
							autores = (ArrayList<Autor>) ois.readObject();
						}
					}

					// Crear un documento XML
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
					Document doc = docBuilder.newDocument();

					// Crear el elemento raíz
					Element rootElement = doc.createElement("autores");
					doc.appendChild(rootElement);

					// Crear elementos para cada autor
					for (Autor autor : autores) {
						Element autorElement = doc.createElement("autor");
						rootElement.appendChild(autorElement);

						Element idElement = doc.createElement("id");
						idElement.appendChild(doc.createTextNode(String.valueOf(autor.getId_autor())));
						autorElement.appendChild(idElement);

						Element nombreElement = doc.createElement("nombre");
						nombreElement.appendChild(doc.createTextNode(autor.getNombre_autor()));
						autorElement.appendChild(nombreElement);

						Element nacionalidadElement = doc.createElement("nacionalidad");
						nacionalidadElement.appendChild(doc.createTextNode(autor.getNacionalidad()));
						autorElement.appendChild(nacionalidadElement);

						Element anioNacimientoElement = doc.createElement("anioNacimiento");
						anioNacimientoElement
								.appendChild(doc.createTextNode(String.valueOf(autor.getAnio_nacimiento())));
						autorElement.appendChild(anioNacimientoElement);
					}

					// Escribir el contenido del documento XML en un archivo
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File("autores.xml"));
					transformer.transform(source, result);

					System.out.println("Autores exportados a XML correctamente.");
					autores.clear();
				} catch (IOException | ClassNotFoundException | ParserConfigurationException | TransformerException e) {
					e.printStackTrace();
				}
				break;
			case 6:
				try {
					// Parsear el documento XML
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document document = builder.parse(new File("autores.xml"));

					// Obtener el nodo raíz "autores"
					// Element rootElement = document.getDocumentElement();

					// Obtener la lista de nodos de autores
					NodeList nodeList = document.getElementsByTagName("autor");

					// Recorrer la lista de nodos y crear objetos Autor
					for (int i = 0; i < nodeList.getLength(); i++) {
						autor = new Autor("", "", 0 ,0);
						Node node = nodeList.item(i);
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							autor.setId_autor(
									Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent()));
							autor.setNombre_autor(element.getElementsByTagName("nombre").item(0).getTextContent());
							autor.setNacionalidad(element.getElementsByTagName("nacionalidad").item(0).getTextContent());
							autor.setAnio_nacimiento(Integer.parseInt(
									element.getElementsByTagName("anioNacimiento").item(0).getTextContent()));
							autores.add(autor);
						}
					}

					// Escribir el ArrayList de objetos Autor en un archivo binario
					try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivoAutores))) {
						oos.writeObject(autores);
						System.out.println("Autores importados correctamente");
						autores.clear();
					}
				} catch (ParserConfigurationException | IOException e) {
					e.printStackTrace();
				} catch (org.xml.sax.SAXException e) {
					e.printStackTrace();
				}
				break;
			case 7:
				mostrarMenu();
				break;
			default:
				System.err.println("Elige una opción del menú");
		}
	}

	private static void gestionarPrestamos(File archivoPrestamos) {
		System.out.println("Elige una de las opciones");
		System.out.println("1. Hacer un préstamo");
		System.out.println("2. Mostrar lista de préstamos");
		System.out.println("3. Hacer devolución");
		System.out.println("4. Salir al menú principal");
		int opcion = sca.nextInt();

		switch (opcion) {
			case 1:
				System.out.println("Introduzca el id del pŕestamo: ");
				int id_prestamo = sca.nextInt();
				System.out.println("Introduzca el id del cliente: ");
				int id_cliente = sca.nextInt();
				System.out.println("Introduzca el id del libro: ");
				int id_libro = sca.nextInt();
				System.out.println("Introduzca la fecha de préstamo: ");
				int fecha_prestamo = sca.nextInt();
				System.out.println("Introduzca la fecha de devolución prevista: ");
				int fecha_devolucion = sca.nextInt();
				boolean esDevuelto = false;

				if (libro.encontrarLibroParaPrestamo(archivoLibros, id_libro)== true){
					prestamo = new Prestamo(id_prestamo, id_cliente, id_libro, fecha_prestamo, fecha_devolucion,
						esDevuelto);
				prestamo.hacerPrestamo(archivoPrestamos);
				}else{
					System.out.println("No se ha encontrado el libro con ese ID. Inténtelo de nuevo");
				}

				break;
			case 2:
				prestamo.mostrarPrestamos(archivoPrestamos);
				break;
			case 3:
				System.out.println("Introduzca el id del prestamo: ");
				id_prestamo = sca.nextInt();
				esDevuelto = true;
				prestamo.hacerDevolucion(id_prestamo, archivoPrestamos);
				break;
			case 4:
				mostrarMenu();
				break;
			default:
				System.err.println("Elige una opción del menú");
		}
	}
}