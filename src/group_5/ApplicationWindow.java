package group_5;


import group_5.Command.CommandType;
import group_5.Gizmo.GizmoType;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.Timer;

public class ApplicationWindow extends JFrame implements ActionListener,KeyListener,MouseListener{


	
	
	//For GUI  
	private JToolBar toolBar;
	private JToolBar gizmoBar;
	private JToolBar operationBar;
	private JLabel scoreBox;
	
	protected GameBoard gameBoard;
	private Timer appTimer;
	
	public ApplicationWindow()
	{
		super();
	    // respond to the window system asking us to quit
	    addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    
	    //Set up the timer
	    appTimer = new Timer(GameBoard.MSECPERTICK, this);
	    appTimer.setInitialDelay(1000);

	    
	    toolBar = new JToolBar();
	    addTopButtons(toolBar);
	    gizmoBar = new JToolBar();
	    addRightButtons(gizmoBar);
	    operationBar = new JToolBar();
	    addLeftButtons(operationBar);
	    operationBar.setOrientation(1);
	    
	    gameBoard = new GameBoard();
	    gameBoard.setPreferredSize(new Dimension(440, 440));
	    //Lay out the content pane.
	    JPanel contentPane = new JPanel();
	    contentPane.setLayout(new BorderLayout());
	    contentPane.add(toolBar,BorderLayout.NORTH);
	    contentPane.add(gizmoBar,BorderLayout.EAST);
	    contentPane.add(operationBar,BorderLayout.WEST);
	    contentPane.add(gameBoard, BorderLayout.CENTER);

	    setContentPane(contentPane);
	    addMouseListener(this);
	    
	    this.setResizable(false);
	    
	    appTimer.start();
	}
	  
	public static void main(String[] args) {
		    ApplicationWindow frame = new ApplicationWindow();

		    // the following code realizes the top level application window
		    frame.pack();
		    frame.setVisible(true);  
	  }
	  
	
	  
