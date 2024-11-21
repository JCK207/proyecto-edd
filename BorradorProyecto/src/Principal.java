import logica.*;
import persistencia.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Comparator;

public class Principal {
    static Scanner sc = new Scanner(System.in);
    static List<Tarea> tareas;
    static List<Tarea> tareasCompletas;
    
    static final String ARCHIVO_TAREAS = "tareas.gestor";
    static final String ARCHIVO_COMPLETAS = "completas.gestor";
    
    static List<String> ordenes = new ArrayList() {{
        add("Última modificación");
        add("Prioridad");
        add("Alfabético");
    }};
    static List<String> filtros = new ArrayList() {{
        add("Ninguno");
        add("Fecha");
        add("Descripción");
    }};
    
    public static void main(String[] args) {
        tareas = (List<Tarea>)ArchivosConexion.leer(ARCHIVO_TAREAS);
        tareasCompletas = (List<Tarea>)ArchivosConexion.leer(ARCHIVO_COMPLETAS);
        
        if (tareas==null) tareas = new ArrayList();
        if (tareasCompletas==null) tareasCompletas = new ArrayList();
        System.out.println("GESTIÓN DE TAREAS");
        
        int opcion;
        do {
            System.out.println();
            System.out.println("0. Salir");
            System.out.println("1. Crear Tarea");
            System.out.println("2. Ver Tareas");
            System.out.println("3. Ver Tareas Completas");
            System.out.print("Selección: ");
            opcion = sc.nextInt();
            sc.nextLine();
            
            switch (opcion) {
                case 0 -> salir();
                case 1 -> crearTarea();
                case 2 -> {
                    if (tareas.isEmpty()){
                        System.out.println();
                        System.out.println("No hay tareas sin completar");
                        if (!tareasCompletas.isEmpty())
                            System.out.println("Puede desmacarcar como completa alguna tarea");
                        break;
                    }
                    
                   modificarTareas(tareas, "TAREAS SIN COMPLETAR");
                }
                case 3 -> {
                    if (tareasCompletas.isEmpty()){
                        System.out.println();
                        System.out.println("No hay tareas completas");
                        break;
                    }
                    
                    modificarTareasCompletas(tareasCompletas, "TAREAS COMPLETAS");
                }
                default -> {
                    System.out.println();
                    System.out.println("Selección no válida");
                }
            }
        } while (opcion!=0);
    }
    
    
    static void crearTarea() {
        System.out.println();
        System.out.print("Descripción de la tarea: ");
        tareas.add(new Tarea(sc.nextLine(), LocalDateTime.now()));
        System.out.println("Tarea creada");
    }
    
    static void operarOrden(String orden, List<Tarea> lista) {
        Comparator<Tarea> comparador = null;
        
        switch (orden) {
            case "Última modificación" -> {
                comparador = Comparator.comparing(Tarea::getModificacion).reversed();
            }
            case "Prioridad" -> {
                comparador = Comparator.comparing(Tarea::getFecha, Comparator.nullsLast(Comparator.naturalOrder()));
            }
            case "Alfabético" -> {
                comparador = Comparator.comparing(Tarea::getDescripcion);
            }
        }
        
        Arbol arbol = new Arbol(comparador);
        for (Tarea tarea : lista) arbol.insertar(tarea);
        arbol.inorden(lista);
    }
    
    static String operarFiltro(String filtro, List<Tarea> lista) {
        String filtrado = null;
        switch (filtro) {
            case "Ninguno" -> {
                filtrado = "";
            }
            case "Fecha" -> {
                System.out.println();
                System.out.println("Buscar desde:");
                System.out.print("Día: ");
                int dia = sc.nextInt();
                System.out.print("Mes: ");
                int mes = sc.nextInt();
                LocalDateTime desde = LocalDateTime.of(LocalDateTime.now().getYear(), mes, dia, 0, 0);
                
                System.out.println("Buscar hasta:");
                System.out.print("Día: ");
                dia = sc.nextInt();
                System.out.print("Mes: ");
                mes = sc.nextInt();
                sc.nextLine();
                LocalDateTime hasta = LocalDateTime.of(LocalDateTime.now().getYear(), mes, dia, 23, 59);
                
                if (desde.isAfter(hasta)) {
                    System.out.println("Intercambie el orden de las fechas");
                }
                
                filtrado = " Desde: " + desde.getDayOfMonth() + "/" + desde.getMonthValue() + " Hasta: " + hasta.getDayOfMonth() + "/" + hasta.getMonthValue();
                lista.removeIf(tarea -> (tarea.getFecha()==null || (tarea.getFecha().isBefore(desde) || tarea.getFecha().isAfter(hasta))));
            }
            case "Descripción" -> {
                System.out.println();
                System.out.print("Buscar palabra: ");
                String palabra = sc.nextLine().toLowerCase();
                filtrado = ": " + palabra;
                
                lista.removeIf(tarea -> (!tarea.getDescripcion().toLowerCase().contains(palabra) && !tarea.getSubtareas().toString().toLowerCase().contains(palabra)));
            }
        }
        return filtrado;
    }
    
