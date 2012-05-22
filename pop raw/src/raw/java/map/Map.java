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

    private int mapSize = 0;
    private int amountOfGrass = 0;
    private int speedOfGrassGrowth = 5000;
    private int numberOfWolves = 0;
    private int maxWolfAge = 0;
    private int wolfReprAge = 4;
    private int woldReprSuccessProb = 0;
    private int numberOfRabbits = 0;
    private int maxRabbitAge = 0;
    private int rappitReprAge = 10;
    private int rabbitReprSuccessProb = 300;

    MapNode[][] mapArray;
    private boolean running = true;
    private boolean paused = false;
    private Communicator mErlCom;

    private MessageThreadExecutor mMsgThrExec;
    private MessageSuper nextMessage;
    private UpdateListener mUpdtLis;
    private Random r;
    private long Seed;
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
        this.mapSize = 25; // Size;
        this.Seed = Seed; // Seed;
        mapArray = new MapNode[mapSize][mapSize];
        this.messagePool = new MessagePool();
        // printMap();
        setUp();
    }

    /**
     * Prints the map to the console
     */
    @SuppressWarnings("unused")
    private void printMap()
    {
        for (int y = 0; y < mapSize; y++)
        {
            for (int x = 0; x < mapSize; x++)
            {
                System.out.print(mapArray[x][y].getType() + " ");
            }
            System.out.print("\n");
        }
    }

    /**
     * Sets up Communicator and thread pool.
     */
    private void setUp()
    {
        r = new Random(Seed);
        mErlCom = new Communicator();
        System.out.println("com started");
        ArrayList<OtpErlangPid> startReceivers = new ArrayList<OtpErlangPid>();
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
                    System.out.println("Sending new: " + i + ", " + j);
                    mErlCom.send(new Message(Map.NEW, null, new int[] { i, j }));

                    MessageSuper msg = mErlCom.receive();
                    System.out.println("got send: " + msg.getPid());
                    mapArray[i][j] = new MapNode(r.nextInt(6), type,
                            msg.getPid());
                    startReceivers.add(msg.getPid());
                } else if (type == MapNode.WOLF)
                {
                    System.out.println("Sending new wolf: " + i + ", " + j);
                    mErlCom.send(new Message(Map.NEWWOLF, null, new int[] { i,
                            j }));

                    MessageSuper msg = mErlCom.receive();
                    System.out.println("got send from wolf: " + msg.getPid());
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

        // mFakeMsgSender = new FakeMsgSender(mErlCom, this);

        mMsgThrExec = new MessageThreadExecutor(1000000, 10, 100, 10);

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
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        grassGrower = new GrassGrower(this, this.speedOfGrassGrowth, mUpdtLis);
        mMsgThrExec.execute(grassGrower);
        while (running)
        {
            if (paused)
            {
                continue;
            }
            handleNextMessage();

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
                    mUpdtLis.update(coords[0], coords[1], mapArray[coords[0]][coords[1]]);
                    mErlCom.send(new Message(Map.START, nextMessage.getPid(),
                            null));
                }
                break;
            case Map.NEWWOLF:
                coords = ((Message) nextMessage).getValues();
                synchronized (mapArray[coords[0]][coords[1]])
                {
                    mapArray[coords[0]][coords[1]].setPid(nextMessage.getPid());
                    mUpdtLis.update(coords[0], coords[1], mapArray[coords[0]][coords[1]]);
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
        paused = false;
    }

    /**
     * Stops the simulation
     */
    public void simulationStop()
    {
        paused = true;
    }

    /**
     * Resets the simulation
     */
    public void simulationReset()
    {
        setUp();

    }

    public int getMapSize()
    {
        return mapSize;
    }

    public void setMapSize(int mapSize)
    {
        this.mapSize = mapSize;
    }

    public int getAmountOfGrass()
    {
        return amountOfGrass;
    }

    public void setAmountOfGrass(int amountOfGrass)
    {
        this.amountOfGrass = amountOfGrass;

    }

    public int getSpeedOfGrassGrowth()
    {
        return speedOfGrassGrowth;

    }

    public void setSpeedOfGrassGrowth(int speedOfGrassGrowth)
    {
        this.speedOfGrassGrowth = speedOfGrassGrowth;

    }

    public int getNumberOfWolves()
    {
        return numberOfWolves;

    }

    public void setNumberOfWolves(int numberOfWolves)
    {
        this.numberOfWolves = numberOfWolves;

    }

    public int getMaxWolfAge()
    {
        return maxWolfAge;

    }

    public void setMaxWolfAge(int maxWolfAge)
    {
        this.maxWolfAge = maxWolfAge;

    }

    public int getWolfReprAge()
    {
        return wolfReprAge;

    }

    public void setWolfReprAge(int wolfReprAge)
    {
        this.wolfReprAge = wolfReprAge;

    }

    public int getWoldReprSuccessProb()
    {
        return woldReprSuccessProb;

    }

    public MapNode[][] getMapArray()
    {
        return mapArray;
    }

    public void setMapArray(MapNode[][] mapArray)
    {
        this.mapArray = mapArray;
    }

    public void setWoldReprSuccessProb(int woldReprSuccessProb)
    {
        this.woldReprSuccessProb = woldReprSuccessProb;
    }

    public int getNumberOfRabbits()
    {
        return numberOfRabbits;
    }

    public void setNumberOfRabbits(int numberOfRabbits)
    {
        this.numberOfRabbits = numberOfRabbits;
    }

    public int getMaxRabbitAge()
    {
        return maxRabbitAge;
    }

    public void setMaxRabbitAge(int maxRabbitAge)
    {
        this.maxRabbitAge = maxRabbitAge;
    }

    public int getRappitReprAge()
    {
        return rappitReprAge;
    }

    public void setRappitReprAge(int rappitReprAge)
    {
        this.rappitReprAge = rappitReprAge;
    }

    public int getRabbitReprSuccessProb()
    {
        return rabbitReprSuccessProb;
    }

    public void setRabbitReprSuccessProb(int rabbitReprSuccessProb)
    {
        this.rabbitReprSuccessProb = rabbitReprSuccessProb;
    }
}