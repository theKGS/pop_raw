package raw.java.map;

import java.util.ArrayList;

public class Map {
//	mpS		- map size
//	mpG		- ammount of grass
//	mpSG	- speed of grass growth
//	wfN		- number of wolves
//	wfA		- maximum wolf age
//	wfRA	- wolf reproduction age
//	wfRS	- wolf reproduction success probability
//	raN		- number of rabbits
//	raA		- maximum rabbit age
//	raRA	- rabbit reproduction age
//	raRS	- rabbit reproduction success probability
	private int mpS = 0;
	private int mpG = 0;
	private int mpSG = 0;
	private int wfN = 0;
	private int wfA = 0;
	private int wfRA = 0;
	private int wfRS = 0;
	private int raN = 0;
	private int raA = 0;
	private int raRA = 0;
	private int raRS = 0;
	
	MapNode[][] mapArray;
	public Map(int Size, int Seed) {
		mapArray = new MapNode[Size][Size];
	}
	public void start(){
		
	}
	public void stop(){
		
	}
	public void reset(){
		
	}
	public int getMpS() {
		return mpS;
	}
	public void setMpS(int mpS) {
		this.mpS = mpS;
	}
	public int getMpG() {
		return mpG;
	}
	public void setMpG(int mpG) {
		this.mpG = mpG;
	}
	public int getMpSG() {
		return mpSG;
	}
	public void setMpSG(int mpSG) {
		this.mpSG = mpSG;
	}
	public int getWfN() {
		return wfN;
	}
	public void setWfN(int wfN) {
		this.wfN = wfN;
	}
	public int getWfA() {
		return wfA;
	}
	public void setWfA(int wfA) {
		this.wfA = wfA;
	}
	public int getWfRA() {
		return wfRA;
	}
	public void setWfRA(int wfRA) {
		this.wfRA = wfRA;
	}
	public int getWfRS() {
		return wfRS;
	}
	public void setWfRS(int wfRS) {
		this.wfRS = wfRS;
	}
	public int getRaN() {
		return raN;
	}
	public void setRaN(int raN) {
		this.raN = raN;
	}
	public int getRaA() {
		return raA;
	}
	public void setRaA(int raA) {
		this.raA = raA;
	}
	public int getRaRA() {
		return raRA;
	}
	public void setRaRA(int raRA) {
		this.raRA = raRA;
	}
	public int getRaRS() {
		return raRS;
	}
	public void setRaRS(int raRS) {
		this.raRS = raRS;
	}
}