    static List<Tarea> verTareas(List<Tarea> lista, String nombreLista) {
        List<Tarea> auxTareas = new ArrayList(lista);
        String orden = "Última modificación";
        String filtro = "Ninguno";
        String filtrado = "";
        operarOrden(orden, auxTareas);
        
        int opcion;
        do {
            System.out.print("Enter");
            sc.nextLine();
            System.out.println();
            System.out.println("Orden: " + orden);
            System.out.println("Filtro: " + filtro + filtrado);
            System.out.println("------------------------------");
            System.out.println(nombreLista + ":");
            System.out.println("------------------------------");
            imprimirTareas(auxTareas);
            if (auxTareas.isEmpty()) {
                System.out.println("Ninguna coincidencia");
                System.out.println();
            }
            
            System.out.print("0) Volver ");
            System.out.print("1) Modificar tarea ");
            System.out.print("2) Ordenar ");
            System.out.println("3) Filtrar");
            System.out.print("Selección: ");
            opcion = sc.nextInt();
            System.out.println();
            sc.nextLine();
            
            switch (opcion) {
                case 0 -> {
                    System.out.println("Volviendo al Menú Principal");
                }
                case 1 -> {
                    return auxTareas;
                }
                case 2 -> {
                    System.out.println("Orden:");
                    imprimirLista(ordenes);
                    
                    String aux;
                    if ((aux = (String)seleccionarDeLista(ordenes))==null) break;
                    
                    orden = aux;
                    operarOrden(orden, auxTareas);
                }
                case 3 -> {
                    System.out.println("Filtro:");
                    imprimirLista(filtros);
                    
                    String aux;
                    if ((aux = (String)seleccionarDeLista(filtros))==null) break;
                    
                    filtro = aux;
                    auxTareas = new ArrayList(lista);
                    filtrado = operarFiltro(filtro, auxTareas);
                    operarOrden(orden, auxTareas);
                    
                }
                default -> {
                    System.out.println("Selección no válida");
                }
            }
        } while (opcion!=0);
        return null;
    }
    
    static void modificarTareas(List<Tarea> lista, String nombreLista) {
        List<Tarea> auxTareas;
        while ((auxTareas = verTareas(lista, nombreLista))!=null) {
            int opcion = 1;
            System.out.println("Seleccione una tarea para modificar");
            Tarea tarea = (Tarea)seleccionarDeLista(auxTareas);
            
            while (opcion!=0 && tarea!=null) {
                System.out.println();
                System.out.println("0. Volver");
                System.out.println("1. Marcar como completa");
                System.out.println("2. Agregar subtarea");
                System.out.println("3. Modificar subtarea");
                System.out.println("4. Modificar descripción");
                System.out.println("5. Modificar Fecha de entrega");
                System.out.print("Selección: ");
                opcion = sc.nextInt();
                sc.nextLine();
                System.out.println();

                switch (opcion) {
                    case 0 -> System.out.println("Volviendo");
                    case 1 -> {
                        tareas.remove(tarea);
                        tareasCompletas.add(tarea);
                        if (tarea.getFecha()!=null && tarea.getFecha().isBefore(LocalDateTime.now()))
                            tarea.setFecha(LocalDateTime.now());
                        
                        System.out.println("Tarea marcada como completa");
                        
                        opcion = 0;
                    }
                    case 2 -> {
                        System.out.print("Descripción de subtarea: ");
                        System.out.println(tarea.agregarSubtarea(sc.nextLine()));
                    }
                    case 3 -> {
                        int op;
                        List<String> subtareas = tarea.getSubtareas();
                        if (subtareas.isEmpty()) {
                            System.out.println("No hay subtareas");
                        } else {
                            for (int i=0; i<subtareas.size(); i++) {
                                if (i<tarea.getSubtareasSinCompletar().size()) {
                                    System.out.println("[ ] " + (i+1) + ". " + subtareas.get(i));
                                } else {
                                    System.out.println("[X] " + (i+1) + ". " + subtareas.get(i));
                                }
                            }
                            System.out.println();
                            System.out.println("Seleccione una subtarea para modificar");
                            String subtarea = (String)seleccionarDeLista(subtareas);
                            
                            if (subtarea!=null) {
                                System.out.println();
                                System.out.println("0. Volver");
                                System.out.println("1. Marcar como completa");
                                System.out.println("2. Desmarcar como completa");
                                System.out.println("3. Modificar descripción");
                                System.out.println("4. Eliminar subtarea");
                                System.out.print("Selección: ");
                                op = sc.nextInt();
                                sc.nextLine();
                                System.out.println();
                                
                                switch (op) {
                                    case 0 -> System.out.println("Volviendo");
                                    case 1 -> {
                                        System.out.println(tarea.marcarSubtarea(subtarea));
                                    }
                                    case 2 -> {
                                        System.out.println(tarea.desmarcarSubtarea(subtarea));
                                    }
                                    case 3 -> {
                                        System.out.print("Descripción de la subtarea: ");
                                        String descripcion = sc.nextLine();
                                        tarea.modificarSubtarea(subtarea, descripcion);
                                    }
                                    case 4 -> {
                                        System.out.println(tarea.eliminarSubtarea(subtarea));
                                    }
                                    default -> System.out.println("Selección no válida");
                                }
                                if (tarea.getSubtareasSinCompletar().isEmpty()) {
                                    opcion = 0;
                                    if (tarea.getSubtareasCompletas().isEmpty()) {
                                        System.out.println("No quedan subtareas");
                                    } else {
                                        System.out.println("Tarea completa al quedar todas sus subtareas completas");
                                        tareas.remove(tarea);
                                        tareasCompletas.add(tarea);
                                        if (tarea.getFecha()!=null && tarea.getFecha().isBefore(LocalDateTime.now()))
                                            tarea.setFecha(LocalDateTime.now());
                                    }
                                }
                                
                            }
                        }
                    }
                    case 4 -> {
                        System.out.print("Descripcion de la tarea: ");
                        tarea.setDescripcion(sc.nextLine());
                    }
                    case 5 -> {
                        System.out.println("Fecha a entregar:");
                        System.out.print("Día: ");
                        int dia = sc.nextInt();
                        System.out.print("Mes: ");
                        int mes = sc.nextInt();
                        sc.nextLine();
                        tarea.setFecha(LocalDateTime.now().withDayOfMonth(dia).withMonth(mes));
                    }
                    default -> System.out.println("Selección no válida");
                }
                tarea.setModificacion(LocalDateTime.now());
                if (tareas.isEmpty()) {
                    System.out.println("Se completaron todas las tareas");
                    return;
                }
            }
        }
    }
    
