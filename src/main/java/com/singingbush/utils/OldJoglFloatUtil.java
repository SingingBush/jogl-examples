package com.singingbush.utils;

import com.jogamp.common.os.Platform;
import com.jogamp.math.Matrix4f;
import com.jogamp.math.Ray;

import java.nio.FloatBuffer;

/**
 * Starting from jogl 2.4.0 various functions were removed from 'com.jogamp.opengl.math.FloatUtil' and in 2.6.0
 * the package was removed entirely to {@link com.jogamp.math.FloatUtil}. However, there wasn't a well documented
 * migration path and rather than deprecating methods they were simply removed with no warning.
 * <p>
 * This class takes some of the methods from com.jogamp.opengl.math.FloatUtil in Jogl 2.3.2 and 2.4.0. Where possible,
 * they've been re-implemented using the newer approach
*/
public final class OldJoglFloatUtil {

    /**
     * Make a rotation matrix from the given axis and angle in radians.
     * <pre>
     Rotation matrix (Column Order):
     xx(1-c)+c  xy(1-c)+zs xz(1-c)-ys 0
     xy(1-c)-zs yy(1-c)+c  yz(1-c)+xs 0
     xz(1-c)+ys yz(1-c)-xs zz(1-c)+c  0
     0          0          0          1
     * </pre>
     * <p>
     * All matrix fields are set.
     * </p>
     * @see <a href="http://web.archive.org/web/20041029003853/http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q38">Matrix-FAQ Q38</a>
     * @param m 4x4 matrix in column-major order (also result)
     * @param m_offset offset in given array <i>m</i>, i.e. start of the 4x4 matrix
     * @return given matrix for chaining
     *
     * @deprecated removed in 2.5.0 onward. Instead use {@link com.jogamp.math.Matrix4f#setToRotationAxis(float, float, float, float)}
     */
    @Deprecated // From Jogl 2.5.0 onward this function is missing. Added for compatibility
    public static float[] makeRotationAxis(float[] m, int m_offset, float angrad, float x, float y, float z, float[] tmpVec3f) {
        final Matrix4f m4f = new Matrix4f(m, m_offset);
        return m4f.setToRotationAxis(angrad, x, y, z).get(m);
    }

    /**
     * Make a concatenated rotation matrix in column-major order from the given Euler rotation angles in radians.
     * <p>
     * The rotations are applied in the given order:
     * <ul>
     *  <li>y - heading</li>
     *  <li>z - attitude</li>
     *  <li>x - bank</li>
     * </ul>
     * </p>
     * <p>
     * All matrix fields are set.
     * </p>
     * @param m 4x4 matrix in column-major order (also result)
     * @param m_offset offset in given array <i>m</i>, i.e. start of the 4x4 matrix
     * @param bankX the Euler pitch angle in radians. (rotation about the X axis)
     * @param headingY the Euler yaw angle in radians. (rotation about the Y axis)
     * @param attitudeZ the Euler roll angle in radians. (rotation about the Z axis)
     * @return given matrix for chaining
     * <p>
     * Implementation does not use Quaternion and hence is exposed to
     * <a href="http://web.archive.org/web/20041029003853/http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q34">Gimbal-Lock</a>
     * </p>
     * @see <a href="http://web.archive.org/web/20041029003853/http://www.j3d.org/matrix_faq/matrfaq_latest.html#Q36">Matrix-FAQ Q36</a>
     * @see <a href="http://www.euclideanspace.com/maths/geometry/rotations/conversions/eulerToMatrix/index.htm">euclideanspace.com-eulerToMatrix</a>
     *
     * @deprecated removed in 2.5.0 onward. Instead use {@link com.jogamp.math.Matrix4f#setToRotationEuler(float, float, float)}
     */
    @Deprecated // in Jogl 2.5.0 onward. this is here for compatibility
    public static float[] makeRotationEuler(float[] m, int m_offset, float bankX, float headingY, float attitudeZ) {
        final Matrix4f m4f = new Matrix4f(m, m_offset);
        return m4f.setToRotationEuler(bankX, headingY, attitudeZ).get(m);
    }

