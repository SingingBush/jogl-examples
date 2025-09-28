package com.singingbush.utils;

import com.jogamp.common.os.Platform;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.math.FovHVHalves;
import com.jogamp.opengl.math.Ray;
import com.jogamp.opengl.math.VectorUtil;
import jogamp.opengl.Debug;

import java.nio.FloatBuffer;

/*
* This is a copy of com.jogamp.opengl.math.FloatUtil from Jogl 2.3.2. It's needed because
* jogl removed various functions in 2.4.0 and 2.5.0, then in 2.6.0 removed the entire class.
* Not deprecation message were provided and no changelog seems to exist anywhere to recommend an alternative.
*
* There is a similar situation with VectorUtil from 2.5.0
*/
public class OldJoglFloatUtil {
    public static final boolean DEBUG = Debug.debug("Math");
    private static volatile boolean machEpsilonAvail = false;
    private static float machEpsilon = 0.0F;
    private static final boolean DEBUG_EPSILON = false;
    public static final float E = (float)Math.E;
    public static final float PI = (float)Math.PI;
    public static final float TWO_PI = ((float)Math.PI * 2F);
    public static final float HALF_PI = ((float)Math.PI / 2F);
    public static final float QUARTER_PI = ((float)Math.PI / 4F);
    public static final float SQUARED_PI = 9.869605F;
    public static final float EPSILON = 1.1920929E-7F;
    public static final float INV_DEVIANCE = 1.0E-5F;

    public static float[] makeIdentity(float[] var0, int var1) {
        var0[var1 + 0 + 0] = 1.0F;
        var0[var1 + 1 + 0] = 0.0F;
        var0[var1 + 2 + 0] = 0.0F;
        var0[var1 + 3 + 0] = 0.0F;
        var0[var1 + 0 + 4] = 0.0F;
        var0[var1 + 1 + 4] = 1.0F;
        var0[var1 + 2 + 4] = 0.0F;
        var0[var1 + 3 + 4] = 0.0F;
        var0[var1 + 0 + 8] = 0.0F;
        var0[var1 + 1 + 8] = 0.0F;
        var0[var1 + 2 + 8] = 1.0F;
        var0[var1 + 3 + 8] = 0.0F;
        var0[var1 + 0 + 12] = 0.0F;
        var0[var1 + 1 + 12] = 0.0F;
        var0[var1 + 2 + 12] = 0.0F;
        var0[var1 + 3 + 12] = 1.0F;
        return var0;
    }

    public static float[] makeIdentity(float[] var0) {
        var0[0] = 1.0F;
        var0[1] = 0.0F;
        var0[2] = 0.0F;
        var0[3] = 0.0F;
        var0[4] = 0.0F;
        var0[5] = 1.0F;
        var0[6] = 0.0F;
        var0[7] = 0.0F;
        var0[8] = 0.0F;
        var0[9] = 0.0F;
        var0[10] = 1.0F;
        var0[11] = 0.0F;
        var0[12] = 0.0F;
        var0[13] = 0.0F;
        var0[14] = 0.0F;
        var0[15] = 1.0F;
        return var0;
    }

    public static float[] makeTranslation(float[] var0, int var1, boolean var2, float var3, float var4, float var5) {
        if (var2) {
            makeIdentity(var0, var1);
        } else {
            var0[var1 + 0 + 0] = 1.0F;
            var0[var1 + 1 + 4] = 1.0F;
            var0[var1 + 2 + 8] = 1.0F;
            var0[var1 + 3 + 12] = 1.0F;
        }

        var0[var1 + 0 + 12] = var3;
        var0[var1 + 1 + 12] = var4;
        var0[var1 + 2 + 12] = var5;
        return var0;
    }

    public static float[] makeTranslation(float[] var0, boolean var1, float var2, float var3, float var4) {
        if (var1) {
            makeIdentity(var0);
        } else {
            var0[0] = 1.0F;
            var0[5] = 1.0F;
            var0[10] = 1.0F;
            var0[15] = 1.0F;
        }

        var0[12] = var2;
        var0[13] = var3;
        var0[14] = var4;
        return var0;
    }

    public static float[] makeScale(float[] var0, int var1, boolean var2, float var3, float var4, float var5) {
        if (var2) {
            makeIdentity(var0, var1);
        } else {
            var0[var1 + 0 + 12] = 0.0F;
            var0[var1 + 1 + 12] = 0.0F;
            var0[var1 + 2 + 12] = 0.0F;
            var0[var1 + 3 + 12] = 1.0F;
        }

        var0[var1 + 0 + 0] = var3;
        var0[var1 + 1 + 4] = var4;
        var0[var1 + 2 + 8] = var5;
        return var0;
    }

    public static float[] makeScale(float[] var0, boolean var1, float var2, float var3, float var4) {
        if (var1) {
            makeIdentity(var0);
        } else {
            var0[12] = 0.0F;
            var0[13] = 0.0F;
            var0[14] = 0.0F;
            var0[15] = 1.0F;
        }

        var0[0] = var2;
        var0[5] = var3;
        var0[10] = var4;
        return var0;
    }

