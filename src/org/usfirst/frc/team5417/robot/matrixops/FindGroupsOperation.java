package org.usfirst.frc.team5417.robot.matrixops;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

//
// See https://en.wikipedia.org/wiki/Disjoint-set_data_structure
//
// The FindGroupsOperation identifies non-overlapping groups of non-black pixels
// and assigns each group a unique color in the output image.
//
// Input: a grayscale or binarized (black or white) image
// Output: a color image where each identified group is color coded 
//
public class FindGroupsOperation implements MatrixOperation {

	private SecureRandom random = new SecureRandom();
	private HashSet<Pixel> usedColors = new HashSet<>();
	
	private boolean isCloseToBlack(Pixel color) {
		return color.r < 50 && color.g < 50 && color.b < 50;
	}
	
	private boolean hasBeenUsedBefore(Pixel color) {
		return usedColors.contains(color);
	}
	
	private Pixel generateNewColor() {
		Pixel newColor = new Pixel(0, 0, 0);
		
		while (isCloseToBlack(newColor) || hasBeenUsedBefore(newColor))
		{
			newColor.r = random.nextInt() % 256;
			newColor.g = random.nextInt() % 256;
			newColor.b = random.nextInt() % 256;
		}

		usedColors.add(newColor);
		
		return newColor;
	}
	
	public class DisjointSetNode {
		public DisjointSetNode parent;
		public int rank;

		public Pixel rgb;
	}

	public boolean isBlackPixel(Pixel gray) {
		return gray.r == 0;
	}

	@Override
	public PixelMatrix doOperation(PixelMatrix m) {

		Matrix nodeGroups = new Matrix(m.rows(), m.cols(), null);

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				// Build a matrix of DisjointSetNodes where every black pixel is
				// null and otherwise is set

				Pixel gray = m.get(r, c);

				if (isBlackPixel(gray)) {
					nodeGroups.put(r, c, null);
				} else {
					DisjointSetNode newNode = MakeSet();
					nodeGroups.put(r, c, newNode);
				}

			}
		}

		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				DisjointSetNode currentNode = (DisjointSetNode) nodeGroups.get(r, c);

				// identifying groups
				if (currentNode != null) {
					DisjointSetNode[] adjacentNodes = getAdjacentNodes(r, c, m.rows(), m.cols(), nodeGroups, 3);
					if (adjacentNodes.length == 8) {
						System.out.println("Found 8!");
					}
					for (DisjointSetNode node : adjacentNodes) {
						Union(currentNode, node);
					}
				}
			}
		}

		PixelMatrix result = new PixelMatrix(m.rows(), m.cols());
		double[] blackPixel = { 0, 0, 0 };
		for (int r = 0; r < m.rows(); r++) {
			for (int c = 0; c < m.cols(); c++) {

				DisjointSetNode currentNode = (DisjointSetNode) nodeGroups.get(r, c);

				if (currentNode == null) {
					result.put(r, c, blackPixel);
				} else {
					DisjointSetNode root = Find(currentNode);
					result.put(r, c, root.rgb);
				}
			}
		}

		return result;
	}

	private DisjointSetNode[] getAdjacentNodes(int r, int c, int rows, int cols, Matrix nodeGroups, int regionWidth) {

		int startR = r - regionWidth / 2;
		int startC = c - regionWidth / 2;

		int endR = r + regionWidth / 2;
		int endC = c + regionWidth / 2;

		List<DisjointSetNode> adjacentNodes = new ArrayList<DisjointSetNode>();

		for (int RR = startR; RR <= endR; RR++) {
			for (int CC = startC; CC <= endC; CC++) {
				if (isInImage(RR, CC, rows, cols)) {

					DisjointSetNode potentialNode = (DisjointSetNode) nodeGroups.get(RR, CC);

					if (potentialNode != null) {
						// pixel is set
						adjacentNodes.add(potentialNode);
					}
				}
			}
		}

		if (adjacentNodes.size() == 8) {
			System.out.println("Found 8!");
		}

		DisjointSetNode[] result = new DisjointSetNode[adjacentNodes.size()];
		result = adjacentNodes.toArray(result);
		return result;
	}

	private boolean isSamePoint(int r1, int c1, int r2, int c2) {
		return r1 == r2 && c1 == c2;
	}

	private boolean isInImage(int r, int c, int rows, int cols) {
		return r >= 0 && c >= 0 && r < rows && c < cols;
	}

	private DisjointSetNode MakeSet() {

		DisjointSetNode newNode = new DisjointSetNode();
		newNode.parent = newNode;
		newNode.rank = 0;

		newNode.rgb = generateNewColor();

		return newNode;
	}

	private DisjointSetNode Find(DisjointSetNode x) {
		if (x.parent != x) {
			x.parent = Find(x.parent);
		}
		return x.parent;
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

}
