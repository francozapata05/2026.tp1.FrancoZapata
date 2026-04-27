package com.bibliotech;

import com.bibliotech.exception.*;
import com.bibliotech.model.*;
import com.bibliotech.repository.*;
import com.bibliotech.service.*;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static SocioRepositoryJsonImpl socioRepo;
    private static LibroRepositoryJsonImpl libroRepo;
    private static PrestamoRepositoryJsonImpl prestamoRepo;
    private static SocioService socioService;
    private static LibroService libroService;
    private static PrestamoService prestamoService;

    public static void main(String[] args) {
        socioRepo = new SocioRepositoryJsonImpl();
        libroRepo = new LibroRepositoryJsonImpl();
        prestamoRepo = new PrestamoRepositoryJsonImpl();
        TransaccionRepositoryJsonImpl transaccionRepo = new TransaccionRepositoryJsonImpl();
        SancionRepositoryJsonImpl sancionRepo = new SancionRepositoryJsonImpl();

        socioService = new SocioServiceImpl(socioRepo);
        libroService = new LibroServiceImpl(libroRepo);
        prestamoService = new PrestamoServiceImpl(prestamoRepo, transaccionRepo, sancionRepo);

        System.out.println("=== BiblioTech ===");

        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\n--- Menu principal ---");
            System.out.println("1. Socios");
            System.out.println("2. Recursos (Libros y E-books)");
            System.out.println("3. Prestamos");
            System.out.println("4. Consultas");
            System.out.println("0. Salir");
            System.out.print("> ");

            switch (leerLinea()) {
                case "1" -> menuSocios();
                case "2" -> menuRecursos();
                case "3" -> menuPrestamos();
                case "4" -> menuConsultas();
                case "0" -> corriendo = false;
                default  -> System.out.println("Opcion invalida.");
            }
        }

        System.out.println("Hasta luego.");
    }

    // -------------------------------------------------------------------------
    // Socios
    // -------------------------------------------------------------------------

    private static void menuSocios() {
        System.out.println("\n-- Socios --");
        System.out.println("1. Registrar socio");
        System.out.println("2. Buscar por DNI");
        System.out.println("3. Listar todos");
        System.out.println("0. Volver");
        System.out.print("> ");

        switch (leerLinea()) {
            case "1" -> registrarSocio();
            case "2" -> buscarSocioPorDni();
            case "3" -> listarSocios();
            case "0" -> {}
            default  -> System.out.println("Opcion invalida.");
        }
    }

    private static void registrarSocio() {
        System.out.println("Tipo (1=Estudiante, 2=Docente):");
        System.out.print("> ");
        String tipo = leerLinea();
        if (!tipo.equals("1") && !tipo.equals("2")) {
            System.out.println("Tipo invalido.");
            return;
        }

        System.out.print("Nombre: ");
        String nombre = leerLinea();
        System.out.print("Apellido: ");
        String apellido = leerLinea();
        System.out.print("DNI: ");
        String dni = leerLinea();
        System.out.print("Email: ");
        String email = leerLinea();

        int id = socioRepo.buscarTodos().stream()
                .mapToInt(Socio::getId)
                .max()
                .orElse(0) + 1;

        Socio socio = tipo.equals("1")
                ? new Estudiante(id, nombre, apellido, dni, email)
                : new Docente(id, nombre, apellido, dni, email);

        try {
            socioService.registrar(socio);
            System.out.println("Socio registrado con id " + id + ".");
        } catch (DniDuplicadoException | EmailInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void buscarSocioPorDni() {
        System.out.print("DNI: ");
        Optional<Socio> resultado = socioRepo.buscarPorDni(leerLinea());
        if (resultado.isPresent()) {
            mostrarSocio(resultado.get());
        } else {
            System.out.println("No se encontro ningun socio con ese DNI.");
        }
    }

    private static void listarSocios() {
        List<Socio> todos = socioRepo.buscarTodos();
        if (todos.isEmpty()) {
            System.out.println("No hay socios registrados.");
            return;
        }
        todos.forEach(Main::mostrarSocio);
    }

    private static void mostrarSocio(Socio s) {
        String tipo = s instanceof Estudiante ? "Estudiante" : "Docente";
        System.out.println("[" + s.getId() + "] " + s.getNombre() + " " + s.getApellido()
                + " | DNI: " + s.getDni() + " | " + tipo
                + " | limite: " + s.getLimitePrestamos() + " prestamos");
    }

    // -------------------------------------------------------------------------
    // Recursos (Libros y E-books)
    // -------------------------------------------------------------------------

    private static void menuRecursos() {
        System.out.println("\n-- Recursos --");
        System.out.println("1. Agregar libro fisico");
        System.out.println("2. Agregar e-book");
        System.out.println("3. Buscar por titulo");
        System.out.println("4. Buscar por autor");
        System.out.println("5. Buscar por categoria");
        System.out.println("6. Listar todos");
        System.out.println("0. Volver");
        System.out.print("> ");

        switch (leerLinea()) {
            case "1" -> agregarLibro();
            case "2" -> agregarEbook();
            case "3" -> buscarPorTitulo();
            case "4" -> buscarPorAutor();
            case "5" -> buscarPorCategoria();
            case "6" -> listarRecursos();
            case "0" -> {}
            default  -> System.out.println("Opcion invalida.");
        }
    }

    private static void agregarLibro() {
        System.out.print("ISBN: ");
        String isbn = leerLinea();
        System.out.print("Titulo: ");
        String titulo = leerLinea();
        System.out.print("Autor: ");
        String autor = leerLinea();
        System.out.print("Anio: ");
        int anio;
        try {
            anio = Integer.parseInt(leerLinea());
        } catch (NumberFormatException e) {
            System.out.println("Anio invalido.");
            return;
        }
        Categoria categoria = pedirCategoria();
        if (categoria == null) return;

        libroRepo.guardar(new Libro(isbn, titulo, autor, anio, categoria));
        System.out.println("Libro agregado.");
    }

    private static void agregarEbook() {
        System.out.print("ISBN: ");
        String isbn = leerLinea();
        System.out.print("Titulo: ");
        String titulo = leerLinea();
        System.out.print("Autor: ");
        String autor = leerLinea();
        System.out.print("Paginas: ");
        int paginas;
        try {
            paginas = Integer.parseInt(leerLinea());
        } catch (NumberFormatException e) {
            System.out.println("Numero de paginas invalido.");
            return;
        }
        Categoria categoria = pedirCategoria();
        if (categoria == null) return;

        FormatoEbook[] formatos = FormatoEbook.values();
        System.out.println("Formato:");
        for (int i = 0; i < formatos.length; i++) {
            System.out.println((i + 1) + ". " + formatos[i]);
        }
        System.out.print("> ");
        int fIdx;
        try {
            fIdx = Integer.parseInt(leerLinea()) - 1;
            if (fIdx < 0 || fIdx >= formatos.length) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Formato invalido.");
            return;
        }

        libroRepo.guardar(new Ebook(isbn, titulo, autor, paginas, categoria, formatos[fIdx]));
        System.out.println("E-book agregado.");
    }

    private static void buscarPorTitulo() {
        System.out.print("Titulo (parcial): ");
        try {
            libroService.buscarPorTitulo(leerLinea()).forEach(Main::mostrarRecurso);
        } catch (LibroNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void buscarPorAutor() {
        System.out.print("Autor (parcial): ");
        try {
            libroService.buscarPorAutor(leerLinea()).forEach(Main::mostrarRecurso);
        } catch (LibroNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void buscarPorCategoria() {
        Categoria categoria = pedirCategoria();
        if (categoria == null) return;
        try {
            libroService.buscarPorCategoria(categoria).forEach(Main::mostrarRecurso);
        } catch (LibroNoEncontradoException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listarRecursos() {
        List<Recurso> todos = libroRepo.buscarTodos();
        if (todos.isEmpty()) {
            System.out.println("No hay recursos registrados.");
            return;
        }
        todos.forEach(Main::mostrarRecurso);
    }

    private static void mostrarRecurso(Recurso r) {
        String disponible = prestamoRepo.estaDisponible(r.isbn()) ? "disponible" : "prestado";
        if (r instanceof Libro l) {
            System.out.println("[LIBRO] [" + l.isbn() + "] \"" + l.titulo() + "\" - " + l.autor()
                    + " (" + l.anio() + ") | " + l.categoria() + " | " + disponible);
        } else if (r instanceof Ebook e) {
            System.out.println("[EBOOK] [" + e.isbn() + "] \"" + e.titulo() + "\" - " + e.autor()
                    + " | " + e.paginas() + " pags | " + e.categoria() + " | " + e.formato() + " | " + disponible);
        }
    }

    private static Categoria pedirCategoria() {
        Categoria[] categorias = Categoria.values();
        System.out.println("Categoria:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + ". " + categorias[i]);
        }
        System.out.print("> ");
        try {
            int idx = Integer.parseInt(leerLinea()) - 1;
            if (idx < 0 || idx >= categorias.length) throw new NumberFormatException();
            return categorias[idx];
        } catch (NumberFormatException e) {
            System.out.println("Categoria invalida.");
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Prestamos
    // -------------------------------------------------------------------------

    private static void menuPrestamos() {
        System.out.println("\n-- Prestamos --");
        System.out.println("1. Realizar prestamo");
        System.out.println("2. Registrar devolucion");
        System.out.println("3. Listar todos");
        System.out.println("0. Volver");
        System.out.print("> ");

        switch (leerLinea()) {
            case "1" -> realizarPrestamo();
            case "2" -> registrarDevolucion();
            case "3" -> listarPrestamos();
            case "0" -> {}
            default  -> System.out.println("Opcion invalida.");
        }
    }

    private static void realizarPrestamo() {
        System.out.print("ISBN del recurso: ");
        Optional<Recurso> recurso = libroRepo.buscarPorId(leerLinea());
        if (recurso.isEmpty()) {
            System.out.println("Recurso no encontrado.");
            return;
        }

        System.out.print("DNI del socio: ");
        Optional<Socio> socio = socioRepo.buscarPorDni(leerLinea());
        if (socio.isEmpty()) {
            System.out.println("Socio no encontrado.");
            return;
        }

        try {
            Prestamo p = prestamoService.realizarPrestamo(recurso.get(), socio.get());
            System.out.println("Prestamo registrado. ID: " + p.id()
                    + " | vence: " + p.fechaDevolucionEsperada());
        } catch (LibroNoDisponibleException | LimitePrestamosException | SocioSancionadoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void registrarDevolucion() {
        System.out.print("ID del prestamo: ");
        long id;
        try {
            id = Long.parseLong(leerLinea());
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
            return;
        }

        try {
            long diasRetraso = prestamoService.registrarDevolucion(id);
            if (diasRetraso > 0) {
                System.out.println("Devolucion registrada con " + diasRetraso + " dias de retraso.");
            } else {
                System.out.println("Devolucion registrada en termino.");
            }
        } catch (PrestamoNoEncontradoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listarPrestamos() {
        List<Prestamo> todos = prestamoService.obtenerTodosLosPrestamos();
        if (todos.isEmpty()) {
            System.out.println("No hay prestamos registrados.");
            return;
        }
        todos.forEach(Main::mostrarPrestamo);
    }

    private static void mostrarPrestamo(Prestamo p) {
        String estado = p.fechaDevolucionReal().isPresent() ? "devuelto" : "activo";
        System.out.println("[" + p.id() + "] \"" + p.recurso().titulo() + "\""
                + " | " + p.socio().getNombre() + " " + p.socio().getApellido()
                + " | vence: " + p.fechaDevolucionEsperada()
                + " | " + estado);
    }

    // -------------------------------------------------------------------------
    // Consultas
    // -------------------------------------------------------------------------

    private static void menuConsultas() {
        System.out.println("\n-- Consultas --");
        System.out.println("1. Historial de transacciones de un socio");
        System.out.println("2. Sanciones de un socio");
        System.out.println("0. Volver");
        System.out.print("> ");

        switch (leerLinea()) {
            case "1" -> historialTransacciones();
            case "2" -> sancionesSocio();
            case "0" -> {}
            default  -> System.out.println("Opcion invalida.");
        }
    }

    private static void historialTransacciones() {
        System.out.print("DNI del socio: ");
        Optional<Socio> socio = socioRepo.buscarPorDni(leerLinea());
        if (socio.isEmpty()) {
            System.out.println("Socio no encontrado.");
            return;
        }
        List<Transaccion> historial = prestamoService.obtenerHistorial(socio.get().getId());
        if (historial.isEmpty()) {
            System.out.println("Sin transacciones.");
            return;
        }
        historial.forEach(t -> System.out.println("[" + t.tipo() + "] \""
                + t.prestamo().recurso().titulo() + "\" - " + t.fecha()));
    }

    private static void sancionesSocio() {
        System.out.print("DNI del socio: ");
        Optional<Socio> socio = socioRepo.buscarPorDni(leerLinea());
        if (socio.isEmpty()) {
            System.out.println("Socio no encontrado.");
            return;
        }
        List<Sancion> sanciones = prestamoService.obtenerSanciones(socio.get().getId());
        if (sanciones.isEmpty()) {
            System.out.println("Sin sanciones.");
            return;
        }
        sanciones.forEach(s -> System.out.println("Desde " + s.fechaInicio() + " hasta " + s.fechaFin()));
    }

    // -------------------------------------------------------------------------

    private static String leerLinea() {
        return sc.nextLine().trim();
    }
}