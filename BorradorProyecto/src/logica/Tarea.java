package logica;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.io.Serializable;

public class Tarea implements Serializable {

    private String descripcion;
    private LocalDateTime fecha;
    private List<String> subtareasSinCompletar;
    private List<String> subtareasCompletas;
    private LocalDateTime modificacion;

    public Tarea(String descripcion, LocalDateTime modificacion) {
        this.descripcion = descripcion;
        this.modificacion = modificacion;
        
        fecha = null;
        subtareasSinCompletar = new ArrayList();
        subtareasCompletas = new ArrayList();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public List<String> getSubtareas() {
        List<String> subtareas = new ArrayList();
        subtareas.addAll(getSubtareasSinCompletar());
        subtareas.addAll(getSubtareasCompletas());
        return subtareas;
    }

    public List<String> getSubtareasSinCompletar() {
        return subtareasSinCompletar;
    }

    public List<String> getSubtareasCompletas() {
        return subtareasCompletas;
    }

    public LocalDateTime getModificacion() {
        return modificacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
    public void setModificacion(LocalDateTime modificacion) {
        this.modificacion = modificacion;
    }
    
    public String imprimirFecha() {
        return fecha.getDayOfMonth() + "/" + fecha.getMonthValue();
    }
    
    public void agregarSubtarea(String descripcion) {
        subtareasSinCompletar.add(descripcion);
    }
    
    public void modificarSubtarea(String subtarea, String descripcion) {
        if (subtareasSinCompletar.contains(subtarea)) {
            subtareasSinCompletar.set(subtareasSinCompletar.indexOf(subtarea), descripcion);
            return;
        }
        subtareasCompletas.set(subtareasCompletas.indexOf(subtarea), descripcion);
    }
    
    public String marcarSubtarea(String subtarea) {
        if (subtareasCompletas.contains(subtarea)) return "La subtarea ya está completa";
        subtareasSinCompletar.remove(subtarea);
        subtareasCompletas.add(subtarea);
        return "Subtarea marcada como completa";
    }

    public String desmarcarSubtarea(String subtarea) {
        if (subtareasSinCompletar.contains(subtarea)) return "La subtarea no está completa";
        agregarSubtarea(subtarea);
        subtareasCompletas.remove(subtarea);
        return "Subtarea desmarcada como completa";
    }
    
    public void desmarcarSubtareas() {
        subtareasSinCompletar.addAll(subtareasCompletas);
        subtareasCompletas.clear();
    }
    
    public boolean eliminarSubtarea(String subtarea) {
        boolean completo;
        subtareasSinCompletar.remove(subtarea);
        completo = subtareasCompletas.remove(subtarea);
        return completo;
    }
}