	protected void addTopButtons(JToolBar toolBar) {
		
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.addSeparator();
		toolBar.addSeparator();
		
		JButton button = null;
		    
		button = new JButton("Save");
		button.setToolTipText("Save the current board");
		button.addActionListener(this);     
		toolBar.add(button);
		    
		button = new JButton("Load");
		button.setToolTipText("Load a specified game board");
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton("Run");
		button.setToolTipText("Start the animation");
		// when this button is pushed the game enters playing mode
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton("Stop");
		button.setToolTipText("Stop the animation");
	    // when this button is pushed the gameBoard goes into building mode
		button.addActionListener(this);
		toolBar.add(button);

		button = new JButton("Quit");
		button.setToolTipText("Quit the program");
		button.addActionListener(this);
	    toolBar.add(button);
	    
	    toolBar.addSeparator();
	    toolBar.addSeparator();
	    
	    scoreBox = new JLabel("Score: 0");
	    toolBar.add(scoreBox);
	    
	}
	protected void addRightButtons(JToolBar gizmoBar)
	{
		gizmoBar.setOrientation(1);
		JButton button = null;
		
		button = new JButton("Circle");
		button.addActionListener(this);
		gizmoBar.add(button);
		gizmoBar.addSeparator();
		
		button = new JButton("Square");
		button.addActionListener(this);
		gizmoBar.add(button);
		gizmoBar.addSeparator();
		
		button = new JButton("Triangle");
		button.addActionListener(this);
		gizmoBar.add(button);
		gizmoBar.addSeparator();
		
		button = new JButton("Trapezoid");
		button.addActionListener(this);
		gizmoBar.add(button);
		gizmoBar.addSeparator();
		
		button = new JButton("LeftFlipper");
		button.addActionListener(this);
		gizmoBar.add(button);
		gizmoBar.addSeparator();
		
		button = new JButton("RightFlipper");
		button.addActionListener(this);
		gizmoBar.add(button);
		gizmoBar.addSeparator();
		
		button = new JButton("Absorber");
		button.addActionListener(this);
		gizmoBar.add(button);
		gizmoBar.addSeparator();
		
		button = new JButton("Ball");
		button.addActionListener(this);
		gizmoBar.add(button);
	}
	protected void addLeftButtons(JToolBar opBar)
	{
		JButton button;
		
		button = new JButton("Move");
		button.addActionListener(this);
		opBar.add(button);
		
		button = new JButton("Rotate");
		button.addActionListener(this);
		opBar.add(button);
		
		button = new JButton("Enlarge");
		button.addActionListener(this);
		opBar.add(button);
		
		button = new JButton("Shrink");
		button.addActionListener(this);
		opBar.add(button);
		
		button = new JButton("Delete");
		button.addActionListener(this);
		opBar.add(button);
		
		button = new JButton("Connect");
		button.addActionListener(this);
		opBar.add(button);
		
		button = new JButton("Disconnect");
		button.addActionListener(this);
		opBar.add(button);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub	
		scoreBox.setText("Score: "+gameBoard.getScore());
		if(arg0.getActionCommand() == null)
		{
			return;
		}
		if(arg0.getActionCommand().equals("Run"))
		{
			gizmoBar.setEnabled(false);
			gameBoard.setMode(true);
		}
		if(arg0.getActionCommand().equals("Stop"))
		{
			gameBoard.setMode(false);
		}
		if(arg0.getActionCommand().equals("Save"))
		{
			//TODO
			if(!gameBoard.getMode())
			{
				FileDialog dialog = new FileDialog(this,"Save game",FileDialog.SAVE);
				dialog.show();
				if(dialog.getDirectory() != null && dialog.getFile() != null)
				{
					gameBoard.saveFile(dialog.getDirectory()+dialog.getFile());
				}
			}
		}
		if(arg0.getActionCommand().equals("Load"))
		{
			//TODO
			if(!gameBoard.getMode())
			{
				FileDialog dialog = new FileDialog(this,"Save game",FileDialog.LOAD);
				dialog.show();
				if(dialog.getDirectory() != null && dialog.getFile() != null)
				{
					gameBoard.loadFile(dialog.getDirectory()+dialog.getFile());
				}
			}
		}
		if(arg0.getActionCommand().equals("Quit"))
		{
			//TODO
			System.exit(0);
		}
		if(arg0.getActionCommand().equals("Circle"))
		{
			gameBoard.setCommand(CommandType.addCircle);
			gameBoard.setAddOrNot(true);
		}
		if(arg0.getActionCommand().equals("Square"))
		{
			gameBoard.setCommand(CommandType.addSquare);
			gameBoard.setAddOrNot(true);
		}
		if(arg0.getActionCommand().equals("Triangle"))
		{
			gameBoard.setCommand(CommandType.addTriangle);
			gameBoard.setAddOrNot(true);
		}
		if(arg0.getActionCommand().equals("Trapezoid"))
		{
			gameBoard.setCommand(CommandType.addTrapezoid);
			gameBoard.setAddOrNot(true);
		}
		if(arg0.getActionCommand().equals("LeftFlipper"))
		{
			gameBoard.setCommand(CommandType.addLeftFlipper);
			gameBoard.setAddOrNot(true);
		}
		if(arg0.getActionCommand().equals("RightFlipper"))
		{
			gameBoard.setCommand(CommandType.addRightFlipper);
			gameBoard.setAddOrNot(true);
		}
		if(arg0.getActionCommand().equals("Absorber"))
		{
			gameBoard.setCommand(CommandType.addAbsorber);
			gameBoard.setAddOrNot(true);
		}
		if(arg0.getActionCommand().equals("Ball"))
		{
			gameBoard.setCommand(CommandType.addBall);
			gameBoard.setAddBallOrNot(true);
		}
		if(arg0.getActionCommand().equals("Move"))
		{
			gameBoard.setCommand(CommandType.moveGizmo);
			gameBoard.setMoveOrNot(true);
		}
		if(arg0.getActionCommand().equals("Delete"))
		{
			gameBoard.setCommand(CommandType.deleteGizmo);
			gameBoard.setDeleteOrNot(true);
		}
		if(arg0.getActionCommand().equals("Connect"))
		{
			gameBoard.setCommand(CommandType.connect);
			gameBoard.setConnectOrNot(true);
		}
		if(arg0.getActionCommand().equals("Disconnect"))
		{
			gameBoard.setCommand(CommandType.disconnect);
			gameBoard.setDisconnectOrNot(true);
		}
		if(arg0.getActionCommand().equals("Rotate"))
		{
			gameBoard.setCommand(CommandType.rotateGizmo);
			gameBoard.setRotateOrNot(true);
		}
		if(arg0.getActionCommand().equals("Enlarge"))
		{
			gameBoard.setCommand(CommandType.enlargeGizmo);
			gameBoard.setEnlargeOrNot(true);
		}
		if(arg0.getActionCommand().equals("Shrink"))
		{
			gameBoard.setCommand(CommandType.shrinkGizmo);
			gameBoard.setShrinkOrNot(true);
		}
	}
	

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		gameBoard.requestFocus();
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
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		gameBoard.requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
