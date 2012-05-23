package raw.java.j_int_java;

/**
 * FIFO queue.
 * @author group 8
 */
public class FIFO {
	private Node first;
	private Node last;
	
	/**
	 * Initializes the queue in an empty state.
	 */
	public FIFO() {
		this.first = null;
		this.last = null;
	}
	/**
	 * A method to return the first message from the queue.
	 * @return Returns the first message from the queue.
	 */
	public synchronized MessageSuper get() {
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
	 * Puts a message in the queue.
	 * @param m Message m has to be of type MessageSuper.
	 */
	public synchronized void put(MessageSuper m) {
		Node ny = new Node(m, null);

		if (last == null) {
			last = ny;
			first = ny;
		} else {
			last.setNext(ny);
			this.last = ny;
		}
		notifyAll();
	}

	/**
	 * Method to check if there's messages in the queue.
	 * @return Returns true if there is messages, false if there isn't.
	 */
	public boolean hasNext() {
		if (this.first != null) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Subclass Node, not reachable from outside FIFO. Handles the Nodes in the list.
	 * @author  group 8
	 *
	 */
	private class Node {
		private MessageSuper m;
		private Node next;
		
		/**
		 * Constructor, takes a MessageSuper and a Node.
		 * @param m MessageSuper
		 * @param next Node
		 */
		public Node(MessageSuper m, Node next) {
			this.m = m;
			this.next = next;
		}
		/**
		 * Sets a nodes next variable to n. 
		 * @param n The Node to set the next variable to.
		 */
		public void setNext(Node n) {
			this.next = n;
		}
		/**
		 * Gets a Nodes next value.
		 * @return The nodes next value.
		 */
		public Node getNext() {
			return this.next;
		}
		/**
		 * Returns the message from a node
		 * @return The message from a node.
		 */
		public MessageSuper getMsg(){
			return this.m;
		}
	}
}
