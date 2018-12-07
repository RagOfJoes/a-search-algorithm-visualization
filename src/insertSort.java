import java.util.List;

/**
 * Created by Victor Ragojos
 */
public class insertSort {

    //Constructor
    public insertSort() {
    }

    //Sorts the ArrayList of Nodes with the lowest fCost being the first index
    public void insertionSort(List<Node> list, Node addNode) {
        int addNodeCost = addNode.getfCost();
        if (list.size() == 0) {
            list.add(list.size(), addNode);
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (addNodeCost < list.get(i).getfCost()) {
                    list.add(i, addNode);
                    return;
                }
            }
            list.add(list.size(), addNode);
        }
    }
}//end class