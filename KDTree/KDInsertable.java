package portfolio;

import java.util.List;
/**
 * Interface that outlines what method's nodes of a K-D tree need.
 *
 * @author Zachary Hoffman
 * @param <Node> A node that implements KDInsertable and that will be part of a
 * KD tree.
 */
public interface KDInsertable<Node> {
  /** Sets the Node's "parent":
   * <p> The node in the KDTree that links to this node as either
   * a left or right child.
   * @param parent The node to be made into this node's parent.
   */
  void setParent(Node parent);

  /** Sets the dimension that this Node is dividing sub nodes by.
   * @param splitDimension The dimension to split with.
   */
  void setSplitDimension(int splitDimension);

  /** Sets sets this Node's "left child":
   * <p> The child with a value less than the curren Node at the
   * split dimension.
   * @param left The node to be made into this Node's left child.
   * @return The node's left child.
   *
   */
  Node setLeft(Node left);

  /** Sets sets this Node's "right child":
   * <p> The child with a value greater than the curren Node at the
   * split dimension.
   * @param right The node to be made into this Node's right child.
   * @return The node's right child.
   *
   */
  Node setRight(Node right);

  /** Return's the node's left child.
   * @return The node's right child.
   * @see setLeft
   *
   */
  Node getLeft();

  /** Return's the node's right child.
   * @return The node's right child.
   * @see setRight
   *
   */
  Node getRight();

  /** Return's the node's parent.
   * @return The node's parent
   * @see setParent
   *
   */
  Node getParent();

  /** Return's the node's coordinates as a list of doubles.
   * <p> The length of the list should correspond with the number of dimensions
   * the kd-tree is using.
   * @return A list of the node's location in doubles.
   *
   */
  List<Double> getLocation();

  /** Returns the dimension that this Node is dividing sub nodes by.
   * @return An integer representing the dimension that this Node
   * is dividing sub nodes by.
   * @see setSplitDimension
   */
  int getSplitDimension();
}