    public static float[] makeRotationAxis(float[] var0, int var1, float var2, float var3, float var4, float var5, float[] var6) {
        float var7 = cos(var2);
        float var8 = 1.0F - var7;
        float var9 = sin(var2);
        var6[0] = var3;
        var6[1] = var4;
        var6[2] = var5;
        VectorUtil.normalizeVec3(var6);
        var3 = var6[0];
        var4 = var6[1];
        var5 = var6[2];
        float var10 = var3 * var4;
        float var11 = var3 * var5;
        float var12 = var3 * var9;
        float var13 = var4 * var9;
        float var14 = var4 * var5;
        float var15 = var5 * var9;
        var0[0 + var1] = var3 * var3 * var8 + var7;
        var0[1 + var1] = var10 * var8 + var15;
        var0[2 + var1] = var11 * var8 - var13;
        var0[3 + var1] = 0.0F;
        var0[4 + var1] = var10 * var8 - var15;
        var0[5 + var1] = var4 * var4 * var8 + var7;
        var0[6 + var1] = var14 * var8 + var12;
        var0[7 + var1] = 0.0F;
        var0[8 + var1] = var11 * var8 + var13;
        var0[9 + var1] = var14 * var8 - var12;
        var0[10 + var1] = var5 * var5 * var8 + var7;
        var0[11 + var1] = 0.0F;
        var0[12 + var1] = 0.0F;
        var0[13 + var1] = 0.0F;
        var0[14 + var1] = 0.0F;
        var0[15 + var1] = 1.0F;
        return var0;
    }

    public static float[] makeRotationEuler(float[] var0, int var1, float var2, float var3, float var4) {
        float var5 = cos(var3);
        float var6 = sin(var3);
        float var7 = cos(var4);
        float var8 = sin(var4);
        float var9 = cos(var2);
        float var10 = sin(var2);
        var0[0 + var1] = var5 * var7;
        var0[1 + var1] = var8;
        var0[2 + var1] = -var6 * var7;
        var0[3 + var1] = 0.0F;
        var0[4 + var1] = var6 * var10 - var5 * var8 * var9;
        var0[5 + var1] = var7 * var9;
        var0[6 + var1] = var6 * var8 * var9 + var5 * var10;
        var0[7 + var1] = 0.0F;
        var0[8 + var1] = var5 * var8 * var10 + var6 * var9;
        var0[9 + var1] = -var7 * var10;
        var0[10 + var1] = -var6 * var8 * var10 + var5 * var9;
        var0[11 + var1] = 0.0F;
        var0[12 + var1] = 0.0F;
        var0[13 + var1] = 0.0F;
        var0[14 + var1] = 0.0F;
        var0[15 + var1] = 1.0F;
        return var0;
    }

    public static float[] makeOrtho(float[] var0, int var1, boolean var2, float var3, float var4, float var5, float var6, float var7, float var8) {
        if (var2) {
            var0[var1 + 1 + 0] = 0.0F;
            var0[var1 + 2 + 0] = 0.0F;
            var0[var1 + 3 + 0] = 0.0F;
            var0[var1 + 0 + 4] = 0.0F;
            var0[var1 + 2 + 4] = 0.0F;
            var0[var1 + 3 + 4] = 0.0F;
            var0[var1 + 0 + 8] = 0.0F;
            var0[var1 + 1 + 8] = 0.0F;
            var0[var1 + 3 + 8] = 0.0F;
        }

        float var9 = var4 - var3;
        float var10 = var6 - var5;
        float var11 = var8 - var7;
        float var12 = -1.0F * (var4 + var3) / var9;
        float var13 = -1.0F * (var6 + var5) / var10;
        float var14 = -1.0F * (var8 + var7) / var11;
        var0[var1 + 0 + 0] = 2.0F / var9;
        var0[var1 + 1 + 4] = 2.0F / var10;
        var0[var1 + 2 + 8] = -2.0F / var11;
        var0[var1 + 0 + 12] = var12;
        var0[var1 + 1 + 12] = var13;
        var0[var1 + 2 + 12] = var14;
        var0[var1 + 3 + 12] = 1.0F;
        return var0;
    }

    public static float[] makeFrustum(float[] var0, int var1, boolean var2, float var3, float var4, float var5, float var6, float var7, float var8) throws GLException {
        if (!(var7 <= 0.0F) && !(var8 <= var7)) {
            if (var3 != var4 && var6 != var5) {
                if (var2) {
                    var0[var1 + 1 + 0] = 0.0F;
                    var0[var1 + 2 + 0] = 0.0F;
                    var0[var1 + 3 + 0] = 0.0F;
                    var0[var1 + 0 + 4] = 0.0F;
                    var0[var1 + 2 + 4] = 0.0F;
                    var0[var1 + 3 + 4] = 0.0F;
                    var0[var1 + 0 + 12] = 0.0F;
                    var0[var1 + 1 + 12] = 0.0F;
                }

                float var9 = 2.0F * var7;
                float var10 = var4 - var3;
                float var11 = var6 - var5;
                float var12 = var8 - var7;
                float var13 = (var4 + var3) / var10;
                float var14 = (var6 + var5) / var11;
                float var15 = -1.0F * (var8 + var7) / var12;
                float var16 = -2.0F * var8 * var7 / var12;
                var0[var1 + 0 + 0] = var9 / var10;
                var0[var1 + 1 + 4] = var9 / var11;
                var0[var1 + 0 + 8] = var13;
                var0[var1 + 1 + 8] = var14;
                var0[var1 + 2 + 8] = var15;
                var0[var1 + 3 + 8] = -1.0F;
                var0[var1 + 2 + 12] = var16;
                var0[var1 + 3 + 12] = 0.0F;
                return var0;
            } else {
                throw new GLException("GL_INVALID_VALUE: top,bottom and left,right must not be equal");
            }
        } else {
            throw new GLException("Requirements zNear > 0 and zFar > zNear, but zNear " + var7 + ", zFar " + var8);
        }
    }