    /**
     * Make given matrix the orthogonal matrix based on given parameters.
     * <pre>
     Ortho matrix (Column Order):
     2/dx  0     0    0
     0     2/dy  0    0
     0     0     2/dz 0
     tx    ty    tz   1
     * </pre>
     * <p>
     * All matrix fields are only set if <code>initM</code> is <code>true</code>.
     * </p>
     * @param m 4x4 matrix in column-major order (also result)
     * @param m_offset offset in given array <i>m</i>, i.e. start of the 4x4 matrix
     * @param initM if true, given matrix will be initialized w/ identity matrix,
     *              otherwise only the orthogonal fields are set.
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param zNear
     * @param zFar
     * @return given matrix for chaining
     *
     * @deprecated removed in 2.5.0 onward. Instead use {@link com.jogamp.math.Matrix4f#setToOrtho(float, float, float, float, float, float)}
     */
    @Deprecated // From Jogl 2.5.0 onward this function is missing. Added for compatibility
    public static float[] makeOrtho(final float[] m, final int m_offset, final boolean initM,
                                    final float left, final float right,
                                    final float bottom, final float top,
                                    float zNear, float zFar) {
        final Matrix4f m4f = new Matrix4f(m, m_offset);
        return m4f.setToOrtho(left, right, bottom, top, zNear, zFar).get(m);
    }

    public static float[] invertMatrix(float[] var0, float[] var1) {
        float var3 = Math.abs(var0[0]);

        for(int var4 = 1; var4 < 16; ++var4) {
            float var5 = Math.abs(var0[var4]);
            if (var5 > var3) {
                var3 = var5;
            }
        }

        if (0.0F == var3) {
            return null;
        } else {
            float var2 = 1.0F / var3;
            var3 = var0[0] * var2;
            float var37 = var0[1] * var2;
            float var38 = var0[2] * var2;
            float var6 = var0[3] * var2;
            float var7 = var0[4] * var2;
            float var8 = var0[5] * var2;
            float var9 = var0[6] * var2;
            float var10 = var0[7] * var2;
            float var11 = var0[8] * var2;
            float var12 = var0[9] * var2;
            float var13 = var0[10] * var2;
            float var14 = var0[11] * var2;
            float var15 = var0[12] * var2;
            float var16 = var0[13] * var2;
            float var17 = var0[14] * var2;
            float var18 = var0[15] * var2;
            float var19 = var8 * (var13 * var18 - var17 * var14) - var12 * (var9 * var18 - var17 * var10) + var16 * (var9 * var14 - var13 * var10);
            float var20 = -(var37 * (var13 * var18 - var17 * var14) - var12 * (var38 * var18 - var17 * var6) + var16 * (var38 * var14 - var13 * var6));
            float var21 = var37 * (var9 * var18 - var17 * var10) - var8 * (var38 * var18 - var17 * var6) + var16 * (var38 * var10 - var9 * var6);
            float var22 = -(var37 * (var9 * var14 - var13 * var10) - var8 * (var38 * var14 - var13 * var6) + var12 * (var38 * var10 - var9 * var6));
            float var23 = -(var7 * (var13 * var18 - var17 * var14) - var11 * (var9 * var18 - var17 * var10) + var15 * (var9 * var14 - var13 * var10));
            float var24 = var3 * (var13 * var18 - var17 * var14) - var11 * (var38 * var18 - var17 * var6) + var15 * (var38 * var14 - var13 * var6);
            float var25 = -(var3 * (var9 * var18 - var17 * var10) - var7 * (var38 * var18 - var17 * var6) + var15 * (var38 * var10 - var9 * var6));
            float var26 = var3 * (var9 * var14 - var13 * var10) - var7 * (var38 * var14 - var13 * var6) + var11 * (var38 * var10 - var9 * var6);
            float var27 = var7 * (var12 * var18 - var16 * var14) - var11 * (var8 * var18 - var16 * var10) + var15 * (var8 * var14 - var12 * var10);
            float var28 = -(var3 * (var12 * var18 - var16 * var14) - var11 * (var37 * var18 - var16 * var6) + var15 * (var37 * var14 - var12 * var6));
            float var29 = var3 * (var8 * var18 - var16 * var10) - var7 * (var37 * var18 - var16 * var6) + var15 * (var37 * var10 - var8 * var6);
            float var30 = -(var3 * (var8 * var14 - var12 * var10) - var7 * (var37 * var14 - var12 * var6) + var11 * (var37 * var10 - var8 * var6));
            float var31 = -(var7 * (var12 * var17 - var16 * var13) - var11 * (var8 * var17 - var16 * var9) + var15 * (var8 * var13 - var12 * var9));
            float var32 = var3 * (var12 * var17 - var16 * var13) - var11 * (var37 * var17 - var16 * var38) + var15 * (var37 * var13 - var12 * var38);
            float var33 = -(var3 * (var8 * var17 - var16 * var9) - var7 * (var37 * var17 - var16 * var38) + var15 * (var37 * var9 - var8 * var38));
            float var34 = var3 * (var8 * var13 - var12 * var9) - var7 * (var37 * var13 - var12 * var38) + var11 * (var37 * var9 - var8 * var38);
            float var35 = (var3 * var19 + var7 * var20 + var11 * var21 + var15 * var22) / var2;
            if (0.0F == var35) {
                return null;
            } else {
                var1[0] = var19 / var35;
                var1[1] = var20 / var35;
                var1[2] = var21 / var35;
                var1[3] = var22 / var35;
                var1[4] = var23 / var35;
                var1[5] = var24 / var35;
                var1[6] = var25 / var35;
                var1[7] = var26 / var35;
                var1[8] = var27 / var35;
                var1[9] = var28 / var35;
                var1[10] = var29 / var35;
                var1[11] = var30 / var35;
                var1[12] = var31 / var35;
                var1[13] = var32 / var35;
                var1[14] = var33 / var35;
                var1[15] = var34 / var35;
                return var1;
            }
        }
    }

