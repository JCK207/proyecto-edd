package logica;

import java.util.Comparator;
import java.util.List;

public class Arbol {

    private Nodo raiz;
    private Comparator<Tarea> comparador;

    public Arbol(Comparator comparador) {
       this.comparador = comparador;
    }

    public void insertar(Tarea dato) {
        raiz = this.insertar(raiz, dato);
    }

    private Nodo insertar(Nodo nodo, Tarea dato) {
        if (nodo == null) {
            return new Nodo(dato);
        }
        
        if (comparador.compare(dato, nodo.dato)<0) {
            nodo.izquierda = this.insertar(nodo.izquierda, dato);
        } else {
            nodo.derecha = this.insertar(nodo.derecha, dato);
        }
        
        return nodo;
    }

    public void inorden(List<Tarea> lista) {
        lista.clear();
        this.inorden(raiz, lista);
    }

    private void inorden(Nodo nodo, List<Tarea> lista) {
        if (nodo!=null) {
            this.inorden(nodo.izquierda, lista);
            lista.add(nodo.dato);
            this.inorden(nodo.derecha, lista);
        }
    }

}
