package raw.java.map;

import raw.java.map.threadpool.MessageThreadExecutor;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map map = new Map(80, 32, null);
		map.start();
	}

}