    public static boolean mapWinToObjCoords(float var0, float var1, float var2, float var3, float[] var4, int[] var5, int var6, float[] var7, int var8, float[] var9, int var10, float[] var11, float[] var12) {
        var11[0] = var0;
        var11[1] = var1;
        var11[3] = 1.0F;
        var11[0] = (var11[0] - (float)var5[0 + var6]) / (float)var5[2 + var6];
        var11[1] = (var11[1] - (float)var5[1 + var6]) / (float)var5[3 + var6];
        var11[0] = var11[0] * 2.0F - 1.0F;
        var11[1] = var11[1] * 2.0F - 1.0F;
        var11[2] = var2;
        var11[2] = var11[2] * 2.0F - 1.0F;
        multMatrixVec(var4, var11, var12);
        if ((double)var12[3] == (double)0.0F) {
            return false;
        } else {
            var12[3] = 1.0F / var12[3];
            var7[0 + var8] = var12[0] * var12[3];
            var7[1 + var8] = var12[1] * var12[3];
            var7[2 + var8] = var12[2] * var12[3];
            var11[2] = var3;
            var11[2] = var11[2] * 2.0F - 1.0F;
            multMatrixVec(var4, var11, var12);
            if ((double)var12[3] == (double)0.0F) {
                return false;
            } else {
                var12[3] = 1.0F / var12[3];
                var9[0 + var10] = var12[0] * var12[3];
                var9[1 + var10] = var12[1] * var12[3];
                var9[2 + var10] = var12[2] * var12[3];
                return true;
            }
        }
    }

