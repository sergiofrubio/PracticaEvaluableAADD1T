package main;

import java.io.*;

public class Prestamo implements Serializable {

    // Atributos de la clase
    private int id_prestamo, id_libro, id_usuario, fecha_prestamo, fecha_devolucion;
    private boolean esDevuelto;

    public Prestamo(int id_prestamo, int id_libro, int id_usuario, int fecha_prestamo, int fecha_devolucion, boolean esDevuelto) {
        this.id_prestamo = id_prestamo;
        this.id_libro = id_libro;
        this.id_usuario = id_usuario;
        this.fecha_prestamo = fecha_prestamo;
        this.fecha_devolucion = fecha_devolucion;
        this.esDevuelto = esDevuelto;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public int getId_libro() {
        return id_libro;
    }

    public void setId_libro(int id_libro) {
        this.id_libro = id_libro;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getFecha_prestamo() {
        return fecha_prestamo;
    }

    public void setFecha_prestamo(int fecha_prestamo) {
        this.fecha_prestamo = fecha_prestamo;
    }

    public int getFecha_devolucion() {
        return fecha_devolucion;
    }

    public void setFecha_devolucion(int fecha_devolucion) {
        this.fecha_devolucion = fecha_devolucion;
    }

    public boolean isEsDevuelto() {
        return esDevuelto;
    }

    public void setEsDevuelto(boolean esDevuelto) {
        this.esDevuelto = esDevuelto;
    }

    public void hacerPrestamo(File archivo) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true));
            bw.write(this.toString());
            bw.newLine();
            bw.close();
            System.out.println("Préstamo guardado en " + archivo);
        } catch (IOException e) {
            System.out.println("Error al guardar el préstamo: " + e.getMessage());
        }
    }

    public void mostrarPrestamos(File archivoPrestamos) {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(archivoPrestamos));
            String linea = buffer.readLine();
            if (linea == null) {
                System.out.println("El archivo está vacío o no existe.");
            } else {
                while (linea != null) {
                    System.out.println(linea);
                    linea = buffer.readLine();
                }
            }
            buffer.close();

        } catch (IOException exception) {
            System.out.println("Error al acceder al fichero");
        }
    }

    public void hacerDevolucion(int idPrestamo, File archivoPrestamos) {
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(archivoPrestamos));
            String linea;
            StringBuilder contenido = new StringBuilder();

            while ((linea = buffer.readLine()) != null) {
                String[] partes = linea.split(",");
                int idActual = Integer.parseInt(partes[0]);

                if (idActual == idPrestamo) {
                    // Marcar como devuelto cambiando el valor del boolean esDevuelto a true
                    partes[5] = "true";
                }

                contenido.append(String.join(",", partes)).append("\n");
            }

            buffer.close();

            // Escribir el contenido actualizado en el archivo de préstamos
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivoPrestamos));
            writer.write(contenido.toString());
            writer.close();

            System.out.println("Devolución realizada correctamente para el préstamo con ID " + idPrestamo);

        } catch (IOException e) {
            System.out.println("Error al realizar la devolución: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return id_prestamo + "," + id_libro + "," + id_usuario + "," + fecha_prestamo + "," + fecha_devolucion + "," + esDevuelto;
    }
}