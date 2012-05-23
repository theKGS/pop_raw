package raw.java.j_int_java;

import java.io.IOException;
import raw.java.map.MapNode;
import com.ericsson.otp.erlang.*;

/**
 * @author group 8
 */
public class Gui_send implements Runnable{
	private FIFO queue;
	private String nodeName = "rawsender";
	private String cookie = "thisissparta";
	private OtpMbox mbox;
	private OtpErlangPid pid;

	/**
	 * Makes a Gui_send object. This will loop over the queue and send all
	 * messages in the order they are received.
	 * @param queue The queue where to get the outgoing messages.
	 * @param pid The PID where to send the messages.
	 */
	public Gui_send(FIFO queue, OtpErlangPid pid) {
		this.queue = queue;
		this.pid = pid;

		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes a node and a mailbox to use for the communication.
	 * @throws IOException Exception if the node could not be made.
	 */
	public void init() throws IOException {
		OtpNode node = new OtpNode(nodeName, cookie);
		mbox = node.createMbox();
	}

	/**
	 * Runnable code, fetches messages and encode them with the enCode() 
	 * function. When messages are encoded they are sent.
	 */
	public void run() {
		while(true) {
			OtpErlangTuple message = enCode(queue.get());
			mbox.send(this.pid, message);
		}
	}

	/**
	 * Encode method to transform messages from either Message or SendMessage
	 * objects to OtpErlangTuples.
	 * @param msgReceive Either a Message or SendMessage object, both instances
	 * of MessageSuper.
	 * @return msgReceive transformed into an OtpErlangTuple.
	 */
	private OtpErlangTuple enCode(MessageSuper msgReceive) {
		OtpErlangTuple answer = null;
		if (msgReceive instanceof Message) {
			Message msg = (Message) msgReceive;
			int sType = msg.getType();
			int[] values = msg.getValues();
			int size = 0;
			if (values != null) {
				size = values.length;
			}
			OtpErlangObject[] message = new OtpErlangObject[size+2];
			message[0] = new OtpErlangInt(sType);
			if (msg.getPid() == null) {
				message[1] = new OtpErlangAtom("null");
			} else {
				message[1] = msg.getPid();
			}
			for (int i = 2; i <= size+1; i++) {
				message[i] = new OtpErlangInt(values[i-2]);
			}
			answer = new OtpErlangTuple(message);
		} else if (msgReceive instanceof SendMessage) {
			SendMessage msg = (SendMessage) msgReceive;
			int sType = msg.getType();
			MapNode[] map = msg.getMap();
			OtpErlangObject[] list = new OtpErlangObject[map.length];
			for (int i = 0; i < map.length; i++) {
				if (map[i] != null) {
					OtpErlangObject[] tuple = new OtpErlangObject[2];
					tuple[0] = new OtpErlangInt(map[i].getGrassLevel());
					int type = map[i].getType();
					if (type == MapNode.NONE) {
						tuple[1] = new OtpErlangAtom("none");
					} else if (type == MapNode.RABBIT) {
						tuple[1] = new OtpErlangAtom("rabbit");
					} else if (type == MapNode.WOLF) {
						tuple[1] = new OtpErlangAtom("wolf");
					}
					list[i] = new OtpErlangTuple(tuple);
				} else {
					OtpErlangObject[] tuple = new OtpErlangObject[2];
					tuple[0] = new OtpErlangAtom("out");
					tuple[1] = new OtpErlangAtom("out");
					list[i] = new OtpErlangTuple(tuple);
				}
			}
			OtpErlangObject[] message = new OtpErlangObject[3];
			message[0] = new OtpErlangInt(sType);
			message[1] = msg.getPid();
			message[2] = new OtpErlangList(list);
			answer = new OtpErlangTuple(message);
		}
		return answer;
	}
}
