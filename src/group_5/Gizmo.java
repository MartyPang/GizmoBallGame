package group_5;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class Gizmo {

	public enum GizmoType{
		CircleBumper,
		SquareBumper,
		TriangleBumper,
		TrapezoidBumper,
		OuterWalls,
		Flipper,
		LeftFlipper,
		RightFlipper,
		Absorber
	};
	protected int x,y,width,height;
	//ÿ��Gizmo���������֣����ڱ����ļ�
	protected String name;
	protected double refCoef;//����ϵ��
	//List of targets to be triggered upon fire
	protected List triggers = new ArrayList();
	GizmoType gt;
	
	protected GameBoard gb;
	//move
	public void moveByX(int x)
	{
		//�������߽磬���ƶ���
		if(getX()+x>=20 && getX()+x<=420)
		{
			this.x += x;
		}
		
	}
	public void moveByY(int y)
	{
		//�������߽磬���ƶ���
		if(getY()+y>=20 && getY()+y<=380)
		{
			this.y += y;
		}
	}
	
	public abstract void rotate();
	public abstract void enlarge();
	public abstract String getSaveString();
	public abstract void paint(Graphics2D g);
	public abstract List getBoundaryShape(Ball b);
	public abstract void startAction();
	public abstract void action();

	public GizmoType getType()
	{
		return this.gt;
	}
	public String getName()
	{
		return this.name;
	}
	public Rectangle boundingBox() {
		return new Rectangle(x, y, width*GameBoard.PixelsPerL, height*GameBoard.PixelsPerL);
    }
	public void connect(Gizmo giz){
		triggers.add(giz);
	}
	//fire ���gizmo�����������gizmo��startAction()
	public void fire(Ball b)
	{
		for(int i = 0;i<triggers.size();++i){
			Gizmo giz = (Gizmo) triggers.get(i);
			giz.startAction();
		}
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public double getRefCoef() {
		return refCoef;
	}
	public void setRefCoef(double refCoef) {
		this.refCoef = refCoef;
	}
	
	
	
}
