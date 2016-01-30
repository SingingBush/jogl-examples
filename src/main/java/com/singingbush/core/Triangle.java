package com.singingbush.core;

import java.util.Arrays;

/**
 * A triangle has 3 vertices
 */
public class Triangle extends Polygon {

    public Triangle(final Vertex[] vertices) {
        super(vertices);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Arrays.equals(vertices, triangle.vertices);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vertices);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Triangle{");
        sb.append("vertices=").append(Arrays.toString(vertices));
        sb.append('}');
        return sb.toString();
    }
}
