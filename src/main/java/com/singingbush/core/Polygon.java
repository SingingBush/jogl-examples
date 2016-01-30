package com.singingbush.core;

/**
 * Created by samael on 29/01/16.
 */
public abstract class Polygon {

    protected Vertex[] vertices;

    public Polygon(final Vertex[] vertices) {
        this.vertices = vertices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }
}
