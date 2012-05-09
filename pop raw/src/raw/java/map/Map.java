package raw.java.map;

import java.util.Random;

import raw.java.gui.Main;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.map.threadpool.MessageThreadExecutor;

public class Map extends Thread {
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
	private boolean paused = false;
	private Communicator mErlCom;

	private MessageThreadExecutor mMsgThrExec;
	private Message nextMessage;

	/**
	 * Constructor for the map class
	 * 
	 * @param Size
	 *            the y, and x size of the map
	 * @param Seed
	 *            seed for generating maps
	 */
	public Map(int Size, long Seed, Main main) {
		this.mapSize = Size;
		Random r = new Random(Seed);
		mapArray = new MapNode[Size][Size];
		for (int i = 0; i < mapArray.length; i++) {
			for (int j = 0; j < mapArray[i].length; j++) {
				mapArray[i][j] = new MapNode(r.nextInt(6), r.nextInt(3), null);

			}

		}

		// printMap();
		setUp();
	}

	/**
	 * Prints the map to the console
	 */
	private void printMap() {
		for (int y = 0; y < mapSize; y++) {
			for (int x = 0; x < mapSize; x++) {
				System.out.print(mapArray[x][y].getType() + " ");
			}
			System.out.print("\n");
		}
	}

	/**
	 * Sets up Communicator and thread pool.
	 */
	private void setUp() {
		mErlCom = new Communicator();
		mErlCom.putReceive(new Message("move", null, new int[] { 1, 0, 0, 0 }));
		mErlCom.putReceive(new Message("move", null, new int[] { 0, 0, 0, 1 }));
		mErlCom.putReceive(new Message("move", null, new int[] { 1, 1, 1, 0 }));
		mErlCom.putReceive(new Message("move", null, new int[] { 1, 0, 0, 0 }));
		mErlCom.putReceive(new Message("stop", null, null));

		mMsgThrExec = new MessageThreadExecutor(5, 10, 20, 10);
	}

	/**
	 * Main loop that gets messages and delegates work to thread pool
	 */
	@Override
	public void run() {
		super.run();
		while (running) {
			if (paused) {
				continue;
			}

			System.out.println("Getting next message");
			printMap();
			handleNextMessage();
			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Starts the correct Runnable according to the current message.
	 */
	private void handleNextMessage() {
		nextMessage = mErlCom.receive();
		if (nextMessage.getType().equalsIgnoreCase("get")) {
			mMsgThrExec.execute(new MapMsgHandler(nextMessage.getPid(),
					mErlCom, mapArray));
		} else if (nextMessage.getType().equalsIgnoreCase("move")) {
			mMsgThrExec.execute(new MoveMsgHandler(nextMessage, mErlCom,
					mapArray));
		} else if (nextMessage.getType().equalsIgnoreCase("stop")) {
			System.out.println("stopping");
			this.running = false;
			// System.exit(0);
		}
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