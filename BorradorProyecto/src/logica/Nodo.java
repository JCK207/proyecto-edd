package logica;

public class Nodo {
    
    protected Tarea dato;
    protected Nodo izquierda, derecha;

    public Nodo(Tarea dato) {
        this.dato = dato;
        izquierda = null;
        derecha = null;
    }
}
