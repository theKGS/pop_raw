package message_handelrs;

import java.util.Stack;

import raw.java.gui.UpdateListener;
import raw.java.j_int_java.Communicator;
import raw.java.j_int_java.Message;
import raw.java.map.Map;

public class MessagePool
{

    private Stack<NewMsgHandler> newStack;
    private Stack<MoveMsgHandler> rabbitMoveStack;
    private Stack<MapMsgHandler> mapStack;
    private Stack<EatMsgHandler> eatStack;
    private Stack<WolfMapMsgHandler> wolfMapStack;
    private Stack<WolfEatMsgHandler> wolfEatStack;
    /**
     * Pool container for all types of runnables
     */
    public MessagePool()
    {
        eatStack = new Stack<EatMsgHandler>();
        mapStack = new Stack<MapMsgHandler>();
        rabbitMoveStack = new Stack<MoveMsgHandler>();
        newStack = new Stack<NewMsgHandler>();
        wolfMapStack = new Stack<WolfMapMsgHandler>();
        wolfEatStack = new Stack<WolfEatMsgHandler>();
    }

    /**
     * Gets an available EatRunnable or creates a new one if there is none
     * available
     * 
     * @param msg
     *            the new message
     * @param mErlCom
     *            communicator to answer to
     * @param map
     *            the map that wants the Runnable
     * @param updtLis
     *            update handle to the gui
     * @param requester
     *            wolf or rabbit
     * @return A new EatMsgHandler with the new params set
     */
    public EatMsgHandler getEatRunnable(Message msg, Communicator mErlCom,
            Map map, UpdateListener updtLis, int requester)
    {
        synchronized (eatStack)
        {
            if (!eatStack.isEmpty())
            {
                EatMsgHandler temp = eatStack.pop();
                temp.setCoords(msg.getValues());
                temp.setPid(msg.getPid());
                temp.setRequester(requester);
                temp.setMap(map);
                return temp;
            } else
            {
                return new EatMsgHandler(msg, mErlCom, map, updtLis, this,
                        requester);
            }
        }
    }

    public void AddEatToStack(EatMsgHandler eatMsg)
    {
        synchronized (eatStack)
        {
            eatStack.push(eatMsg);
        }
    }

    /**
     * Gets an available MapRunnable or creates a new one if there is none
     * available
     * 
     * @param msg
     *            the new message
     * @param mErlCom
     *            communicator to answer to
     * @param map
     *            the map that wants the Runnable
     * @param updtLis
     *            update handle to the gui
     * @param requester
     *            wolf or rabbit
     * @return A new MapMsgHandler with the new params set
     */
    public MapMsgHandler getMapRunnable(Message msg, Communicator mErlCom,
            Map map, UpdateListener updtLis, int requester)
    {
        synchronized (mapStack)
        {
            if (!mapStack.isEmpty())
            {
                MapMsgHandler temp = mapStack.pop();
                temp.setCoords(msg.getValues());
                temp.setPid(msg.getPid());
                temp.setRequester(requester);
                temp.setMap(map);
                return temp;
            } else
            {
                return new MapMsgHandler(msg, mErlCom, map,this, updtLis, requester);
            }
        }
    }

    public void AddMapToStack(MapMsgHandler mapMsg)
    {
        synchronized (mapStack)
        {
            mapStack.push(mapMsg);
        }
    }

    /**
     * Gets an available MoveRunnable or creates a new one if there is none
     * available
     * 
     * @param msg
     *            the new message
     * @param mErlCom
     *            communicator to answer to
     * @param map
     *            the map that wants the Runnable
     * @param updtLis
     *            update handle to the gui
     * @param requester
     *            wolf or rabbit
     * @return A new MoveMsgHandler with the new params set
     */
    public MoveMsgHandler getMoveRunnable(Message msg, Communicator mErlCom,
            Map map, UpdateListener updtLis, int requester)
    {
        synchronized (rabbitMoveStack)
        {
            if (!rabbitMoveStack.isEmpty())
            {
                MoveMsgHandler temp = rabbitMoveStack.pop();
                temp.setCoords(msg.getValues());
                temp.setPid(msg.getPid());
                temp.setRequester(requester);
                temp.setMap(map);
                return temp;
            } else
            {
                return new MoveMsgHandler(msg, mErlCom, map, updtLis, this,
                        requester);
            }
        }
    }

