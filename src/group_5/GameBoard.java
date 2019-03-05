package group_5;


import group_5.Command.CommandType;
import group_5.Gizmo.GizmoType;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
import javax.swing.JPanel; 
import javax.swing.Timer;

public class GameBoard extends JPanel{

	//每个格子20像素,即 1L = 20 pixels
	protected static final int PixelsPerL = 20;
	protected static final int BoardWidth = 22;
	protected static final int BoardHeight = 22;
	protected static final int MSECPERTICK = 50;
	protected static final int FLIPPERANGVEL = 1080; // deg/sec
	//For the name of each Gizmo
	private static int countAbsorber;
	private static int countFlipper;
	private static int countCircle;
	private static int countSquare;
	private static int countTriangle;
	private static int countTrapezoid;
	
	Connection conn;
	GridMap grid;
	Gizmo activeGizmo;
	
	double gravity; //pixels/sec^2
	private Timer timerPlay;
	private Timer timerBuild;
	private List gizmo;
	private ArrayList activeList;
	Ball ball;
	OutWall outwall;

	private boolean mode;//true for play mode;
	                     //false for build mode;
	                     //default value: false.
	private static int score;
	
	private CommandType currentCommand;
	private boolean addOrNot;
	private boolean addBallOrNot;
	private boolean deleteOrNot;
	private boolean connectOrNot;
	private boolean disconnectOrNot;
	private boolean rotateOrNot;
	private boolean moveOrNot;
	private boolean enlargeOrNot;
	private boolean shrinkOrNot;
	private boolean mousecontain;
	
	private Rectangle rect;
	
    private EventListener playListener;
    private EventListener buildListener;
    
	public GameBoard()
	{
		super();
		this.setBackground(new Color(0,0,0));
	    
	    this.countAbsorber = 0;
	    this.countCircle = 0;
	    this.countFlipper = 0;
	    this.countSquare = 0;
	    this.countTriangle = 0;
	    this.countTrapezoid = 0;
	    this.score = 0;
		gravity = 500;
		playListener = new EventListener();
		buildListener = new EventListener();
		timerPlay = new Timer(50,playListener);
		timerBuild = new Timer(50,buildListener);
		//ball = new Ball(this,100,100,0,0,5);
		ball = null;
		outwall = new OutWall(this);
		gizmo = new ArrayList();
		activeList = new ArrayList();
		activeGizmo = null;
	
		mode = false;
		addOrNot = false;
		addBallOrNot = false;
		deleteOrNot = false;
		connectOrNot = false;
		disconnectOrNot = false;
		rotateOrNot = false;
		moveOrNot = false;
		enlargeOrNot = false;
		shrinkOrNot = false;
		mousecontain = false;
		
		rect = null;
		
		currentCommand = CommandType.standby;
		conn = new Connection();
		grid = new GridMap(BoardWidth,BoardHeight);

	    addMouseListener(buildListener);
		addMouseMotionListener(buildListener);
		addKeyListener(buildListener);
		
		gizmo.add(outwall);
		requestFocus(); 
		timerBuild.start();
	}
	
	public void setMode(boolean m)
	{
		//remove old listeners
		if(mode == true){
			removeKeyListener(playListener);
			removeMouseListener(playListener);
			removeMouseMotionListener(playListener);
			timerPlay.stop();
		}else{
			removeKeyListener(buildListener);
			removeMouseListener(buildListener);
			removeMouseMotionListener(buildListener);
			timerBuild.stop();
		}
		
		this.mode = m;
		
		//switch to play mode
		if(mode == true)
		{
			//ball.reset();
		    addMouseListener(playListener);
			addMouseMotionListener(playListener);
			addKeyListener(playListener);
			requestFocus();
			timerPlay.start();
		}else{
		    addMouseListener(buildListener);
			addMouseMotionListener(buildListener);
			addKeyListener(buildListener);
			if(ball == null){
				JOptionPane.showMessageDialog(this, "没球你玩个球啊！！！");
			}else{
				ball.reset();
			}
			requestFocus();
			paintGrid(this.getGraphics());
			timerBuild.start();
		}
			
	}
	
