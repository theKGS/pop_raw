package raw.java.map;

import raw.java.map.threadpool.MessageThreadExecutor;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MessageThreadExecutor t = new MessageThreadExecutor(4, 5, 10, 10);
		for (int i = 0; i < 10; i++)
			t.execute(new Runnable() {

				@Override
				public void run() {
					System.out.println("Thread!!!");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
	}

}
