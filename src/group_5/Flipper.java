package group_5;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;
import java.lang.*;
import physics.*;

class Flipper extends Gizmo {  

  //extra fields for flippers only
  //these booleans specify whether the flipper's pivot corner is
  // offset from (x, y) in the right and downward directions
  private boolean pivotRight;
  private boolean pivotDown;
  //leftflippers pivot counterclockwise - tell them apart
  private boolean leftFlipper;
  //booleans to tell if the flipper is moving and which way
  private boolean forwardMotion;
  private boolean backwardMotion;
  //angle of flipper (0-360)
  private double flipperAngle;
  //max and min angles for flipper (0-360)
  private double startAngle;
  private double flippedAngle;
    
  //constructors
  public Flipper(GameBoard gb, String name, int xL, int yL,
		      boolean left) {
    this.gb = gb;
    this.x = xL*GameBoard.PixelsPerL;
    this.y = yL*GameBoard.PixelsPerL;
    this.width = 2;
    this.height = 2;
    this.name = name;
    this.refCoef = 0.95;
    this.leftFlipper = left;
    this.forwardMotion = false;
    this.backwardMotion = false;
    this.flipperAngle = 270.0;
    this.startAngle = 270.0;
    if (leftFlipper) {
      flippedAngle = 0.0;
      pivotRight = false;
      pivotDown = false;
      this.gt = GizmoType.LeftFlipper;
    } else {
      flippedAngle = 180.0;
      pivotRight = true;
      pivotDown = false;
      this.gt = GizmoType.RightFlipper;
    }
  }

  /**
   *@modifies this
   *@effects change the corner of this flipper to be next of the four
   *bounding box corners in a clockwise rotation
   */
  public void rotate() {
    //define offsets from (x, y) in response to current pivot state
    if (!pivotRight && !pivotDown) {
      pivotRight = true;
    } else if (pivotRight && !pivotDown) {
      pivotDown = true;
    } else if (pivotRight && pivotDown) {
      pivotRight = false;
    } else if (!pivotRight && pivotDown) {
      pivotDown = false;
    }
    //rotate all defining angles clockwise 90 degrees
    flipperAngle += 270.0;
    flipperAngle %= 360.0;
    startAngle += 270.0;
    startAngle %= 360.0;
    flippedAngle += 270.0;
    flippedAngle %= 360.0;
  }

  

  @Override
public void enlarge() {
	// TODO Auto-generated method stub
	
}

public void startAction() {
    //flippers only have one action - ignore action variable
    if (!forwardMotion && !backwardMotion
	&& (flipperAngle == startAngle)) {
      //if not moving, and in start state, start forward motion
      gb.addToActiveList(this);
      forwardMotion = true;
    } else if (!forwardMotion && !backwardMotion
	       && (flipperAngle == flippedAngle)) {
      //if not moving, and in flipped state, start backward motion
      gb.addToActiveList(this);
      backwardMotion = true;
    } else if (forwardMotion) {
      //if moving forward, reverse direction
      //don't need to add to active list, already there
      forwardMotion = false;
      backwardMotion = true;
    }
    //if moving backward, do nothing
  }
  

  public void action() {
    // FLIPPERANGVEL deg/sec * 0.001 sec/msec * MSECPERTICK msec/tick
    // * 1 tick = degreesToFlip in one tick
    double degreesToFlip = GameBoard.FLIPPERANGVEL * GameBoard.MSECPERTICK * 0.001;
    if (degreesToFlip != 90.0)
      degreesToFlip = degreesToFlip % 90;
    if (forwardMotion) {
      if (leftFlipper) {
	//left flipper forwards = counterclockwise
	flipperAngle = (flipperAngle + degreesToFlip + 360) % 360;
	//if becomes more than or equal to flipped, set to flipped state
	//and end (remove active, set bool to false)
	if (anglediff(startAngle, flipperAngle) >= 90) {
	  flipperAngle = flippedAngle;
	  forwardMotion = false;
	  gb.removeFromActiveList(this);
	}
      } else {
	//right flipper forwards = clockwise
	flipperAngle = (flipperAngle - degreesToFlip + 360) % 360;
	//if becomes more than or equal to flipped, set to flipped state
	//and end (remove active, set bool to false)
	if (anglediff(startAngle, flipperAngle) <= -90) {
	  flipperAngle = flippedAngle;
	  forwardMotion = false;
	  gb.removeFromActiveList(this);
	}
      }
    } else if (backwardMotion) {
      if (leftFlipper) {
	//left flipper backwards = clockwise
	flipperAngle = (flipperAngle - degreesToFlip + 360) % 360;
	//if becomes less than or equal to unflipped, set to unflipped
	//state and end (remove active, set bool to false)
	if (anglediff(startAngle, flipperAngle) <= 0) {
	  flipperAngle = startAngle;
	  backwardMotion = false;
	  gb.removeFromActiveList(this);
	}
      } else {
	//right flipper backwards = counterclockwise
	flipperAngle = (flipperAngle + degreesToFlip + 360) % 360;
	//if becomes less than or equal to unflipped, set to unflipped
	//state and end (remove active, set bool to false)
	if (anglediff(startAngle, flipperAngle) >= 0) {
	  flipperAngle = startAngle;
	  backwardMotion = false;
	  gb.removeFromActiveList(this);
	}
      }
    }
  }