    public static float[] makePerspective(float[] var0, int var1, boolean var2, float var3, float var4, float var5, float var6) throws GLException {
        float var7 = tan(var3 / 2.0F) * var5;
        float var8 = -1.0F * var7;
        float var9 = var4 * var8;
        float var10 = var4 * var7;
        return makeFrustum(var0, var1, var2, var9, var10, var8, var7, var5, var6);
    }

    public static float[] makePerspective(float[] var0, int var1, boolean var2, FovHVHalves var3, float var4, float var5) throws GLException {
        FovHVHalves var6 = var3.toTangents();
        float var7 = var6.top * var4;
        float var8 = -1.0F * var6.bottom * var4;
        float var9 = -1.0F * var6.left * var4;
        float var10 = var6.right * var4;
        return makeFrustum(var0, var1, var2, var9, var10, var8, var7, var4, var5);
    }

    public static float[] makeLookAt(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5, float[] var6, int var7, float[] var8) {
        var8[0] = var4[0 + var5] - var2[0 + var3];
        var8[1] = var4[1 + var5] - var2[1 + var3];
        var8[2] = var4[2 + var5] - var2[2 + var3];
        VectorUtil.normalizeVec3(var8);
        VectorUtil.crossVec3(var8, 3, var8, 0, var6, var7);
        VectorUtil.normalizeVec3(var8, 3);
        VectorUtil.crossVec3(var8, 6, var8, 3, var8, 0);
        var0[var1 + 0 + 0] = var8[3];
        var0[var1 + 0 + 1] = var8[6];
        var0[var1 + 0 + 2] = -var8[0];
        var0[var1 + 0 + 3] = 0.0F;
        var0[var1 + 4 + 0] = var8[4];
        var0[var1 + 4 + 1] = var8[7];
        var0[var1 + 4 + 2] = -var8[1];
        var0[var1 + 4 + 3] = 0.0F;
        var0[var1 + 8 + 0] = var8[5];
        var0[var1 + 8 + 1] = var8[8];
        var0[var1 + 8 + 2] = -var8[2];
        var0[var1 + 8 + 3] = 0.0F;
        var0[var1 + 12 + 0] = 0.0F;
        var0[var1 + 12 + 1] = 0.0F;
        var0[var1 + 12 + 2] = 0.0F;
        var0[var1 + 12 + 3] = 1.0F;
        makeTranslation(var8, true, -var2[0 + var3], -var2[1 + var3], -var2[2 + var3]);
        multMatrix(var0, var1, var8, 0);
        return var0;
    }

    public static float[] makePick(float[] var0, int var1, float var2, float var3, float var4, float var5, int[] var6, int var7, float[] var8) {
        if (!(var4 <= 0.0F) && !(var5 <= 0.0F)) {
            makeTranslation(var0, var1, true, ((float)var6[2 + var7] - 2.0F * (var2 - (float)var6[0 + var7])) / var4, ((float)var6[3 + var7] - 2.0F * (var3 - (float)var6[1 + var7])) / var5, 0.0F);
            makeScale(var8, true, (float)var6[2 + var7] / var4, (float)var6[3 + var7] / var5, 1.0F);
            multMatrix(var0, var1, var8, 0);
            return var0;
        } else {
            return null;
        }
    }

    public static float[] transposeMatrix(float[] var0, int var1, float[] var2, int var3) {
        var2[var3 + 0] = var0[var1 + 0];
        var2[var3 + 1] = var0[var1 + 4];
        var2[var3 + 2] = var0[var1 + 8];
        var2[var3 + 3] = var0[var1 + 12];
        var2[var3 + 0 + 4] = var0[var1 + 1 + 0];
        var2[var3 + 1 + 4] = var0[var1 + 1 + 4];
        var2[var3 + 2 + 4] = var0[var1 + 1 + 8];
        var2[var3 + 3 + 4] = var0[var1 + 1 + 12];
        var2[var3 + 0 + 8] = var0[var1 + 2 + 0];
        var2[var3 + 1 + 8] = var0[var1 + 2 + 4];
        var2[var3 + 2 + 8] = var0[var1 + 2 + 8];
        var2[var3 + 3 + 8] = var0[var1 + 2 + 12];
        var2[var3 + 0 + 12] = var0[var1 + 3 + 0];
        var2[var3 + 1 + 12] = var0[var1 + 3 + 4];
        var2[var3 + 2 + 12] = var0[var1 + 3 + 8];
        var2[var3 + 3 + 12] = var0[var1 + 3 + 12];
        return var2;
    }

    public static float[] transposeMatrix(float[] var0, float[] var1) {
        var1[0] = var0[0];
        var1[1] = var0[4];
        var1[2] = var0[8];
        var1[3] = var0[12];
        var1[4] = var0[1];
        var1[5] = var0[5];
        var1[6] = var0[9];
        var1[7] = var0[13];
        var1[8] = var0[2];
        var1[9] = var0[6];
        var1[10] = var0[10];
        var1[11] = var0[14];
        var1[12] = var0[3];
        var1[13] = var0[7];
        var1[14] = var0[11];
        var1[15] = var0[15];
        return var1;
    }

