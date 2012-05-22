package raw.java.map;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class EatMsgHandler extends MsgHandler implements Runnable
{

    /**
     * Constructor for the Eat message handler
     * 
     * @param msg
     *            the message containing pid and coords
     * @param mErlCom
     *            communicator used to send responses
     * @param mapArray
     *            the map representation.
     */
    public EatMsgHandler(Message msg, Communicator mErlCom, Map map,
            UpdateListener updtLis, MessagePool msgPool, int requester)
    {
        super(msg, mErlCom, map, updtLis, msgPool, requester);
    }

    @Override
    public void run()
    {
        // System.out.println("Eat coords:" + coords[X1] + " , " + coords[Y1]);
        mate();

        if (requester == MapNode.RABBIT)
        {
            synchronized (map.getMapArray()[coords[X1]][coords[Y1]])
            {
                
                MapNode tNode = map.getMapArray()[coords[X1]][coords[Y1]];
                if (tNode.getType() == requester)
                {
                    if (tNode.getGrassLevel() > 0)
                    {
                        tNode.setGrassLevel(tNode.getGrassLevel() - 1);
                        mUpdtLis.update(coords[X1], coords[Y1],
                                map.getMapArray()[coords[X1]][coords[Y1]]);

                    }
                }
            }
        } else if (requester == MapNode.WOLF)
        {
            if (coords[X2] < 0 || coords[X2] >= map.getMapSize()
                    || coords[Y2] < 0 || coords[Y2] >= map.getMapSize())
            {
                mErlCom.send(new Message(Map.NO, pid, null));
            } else
            {
                int[] sCoors = compareCoordsArr(coords);
                synchronized (map.getMapArray()[sCoors[0]][sCoors[1]])
                {
                    synchronized (map.getMapArray()[sCoors[2]][sCoors[3]])
                    {
                        MapNode tNode = map.getMapArray()[coords[X1]][coords[Y1]];
                        if(tNode.getType() != requester){
                            mErlCom.send(new Message(Map.NO, pid, null));
                        }
                        MapNode targetNode = map.getMapArray()[coords[X2]][coords[Y2]];
                        if (targetNode.getType() == MapNode.RABBIT && targetNode.getPid() != null)
                        {
                            mErlCom.send(new Message(Map.DEATH, targetNode
                                    .getPid(), null));
                            targetNode.setPid(tNode.getPid());
                            targetNode.setType(tNode.getType());
                            tNode.setType(MapNode.NONE);
                            tNode.setPid(null);
                            mErlCom.send(new Message(Map.YES, pid, null));
                        } else if (targetNode.getType() == MapNode.NONE)
                        {
                            targetNode.setPid(tNode.getPid());
                            targetNode.setType(tNode.getType());
                            tNode.setType(MapNode.NONE);
                            tNode.setPid(null);
                            mErlCom.send(new Message(Map.EATMOVE, pid, null));
                        } else
                        {
                            mErlCom.send(new Message(Map.NO, pid, null));
                        }
                        sendUpdate(coords[X1], coords[Y1],
                                map.getMapArray()[coords[X1]][coords[Y1]]);
                        sendUpdate(coords[X2], coords[Y2],
                                map.getMapArray()[coords[X2]][coords[Y2]]);
                    }
                }
            }
        }
        msgPool.AddEatToStack(this);
    }

}