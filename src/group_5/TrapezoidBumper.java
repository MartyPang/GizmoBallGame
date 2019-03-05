package group_5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;

public class TrapezoidBumper extends Gizmo{

	private enum Position{
		top,
		right,
		bottom,
		left
	};
	private Position pos;
	
	public TrapezoidBumper(GameBoard gb,String name,int xInL,int yInL)
	{
		this.gb = gb;
		this.name = name;
		this.x = xInL*GameBoard.PixelsPerL;
		this.y = yInL*GameBoard.PixelsPerL;
		this.width = 2;
		this.height = 2;
		this.refCoef = 1.0;
		this.gt = GizmoType.TrapezoidBumper;
		pos = Position.top;
	}
	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		if(pos == Position.top){
			pos = Position.right;
		}else if(pos == Position.right){
			pos = Position.bottom;
		}else if(pos == Position.bottom){
			pos = Position.left;
		}else{
			pos = Position.top;
		}
	}

	
	@Override
	public void enlarge() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getSaveString() {
		// TODO Auto-generated method stub
		String save = "TrapezoidBumper "+name+" "+this.x/GameBoard.PixelsPerL+" "+this.y/GameBoard.PixelsPerL+" "+this.getWidth()+" "+this.getHeight();
		int rotatecount = 0;
		if(pos == Position.right){
			rotatecount = 1;
		}else if(pos == Position.bottom){
			rotatecount = 2;
		}else if(pos == Position.left){
			rotatecount = 3;
		}
		for(int i=0;i<rotatecount;++i)
		{
			save += "\r\nRotate "+name;
		}
		return save;
	}

	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.lightGray);
		double x1 = x+width*GameBoard.PixelsPerL/3;
		double y1 = y;
		double x2 = x+2*width*GameBoard.PixelsPerL/3;
		double y2 = y;
		double x3 = x+width*GameBoard.PixelsPerL;
		double y3 = y+width*GameBoard.PixelsPerL;
		double x4 = x;
		double y4 = y+width*GameBoard.PixelsPerL;
		switch(pos)
		{
		case top:break;
		case right:x1 = x+height*GameBoard.PixelsPerL;
		          y1 = y+height*GameBoard.PixelsPerL/3;
		          x2 = x+height*GameBoard.PixelsPerL;
		          y2 = y+2*width*GameBoard.PixelsPerL/3;
		          x3 = x;
		          y3 = y+height*GameBoard.PixelsPerL;
		          x4 = x;
		          y4 = y;
		          break;
		case left:x1 = x;
		          y1 = y+2*height*GameBoard.PixelsPerL/3;
		          x2 = x;
		          y2 = y+height*GameBoard.PixelsPerL/3;
		          x3 = x+width*GameBoard.PixelsPerL;
		          y3 = y;
		          x4 = x+width*GameBoard.PixelsPerL;
		          break;
		case bottom:x1 = x+2*width*GameBoard.PixelsPerL/3;
		            y1 = y+height*GameBoard.PixelsPerL;;
		            x2 = x+width*GameBoard.PixelsPerL/3;
		            y2 = y+height*GameBoard.PixelsPerL;
		            x3 = x;
		            y3 = y;
		            x4 = x+width*GameBoard.PixelsPerL;
		            y4 = y;
		            break;
		}
		int xPoints[] = {(int)x1,(int)x2,(int)x3,(int)x4};
		int yPoints[] = {(int)y1,(int)y2,(int)y3,(int)y4};
		int nPoints = 4;
		g.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public List getBoundaryShape(Ball b) {
		// TODO Auto-generated method stub
		List shapes = new ArrayList();
		double x1 = x+width*GameBoard.PixelsPerL/3;
		double y1 = y;
		double x2 = x+2*width*GameBoard.PixelsPerL/3;
		double y2 = y;
		double x3 = x+width*GameBoard.PixelsPerL;
		double y3 = y+width*GameBoard.PixelsPerL;
		double x4 = x;
		double y4 = y+width*GameBoard.PixelsPerL;
		switch(pos)
		{
		case top:break;
		case right:x1 = x+height*GameBoard.PixelsPerL;
		          y1 = y+height*GameBoard.PixelsPerL/3;
		          x2 = x+height*GameBoard.PixelsPerL;
		          y2 = y+2*width*GameBoard.PixelsPerL/3;
		          x3 = x;
		          y3 = y+height*GameBoard.PixelsPerL;
		          x4 = x;
		          y4 = y;
		          break;
		case left:x1 = x;
		          y1 = y+2*height*GameBoard.PixelsPerL/3;
		          x2 = x;
		          y2 = y+height*GameBoard.PixelsPerL/3;
		          x3 = x+width*GameBoard.PixelsPerL;
		          y3 = y;
		          x4 = x+width*GameBoard.PixelsPerL;
		          break;
		case bottom:x1 = x+2*width*GameBoard.PixelsPerL/3;
		            y1 = y+height*GameBoard.PixelsPerL;;
		            x2 = x+width*GameBoard.PixelsPerL/3;
		            y2 = y+height*GameBoard.PixelsPerL;
		            x3 = x;
		            y3 = y;
		            x4 = x+width*GameBoard.PixelsPerL;
		            y4 = y;
		            break;
		}
		shapes.add(new LineSegment(x1,y1,x2,y2));
		shapes.add(new LineSegment(x1,y1,x4,y4));
		shapes.add(new LineSegment(x2,y2,x3,y3));
		shapes.add(new LineSegment(x3,y3,x4,y4));
		
		shapes.add(new Circle(x1,y1,0));
		shapes.add(new Circle(x2,y2,0));
		shapes.add(new Circle(x3,y3,0));
		shapes.add(new Circle(x4,y4,0));
		
		return shapes;
	}

	@Override
	public void startAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void fire(Ball b) {
		// TODO Auto-generated method stub
		gb.addScore(10);
		super.fire(b);
	}

}
