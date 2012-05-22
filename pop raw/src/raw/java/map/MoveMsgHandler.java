package raw.java.map;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;

public class MoveMsgHandler extends MsgHandler implements Runnable
{
    /**
     * Constructor for a runnable to handle move messages
     * 
     * @param message
     *            the message containing the move message
     * @param mErlCom
     *            the communicator the runnable should send it's response
     *            through
     * @param map
     *            the map that created the runnable
     * @param updtLis
     *            update listener that wants to know when something has changed.
     */
    public MoveMsgHandler(Message message, Communicator mErlCom, Map map,
            UpdateListener updtLis, MessagePool msgPool, int requester)
    {
        super(message, mErlCom, map, updtLis, msgPool, requester);
    }

    @Override
    public void run()
    {

        if (coords[X2] < 0 || coords[X2] >= map.getMapSize() || coords[Y2] < 0
                || coords[Y2] >= map.getMapSize())
        {
            mErlCom.send(new Message(Map.NO, pid, null));
        } else
        {
            mate();

            int[] sCoors = compareCoordsArr(coords);
            try
            {
                map.getMapArray()[sCoors[0]][sCoors[1]].lock();
                map.getMapArray()[sCoors[2]][sCoors[3]].lock();
                if (map.getMapArray()[coords[X1]][coords[Y1]].getType() != requester)
                {
                    mErlCom.send(new Message(Map.NO, pid, null));
                    return;
                }
                checkMove();
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
        msgPool.AddMoveToStack(this);

    }

    /**
     * Method that get the index of both coordinate pairs and returns the
     * smallest one
     * 
     * @param coords
     *            int[4] with {x1,y1,x,y2} coords
     * @return coord-pair with the smallest index
     */
    private void checkMove()
    {
        // System.out.println("moving: " + coords[X1] + " , " + coords[Y1] +
        // "to " + coords[X2] + ", " + coords[Y2]);
        MapNode currentNode = map.getMapArray()[coords[X1]][coords[Y1]];
        MapNode targetNode = map.getMapArray()[coords[X2]][coords[Y2]];
        if (targetNode.getType() != MapNode.NONE)
        {
            mErlCom.send(new Message(Map.NO, pid, null));
        } else
        {
            targetNode.setPid(currentNode.getPid());
            targetNode.setType(currentNode.getType());
            currentNode.setPid(null);
            currentNode.setType(MapNode.NONE);
            mErlCom.send(new Message(Map.YES, pid, null));

        }
        // System.out.println("Move handled");
    }
}
