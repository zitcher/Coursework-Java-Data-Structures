package portfolio;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Runs Dijkstra's algroithm on given nodes.
 *
 * @author Zachary Hoffman
 * @param <Node> A node that implements DijkstraUsable and that will be used in
 * Dijkstra.
 * @param <Goal> The value that the end node should return true for isGoal().
 */
public final class Dijkstra<Node extends DijkstraUsable<Node, Goal>, Goal> {
  /** There are no args needed for Dijkstra, simply fill out the type.
   */
  public Dijkstra() {

  }

  /** Finds the shortest path between given start nodes and an end node.
   * @param start A list of nodes to start searching from.
   * @param finish A representation of the node being searched for
   * @return A list of nodes representing the path.
   * @throws RuntimeException If the node's isGoal/getChildren methods throw
   * an error, this will rethrow the errors.
   */
  public List<Node> findPath(List<Node> start, Goal finish)
    throws RuntimeException {

    //creates a priority queue which uses node wieghts as a comparator.
    PriorityQueue<Node> fringe =
        new PriorityQueue<>(
            (Node n1, Node n2) ->
              n1.getWeight().compareTo(n2.getWeight()));

    fringe.addAll(start);

    //Dijkstra's algorithm
    HashSet<Node> traversed = new HashSet<>();
    while (true) {
      if (fringe.size() == 0) {
        return new ArrayList<>();
      }
      Node selected = fringe.poll();

      // to remove backtracking and looping
      if (traversed.contains(selected)) {
        continue;
      } else {
        traversed.add(selected);
      }


      // if selected is the goal we are done
      // otherwise expand it and add its children to the fringe
      try {
        if (selected.isGoal(finish)) {
          return path(selected);
        } else {
          List<Node> children = selected.getChildren();
          fringe.addAll(children);
        }
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }

  // returns a lists representing the path to the given node
  private List<Node> path(Node curnode) {
    Node parent = curnode.getParent();
    if (parent != null) {
      List<Node> nodePath = path(parent);
      nodePath.add(curnode);
      return nodePath;
    } else {
      List<Node> base = new ArrayList<>();
      base.add(curnode);
      return base;
    }
  }
}
