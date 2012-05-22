package raw.java.j_int_java;

import java.io.IOException;
import raw.java.map.MapNode;
import com.ericsson.otp.erlang.*;

public class Gui_send implements Runnable{
	private FIFO queue;
	private String nodeName = "rawsender";
	private String cookie = "thisissparta";
	private OtpMbox mbox;
	private OtpErlangPid pid;

	public Gui_send(FIFO queue, OtpErlangPid pid) {
		this.queue = queue;
		this.pid = pid;

		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws IOException {
		OtpNode node = new OtpNode(nodeName, cookie);
		mbox = node.createMbox();
	}

	public void run() {
		while(true) {
			OtpErlangTuple message = enCode(queue.get());
			mbox.send(this.pid , message);
		}
	}

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
			OtpErlangObject[] list = new OtpErlangObject[9];
			for (int i = 0; i < 9; i++) {
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
