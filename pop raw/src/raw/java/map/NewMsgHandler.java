package raw.java.map;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class NewMsgHandler extends MsgHandler implements Runnable {

	public NewMsgHandler(Message msg, Communicator mErlCom, Map map) {
		super(msg, mErlCom, map, null);
	}

	@Override
	public void run() {
		synchronized (map.getMapArray()[coords[0]][coords[1]]) {
			map.getMapArray()[coords[0]][coords[1]].setPid(pid);
			mErlCom.send(new Message("start", pid, null));
		}
	}
}
