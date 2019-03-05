package group_5;

import group_5.Gizmo.GizmoType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;

public class OutWall extends Gizmo{

	public OutWall(GameBoard gb)
	{
		this.gb = gb;
		this.name = "OuterWalls";
		this.x = this.y = 0;
		this.width = GameBoard.BoardWidth;
		this.height = GameBoard.BoardHeight;
		this.refCoef = 1.0;
		this.gt = GizmoType.OuterWalls;
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


	@Override
	public String getSaveString() {
		// TODO Auto-generated method stub
		return "";
	}

	public void paint(Graphics2D g)
	{
		g.setColor(Color.GRAY);
		
		//画四个矩形;
		g.fillRect(x, y, GameBoard.PixelsPerL, GameBoard.BoardHeight*GameBoard.PixelsPerL);
		g.fillRect(x+GameBoard.PixelsPerL, y, 21*GameBoard.PixelsPerL, GameBoard.PixelsPerL);
		g.fillRect(x+GameBoard.PixelsPerL, y+21*GameBoard.PixelsPerL, 21*GameBoard.PixelsPerL, GameBoard.PixelsPerL);
		g.fillRect(x+21*GameBoard.PixelsPerL, y+GameBoard.PixelsPerL, GameBoard.PixelsPerL, 20*GameBoard.PixelsPerL);
		
	}
	
	public List getBoundaryShape(Ball b)
	{
		List shapes = new ArrayList();
		//边界为内部矩形的四条边和顶点处半径为0的四个圆;
		int x1 = x+20;
		int y1 = y+20;
		int x2 = x+20;
		int y2 = y+21*20;
		int x3 = x+21*20;
		int y3 = y+20;
		int x4 = x+21*20;
		int y4 = y+21*20;
		
		shapes.add(new LineSegment(x1,y1,x2,y2));
		shapes.add(new LineSegment(x1,y1,x3,y3));
		shapes.add(new LineSegment(x2,y2,x4,y4));
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
		gb.addScore(-10);
		super.fire(b);
	}
	
	
}
