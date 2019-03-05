package group_5;

import java.util.ArrayList;
import java.util.HashMap;

public class Connection {
	  
	
	private HashMap keyConnectDown; //maps Integers (keynums) to lists of
                                    //gizmos triggered. (KeyPress)  
	private HashMap keyConnectUp; //maps Integers (keynums) to lists of
                                  //gizmos triggered. (KeyRelease)
    private HashMap gizmoConnect; //maps gizmos to lists of connected
                                  //gizmo

    private HashMap mouseConnectDown;
    
    public Connection()
    {
    	keyConnectDown =  new HashMap();
    	keyConnectUp = new HashMap();
    	gizmoConnect = new HashMap();
    	mouseConnectDown = new HashMap();
    }
    
    public void connectKeyDown(Gizmo giz,Integer keynum)
    {
    	//一个keynum对应一个所有连接的gizmo的List
    	//检查 次列表是否存在
    	if(keyConnectDown.get(keynum) == null){
    		ArrayList tmp = new ArrayList();
    		tmp.add(giz);
    		keyConnectDown.put(keynum, tmp);
    	}else{
    		ArrayList boundGizmos = (ArrayList)keyConnectDown.get(keynum);
    		if(!boundGizmos.contains(giz)){
    			boundGizmos.add(giz);
    			keyConnectDown.put(keynum, boundGizmos);
    		}
    	}
    }
    
    public void connectMouseDown(Gizmo giz,Integer keynum)
    {
    	//一个buttonnum对应一个所有连接的gizmo的List
    	//检查 次列表是否存在
    	if(mouseConnectDown.get(keynum) == null){
    		ArrayList tmp = new ArrayList();
    		tmp.add(giz);
    		mouseConnectDown.put(keynum, tmp);
    	}else{
    		ArrayList boundGizmos = (ArrayList)mouseConnectDown.get(keynum);
    		if(!boundGizmos.contains(giz)){
    			boundGizmos.add(giz);
    			mouseConnectDown.put(keynum, boundGizmos);
    		}
    	}
    }
	
    public void connectGizmo(Gizmo g1,Gizmo g2)
    {
    	if(gizmoConnect.get(g1) == null){
    		ArrayList tmp = new ArrayList();
    		tmp.add(g2);
    		gizmoConnect.put(g1, tmp);
    	}else{
    		ArrayList connected = (ArrayList)gizmoConnect.get(g1);
    		if(!connected.contains(g2)){
    			connected.add(g2);
    			gizmoConnect.put(g1, connected);
    		}
    	}
    }
    
    public void removeConnect(Gizmo giz)
    {
    	gizmoConnect.remove(giz);
    }
    
    public HashMap getKeyConnectDown() {
		return keyConnectDown;
	}

	public void setKeyConnectDown(HashMap keyConnectDown) {
		this.keyConnectDown = keyConnectDown;
	}

	public HashMap getMouseConnectDown() {
		return mouseConnectDown;
	}

	public void setMouseConnectDown(HashMap mouseConnectDown) {
		this.mouseConnectDown = mouseConnectDown;
	}

	public HashMap getGizmoConnect() {
		return gizmoConnect;
	}

	public void setGizmoConnect(HashMap gizmoConnect) {
		this.gizmoConnect = gizmoConnect;
	}
    
}
