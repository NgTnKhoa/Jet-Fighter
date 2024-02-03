package Model;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private int heuristic;
	private Node parent;
	private List<Node> childrens;
	private int x, y;
	
	public Node(int heurisic, Node parent, int x, int y) {
		this.heuristic = heurisic;
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.childrens = new ArrayList<Node>();
	}
	
	public int getHeuristic() {
		return heuristic;
	}
	
	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<Node> childrens) {
		this.childrens = childrens;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void addChild(Node node) {
		this.childrens.add(node);
	}
}