    /**
     * Map two window coordinates w/ shared X/Y and distinctive Z
     * to a {@link Ray}. The resulting {@link Ray} maybe used for <i>picking</i>
     * using a AABBox#getRayIntersection(Ray, float[]) bounding box.
     * <p>
     * Notes for picking <i>winz0</i> and <i>winz1</i>:
     * <ul>
     *   <li>see {@link com.jogamp.math.FloatUtil#getZBufferEpsilon(int, float, float)}</li>
     *   <li>see {@link com.jogamp.math.FloatUtil#getZBufferValue(int, float, float, float)}</li>
     *   <li>see {@link com.jogamp.math.FloatUtil#getOrthoWinZ(float, float, float)}</li>
     * </ul>
     * </p>
     * @param winx
     * @param winy
     * @param winz0
     * @param winz1
     * @param modelMatrix 4x4 modelview matrix
     * @param modelMatrix_offset
     * @param projMatrix 4x4 projection matrix
     * @param projMatrix_offset
     * @param viewport 4 component viewport vector
     * @param viewport_offset
     * @param ray storage for the resulting {@link Ray}
     * @param mat4Tmp1 16 component matrix for temp storage
     * @param mat4Tmp2 16 component matrix for temp storage
     * @param vec4Tmp2 4 component vector for temp storage
     * @return true if successful, otherwise false (failed to invert matrix, or becomes z is infinity)
     *
     * @deprecated in Jogl 2.6.0 onward this should be {@link com.jogamp.math.Matrix4f#mapWinToRay} this is here for compatibility
     */
    @Deprecated // in Jogl 2.6.0 onward this should be 'com.jogamp.math.Matrix4f::mapWinToRay()' this is here for compatibility
    public static boolean mapWinToRay(
        final float winx, final float winy, final float winz0, final float winz1,
        final float[] modelMatrix, final int modelMatrix_offset,
        final float[] projMatrix, final int projMatrix_offset,
        final int[] viewport, final int viewport_offset,
        final Ray ray,
        final float[/*16*/] mat4Tmp1, final float[/*16*/] mat4Tmp2, final float[/*4*/] vec4Tmp2
    ) {
        // I'm not sure but new equivalent may be:
        // return Matrix4f.mapWinToRay(winx, winy, winz0, winz1,
        //     new Matrix4f(projMatrix, projMatrix_offset),
        //     new com.jogamp.math.Recti(viewport),
        //     ray
        // );

        multMatrix(projMatrix, projMatrix_offset, modelMatrix, modelMatrix_offset, mat4Tmp1, 0);
        if (null == invertMatrix(mat4Tmp1, mat4Tmp1)) {
            return false;
        } else if (mapWinToObjCoords(winx, winy, winz0, winz1, mat4Tmp1, viewport, viewport_offset, ray.orig.get(new float[] {}), 0, ray.dir.get(new float[] {}), 0, mat4Tmp2, vec4Tmp2)) {
            (ray.dir.minus(ray.dir, ray.orig)).normalize();
            return true;
        } else {
            return false;
        }
    }

