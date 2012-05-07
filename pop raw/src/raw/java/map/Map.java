package raw.java.map;

import java.util.ArrayList;
import com.ericsson.otp.erlang.*;
public class Map {
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

	public Map(int Size, int Seed) {
		mapArray = new MapNode[Size][Size];
		for (MapNode[] tMapNodeArr : mapArray) {
			for (MapNode tMapNode : tMapNodeArr) {
				tMapNode = new MapNode();
			}
		}
		//OtpErlangP
	}

	public void start() {

	}

	public void stop() {

	}

	public void reset() {

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