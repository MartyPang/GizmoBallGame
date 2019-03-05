package group_5;

import group_5.Gizmo.GizmoType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;

public class Absorber extends Gizmo {

	//private Ball absorbedBall;

	private ArrayList balls;
	private Ball firingBall;
	public Absorber(GameBoard gb,String name,int x,int y,int x1L,int y1L)
	{
		this.gb = gb;
		this.name = name;
		this.x = x*GameBoard.PixelsPerL;
		this.y = y*GameBoard.PixelsPerL;
		this.width = x1L;
		this.height = y1L;
		this.refCoef = 0.0;
	    this.balls = new ArrayList();
	    this.firingBall = null;
		this.gt = GizmoType.Absorber;
	}
	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		//Do nothing!!!
		//Assume absorber cannot rotate.
	}

	
	
	@Override
	public void enlarge() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getSaveString() {
		// TODO Auto-generated method stub
		String save = "Absorber "+this.name+" "+this.x/GameBoard.PixelsPerL+" "+this.y/GameBoard.PixelsPerL+" "+this.width+" "+this.height;
		return save;
	}
	
	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.PINK);
		g.fillRect(x, y, width*GameBoard.PixelsPerL, height*GameBoard.PixelsPerL);
	}

	@Override
	public List getBoundaryShape(Ball b) {
		// TODO Auto-generated method stub
	    List shapes = new ArrayList();
	    double x2 = x+width*GameBoard.PixelsPerL - 1;
	    double y2 = y+height*GameBoard.PixelsPerL - 1;
	      
	    if (b.equals(firingBall)) {
	        //leave out section of top and corner cirle so ball being fired
	        //can exit
	        shapes.add(new LineSegment(x, y, x2-GameBoard.PixelsPerL, y));
	      } else {
	        shapes.add(new Circle(x2, y, 0));
	        shapes.add(new LineSegment(x, y, x2, y));
	      }

	    shapes.add(new LineSegment(x, y, x, y2));
	    shapes.add(new LineSegment(x2, y, x2, y2));
	    shapes.add(new LineSegment(x, y2, x2, y2));
	    //0-radius circles at corners
	    
	    shapes.add(new Circle(x2, y2, 0));
	    shapes.add(new Circle(x, y2, 0));
	    shapes.add(new Circle(x, y, 0));
	    return shapes;
	}

	@Override
	public void fire(Ball firer) {
		// TODO Auto-generated method stub
	    balls.add(firer);
	    firer.setAbsorbed(true);
	    setBalls();
	    firer.setVx(0);
	    firer.setVy(0);
	    //somehow ball being fired out came back and hit!
	    if (firer.equals(firingBall)) {
	      firingBall = null;
	      gb.removeFromActiveList(this);
	    }
	    gb.resetScore();
		super.fire(firer);
	}
	@Override
	public void startAction() {
		// TODO Auto-generated method stub
	    if (!balls.isEmpty() && firingBall == null) {
	        gb.addToActiveList(this);
	        //fire next ball
	        firingBall = (Ball)balls.remove(0);
	        firingBall.setAbsorbed(false);
	        firingBall.setVy(-50*25);
	        //align balls that are being held
	        if (!balls.isEmpty()) setBalls();
	      }
	}
	@Override
	public void action() {
		// TODO Auto-generated method stub
	    if (firingBall.getY() + firingBall.getRadius() < this.y) { 
	        //deactivate absorber locally and in GameBoard
	        firingBall = null;
	        gb.removeFromActiveList(this);
	      }
	}

	  
	private void setBalls() {
	
		int numballs = balls.size();
		int ballrad = ((Ball)balls.get(0)).getRadius();
		if (2*numballs*ballrad <= width * GameBoard.PixelsPerL) {
			//all balls fit next to each other in the absorber
		    for (int i = 0; i < balls.size(); i++) {
		    	Ball balli = (Ball)balls.get(i);
			    balli.setX(x + width*GameBoard.PixelsPerL - 1 - (1 + 2*i)*(ballrad));
			    balli.setY(y + height*GameBoard.PixelsPerL - 1 - ballrad);  
		    }   
		}
	}
}
