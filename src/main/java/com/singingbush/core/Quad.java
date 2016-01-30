package com.singingbush.core;

import java.util.Arrays;

/**
 * A Quad has 4 vertices
 */
public class Quad extends Polygon {

    public Quad(Vertex[] vertices) {
        super(vertices);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quad quad = (Quad) o;
        return Arrays.equals(vertices, quad.vertices);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vertices);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Quad{");
        sb.append("vertices=").append(Arrays.toString(vertices));
        sb.append('}');
        return sb.toString();
    }
}
