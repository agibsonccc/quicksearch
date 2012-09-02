package com.ccc.quicksearch.util;

import java.util.Map;

public class Node {


	
	
	
	public Node(String nodeKey, double count, Map<String, Node> children) {
		super();
		this.nodeKey = nodeKey;
		this.count = count;
		this.children = children;
	}
	
	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public Map<String, Node> getChildren() {
		return children;
	}

	public void setChildren(Map<String, Node> children) {
		this.children = children;
	}

	public String getNodeKey() {
		return nodeKey;
	}
	public void setNodeKey(String nodeKey) {
		this.nodeKey = nodeKey;
	}
	public void increment() {
		count++;
	}
	
	private String nodeKey;
	private double count;
	private Map<String,Node> children;
}
