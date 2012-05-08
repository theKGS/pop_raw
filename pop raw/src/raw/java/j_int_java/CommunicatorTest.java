package raw.java.j_int_java;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommunicatorTest {

	@Test
	public void testCommunicator() {
		fail("Not yet implemented");
	}

	@Test
	public void testSend() {
		Communicator com = new Communicator();
		Message msg1 = new Message("abba", 42);
		Message msg2 = new Message("abbadabba", 56);

		com.send(msg1);
		com.send(msg2);
//		System.out.println("waiting1");
//		System.out.println(com.receive().getType());
//		System.out.println("waiting2");
//		System.out.println(com.receive().getType());
	}

	@Test
	public void testReceive() {
		fail("Not yet implemented");
	}

}
