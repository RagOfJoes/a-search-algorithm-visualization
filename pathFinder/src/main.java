import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Victor Ragojos
 * Known Bugs:
 * TO DO:
 * Optimize/encapsulate program to ensure all executions are smooth.
 * Implement a reset button
 *
 */
public class main extends JPanel
        implements MouseListener, MouseMotionListener, KeyListener {
    //Classes
    JFrame frame;
    pathFinder pF;
    //Booleans
    private boolean isReady;
    //PathFinder grid coordinate lists
    private List<Node> obstacles;
    //Testing
    //Start and End Cells
    private Node startCell, endCell;
    //Constant Variables
    private Integer GRID_SIZE, FRAME_WIDTH, FRAME_LENGTH, NUM_OF_COL, NUM_OF_ROW;


    public static void main(String[] args) {
        new main();
    }

    //Constructor
    public main() {
        //Constants
        GRID_SIZE = 15;
        NUM_OF_COL = 61;
        NUM_OF_ROW = 61;
        FRAME_WIDTH = 915;
        FRAME_LENGTH = 915;
        //Booleans
        isReady = false;
        //Class Initialization
        pF = new pathFinder();
        //Copies List of Nodes from pathFinder
        obstacles = pF.getObstaclesList();
        //Adds Listeners
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        //Creates window
        frame = new JFrame();
        frame.setContentPane(this);
        frame.setTitle("A* Algorithm");
        frame.getContentPane().setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_LENGTH));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /*Snaps x value of mouseClick to an existing cell*/
    public int xInBorder(int X) {
        if (X < 0) {
            X = 0;
        } else {
            X = X + GRID_SIZE - X % GRID_SIZE;
        }
        return X;
    }

    /*Snaps y value of mouseClick to an existing cell*/
    public int yInBorder(int Y) {
        if (Y < 0) {
            Y = 0;
        } else {
            Y = Y + GRID_SIZE - Y % GRID_SIZE;
        }
        return Y;
    }

    //Creates and removes start and end cells
    public void createStartAndEndNode(MouseEvent e) {
        int x = e.getX() - GRID_SIZE;
        int y = e.getY() - GRID_SIZE;
        if (e.getButton() == 1 && isReady) {
            if (startCell == null) {
                x = xInBorder(x);
                y = yInBorder(y);
                startCell = new Node(x, y);
                System.out.println(startCell.getX() + " " + startCell.getY());
            } else if (startCell != null) {
                startCell = null;
            }
            repaint();
        }
        if (e.getButton() == 3 && isReady) {
            if (endCell == null) {
                x = xInBorder(x);
                y = yInBorder(y);
                endCell = new Node(x, y);
                System.out.println(endCell.getX() + " " + endCell.getY());
            } else if (endCell != null) {
                endCell = null;
            }
            repaint();
        }
    }

    //Creates obstacles with left mouse button and removes them with right mouse button
    public void createObstacles(MouseEvent e) {
        int x = e.getX() - GRID_SIZE;
        int y = e.getY() - GRID_SIZE;
        x = xInBorder(x);
        y = yInBorder(y);
        if (!isReady) {
            /*If holding left click then creates obstacles
            else if holding right click then removes existing obstacle*/
            if (pF.isInList(pF.getObstaclesList(), new Node(x, y))) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    pF.removeObstacle(new Node(x, y));
                    repaint();
                }
            } else {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    pF.addObstacle(new Node(x, y));
                    repaint();
                }
            }
        }
    }

    //Function that initializes the pathFinder Class
    public void runPathFinder() {
        if (startCell != null && endCell != null) {
            pF.setStartCell(startCell);
            pF.setEndCell(endCell);
            pF.setCOL(FRAME_WIDTH);
            pF.setROW(FRAME_LENGTH);
            pF.setGRID_SIZE(GRID_SIZE);
            pF.start();
            pF.findNextPath();
            repaint();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draws obstacles
        for (int i = 0; i < pF.getObstacleListSize(); i++) {
            g.setColor(new Color(189, 234, 147));
            g.fillRect(obstacles.get(i).getX(), obstacles.get(i).getY(), GRID_SIZE, GRID_SIZE);
        }
        //Draws start and end cells
        if (startCell != null) {
            g.setColor(new Color(209, 43, 43));
            g.fillRect(startCell.getX(), startCell.getY(), GRID_SIZE, GRID_SIZE);
        }
        if (endCell != null) {
            g.setColor(new Color(3, 89, 19));
            g.fillRect(endCell.getX(), endCell.getY(), GRID_SIZE, GRID_SIZE);
        }


        //Draws the shortest path from start cell to end cell
        if (startCell != null && endCell != null) {
            g.setColor(new Color(104, 63, 5));
            pF.drawPath(g);
        }
        /*Draws Grid after drawing startCells, endCells, and path to
         * allow grids to become visible*/
        for (int y = 0; y < NUM_OF_ROW + 1; y++) {
            for (int x = 0; x < NUM_OF_COL + 1; x++) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }
    }

    //Implemented methods
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        createObstacles(e);
        createStartAndEndNode(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        createObstacles(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        /*Disables ability to create obstacles and enables
        ability to create start and end nodes(vice versa if pressed again)*/
        if (!isReady && e.getKeyCode() == KeyEvent.VK_SPACE) {
            isReady = true;
        } else if (isReady && e.getKeyCode() == KeyEvent.VK_SPACE) {
            isReady = false;
        }
        //Runs algorithm to find shortest path
        if (e.getKeyCode() == KeyEvent.VK_R) {
            runPathFinder();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}