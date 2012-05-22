package raw.java.map;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MapNode implements Lock
{
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

    private int type;

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    private OtpErlangPid pid;

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
    public void clearNode(){
        this.type = MapNode.NONE;
        this.pid = null;
    }
    public void takeDataFrom(MapNode node){
        type = node.getType();
        pid = node.getPid();
    }

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
        // TODO Auto-generated method stub

    }

    @Override
    public Condition newCondition()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean tryLock()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit)
            throws InterruptedException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public synchronized void unlock()
    {
        isLocked = false;
        notify();
    }
}
