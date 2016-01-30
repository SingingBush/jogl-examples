package com.singingbush.core;

import java.util.Arrays;

/**
 * A Mesh is made up of polygons. Either triangles or Quads
 */
public class PolygonMesh<T extends Polygon> {

    private T[] polygons;

    // Constructor
    public PolygonMesh(final T[] polygons) {
        this.polygons = polygons;
    }

    public T[] getPolygons() {
        return polygons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolygonMesh polygonMesh = (PolygonMesh) o;
        return Arrays.equals(polygons, polygonMesh.polygons);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(polygons);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PolygonMesh{");
        sb.append("polygons=").append(Arrays.toString(polygons));
        sb.append('}');
        return sb.toString();
    }
}
