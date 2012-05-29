package message_handelrs;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.j_int_java.SendMessage;
import raw.java.map.Map;
import raw.java.map.MapNode;

public class WolfMapMsgHandler extends MsgHandler implements Runnable
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
    public WolfMapMsgHandler(Message msg, Communicator mErlCom, Map map, UpdateListener mUpdtLis,
            MessagePool msgPool, int requester)
    {
        super(msg, mErlCom, map, mUpdtLis, msgPool, requester);
    }

    @Override
    public void run()
    {
        int x = coords[0];
        int y = coords[1];
        int index = 0;
        MapNode[] squares =  new MapNode[25];
        if (map.getMapArray()[x][y].getType() == MapNode.RABBIT && map.getMapArray()[x][y].getPid() != null)
        {
            mErlCom.send(new Message(Map.DEATH, map.getMapArray()[x][y].getPid(), null));
            map.getMapArray()[x][y].clearNode();
            mUpdtLis.update(x, y, map.getMapArray()[x][y]);
        } else
        {

            
            for (int j = -2; j < 3; j++)
            {
                for (int i = -2; i < 3; i++)
                {
                    if (x + i >= 0 && x + i < Map.getMapSize() && y + j >= 0
                            && y + j < Map.getMapSize())
                    {
                        squares[index] = map.getMapArray()[x + i][y + j];
                    }
                    index++;
                }
            }
            mErlCom.send(new SendMessage(Map.WOLFMAP, pid, squares));
            msgPool.AddWolfMapToStack(this);
        }
       
    }
}
