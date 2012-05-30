package raw.java.j_int_java;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import raw.java.map.MapNode;

import com.ericsson.otp.erlang.*;

public class CommunicatorTest {

	@Test
	public void testSendingAndReceive() {
//		SETUP
		Communicator com = new Communicator();
		MessageSuper msg;

		MapNode node1 = new MapNode(3, MapNode.NONE, null);
		MapNode node2 = new MapNode(3, MapNode.RABBIT, null);
		MapNode node3 = new MapNode(3, MapNode.WOLF, null);
		MapNode[] map = {node1, node2, node3};
		
		Message msgNew = new Message(42, com.getPid(), null);
		com.send(msgNew);
		msg = com.receive();
		assertTrue(msg.getType() == 4);
		OtpErlangPid pid = msg.getPid();
		
		int[] values1 = {1,2,3,4,5,6};
		int[] values2 = {1,2};
//		SETUP ENDS
		
//		0 = rabbitMap, 3
		SendMessage msg0 = new SendMessage(0, pid, map);
		com.send(msg0);
		msg = com.receive();
		assertTrue(msg.getType() == 2);
		assertTrue(Arrays.equals(((Message) msg).getValues(), values1));
		
//		5 = death, 2
		Message msg5 = new Message(5, pid, null);
		com.send(msg5);
		msg = com.receive();
		assertTrue(msg.getType() == 5);
		assertTrue(Arrays.equals(((Message) msg).getValues(), values2));
		
//		7 = wolfMap, 3
		SendMessage msg7 = new SendMessage(7, pid, map);
		com.send(msg7);
		msg = com.receive();
		assertTrue(msg.getType() == 6);
		assertTrue(Arrays.equals(((Message) msg).getValues(), values1));
		/*
//		9 = yes, 2
		Message msg9 = new Message(9, pid, null);
		
//		10 = no, 2
		Message msg10 = new Message(10, pid, null);
		
//		11 = start, 2
		Message msg11 = new Message(11, pid, null);
		
//		12 = eatMove, 2
		Message msg12 = new Message(12, pid, null);
		*/
	}

//	@Test
//	public void testFlush() {
//		fail("Not yet implemented");
//	}
}