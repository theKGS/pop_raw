package message_handelrs;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;
import raw.java.map.Map;
import raw.java.map.MapNode;

public class MapMsgHandler extends MsgHandler implements Runnable
{

    /**
     * Constructor for the Map message runnable.
     * 
     * @param pid
     *            the pid of the calling process
     * @param mErlCom
     *            the communicator used to send messages.
     * @param mapArray
     *            the representation of the map
     */
    public MapMsgHandler(Message msg, Communicator mErlCom, Map map,
            MessagePool msgPool, UpdateListener mUpdtLis, int requester)
    {
        super(msg, mErlCom, map, mUpdtLis, msgPool, requester);
    }

    @Override
    public void run()
    {
        int x = coords[0];
        int y = coords[1];
        int index = 0;
        MapNode[] squares = null;
        if (map.getMapArray()[x][y].getType() == MapNode.RABBIT)
        {
            squares = new MapNode[9];

            for (int j = -1; j < 2; j++)
            {
                for (int i = -1; i < 2; i++)
                {
                    if (x + i >= 0 && x + i < map.getMapSize() && y + j >= 0
                            && y + j < map.getMapSize())
                    {
                        squares[index] = map.getMapArray()[x + i][y + j];
                    }
                    index++;
                }
            }
            mErlCom.send(new SendMessage(Map.RABBITMAP, pid, squares));
            msgPool.AddMapToStack(this);
        } else
        {
            if (map.getMapArray()[x][y].getPid() != null)
            {
                mErlCom.send(new Message(Map.DEATH, map.getMapArray()[x][y]
                        .getPid(), null));
                map.getMapArray()[x][y].setType(MapNode.NONE);
                map.getMapArray()[x][y].setPid(null);
                mUpdtLis.update(x, y, map.getMapArray()[x][y]);
            }
        }

    }
}
