package raw.java.map;

import java.util.ArrayList;
import java.util.Random;

import message_handelrs.MessagePool;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.MessageSuper;
import raw.java.map.threadpool.MessageThreadExecutor;

import com.ericsson.otp.erlang.OtpErlangPid;

public class Map extends Thread
{
    private int simulationSpeed = 0;

    public int getSimulationSpeed()
    {
        return simulationSpeed;
    }

    public void setSimulationSpeed(int simulationSpeed)
    {
        this.simulationSpeed = simulationSpeed;

    }

    public static final int RABBITMAP = 0;
    public static final int RABBITEAT = 1;
    public static final int MOVE = 2;
    public static final int STOP = 3;
    public static final int NEW = 4;
    public static final int DEATH = 5;
    public static final int WOLFEAT = 6;
    public static final int WOLFMAP = 7;
    public static final int NEWWOLF = 8;
    public static final int YES = 9;
    public static final int NO = 10;
    public static final int START = 11;
    public static final int EATMOVE = 12;
    public static final int WOLFMOVE = 13;

    private static int mapSize = 25;
    private static int amountOfGrass = 5;
    private static int speedOfGrassGrowth = 5000;
    private static int numberOfWolves = 0;
    private static int maxWolfAge = 0;
    private static int wolfReprAge = 8;
    private static int woldReprSuccessProb = 450;
    private static int numberOfRabbits = 0;
    private static int maxRabbitAge = 0;
    private static int rappitReprAge = 10;
    private static int rabbitReprSuccessProb = 300;

    MapNode[][] mapArray;
    private boolean running = true;
    public static boolean paused = true;
    private Communicator mErlCom;

    private MessageThreadExecutor mMsgThrExec;
    private MessageSuper nextMessage;
    private UpdateListener mUpdtLis;
    private Random r;
    private static long Seed;
    // private FakeMsgSender mFakeMsgSender;
    private GrassGrower grassGrower;
    private MessagePool messagePool;

    /**
     * Constructor for the map class
     * 
     * @param Size
     *            the y, and x size of the map
     * @param Seed
     *            seed for generating maps
     */
    public Map(int Size, long Seed, UpdateListener udpLis)
    {
        if (udpLis != null)
        {
            this.mUpdtLis = udpLis;
        }
        Map.mapSize = Size;
        Map.Seed = Seed;
        this.mErlCom = new Communicator();
        setUp();
    }

    /**
     * Sets up Communicator and thread pool.
     */
    private void setUp()
    {
        System.out.println("Variables: " + wolfReprAge + ", "
                + woldReprSuccessProb + ", " + rappitReprAge + ", "
                + rabbitReprSuccessProb + ", " + speedOfGrassGrowth);

        r = new Random(Seed);
        this.mapArray = new MapNode[mapSize][mapSize];
        ArrayList<OtpErlangPid> startReceivers = new ArrayList<OtpErlangPid>();
        mErlCom.flush();
        for (int i = 0; i < mapArray.length; i++)
        {
            for (int j = 0; j < mapArray[i].length; j++)
            {

                int type = r.nextInt(4);
                if (type > 2)
                    type = 0;
                else
                    type %= 3;
                if (type == MapNode.RABBIT)
                {
                    mErlCom.send(new Message(Map.NEW, null, new int[] { i, j }));
                    MessageSuper msg = mErlCom.receive();
                    mapArray[i][j] = new MapNode(r.nextInt(6), type,
                            msg.getPid());

                    startReceivers.add(msg.getPid());
                } else if (type == MapNode.WOLF)
                {

                    mErlCom.send(new Message(Map.NEWWOLF, null, new int[] { i,
                            j }));
                    MessageSuper msg = mErlCom.receive();

                    mapArray[i][j] = new MapNode(r.nextInt(6), type,
                            msg.getPid());
                    startReceivers.add(msg.getPid());
                } else
                {
                    mapArray[i][j] = new MapNode(r.nextInt(6), MapNode.NONE,
                            null);
                }
            }
        }
        for (OtpErlangPid pid : startReceivers)
        {

            mErlCom.send(new Message(Map.START, pid, null));
        }
        mMsgThrExec = new MessageThreadExecutor(10000, 4, 100, 10);
        this.messagePool = new MessagePool();
    }

