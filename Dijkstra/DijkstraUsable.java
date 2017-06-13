package portfolio;

import java.util.List;
/**
 * Interface that outlines what method's nodes used in Dijkstra need.
 *
 * @author Zachary Hoffman
 * @param <Node> A node that implements KDInsertable and that will be used in
 * Dijkstra.
 * @param <Goal> Representation that isGoal can use to see if one has reached
 * the node being searched for.
 */
public interface DijkstraUsable<Node, Goal> {
  /** Gets the node's weight to be used in the Dijkstra algorithm.
   * @return A double representing the node's weight.
   */
  Double getWeight();

  /** Gets the node's heuristic weight to be used in the Dijkstra A* algorithm.
   * @return A double representing the node's heuristic weight.
   */
  Double getHeuristic();

  /** Return's the node's children nodes its connected to in the graph.
   * @return A list of the node's children.
   * @throws Exception Allows users to throw an Exception if needed.
   */
  List<Node> getChildren() throws Exception;

  /** Return's the node's parent.
   * @return The node's parent
   */
  Node getParent();

  /** Return's the node's parent.
   * @param g What is needed to identify that the node is what is being searched
   * for.
   * @return The node's parent
   * @throws Exception Allows users to throw an Exception if needed.
   */
  boolean isGoal(Goal g) throws Exception;
}
