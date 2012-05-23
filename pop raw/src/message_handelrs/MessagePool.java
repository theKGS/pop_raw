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

    public MessagePool()
    {
        eatStack = new Stack<EatMsgHandler>();
        mapStack = new Stack<MapMsgHandler>();
        rabbitMoveStack = new Stack<MoveMsgHandler>();
        newStack = new Stack<NewMsgHandler>();
        wolfMapStack = new Stack<WolfMapMsgHandler>();
        wolfEatStack = new Stack<WolfEatMsgHandler>();
    }

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
