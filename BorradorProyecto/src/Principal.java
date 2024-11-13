import logica.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Comparator;

public class Principal {
    static Scanner sc = new Scanner(System.in, "ISO-8859-1");
    static List<Tarea> tareas = new ArrayList();
    static List<Tarea> tareasCompletas = new ArrayList();
    
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
        
        //generación aleatoria
        LocalDateTime fechaActual = LocalDateTime.now().minusWeeks(2);
        for (int i=1; i<=10; i++) {
            int random = (int)(Math.random()*3+3);
            char letra = (char)(Math.random()*26+65);
            char[] letras = new char[random*4];
            letras[1] = letra;
            for (int j=2; j<random*4-2; j++) {
                letra = (char)(Math.random()*26+97);
                letras[j] = letra;
            }
            String palabra = new String(letras);
            Tarea tarea = new Tarea(palabra, LocalDateTime.now());
            
            if (random>1)
                tarea.setFecha(fechaActual.plusDays((long)(Math.random()*30+1)));
            
            if (random==3) {
                String[] palabras = palabra.split(letras[i]+"");
                for (String p : palabras) {
                    tarea.agregarSubtarea(p);
                }
            }
                
            tareas.add(tarea);
        }//*/
        
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
                        System.out.println("No hay tareas sin completar");
                        if (!tareasCompletas.isEmpty())
                            System.out.println("Puede desmacarcar como completa alguna tarea");
                        break;
                    }
                    
                   modificarTareas(tareas, "TAREAS SIN COMPLETAR");
                }
                case 3 -> {
                    if (tareasCompletas.isEmpty()){
                        System.out.println("No hay tareas completas");
                        break;
                    }
                    
                    modificarTareasCompletas(tareasCompletas, "TAREAS COMPLETAS");
                }
                default -> System.out.println("Selección no válida");
            }
        } while (opcion!=0);
    }
    
    
    static void crearTarea() {
        System.out.println();
        System.out.print("Descripción de la tarea: ");
        tareas.add(new Tarea(sc.nextLine(), LocalDateTime.now()));
    }
    
    static void operarOrden(String orden, List<Tarea> lista) {
        switch (orden) {
            case "Última modificación" -> {
                lista.sort(Comparator.comparing(Tarea::getModificacion).reversed());
            }
            case "Prioridad" -> {
                lista.sort(Comparator.comparing(Tarea::getFecha, Comparator.nullsLast(Comparator.naturalOrder())));
            }
            case "Alfabético" -> {
                lista.sort(Comparator.comparing(Tarea::getDescripcion));
            }
        }
    }
    
    static String operarFiltro(String filtro, List<Tarea> lista) {
        String filtrado = null;
        switch (filtro) {
            case "Ninguno" -> {
                filtrado = "";
            }
            case "Fecha" -> {
                System.out.println("Buscar desde:");
                //mirar si "Enter para año actual: " + LocalDateTime.now().getYear();
                System.out.print("Día: ");
                int dia = sc.nextInt(); //leerEnRango() o similar
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
                System.out.print("Buscar palabra: ");
                //sc.nextLine();
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
        System.out.print("Enter");
        
        int opcion;
        do {
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
            sc.nextLine();
            
            switch (opcion) {
                case 0 -> {
                    System.out.println();
                    System.out.println("Volviendo al Menú Principal");
                }
                case 1 -> {
                    return auxTareas;
                }
                case 2 -> {
                    System.out.println();
                    System.out.println("Orden:");
                    imprimirLista(ordenes);
                    
                    String aux;
                    if ((aux = (String)seleccionarDeLista(ordenes))==null) break;
                    
                    orden = aux;
                    operarOrden(orden, auxTareas);
                }
                case 3 -> {
                    System.out.println();
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

                switch (opcion) {
                    case 0 -> System.out.println("Volviendo");
                    case 1 -> {
                        tareas.remove(tarea);
                        tareasCompletas.add(tarea);
                        if (tarea.getFecha().isBefore(LocalDateTime.now()))
                            tarea.setFecha(LocalDateTime.now());
                        System.out.println("Tarea marcada como completa");
                        
                        opcion = 0;
                    }
                    case 2 -> {
                        System.out.print("Descripción de subtarea: ");
                        tarea.agregarSubtarea(sc.nextLine());
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
                                switch (op) {
                                    case 0 -> System.out.println("Volviendo");
                                    case 1 -> {
                                        System.out.println(tarea.marcarSubtarea(subtarea));

                                        if (tarea.getSubtareasSinCompletar().isEmpty()) {
                                            System.out.println("Tarea completa al completar todas sus subtareas");
                                            tareas.remove(tarea);
                                            tareasCompletas.add(tarea);
                                            tarea.setFecha(LocalDateTime.now());
                                            opcion = 0;
                                        }
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
                                        tarea.eliminarSubtarea(subtarea);
                                        System.out.println("Subtarea eliminada");

                                        if (tarea.getSubtareasSinCompletar().isEmpty()) {
                                            opcion = 0;
                                            if (tarea.getSubtareasCompletas().isEmpty()) {
                                                System.out.println("No quedan subtareas");
                                            } else {
                                                System.out.println("Tarea completa al quedar todas sus subtareas completas");
                                                tareas.remove(tarea);
                                                tareasCompletas.add(tarea);
                                                if (tarea.getFecha().isBefore(LocalDateTime.now()))
                                                    tarea.setFecha(LocalDateTime.now());
                                            }
                                        }

                                    }
                                    default -> System.out.println("Selección no válida");
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
                        //mirar si "Enter para año actual: " + LocalDateTime.now().getYear();
                        System.out.print("Día: ");
                        int dia = sc.nextInt(); //leerEnRango() o similar
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
        //Agregar menú
        
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
        do {
            System.out.print("Selección: ");
            seleccion = sc.nextInt();
            sc.nextLine();
            if (seleccion==0) return null;
            if (seleccion<0 || seleccion>lista.size()) System.out.println("Selección no válida");
        } while (seleccion<0 || seleccion>lista.size());
        return lista.get(seleccion-1); //se requiere un cast de la clase de llegada al llamar el método
    }
    
}