  private double anglediff(double a, double b) {
    a %= 360;
    b %= 360;
    if ((b - a) < -180) { b += 360; }
    else if ((b - a) > 180) { a += 360; }
    return b - a;
  }
  

  public String getSaveString() {
    String ss = "";
    if (leftFlipper) {
      ss += "LeftFlipper ";
    } else {
      ss += "RightFlipper ";
    }
    ss += name + " " + (x/GameBoard.PixelsPerL) + " " + (y/GameBoard.PixelsPerL);
    int rotatecount = 0;
    if (!pivotRight && !pivotDown) {
      rotatecount = 0;
    } else if (pivotRight && !pivotDown) {
      rotatecount = 1;
    } else if (pivotRight && pivotDown) {
      rotatecount = 2;
    } else if (!pivotRight && pivotDown) {
      rotatecount = 3;
    }
    if (!leftFlipper) {
      //if rightflipper, subtract one from rotatecount and mod 4
      //(rightflipper pivot starts in topright, not topleft)
      rotatecount += 3;
      rotatecount %= 4;
    }
    for (int i = 0; i < rotatecount; i++) {
      ss += "\r\nRotate " + name;
    }
    return ss;
  }
  



  public double getAngularVelocity() {
    //NEGATIVE OF INTERNAL FLIPPER STATE!
    //differences in our rotation definition and physics API
    if ((leftFlipper && forwardMotion)
	|| (!leftFlipper && backwardMotion)) {
      return -1*Math.toRadians(GameBoard.FLIPPERANGVEL);
    } else if ((leftFlipper && backwardMotion)
	       || (!leftFlipper && forwardMotion)) {
      return Math.toRadians(GameBoard.FLIPPERANGVEL);
    }
    return 0;
  }

  public Point2D.Double getPivot() {
    double radius = GameBoard.PixelsPerL*0.25;
    double pivotx, pivoty;
    if (pivotRight) {
      pivotx = x + GameBoard.PixelsPerL*width - radius;
    } else {
      pivotx = x + radius;
    }
    if (pivotDown) {
      pivoty = y + GameBoard.PixelsPerL*height - radius;
    } else {
      pivoty = y + radius;
    }
    return new Point2D.Double(pivotx, pivoty);
  }

  /**
   *@return a Point2D.Double of the non-pivot center of this flipper,
   *in its start position (not turned at all)
   */
  private Point2D.Double getStartNonPivot() {
    double radiusdiff = GameBoard.PixelsPerL*1.5;
    Point2D.Double pivot = getPivot();
    //find nonpivot center by extending in direction of startAngle by
    //appropriate radius difference
    double nonpivotx = pivot.x
      + radiusdiff*Math.cos(Math.toRadians(startAngle));
    double nonpivoty = pivot.y
      - radiusdiff*Math.sin(Math.toRadians(startAngle));
    return new Point2D.Double(nonpivotx, nonpivoty);
  }


  private List getShapes() {
    List shapes = new ArrayList();
    double radius = GameBoard.PixelsPerL*0.25;
    Point2D.Double pivot = getPivot();
    Point2D.Double nonpivot = getStartNonPivot();

    double x1 = pivot.x + radius*Math.cos(Math.toRadians(startAngle+90));
    double y1 = pivot.y - radius*Math.sin(Math.toRadians(startAngle+90));
    double x2 = pivot.x + radius*Math.cos(Math.toRadians(startAngle-90));
    double y2 = pivot.y - radius*Math.sin(Math.toRadians(startAngle-90));
    double x3 = nonpivot.x + radius*Math.cos(Math.toRadians(startAngle-90));
    double y3 = nonpivot.y - radius*Math.sin(Math.toRadians(startAngle-90));
    double x4 = nonpivot.x + radius*Math.cos(Math.toRadians(startAngle+90));
    double y4 = nonpivot.y - radius*Math.sin(Math.toRadians(startAngle+90));
    double[] xs = {x1, x2, x3, x4};
    double[] ys = {y1, y2, y3, y4};
    //min x should also be min y!!! we are looking at unrotated state
    //x1 == x2, x3 == x4, y1 == y2, y3 == y4
    Arrays.sort(xs);
    Arrays.sort(ys);
    //center rectangle
    Shape rect = new Rectangle2D.Double(xs[0], ys[0],
					xs[2]-xs[0],
					ys[2]-ys[0]);
    //pivot ellipse
    Shape pivel = new Ellipse2D.Double(pivot.x - radius,
				       pivot.y - radius,
				       2*radius, 2*radius);
    //extreme ellipse
    Shape nonpivel = new Ellipse2D.Double(nonpivot.x - radius,
					  nonpivot.y - radius,
					  2*radius, 2*radius);
    //create a transformer to rotate shapes around pivot point by
    //startAngle - flipperAngle
    double rotang = Math.toRadians(startAngle - flipperAngle);
    AffineTransform rotator =
      AffineTransform.getRotateInstance(rotang, pivot.x, pivot.y);
    
    //apply rotation by transformer to each shape
    rect = rotator.createTransformedShape(rect);
    pivel = rotator.createTransformedShape(pivel);
    nonpivel = rotator.createTransformedShape(nonpivel);

    //add rotated shapes to List and return
    shapes.add(rect);
    shapes.add(pivel);
    shapes.add(nonpivel);
    return shapes;
  }
  

