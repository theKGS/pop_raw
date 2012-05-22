package message_handelrs;

import raw.java.gui.RabbitsAndWolves;
import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.map.Map;
import raw.java.map.MapNode;

import com.ericsson.otp.erlang.OtpErlangPid;

public class MsgHandler
{
    public static final int AGE = 0;
    public static final int HUNGER = 1;
    public static final int X1 = 2;
    public static final int Y1 = 3;
    public static final int X2 = 4;
    public static final int Y2 = 5;

    protected OtpErlangPid pid;
    protected int[] coords;
    protected Communicator mErlCom;
    protected Map map;
    protected UpdateListener mUpdtLis;
    protected MessagePool msgPool;
    protected int requester;

    public int getRequester()
    {
        return requester;
    }

    public void setRequester(int requester)
    {
        this.requester = requester;
    }

    /**
     * Super constructor called by all message handlers.
     * 
     * @param msg
     *            the message received from communicator
     * @param com
     *            the communicator
     * @param map
     *            map that created object
     * @param updtLis
     *            update lister
     */
    public MsgHandler(Message msg, Communicator com, Map map,
            UpdateListener updtLis, MessagePool msgPool, int requester)
    {
        this.pid = msg.getPid();
        this.coords = msg.getValues();
        this.msgPool = msgPool;
        this.mErlCom = com;
        this.map = map;
        this.mUpdtLis = updtLis;
        this.requester = requester;
    }

    /**
     * Tries to mate with the current rabbit or wolf.
     * 
     */
    protected boolean mate()
    {
        int x = coords[X1];
        int y = coords[Y1];
        int[][] syncCoords = null;
        if (requester == MapNode.RABBIT)
        {
            syncCoords = new int[][] { new int[] { x - 1, y - 1 },

            new int[] { x, y - 1 },

            new int[] { x + 1, y - 1 },

            new int[] { x - 1, y },

            new int[] { x, y },

            new int[] { x + 1, y },

            new int[] { x - 1, y + 1 },

            new int[] { x, y + 1 },

            new int[] { x + 1, y + 1 } };
        } else if (requester == MapNode.WOLF)
        {
            syncCoords = new int[][] {

            new int[] { x - 2, y - 2 }, new int[] { x - 1, y - 2 },
                    new int[] { x, y - 2 },

                    new int[] { x + 1, y - 2 }, new int[] { x + 2, y - 2 },

                    new int[] { x - 2, y - 1 }, new int[] { x - 1, y - 1 },

                    new int[] { x, y - 1 }, new int[] { x + 1, y - 1 },

                    new int[] { x + 2, y - 1 }, new int[] { x - 2, y },

                    new int[] { x - 1, y }, new int[] { x, y },

                    new int[] { x + 1, y }, new int[] { x + 2, y },

                    new int[] { x - 2, y + 1 }, new int[] { x - 1, y + 1 },

                    new int[] { x, y + 1 }, new int[] { x + 1, y + 1 },

                    new int[] { x + 2, y + 1 }, new int[] { x - 2, y + 2 },

                    new int[] { x - 1, y + 2 }, new int[] { x, y + 2 },

                    new int[] { x + 1, y + 2 }, new int[] { x + 2, y + 2 } };
        }
        syncAll(syncCoords, 0);
        return false;
    }

    /**
     * Synchronizes all squares in cVector, from smallest array index to the
     * largest
     * 
     * @param cVector
     *            the int array containing all coordinates that shall be
     *            synchronized (x1,y1,x2,y2,...,xn,yn)
     * @param index
     *            due to recursion this index is used to get the correct
     *            cordinate pair
     */
    protected void syncAll(int[][] cVector, int index)
    {
        try
        {
            for (int[] i : cVector)
            {
                if (isInsideMap(i))
                {
                    map.getMapArray()[i[0]][i[1]].lock();
                }
            }
            doMate(this.coords[X1], this.coords[Y1]);
        } finally
        {
            for (int[] i : cVector)
            {
                if (isInsideMap(i))
                {
                    map.getMapArray()[i[0]][i[1]].unlock();
                }
            }
        }
    }

    private boolean isInsideMap(int[] i)
    {
        return (i[0] > 0 && i[0] < map.getMapSize() && i[1] > 0 && i[1] < map
                .getMapSize());
    }

