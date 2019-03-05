package group_5;

import group_5.Gizmo.GizmoType;


public class GridMap {

	int width;
	int height;
	Gizmo gizmo[][];
	public GridMap(int width,int height)
	{
		this.width = width;
		this.height = height;
		gizmo = new Gizmo[width][height];
	}
	
	public void addGizmo(Gizmo tmp)
	{
		int x = tmp.getX() / GameBoard.PixelsPerL;
		int y = tmp.getY() / GameBoard.PixelsPerL;
	    int width = tmp.getWidth();
	    int height = tmp.getHeight();

	    for (int i = y; i<height+y; i++) {
	      for (int j = x; j<width+x; j++) {
	    	  gizmo[j][i] = tmp;
	      }
	    }
	}
	
	  
	public void addWalls(Gizmo giz) {
	    int width = GameBoard.BoardWidth;
	    int height = GameBoard.BoardHeight;
	    
	    for (int i = 0; i<width; i++) {
	      gizmo[0][i] = giz;
	      gizmo[height-1][i] = giz;
	    }

	    for (int j = 1; j<height-1; j++) {
	      gizmo[j][0] = giz;
	      gizmo[j][width-1] = giz;
	    }
	  }
	public Gizmo get(int i,int j)
	{
		return gizmo[i][j];
	}
	/**
	 * precondition: get(i,j) != null
	 * @param i
	 * @param j
	 */
	public void remove(Gizmo giz)
	{
	    int x = giz.getX() / GameBoard.PixelsPerL;
	    int y = giz.getY() / GameBoard.PixelsPerL;

	    int width = giz.getWidth();
	    int height = giz.getHeight();

	    for (int i = y; i<height+y; i++) {
	      for (int j = x; j<width+x; j++) {
	    	  if (gizmo[j][i] != null && gizmo[j][i].equals(giz))
	    		  gizmo[j][i] = null;
	      }
	    }
	}
	public void removeGizmo(int x,int y)
	{
		gizmo[x][y] = null;
	}
	
	public boolean checkClear(Gizmo giz,int xInL,int yInL)
	{
		if(giz instanceof Flipper)
		{
			if(giz.getType() == GizmoType.LeftFlipper)
			{
				
				if((get(xInL,yInL) != null)||(get(xInL+1,yInL) != null)||(get(xInL,yInL+1) != null)||(get(xInL+1,yInL+1) != null))		
			    {
					return false;
			    }else{
			    	return true;
			    	}
			}else{
				if((get(xInL,yInL) != null)||(get(xInL-1,yInL) != null)||(get(xInL,yInL+1) != null)||(get(xInL-1,yInL+1) != null))		
			    {
					return false;
			    }else{
			    	return true;
			    	}
			}

		}else if(giz instanceof Absorber){
			for(int x = xInL;x<xInL+giz.getWidth();x++)
			{
				for(int y = yInL;y<yInL+giz.getHeight();y++)
				{
					if(get(x,y) != null)return false;
				}
				
			}
			return true;
		}else{
			if(get(xInL,yInL) != null)
			{
				return false;
			}else{
				return true;
			}
		}
	}
}
