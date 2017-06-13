package portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

import java.math.BigDecimal;

import com.google.common.collect.Lists;

/**
 * Represents a k-d treee in java.
 *
 * @author Zachary Hoffman
 * @param <Node> A object that implements the interface KDInsertable
 * so that it is insertable into a k-d tree.
 */

public class KDTree<Node extends KDInsertable<Node>> {

  //holds the root node of the tree
  private Node root;

  /** Given a root node of an already built tree, constructs a KDTree.
   * @param root A node that implements KDInsertable and is already a built
   * and finished.
   */
  public KDTree(Node root) {
    this.root = root;
  }

  /** Given a list of nodes that have to parent left child or right child,
   * constructs a kd-tree.
   * @param nodeList a list of nodes that have to parent left child or right
   * child
   */
  public KDTree(List<Node> nodeList) {
    this.root = recurGenTree(nodeList, 0, null);
  }

  //WARNING: do not call for massive trees, will generate a massive string
  @Override
  public String toString() {
    return stringHelper(root);
  }

  //handles recursively printing the tree
  private String stringHelper(Node nodeToPrint) {
    if (nodeToPrint == null) {
      return "empty";
    } else {
      return nodeToPrint.toString() + "\n"
             + "Left: " + stringHelper(nodeToPrint.getLeft()) + "\n"
             + "Right: " + stringHelper(nodeToPrint.getRight()) + "\n";
    }
  }

  //sorts list of nodes looking at a certain dimension in ascending order
  private void sortByDimension(int dimension, List<Node> toSort) {
    //creates a generic comparator to sort with
    Collections.sort(toSort, new Comparator<Node>() {
      @Override
      public int compare(Node one, Node two) {
          // compare using whichever properties of ListType you need
          Double oneLocation = one.getLocation().get(dimension);
          Double twoLocation = two.getLocation().get(dimension);
          if (Double.compare(oneLocation, twoLocation) < 0) {
            return -1;
          } else if (Double.compare(oneLocation, twoLocation) > 0) {
            return 1;
          } else {
            return 0;
          }
      }
    });
  }

  //generates the rest of the KD tree from the parent node w/ the nodesToAdd
  private Node recurGenTree(List<Node> nodesToAdd,
                            int depth,
                            Node parent) {
    //makes sure there is a star to add
    if (nodesToAdd.size() <= 0) {
      return null;
    }
    // number of dimension the tree is built in
    int numDimensions = nodesToAdd.get(0).getLocation().size();

    // Select axis based on depth so that axis cycles through all valid values
    int axis = depth % numDimensions;

    //sorts by selected dimension
    sortByDimension(axis, nodesToAdd);

    //Gets node and adds children around median to balance tree
    int pointer =  (nodesToAdd.size()) / 2;
    Node node = nodesToAdd.remove(pointer);
    node.setSplitDimension(axis);
    node.setParent(parent);

    //makes sure its partitionable
    if (nodesToAdd.size() <= 0) {
      return node;
    }

    //add parents and left/right children based on surround median values

    final List<List<Node>> partition =  Lists.partition(nodesToAdd, pointer);
    node.setLeft(recurGenTree(new ArrayList<Node>(partition.get(0)),
                                                  depth + 1, node));

    if (partition.size() == 2) {
      node.setRight(recurGenTree(new ArrayList<Node>(partition.get(1)),
                                                     depth + 1, node));
    }
    return node;
  }

  //returns the distance between two double cordinate lists as a BigDecimal
  private BigDecimal dist(List<Double> point1, List<Double> point2) {
    BigDecimal totalDist = BigDecimal.ZERO;
    for (int i = 0; i < point1.size(); i++) {
      totalDist = totalDist.add(
                    new BigDecimal(point1.get(i)).subtract(
                      new BigDecimal(point2.get(i))).pow(2));
    }

    return totalDist;
  }

  //sorts list of node in decending order by distance from location
  private void sortByDist(List<Double> targetLocation, List<Node> toSort) {
    Collections.sort(toSort,
        new Comparator<Node>() {
          @Override
          public int compare(Node one, Node two) {
              // compare using whichever properties of ListType you need
              List<Double> oneLocation = one.getLocation();
              List<Double> twoLocation = two.getLocation();
              BigDecimal oneDist = dist(oneLocation, targetLocation);
              BigDecimal twoDist = dist(twoLocation, targetLocation);
              if (oneDist.compareTo(twoDist) < 0) {
                return 1;
              } else if (oneDist.compareTo(twoDist) > 0) {
                return -1;
              } else {
                return 0;
              }
          }
        }
    );
  }

  /** Given a number of nodes to search for, and a location to search at,
   * returns a list of nodes containing the neigbhors closest to the target
   * location. The list will be of length numNeighbors unless there are less
   * nodes than numNeighbors. If so, the list will contain all nodes in the
   * tree.
   * @param numNeighbors Integer represeinging the number of nodes to search
   * for.
   * @param targetLocation Integer representing the location to search around
   * for nearest nodes/neighbors.
   * @return A list of the nearest nodes to targetLocation.
   */
  public List<Node> nearestNeighbors(int numNeighbors,
                                     List<Double> targetLocation) {
    List<Node> neighborList = new ArrayList<Node>();
    nearestNeighborsHelper(numNeighbors,
                           Collections.unmodifiableList(targetLocation),
                           neighborList,
                           root,
                           BigDecimal.ZERO);
    return neighborList;
  }

