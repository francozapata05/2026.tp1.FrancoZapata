package com.bibliotech;

import com.bibliotech.exception.DniDuplicadoException;
import com.bibliotech.exception.EmailInvalidoException;
import com.bibliotech.exception.LibroNoDisponibleException;
import com.bibliotech.exception.LibroNoEncontradoException;
import com.bibliotech.exception.LimitePrestamosException;
import com.bibliotech.exception.PrestamoNoEncontradoException;
import com.bibliotech.exception.SocioSancionadoException;
import com.bibliotech.model.Categoria;
import com.bibliotech.model.Docente;
import com.bibliotech.model.Estudiante;
import com.bibliotech.model.Libro;
import com.bibliotech.model.Prestamo;
import com.bibliotech.model.Sancion;
import com.bibliotech.model.Socio;
import com.bibliotech.model.Transaccion;
import com.bibliotech.repository.LibroRepositoryImpl;
import com.bibliotech.repository.PrestamoRepositoryImpl;
import com.bibliotech.repository.SancionRepositoryImpl;
import com.bibliotech.repository.SocioRepositoryImpl;
import com.bibliotech.repository.TransaccionRepositoryImpl;
import com.bibliotech.service.LibroService;
import com.bibliotech.service.LibroServiceImpl;
import com.bibliotech.service.PrestamoService;
import com.bibliotech.service.PrestamoServiceImpl;
import com.bibliotech.service.SocioService;
import com.bibliotech.service.SocioServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        // --- Inicialización de repositorios y servicios ---
        SocioRepositoryImpl socioRepo = new SocioRepositoryImpl();
        LibroRepositoryImpl libroRepo = new LibroRepositoryImpl();
        PrestamoRepositoryImpl prestamoRepo = new PrestamoRepositoryImpl();
        TransaccionRepositoryImpl transaccionRepo = new TransaccionRepositoryImpl();
        SancionRepositoryImpl sancionRepo = new SancionRepositoryImpl();

        SocioService socioService = new SocioServiceImpl(socioRepo);
        LibroService libroService = new LibroServiceImpl(libroRepo);
        PrestamoService prestamoService = new PrestamoServiceImpl(prestamoRepo, transaccionRepo, sancionRepo);

        System.out.println("=== BiblioTech - Sistema de Gestión de Biblioteca ===\n");

        // --- 1. Registro de socios ---
        System.out.println("--- Registro de socios ---");
        Socio estudiante = new Estudiante(1, "Ana", "García", "12345678", "ana@gmail.com");
        Socio docente = new Docente(2, "Carlos", "López", "87654321", "carlos@universidad.edu.ar");

        try {
            socioService.registrar(estudiante);
            System.out.println("Socio registrado: " + estudiante.getNombre() + " " + estudiante.getApellido() + " (Estudiante, límite: " + estudiante.getLimitePrestamos() + " préstamos)");

            socioService.registrar(docente);
            System.out.println("Socio registrado: " + docente.getNombre() + " " + docente.getApellido() + " (Docente, límite: " + docente.getLimitePrestamos() + " préstamos)");
        } catch (DniDuplicadoException | EmailInvalidoException e) {
            System.out.println("Error al registrar socio: " + e.getMessage());
        }

        // Intento de registro con DNI duplicado
        try {
            Socio duplicado = new Estudiante(3, "Otra", "Persona", "12345678", "otra@gmail.com");
            socioService.registrar(duplicado);
        } catch (DniDuplicadoException e) {
            System.out.println("Error esperado - DNI duplicado: " + e.getMessage());
        } catch (EmailInvalidoException e) {
            System.out.println("Error al registrar socio: " + e.getMessage());
        }

        // Intento de registro con email inválido
        try {
            Socio emailMalo = new Estudiante(4, "Pedro", "Ruiz", "11111111", "emailsinvalido");
            socioService.registrar(emailMalo);
        } catch (EmailInvalidoException e) {
            System.out.println("Error esperado - Email inválido: " + e.getMessage());
        } catch (DniDuplicadoException e) {
            System.out.println("Error al registrar socio: " + e.getMessage());
        }

        // --- 2. Registro de libros ---
        System.out.println("\n--- Registro de libros ---");
        Libro libro1 = new Libro("978-0-06-112008-4", "Cien años de soledad", "Gabriel García Márquez", 1967, Categoria.FICCION);
        Libro libro2 = new Libro("978-0-14-028329-7", "Sapiens", "Yuval Noah Harari", 2011, Categoria.HISTORIA);
        Libro libro3 = new Libro("978-0-13-468599-1", "Clean Code", "Robert C. Martin", 2008, Categoria.TECNOLOGIA);

        libroRepo.guardar(libro1);
        libroRepo.guardar(libro2);
        libroRepo.guardar(libro3);
        System.out.println("Libros registrados: " + libroRepo.buscarTodos().size());

        // --- 3. Búsqueda de libros ---
        System.out.println("\n--- Búsqueda de libros ---");
        try {
            List<Libro> porAutor = libroService.buscarPorAutor("Harari");
            System.out.println("Búsqueda por autor 'Harari': " + porAutor.getFirst().titulo());

            List<Libro> porCategoria = libroService.buscarPorCategoria(Categoria.TECNOLOGIA);
            System.out.println("Búsqueda por categoría TECNOLOGÍA: " + porCategoria.getFirst().titulo());

            List<Libro> porTitulo = libroService.buscarPorTitulo("Clean Code");
            System.out.println("Búsqueda por título 'Clean Code': " + porTitulo.getFirst().titulo());
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error al buscar libro: " + e.getMessage());
        }

        // Búsqueda de libro inexistente
        try {
            libroService.buscarPorAutor("Autor Inexistente");
        } catch (LibroNoEncontradoException e) {
            System.out.println("Error esperado - " + e.getMessage());
        }

        // --- 4. Préstamos ---
        System.out.println("\n--- Préstamos ---");
        Prestamo p1 = null;
        try {
            p1 = prestamoService.realizarPrestamo(libro1, estudiante);
            System.out.println("Préstamo registrado: '" + p1.libro().titulo() + "' para " + p1.socio().getNombre() + " (vence: " + p1.fechaDevolucionEsperada() + ")");

            Prestamo p2 = prestamoService.realizarPrestamo(libro2, estudiante);
            System.out.println("Préstamo registrado: '" + p2.libro().titulo() + "' para " + p2.socio().getNombre() + " (vence: " + p2.fechaDevolucionEsperada() + ")");

            Prestamo p3 = prestamoService.realizarPrestamo(libro3, docente);
            System.out.println("Préstamo registrado: '" + p3.libro().titulo() + "' para " + p3.socio().getNombre() + " (vence: " + p3.fechaDevolucionEsperada() + ")");
        } catch (LibroNoDisponibleException | LimitePrestamosException | SocioSancionadoException e) {
            System.out.println("Error al realizar préstamo: " + e.getMessage());
        }

        // Intento de préstamo de libro ya prestado
        System.out.println("\n--- Préstamo de libro no disponible ---");
        try {
            prestamoService.realizarPrestamo(libro1, docente);
        } catch (LibroNoDisponibleException e) {
            System.out.println("Error esperado - Libro no disponible: " + e.getMessage());
        } catch (LimitePrestamosException | SocioSancionadoException e) {
            System.out.println("Error al realizar préstamo: " + e.getMessage());
        }

        // Intento de superar límite de préstamos (estudiante tiene límite 3)
        System.out.println("\n--- Límite de préstamos ---");
        try {
            Libro libro4 = new Libro("978-0-74-327356-5", "El nombre del viento", "Patrick Rothfuss", 2007, Categoria.FICCION);
            libroRepo.guardar(libro4);
            Prestamo p4 = prestamoService.realizarPrestamo(libro4, estudiante);
            System.out.println("Préstamo registrado: '" + p4.libro().titulo() + "' para " + p4.socio().getNombre() + " (vence: " + p4.fechaDevolucionEsperada() + ")");

            Libro libro5 = new Libro("978-0-14-243723-1", "1984", "George Orwell", 1949, Categoria.FICCION);
            libroRepo.guardar(libro5);
            prestamoService.realizarPrestamo(libro5, estudiante);
        } catch (LimitePrestamosException e) {
            System.out.println("Error esperado - Límite alcanzado: " + e.getMessage());
        } catch (LibroNoDisponibleException | SocioSancionadoException e) {
            System.out.println("Error al realizar préstamo: " + e.getMessage());
        }

        // --- 5. Devolución ---
        System.out.println("\n--- Devolución ---");
        try {
            long diasRetraso = prestamoService.registrarDevolucion(p1.id());
            if (diasRetraso > 0) {
                System.out.println("Devolución registrada. Días de retraso: " + diasRetraso);
            } else {
                System.out.println("Devolución registrada en término.");
            }
        } catch (PrestamoNoEncontradoException e) {
            System.out.println("Error al registrar devolución: " + e.getMessage());
        }

        // Intento de devolución de préstamo inexistente
        try {
            prestamoService.registrarDevolucion(999);
        } catch (PrestamoNoEncontradoException e) {
            System.out.println("Error esperado - Préstamo no encontrado: " + e.getMessage());
        }

        // --- 6. Sistema de sanciones (3 strikes → mes de suspensión) ---
        System.out.println("\n--- Sistema de sanciones por devolución tardía ---");

        Random rnd = new Random();

        // Strike 1: 5 días de retraso → sanción de 5 días
        Libro libroS1 = new Libro("978-0-06-093546-9", "El Gran Gatsby", "F. Scott Fitzgerald", 1925, Categoria.FICCION);
        libroRepo.guardar(libroS1);
        Prestamo ps1 = new Prestamo(System.currentTimeMillis() * 10000 + rnd.nextInt(10000), libroS1, docente,
                LocalDate.now().minusDays(20), LocalDate.now().minusDays(5), Optional.empty());
        prestamoRepo.guardar(ps1);

        // Strike 2: 3 días de retraso → sanción de 3 días
        Libro libroS2 = new Libro("978-0-14-243-7001-2", "Don Quijote", "Miguel de Cervantes", 1605, Categoria.FICCION);
        libroRepo.guardar(libroS2);
        Prestamo ps2 = new Prestamo(System.currentTimeMillis() * 10000 + rnd.nextInt(10000), libroS2, docente,
                LocalDate.now().minusDays(15), LocalDate.now().minusDays(3), Optional.empty());
        prestamoRepo.guardar(ps2);

        // Strike 3: 7 días de retraso → sanción de 7 días
        Libro libroS3 = new Libro("978-0-14-310-4381-3", "Hamlet", "William Shakespeare", 1603, Categoria.FICCION);
        libroRepo.guardar(libroS3);
        Prestamo ps3 = new Prestamo(System.currentTimeMillis() * 10000 + rnd.nextInt(10000), libroS3, docente,
                LocalDate.now().minusDays(25), LocalDate.now().minusDays(7), Optional.empty());
        prestamoRepo.guardar(ps3);

        // Strike 4: 2 días de retraso → sanción de 30 días (mes de suspensión)
        Libro libroS4 = new Libro("978-0-14-044-9564-4", "La Odisea", "Homero", -800, Categoria.FICCION);
        libroRepo.guardar(libroS4);
        Prestamo ps4 = new Prestamo(System.currentTimeMillis() * 10000 + rnd.nextInt(10000), libroS4, docente,
                LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), Optional.empty());
        prestamoRepo.guardar(ps4);

        try {
            long r1 = prestamoService.registrarDevolucion(ps1.id());
            System.out.println("Strike 1: " + r1 + " días de retraso → sancionado " + r1 + " días.");
            long r2 = prestamoService.registrarDevolucion(ps2.id());
            System.out.println("Strike 2: " + r2 + " días de retraso → sancionado " + r2 + " días.");
            long r3 = prestamoService.registrarDevolucion(ps3.id());
            System.out.println("Strike 3: " + r3 + " días de retraso → sancionado " + r3 + " días.");
            long r4 = prestamoService.registrarDevolucion(ps4.id());
            System.out.println("Strike 4: " + r4 + " días de retraso → 3 strikes previos, sancionado 30 días.");
        } catch (PrestamoNoEncontradoException e) {
            System.out.println("Error al registrar devolución: " + e.getMessage());
        }

        // --- 7. Sanciones por devolución tardía ---
        System.out.println("\n--- Sanción aplicada a Carlos ---");
        List<Sancion> sanciones = prestamoService.obtenerSanciones(docente.getId());
        for (Sancion s : sanciones) {
            System.out.println("  Sancionado desde " + s.fechaInicio() + " hasta " + s.fechaFin());
        }

        // Intento de préstamo con sanción activa
        System.out.println("\n--- Préstamo con sanción activa ---");
        try {
            Libro libroExtra = new Libro("978-0-7432-7356-5", "Harry Potter", "J.K. Rowling", 1997, Categoria.FICCION);
            libroRepo.guardar(libroExtra);
            prestamoService.realizarPrestamo(libroExtra, docente);
        } catch (SocioSancionadoException e) {
            System.out.println("Error esperado - " + e.getMessage());
        } catch (LibroNoDisponibleException | LimitePrestamosException e) {
            System.out.println("Error al realizar préstamo: " + e.getMessage());
        }

        // --- 8. Listado de todos los préstamos ---
        System.out.println("\n--- Todos los préstamos ---");
        List<Prestamo> todos = prestamoService.obtenerTodosLosPrestamos();
        for (Prestamo p : todos) {
            String estado = p.fechaDevolucionReal().isPresent() ? "Devuelto" : "Activo";
            System.out.println("  [" + estado + "] '" + p.libro().titulo() + "' - " + p.socio().getNombre() + " " + p.socio().getApellido());
        }

        // --- 9. Historial de transacciones de Ana ---
        System.out.println("\n--- Historial de transacciones de Ana ---");
        List<Transaccion> historial = prestamoService.obtenerHistorial(estudiante.getId());
        for (Transaccion t : historial) {
            System.out.println("  [" + t.tipo() + "] " + t.prestamo().libro().titulo() + " - " + t.fecha());
        }

        System.out.println("\n=== Fin de la demo ===");
    }
}
