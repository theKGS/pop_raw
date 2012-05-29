package raw.java.map;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MapNode implements Lock
{

    private int type;
    /**
     * int representing an empty square value: 0
     */
    public static final int NONE = 0;
    /**
     * int representing rabbit value: 1
     */
    public static final int RABBIT = 1;
    /**
     * int representing an wolf value: 2
     */
    public static final int WOLF = 2;
    private int grassLevel;
    private OtpErlangPid pid;
    private boolean isLocked = false;

    public int getGrassLevel()
    {
        return grassLevel;
    }

    public void setGrassLevel(int grassLevel)
    {
        this.grassLevel = grassLevel;
    }

    public OtpErlangPid getPid()
    {
        return pid;
    }

    public void setPid(OtpErlangPid pid)
    {
        this.pid = pid;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * Constructor for the MapNode.
     * 
     * @param grassLevel
     *            the level of grass for this square
     * @param type
     *            the type that populates this square
     * @param pid
     *            the pid of the entity populating this square (null if it is
     *            empty)
     */
    public MapNode(int grassLevel, int type, OtpErlangPid pid)
    {
        this.grassLevel = grassLevel;
        this.type = type;
        this.pid = pid;

    }

    /**
     * Clear the node, setting pid to null and type to NONE
     */
    public void clearNode()
    {
        this.type = MapNode.NONE;
        this.pid = null;
    }

    /**
     * Copies the data from another node
     * 
     * @param node
     *            the node to copy data from
     */
    public void takeDataFrom(MapNode node)
    {
        type = node.getType();
        pid = node.getPid();
    }
    /**
     * Locks the node.
     */
    @Override
    public synchronized void lock()
    {
        while (isLocked)
        {
            try
            {

                wait();
            } catch (InterruptedException e)
            {

                e.printStackTrace();
            }
        }
        isLocked = true;

    }

    @Override
    public void lockInterruptibly() throws InterruptedException
    {
       

    }

    @Override
    public Condition newCondition()
    {
       
        return null;
    }

    @Override
    public boolean tryLock()
    {
     
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit)
            throws InterruptedException
    {
     
        return false;
    }
    /**
     * Unlocks the node.
     */
    @Override
    public synchronized void unlock()
    {
        isLocked = false;
        notify();
    }
}