  //recursively searches through the tree for nearest neighbor
  private void nearestNeighborsHelper(
                                 int num,
                                 List<Double> targetLocation,
                                 List<Node> nearestNeighbors,
                                 Node currentNode,
                                 BigDecimal worstDistance) {
    List<Double> currentLocation = currentNode.getLocation();
    BigDecimal curDistance = dist(targetLocation, currentLocation);

    /* Neighbor list is sorted in ascending order so the furthest node
    from the targetLocation will be the first node in the list */
    if (nearestNeighbors.size() > 0) {
      worstDistance = dist(nearestNeighbors.get(0).getLocation(),
                           targetLocation);
    }

    /* If we aren't at the max number of neighbors, add the currentNode by
    default */
    if (nearestNeighbors.size() < num) {

      nearestNeighbors.add(currentNode);
      sortByDist(targetLocation, nearestNeighbors);
      worstDistance = dist(nearestNeighbors.get(0).getLocation(),
                           targetLocation);

    } else if (curDistance.compareTo(worstDistance) < 0) {
      /* otherwise check if the current node is better than the worst node in
      the list */
      nearestNeighbors.remove(0);
      nearestNeighbors.add(currentNode);

      sortByDist(targetLocation, nearestNeighbors);
      worstDistance = dist(nearestNeighbors.get(0).getLocation(),
                           targetLocation);
    }

    //Recur through searching for neigbhors closer to target location.
    int axis = currentNode.getSplitDimension();
    BigDecimal targetAxisPos = new BigDecimal(targetLocation.get(axis));
    BigDecimal curAxisPos =  new BigDecimal(currentLocation.get(axis));
    BigDecimal axisDist = curAxisPos.subtract(targetAxisPos).pow(2);

    Node leftNode = currentNode.getLeft();
    Node rightNode = currentNode.getRight();
    //if axis is inside hypersphere go down each path
    if (axisDist.compareTo(worstDistance) <= 0) {
      if (leftNode != null) {
        nearestNeighborsHelper(
                    num,
                    targetLocation,
                    nearestNeighbors,
                    leftNode,
                    worstDistance);
      }
      if (rightNode != null) {
        nearestNeighborsHelper(
                    num,
                    targetLocation,
                    nearestNeighbors,
                    rightNode,
                    worstDistance);
      }
    } else {
      //otherwise only go in the direction of the targetLocation
      if (targetAxisPos.compareTo(curAxisPos) < 0) {
        //if target axis is to the left of current axis, go down left branch

        if (leftNode != null) {
          nearestNeighborsHelper(
                      num,
                      targetLocation,
                      nearestNeighbors,
                      leftNode,
                      worstDistance);
        }
      } else {
        ///otherwise go down right
        if (rightNode != null) {
          nearestNeighborsHelper(
                      num,
                      targetLocation,
                      nearestNeighbors,
                      rightNode,
                      worstDistance);
        }
      }

    }
  }

  /** Given a double representing a distance from a point in k-dimensional space
   * targetLocation, returns a list of all nodes within distance radius to
   * targetLocation.
   * @param radius Double representing the radius in which to search for nodes
   * @param targetLocation Integer representing the location to search around
   * for nodes within the given radius.
   * @return A list of the nodes within the given radius to targetLocation
   */
  public List<Node> radiusSearch(Double radius, List<Double> targetLocation) {
    List<Node> nodeList = new ArrayList<Node>();
    String stringRadius = Double.toString(radius);
    radiusSearchHelper(new BigDecimal(stringRadius).pow(2),
                       Collections.unmodifiableList(targetLocation),
                       root,
                       nodeList);
    sortByDist(targetLocation, nodeList);
    return nodeList;
  }

  private void radiusSearchHelper(
                                 BigDecimal radius,
                                 List<Double> targetLocation,
                                 Node currentNode,
                                 List<Node> nodeList) {
    List<Double> currentLocation = currentNode.getLocation();
    BigDecimal curDistance = dist(targetLocation, currentLocation);

    if (curDistance.compareTo(radius) <= 0) {
      nodeList.add(currentNode);
    }

    //Recurring through subtrees
    int axis = currentNode.getSplitDimension();
    BigDecimal targetAxisPos = new BigDecimal(targetLocation.get(axis));
    BigDecimal curAxisPos =  new BigDecimal(currentLocation.get(axis));
    BigDecimal axisDist = curAxisPos.subtract(targetAxisPos).pow(2);

    Node leftNode = currentNode.getLeft();
    Node rightNode = currentNode.getRight();
    //if axis is inside hypersphere go down each path
    if (axisDist.compareTo(radius) <= 0) {
      if (leftNode != null) {
        radiusSearchHelper(radius,
                     targetLocation,
                     leftNode,
                     nodeList);
      }
      if (rightNode != null) {
        radiusSearchHelper(radius,
                           targetLocation,
                           rightNode,
                           nodeList);
      }
    } else {
      //otherwise only go in the direction of the targetLocation
      if (targetAxisPos.compareTo(curAxisPos) < 0) {
        //if target axis is to the left of current axis, go down left branch

        if (leftNode != null) {
          radiusSearchHelper(radius,
                             targetLocation,
                             leftNode,
                             nodeList);
        }
      } else {
        //otherwise go down right
        if (rightNode != null) {
          radiusSearchHelper(radius,
                             targetLocation,
                             rightNode,
                             nodeList);
        }
      }
    }
  }
}