    /**
     * Executes the mating
     * 
     * @param x
     *            x position to mate
     * @param y
     *            y position to mate
     */
    protected void doMate(int x, int y)
    {
        int mateType = map.getMapArray()[x][y].getType();
        boolean matePossible = false;
        int[] newSpot = null;
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        if (requester == MapNode.RABBIT)
        {
            minX = x > 0 ? -1 : 0;
            maxX = x < map.getMapSize() - 1 ? 2 : 1;

            minY = y > 0 ? -1 : 0;
            maxY = y < map.getMapSize() - 1 ? 2 : 1;
        } else if (requester == MapNode.WOLF)
        {
            minX = x > 0 ? (x > 1 ? -2 : -1) : 0;

            maxX = x < map.getMapSize() - 1 ? (x < map.getMapSize() - 2 ? 2 : 1)
                    : 1;

            minY = y > 0 ? (y > 1 ? -2 : 1) : 0;
            maxY = y < map.getMapSize() - 1 ? (y < map.getMapSize() - 2 ? 2 : 1)
                    : 1;

        }
        for (int i = minX; i < maxX; i++)
        {
            for (int j = minY; j < maxY; j++)
            {
                if (map.getMapArray()[x + i][y + j].getType() != MapNode.NONE)
                {
                    if (map.getMapArray()[x + i][y + j].getType() != mateType
                            && requester == MapNode.RABBIT)
                    {
                        matePossible = false;
                    } else
                    {
                        matePossible = true;
                    }
                } else
                {
                    newSpot = new int[] { x + i, y + j };
                }
            }
        }
        if (matePossible && newSpot != null)
        {
            int r = (int) (Math.random() * 1000);
            if (mateType == MapNode.RABBIT)
            {
                if (r > map.getRabbitReprSuccessProb()
                        && coords[AGE] > map.getRappitReprAge()
                        && coords[HUNGER] < 3)
                {
                    map.getMapArray()[newSpot[0]][newSpot[1]]
                            .setType(MapNode.RABBIT);
                    mErlCom.send(new Message(Map.NEW, null, newSpot));
                }

            } else
            {
                if (r > map.getWoldReprSuccessProb()
                        && coords[AGE] > map.getWolfReprAge()
                        && coords[HUNGER] < 20)
                {
                    map.getMapArray()[newSpot[0]][newSpot[1]]
                            .setType(MapNode.WOLF);
                    mErlCom.send(new Message(Map.NEWWOLF, null, newSpot));
                }
            }

        }

    }

    /**
     * Compares two coord pairs
     * 
     * @param coords
     *            array of coord pairs (x1,y1,x2,y2)
     * @return true if (x1,y1) < (x2,y2) false otherwise
     */
    protected int[] compareCoordsArr(int[] coords)
    {
        return coords[Y1] * map.getMapSize() + coords[X1] + map.getMapSize() < coords[Y2]
                * map.getMapSize() + coords[X2] + map.getMapSize() ? new int[] {
                coords[X1], coords[Y1], coords[X2], coords[Y2] } : new int[] {
                coords[X2], coords[Y2], coords[X1], coords[Y1] };

    }

    /**
     * Sends update to the upådate listener
     * 
     * @param x
     *            x coord of the node
     * @param y
     *            y coord of the node
     * @param updtNode
     *            instance of the updated node
     */
    protected void sendUpdate(int x, int y, MapNode updtNode)
    {
        if (mUpdtLis != null)
        {
            mUpdtLis.update(x, y, updtNode);
        }
    }

    public OtpErlangPid getPid()
    {
        return pid;
    }

    public void setPid(OtpErlangPid pid)
    {
        this.pid = pid;
    }

    public int[] getCoords()
    {
        return coords;
    }

    public void setCoords(int[] coords)
    {
        this.coords = coords;
    }

    public Communicator getmErlCom()
    {
        return mErlCom;
    }

    public void setmErlCom(Communicator mErlCom)
    {
        this.mErlCom = mErlCom;
    }

    public Map getMap()
    {
        return map;
    }

    public void setMap(Map map)
    {
        this.map = map;
    }

    public UpdateListener getmUpdtLis()
    {
        return mUpdtLis;
    }

    public void setmUpdtLis(UpdateListener mUpdtLis)
    {
        this.mUpdtLis = mUpdtLis;
    }

}
