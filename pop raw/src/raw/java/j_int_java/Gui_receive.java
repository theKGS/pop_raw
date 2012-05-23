package raw.java.j_int_java;

import java.io.IOException;
import com.ericsson.otp.erlang.*;

/**
 * @author group 8
 */
public class Gui_receive implements Runnable{
	private String nodeName = "athens";
	private String mboxName = "sparta";
	private String cookie = "thisissparta";
	private OtpMbox mbox;
	private FIFO queue;
	private Communicator com;

	/**
	 * Makes a Gui_receive object, this object waits for an incoming 
	 * transmission containing an OtpErlangTuple where the first object in the 
	 * tuple is an OtpErlangPid. The constructor calls the init() method last.
	 * @param send The queue where to send incoming messages.
	 * @param com A Communicator object, has to have the method 
	 * setPid(OtpErlangPid).
	 */
	public Gui_receive(FIFO send, Communicator com) {
		this.queue = send;
		this.com = com;
		
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the node and creates a mailbox for that node. 
	 * @throws IOException Exception if the node could not be made.
	 */
	private void init() throws IOException{
		OtpNode self = new OtpNode(this.nodeName, this.cookie);
		mbox = self.createMbox(this.mboxName);
	}

	/**
	 * The runnable code. It first listens for 1 message containing an 
	 * OtpErlangTuple where the first item is an OtpErlangPid (any other parts
	 * of the tuple are ignored). When this is received the setPid method in 
	 * the Communication object (sent in the constructor) is called with the
	 * PID. When this is done run() will listen to incoming messages.
	 */
	public void run() {
		try {
			OtpErlangObject tempO = mbox.receive();
			if (tempO instanceof OtpErlangTuple) {
				OtpErlangTuple tupleTemp = (OtpErlangTuple) tempO;
				if (tupleTemp.elementAt(0) instanceof OtpErlangPid) {
					com.setPid((OtpErlangPid) tupleTemp.elementAt(0));
				} else {
					System.out.println("No Erlang pid");
				}
			} else {
				System.out.println("No Erlang tuple");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		while(true) {
			try {
				OtpErlangObject o = mbox.receive();
				if(o instanceof OtpErlangTuple) {
					OtpErlangTuple msg = (OtpErlangTuple) o;
					Message message = deCode(msg);
					queue.put(message);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to decode messages from OtpErlangTuple to Message object. The
	 * OtpErlangObject has to be an OtpErlangTuple where the item at 0 is an
	 * OtpErlangLong, the item at 1 is an OtpErlangPid and any additional items
	 * is OtpErlangLong.
	 * @param msg An OtpErlangTuple where the first item is an OtpErlangLong, 
	 * the second item is an OtpErlangPid and any other items is OtpErlangLong.
	 * @return A Message object containing the data in the OtpErlangTuple.
	 * @throws OtpErlangRangeException Thrown if the int values are to big.
	 */
	private Message deCode(OtpErlangTuple msg) throws OtpErlangRangeException {
		int sType = -1;
		OtpErlangPid pidTemp = null;
		int size = msg.arity();
		int[] values = new int[size - 2];
		OtpErlangObject type = msg.elementAt(0);
		OtpErlangObject pid = msg.elementAt(1);
		if (type instanceof OtpErlangLong) {
			OtpErlangLong tempType = (OtpErlangLong) type;
			sType = tempType.intValue();
		}
		if (pid instanceof OtpErlangPid) {
			pidTemp = (OtpErlangPid) pid;
		}
		for (int i = 2; i <= size; i++) {
			if (msg.elementAt(i) instanceof OtpErlangLong) {
				values[i-2] = ((OtpErlangLong) msg.elementAt(i)).intValue(); 
			}
		}
		return new Message(sType, pidTemp, values);
	}
}
