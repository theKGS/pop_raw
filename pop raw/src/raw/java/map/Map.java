package raw.java.map;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;
import raw.java.map.threadpool.MessageThreadExecutor;

import com.ericsson.otp.erlang.*;

public class Map extends Thread {

	// mpS - map size
	// mpG - ammount of grass
	// mpSG - speed of grass growth
	// wfN - number of wolves
	// wfA - maximum wolf age
	// wfRA - wolf reproduction age
	// wfRS - wolf reproduction success probability
	// raN - number of rabbits
	// raA - maximum rabbit age
	// raRA - rabbit reproduction age
	// raRS - rabbit reproduction success probability
	private int simulationSpeed = 0;

	public int getSimulationSpeed() {
		return simulationSpeed;
	}

	public void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
	}

	private int mapSize = 0;
	private int amountOfGrass = 0;
	private int speedOfGrassGrowth = 0;
	private int numberOfWolves = 0;
	private int maxWolfAge = 0;
	private int wolfReprAge = 0;
	private int woldReprSuccessProb = 0;
	private int numberOfRabbits = 0;
	private int maxRabbitAge = 0;
	private int rappitReprAge = 0;
	private int rabbitReprSuccessProb = 0;

	MapNode[][] mapArray;
	private boolean running = true;
	private boolean paused = true;
	private Communicator mErlCom;

	private MessageThreadExecutor mMsgThrExec;
	private Message nextMessage;

	public Map(int Size, int Seed) {
		this.mapSize = Size;
		mapArray = new MapNode[Size][Size];
		for (int i = 0; i < mapArray.length;i++) {
			for (int j = 0; j < mapArray[i].length;j++) {
				mapArray[i][j] = new MapNode(i*j%5, (i*j^(i+j))%3, null);
			}
		}
		for(MapNode[] tArr : mapArray){
			for(MapNode tMapNode : tArr){
				System.out.print("(" + tMapNode.getGrassLevel() + ", " + tMapNode.getType() + ")");
			}
			System.out.print("\n");
		}
		setUp();
	}

	private void setUp() {
		mErlCom = new Communicator();
		mErlCom.inComming.put(new Message("move", null, new int[]{0,0,0,1}));
		mMsgThrExec = new MessageThreadExecutor(5, 10, 20, 10);
	}

	@Override
	public void run() {
		super.run();
		while (running) {
			if (!paused) {
				continue;
			}
			
			System.out.println("Getting next message");
			handleNextMessage();

		}
	}

	private void handleNextMessage() {
		nextMessage = mErlCom.receive();
		if (nextMessage.getType().equalsIgnoreCase("get")) {
			mMsgThrExec.execute(new MapMsgHandler(nextMessage.getPid()));
		} else if (nextMessage.getType().equalsIgnoreCase("move")) {
			mMsgThrExec.execute(new MoveMsgHandler(nextMessage.getPid(), nextMessage.getValues()));
		}
	}

	class MapMsgHandler implements Runnable {
		final OtpErlangPid pid;

		public MapMsgHandler(OtpErlangPid pid) {
			this.pid = pid;
		}

		@Override
		public void run() {
			mErlCom.send(new SendMessage("map", mapArray, pid));
		}
	}

	class MoveMsgHandler implements Runnable {
		final OtpErlangPid pid;
		private int[] coords;

		public MoveMsgHandler(OtpErlangPid pid, int[] coords) {
			this.pid = pid;
			this.coords = coords;
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
					synchronized(mapArray[coords[1]][coords[0]]){
						checkMove();
					}
				}
			}

		}
		private void checkMove(){
			MapNode currentNode = mapArray[coords[1]][coords[2]];
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
			System.out.println("Move handled");
		}

	}
	
	class EatMsgHandler implements Runnable {
		OtpErlangPid pid;
		int[] coords;
		public EatMsgHandler(OtpErlangPid pid, int[] coords){
			this.pid = pid;
			this.coords = coords;
		}
		@Override
		public void run() {
			
			synchronized(mapArray[coords[0]][coords[1]]){
				MapNode tNode = mapArray[coords[0]][coords[1]];
				tNode.setGrassLevel(tNode.getGrassLevel());
				mErlCom.send(new SendMessage("yes", null, pid));
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
	public boolean compareCoordsArr(int[] coords) {
		return coords[1] * mapArray.length + coords[0] + mapArray.length < coords[3]
				* mapArray.length + coords[2] + mapArray.length ? true : false;

	}

	public void simulationStart() {

	}

	public void simulationStop() {

	}

	public void simulationReset() {

	}

	public int getMapSize() {
		return mapSize;
	}

	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}

	public int getAmountOfGrass() {
		return amountOfGrass;
	}

	public void setAmountOfGrass(int amountOfGrass) {
		this.amountOfGrass = amountOfGrass;

	}

	public int getSpeedOfGrassGrowth() {
		return speedOfGrassGrowth;

	}

	public void setSpeedOfGrassGrowth(int speedOfGrassGrowth) {
		this.speedOfGrassGrowth = speedOfGrassGrowth;

	}

	public int getNumberOfWolves() {
		return numberOfWolves;

	}

	public void setNumberOfWolves(int numberOfWolves) {
		this.numberOfWolves = numberOfWolves;

	}

	public int getMaxWolfAge() {
		return maxWolfAge;

	}

	public void setMaxWolfAge(int maxWolfAge) {
		this.maxWolfAge = maxWolfAge;

	}

	public int getWolfReprAge() {
		return wolfReprAge;

	}

	public void setWolfReprAge(int wolfReprAge) {
		this.wolfReprAge = wolfReprAge;

	}

	public int getWoldReprSuccessProb() {
		return woldReprSuccessProb;

	}

	public MapNode[][] getMapArray() {
		return mapArray;
	}

	public void setMapArray(MapNode[][] mapArray) {
		this.mapArray = mapArray;
	}

	public void setWoldReprSuccessProb(int woldReprSuccessProb) {
		this.woldReprSuccessProb = woldReprSuccessProb;
	}

	public int getNumberOfRabbits() {
		return numberOfRabbits;
	}

	public void setNumberOfRabbits(int numberOfRabbits) {
		this.numberOfRabbits = numberOfRabbits;
	}

	public int getMaxRabbitAge() {
		return maxRabbitAge;
	}

	public void setMaxRabbitAge(int maxRabbitAge) {
		this.maxRabbitAge = maxRabbitAge;
	}

	public int getRappitReprAge() {
		return rappitReprAge;
	}

	public void setRappitReprAge(int rappitReprAge) {
		this.rappitReprAge = rappitReprAge;
	}

	public int getRabbitReprSuccessProb() {
		return rabbitReprSuccessProb;
	}

	public void setRabbitReprSuccessProb(int rabbitReprSuccessProb) {
		this.rabbitReprSuccessProb = rabbitReprSuccessProb;
	}
}