import java.util.Iterator;
import java.util.LinkedList;

public class MyHashMap<K, V> implements Iterable<K> {
	
	private class KVPair {
		private K key;
		private V value;
	
		public KVPair(K key, V value){
			this.key = key;
			this.value = value;
		}
		
		public K key(){
			return key;
		}
		
		public V value(){
			return value;
		}
		
		public void setV(V newValue){
			value = newValue;
		}
		
		public void delete(){
			value = null;
			key = null;
		}
	}
	
	/* Default size of the map if not set in constructor */
	private static final int DEFAULT_CAPACITY = 30;

	/* Default load factor if not set in constructor */
	private static final double DEFAULT_LOAD_FACTOR = 0.7;

	private int capacity; // the number of buckets in the map
	private int size; // the number of items that have been put into the map
	private double loadFactor;
    private Object[] table; 
	/**
	 * Constructs an empty map.
	 */
	public MyHashMap() {
		capacity = DEFAULT_CAPACITY;
		table = new Object[DEFAULT_CAPACITY];
		for(int i = 0; i < DEFAULT_CAPACITY; i++) 
			table[i] = new LinkedList<KVPair>();
		loadFactor = DEFAULT_LOAD_FACTOR;
		size = 0;
	}

	/**
	 * Constructs an empty map with the given initial capacity.
	 */
	public MyHashMap(int initialCapacity) {
		capacity = initialCapacity;
		table = new Object[initialCapacity];
		for(int i = 0; i < initialCapacity; i++) 
			table[i] = new LinkedList<KVPair>();
		loadFactor = DEFAULT_LOAD_FACTOR;
		size = 0;
	}

	/**
	 * Constructs an empty map with the given intial capacity and the given load
	 * factor.
	 * 
	 * @param loadFactor
	 *            A fraction greater than 0 and less than 1. Once the size /
	 *            capacity exceeds this number, the map's underlying array
	 *            should expand.
	 */
	public MyHashMap(int initialCapacity, double loadFactor) {
		capacity = initialCapacity;
		table = new Object[initialCapacity];
		for(int i = 0; i < initialCapacity; i++) 
			table[i] = new LinkedList<KVPair>();
		size = 0;
		this.loadFactor = loadFactor;
		
	}

	/**
	 * Returns the number of items put into the map (and not subsequently
	 * removed).
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the capacity of the underlying array of the map.
	 */
	public int capacity() {
		return this.capacity;
	}

	public double loadFactor(){
		return loadFactor;
	}
	/**
	 * Returns whether the map contains the given key. Runs in O(1) time on
	 * average, relative to the size of the map.
	 */
	public boolean containsKey(K key) {
		int hash = hash(key);
		LinkedList<KVPair> temp = (LinkedList<KVPair>)table[hash];
        if(!(temp.isEmpty())) return true;
		return false;
	}
		

	/**
	 * Returns whether there is some key in the map that contains this value.
	 * How fast can you make this run?
	 */
	public boolean containsValue(V value) {
		for(int i = 0; i < capacity; i++){
			LinkedList<KVPair> temp = (LinkedList<KVPair>)table[i];
			if(!(temp.isEmpty())){
			    int s = temp.size();
			    for(int j = 0; j < s; j++) if(temp.get(j).value().equals(value)) return true;
		    }
		}
		return false;
	}

	/**
	 * Puts the key in the map with the given value. If the key is already in
	 * the map, replaces the value. Should run in O(1) time on average with
	 * respect to the size of the map.
	 * 
	 * Returns the previous value associated with the key, or null if there was
	 * no such value.
	 * 
	 * Note: If this method causes size / capacity to be greater than the load
	 * factor, then this method should also expand the map.
	 */
	public V put(K key, V value) {
		int hash = hash(key);
		if(get(key) != null){
			V rtn = get(key);
			int index = getIndex(key);
			((LinkedList<KVPair>)table[hash]).get(index).setV(value);
			return rtn; 
		}
		else{
		    KVPair toadd = new KVPair(key, value);
		    ((LinkedList<KVPair>)table[hash]).add(toadd);
		    size++;
		    if(size >= capacity * loadFactor) expand(capacity*2);
		    return null;
		}
	}

	/**
	 * Removes the key from the map. Should run in O(1) time on average with
	 * respect to the size of the map.
	 * 
	 * Returns the value associated with the key, or null if there was no key.
	 */
	
	public int getIndex(K key){
		int hash = hash(key);
		LinkedList<KVPair> temp = (LinkedList<KVPair>)table[hash];
		int s = temp.size();
		for(int i = 0; i < s; i++){
			if(temp.get(i).key().equals(key)) return i;
		}
		return -1;
	}
	
	public V remove(K key) {
		if (get(key) != null){
			int index = getIndex(key);
			V rtn = get(key);
			int hash = hash(key);
			KVPair target = ((LinkedList<KVPair>)table[hash]).get(index);
			((LinkedList<KVPair>)table[hash]).remove(target);
			size--;
			return rtn; 
		}
		return null;
	}

	/**
	 * Returns the value associated with the key in the map, or null if there is
	 * no such value. Should run in O(1) time on average with respect to the
	 * size of the map.
	 */
	public V get(K key) {
		int hash = hash(key);
		LinkedList<KVPair> temp = (LinkedList<KVPair>)table[hash];
		if (temp != null){
			int s = temp.size();
		    for(int i = 0; i < s; i++){
			    if(temp.get(i).key().equals(key)) return temp.get(i).value();
		    }
		}
	    return null;
	}

	/**
	 * Expands the underlying array to the given capacity.
	 * 
	 * ALSO, rehashes all of the items into this new array. Items will likely
	 * end up in different buckets after the expansion.
	 */
	private void expand(int newCapacity) {
		Object[] temp = new Object[newCapacity];
		for(int i = 0; i < size; i++)
			temp[i] = table[i];
		for(int i = size; i < newCapacity; i++)
			temp[i] = new LinkedList<KVPair>();
		capacity = newCapacity;
		table = temp;
	}

	/**
	 * Returns an iterator over the keys of this map.
	 */
	public Iterator<K> iterator() {
		return new HashMapIterator();
	}

	/**
	 * An iterator for the keys of the enclosing map.
	 */
	private class HashMapIterator implements Iterator<K> {
		
		private int r;
		private int count;
		private KVPair p;
		private LinkedList<KVPair> head;

		public HashMapIterator() {
			r = 0;
			count = 0;
			head = (LinkedList<KVPair>)table[0];
			p = null;
		}

		@Override
		public boolean hasNext() {
			if (size == 0 || count >= size){
				return false;
			}
			else return true;
		}

		@Override
		public K next() {
			if (p == null){
			    for(int i = r; i < capacity; i++){
			        if (((LinkedList<KVPair>)table[i]).isEmpty()) r++;
			        else {
					    head = (LinkedList<KVPair>)table[r];
					    p = head.peekFirst();
					    count++;
					    return p.key();
			        }
			    }
			}
			else {
				if(p != head.peekLast()){
				    int index = ((LinkedList<KVPair>)table[r]).indexOf(p);
				    count++;
				    return ((LinkedList<KVPair>)table[r]).get(index+1).key();
				}
				else {
					r++;
					for(int i = r; i < capacity; i++){			
				        if (((LinkedList<KVPair>)table[i]).isEmpty()) r++;
				        else {
						    head = (LinkedList<KVPair>)table[r];
						    p = head.peekFirst();
						    count++;
						    return p.key();
				        }
				    }
				}
			}
			return null;
		}
	}

	public int hash(K key){
		return Math.abs(key.hashCode() % capacity);
	}
}