  public boolean containsPoint(Point p) {
    List shapes = getShapes();
    //use java.awt.Shape contains(Point)
    for (int i = 0; i < shapes.size(); i++) {
      Shape tempshape = (Shape)shapes.get(i);
      //if any of the shapes contain the point, return true
      if (tempshape.contains(p)) {
	return true;
      }
    }
    return false;
  }
  

  public List getBoundaryShape(Ball mover) {
    List shapes = new ArrayList();
    double radius = GameBoard.PixelsPerL*0.25;
    Point2D.Double pivot = getPivot();
    Point2D.Double nonpivot = getStartNonPivot();

    double x1 = pivot.x + radius*Math.cos(Math.toRadians(startAngle+90));
    double y1 = pivot.y - radius*Math.sin(Math.toRadians(startAngle+90));
    double x2 = pivot.x + radius*Math.cos(Math.toRadians(startAngle-90));
    double y2 = pivot.y - radius*Math.sin(Math.toRadians(startAngle-90));
    double x3 = nonpivot.x + radius*Math.cos(Math.toRadians(startAngle-90));
    double y3 = nonpivot.y - radius*Math.sin(Math.toRadians(startAngle-90));
    double x4 = nonpivot.x + radius*Math.cos(Math.toRadians(startAngle+90));
    double y4 = nonpivot.y - radius*Math.sin(Math.toRadians(startAngle+90));
    
    //create Shape objects (these are rotatable by AffineTransform)
    Point2D.Double pt1 = new Point2D.Double(x1, y1);
    Point2D.Double pt2 = new Point2D.Double(x2, y2);
    Point2D.Double pt3 = new Point2D.Double(x3, y3);
    Point2D.Double pt4 = new Point2D.Double(x4, y4);
    Point2D.Double nonpiv = new Point2D.Double(nonpivot.x, nonpivot.y);
    
    //create a transformer to rotate shapes around pivot point by
    //startAngle - flipperAngle
    double rotang = Math.toRadians(startAngle - flipperAngle);
    AffineTransform rotator =
      AffineTransform.getRotateInstance(rotang, pivot.x, pivot.y);
    //apply rotation by transformer to each shape
    pt1 = (Point2D.Double)rotator.transform((Point2D)pt1, (Point2D)pt1);
    pt2 = (Point2D.Double)rotator.transform((Point2D)pt2, (Point2D)pt2);
    pt3 = (Point2D.Double)rotator.transform((Point2D)pt3, (Point2D)pt3);
    pt4 = (Point2D.Double)rotator.transform((Point2D)pt4, (Point2D)pt4);
    nonpiv = (Point2D.Double)rotator.transform((Point2D)nonpiv,
					       (Point2D)nonpiv);
    
    //now rebuild physics shapes from rotated shapes
    //center rectangle, defined by two lines (P1->P4, P2->P3)
    shapes.add(new LineSegment(pt1.x, pt1.y, pt4.x, pt4.y));
    shapes.add(new LineSegment(pt2.x, pt2.y, pt3.x, pt3.y));

    //4 zero-radius circles - if needed
    shapes.add(new Circle(pt1, 0));
    shapes.add(new Circle(pt2, 0));
    shapes.add(new Circle(pt3, 0));
    shapes.add(new Circle(pt4, 0));
    
    //pivot ellipse - note: pivot hasn't moved, just use original
    shapes.add(new Circle(pivot, radius));
    //extreme ellipse
    shapes.add(new Circle(nonpiv, radius));
    //shapes.add(new Circle(((Line2D.Double)pivotline).getP2(), radius));
    
    return shapes;
  }

  public void paint(Graphics2D g) {
    g.setColor(Color.YELLOW);
    java.util.List shapes = getShapes();
    for (int i = 0; i < shapes.size(); i++) {
      Shape shapetodraw = (Shape)shapes.get(i);
      //paint each shape onto the board
      g.fill(shapetodraw);
    }
    Point2D.Double pivot = getPivot();
    g.setColor(Color.red);
    g.fill(new Ellipse2D.Double(pivot.x - 2, pivot.y - 2, 4, 4));
  }


  public void fire(Ball firer) {
	  gb.addScore(15);
    super.fire(firer);
  }
  
}