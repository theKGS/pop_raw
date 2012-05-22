package raw.java.map.threadpool;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import message_handelrs.MsgHandler;


public class MyRejectedExecutionHandelerImpl implements
		RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable runnable, ThreadPoolExecutor executor) {
		System.out.println("Message rejected: ");
	}
}
