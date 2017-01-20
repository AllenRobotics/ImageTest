package org.usfirst.frc.team5417.robot;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Mat;

public class FindGroupsOperation implements MatrixOperation {

	private SecureRandom random = new SecureRandom();

	public class DisjointSetNode {
		public DisjointSetNode parent;
		public int id;
		public int rank = 0;
		public byte[] rgb = new byte[3];
	}

	public boolean isBlackPixel(byte[] rgb) {
		return rgb[0] == 0 && rgb[1] == 0 && rgb[2] == 0;
	}

	@Override
	public Mat doOperation(Mat m) {
		// TODO Auto-generated method stub
		DisjointSetNode[][] nodeGroups = new DisjointSetNode[m.rows()][];

		for (int i = 0; i < m.rows(); i++) {
			nodeGroups[i] = new DisjointSetNode[m.cols()];
		}

		int nextGroupId = 1;

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				// Build a matrix of DisjointSetNodes where every black pixel is
				// null and otherwise is set

				byte[] rgb = new byte[3];

				m.get(r, c, rgb);

				if (isBlackPixel(rgb)) {
					nodeGroups[r][c] = null;
				} else {
					nodeGroups[r][c] = MakeSet(nextGroupId);
					nextGroupId++;
				}

			}
		}

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				// identifying groups

				if (nodeGroups[r][c] != null) {

					DisjointSetNode[] adjacentNodes = getAdjacentNodes(r, c, m.rows(), m.cols(), nodeGroups, 3);
					for (DisjointSetNode node : adjacentNodes) {
						Union(node, nodeGroups[r][c]);

					}
				}
			}
		}

		Mat result = new Mat(m.rows(), m.cols(), m.type());
		byte[] blackPixel = { 0, 0, 0 };
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {
				if (nodeGroups[r][c] == null) {
					result.put(r, c, blackPixel);
				} else {
					DisjointSetNode root = GetRoot(nodeGroups[r][c]);
					result.put(r, c, root.rgb);
				}

			}
		}

		return result;
	}

	private DisjointSetNode[] getAdjacentNodes(int r, int c, int rows, int cols, DisjointSetNode[][] nodeGroups,
			int regionWidth) {
		int startR = r - regionWidth / 2;
		int startC = c - regionWidth / 2;

		int endR = r + regionWidth / 2;
		int endC = c + regionWidth / 2;

		List<DisjointSetNode> adjacentNodes = new ArrayList<DisjointSetNode>();

		for (int RR = startR; RR <= endR; RR++) {
			for (int CC = startC; CC <= endC; CC++) {

				if (RR != r && CC != c && isInImage(RR, CC, rows, cols)) {
					if (nodeGroups[RR][CC] == null) {
						// pixel is not set

					} else {
						// pixel is set
						adjacentNodes.add(nodeGroups[RR][CC]);
					}

				}
			}
		}

		DisjointSetNode[] result = new DisjointSetNode[adjacentNodes.size()];
		result = adjacentNodes.toArray(result);
		return result;
	}

	private boolean isInImage(int r, int c, int rows, int cols) {
		if ((r < 0) || (c < 0)) {
			return false;
		}
		if ((r >= rows) || (c >= cols)) {
			return false;
		} else {
			return true;
		}
	}

	private DisjointSetNode MakeSet(int id) {

		DisjointSetNode newNode = new DisjointSetNode();
		newNode.id = id;
		newNode.parent = newNode;
		newNode.rank = 0;

		// NOTE: we *might* generate the same color accidentally
		// TODO: fixme
		newNode.rgb[0] = (byte) ((random.nextInt() % 120) - 120);
		newNode.rgb[1] = (byte) ((random.nextInt() % 120) - 120);
		newNode.rgb[2] = (byte) ((random.nextInt() % 120) - 120);
		return newNode;
	}

	private DisjointSetNode Find(DisjointSetNode x) {
		if (x.parent != x) {
			x.parent = Find(x.parent);
		}
		return x.parent;
	}
	
	private DisjointSetNode GetRoot(DisjointSetNode x)
	{
		while (x.parent != x) {
			x = x.parent;
		}
		return x;
	}

	private void Union(DisjointSetNode x, DisjointSetNode y) {
		DisjointSetNode xRoot = Find(x);
		DisjointSetNode yRoot = Find(y);

		if (xRoot == yRoot)
			return;

		if (xRoot.rank < yRoot.rank) {
			xRoot.parent = yRoot;
		} else if (xRoot.rank > yRoot.rank) {
			yRoot.parent = xRoot;
		} else {
			yRoot.parent = xRoot;
			xRoot.rank = xRoot.rank + 1;
		}
	}
	// https://en.wikipedia.org/wiki/Disjoint-set_data_structure
}