	public void addScore(int s)
	{
		score += s;
	}
	public int getScore()
	{
		return score;
	}
	public void resetScore()
	{
		score = 0;
	}
	public boolean getMode()
	{
		return this.mode;
	}
	public void setAddOrNot(boolean a)
	{
		this.addOrNot = a;
	}
	public void setAddBallOrNot(boolean a)
	{
		addBallOrNot = a;
	}
	public void setMoveOrNot(boolean a)
	{
		moveOrNot = a;
	}
	public void setDeleteOrNot(boolean a)
	{
		deleteOrNot = a;
	}
	public void setConnectOrNot(boolean a)
	{
		connectOrNot = a;
	}
	public void setDisconnectOrNot(boolean a)
	{
		disconnectOrNot = a;
	}
	public void setRotateOrNot(boolean a)
	{
		rotateOrNot = a;
	}
	public void setEnlargeOrNot(boolean a)
	{
		enlargeOrNot = a;
	}
	public void setShrinkOrNot(boolean a)
	{
		shrinkOrNot = a;
	}
	public void setCommand(CommandType ct)
	{
		this.currentCommand = ct;
	}
	public void addGizmo(double xpoint,double ypoint,int width,int height,String name,GizmoType gt)
	{
		int x = (int)(xpoint / this.PixelsPerL);
		int y = (int)(ypoint / this.PixelsPerL);
		switch(gt)
		{
		case CircleBumper:activeGizmo = new CircleBumper(this,name,x,y);
		                  if(grid.checkClear(activeGizmo, x, y)){
		                  gizmo.add(activeGizmo);grid.addGizmo(activeGizmo);countCircle++;activeGizmo = null;}
		                  else{JOptionPane.showMessageDialog(this, "此处无法放置Gizmo", "警告", JOptionPane.WARNING_MESSAGE);}break;
		case SquareBumper:activeGizmo = new SquareBumper(this,name,x,y);
                          if(grid.checkClear(activeGizmo, x, y)){
                        	  gizmo.add(activeGizmo);grid.addGizmo(activeGizmo);countSquare++;activeGizmo = null;}
                          else{JOptionPane.showMessageDialog(this, "此处无法放置Gizmo", "警告", JOptionPane.WARNING_MESSAGE);}break;
		case TriangleBumper:activeGizmo = new TriangleBumper(this,name,x,y);
                            if(grid.checkClear(activeGizmo, x, y)){
                            	gizmo.add(activeGizmo);grid.addGizmo(activeGizmo);countTriangle++;activeGizmo = null;}
                            else{JOptionPane.showMessageDialog(this, "此处无法放置Gizmo", "警告", JOptionPane.WARNING_MESSAGE);}break;
		case TrapezoidBumper:activeGizmo = new TrapezoidBumper(this,name,x,y);
                             if(grid.checkClear(activeGizmo, x, y)){
        	                     gizmo.add(activeGizmo);grid.addGizmo(activeGizmo);countTrapezoid++;activeGizmo = null;}
                             else{JOptionPane.showMessageDialog(this, "此处无法放置Gizmo", "警告", JOptionPane.WARNING_MESSAGE);}break;
		case LeftFlipper:activeGizmo = new Flipper(this,name, x,y,true);//conn.connectKeyDown(tmp, 76);
		                 if(grid.checkClear(activeGizmo, x, y)){
		                	 gizmo.add(activeGizmo);grid.addGizmo(activeGizmo);countFlipper++;activeGizmo = null;}
		                 else{JOptionPane.showMessageDialog(this, "此处无法放置Gizmo", "警告", JOptionPane.WARNING_MESSAGE);}break;
		case RightFlipper:activeGizmo = new Flipper(this,name, x,y,false);
                          if(grid.checkClear(activeGizmo, x, y)){
                        	  gizmo.add(activeGizmo);grid.addGizmo(activeGizmo);countFlipper++;activeGizmo = null;}
                          else{JOptionPane.showMessageDialog(this, "此处无法放置Gizmo", "警告", JOptionPane.WARNING_MESSAGE);}break;
		case Absorber:activeGizmo = new Absorber(this,name,x,y,width,height);
		                  if(grid.checkClear(activeGizmo, x, y)){
			                  gizmo.add(activeGizmo);grid.addGizmo(activeGizmo);countAbsorber++;activeGizmo = null;}
		                  else{JOptionPane.showMessageDialog(this, "此处无法放置Gizmo", "警告", JOptionPane.WARNING_MESSAGE);}break;
		}
		repaint();
		requestFocus();
	}
	public void addBall(double xpoint,double ypoint)
	{
		int x = (int)(xpoint / this.PixelsPerL);
		int y = (int)(ypoint / this.PixelsPerL);
		ball = new Ball(this,x*PixelsPerL,y*PixelsPerL,0,0,5);
	}
	public void moveGizmo(Gizmo giz,int x,int y)
	{		
		if(grid.checkClear(giz, x, y)){
			grid.remove(giz);
			giz.setX(x*PixelsPerL);
			giz.setY(y*PixelsPerL);
			grid.addGizmo(giz);
			repaint();
		}else{
			JOptionPane.showMessageDialog(this, "You cannot place gizmo here.", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}
	public void deleteGizmo(double xpoint,double ypoint)
	{
		int x = (int)(xpoint / this.PixelsPerL);
		int y = (int)(ypoint / this.PixelsPerL);
		if(ball != null){
			if((int)(ball.getX()/PixelsPerL) == x&&(int)(ball.getY()/PixelsPerL) == y){
				ball = null;
				}else{
					Gizmo tmp = grid.get(x, y);	
			        if(tmp == null)
			        {
			        	//do nothing
			            return;
			        }
			        gizmo.remove(tmp);
	    	        grid.remove(tmp);
				}
		}else{
			Gizmo tmp = grid.get(x, y);	
	        if(tmp == null)
	        {
	        	//do nothing
	            return;
	        }
	        gizmo.remove(tmp);
	        grid.remove(tmp);
		}
		repaint();
	}
	
	public void connectGizmoToGizmo(Gizmo source,Gizmo target)
	{
		conn.connectGizmo(source, target);
		source.connect(target);
		JOptionPane.showMessageDialog(this, "Gizmo "+source.getName()+ "与Gizmo "+target.getName()+"连接成功！！！");
	}

	public void connectGizmoToKey(Gizmo source,Integer keynum)
	{
		conn.connectKeyDown(source, keynum);
	}
	
	public void disconnectAll(Gizmo giz)
	{
		conn.removeConnect(giz);
	}
	public void rotateGizmo(Gizmo giz)
	{
		giz.rotate();
		repaint(giz.boundingBox());
	}
	//成倍增大一个gizmo
	public void enlargeGizmo(Gizmo giz)
	{
		int xInL = giz.getX()/PixelsPerL;
		int yInL = giz.getY()/PixelsPerL;
		int widthInL = giz.getWidth();
		int heightInL = giz.getHeight();
		boolean flag = true;
		if(giz instanceof TriangleBumper || giz instanceof SquareBumper || giz instanceof TrapezoidBumper || giz instanceof CircleBumper){
			if(yInL-1<0 || xInL+widthInL+1>21){
				flag = false;
			}else{
				for(int i=xInL;i<xInL+widthInL+1;++i){
					for(int j=yInL-1;j<yInL+heightInL;++j){
						if(grid.get(i, j) != null && !grid.get(i, j).equals(giz)){
							flag = false;
							break;
						}
					}
				}
			}
			if(flag){
				grid.remove(giz);
				giz.setY((yInL-1)*PixelsPerL);
				giz.setWidth(widthInL+1);
				giz.setHeight(heightInL+1);
				grid.addGizmo(giz);
			}else{
		    	JOptionPane.showMessageDialog(this, "已经放大到极限了！！！", "Warning", JOptionPane.WARNING_MESSAGE);
		    }
		}else{
			JOptionPane.showMessageDialog(this, giz.getName()+"不能被放大！！！");
		}

	}
	//缩小一个gizmo
	public void shrinkGizmo(Gizmo giz)
	{
		int xInL = giz.getX()/PixelsPerL;
		int yInL = giz.getY()/PixelsPerL;
		int widthInL = giz.getWidth();
		int heightInL = giz.getHeight();
		boolean flag = true;
		if(giz instanceof CircleBumper || giz instanceof SquareBumper || giz instanceof TriangleBumper){
			if(widthInL == 1 && heightInL == 1){
				flag = false;
			}
		}else if(giz instanceof TrapezoidBumper){
			if(widthInL == 2){
				flag = false;
			}
		}
		if(flag){
			if(giz instanceof Flipper || giz instanceof Absorber){
				JOptionPane.showMessageDialog(this, giz.getName()+"不能被缩小");
			}else{
				grid.remove(giz);
				giz.setY((yInL+1)*PixelsPerL);
				giz.setWidth(widthInL-1);
				giz.setHeight(heightInL-1);
				grid.addGizmo(giz);
			}
		}else{
			JOptionPane.showMessageDialog(this, "已经缩小到极限！！！");
		}
	}
	
	public void saveFile(String filename)
	{
		try{
			PrintStream output = new PrintStream(new FileOutputStream(filename));
			
			//save gizmo
			if(gizmo.size() != 0)
			{
				for(int i=0;i<gizmo.size();++i)
				{
					Gizmo tmp = (Gizmo) gizmo.get(i);
					if(tmp instanceof OutWall)
					{
						
					}else{
						output.println(tmp.getSaveString());
					}
				}
			}
			
			//save ball
			output.println(ball.getSaveString());
			
			//save connection
			HashMap gizmoTogizmo = conn.getGizmoConnect();
			if(gizmo.size() != 0)
			{
				for(int j=0;j<gizmo.size();++j)
				{
					Gizmo giz = (Gizmo) gizmo.get(j);
					ArrayList tmp = (ArrayList)gizmoTogizmo.get(giz);
					if(tmp == null)
					{
						
					}
					else
					{
										
						for(int k =0;k<tmp.size();++k)
					    {
							Gizmo giz2 = (Gizmo) tmp.get(k);
						    output.println("ConnectGizmo "+giz.getName()+ " "+giz2.getName()); 
					    }
					}

				}
			}
			HashMap keyDown = conn.getKeyConnectDown();
			if(gizmo.size() != 0)
			{
			    Iterator keyBindingsDown = keyDown.keySet().iterator();
			    while (keyBindingsDown.hasNext()) {
			    	Integer keynum = (Integer)keyBindingsDown.next();
					ArrayList boundGizmo = (ArrayList) keyDown.get(keynum);
				    for(int j=0;j<boundGizmo.size();++j)
				    {
					
				    	Gizmo giz = (Gizmo)boundGizmo.get(j);
					    output.println("ConnectKeyDown "+keynum.toString()+" "+giz.getName());
				    }
			    }
			}
			
			
		}catch(FileNotFoundException e){
			
		}
	}
	
	public void loadFile(String filename)
	{
		gizmo = null;
		gizmo = new ArrayList();
		grid = null;
		grid = new GridMap(BoardWidth,BoardHeight);
		conn = null;
		conn = new Connection();
		
		activeGizmo = null;
		activeList = null;
		activeList = new ArrayList();
		outwall = null;
		outwall = new OutWall(this);
		ball = null;
		gizmo.add(outwall);
		grid.addWalls(outwall);
		
	    this.countAbsorber = 0;
	    this.countCircle = 0;
	    this.countFlipper = 0;
	    this.countSquare = 0;
	    this.countTriangle = 0;
	    this.countTrapezoid = 0;
	    
		String file = fileRead(filename);
	    StringTokenizer lineReader = new StringTokenizer(file, "\n");
	    
	    while (lineReader.hasMoreTokens()) {
	    	String line = lineReader.nextToken();
	        StringTokenizer command = new StringTokenizer(line);
	        String type = command.nextToken();
	        Gizmo giz;
	        if(type.equals("CircleBumper"))
	        {
	        	giz = new CircleBumper(this,command.nextToken(),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()));
	        	giz.setWidth(Integer.parseInt(command.nextToken()));
	        	giz.setHeight(Integer.parseInt(command.nextToken()));
	        	countCircle++;
	        	gizmo.add(giz);
	        	grid.addGizmo(giz);
	        }else if(type.equals("SquareBumper"))
	        {
	        	giz = new SquareBumper(this,command.nextToken(),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()));
	        	giz.setWidth(Integer.parseInt(command.nextToken()));
	        	giz.setHeight(Integer.parseInt(command.nextToken()));
	        	countSquare++;
	        	gizmo.add(giz);
	        	grid.addGizmo(giz);
	        }else if(type.equals("TriangleBumper"))
	        {
	        	giz = new TriangleBumper(this,command.nextToken(),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()));
	        	giz.setWidth(Integer.parseInt(command.nextToken()));
	        	giz.setHeight(Integer.parseInt(command.nextToken()));
	        	countTriangle++;
	        	gizmo.add(giz);
	        	grid.addGizmo(giz);
	        }else if(type.equals("TrapezoidBumper"))
	        {
	        	giz = new TrapezoidBumper(this,command.nextToken(),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()));
	        	giz.setWidth(Integer.parseInt(command.nextToken()));
	        	giz.setHeight(Integer.parseInt(command.nextToken()));
	        	countTrapezoid++;
	        	gizmo.add(giz);
	        	grid.addGizmo(giz);
	        }else if(type.equals("LeftFlipper"))
	        {
	        	giz = new Flipper(this,command.nextToken(),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()),true);
	        	countFlipper++;
	        	gizmo.add(giz);
	        	grid.addGizmo(giz);
	        }else if(type.equals("RightFlipper"))
	        {
	        	giz = new Flipper(this,command.nextToken(),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()),false);
	        	countFlipper++;
	        	gizmo.add(giz);
	        	grid.addGizmo(giz);
	        }else if(type.equals("Absorber"))
	        {
	        	giz = new Absorber(this,command.nextToken(),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()),Integer.parseInt(command.nextToken()));
	        	countAbsorber++;
	        	gizmo.add(giz);
	        	grid.addGizmo(giz);
	        }else if(type.equals("Ball"))
	        {
	        	ball = new Ball(this,Double.parseDouble(command.nextToken())*PixelsPerL,Double.parseDouble(command.nextToken())*PixelsPerL,Double.parseDouble(command.nextToken()),Double.parseDouble(command.nextToken()),Integer.parseInt(command.nextToken()));
	        } else if(type.equals("ConnectGizmo"))
	        {
	        	Gizmo giz1 = get(command.nextToken());
	        	Gizmo giz2 = get(command.nextToken());
	        	connectGizmoToGizmo(giz1, giz2);
	        	
	        }else if(type.equals("ConnectKeyDown"))
	        {
	        	int keynum = Integer.parseInt(command.nextToken());
	        	Gizmo target = get(command.nextToken());
	        	conn.connectKeyDown(target, new Integer(keynum));
	        }else if(type.equals(("Rotate"))){
	        	Gizmo giz1 = get(command.nextToken());
	        	giz1.rotate();
	        }
	    }
	    repaint();

	}
    private static String fileRead(String filename) {	
    	if (filename == null)throw new RuntimeException("No file specified");
	    String answer = new String();
	    try {
	    
	    	BufferedReader in = new BufferedReader(new FileReader(filename));
	        // read each line until the end of the file
	        for (String line = in.readLine(); line != null;line = in.readLine()) {
	        	// if the line is blank, or has a # as it's 1st
	    	    // character, ignore it
	    	    if (!(line.trim().equals("") || line.startsWith("#")))
	    	    	answer += line.trim() + "\n";  
	        }
	    }    
	    catch (Exception e) {
	          throw new RuntimeException("File not accessible");
	    }
	    return answer;
    }
    public Gizmo get(String name)
    {
    	if(gizmo == null)return null;
    	for(int i =0;i<gizmo.size();++i)
    	{
    		Gizmo giz = (Gizmo) gizmo.get(i);
    		if(name.equals(giz.getName()))
    		{
    			return giz;
    		}
    	}
    	return null;
    }
	public List getGizmo() {
		return gizmo;
	}

	public void setGizmo(List gizmo) {
		this.gizmo = gizmo;
	}
	
	public Gizmo getActiveGizmo()
	{
		return activeGizmo;
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		paintGrid(g);
		Graphics2D g2 = (Graphics2D) g;
		outwall.paint(g2);
		Iterator gizmoIter = gizmo.iterator();
		while(gizmoIter.hasNext())
		{
			Gizmo tmp = (Gizmo)gizmoIter.next();
			tmp.paint(g2);
		}
		if(ball != null)
		{
			ball.paint(g2);
		}
		if(mousecontain && !mode){
			g2.setColor(Color.RED);
			g2.draw(rect);
		}	
	}

	public void paintGrid(Graphics g)
	{
		if(mode == true){
			g.setColor(Color.black);
		}else{
			g.setColor(Color.green);
		}
	    //paint all vertical lines
	    for (int i = 1; i < BoardWidth; i++) {
	      g.drawLine(i*PixelsPerL, PixelsPerL,
	                 i*PixelsPerL, (BoardHeight-1)*PixelsPerL);
	    }
	    //paint all horizontal lines
	    for (int i = 1; i < BoardHeight; i++) {
	      g.drawLine(PixelsPerL, i*PixelsPerL,
	                 (BoardWidth-1)*PixelsPerL, i*PixelsPerL);
	    }
	}
	public void paintRect(int x,int y)
	{
		rect = new Rectangle(x*PixelsPerL,y*PixelsPerL,PixelsPerL,PixelsPerL);
		repaint();
	}

	public void addToActiveList(Gizmo giz)
	{
		activeList.add(giz);
	}


	public void removeFromActiveList(Gizmo giz)
	{
		activeList.remove(giz);
	}
	//以下代码没有考虑Flipper的旋转，waiting to be fixed.
	public void moveUp(Gizmo giz)
	{
		int i= 0;
		for(i=0;i<gizmo.size();++i)
		{
			if(gizmo.get(i) == giz)break;
		}
		Gizmo tmp = (Gizmo) gizmo.get(i);
		int xInL = tmp.getX()/GameBoard.PixelsPerL;
		int yInL = tmp.getY()/GameBoard.PixelsPerL-1;
	    if(grid.get(xInL,yInL) == null){		
			grid.remove(tmp);
			tmp.moveByY(-1*20);
		    grid.addGizmo(tmp);
		    repaint();
		}
	}
	public void moveDown(Gizmo giz)
	{
		int i= 0;
		for(i=0;i<gizmo.size();++i)
		{
			if(gizmo.get(i) == giz)break;
		}
		
		Gizmo tmp = (Gizmo) gizmo.get(i);
		int xInL = tmp.getX()/GameBoard.PixelsPerL;
		int yInL = tmp.getY()/GameBoard.PixelsPerL+2;
		if(grid.get(xInL,yInL) == null){		
			grid.remove(tmp);
		    tmp.moveByY(1*20);
		    grid.addGizmo(tmp);
		    repaint();
		}

	}
	public void moveLeft(Gizmo giz)
	{
		int i= 0;
		for(i=0;i<gizmo.size();++i)
		{
			if(gizmo.get(i) == giz)break;
		}
		int xInL = giz.getX()/GameBoard.PixelsPerL;
		int yInL = giz.getY()/GameBoard.PixelsPerL;
			
		if(grid.get(xInL-1, yInL) == null)		
			if(grid.get(xInL-1, yInL+1) == null){	
				Gizmo tmp = (Gizmo) gizmo.get(i);
				grid.remove(tmp);
				tmp.moveByX(-1*20);
				grid.addGizmo(tmp);	
			}
		repaint();
	}
	public void moveRight(Gizmo giz)
	{
		int i= 0;
		for(i=0;i<gizmo.size();++i)
		{
			if(gizmo.get(i) == giz)break;
		}
		int xInL = giz.getX()/GameBoard.PixelsPerL;
		int yInL = giz.getY()/GameBoard.PixelsPerL;	
		
		if(grid.get(xInL+2, yInL) == null){
			if(grid.get(xInL+2, yInL+1) == null){	
				Gizmo tmp = (Gizmo) gizmo.get(i);
		        grid.remove(tmp);
		        tmp.moveByX(1*20);
		        grid.addGizmo(tmp);
			}
		}

		repaint();
	}
	
	class EventListener extends MouseAdapter
	implements MouseMotionListener, KeyListener, ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
		     
			if(mode)
			{
				if(ball == null){
					setMode(false);
				}else{	
					ball.move(MSECPERTICK);
			        for (int i = 0; i < activeList.size(); i++) {	
			        	Gizmo activegiz = (Gizmo)activeList.get(i);		    	
				        activegiz.action();	    	
			        }
				}
			}
			repaint();

		}

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
		    int keynum = arg0.getKeyCode();

		    ArrayList firedGizmos = (ArrayList)conn.getKeyConnectDown().get(new Integer(keynum));
		    if (firedGizmos != null) {
		    	//System.out.println(keynum);
		    	for (int i = 0; i < firedGizmos.size(); i++) {
		    		Gizmo firedGiz = (Gizmo)firedGizmos.get(i);
		    		//以下为需求变更	
		    		if(firedGiz instanceof Flipper)
		    		{
		    			if(keynum == arg0.VK_UP||keynum == arg0.VK_W){
		    				moveUp(firedGiz);
		    			}else if(keynum == arg0.VK_DOWN||keynum == arg0.VK_S){
		    				moveDown(firedGiz);
		    			}else if(keynum == arg0.VK_LEFT||keynum == arg0.VK_A){
		    				moveLeft(firedGiz);
		    			}else if(keynum == arg0.VK_RIGHT||keynum == arg0.VK_D){
		    				moveRight(firedGiz);
		    			}else{
		    				firedGiz.startAction();
		    			}
		    		}
		    		else{
		    			firedGiz.startAction();
		    		}
		    		
			        
		    	}
		    }
		    //for connect
		    if(activeGizmo == null){
		    	
		    }else{
		    	//System.out.println(4);
		    	connectGizmoToKey(activeGizmo,new Integer(keynum));
		    	activeGizmo =null;
		    	connectOrNot = false;
		    }
		    

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(!getMode())
			{
				double xpoint = arg0.getPoint().getX();
			    double ypoint = arg0.getPoint().getY();
			    int x = (int) xpoint / PixelsPerL;
			    int y = (int) ypoint / PixelsPerL;
				if(addOrNot)
				{
				    GizmoType gt = GizmoType.CircleBumper;
				    String name = "";int width = 1;int height = 1;
				    switch(currentCommand)
				    {
				    case addCircle:gt = GizmoType.CircleBumper;name = "C"+countCircle;break;
				    case addSquare:gt = GizmoType.SquareBumper;name = "S"+countSquare;break;
			        case addTriangle:gt = GizmoType.TriangleBumper;name = "T"+countTriangle;break;
			        case addTrapezoid:gt = GizmoType.TrapezoidBumper;name = "Tr"+countTrapezoid;break;
			        case addLeftFlipper:gt = GizmoType.LeftFlipper;name = "F"+countFlipper;break;
			        case addRightFlipper:gt = GizmoType.RightFlipper;name = "F"+countFlipper;break;
			        case addAbsorber:gt = GizmoType.Absorber;name = "A"+countAbsorber;break;
				    }
				    if(gt == GizmoType.Absorber)
				    {
				    	width = Integer.parseInt(JOptionPane.showInputDialog("Please enter the width of this absorber", 1));
				    	height = Integer.parseInt(JOptionPane.showInputDialog("Please enter the height of this absorber", 1));
				    }
				    addGizmo(xpoint, ypoint, width,height,name, gt);
				    addOrNot = false;
				}
				if(addBallOrNot){
					addBall(xpoint,ypoint);
					addBallOrNot = false;
				}
				if(moveOrNot)
				{
					if(activeGizmo == null){
						activeGizmo = grid.get(x, y);
						if(activeGizmo == null){
							moveOrNot = false;
						}
					}else{
						moveGizmo(activeGizmo,x,y);
						activeGizmo = null;
						moveOrNot = false;
					}
				}
				if(deleteOrNot && currentCommand == CommandType.deleteGizmo)
				{
				    deleteGizmo(xpoint,ypoint);
				    deleteOrNot = false;
				}
				if(connectOrNot && currentCommand == CommandType.connect)
				{
				    
				    //选择 source gizmo
				    if(activeGizmo == null)
				    {
				    	activeGizmo = grid.get(x, y);//System.out.println(1);
				    	if(activeGizmo == null){
				    		//点击处无Gizmo存在
				    		
				    		connectOrNot = false;
				    	}
				    }
				    //选择target gizmo
				    else{
				    	Gizmo target = grid.get(x, y);
				    	if(target == null)
				    	{
				    		//点击处无Gizmo存在
				    		//System.out.println(2);
				    		connectOrNot = false;
				    	}
				    	else{
				    		//System.out.println(3);
				    		connectGizmoToGizmo(activeGizmo,target);
				    	    activeGizmo = null;
				    	    connectOrNot = false;
				    	}
				    	
				    }
					
				}
				if(disconnectOrNot){
					activeGizmo = grid.get(x, y);
					if(activeGizmo != null){
						disconnectAll(activeGizmo);
						activeGizmo = null;
						disconnectOrNot = false;
					}
					else{
						disconnectOrNot = false;
					}
				}
				if(rotateOrNot){
					activeGizmo = grid.get(x, y);
					if(activeGizmo != null){
						rotateGizmo(activeGizmo);
						activeGizmo = null;
						rotateOrNot = false;
					}else{
						rotateOrNot = false;
					}
				}
				if(enlargeOrNot && currentCommand == CommandType.enlargeGizmo)
				{
					activeGizmo = grid.get(x, y);
					if(activeGizmo != null){
						enlargeGizmo(activeGizmo);
						activeGizmo = null;
						enlargeOrNot = false;
					}else{
						enlargeOrNot = false;
					}
				}
				if(shrinkOrNot && currentCommand == CommandType.shrinkGizmo)
				{				
					activeGizmo = grid.get(x, y);
				    if(activeGizmo != null){
				    	shrinkGizmo(activeGizmo);
					    activeGizmo = null;
					    shrinkOrNot = false;
				    }else{
				    	shrinkOrNot = false;
				}
					
				}
			}
			//play mode
			else
			{
				int buttonnum = arg0.getButton();
				ArrayList firedGizmos = (ArrayList)conn.getMouseConnectDown().get(buttonnum);
			    if (firedGizmos != null) {
			    	//System.out.println(keynum);
			    	for (int i = 0; i < firedGizmos.size(); i++) {
			    		Gizmo firedGiz = (Gizmo)firedGizmos.get(i);
				        firedGiz.startAction();
			    	}
			    }
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			//System.out.print(1);
			double x = arg0.getPoint().getX();
			double y = arg0.getPoint().getY();
			int x1 = (int)(x/PixelsPerL);
			int y1 = (int)(y/PixelsPerL);
			mousecontain = true;
			paintRect(x1,y1);
		}
		
		
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			double x = e.getPoint().getX();
			double y = e.getPoint().getY();
			int x1 = (int)(x/PixelsPerL);
			int y1 = (int)(y/PixelsPerL);
			paintRect(x1,y1);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			mousecontain = false;
		}
		
		
		
		
		
		
	}

}

