import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Ragojos
 */
public class pathFinder {
    //Variables
    private boolean complete, noPath;
    private insertSort insertSort;
    private Node startCell, endCell;
    private List<Node> pathList, obstaclesList;
    private Integer ROW, COL, GRID_SIZE, numberOfNodes;

    //Constructors for pathFinder class
    public pathFinder() {
        //Booleans
        noPath = false;
        complete = false;

        obstaclesList = new ArrayList<>();
        insertSort = new insertSort();
        pathList = new ArrayList<>();
    }

    //Adds start node to close list
    //Also sets default value of startCell
    public void start() {
        startCell.setgCost(0);
        startCell.sethCost(0);
        startCell.setfCost(0);
    }

    public boolean cellIsDiagonal(int i, int j) {
        if (i == -1 && j == -1 || i == -1 && j == 1
                || i == 1 && j == -1 || i == 1 && j == 1) {
            return true;
        } else {
            return false;
        }
    }

    //Checks all open Adjacent Cells of parent cell
    public void findNextPath() {
        int x, y;
        int endX = endCell.getX();
        int endY = endCell.getY();
        Node tempCell;
        Node controlNode = new Node(0, 0);
        Node parent = startCell;
        List<Node> nextPath = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();
        List<Node> obstaclesList = getObstaclesList();
        while (!complete) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    x = parent.getX() + (j * GRID_SIZE);
                    y = parent.getY() + (i * GRID_SIZE);
                    tempCell = new Node(x, y);
                    //Prevents from checking itself
                    if (i == 0 && j == 0) {
                        continue;
                    }//Checks diagonal cells
                    else if (cellIsDiagonal(i, j)) {
                        //Checks to see if tempCell is in the window
                        if (x < 0 || y < 0 || x > COL || y > ROW) {
                            continue;
                        }
                        //If unobstructed or has already been checked then continue to next neighbor
                        if (isInList(obstaclesList, tempCell) || isInList(closedList, tempCell)) {
                            continue;
                        }
                        /*
                        Calculates the Node object tempCell's gCost, hCost, and fCost
                        Along with adding it to the adjacentCell ArrayList for further checking
                        Then use insertionSort method to plug into nextPath ArrayList
                         */
                        getNodeValues(parent, tempCell, true);
                        insertSort.insertionSort(nextPath, tempCell);
                    }//Checks left, right, up, down cells
                    else {
                        //Checks to see if tempCell is in the window
                        if (x < 0 || y < 0 || x > COL || y > ROW) {
                            continue;
                        }
                        //If unobstructed or has already been checked then continue to next neighbor
                        if (isInList(obstaclesList, tempCell) || isInList(closedList, tempCell)) {
                            continue;
                        }
                        /*
                        Calculates the Node object tempCell's gCost, hCost, and fCost
                        Along with adding it to the adjacentCell ArrayList for further checking
                        Then use insertionSort method to plug into nextPath ArrayList
                         */
                        getNodeValues(parent, tempCell, false);
                        insertSort.insertionSort(nextPath, tempCell);
                    }
                }
            }
            if (nextPath.size() > 1 && nextPath.get(0).getfCost() == nextPath.get(1).getfCost()) {
                if (nextPath.get(0).gethCost() < nextPath.get(1).gethCost()) {
                    parent = nextPath.get(0);
                } else {
                    parent = nextPath.get(1);
                }
            } else {
                try {
                    parent = nextPath.get(0);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("WORKING");
                    noPath = true;
                    return;
                }
            }
            //System.out.println("X: " + parent.getX() + ", Y:" + parent.getY());

            /*Removes new parentCell from nextPath list and adds to close list
            preventing it from being checked again*/
            controlNode.removeNode(nextPath, parent);
            closedList.add(closedList.size(), parent);

            //Checks if parentCell and endCell
            int parentX = parent.getX();
            int parentY = parent.getY();
            if (parentX == endX && parentY == endY) {
                getShortestPath(closedList, pathList, parent);
                complete = true;
            }
        }
    }

    //Takes all checked Node Cells and finds the shortest path from start to end cell
    public void getShortestPath(List<Node> list, List<Node> listTwo, Node parent) {
        for (int i = 0; i < list.size(); i++) {
            listTwo.add(listTwo.size(), parent.getParent());
            parent = parent.getParent();
            if (parent == startCell) {
                return;
            }
        }
    }

    //Draws the shortest path from start to end
    public void drawPath(Graphics g) {
        numberOfNodes = pathList.size() - 1;
        for (int i = 0; i < pathList.size() - 1; i++) {
            Node temp = new Node(pathList.get(i).getX(), pathList.get(i).getY());
            g.fillRect(temp.getX(), temp.getY(), GRID_SIZE, GRID_SIZE);
        }
    }

    //Calculates the values for the node that's being checked
    public void getNodeValues(Node parent, Node tempCell, boolean isDiagonal) {
        //Checks to see if tempCell is null
        if (tempCell != null) {
            tempCell.setParent(parent);
            //Calculates g cost
            int g = parent.getgCost();
            if (isDiagonal) {
                g += diagonalCost();
            } else {
                g += GRID_SIZE;
            }
            tempCell.setgCost(g);

            //Calculates h cost using Diagonal Distance method
            int h = Math.abs(endCell.getX() - tempCell.getX()) + Math.abs(endCell.getY() - tempCell.getY());
            tempCell.sethCost(h);

            //Calculates f cost
            int f = g + h;
            tempCell.setfCost(f);
        }
    }

    //Adds obstacle node object into ArrayList of Obstacles for further searching
    public void addObstacle(Node obstacle) {
        obstaclesList.add(obstaclesList.size(), obstacle);
    }

    //Removes Obstacles
    public void removeObstacle(Node temp) {
        int x = temp.getX();
        int y = temp.getY();
        for (int i = 0; i < obstaclesList.size(); i++) {
            if (obstaclesList.get(i).getX() == x && obstaclesList.get(i).getY() == y) {
                obstaclesList.remove(i);
            }
        }
    }

    //Checks if the tempCell Node Object is in a List
    public boolean isInList(List<Node> list, Node tempCell) {
        int x = tempCell.getX();
        int y = tempCell.getY();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getX() == x && list.get(i).getY() == y) {
                return true;
            }
        }
        return false;
    }

    //Calculates the diagonal cost for g
    public int diagonalCost() {
        return (int) (GRID_SIZE * Math.sqrt(2));
    }

    //Getters and Setters
    public void setROW(Integer ROW) {
        this.ROW = ROW;
    }

    public void setCOL(Integer COL) {
        this.COL = COL;
    }

    public void setGRID_SIZE(Integer GRID_SIZE) {
        this.GRID_SIZE = GRID_SIZE;
    }

    public void setObstaclesList(List<Node> obstaclesList) {
        this.obstaclesList = obstaclesList;
    }

    public int getObstacleListSize() {
        return obstaclesList.size();
    }

    public List<Node> getObstaclesList() {
        return obstaclesList;
    }

    public void setStartCell(Node startCell) {
        this.startCell = startCell;
    }

    public void setEndCell(Node endCell) {
        this.endCell = endCell;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isNoPath() {
        return noPath;
    }

    public int getNumberOfNode() {
        return numberOfNodes;
    }
}