    public static float[] multMatrix(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5) {
        float var6 = var2[var3 + 0 + 0];
        float var7 = var2[var3 + 1 + 0];
        float var8 = var2[var3 + 2 + 0];
        float var9 = var2[var3 + 3 + 0];
        float var10 = var2[var3 + 0 + 4];
        float var11 = var2[var3 + 1 + 4];
        float var12 = var2[var3 + 2 + 4];
        float var13 = var2[var3 + 3 + 4];
        float var14 = var2[var3 + 0 + 8];
        float var15 = var2[var3 + 1 + 8];
        float var16 = var2[var3 + 2 + 8];
        float var17 = var2[var3 + 3 + 8];
        float var18 = var2[var3 + 0 + 12];
        float var19 = var2[var3 + 1 + 12];
        float var20 = var2[var3 + 2 + 12];
        float var21 = var2[var3 + 3 + 12];
        float var22 = var0[var1 + 0];
        float var23 = var0[var1 + 4];
        float var24 = var0[var1 + 8];
        float var25 = var0[var1 + 12];
        var4[var5 + 0] = var22 * var6 + var23 * var7 + var24 * var8 + var25 * var9;
        var4[var5 + 4] = var22 * var10 + var23 * var11 + var24 * var12 + var25 * var13;
        var4[var5 + 8] = var22 * var14 + var23 * var15 + var24 * var16 + var25 * var17;
        var4[var5 + 12] = var22 * var18 + var23 * var19 + var24 * var20 + var25 * var21;
        var22 = var0[var1 + 1 + 0];
        var23 = var0[var1 + 1 + 4];
        var24 = var0[var1 + 1 + 8];
        var25 = var0[var1 + 1 + 12];
        var4[var5 + 1 + 0] = var22 * var6 + var23 * var7 + var24 * var8 + var25 * var9;
        var4[var5 + 1 + 4] = var22 * var10 + var23 * var11 + var24 * var12 + var25 * var13;
        var4[var5 + 1 + 8] = var22 * var14 + var23 * var15 + var24 * var16 + var25 * var17;
        var4[var5 + 1 + 12] = var22 * var18 + var23 * var19 + var24 * var20 + var25 * var21;
        var22 = var0[var1 + 2 + 0];
        var23 = var0[var1 + 2 + 4];
        var24 = var0[var1 + 2 + 8];
        var25 = var0[var1 + 2 + 12];
        var4[var5 + 2 + 0] = var22 * var6 + var23 * var7 + var24 * var8 + var25 * var9;
        var4[var5 + 2 + 4] = var22 * var10 + var23 * var11 + var24 * var12 + var25 * var13;
        var4[var5 + 2 + 8] = var22 * var14 + var23 * var15 + var24 * var16 + var25 * var17;
        var4[var5 + 2 + 12] = var22 * var18 + var23 * var19 + var24 * var20 + var25 * var21;
        var22 = var0[var1 + 3 + 0];
        var23 = var0[var1 + 3 + 4];
        var24 = var0[var1 + 3 + 8];
        var25 = var0[var1 + 3 + 12];
        var4[var5 + 3 + 0] = var22 * var6 + var23 * var7 + var24 * var8 + var25 * var9;
        var4[var5 + 3 + 4] = var22 * var10 + var23 * var11 + var24 * var12 + var25 * var13;
        var4[var5 + 3 + 8] = var22 * var14 + var23 * var15 + var24 * var16 + var25 * var17;
        var4[var5 + 3 + 12] = var22 * var18 + var23 * var19 + var24 * var20 + var25 * var21;
        return var4;
    }


    /** @deprecated */
    public static void multMatrix(FloatBuffer var0, FloatBuffer var1, float[] var2) {
        int var3 = var0.position();
        int var4 = var1.position();

        for(int var5 = 0; var5 < 4; ++var5) {
            int var6 = var3 + var5;
            float var7 = var0.get(var6 + 0);
            float var8 = var0.get(var6 + 4);
            float var9 = var0.get(var6 + 8);
            float var10 = var0.get(var6 + 12);
            var2[var5 + 0] = var7 * var1.get(var4 + 0 + 0) + var8 * var1.get(var4 + 1 + 0) + var9 * var1.get(var4 + 2 + 0) + var10 * var1.get(var4 + 3 + 0);
            var2[var5 + 4] = var7 * var1.get(var4 + 0 + 4) + var8 * var1.get(var4 + 1 + 4) + var9 * var1.get(var4 + 2 + 4) + var10 * var1.get(var4 + 3 + 4);
            var2[var5 + 8] = var7 * var1.get(var4 + 0 + 8) + var8 * var1.get(var4 + 1 + 8) + var9 * var1.get(var4 + 2 + 8) + var10 * var1.get(var4 + 3 + 8);
            var2[var5 + 12] = var7 * var1.get(var4 + 0 + 12) + var8 * var1.get(var4 + 1 + 12) + var9 * var1.get(var4 + 2 + 12) + var10 * var1.get(var4 + 3 + 12);
        }

    }

