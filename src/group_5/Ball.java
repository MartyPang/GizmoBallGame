package group_5;



import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;

public class Ball {

	//position (x,y) in pixels
	private double x;
	private double y;
	
	//velocity (vx,vy)代表x方向和y方向的速度，单位：pixels/sec
	private double vx;
	private double vy;
	
	//记录初始位置与初始速度，FOR RESET
	private double startX,startY;
	private double startVX,startVY;
	
	// vector representations
	private Vect posVector = null;
	private Vect velVector = null;
	
	//球的半径
	private int radius;
	
	//与最近的Gizmo的相碰所需时间  
	private double timeToClosestGizmo = Double.POSITIVE_INFINITY;
	private Gizmo closestGizmo = null;
	private Object closestShape = null;
	
	//gravity
	private static double g;
	private double timerLength;
	private double tl;
	private int depth;
	private GameBoard gb;
	private List gizmos;
	
	private boolean absorbed; 
	private static int callCount = 0;
	
	//constructor
	public Ball(GameBoard gb,double x,double y,double vx,double vy,int radius)
	{
		this.gb = gb;
		gizmos = new ArrayList();
		g = 500;
	    this.timerLength = gb.MSECPERTICK;
	    this.tl = timerLength / 1000.0;
		this.startX = this.x = x;
		this.startY = this.y = y;
		this.startVX = this.vx = vx;
		this.startVY = this.vy = vy;
		this.radius = radius;
		this.absorbed = false;
	}
	
	public String getSaveString()
	{
		String save = "";
		save += "Ball ";
		save += this.x/GameBoard.PixelsPerL+" ";
		save += this.y/GameBoard.PixelsPerL+" ";
		save += this.vx+" ";
		save += this.vy+" ";
		save += this.radius+" ";
		return save;
	}
	
	//t的单位：ms
	public void move(double t)
	{
		//以秒为单位
		t /= 1000.0;
		if(callCount == 0)
		{
			gizmos = gb.getGizmo();
			callCount++;
		}
		if(this.absorbed == true)
		{
			//球被absorber吸收;
			return;
		}
		calMinTimeToGizmo(t);
		//在下一个tick会发生碰撞
		if(timeToClosestGizmo < t)
		{
			reflectGizmo(t);
		} else {
		      x = x + vx*t;
		      y = y + vy*t;
		      
		      //考虑摩擦力与重力
		      vy = vy + g*tl;
		      vx = vx * (1 - (0.025 * tl) - (0.025/20 * Math.abs(vx) * tl));
		      vy = vy * (1 - (0.025 * tl) - (0.025/20 * Math.abs(vy) * tl));
		    }
	}
	
	public void calMinTimeToGizmo(double t)
	{
		if(gizmos != null)
		{
		Iterator gizmoIter = gizmos.iterator();
		posVector = new Vect(x, y);
	    velVector = new Vect(vx, vy);
	    Circle thisCircle = new Circle(x, y, radius);
	    
		timeToClosestGizmo = Double.POSITIVE_INFINITY;
		Gizmo closestGizmo = null;
		Object closestShape = null;
	    while(gizmoIter.hasNext()){
	        Gizmo curGizmo = (Gizmo)gizmoIter.next();
	        List shapes = curGizmo.getBoundaryShape(this);
	        Iterator shapeIter = shapes.iterator();
	        // then iterate on the shapes within each gizmo
	        while (shapeIter.hasNext()) {
	          Object curShape = shapeIter.next();
	          double timeToCollision;
	          if (curShape instanceof physics.Circle) {
	              Circle curCircle = (Circle)curShape;
	              if (curGizmo instanceof Flipper) {
	                Flipper curFlipper = (Flipper)curGizmo;
	                Vect pivot = new Vect(curFlipper.getPivot().getX(),
	                                      curFlipper.getPivot().getY());
	                timeToCollision =
	                  Geometry.timeUntilRotatingCircleCollision(curCircle,
	                                                            pivot,
	                                                            curFlipper.getAngularVelocity(),
	                                                            thisCircle,
	                                                            velVector);
	              } else {
	                // otherwise we assume it's a stationary circle
	                timeToCollision =
	                  Geometry.timeUntilCircleCollision(curCircle,
	                                                    thisCircle,
	                                                    velVector);
	              }
	          }
	          else {
	              // if it's not a Circle, it's a LineSegment
	              LineSegment curSegment = (LineSegment)curShape;
	              if (curGizmo instanceof Flipper) {
	                Flipper curFlipper = (Flipper)curGizmo;
	                Vect pivot = new Vect(curFlipper.getPivot().getX(),
	                                      curFlipper.getPivot().getY());
	                timeToCollision =
	                  Geometry.timeUntilRotatingWallCollision(curSegment,
	                                                          pivot,
	                                                          curFlipper.getAngularVelocity(),
	                                                          thisCircle,
	                                                          velVector);
	              } else {
	                // otherwise we assume it's a wall
	                timeToCollision =
	                  Geometry.timeUntilWallCollision(curSegment,
	                                                  thisCircle,
	                                                  velVector);
	              }
	            }
	          if (timeToCollision < timeToClosestGizmo) {
	              this.closestGizmo = curGizmo;
	              this.closestShape = curShape;
	              this.timeToClosestGizmo = timeToCollision;
	              }
	        }
	    }
		}
	        
	}
	
