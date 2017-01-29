import java.util.ArrayList;
import java.util.List;

public class Solution {

	public static class Node {
		protected final int value;
		protected final Node parent;
		protected Node left = null;
		protected Node right = null;

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		public Node(int value, Node parent) {
			this.value = value;
			this.parent = parent;
		}

		protected Node findNode(int value, List<Node> hierarchy) {
			Node node = null;
			if (this.value == value) {
				node = this;
			} else if (value < this.value) {
				if (left != null) {
					node = left.findNode(value, hierarchy);
				}
				hierarchy.add(0, this);
			} else if (value > this.value) {
				if (right != null) {
					node = right.findNode(value, hierarchy);
				}
				hierarchy.add(0, this);
			}
			return node;
		}

		public boolean addNode(int value) {
			boolean result = false;
			if (value < this.value) {
				if (left != null) {
					result = left.addNode(value);
				} else {
					left = new Node(value, this);
					result = true;
				}
			} else if (value > this.value) {
				if (right != null) {
					result = right.addNode(value);
				} else {
					right = new Node(value, this);
					result = true;
				}
			}
			return result;
		}

		public int computeDistance(int node1, int node2) {
			int distance = Integer.MIN_VALUE;
			if (node1 != node2) {
				List<Node> node1Hierarchy = new ArrayList<Node>();
				Node node1Node = findNode(node1, node1Hierarchy);
				System.out.println(node1Hierarchy);
				List<Node> node2Hierarchy = new ArrayList<Node>();
				Node node2Node = findNode(node2, node2Hierarchy);
				System.out.println(node2Hierarchy);
				if (node1Node != null && node2Node != null) {
					int iNode = 0;
					while (iNode < node1Hierarchy.size() && iNode < node2Hierarchy.size()
							&& node1Hierarchy.get(iNode) == node2Hierarchy.get(iNode)) {
						iNode++;
					}
					// We found the first discrepency now we can compute
					// distance
					distance = 2 + (node1Hierarchy.size() - iNode) + (node2Hierarchy.size() - iNode);
				} else {
					// One or both nodes do not exist
					distance = -1;
				}
			} else {
				// No need to look it's the same
				distance = 0;
			}
			return distance;
		}
	}

	protected static Node createTree(int[] values, int n) {
		Node tree = null;
		if (n > 0) {
			// Root of the tree
			tree = new Solution.Node(values[0], null);
			// Branches
			for (int iValues = 1; iValues < n; iValues++) {
				tree.addNode(values[iValues]);
			}
		}
		return tree;
	}

	public static int bstDistance(int[] values, int n, int node1, int node2) {
		Node tree = createTree(values, n);
		return tree.computeDistance(node1, node2);
	}
}