    public void AddMoveToStack(MoveMsgHandler moveMsg)
    {
        synchronized (rabbitMoveStack)
        {
            rabbitMoveStack.push(moveMsg);
        }
    }

    /**
     * Gets an available NewRunnable or creates a new one if there is none
     * available
     * 
     * @param msg
     *            the new message
     * @param mErlCom
     *            communicator to answer to
     * @param map
     *            the map that wants the Runnable
     * @param updtLis
     *            update handle to the gui
     * @param requester
     *            wolf or rabbit
     * @return A new NewMsgHandler with the new params set
     */
    public NewMsgHandler getNewRunnable(Message msg, Communicator mErlCom,
            Map map, UpdateListener updtLis, int requester)
    {
        synchronized (newStack)
        {
            if (!newStack.isEmpty())
            {
                NewMsgHandler temp = newStack.pop();
                temp.setCoords(msg.getValues());
                temp.setPid(msg.getPid());
                temp.setRequester(requester);
                temp.setMap(map);
                return temp;
            } else
            {
                return new NewMsgHandler(msg, mErlCom, map, this, requester);
            }
        }
    }

    public void AddNewToStack(NewMsgHandler newMsg)
    {
        synchronized (newStack)
        {
            newStack.push(newMsg);
        }
    }

    /**
     * Gets an available WolfMapRunnable or creates a new one if there is none
     * available
     * 
     * @param msg
     *            the new message
     * @param mErlCom
     *            communicator to answer to
     * @param map
     *            the map that wants the Runnable
     * @param updtLis
     *            update handle to the gui
     * @param requester
     *            wolf or rabbit
     * @return A new WolfMapMsgHandler with the new params set
     */
    public WolfMapMsgHandler getWolfMapRunnable(Message msg, Communicator mErlCom,
            Map map, UpdateListener updtLis, int requester)
    {
        synchronized (mapStack)
        {
            if (!wolfMapStack.isEmpty())
            {
                WolfMapMsgHandler temp = wolfMapStack.pop();
                temp.setCoords(msg.getValues());
                temp.setPid(msg.getPid());
                temp.setRequester(requester);
                temp.setMap(map);
                return temp;
            } else
            {
                return new WolfMapMsgHandler(msg, mErlCom, map, updtLis,this, requester);
            }
        }
    }
    public void AddWolfMapToStack(WolfMapMsgHandler wolffMapMsgHandler)
    {
       this.wolfMapStack.push(wolffMapMsgHandler);
        
    }

    /**
     * Gets an available WolfEatRunnable or creates a new one if there is none
     * available
     * 
     * @param msg
     *            the new message
     * @param mErlCom
     *            communicator to answer to
     * @param map
     *            the map that wants the Runnable
     * @param updtLis
     *            update handle to the gui
     * @param requester
     *            wolf or rabbit
     * @return A new WolfEatMsgHandler with the new params set
     */
    public WolfEatMsgHandler getWolfEatRunnable(Message msg, Communicator mErlCom,
            Map map, UpdateListener updtLis, int requester)
    {
        synchronized (wolfEatStack)
        {
            if (!wolfEatStack.isEmpty())
            {
                WolfEatMsgHandler temp = wolfEatStack.pop();
                temp.setCoords(msg.getValues());
                temp.setPid(msg.getPid());
                temp.setRequester(requester);
                temp.setMap(map);
                return temp;
            } else
            {
                return new WolfEatMsgHandler(msg, mErlCom, map, updtLis, this,
                        requester);
            }
        }
    }

    public void AddWolfEatToStack(WolfEatMsgHandler eatMsg)
    {
        synchronized (wolfEatStack)
        {
            wolfEatStack.push(eatMsg);
        }
    }
}