    /**
     * Moves the type of node x1,y1 to x2,y2
     * 
     * @param x1
     *            x coordinate to move from
     * @param y1
     *            y coordinate to move from
     * @param x2
     *            x coordinate to move to
     * @param y2
     *            x coordinate to move to
     */
    void moveNode(int x1, int y1, int x2, int y2)
    {
        mapArray[x2][y2].setType(mapArray[x1][y1].getType());
        mapArray[x1][y1].setType(MapNode.NONE);
    }

    /**
     * Main loop that gets messages and delegates work to thread pool
     */
    @Override
    public void run()
    {
        super.run();
        while (running)
        {
            if (!paused)
            {
                handleNextMessage();
            }
        }
    }

    /**
     * Starts the correct Runnable according to the current message.
     */
    private void handleNextMessage()
    {
        nextMessage = mErlCom.receive();
        switch (nextMessage.getType())
        {
            case Map.RABBITMAP:
                mMsgThrExec.execute(messagePool.getMapRunnable(
                        (Message) nextMessage, mErlCom, this, mUpdtLis,
                        MapNode.RABBIT));
                break;
            case Map.MOVE:
                mMsgThrExec.execute(messagePool.getMoveRunnable(
                        (Message) nextMessage, mErlCom, this, mUpdtLis,
                        MapNode.RABBIT));
                break;
            case Map.RABBITEAT:
                mMsgThrExec.execute(messagePool.getEatRunnable(
                        (Message) nextMessage, mErlCom, this, mUpdtLis,
                        MapNode.RABBIT));
                break;
            case Map.WOLFMAP:
                mMsgThrExec.execute(messagePool.getWolfMapRunnable(
                        (Message) nextMessage, mErlCom, this, mUpdtLis,
                        MapNode.WOLF));
                break;
            case Map.WOLFMOVE:
                mMsgThrExec.execute(messagePool.getMoveRunnable(
                        (Message) nextMessage, mErlCom, this, mUpdtLis,
                        MapNode.WOLF));
                break;
            case Map.WOLFEAT:
                mMsgThrExec.execute(messagePool.getWolfEatRunnable(
                        (Message) nextMessage, mErlCom, this, mUpdtLis,
                        MapNode.WOLF));
                break;
            case Map.STOP:
                System.out.println("stopping");
                this.running = false;
                break;
            case Map.NEW:
                int[] coords = ((Message) nextMessage).getValues();
                synchronized (mapArray[coords[0]][coords[1]])
                {
                    mapArray[coords[0]][coords[1]].setPid(nextMessage.getPid());
                    mUpdtLis.update(coords[0], coords[1],
                            mapArray[coords[0]][coords[1]]);
                    mErlCom.send(new Message(Map.START, nextMessage.getPid(),
                            null));
                }
                break;
            case Map.NEWWOLF:
                coords = ((Message) nextMessage).getValues();
                synchronized (mapArray[coords[0]][coords[1]])
                {
                    mapArray[coords[0]][coords[1]].setPid(nextMessage.getPid());
                    mapArray[coords[0]][coords[1]].setType(MapNode.WOLF);
                    mUpdtLis.update(coords[0], coords[1],
                            mapArray[coords[0]][coords[1]]);
                    mErlCom.send(new Message(Map.START, nextMessage.getPid(),
                            null));
                }
                break;
            case Map.DEATH:
                coords = ((Message) nextMessage).getValues();
                synchronized (mapArray[coords[0]][coords[1]])
                {
                    mapArray[coords[0]][coords[1]].setType(MapNode.NONE);
                    mapArray[coords[0]][coords[1]].setPid(null);
                    mUpdtLis.update(coords[0], coords[1],
                            mapArray[coords[0]][coords[1]]);
                    break;
                }
        }
    }

    /**
     * Starts the simulation
     */
    public void simulationStart()
    {
        mMsgThrExec.log(true);
        System.out.println("Got sim start");
        if(grassGrower != null){
            grassGrower.running = false;
            grassGrower.interrupt();
        }
        grassGrower = new GrassGrower(this, Map.speedOfGrassGrowth, mUpdtLis);
        grassGrower.start();

        paused = false;
    }

    /**
     * Stops the simulation
     */
    public void simulationStop()
    {

        // grassGrower.running = false;

        paused = true;
    }

    /**
     * Stops the simulation and prepares the board for a reset.
     */
    public void simulationResetStop()
    {
        paused = true;
        if (grassGrower != null)
        {
            grassGrower.running = false;
            grassGrower.interrupt();
        }
        this.mMsgThrExec.flush();
        for (MapNode[] col : mapArray)
        {
            for (MapNode node : col)
            {
                if (node.getPid() != null)
                {
                    mErlCom.send(new Message(Map.DEATH, node.getPid(), null));
                }
            }
        }
        mErlCom.flush();
    }

