package raw.java.j_int_java;

import com.ericsson.otp.erlang.*;

/**
 * Object handling the Java side of the Java-Erlang communication. For the 
 * Erlang side see jint_rec and jint_send.
 * @author group 8
 *
 */
public class Communicator {
	private FIFO outGoing = new FIFO();
	private FIFO inComing = new FIFO();
	private Gui_send sender;
	private Gui_receive receiver;
	private OtpErlangPid pid = null;
	
	/**
	 * Creating the Java side of the communication. It will create a receive 
	 * object, then wait for an incomming message containing the PID of the 
	 * receiver. This message has to have the form OtpErlangTuple and the item
	 * at location 0 has to be an OtpErlangPid. Anything else the tuple 
	 * contains will be disregarded.
	 */
	public Communicator() {
		receiver = new Gui_receive(inComing, this);
		new Thread(receiver).start();
		while (this.pid == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sender = new Gui_send(outGoing, pid);
		new Thread(sender).start();
	}
	
	/**
	 * Send a message, messages are sent as fast as possible. No confirmations
	 * are given.
	 * @param m A message of type MessageSuper
	 */
	public void send(MessageSuper m) {
		outGoing.put(m);
	}
	
	/**
	 * Get the message next in line to be sent. This removes the message and it
	 * will not be sent.
	 * @return The first message from the send queue.
	 */
	public MessageSuper getSend() {
		return outGoing.get();
	}
	
	/**
	 * Get the first received message.
	 * @return The first unread message received.
	 */
	public MessageSuper receive() {
		return inComing.get();
	}
	
	/**
	 * Method to clear the queues of messages. All incoming and outgoing 
	 * messages will be removed but not read.
	 */
	public void flush() {
		while (this.outGoing.hasNext()) {
			this.outGoing.get();
		}
		while (this.inComing.hasNext()) {
			this.inComing.get();
		}
	}
	
	/**
	 * Put a message as received. It will be put as the last message.
	 * @param m A MessageSuper object.
	 */
	public void putReceive(MessageSuper m) {
		inComing.put(m);
	}
	
	/**
	 * Get the PID to whom Communicator send the messages.
	 * @return The PID on the form OtpErlangPid.
	 */
	public OtpErlangPid getPid() {
		return this.pid;
	}
	
	/**
	 * Set the pid to the included pid.
	 * @param pid The PID to set the value, has to be an OtpErlangPid object.
	 */
	public void setPid(OtpErlangPid pid) {
		this.pid = pid;
	}
}
