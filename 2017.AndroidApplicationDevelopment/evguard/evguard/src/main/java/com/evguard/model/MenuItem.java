package com.evguard.model;

public class MenuItem {

	private String text;
	private int resource;
	
	public MenuItem(String s, int res) {
		this.text = s;
		this.resource = res;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}
}