    /** @deprecated */
    public static void multMatrix(FloatBuffer var0, FloatBuffer var1) {
        int var2 = var0.position();
        int var3 = var1.position();

        for(int var4 = 0; var4 < 4; ++var4) {
            int var5 = var2 + var4;
            float var6 = var0.get(var5 + 0);
            float var7 = var0.get(var5 + 4);
            float var8 = var0.get(var5 + 8);
            float var9 = var0.get(var5 + 12);
            var0.put(var5 + 0, var6 * var1.get(var3 + 0 + 0) + var7 * var1.get(var3 + 1 + 0) + var8 * var1.get(var3 + 2 + 0) + var9 * var1.get(var3 + 3 + 0));
            var0.put(var5 + 4, var6 * var1.get(var3 + 0 + 4) + var7 * var1.get(var3 + 1 + 4) + var8 * var1.get(var3 + 2 + 4) + var9 * var1.get(var3 + 3 + 4));
            var0.put(var5 + 8, var6 * var1.get(var3 + 0 + 8) + var7 * var1.get(var3 + 1 + 8) + var8 * var1.get(var3 + 2 + 8) + var9 * var1.get(var3 + 3 + 8));
            var0.put(var5 + 12, var6 * var1.get(var3 + 0 + 12) + var7 * var1.get(var3 + 1 + 12) + var8 * var1.get(var3 + 2 + 12) + var9 * var1.get(var3 + 3 + 12));
        }

    }

    public static float[] multMatrixVec(float[] var0, float[] var1, float[] var2) {
        var2[0] = var1[0] * var0[0] + var1[1] * var0[4] + var1[2] * var0[8] + var1[3] * var0[12];
        var2[1] = var1[0] * var0[1] + var1[1] * var0[5] + var1[2] * var0[9] + var1[3] * var0[13];
        var2[2] = var1[0] * var0[2] + var1[1] * var0[6] + var1[2] * var0[10] + var1[3] * var0[14];
        var2[3] = var1[0] * var0[3] + var1[1] * var0[7] + var1[2] * var0[11] + var1[3] * var0[15];
        return var2;
    }

    /** @deprecated */
    public static void multMatrixVec(FloatBuffer var0, float[] var1, float[] var2) {
        int var3 = var0.position();

        for(int var4 = 0; var4 < 4; ++var4) {
            int var5 = var4 + var3;
            var2[var4] = var1[0] * var0.get(0 + var5) + var1[1] * var0.get(4 + var5) + var1[2] * var0.get(8 + var5) + var1[3] * var0.get(12 + var5);
        }

    }


    /** @deprecated */
    public static StringBuilder matrixRowToString(StringBuilder var0, String var1, FloatBuffer var2, int var3, int var4, int var5, boolean var6, int var7) {
        if (null == var0) {
            var0 = new StringBuilder();
        }

        int var8 = var3 + var2.position();
        if (var6) {
            for(int var9 = 0; var9 < var5; ++var9) {
                var0.append(String.format(var1 + " ", var2.get(var8 + var7 * var5 + var9)));
            }
        } else {
            for(int var10 = 0; var10 < var5; ++var10) {
                var0.append(String.format(var1 + " ", var2.get(var8 + var7 + var10 * var4)));
            }
        }

        return var0;
    }

    /** @deprecated */
    public static StringBuilder matrixToString(StringBuilder var0, String var1, String var2, FloatBuffer var3, int var4, int var5, int var6, boolean var7) {
        if (null == var0) {
            var0 = new StringBuilder();
        }

        String var8 = null == var1 ? "" : var1;

        for(int var9 = 0; var9 < var5; ++var9) {
            var0.append(var8).append("[ ");
            matrixRowToString(var0, var2, var3, var4, var5, var6, var7, var9);
            var0.append("]").append(Platform.getNewline());
        }

        return var0;
    }

    /** @deprecated */
    public static StringBuilder matrixToString(StringBuilder var0, String var1, String var2, FloatBuffer var3, int var4, FloatBuffer var5, int var6, int var7, int var8, boolean var9) {
        if (null == var0) {
            var0 = new StringBuilder();
        }

        String var10 = null == var1 ? "" : var1;

        for(int var11 = 0; var11 < var7; ++var11) {
            var0.append(var10).append("[ ");
            matrixRowToString(var0, var2, var3, var4, var7, var8, var9, var11);
            var0.append("=?= ");
            matrixRowToString(var0, var2, var5, var6, var7, var8, var9, var11);
            var0.append("]").append(Platform.getNewline());
        }

        return var0;
    }

}
