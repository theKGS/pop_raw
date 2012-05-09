package raw.java.map.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 
 * @author Johan
 * Wrapper class for a thread pool executor {@see ThreadPoolExecutor}
 */
public class MessageThreadExecutor {
	private BlockingQueue<Runnable> mWorksQueue;
	
	private RejectedExecutionHandler executionHandler;

	private ThreadPoolExecutor executor;	
	/**
	 * Creates a new instance of a MessageThreadExecutor 
	 * @param queueSize maximum number of queued jobs
	 * @param corNoThread preferred number of threads when low load
	 * @param maxNoThread maximum number of threads
	 * @param timeOut threads will be released if they've been idle in timeOut seconds
	 */
	public MessageThreadExecutor(int queueSize, int corNoThread, int maxNoThread, int timeOut){
		mWorksQueue = new ArrayBlockingQueue<Runnable>(queueSize);
		executionHandler = new MyRejectedExecutionHandelerImpl();
		
		executor = new ThreadPoolExecutor(corNoThread, maxNoThread, timeOut,
		        TimeUnit.SECONDS, mWorksQueue, executionHandler);
		executor.allowCoreThreadTimeOut(true);
	}
	/**
	 * Adds the specified runnable to the job queue
	 * @param r the runnable to be queued.
	 */
	public void execute(Runnable r) {
		executor.execute(r);
	}
}
