package com.rivergillis.androidgames.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * This class reuses previously defined instances of objects in order to prevent garbage collection
 * @author jgillis
 *
 */
public class Pool<T> {
	
	// Generic interface, createObject will return a new object with the type of the Pool instance
    public interface PoolObjectFactory<T> {
        public T createObject();
    }

    private final List<T> freeObjects;				// stores pooled object
    private final PoolObjectFactory<T> factory;		// used to generate new instances of the type held by the class
    private final int maxSize;						// stores the max number of objects the pool can hold

    public Pool(PoolObjectFactory<T> factory, int maxSize) {
        this.factory = factory;							// Takes a PoolObjectFactory and a max size
        this.maxSize = maxSize;
        this.freeObjects = new ArrayList<T>(maxSize);	// Instantiate freeObjects with maxSize
    }

    public T newObject() {
        T object = null;		// create an empty object

        if (freeObjects.isEmpty())
            object = factory.createObject();					// create an object using the factory if the pool is empty
        else
            object = freeObjects.remove(freeObjects.size() - 1);// otherwise take an object from the pool
        return object;			// return the object
    }

    // Inserts an object into the freeObjects list if it is not yet full
    public void free(T object) {
        if (freeObjects.size() < maxSize)
            freeObjects.add(object);
    }
}
