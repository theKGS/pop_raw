package raw.java.map.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Johan Wrapper class for a thread pool executor {@see
 *         ThreadPoolExecutor}
 */
public class MessageThreadExecutor {
	private BlockingQueue<Runnable> mWorksQueue;

	private RejectedExecutionHandler executionHandler;

	private ThreadPoolExecutor executor;

	/**
	 * Creates a new instance of a MessageThreadExecutor
	 * 
	 * @param queueSize
	 *            maximum number of queued jobs
	 * @param corNoThread
	 *            preferred number of threads when low load
	 * @param maxNoThread
	 *            maximum number of threads
	 * @param timeOut
	 *            threads will be released if they've been idle in timeOut
	 *            seconds
	 */
	public MessageThreadExecutor(int queueSize, int corNoThread,
			int maxNoThread, int timeOut) {
		mWorksQueue = new ArrayBlockingQueue<Runnable>(queueSize);
		executionHandler = new MyRejectedExecutionHandelerImpl();

		executor = new ThreadPoolExecutor(corNoThread, maxNoThread, timeOut,
				TimeUnit.SECONDS, mWorksQueue, executionHandler);
		executor.allowCoreThreadTimeOut(true);
//		executor.prestartAllCoreThreads();
		
	}

	/**
	 * Adds the specified runnable to the job queue
	 * 
	 * @param r
	 *            the runnable to be queued.
	 */
	public void execute(Runnable r) {
		executor.execute(r);
		//log();

	}

	private static int counter = 0;
	
	@SuppressWarnings("unused")
    public void log(boolean override) {
		if ((counter++) % 100 == 0 || override) {
			StringBuilder sb = new StringBuilder();
			sb.append("Thread logging--------------------------------------------\n");
			sb.append("Completed threads: ");
			sb.append(executor.getCompletedTaskCount());
			sb.append("\nMaximum number of threads used so far: ");
			sb.append(executor.getLargestPoolSize());
			sb.append("\nAprox number of tasks scheduled: ");
			sb.append(executor.getTaskCount());
			sb.append("\nAprox number of tasks active: ");
			sb.append(executor.getActiveCount());
			sb.append("\nJobs in queue");
			sb.append(mWorksQueue.size());
			sb.append("\n-----------------------------------------------------------");
			System.out.println(sb.toString());
			counter = 1;
		}

	}
	public void flush(){
	    mWorksQueue.clear();
	    
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	    
	}
}
