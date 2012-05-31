package raw.java.j_int_java;

import static org.junit.Assert.*;

import org.junit.Test;

public class FIFOTest {

	@Test
	public void testGetAndPut() {
		FIFO queue = new FIFO();
		int[] arr = {1,2};
		Message msg = new Message(1, null, arr);
		queue.put(msg);
		assertTrue(queue.get().getType() == 1);
	}

	@Test
	public void testHasNext() {
		FIFO queue = new FIFO();
		int[] arr = {1,2};
		Message msg = new Message(1, null, arr);
		queue.put(msg);
		assertTrue(queue.hasNext());
	}

}
