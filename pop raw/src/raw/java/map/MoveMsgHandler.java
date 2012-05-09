package raw.java.map;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MoveMsgHandler implements Runnable {
	
		final OtpErlangPid pid;
		private int[] coords;
		private Communicator mErlCom;
		private MapNode[][] mapArray;

		public MoveMsgHandler(Message message, Communicator mErlCom, MapNode[][] mapArray) {
			this.pid = message.getPid();
			this.coords = message.getValues();
			this.mapArray = mapArray;
			this.mErlCom = mErlCom;
		}

		@Override
		public void run() {
			if (compareCoordsArr(coords)) {
				synchronized(mapArray[coords[0]][coords[1]]){
					synchronized(mapArray[coords[2]][coords[3]]){
						checkMove();
					}
				}
			} else {
				synchronized(mapArray[coords[2]][coords[3]]){
					synchronized(mapArray[coords[0]][coords[1]]){
						checkMove();
					}
				}
			}

		}
		/**
		 * Method that get the index of both coordinate pairs and returns the
		 * smallest one
		 * 
		 * @param coords
		 *            int[4] with {x1,y1,x,y2} coords
		 * @return coord-pair with the smallest index
		 */
		private boolean compareCoordsArr(int[] coords) {
			return coords[1] * mapArray.length + coords[0] + mapArray.length < coords[3]
					* mapArray.length + coords[2] + mapArray.length ? true : false;

		}
		private void checkMove(){
			MapNode currentNode = mapArray[coords[0]][coords[1]];
			MapNode targetNode = mapArray[coords[2]][coords[3]];
			if(targetNode.getType() != MapNode.NONE){
				mErlCom.send(new SendMessage("no", null, pid));
			} else {
				
				
				targetNode.setPid(currentNode.getPid());
				targetNode.setType(currentNode.getType());
				currentNode.setPid(null);
				currentNode.setType(MapNode.NONE);
				mErlCom.send(new SendMessage("yes",null,pid));
				
			}
			//System.out.println("Move handled");
		}
}
