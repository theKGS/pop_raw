package raw.java.j_int_java;

import raw.java.map.MapNode;

public class SendMessage {
	private final String type;
	private MapNode[][] map;
	public SendMessage(String type, MapNode[][] map){
		this.type = type;
		this.map = map;
	}
}
