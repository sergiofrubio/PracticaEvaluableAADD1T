package main;

import java.io.*;
import java.util.ArrayList;

public class Libro implements Serializable {
	private String titulo, genero;
	private int anio_publicacion, id_libro;
	private static final long serialVersionUID = 1L;

	public Libro(String titulo, String genero, int anio_publicacion, int id_libro) {
		this.titulo = titulo;
		this.genero = genero;
		this.anio_publicacion = anio_publicacion;
		this.id_libro = id_libro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public int getAnio_publicacion() {
		return anio_publicacion;
	}

	public void setAnio_publicacion(int anio_publicacion) {
		this.anio_publicacion = anio_publicacion;
	}

	public int getId_libro() {
		return id_libro;
	}

	public void setId_libro(int id_libro) {
		this.id_libro = id_libro;
	}

	// @SuppressWarnings("unchecked")
	public void agregarLibro(File archivo, ArrayList<Libro> libros) {
		try {
			// Cargar libros existentes si el archivo ya tiene datos
			if (archivo.exists() && archivo.length() > 0) {
				ArrayList<Libro> librosExistente = obtenerLibrosDesdeArchivo(archivo);

				// Agregar el nuevo libro a la lista existente
				librosExistente.add(this);

				// Escribir la lista completa de nuevo en el archivo
				guardarLibrosEnArchivo(librosExistente, archivo);
			} else {
				// Si el archivo está vacío o no existe, simplemente escribir el libro
				guardarLibrosEnArchivo(libros, archivo);
			}

			System.out.println("Libro guardado en " + archivo);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("No se ha guardado: " + e.getMessage());
		}
	}

	// Método para verificar si existe el id del libro a prestar
	public boolean encontrarLibroParaPrestamo(File archivo, int idBuscado){
		boolean resultado = false;
		try {
			// Obtener la lista de libros desde el archivo
			ArrayList<Libro> libros = obtenerLibrosDesdeArchivo(archivo);

			for (Libro libro : libros) {
				if(libro.getId_libro() == idBuscado){
					resultado = true;
				}else{
					resultado = false;
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("No se puede leer el archivo: " + e.getMessage());
		}
		return resultado;

	}

	// Método para mostrar los libros en la consola
	public void mostrarLibros(File archivo) {
		try {
			ArrayList<Libro> libros = obtenerLibrosDesdeArchivo(archivo);

			for (Libro libro : libros) {
				System.out.println("ID: " + libro.getId_libro());
				System.out.println("Título: " + libro.getTitulo());
				System.out.println("Género: " + libro.getGenero());
				System.out.println("Año de publicacion: " + libro.getAnio_publicacion());
				System.out.println("------------------------");
			}

		} catch (IOException | ClassNotFoundException e) {
			System.out.println("No se puede leer el archivo: " + e.getMessage());
		}
	}

	public void modificarLibro(File archivo, int idLibro, String nuevoTitulo, String nuevoGenero,
			int nuevoAnioPublicacion) {
		try {
			// Cargar libros existentes si el archivo ya tiene datos
			ArrayList<Libro> libros = obtenerLibrosDesdeArchivo(archivo);

			// Buscar el libro con el id proporcionado
			for (Libro libro : libros) {
				if (libro.getId_libro() == idLibro) {
					// Modificar los campos del libro
					libro.setTitulo(nuevoTitulo);
					libro.setGenero(nuevoGenero);
					libro.setAnio_publicacion(nuevoAnioPublicacion);

					// Escribir la lista completa de nuevo en el archivo
					guardarLibrosEnArchivo(libros, archivo);
					System.out.println("Libro modificado correctamente.");
					return;
				}
			}

			System.out.println("No se encontró ningún libro con el ID proporcionado.");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error al modificar el libro: " + e.getMessage());
		}
	}

	public void borrarLibro(File archivo, int idLibro) {
        try {
            // Cargar la lista actual de libros desde el archivo binario
            ArrayList<Libro> libros = obtenerLibrosDesdeArchivo(archivo);

			if(libros.size()>0){
				    // Marcar la posición del libro que queremos eliminar como null
            for (int i = 0; i < libros.size(); i++) {
                if (libros.get(i).getId_libro() == idLibro) {
                    libros.set(i, null);
                    break;
                }
            }

            // Filtrar la lista para eliminar los objetos null
            libros.removeIf(libro -> libro == null);

            // Guardar la lista actualizada en el archivo binario
            guardarLibrosEnArchivo(libros, archivo);

            System.out.println("Libro con ID " + idLibro + " eliminado correctamente.");
			}
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Método para guardar la nueva lista en el archivo libros.bin
    private void guardarLibrosEnArchivo(ArrayList<Libro> libros, File archivo) throws IOException {
		try (ObjectOutputStream objetoSalida = new ObjectOutputStream(new FileOutputStream(archivo))) {
            objetoSalida.writeObject(libros);
		}
	}

	@SuppressWarnings("unchecked")
	// Método auxiliar para leer y escribir la lista de libros en el archivo libros.bin
	public ArrayList<Libro> obtenerLibrosDesdeArchivo(File archivo) throws IOException, ClassNotFoundException {
		if (archivo.exists() && archivo.length() > 0) {
			ObjectInputStream objetoEntrada = new ObjectInputStream(new FileInputStream(archivo));
			ArrayList<Libro> libros = (ArrayList<Libro>) objetoEntrada.readObject();
			objetoEntrada.close();
			return libros;
		} else {
			System.out.println("El archivo está vacío o no existe.");
			return new ArrayList<>(); // Devuelve una lista vacía si el archivo está vacío o no existe
		}
	}
}