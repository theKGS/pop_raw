package raw.java.j_int_java;

import java.util.*;

//Interface List<Message>;
/**
 * FIFO queue.
 * @author 
 *
 */
public class FIFO {
	private Node first;
	private Node last;
	/**
	 * Konstruktorn som g�r ingenting.
	 */
	public FIFO() {
		this.first = null;
		this.last = null;
	}
	/**
	 * Plockar ut f�rsta element ur k�n och returnera str�ngen.
	 * @return str�ngen i f�rsta elementet om det finns annars null.
	 */
	public synchronized Message get() {
		while (this.first == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("Wait failed.");
				e.printStackTrace();
			}
		}
		Node temp = first;
		first = first.getNext();
		temp.setNext(null);
		if (this.first == null) {
			this.last = null;
		}
		return temp.getMsg();

	}
	/**
	 * Tar ett str�ng och skapar en Node i listan.
	 * @param m som ett str�ng
	 */
	public synchronized void put(Message m) {
		Node ny = new Node(m, null);

		if (last == null) {
			last = ny;
			first = ny;
		} else {
			last.setNext(ny);
			this.last = ny;
		}
		notify();
	}

	/**
	 * Subklassen Node som hanterar alla noder.
	 * @author  Athanasios, Hassan(Ramin), Daniel, Eric, Marcus
	 *
	 */
	private class Node {
		private Message m;
		private Node next;
		/**
		 * Konstruktorn Node tar en str�ng m och en Node next.
		 * @param m
		 * @param next
		 */
		public Node(Message m, Node next) {
			this.m = m;
			this.next = next;
		}
		/**
		 * S�tter en nodes next variabel till Noden n. 
		 * @param n
		 */
		public void setNext(Node n) {
			this.next = n;
		}
		/**
		 * Returnera next pekaren.
		 * @return next pekaren.
		 */
		public Node getNext() {
			return this.next;
		}
		/**
		 * Returnera medelandet ur en nod
		 * @return medelandet ur en nod
		 */
		public Message getMsg(){
			return this.m;
		}
	}
}
