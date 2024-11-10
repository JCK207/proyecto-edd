import logica.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    static Scanner sc = new Scanner(System.in);
    static List<Tarea> tareas = new LinkedList();
    static List<Tarea> tareasCompletas = new LinkedList();
    static List<String> ordenes = new LinkedList();
    static List<String> filtros = new LinkedList();
    
    public static void main(String[] args) {
        ordenes.add("Fecha de modificación");
        ordenes.add("Prioridad");
        ordenes.add("Alfabético");
        filtros.add("Ninguno");
        filtros.add("Fecha de entrega");
        filtros.add("Palabras clave");
        
        System.out.println("GESTIÓN DE TAREAS");
        
        int opcion;
        do {
            System.out.println();
            System.out.println("1. Crear Tarea");
            System.out.println("2. Ver Tareas");
            System.out.println("3. Ver Tareas Completas");
            System.out.println("4. Salir");
            System.out.print("Selección: ");
            opcion = sc.nextInt();
            System.out.println();
            
            switch (opcion) {
                case 1 -> crearTarea();
                case 2 -> verTareas();
                case 3 -> verTareasCompletas();
                case 4 -> salir();
                default -> System.out.println("Selección no válida");
            }
        } while (opcion!=4);
    }
    
    static void crearTarea() {
        System.out.println("Descripcion de la tarea:");
        tareas.add(new Tarea(sc.next()));
    }
    
    
    static boolean visualizarTareas(List<Tarea> lista, String nombreLista) {
        int orden = 1;
        int filtro = 1;
        
        int opcion;
        do {
            imprimirTareas(lista, nombreLista, orden, filtro);            
            System.out.println();
            
            System.out.print("1. Ordenar");
            System.out.print(" 2. Filtrar");
            System.out.print(" 3. Modificar Tarea");
            System.out.println(" 4. Volver");
            System.out.print("Selección: ");
            opcion = sc.nextInt();
            System.out.println();
            
            switch (opcion) {
                case 1 -> {
                    imprimirLista(ordenes);
                    orden = (int)seleccionarDeLista(ordenes);//crear seleccionarDeArray
                }
                case 2 -> {
                    imprimirLista(filtros);
                    orden = (int)seleccionarDeLista(filtros);
                }
                case 3 -> {
                    return true;
                }
                case 4 -> System.out.println("Volviendo al Menú Principal");
                default -> {
                    System.out.println();
                    System.out.println("Selección no válida");
                }
            }
        } while (opcion!=4);
        return false;
    }
    
    
    static void verTareas() {
        if (tareas.isEmpty()){
            System.out.println("No hay tareas sin completar");
            if (!tareasCompletas.isEmpty())
                System.out.println("Puede desmacarcar como completa una tarea");
            return;
        }
        
        if (visualizarTareas(tareas, "TAREAS SIN COMPLETAR")) {
            System.out.println();
            System.out.println("Seleccione una Tarea para modificar");
            Tarea tarea = (Tarea)seleccionarDeLista(tareas);
            if (tarea==null) return;

            int opcion;
            do {
                System.out.println();
                System.out.println("1. Marcar como completa");
                System.out.println("2. Agregar subtarea");
                System.out.println("3. Modificar subtareas");
                System.out.println("4. Modificar descripción");
                System.out.println("5. Agregar o Modificar Fecha de entrega");
                System.out.println("6. Volver");
                System.out.print("Selección: ");
                opcion = sc.nextInt();
                System.out.println();

                switch (opcion) {
                    case 1 -> {
                        tareas.remove(tarea);
                        tareasCompletas.add(tarea);
                        System.out.println("Tarea marcada como completa");
                        return;
                    }
                    case 2 -> {
                        System.out.println("Descripción de subtarea:");
                        tarea.agregarSubtarea(sc.nextLine());
                    }
                    case 3 -> {
                        //Agregar menú;
                    }
                    case 4 -> {
                        System.out.println("Descripcion de la tarea:");
                        tarea.setDescripcion(sc.nextLine());
                    }
                    case 5 -> {
                        System.out.println("Fecha a entregar:");
                        tarea.setFecha(sc.nextInt());
                    }
                    case 6 -> System.out.println("Volviendo al Menú Principal");
                    default -> System.out.println("Selección no válida");
                }
            } while (opcion!=6);
        } else return;
    }
    
    
    static void verTareasCompletas() {
        if (tareasCompletas.isEmpty()){
            System.out.println("No hay tareas completas");
            return;
        }
        
        System.out.println("Tareas completas:");
        imprimirTareas(tareasCompletas);
        
        System.out.println();
        System.out.println("Seleccione una Tarea para modificar");
        Tarea tarea = (Tarea)seleccionarDeLista(tareasCompletas);
        if (tarea==null) return;
        
        int opcion;
        do {
            System.out.println();
            System.out.println("1. Desmarcar como completa");
            System.out.println("2. Eliminar tarea");
            System.out.println("3. Volver");
            System.out.println("Selección: ");
            opcion = sc.nextInt();
            System.out.println();
            
            switch(opcion){
                case 1 -> {
                    tareasCompletas.remove(tarea);
                    tareas.add(tarea);
                    System.out.println("Tarea desmarcada como completa");
                    return;
                }
                case 2 -> {
                    tareasCompletas.remove(tarea);
                    System.out.println("Tarea eliminada");
                }
                case 3 -> System.out.println("Volviendo al Menú Principal");
                default -> System.out.println("Selección no válida");
            }
        } while (opcion!=3);
    }
    
    
    static void salir() {
        //Agregar menú
    }
    
    
    /*static int imprimirMenu(String[] menu) {
        int numeral = 1;
        System.out.println();
        for (String texto : menu)
            System.out.println(numeral + "." + texto);
        System.out.println(numeral + ". Salir");
        
        return numeral;
    }//*/
    
    
    static void imprimirLista(List lista) {
        for (int i=0; i<lista.size(); i++) {
            System.out.println(i+1 + ". " + lista.get(i));
        }
    }
    
    
    static void imprimirTareas(List<Tarea> lista, String nombreLista, int orden, int filtro) {
        System.out.println("Orden: " + ordenes.get(orden-1));
        System.out.println("Filtro: " + filtros.get(filtro-1));
        System.out.println();
        System.out.println(nombreLista + ":");
        for (int i=0; i<lista.size(); i++) {
            Tarea tarea = lista.get(i);
            System.out.print(i+1 + ". " + tarea.getDescripcion());
            
            if (tarea.getFecha()!=0)
                System.out.println(" Fecha: " + tarea.getFecha());
            
            List<String> subTareas = tarea.getSubtareas();
            if (subTareas!=null) {
                System.out.println("Subtareas:");
                for (String subTarea : subTareas) {
                    System.out.println(subTarea);
                }
            }
        }
    }
    
    
    static Object seleccionarDeLista(List lista) {
        int seleccion;
        System.out.println("0 para Cancelar");
        do {
            System.out.print("Selección: ");
            seleccion = sc.nextInt();
            if (seleccion==0) return null; //selección para cancelar
            if (seleccion<0 || seleccion>lista.size()) System.out.println("Selección no válida");
        } while (seleccion<0 || seleccion>lista.size());
        
        return lista.get(seleccion-1); //se requiere un cast de la clase de llegada al llamar el método
    }
    
    
}