    static void modificarTareasCompletas(List<Tarea> lista, String nombreLista) {
        List<Tarea> auxTareas;
        while ((auxTareas = verTareas(lista, nombreLista))!=null) {
            int opcion = 1;
            System.out.println("Seleccione una Tarea para modificar");
            Tarea tarea = (Tarea)seleccionarDeLista(auxTareas);
            while (opcion!=0 && tarea!=null) {
                System.out.println();
                System.out.println("0. Volver");
                System.out.println("1. Desmarcar como completa");
                System.out.println("2. Eliminar tarea");
                System.out.println("Selección: ");
                opcion = sc.nextInt();
                sc.nextLine();
                System.out.println();
                
                switch(opcion){
                    case 0 -> System.out.println("Volviendo a Ver Tareas Completas");
                    case 1 -> {
                        tareasCompletas.remove(tarea);
                        tareas.add(tarea);
                        tarea.desmarcarSubtareas();
                        if (tarea.getFecha().isBefore(LocalDateTime.now()))
                            tarea.setFecha(LocalDateTime.now());
                        System.out.println("Tarea desmarcada como completa");
                        
                        opcion = 0;
                    }
                    case 2 -> {
                        tareasCompletas.remove(tarea);
                        System.out.println("Tarea eliminada");
                        
                        opcion = 0;
                    }
                    default -> System.out.println("Selección no válida");
                }
                tarea.setModificacion(LocalDateTime.now());
                if (tareasCompletas.isEmpty()) {
                    System.out.println("No quedan tareas completas");
                    return;
                }
            }
        }
    }
    
    static void salir() {
        ArchivosConexion.guardar(tareas, ARCHIVO_TAREAS);
        ArchivosConexion.guardar(tareasCompletas, ARCHIVO_COMPLETAS);
    }
    
    static void imprimirLista(List lista) {
        for (int i=0; i<lista.size(); i++) {
            System.out.println(i+1 + ". " + lista.get(i));
        }
    }
    
    static void imprimirTareas(List<Tarea> lista) {
        for (int i=0; i<lista.size(); i++) {
            Tarea tarea = lista.get(i);
            System.out.println((i+1) + ". " + tarea.getDescripcion());
            
            if (tarea.getFecha()!=null) {
                String expirado = "";
                if (tareas.contains(tarea) && LocalDateTime.now().isAfter(tarea.getFecha()))
                    expirado = " Expirada";
                
                System.out.println("Fecha: " + tarea.imprimirFecha() + expirado);
            }
            
            List<String> subtareas = tarea.getSubtareas();
            if (!subtareas.isEmpty()) {
                for (int j=0; j<subtareas.size(); j++) {
                    if (j<tarea.getSubtareasSinCompletar().size()) {
                        System.out.println("[ ] " + (j+1) + ". " + subtareas.get(j));
                    } else {
                        System.out.println("[X] " + (j+1) + ". " + subtareas.get(j));
                    }
                }
            }
            
            System.out.println();
        }
    }
    
    static Object seleccionarDeLista(List lista) {
        int seleccion;
        System.out.println("0. Cancelar");
        System.out.println();
        do {
            System.out.print("Selección: ");
            seleccion = sc.nextInt();
            sc.nextLine();
            if (seleccion==0) return null;
            if (seleccion<0 || seleccion>lista.size()) System.out.println("Selección no válida");
        } while (seleccion<0 || seleccion>lista.size());
        return lista.get(seleccion-1);
    }
    
}