package group_5;

import group_5.Gizmo.GizmoType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;

public class SquareBumper extends Gizmo {
	
	public SquareBumper(GameBoard gb,String name,int x,int y)
	{
		this.width = 1;
		this.height = 1;
		this.x = x*GameBoard.PixelsPerL;
		this.y = y*GameBoard.PixelsPerL;
		this.refCoef = 1.0;
		this.name = name;
		this.gb = gb;
		this.gt = GizmoType.SquareBumper;
	}

	
	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		//Do nothing!!!
		//Assume squarebumper cannot rotate
	}
	
	
	
	@Override
	public void enlarge() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getSaveString() {
		// TODO Auto-generated method stub
		String save = "SquareBumper "+this.name+" "+this.x/GameBoard.PixelsPerL+" "+this.y/GameBoard.PixelsPerL+" "+this.getWidth()+" "+this.getHeight();
		return save;
	}

	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width*GameBoard.PixelsPerL, height*GameBoard.PixelsPerL);
	}

	@Override
	public List getBoundaryShape(Ball b) {
		// TODO Auto-generated method stub
		List shapes = new ArrayList();
	    double x2 = x+width*GameBoard.PixelsPerL-1;
	    double y2 = y+height*GameBoard.PixelsPerL-1;
	    shapes.add(new LineSegment(x, y, x2, y));
	    shapes.add(new LineSegment(x, y2, x2, y2));
	    shapes.add(new LineSegment(x, y, x, y2));
	    shapes.add(new LineSegment(x2, y, x2, y2));
	    //0-radius circles at corners
	    shapes.add(new Circle(x, y, 0));
	    shapes.add(new Circle(x, y2, 0));
	    shapes.add(new Circle(x2, y, 0));
	    shapes.add(new Circle(x2, y2, 0));
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
		gb.addScore(7);
		super.fire(b);
	}
	
}