    /**
     * Resets the simulation
     */
    public void simulationReset()
    {

        setUp();

    }

    public static int getMapSize()
    {
        return mapSize;
    }

    public static void setMapSize(int mapSize)
    {
        Map.mapSize = mapSize;
    }

    public static int getAmountOfGrass()
    {
        return amountOfGrass;
    }

    public static void setAmountOfGrass(int amountOfGrass)
    {
        Map.amountOfGrass = amountOfGrass;
    }

    public static int getSpeedOfGrassGrowth()
    {
        return speedOfGrassGrowth;
    }

    public static void setSpeedOfGrassGrowth(int speedOfGrassGrowth)
    {
        Map.speedOfGrassGrowth = speedOfGrassGrowth;
    }

    public static int getNumberOfWolves()
    {
        return numberOfWolves;
    }

    public static void setNumberOfWolves(int numberOfWolves)
    {
        Map.numberOfWolves = numberOfWolves;
    }

    public static int getMaxWolfAge()
    {
        return maxWolfAge;
    }

    public static void setMaxWolfAge(int maxWolfAge)
    {
        Map.maxWolfAge = maxWolfAge;
    }

    public static int getWolfReprAge()
    {
        return wolfReprAge;
    }

    public static void setWolfReprAge(int wolfReprAge)
    {
        Map.wolfReprAge = wolfReprAge;
    }

    public static int getWoldReprSuccessProb()
    {
        return woldReprSuccessProb;
    }

    public static void setWoldReprSuccessProb(int woldReprSuccessProb)
    {
        Map.woldReprSuccessProb = woldReprSuccessProb;
    }

    public static int getNumberOfRabbits()
    {
        return numberOfRabbits;
    }

    public static void setNumberOfRabbits(int numberOfRabbits)
    {
        Map.numberOfRabbits = numberOfRabbits;
    }

    public static int getMaxRabbitAge()
    {
        return maxRabbitAge;
    }

    public static void setMaxRabbitAge(int maxRabbitAge)
    {
        Map.maxRabbitAge = maxRabbitAge;
    }

    public static int getRappitReprAge()
    {
        return rappitReprAge;
    }

    public static void setRappitReprAge(int rappitReprAge)
    {
        Map.rappitReprAge = rappitReprAge;
    }

    public static int getRabbitReprSuccessProb()
    {
        return rabbitReprSuccessProb;
    }

    public static void setRabbitReprSuccessProb(int rabbitReprSuccessProb)
    {
        Map.rabbitReprSuccessProb = rabbitReprSuccessProb;
    }

    public MapNode[][] getMapArray()
    {
        return mapArray;
    }

    public void setMapArray(MapNode[][] mapArray)
    {
        this.mapArray = mapArray;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public static boolean isPaused()
    {
        return paused;
    }

    public static void setPaused(boolean paused)
    {
        Map.paused = paused;
    }

    public Communicator getmErlCom()
    {
        return mErlCom;
    }

    public void setmErlCom(Communicator mErlCom)
    {
        this.mErlCom = mErlCom;
    }

    public MessageThreadExecutor getmMsgThrExec()
    {
        return mMsgThrExec;
    }

    public void setmMsgThrExec(MessageThreadExecutor mMsgThrExec)
    {
        this.mMsgThrExec = mMsgThrExec;
    }

    public MessageSuper getNextMessage()
    {
        return nextMessage;
    }

    public void setNextMessage(MessageSuper nextMessage)
    {
        this.nextMessage = nextMessage;
    }

    public UpdateListener getmUpdtLis()
    {
        return mUpdtLis;
    }

    public void setmUpdtLis(UpdateListener mUpdtLis)
    {
        this.mUpdtLis = mUpdtLis;
    }

    public Random getR()
    {
        return r;
    }

    public void setR(Random r)
    {
        this.r = r;
    }

    public long getSeed()
    {
        return Seed;
    }

    public void setSeed(long seed)
    {
        Seed = seed;
    }

    public GrassGrower getGrassGrower()
    {
        return grassGrower;
    }

    public void setGrassGrower(GrassGrower grassGrower)
    {
        this.grassGrower = grassGrower;
    }

    public MessagePool getMessagePool()
    {
        return messagePool;
    }

    public void setMessagePool(MessagePool messagePool)
    {
        this.messagePool = messagePool;
    }

}