package group_5;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;

public class CircleBumper extends Gizmo{

	public CircleBumper(GameBoard gb,String name,int xInL,int yInL)
	{
		this.gb = gb;
		this.name = name;
		this.x = xInL*GameBoard.PixelsPerL;
		this.y = yInL*GameBoard.PixelsPerL;
		this.width = 1;
		this.height = 1;
		this.gt = GizmoType.CircleBumper;
		this.refCoef = 1.0;
	}
	
	
	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		//Do nothing!!!
		//Assume circlebumper cannot rotate
	}

	
	
	@Override
	public void enlarge() {
		// TODO Auto-generated method stub
		
	}


	public String getSaveString()
	{
		String save = "CircleBumper "+this.name+" "+this.x/GameBoard.PixelsPerL+" "+this.y/GameBoard.PixelsPerL+" "+this.getWidth()+" "+this.getHeight();
		return save;
	}

	public void paint(Graphics2D g)
	{
		g.setColor(Color.RED);
		g.fillOval(x, y, width*GameBoard.PixelsPerL, height*GameBoard.PixelsPerL);
	}
	
	//圆形Bumper的边界就是一个圆
	public List getBoundaryShape(Ball b)
	{
		List  shapes = new ArrayList();
		double radius = width*GameBoard.PixelsPerL*0.5;
		shapes.add(new Circle(x+radius-0.5,y+radius-0.5,radius));
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
		gb.addScore(9);
		super.fire(b);
	}
	
}
