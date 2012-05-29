package message_handelrs;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.map.Map;
import raw.java.map.MapNode;

public class WolfEatMsgHandler extends MsgHandler implements Runnable
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
    public WolfEatMsgHandler(Message msg, Communicator mErlCom, Map map,
            UpdateListener updtLis, MessagePool msgPool, int requester)
    {
        super(msg, mErlCom, map, updtLis, msgPool, requester);
    }

    @Override
    public void run()
    {
        mate();
        if (coords[X2] < 0 || coords[X2] >= Map.getMapSize() || coords[Y2] < 0
                || coords[Y2] >= Map.getMapSize())
        {
            mErlCom.send(new Message(Map.NO, pid, null));
        } else
        {
            int[] sCoors = compareCoordsArr(coords);
            try
            {
                map.getMapArray()[sCoors[0]][sCoors[1]].lock();
                map.getMapArray()[sCoors[2]][sCoors[3]].lock();
                MapNode tNode = map.getMapArray()[coords[X1]][coords[Y1]];
                MapNode targetNode = map.getMapArray()[coords[X2]][coords[Y2]];
                if (tNode.getType() != MapNode.WOLF
                        || targetNode.getType() == MapNode.WOLF)
                {
                    mErlCom.send(new Message(Map.NO, pid, null));
                }

                else if (targetNode.getType() == MapNode.RABBIT
                        && targetNode.getPid() != null)
                {
                    mErlCom.send(new Message(Map.DEATH, targetNode.getPid(),
                            null));
                    targetNode.takeDataFrom(tNode);
                    tNode.clearNode();
                    mErlCom.send(new Message(Map.YES, pid, null));
                } else if (targetNode.getType() == MapNode.NONE)
                {
                    targetNode.takeDataFrom(tNode);
                    tNode.clearNode();
                    mErlCom.send(new Message(Map.EATMOVE, pid, null));
                }
                sendUpdate(coords[X1], coords[Y1],
                        map.getMapArray()[coords[X1]][coords[Y1]]);
                sendUpdate(coords[X2], coords[Y2],
                        map.getMapArray()[coords[X2]][coords[Y2]]);

            } finally
            {
                map.getMapArray()[sCoors[0]][sCoors[1]].unlock();
                map.getMapArray()[sCoors[2]][sCoors[3]].unlock();
            }
        }

        msgPool.AddWolfEatToStack(this);
    }

}