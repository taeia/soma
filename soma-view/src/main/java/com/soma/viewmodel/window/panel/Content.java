package com.soma.viewmodel.window.panel;

public class Content implements Panel {

    private final String title;
    private final Type type;

    public Content(String title, Content.Type type) {
        this.title = title;
        this.type = type;
	}

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Panel.Type getPanelType() {
        return Panel.Type.Content;
    }

    public Content.Type getContentType() {
        return type;
    }

    public static enum Type {
        Toolbar, Thumbs, Image2d;
    }
}