    public static float matrixDeterminant(float[] var0, int var1) {
        float var2 = var0[5 + var1];
        float var3 = var0[6 + var1];
        float var4 = var0[7 + var1];
        float var5 = var0[9 + var1];
        float var6 = var0[10 + var1];
        float var7 = var0[11 + var1];
        float var8 = var0[13 + var1];
        float var9 = var0[14 + var1];
        float var10 = var0[15 + var1];
        float var11 = 0.0F;
        var11 += var0[0 + var1] * (var2 * (var6 * var10 - var9 * var7) - var5 * (var3 * var10 - var9 * var4) + var8 * (var3 * var7 - var6 * var4));
        var2 = var0[1 + var1];
        var3 = var0[2 + var1];
        var4 = var0[3 + var1];
        var11 -= var0[4 + var1] * (var2 * (var6 * var10 - var9 * var7) - var5 * (var3 * var10 - var9 * var4) + var8 * (var3 * var7 - var6 * var4));
        var5 = var0[5 + var1];
        var6 = var0[6 + var1];
        var7 = var0[7 + var1];
        var11 += var0[8 + var1] * (var2 * (var6 * var10 - var9 * var7) - var5 * (var3 * var10 - var9 * var4) + var8 * (var3 * var7 - var6 * var4));
        var8 = var0[9 + var1];
        var9 = var0[10 + var1];
        var10 = var0[11 + var1];
        var11 -= var0[12 + var1] * (var2 * (var6 * var10 - var9 * var7) - var5 * (var3 * var10 - var9 * var4) + var8 * (var3 * var7 - var6 * var4));
        return var11;
    }

    public static float matrixDeterminant(float[] var0) {
        float var1 = var0[5];
        float var2 = var0[6];
        float var3 = var0[7];
        float var4 = var0[9];
        float var5 = var0[10];
        float var6 = var0[11];
        float var7 = var0[13];
        float var8 = var0[14];
        float var9 = var0[15];
        float var10 = 0.0F;
        var10 += var0[0] * (var1 * (var5 * var9 - var8 * var6) - var4 * (var2 * var9 - var8 * var3) + var7 * (var2 * var6 - var5 * var3));
        var1 = var0[1];
        var2 = var0[2];
        var3 = var0[3];
        var10 -= var0[4] * (var1 * (var5 * var9 - var8 * var6) - var4 * (var2 * var9 - var8 * var3) + var7 * (var2 * var6 - var5 * var3));
        var4 = var0[5];
        var5 = var0[6];
        var6 = var0[7];
        var10 += var0[8] * (var1 * (var5 * var9 - var8 * var6) - var4 * (var2 * var9 - var8 * var3) + var7 * (var2 * var6 - var5 * var3));
        var7 = var0[9];
        var8 = var0[10];
        var9 = var0[11];
        var10 -= var0[12] * (var1 * (var5 * var9 - var8 * var6) - var4 * (var2 * var9 - var8 * var3) + var7 * (var2 * var6 - var5 * var3));
        return var10;
    }

