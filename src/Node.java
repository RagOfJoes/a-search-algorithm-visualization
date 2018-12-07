import java.util.List;

/**
 * Created by Victor Ragojos
 */
public class Node {
    public Node parent;
    private Integer x, y, gCost, hCost, fCost;

    //Constructor
    public Node(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void removeNode(List<Node> list, Node tempCell) {
        int x = tempCell.getX();
        int y = tempCell.getY();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getX() == x && list.get(i).getY() == y) {
                list.remove(i);
            }
        }
    }

    //Getters and Setters
    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void setgCost(Integer gCost) {
        this.gCost = gCost;
    }

    public void sethCost(Integer hCost) {
        this.hCost = hCost;
    }

    public Integer getfCost() {
        return fCost;
    }

    public Integer gethCost() {
        return hCost;
    }

    public Integer getgCost() {
        return gCost;
    }

    public void setfCost(Integer fCost) {
        this.fCost = fCost;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}