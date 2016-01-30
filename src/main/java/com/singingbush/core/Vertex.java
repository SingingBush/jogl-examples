package com.singingbush.core;

import java.util.Objects;

/**
 * A vertex has X, Y, Z (3D location) and uv (for texture coordinates)
 */
public class Vertex {

    float x, y, z; // 3D x,y,z location
    float u, v; // 2D texture coordinates

    public Vertex(float x, float y, float z, float u, float v) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getU() {
        return u;
    }

    public float getV() {
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Float.compare(vertex.x, x) == 0 &&
                Float.compare(vertex.y, y) == 0 &&
                Float.compare(vertex.z, z) == 0 &&
                Float.compare(vertex.u, u) == 0 &&
                Float.compare(vertex.v, v) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, u, v);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Vertex{");
        sb.append("x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", z=").append(z);
        sb.append(", u=").append(u);
        sb.append(", v=").append(v);
        sb.append('}');
        return sb.toString();
    }
}
