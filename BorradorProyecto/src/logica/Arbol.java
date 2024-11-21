package logica;

import java.util.Comparator;
import java.util.List;

public class Arbol {

    private Nodo raiz;
    private Comparator comparador;

    public Arbol(Comparator comparador) {
       this.comparador = comparador;
    }

    public void insertar(Tarea dato) {
        raiz = this.insertar(raiz, dato);
    }

    private Nodo insertar(Nodo padre, Tarea dato) {
        if (padre == null) {
            return new Nodo(dato);
        }
        
        if (comparador.compare(dato, padre.dato)<0) {
            padre.izquierda = this.insertar(padre.izquierda, dato);
        } else {
            padre.derecha = this.insertar(padre.derecha, dato);
        }
        
        return padre;
    }

    private void inorden(Nodo n, List<Tarea> lista) {
        if (n != null) {
            this.inorden(n.izquierda, lista);
            lista.add(n.dato);
            this.inorden(n.derecha, lista);
        }
    }

    public void inorden(List<Tarea> lista) {
        lista.clear();
        this.inorden(raiz, lista);
    }

}
