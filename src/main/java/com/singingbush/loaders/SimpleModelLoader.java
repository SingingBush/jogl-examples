package com.singingbush.loaders;

import com.singingbush.core.Quad;
import com.singingbush.core.PolygonMesh;
import com.singingbush.core.Triangle;
import com.singingbush.core.Vertex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Can be used to load a simple 3D model
 */
public class SimpleModelLoader {

    private static final String NUMPOLLIES = "NUMPOLLIES";
    private static final String COMMENT_MARKER = "//";

    /**
     * loads a model created from quads
     * @param modelFile resource file to load in
     * @return A PolygonMesh made up of quads
     */
    public static PolygonMesh<Quad> loadQuadMesh(final String modelFile) {
        try(InputStream stream = SimpleModelLoader.class.getClassLoader().getResourceAsStream(modelFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(stream))
        ) {
            int polygonCount = getPolyCount(in);

            final Quad[] quads = new Quad[polygonCount];

            for (int i = 0; i < polygonCount; i++) {
                final Vertex[] vertices = parseVertices(in, 4);
                quads[i] = new Quad(vertices);
            }

            return new PolygonMesh<>(quads);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * loads a model created from triangles
     * @param modelFile resource file to load in
     * @return A PolygonMesh made up of triangles
     */
    public static PolygonMesh<Triangle> loadTriangleMesh(final String modelFile) {
        try(InputStream stream = SimpleModelLoader.class.getClassLoader().getResourceAsStream(modelFile);
            BufferedReader in = new BufferedReader(new InputStreamReader(stream))
        ) {
            int polygonCount = getPolyCount(in);

            final Triangle[] triangles = new Triangle[polygonCount];

            for (int i = 0; i < polygonCount; i++) {
                final Vertex[] vertices = parseVertices(in, 3);
                triangles[i] = new Triangle(vertices);
            }

            return new PolygonMesh<>(triangles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean lineContainsData(final String line) {
        return line != null && !line.isEmpty() && !line.startsWith(COMMENT_MARKER);
    }

    private static int getPolyCount(BufferedReader reader) throws IOException {
        String line = null;

        while ((line = reader.readLine()) != null) {
            final String trimmedLine = line.trim();

            if (!lineContainsData(trimmedLine)) {
                continue;
            } else {
                if (trimmedLine.startsWith(NUMPOLLIES)) {
                    return Integer.parseInt(line.split(" ")[1]);
                }
            }
        }
        return 0;
    }

    private static Vertex[] parseVertices(BufferedReader in, int amount) throws IOException {
        final Vertex[] vertices = new Vertex[amount];

        for (int vert = 0; vert < amount; vert++) {
            String line = null;
            while ((line = in.readLine()) != null) {
                if (!lineContainsData(line.trim())) {
                    continue;
                }
                break;
            }

            if (lineContainsData(line.trim())) {
                Scanner scanner = new Scanner(line);
                float x = scanner.nextFloat();
                float y = scanner.nextFloat();
                float z = scanner.nextFloat();
                float u = scanner.nextFloat();
                float v = scanner.nextFloat();
                vertices[vert] = new Vertex(x, y, z, u, v);
            }
        }
        return vertices;
    }
}
