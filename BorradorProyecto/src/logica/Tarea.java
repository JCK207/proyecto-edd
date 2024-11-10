package logica;

import java.util.LinkedList;
import java.util.List;

public class Tarea {

    private String descripcion;
    private int fecha;
    private List<String> subtareas;

    public Tarea(String descripcion) {
        this.descripcion = descripcion;
        this.fecha = 0;
        this.subtareas = new LinkedList();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public List getSubtareas() {
        return subtareas;
    }

    public void agregarSubtarea(String subtarea) {
        subtareas.add(descripcion);
    }

}
