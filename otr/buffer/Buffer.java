package otr.buffer;

public class Buffer<T>  {

	private T[] buffer;

	private int counter = 0;
	private int head = 0;
	private int tail = 0;
	
	@SuppressWarnings("unchecked")
	public Buffer(int size) {
		buffer = (T[]) new Object[size];
	}


	public synchronized void put(T item) {
		while(buffer.length == counter)
			try {
				wait();
			} catch (InterruptedException e) {}
		
		buffer[head] = item;
		head = (head + 1) % buffer.length;
		counter++;
		notifyAll();
	}

	public synchronized T get() {
		T item = null;
		while(counter == 0)
			try {
				wait();
			} catch (InterruptedException e) {}
		
		item = buffer[tail];
		tail = (tail + 1) % buffer.length;
		counter--;
		notifyAll();
		return item;
	}

}