    public static float[] invertMatrix(float[] var0, int var1, float[] var2, int var3) {
        float var5 = Math.abs(var0[0]);

        for(int var6 = 1; var6 < 16; ++var6) {
            float var7 = Math.abs(var0[var6]);
            if (var7 > var5) {
                var5 = var7;
            }
        }

        if (0.0F == var5) {
            return null;
        } else {
            float var4 = 1.0F / var5;
            var5 = var0[0 + var1] * var4;
            float var39 = var0[1 + var1] * var4;
            float var40 = var0[2 + var1] * var4;
            float var8 = var0[3 + var1] * var4;
            float var9 = var0[4 + var1] * var4;
            float var10 = var0[5 + var1] * var4;
            float var11 = var0[6 + var1] * var4;
            float var12 = var0[7 + var1] * var4;
            float var13 = var0[8 + var1] * var4;
            float var14 = var0[9 + var1] * var4;
            float var15 = var0[10 + var1] * var4;
            float var16 = var0[11 + var1] * var4;
            float var17 = var0[12 + var1] * var4;
            float var18 = var0[13 + var1] * var4;
            float var19 = var0[14 + var1] * var4;
            float var20 = var0[15 + var1] * var4;
            float var21 = var10 * (var15 * var20 - var19 * var16) - var14 * (var11 * var20 - var19 * var12) + var18 * (var11 * var16 - var15 * var12);
            float var22 = -(var39 * (var15 * var20 - var19 * var16) - var14 * (var40 * var20 - var19 * var8) + var18 * (var40 * var16 - var15 * var8));
            float var23 = var39 * (var11 * var20 - var19 * var12) - var10 * (var40 * var20 - var19 * var8) + var18 * (var40 * var12 - var11 * var8);
            float var24 = -(var39 * (var11 * var16 - var15 * var12) - var10 * (var40 * var16 - var15 * var8) + var14 * (var40 * var12 - var11 * var8));
            float var25 = -(var9 * (var15 * var20 - var19 * var16) - var13 * (var11 * var20 - var19 * var12) + var17 * (var11 * var16 - var15 * var12));
            float var26 = var5 * (var15 * var20 - var19 * var16) - var13 * (var40 * var20 - var19 * var8) + var17 * (var40 * var16 - var15 * var8);
            float var27 = -(var5 * (var11 * var20 - var19 * var12) - var9 * (var40 * var20 - var19 * var8) + var17 * (var40 * var12 - var11 * var8));
            float var28 = var5 * (var11 * var16 - var15 * var12) - var9 * (var40 * var16 - var15 * var8) + var13 * (var40 * var12 - var11 * var8);
            float var29 = var9 * (var14 * var20 - var18 * var16) - var13 * (var10 * var20 - var18 * var12) + var17 * (var10 * var16 - var14 * var12);
            float var30 = -(var5 * (var14 * var20 - var18 * var16) - var13 * (var39 * var20 - var18 * var8) + var17 * (var39 * var16 - var14 * var8));
            float var31 = var5 * (var10 * var20 - var18 * var12) - var9 * (var39 * var20 - var18 * var8) + var17 * (var39 * var12 - var10 * var8);
            float var32 = -(var5 * (var10 * var16 - var14 * var12) - var9 * (var39 * var16 - var14 * var8) + var13 * (var39 * var12 - var10 * var8));
            float var33 = -(var9 * (var14 * var19 - var18 * var15) - var13 * (var10 * var19 - var18 * var11) + var17 * (var10 * var15 - var14 * var11));
            float var34 = var5 * (var14 * var19 - var18 * var15) - var13 * (var39 * var19 - var18 * var40) + var17 * (var39 * var15 - var14 * var40);
            float var35 = -(var5 * (var10 * var19 - var18 * var11) - var9 * (var39 * var19 - var18 * var40) + var17 * (var39 * var11 - var10 * var40));
            float var36 = var5 * (var10 * var15 - var14 * var11) - var9 * (var39 * var15 - var14 * var40) + var13 * (var39 * var11 - var10 * var40);
            float var37 = (var5 * var21 + var9 * var22 + var13 * var23 + var17 * var24) / var4;
            if (0.0F == var37) {
                return null;
            } else {
                var2[0 + var3] = var21 / var37;
                var2[1 + var3] = var22 / var37;
                var2[2 + var3] = var23 / var37;
                var2[3 + var3] = var24 / var37;
                var2[4 + var3] = var25 / var37;
                var2[5 + var3] = var26 / var37;
                var2[6 + var3] = var27 / var37;
                var2[7 + var3] = var28 / var37;
                var2[8 + var3] = var29 / var37;
                var2[9 + var3] = var30 / var37;
                var2[10 + var3] = var31 / var37;
                var2[11 + var3] = var32 / var37;
                var2[12 + var3] = var33 / var37;
                var2[13 + var3] = var34 / var37;
                var2[14 + var3] = var35 / var37;
                var2[15 + var3] = var36 / var37;
                return var2;
            }
        }
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

    public static boolean mapObjToWinCoords(float var0, float var1, float var2, float[] var3, int var4, float[] var5, int var6, int[] var7, int var8, float[] var9, int var10, float[] var11, float[] var12) {
        var11[0] = var0;
        var11[1] = var1;
        var11[2] = var2;
        var11[3] = 1.0F;
        multMatrixVec(var3, var4, var11, 0, var12, 0);
        multMatrixVec(var5, var6, var12, 0, var11, 0);
        if (var11[3] == 0.0F) {
            return false;
        } else {
            var11[3] = 1.0F / var11[3] * 0.5F;
            var11[0] = var11[0] * var11[3] + 0.5F;
            var11[1] = var11[1] * var11[3] + 0.5F;
            var11[2] = var11[2] * var11[3] + 0.5F;
            var9[0 + var10] = var11[0] * (float)var7[2 + var8] + (float)var7[0 + var8];
            var9[1 + var10] = var11[1] * (float)var7[3 + var8] + (float)var7[1 + var8];
            var9[2 + var10] = var11[2];
            return true;
        }
    }

    public static boolean mapObjToWinCoords(float var0, float var1, float var2, float[] var3, int[] var4, int var5, float[] var6, int var7, float[] var8, float[] var9) {
        var9[0] = var0;
        var9[1] = var1;
        var9[2] = var2;
        var9[3] = 1.0F;
        multMatrixVec(var3, var9, var8);
        if (var8[3] == 0.0F) {
            return false;
        } else {
            var8[3] = 1.0F / var8[3] * 0.5F;
            var8[0] = var8[0] * var8[3] + 0.5F;
            var8[1] = var8[1] * var8[3] + 0.5F;
            var8[2] = var8[2] * var8[3] + 0.5F;
            var6[0 + var7] = var8[0] * (float)var4[2 + var5] + (float)var4[0 + var5];
            var6[1 + var7] = var8[1] * (float)var4[3 + var5] + (float)var4[1 + var5];
            var6[2 + var7] = var8[2];
            return true;
        }
    }

    public static boolean mapWinToObjCoords(float var0, float var1, float var2, float[] var3, int var4, float[] var5, int var6, int[] var7, int var8, float[] var9, int var10, float[] var11, float[] var12) {
        multMatrix(var5, var6, var3, var4, var11, 0);
        if (null == invertMatrix(var11, var11)) {
            return false;
        } else {
            var12[0] = var0;
            var12[1] = var1;
            var12[2] = var2;
            var12[3] = 1.0F;
            var12[0] = (var12[0] - (float)var7[0 + var8]) / (float)var7[2 + var8];
            var12[1] = (var12[1] - (float)var7[1 + var8]) / (float)var7[3 + var8];
            var12[0] = var12[0] * 2.0F - 1.0F;
            var12[1] = var12[1] * 2.0F - 1.0F;
            var12[2] = var12[2] * 2.0F - 1.0F;
            multMatrixVec(var11, 0, var12, 0, var12, 4);
            if ((double)var12[7] == (double)0.0F) {
                return false;
            } else {
                var12[7] = 1.0F / var12[7];
                var9[0 + var10] = var12[4] * var12[7];
                var9[1 + var10] = var12[5] * var12[7];
                var9[2 + var10] = var12[6] * var12[7];
                return true;
            }
        }
    }

    public static boolean mapWinToObjCoords(float var0, float var1, float var2, float[] var3, int[] var4, int var5, float[] var6, int var7, float[] var8, float[] var9) {
        var8[0] = var0;
        var8[1] = var1;
        var8[2] = var2;
        var8[3] = 1.0F;
        var8[0] = (var8[0] - (float)var4[0 + var5]) / (float)var4[2 + var5];
        var8[1] = (var8[1] - (float)var4[1 + var5]) / (float)var4[3 + var5];
        var8[0] = var8[0] * 2.0F - 1.0F;
        var8[1] = var8[1] * 2.0F - 1.0F;
        var8[2] = var8[2] * 2.0F - 1.0F;
        multMatrixVec(var3, var8, var9);
        if ((double)var9[3] == (double)0.0F) {
            return false;
        } else {
            var9[3] = 1.0F / var9[3];
            var6[0 + var7] = var9[0] * var9[3];
            var6[1 + var7] = var9[1] * var9[3];
            var6[2 + var7] = var9[2] * var9[3];
            return true;
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

    public static boolean mapWinToObjCoords(float var0, float var1, float var2, float var3, float[] var4, int var5, float[] var6, int var7, int[] var8, int var9, float var10, float var11, float[] var12, int var13, float[] var14, float[] var15) {
        multMatrix(var6, var7, var4, var5, var14, 0);
        if (null == invertMatrix(var14, var14)) {
            return false;
        } else {
            var15[0] = var0;
            var15[1] = var1;
            var15[2] = var2;
            var15[3] = 1.0F;
            var15[0] = (var15[0] - (float)var8[0 + var9]) / (float)var8[2 + var9];
            var15[1] = (var15[1] - (float)var8[1 + var9]) / (float)var8[3 + var9];
            var15[2] = (var15[2] - var10) / (var11 - var10);
            var15[0] = var15[0] * 2.0F - 1.0F;
            var15[1] = var15[1] * 2.0F - 1.0F;
            var15[2] = var15[2] * 2.0F - 1.0F;
            multMatrixVec(var14, 0, var15, 0, var15, 4);
            if ((double)var15[7] == (double)0.0F) {
                return false;
            } else {
                var15[7] = 1.0F / var15[7];
                var12[0 + var13] = var15[4];
                var12[1 + var13] = var15[5];
                var12[2 + var13] = var15[6];
                var12[3 + var13] = var15[7];
                return true;
            }
        }
    }

    public static boolean mapWinToRay(float var0, float var1, float var2, float var3, float[] var4, int var5, float[] var6, int var7, int[] var8, int var9, Ray var10, float[] var11, float[] var12, float[] var13) {
        multMatrix(var6, var7, var4, var5, var11, 0);
        if (null == invertMatrix(var11, var11)) {
            return false;
        } else if (mapWinToObjCoords(var0, var1, var2, var3, var11, var8, var9, var10.orig, 0, var10.dir, 0, var12, var13)) {
            VectorUtil.normalizeVec3(VectorUtil.subVec3(var10.dir, var10.dir, var10.orig));
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

    public static float[] multMatrix(float[] var0, float[] var1, float[] var2) {
        float var3 = var1[0];
        float var4 = var1[1];
        float var5 = var1[2];
        float var6 = var1[3];
        float var7 = var1[4];
        float var8 = var1[5];
        float var9 = var1[6];
        float var10 = var1[7];
        float var11 = var1[8];
        float var12 = var1[9];
        float var13 = var1[10];
        float var14 = var1[11];
        float var15 = var1[12];
        float var16 = var1[13];
        float var17 = var1[14];
        float var18 = var1[15];
        float var19 = var0[0];
        float var20 = var0[4];
        float var21 = var0[8];
        float var22 = var0[12];
        var2[0] = var19 * var3 + var20 * var4 + var21 * var5 + var22 * var6;
        var2[4] = var19 * var7 + var20 * var8 + var21 * var9 + var22 * var10;
        var2[8] = var19 * var11 + var20 * var12 + var21 * var13 + var22 * var14;
        var2[12] = var19 * var15 + var20 * var16 + var21 * var17 + var22 * var18;
        var19 = var0[1];
        var20 = var0[5];
        var21 = var0[9];
        var22 = var0[13];
        var2[1] = var19 * var3 + var20 * var4 + var21 * var5 + var22 * var6;
        var2[5] = var19 * var7 + var20 * var8 + var21 * var9 + var22 * var10;
        var2[9] = var19 * var11 + var20 * var12 + var21 * var13 + var22 * var14;
        var2[13] = var19 * var15 + var20 * var16 + var21 * var17 + var22 * var18;
        var19 = var0[2];
        var20 = var0[6];
        var21 = var0[10];
        var22 = var0[14];
        var2[2] = var19 * var3 + var20 * var4 + var21 * var5 + var22 * var6;
        var2[6] = var19 * var7 + var20 * var8 + var21 * var9 + var22 * var10;
        var2[10] = var19 * var11 + var20 * var12 + var21 * var13 + var22 * var14;
        var2[14] = var19 * var15 + var20 * var16 + var21 * var17 + var22 * var18;
        var19 = var0[3];
        var20 = var0[7];
        var21 = var0[11];
        var22 = var0[15];
        var2[3] = var19 * var3 + var20 * var4 + var21 * var5 + var22 * var6;
        var2[7] = var19 * var7 + var20 * var8 + var21 * var9 + var22 * var10;
        var2[11] = var19 * var11 + var20 * var12 + var21 * var13 + var22 * var14;
        var2[15] = var19 * var15 + var20 * var16 + var21 * var17 + var22 * var18;
        return var2;
    }

    public static float[] multMatrix(float[] var0, int var1, float[] var2, int var3) {
        float var4 = var2[var3 + 0 + 0];
        float var5 = var2[var3 + 1 + 0];
        float var6 = var2[var3 + 2 + 0];
        float var7 = var2[var3 + 3 + 0];
        float var8 = var2[var3 + 0 + 4];
        float var9 = var2[var3 + 1 + 4];
        float var10 = var2[var3 + 2 + 4];
        float var11 = var2[var3 + 3 + 4];
        float var12 = var2[var3 + 0 + 8];
        float var13 = var2[var3 + 1 + 8];
        float var14 = var2[var3 + 2 + 8];
        float var15 = var2[var3 + 3 + 8];
        float var16 = var2[var3 + 0 + 12];
        float var17 = var2[var3 + 1 + 12];
        float var18 = var2[var3 + 2 + 12];
        float var19 = var2[var3 + 3 + 12];
        float var20 = var0[var1 + 0];
        float var21 = var0[var1 + 4];
        float var22 = var0[var1 + 8];
        float var23 = var0[var1 + 12];
        var0[var1 + 0] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        var0[var1 + 4] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        var0[var1 + 8] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        var0[var1 + 12] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
        var20 = var0[var1 + 1 + 0];
        var21 = var0[var1 + 1 + 4];
        var22 = var0[var1 + 1 + 8];
        var23 = var0[var1 + 1 + 12];
        var0[var1 + 1 + 0] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        var0[var1 + 1 + 4] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        var0[var1 + 1 + 8] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        var0[var1 + 1 + 12] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
        var20 = var0[var1 + 2 + 0];
        var21 = var0[var1 + 2 + 4];
        var22 = var0[var1 + 2 + 8];
        var23 = var0[var1 + 2 + 12];
        var0[var1 + 2 + 0] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        var0[var1 + 2 + 4] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        var0[var1 + 2 + 8] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        var0[var1 + 2 + 12] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
        var20 = var0[var1 + 3 + 0];
        var21 = var0[var1 + 3 + 4];
        var22 = var0[var1 + 3 + 8];
        var23 = var0[var1 + 3 + 12];
        var0[var1 + 3 + 0] = var20 * var4 + var21 * var5 + var22 * var6 + var23 * var7;
        var0[var1 + 3 + 4] = var20 * var8 + var21 * var9 + var22 * var10 + var23 * var11;
        var0[var1 + 3 + 8] = var20 * var12 + var21 * var13 + var22 * var14 + var23 * var15;
        var0[var1 + 3 + 12] = var20 * var16 + var21 * var17 + var22 * var18 + var23 * var19;
        return var0;
    }

    public static float[] multMatrix(float[] var0, float[] var1) {
        float var2 = var1[0];
        float var3 = var1[1];
        float var4 = var1[2];
        float var5 = var1[3];
        float var6 = var1[4];
        float var7 = var1[5];
        float var8 = var1[6];
        float var9 = var1[7];
        float var10 = var1[8];
        float var11 = var1[9];
        float var12 = var1[10];
        float var13 = var1[11];
        float var14 = var1[12];
        float var15 = var1[13];
        float var16 = var1[14];
        float var17 = var1[15];
        float var18 = var0[0];
        float var19 = var0[4];
        float var20 = var0[8];
        float var21 = var0[12];
        var0[0] = var18 * var2 + var19 * var3 + var20 * var4 + var21 * var5;
        var0[4] = var18 * var6 + var19 * var7 + var20 * var8 + var21 * var9;
        var0[8] = var18 * var10 + var19 * var11 + var20 * var12 + var21 * var13;
        var0[12] = var18 * var14 + var19 * var15 + var20 * var16 + var21 * var17;
        var18 = var0[1];
        var19 = var0[5];
        var20 = var0[9];
        var21 = var0[13];
        var0[1] = var18 * var2 + var19 * var3 + var20 * var4 + var21 * var5;
        var0[5] = var18 * var6 + var19 * var7 + var20 * var8 + var21 * var9;
        var0[9] = var18 * var10 + var19 * var11 + var20 * var12 + var21 * var13;
        var0[13] = var18 * var14 + var19 * var15 + var20 * var16 + var21 * var17;
        var18 = var0[2];
        var19 = var0[6];
        var20 = var0[10];
        var21 = var0[14];
        var0[2] = var18 * var2 + var19 * var3 + var20 * var4 + var21 * var5;
        var0[6] = var18 * var6 + var19 * var7 + var20 * var8 + var21 * var9;
        var0[10] = var18 * var10 + var19 * var11 + var20 * var12 + var21 * var13;
        var0[14] = var18 * var14 + var19 * var15 + var20 * var16 + var21 * var17;
        var18 = var0[3];
        var19 = var0[7];
        var20 = var0[11];
        var21 = var0[15];
        var0[3] = var18 * var2 + var19 * var3 + var20 * var4 + var21 * var5;
        var0[7] = var18 * var6 + var19 * var7 + var20 * var8 + var21 * var9;
        var0[11] = var18 * var10 + var19 * var11 + var20 * var12 + var21 * var13;
        var0[15] = var18 * var14 + var19 * var15 + var20 * var16 + var21 * var17;
        return var0;
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

    public static float[] multMatrixVec(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5) {
        var4[0 + var5] = var2[0 + var3] * var0[0 + var1] + var2[1 + var3] * var0[4 + var1] + var2[2 + var3] * var0[8 + var1] + var2[3 + var3] * var0[12 + var1];
        int var6 = 1 + var1;
        var4[1 + var5] = var2[0 + var3] * var0[0 + var6] + var2[1 + var3] * var0[4 + var6] + var2[2 + var3] * var0[8 + var6] + var2[3 + var3] * var0[12 + var6];
        int var7 = 2 + var1;
        var4[2 + var5] = var2[0 + var3] * var0[0 + var7] + var2[1 + var3] * var0[4 + var7] + var2[2 + var3] * var0[8 + var7] + var2[3 + var3] * var0[12 + var7];
        int var8 = 3 + var1;
        var4[3 + var5] = var2[0 + var3] * var0[0 + var8] + var2[1 + var3] * var0[4 + var8] + var2[2 + var3] * var0[8 + var8] + var2[3 + var3] * var0[12 + var8];
        return var4;
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

    public static float[] copyMatrixColumn(float[] var0, int var1, int var2, float[] var3, int var4) {
        var3[0 + var4] = var0[0 + var2 * 4 + var1];
        var3[1 + var4] = var0[1 + var2 * 4 + var1];
        var3[2 + var4] = var0[2 + var2 * 4 + var1];
        if (var3.length > 3 + var4) {
            var3[3 + var4] = var0[3 + var2 * 4 + var1];
        }

        return var3;
    }

    public static float[] copyMatrixRow(float[] var0, int var1, int var2, float[] var3, int var4) {
        var3[0 + var4] = var0[var2 + 0 + var1];
        var3[1 + var4] = var0[var2 + 4 + var1];
        var3[2 + var4] = var0[var2 + 8 + var1];
        if (var3.length > 3 + var4) {
            var3[3 + var4] = var0[var2 + 12 + var1];
        }

        return var3;
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

    public static StringBuilder matrixRowToString(StringBuilder var0, String var1, float[] var2, int var3, int var4, int var5, boolean var6, int var7) {
        if (null == var0) {
            var0 = new StringBuilder();
        }

        if (var6) {
            for(int var8 = 0; var8 < var5; ++var8) {
                var0.append(String.format(var1 + " ", var2[var3 + var7 * var5 + var8]));
            }
        } else {
            for(int var9 = 0; var9 < var5; ++var9) {
                var0.append(String.format(var1 + " ", var2[var3 + var7 + var9 * var4]));
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

    public static StringBuilder matrixToString(StringBuilder var0, String var1, String var2, float[] var3, int var4, int var5, int var6, boolean var7) {
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

    public static StringBuilder matrixToString(StringBuilder var0, String var1, String var2, float[] var3, int var4, float[] var5, int var6, int var7, int var8, boolean var9) {
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

    private static void calculateMachineEpsilonFloat() {
        float var2 = 1.0F;
        int var3 = 0;

        do {
            var2 /= 2.0F;
            ++var3;
        } while(1.0F + var2 / 2.0F != 1.0F);

        machEpsilon = var2;
    }

    public static float getMachineEpsilon() {
        if (!machEpsilonAvail) {
            synchronized(OldJoglFloatUtil.class) {
                if (!machEpsilonAvail) {
                    machEpsilonAvail = true;
                    calculateMachineEpsilonFloat();
                }
            }
        }

        return machEpsilon;
    }

    public static boolean isEqual(float var0, float var1) {
        return Float.floatToIntBits(var0) == Float.floatToIntBits(var1);
    }

    public static boolean isEqual(float var0, float var1, float var2) {
        if (Math.abs(var0 - var1) < var2) {
            return true;
        } else {
            return Float.floatToIntBits(var0) == Float.floatToIntBits(var1);
        }
    }

    public static int compare(float var0, float var1) {
        if (var0 < var1) {
            return -1;
        } else if (var0 > var1) {
            return 1;
        } else {
            int var2 = Float.floatToIntBits(var0);
            int var3 = Float.floatToIntBits(var1);
            if (var2 == var3) {
                return 0;
            } else {
                return var2 < var3 ? -1 : 1;
            }
        }
    }

    public static int compare(float var0, float var1, float var2) {
        return Math.abs(var0 - var1) < var2 ? 0 : compare(var0, var1);
    }

    public static boolean isZero(float var0, float var1) {
        return Math.abs(var0) < var1;
    }

    public static float abs(float var0) {
        return Math.abs(var0);
    }

    public static float pow(float var0, float var1) {
        return (float)Math.pow((double)var0, (double)var1);
    }

    public static float sin(float var0) {
        return (float)Math.sin((double)var0);
    }

    public static float asin(float var0) {
        return (float)Math.asin((double)var0);
    }

    public static float cos(float var0) {
        return (float)Math.cos((double)var0);
    }

    public static float acos(float var0) {
        return (float)Math.acos((double)var0);
    }

    public static float tan(float var0) {
        return (float)Math.tan((double)var0);
    }

    public static float atan(float var0) {
        return (float)Math.atan((double)var0);
    }

    public static float atan2(float var0, float var1) {
        return (float)Math.atan2((double)var0, (double)var1);
    }

    public static float sqrt(float var0) {
        return (float)Math.sqrt((double)var0);
    }

    public static float getZBufferEpsilon(int var0, float var1, float var2) {
        return var1 * var1 / (var2 * (float)(1 << var0) - var1);
    }

    public static int getZBufferValue(int var0, float var1, float var2, float var3) {
        float var4 = var3 / (var3 - var2);
        float var5 = var3 * var2 / (var2 - var3);
        return (int)((float)(1 << var0) * (var4 + var5 / var1));
    }

    public static float getOrthoWinZ(float var0, float var1, float var2) {
        return (1.0F / var1 - 1.0F / var0) / (1.0F / var1 - 1.0F / var2);
    }
}
