package raw.java.j_int_java;

import static org.junit.Assert.*;

import org.junit.Test;

import raw.java.map.MapNode;

public class SendMessageTest {

	@Test
	public void testGetMap() {
		MapNode[] arr = {new MapNode(1,2,null), new MapNode(2,3,null)};
		SendMessage msg = new SendMessage(1, null, arr);
		assertTrue(msg.getMap()[1].getGrassLevel() == 2);
		assertTrue(msg.getType() == 1);
	}

}
