package raw.java.j_int_java;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.ericsson.otp.erlang.*;

public class CommunicatorTest {

	@Test
	public void testSendingAndReceiving() {
		// TEST SETUP START
		String nodeName = "test";
		String mboxName = "test";
		String cookie = "thisissparta";
		
		OtpNode self = null;
		try {
			self = new OtpNode(nodeName, cookie);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OtpMbox mbox = self.createMbox(mboxName);
		
		OtpErlangPid pid = null;
		
		try {
			OtpErlangObject tempO = mbox.receive();
			if (tempO instanceof OtpErlangTuple) {
				OtpErlangTuple tupleTemp = (OtpErlangTuple) tempO;
				if (tupleTemp.elementAt(0) instanceof OtpErlangPid) {
					pid = (OtpErlangPid) tupleTemp.elementAt(0);
				} else {
					System.out.println("No Erlang pid");
				}
			} else {
				System.out.println("No Erlang tuple");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// TEST SETUP END
		
		Communicator com = new Communicator();
		
		// starta erlang med 'make start'!
		
//		SendMessage msg0 = new SendMessage()
//		Message msg4 = new Message(4, com.getPid(), )
		Message msg5 = new Message(5, pid, null);
//		SendMessage msg7 = new SendMessage()
//		Message msg8 = 
//		Message msg9 = 
//		Message msg10 = 
//		Message msg11 = 
//		Message msg12 = 
		
		com.send(msg5);
//		System.out.println("before test");
//		MessageSuper msg = com.receive();
//		assertTrue(msg.getType() == 5);
//		System.out.println("after test");
	}
//	
//	@Test
//	public void testCommunicator() {
//		fail("Not yet implemented");
//	}
//	
//	@Test
//	public void testSend() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetSend() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testReceive() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testFlush() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPutReceive() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPid() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetPid() {
//		fail("Not yet implemented");
//	}

}
