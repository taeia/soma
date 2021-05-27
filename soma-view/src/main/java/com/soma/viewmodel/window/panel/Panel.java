package com.soma.viewmodel.window.panel;

public interface Panel {

    String getTitle();

    Type getPanelType();

	public static enum Type {
        Grid, Content;
	}
}