package group_5;


import group_5.Gizmo.GizmoType;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import physics.Circle;
import physics.LineSegment;

public class TriangleBumper extends Gizmo {
	
	//记录三角形直角位置，有左上右上左下右下四种情况
	private enum RightAngle{
		leftTop,
		rightTop,
		leftBottom,
		rightBottom
	};
	private RightAngle pos;
	
	//constructor
	public TriangleBumper(GameBoard gb,String name,int x,int y)
	{
		this.width = 1;
		this.height = 1;
		this.x = x*GameBoard.PixelsPerL;
		this.y = y*GameBoard.PixelsPerL;
		this.name = name;
		this.gb = gb;
		this.refCoef = 1.0;
		this.gt = GizmoType.TriangleBumper;
		//默认直角在左下方,如下形状
		//
		//  |\
		//  |_\
		pos = RightAngle.leftBottom;
	}

	
	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		if(pos == RightAngle.leftTop){
			pos = RightAngle.rightTop;
		}else if(pos == RightAngle.rightTop){
			pos = RightAngle.rightBottom;
		}else if(pos == RightAngle.rightBottom){
			pos = RightAngle.leftBottom;
		}else{
			pos = RightAngle.leftTop;
		}
	}


	@Override
	public void enlarge() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getSaveString() {
		// TODO Auto-generated method stub
		String save = "TriangleBumper "+this.name+" "+this.x/GameBoard.PixelsPerL+" "+this.y/GameBoard.PixelsPerL+" "+this.getWidth()+" "+this.getHeight();
		int rotatecount = 0;
		if(pos == RightAngle.leftTop){
			rotatecount = 1;
		}else if(pos == RightAngle.rightTop){
			rotatecount = 2;
		}else if(pos == RightAngle.rightBottom){
			rotatecount = 3;
		}else{
			rotatecount = 0;
		}
		for(int i =0;i<rotatecount;++i)
		{
			save += "\r\n"+"Rotate "+name;
		}
		return save;
	}


	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.WHITE);
		//(x1,y1)代表直角的坐标
		int x1,x2,x3,y1,y2,y3;
		//默认情况
		x1 = x;
        y1 = y+height*GameBoard.PixelsPerL;
        x2 = x+width*GameBoard.PixelsPerL;
        y2 = y+height*GameBoard.PixelsPerL;
        x3 = x;
        y3 = y;
		switch(pos)
		{
		case leftTop:x1 = x;
		             y1 = y;
		             x3 = x+width*GameBoard.PixelsPerL;
		             y3 = y;
		             x2 = x;
		             y2 = y+height*GameBoard.PixelsPerL;
		             break;
		case rightTop:x1 = x+width*GameBoard.PixelsPerL;
		              y1 = y;
		              x3 = x;
		              y3 = y;
		              x2 = x+width*GameBoard.PixelsPerL;
		              y2 = y+height*GameBoard.PixelsPerL;
		              break;
		case leftBottom:x1 = x;
                        y1 = y+height*GameBoard.PixelsPerL;
                        x3 = x+width*GameBoard.PixelsPerL;
                        y3 = y+height*GameBoard.PixelsPerL;
                        x2 = x;
                        y2 = y;
                        break;
		case rightBottom:x1 = x+width*GameBoard.PixelsPerL;
		                 y1 = y+height*GameBoard.PixelsPerL;
		                 x2 = x+width*GameBoard.PixelsPerL;
		                 y2 = y;
		                 x3 = x;
		                 y3 = y+height*GameBoard.PixelsPerL;
		                 break;
		}
		int xPoints[] = {x1,x2,x3};
		int yPoints[] = {y1,y2,y3};
		int nPoints = 3;
		//填充多边形
		g.fillPolygon(xPoints, yPoints, nPoints);
	}

	@Override
	public List getBoundaryShape(Ball b) {
		// TODO Auto-generated method stub
		List shapes = new ArrayList();
		int x1,x2,x3,y1,y2,y3;
		//默认情况
		x1 = x;
        y1 = y+height*GameBoard.PixelsPerL;
        x2 = x+width*GameBoard.PixelsPerL;
        y2 = y+height*GameBoard.PixelsPerL;
        x3 = x;
        y3 = y;
		switch(pos)
		{
		case leftTop:x1 = x;
		             y1 = y;
		             x3 = x+width*GameBoard.PixelsPerL;
		             y3 = y;
		             x2 = x;
		             y2 = y+height*GameBoard.PixelsPerL;
		             break;
		case rightTop:x1 = x+width*GameBoard.PixelsPerL;
		              y1 = y;
		              x3 = x;
		              y3 = y;
		              x2 = x+width*GameBoard.PixelsPerL;
		              y2 = y+height*GameBoard.PixelsPerL;
		              break;
		case leftBottom:x1 = x;
                        y1 = y+height*GameBoard.PixelsPerL;
                        x3 = x+width*GameBoard.PixelsPerL;
                        y3 = y+height*GameBoard.PixelsPerL;
                        x2 = x;
                        y2 = y;
                        break;
		case rightBottom:x1 = x+width*GameBoard.PixelsPerL;
		                 y1 = y+height*GameBoard.PixelsPerL;
		                 x2 = x+width*GameBoard.PixelsPerL;
		                 y2 = y;
		                 x3 = x;
		                 y3 = y+height*GameBoard.PixelsPerL;
		                 break;
		}
	    shapes.add(new LineSegment(x1, y1, x2, y2));
	    shapes.add(new LineSegment(x1, y1, x3, y3));
	    shapes.add(new LineSegment(x2, y2, x3, y3));
	    //0-radius circles at corners
	    shapes.add(new Circle(x1, y1, 0));
	    shapes.add(new Circle(x2, y2, 0));
	    shapes.add(new Circle(x3, y3, 0));
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
		gb.addScore(5);
		super.fire(b);
	}

	
}