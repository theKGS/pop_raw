package message_handelrs;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.map.Map;
import raw.java.map.MapNode;

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

        try
        {
            map.getMapArray()[coords[X1]][coords[Y1]].lock();
            MapNode tNode = map.getMapArray()[coords[X1]][coords[Y1]];
            if (tNode.getType() == MapNode.RABBIT)
            {
                if (tNode.getGrassLevel() > 0)
                {
                    tNode.setGrassLevel(tNode.getGrassLevel() - 1);
                    mUpdtLis.update(coords[X1], coords[Y1],
                            map.getMapArray()[coords[X1]][coords[Y1]]);
                }
            }
        } finally
        {
            map.getMapArray()[coords[X1]][coords[Y1]].unlock();
        }

        msgPool.AddEatToStack(this);
    }

}