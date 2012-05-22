package raw.java.map;

import java.util.Stack;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class MessagePool {

	private Stack<NewMsgHandler> newStack;
	private Stack<MoveMsgHandler> moveStack;
	private Stack<MapMsgHandler> mapStack;
	private Stack<EatMsgHandler> eatStack;

	public MessagePool() {
		eatStack = new Stack<EatMsgHandler>();
		mapStack = new Stack<MapMsgHandler>();
		moveStack = new Stack<MoveMsgHandler>();
		newStack = new Stack<NewMsgHandler>();
	}

	public EatMsgHandler getEatRunnable(Message msg, Communicator mErlCom,
			Map map, UpdateListener updtLis) {
		synchronized (eatStack) {
			if (!eatStack.isEmpty()) {
				EatMsgHandler temp = eatStack.pop();
				temp.setCoords(msg.getValues());
				temp.setPid(msg.getPid());
				return temp;
			} else {
				return new EatMsgHandler(msg, mErlCom, map, updtLis, this);
			}
		}
	}

	public void AddEatToStack(EatMsgHandler eatMsg) {
		synchronized(eatStack){
			eatStack.push(eatMsg);
		}
	}
	
	public MapMsgHandler getMapRunnable(Message msg, Communicator mErlCom,
			Map map, UpdateListener updtLis) {
		synchronized (mapStack) {
			if (!mapStack.isEmpty()) {
				MapMsgHandler temp = mapStack.pop();
				temp.setCoords(msg.getValues());
				temp.setPid(msg.getPid());
				return temp;
			} else {
				return new MapMsgHandler(msg, mErlCom, map, this);
			}
		}
	}
	public void AddMapToStack(MapMsgHandler mapMsg) {
		synchronized(mapStack){
			mapStack.push(mapMsg);
		}
	}
	
	public MoveMsgHandler getMoveRunnable(Message msg, Communicator mErlCom,
			Map map, UpdateListener updtLis) {
		synchronized (moveStack) {
			if (!moveStack.isEmpty()) {
				MoveMsgHandler temp = moveStack.pop();
				temp.setCoords(msg.getValues());
				temp.setPid(msg.getPid());
				return temp;
			} else {
				return new MoveMsgHandler(msg, mErlCom, map, updtLis, this);
			}
		}
	}
	public void AddMoveToStack(MoveMsgHandler moveMsg) {
		synchronized(moveStack){
			moveStack.push(moveMsg);
		}
	}
	
	
	public NewMsgHandler getNewRunnable(Message msg, Communicator mErlCom,
			Map map, UpdateListener updtLis) {
		synchronized (newStack) {
			if (!newStack.isEmpty()) {
				NewMsgHandler temp = newStack.pop();
				temp.setCoords(msg.getValues());
				temp.setPid(msg.getPid());
				return temp;
			} else {
				return new NewMsgHandler(msg, mErlCom, map, this);
			}
		}
	}
	public void AddNewToStack(NewMsgHandler newMsg) {
		synchronized(newStack){
			newStack.push(newMsg);
		}
	}
}
