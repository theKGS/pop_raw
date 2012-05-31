package raw.java.j_int_java;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class MessageTest {

	@Test
	public void testGetValuesAndType() {
		int[] arr1 = {1,2,3};
		int[] arr2 = {1,2,3};
		Message msg = new Message(1, null, arr1);
		assertTrue(Arrays.equals(msg.getValues(), arr2));
		assertTrue(msg.getType() == 1);
	}
}