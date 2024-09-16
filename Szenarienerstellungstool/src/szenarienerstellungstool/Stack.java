package szenarienerstellungstool;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class Stack<T> {
    private ArrayList<T> elements;

    public Stack() {
        elements = new ArrayList<>();
    }

    // Fügt ein Element auf den Stack hinzu
    public void push(T item) {
        elements.add(item);
    }

    // Entfernt und gibt das oberste Element des Stacks zurück
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.remove(elements.size() - 1);
    }

    // Gibt das oberste Element des Stacks zurück, ohne es zu entfernen
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return elements.get(elements.size() - 1);
    }

    // Überprüft, ob der Stack leer ist
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    // Gibt die Anzahl der Elemente im Stack zurück
    public int size() {
        return elements.size();
    }

    // Leert den Stack
    public void clear() {
        elements.clear();
    }
    public void deletes(T item) {
    	if (isEmpty()) {
            throw new EmptyStackException();
        }
    	for (int i = 0; i<elements.size()-1; i++) {
    		if (elements.get(i).equals(item)) {
    			elements.remove(elements.get(i));
    		}	
    	}
    }
}