	public void reflectGizmo(double t)
	{
		Circle thisCircle = new Circle(x,y,radius);
		if(closestShape instanceof Circle)
		{
			Circle curCircle = (Circle)closestShape;
		    if (closestGizmo instanceof Flipper) {
		        Flipper curFlipper = (Flipper)closestGizmo;
		        Vect pivot = new Vect(curFlipper.getPivot().getX(),
		                              curFlipper.getPivot().getY());
		        velVector = Geometry.reflectRotatingCircle(curCircle,
		                                                   pivot,
		                                                   curFlipper.getAngularVelocity(),
		                                                   thisCircle,
		                                                   velVector,
		                                                   curFlipper.getRefCoef());
		  
		      } else {
		        velVector = Geometry.reflectCircle(curCircle.getCenter(),
		                                           posVector,
		                                           velVector,
		                                           closestGizmo.getRefCoef());
		      }
		} else if (closestShape instanceof physics.LineSegment) {
		      LineSegment curSegment = (LineSegment)closestShape;
		      if (closestGizmo instanceof Flipper) {
		        Flipper curFlipper = (Flipper)closestGizmo;
		        Vect pivot = new Vect(curFlipper.getPivot().getX(),
		                              curFlipper.getPivot().getY());
		        velVector = Geometry.reflectRotatingWall(curSegment,
		                                                 pivot,
		                                                 curFlipper.getAngularVelocity(),
		                                                 thisCircle,
		                                                 velVector,
		                                                 curFlipper.getRefCoef());
		      } else {
		        velVector = Geometry.reflectWall(curSegment,
		                                         velVector,
		                                         closestGizmo.getRefCoef());
		      }
		    }
	    x = x + vx*timeToClosestGizmo;
	    y = y + vy*timeToClosestGizmo;
	    
	    
	    // then update to the new velocities that are the result of
	    // the reflection with the gizmo
	    vx = velVector.x();
	    vy = velVector.y();
	    
	    closestGizmo.fire(this);

	    double newTime = 1000*(t - timeToClosestGizmo); // 1000*ms = s
	    move(newTime);
	}
	
	public void paint(Graphics g) {
		  Rectangle clipRect = g.getClipBounds();

		  if (clipRect.intersects(this.boundingBox())) {
		    g.setColor(Color.YELLOW);
		    g.fillOval((int)Math.round(x)-radius, (int)Math.round(y)-radius,
		               radius+radius, radius+radius);
		  }	
	}
	 
	public Rectangle boundingBox() {
	    // a Rectangle is the x,y for the upper left corner and then the
	    // width and height
	    return new Rectangle((int)Math.round(x)-radius, (int)Math.round(y)-radius,
	                         radius+radius+1, radius+radius+1);
	}
	
	public void reset()
	{
		this.x = this.startX;
		this.y = this.startY;
		this.vx = this.startVX;
		this.vy = this.startVY;
		absorbed = false;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public Vect getVelVector() {
		return velVector;
	}

	public void setVelVector(Vect velVector) {
		this.velVector = velVector;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public boolean isAbsorbed() {
		return absorbed;
	}

	public void setAbsorbed(boolean absorbed) {
		this.absorbed = absorbed;
	}
	
	
}
