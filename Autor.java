package main;

import java.io.*;
import java.util.ArrayList;

public class Autor implements Serializable {
	private String nombre_autor, nacionalidad;
	private int anio_nacimiento, id_autor;
	private static final long serialVersionUID = 1L;

	public Autor(String nombre_autor, String nacionalidad, int anio_nacimiento, int id_autor) {
		this.nombre_autor = nombre_autor;
		this.nacionalidad = nacionalidad;
		this.anio_nacimiento = anio_nacimiento;
		this.id_autor = id_autor;
	}

	public String getNombre_autor() {
		return nombre_autor;
	}

	public void setNombre_autor(String nombre_autor) {
		this.nombre_autor = nombre_autor;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public int getAnio_nacimiento() {
		return anio_nacimiento;
	}

	public void setAnio_nacimiento(int anio_nacimiento) {
		this.anio_nacimiento = anio_nacimiento;
	}

	public int getId_autor() {
		return id_autor;
	}

	public void setId_autor(int id_autor) {
		this.id_autor = id_autor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void agregarAutor(File archivo, ArrayList<Autor> autores) {
		try {
			// Cargar autores existentes si el archivo ya tiene datos
			if (archivo.exists() && archivo.length() > 0) {
				ArrayList<Autor> autoresExistente = obtenerAutoresDesdeArchivo(archivo);

				// Agregar el nuevo autor a la lista existente
				autoresExistente.add(this);

				// Escribir la lista completa de nuevo en el archivo
				guardarAutoresEnArchivo(autoresExistente, archivo);
			} else {
				// Si el archivo está vacío o no existe, simplemente escribir el libro
				guardarAutoresEnArchivo(autores, archivo);
			}

			System.out.println("Autor guardado en " + archivo);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("No se ha guardado: " + e.getMessage());
		}
	}

	// Método para mostrar los autores en la consola
	public void mostrarAutores(File archivo) {
		try {
			ArrayList<Autor> autores = obtenerAutoresDesdeArchivo(archivo);

			for (Autor autor : autores) {
				System.out.println("ID: " + autor.getId_autor());
				System.out.println("Nombre: " + autor.getNombre_autor());
				System.out.println("Nacionalidad: " + autor.getNacionalidad());
				System.out.println("Año de nacimiento: " + autor.getAnio_nacimiento());
				System.out.println("------------------------");
			}

		} catch (IOException | ClassNotFoundException e) {
			System.out.println("No se puede leer el archivo: " + e.getMessage());
		}
	}

	public void modificarAutor(File archivo, int idAutor, String nuevoNombre, String nuevaNacionalidad,
			int nuevoAnioNacimiento) {
		try {
			// Cargar autores existentes si el archivo ya tiene datos
			ArrayList<Autor> autores = obtenerAutoresDesdeArchivo(archivo);

			// Buscar el autor con el id proporcionado
			for (Autor autor : autores) {
				if (autor.getId_autor() == idAutor) {
					// Modificar los campos del autor
					autor.setNombre_autor(nuevoNombre);
					autor.setNacionalidad(nuevaNacionalidad);
					autor.setAnio_nacimiento(nuevoAnioNacimiento);

					// Escribir la lista completa de nuevo en el archivo
					guardarAutoresEnArchivo(autores, archivo);
					System.out.println("Autor modificado correctamente.");
					return;
				}
			}

			System.out.println("No se encontró ningún autor con el ID proporcionado.");
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Error al modificar el autor: " + e.getMessage());
		}
	}

	public void borrarAutor(File archivo, int idAutor) {
		try {
			// Cargar la lista actual de autores desde el archivo binario
			ArrayList<Autor> autores = obtenerAutoresDesdeArchivo(archivo);

			if (autores.size() > 0) {
				// Marcar la posición del autor que queremos eliminar como null
				for (int i = 0; i < autores.size(); i++) {
					if (autores.get(i).getId_autor() == idAutor) {
						autores.set(i, null);
						break;
					}
				}
				// Filtrar la lista para eliminar los objetos null
				autores.removeIf(autor -> autor == null);

				// Guardar la lista actualizada en el archivo binario
				guardarAutoresEnArchivo(autores, archivo);

				System.out.println("Autor con ID " + idAutor + " eliminado correctamente.");
			} else {
				System.out.println("No hay ningún autor registrado.");
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Método para guardar la nueva lista en el archivo autores.bin
	private void guardarAutoresEnArchivo(ArrayList<Autor> autores, File archivo) throws IOException {
		try (ObjectOutputStream objetoSalida = new ObjectOutputStream(new FileOutputStream(archivo))) {
			objetoSalida.writeObject(autores);
		}
	}

	@SuppressWarnings("unchecked")
	// Método auxiliar para leer y escribir la lista de autores en el archivo
	private ArrayList<Autor> obtenerAutoresDesdeArchivo(File archivo) throws IOException, ClassNotFoundException {
		if (archivo.exists() && archivo.length() > 0) {
			ObjectInputStream objetoEntrada = new ObjectInputStream(new FileInputStream(archivo));
			ArrayList<Autor> autores = (ArrayList<Autor>) objetoEntrada.readObject();
			objetoEntrada.close();
			return autores;
		} else {
			System.out.println("El archivo está vacío o no existe.");
			return new ArrayList<>(); // Devuelve una lista vacía si el archivo está vacío o no existe
		}
	}
}