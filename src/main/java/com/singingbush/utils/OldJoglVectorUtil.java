package com.singingbush.utils;

import com.jogamp.opengl.math.*;

import java.util.ArrayList;

/*
 * This is a copy of com.jogamp.opengl.math.VectorUtil from Jogl 2.4.0 with some changes from 2.5.0. It's needed because
 * jogl removed various functions in 2.5.0, then in 2.6.0 removed the entire class.
 * No deprecation messages were provided and no changelog seems to exist anywhere to recommend an alternative.
 *
 */
public final class OldJoglVectorUtil {
    public static final float[] VEC3_ONE = new float[]{1.0F, 1.0F, 1.0F};
    public static final float[] VEC3_ZERO = new float[]{0.0F, 0.0F, 0.0F};
    public static final float[] VEC3_UNIT_Y = new float[]{0.0F, 1.0F, 0.0F};
    public static final float[] VEC3_UNIT_Y_NEG = new float[]{0.0F, -1.0F, 0.0F};
    public static final float[] VEC3_UNIT_Z = new float[]{0.0F, 0.0F, 1.0F};
    public static final float[] VEC3_UNIT_Z_NEG = new float[]{0.0F, 0.0F, -1.0F};

    public static float[] copyVec2(float[] var0, int var1, float[] var2, int var3) {
        System.arraycopy(var2, var3, var0, var1, 2);
        return var0;
    }

    public static float[] copyVec3(float[] var0, int var1, float[] var2, int var3) {
        System.arraycopy(var2, var3, var0, var1, 3);
        return var0;
    }

    public static float[] copyVec4(float[] var0, int var1, float[] var2, int var3) {
        System.arraycopy(var2, var3, var0, var1, 4);
        return var0;
    }

    public static boolean isVec2Equal(float[] var0, int var1, float[] var2, int var3) {
        return FloatUtil.isEqual(var0[0 + var1], var2[0 + var3]) && FloatUtil.isEqual(var0[1 + var1], var2[1 + var3]);
    }

    public static boolean isVec3Equal(float[] var0, int var1, float[] var2, int var3) {
        return FloatUtil.isEqual(var0[0 + var1], var2[0 + var3]) && FloatUtil.isEqual(var0[1 + var1], var2[1 + var3]) && FloatUtil.isEqual(var0[2 + var1], var2[2 + var3]);
    }

    public static boolean isVec2Equal(float[] var0, int var1, float[] var2, int var3, float var4) {
        return FloatUtil.isEqual(var0[0 + var1], var2[0 + var3], var4) && FloatUtil.isEqual(var0[1 + var1], var2[1 + var3], var4);
    }

    public static boolean isVec3Equal(float[] var0, int var1, float[] var2, int var3, float var4) {
        return FloatUtil.isEqual(var0[0 + var1], var2[0 + var3], var4) && FloatUtil.isEqual(var0[1 + var1], var2[1 + var3], var4) && FloatUtil.isEqual(var0[2 + var1], var2[2 + var3], var4);
    }

    /**
    From 2.4.0
    */
    public static boolean isVec2Zero(float[] var0, int var1) {
        return 0.0F == var0[0 + var1] && 0.0F == var0[1 + var1];
    }

    /**
    From 2.5.0
    */
    public static boolean isVec2Zero(Vec3f var0) {
        return 0.0F == var0.x() && 0.0F == var0.y();
    }

    public static boolean isVec3Zero(float[] var0, int var1) {
        return 0.0F == var0[0 + var1] && 0.0F == var0[1 + var1] && 0.0F == var0[2 + var1];
    }

    public static boolean isVec2Zero(float[] var0, int var1, float var2) {
        return isZero(var0[0 + var1], var0[1 + var1], var2);
    }

    public static boolean isVec3Zero(float[] var0, int var1, float var2) {
        return isZero(var0[0 + var1], var0[1 + var1], var0[2 + var1], var2);
    }

    public static boolean isZero(float var0, float var1, float var2) {
        return FloatUtil.isZero(var0, var2) && FloatUtil.isZero(var1, var2);
    }

    public static boolean isZero(float var0, float var1, float var2, float var3) {
        return FloatUtil.isZero(var0, var3) && FloatUtil.isZero(var1, var3) && FloatUtil.isZero(var2, var3);
    }

    public static float distSquareVec3(float[] var0, float[] var1) {
        float var2 = var0[0] - var1[0];
        float var3 = var0[1] - var1[1];
        float var4 = var0[2] - var1[2];
        return var2 * var2 + var3 * var3 + var4 * var4;
    }

    public static float distVec3(float[] var0, float[] var1) {
        return FloatUtil.sqrt(distSquareVec3(var0, var1));
    }

    public static float dotVec3(float[] var0, float[] var1) {
        return var0[0] * var1[0] + var0[1] * var1[1] + var0[2] * var1[2];
    }

    public static float cosAngleVec3(float[] var0, float[] var1) {
        return dotVec3(var0, var1) / (normVec3(var0) * normVec3(var1));
    }

    public static float angleVec3(float[] var0, float[] var1) {
        return FloatUtil.acos(cosAngleVec3(var0, var1));
    }

    public static float normSquareVec2(float[] var0) {
        return var0[0] * var0[0] + var0[1] * var0[1];
    }

    public static float normSquareVec2(float[] var0, int var1) {
        float var2 = var0[0 + var1];
        float var3 = var2 * var2;
        var2 = var0[1 + var1];
        return var3 + var2 * var2;
    }

    public static float normSquareVec3(float[] var0) {
        return var0[0] * var0[0] + var0[1] * var0[1] + var0[2] * var0[2];
    }

    public static float normSquareVec3(float[] var0, int var1) {
        float var2 = var0[0 + var1];
        float var3 = var2 * var2;
        var2 = var0[1 + var1];
        var3 += var2 * var2;
        var2 = var0[2 + var1];
        return var3 + var2 * var2;
    }

    public static float normVec2(float[] var0) {
        return FloatUtil.sqrt(normSquareVec2(var0));
    }

    public static float normVec3(float[] var0) {
        return FloatUtil.sqrt(normSquareVec3(var0));
    }

    public static float[] normalizeVec2(float[] var0, float[] var1) {
        float var2 = normSquareVec2(var1);
        if (FloatUtil.isZero(var2, 1.1920929E-7F)) {
            var0[0] = 0.0F;
            var0[1] = 0.0F;
        } else {
            float var3 = 1.0F / FloatUtil.sqrt(var2);
            var0[0] = var1[0] * var3;
            var0[1] = var1[1] * var3;
        }

        return var0;
    }

    public static float[] normalizeVec2(float[] var0) {
        float var1 = normSquareVec2(var0);
        if (FloatUtil.isZero(var1, 1.1920929E-7F)) {
            var0[0] = 0.0F;
            var0[1] = 0.0F;
        } else {
            float var2 = 1.0F / FloatUtil.sqrt(var1);
            var0[0] *= var2;
            var0[1] *= var2;
        }

        return var0;
    }

    public static float[] normalizeVec3(float[] var0, float[] var1) {
        float var2 = normSquareVec3(var1);
        if (FloatUtil.isZero(var2, 1.1920929E-7F)) {
            var0[0] = 0.0F;
            var0[1] = 0.0F;
            var0[2] = 0.0F;
        } else {
            float var3 = 1.0F / FloatUtil.sqrt(var2);
            var0[0] = var1[0] * var3;
            var0[1] = var1[1] * var3;
            var0[2] = var1[2] * var3;
        }

        return var0;
    }

    public static float[] normalizeVec3(float[] var0) {
        float var1 = normSquareVec3(var0);
        if (FloatUtil.isZero(var1, 1.1920929E-7F)) {
            var0[0] = 0.0F;
            var0[1] = 0.0F;
            var0[2] = 0.0F;
        } else {
            float var2 = 1.0F / FloatUtil.sqrt(var1);
            var0[0] *= var2;
            var0[1] *= var2;
            var0[2] *= var2;
        }

        return var0;
    }

    public static float[] normalizeVec3(float[] var0, int var1) {
        float var2 = normSquareVec3(var0, var1);
        if (FloatUtil.isZero(var2, 1.1920929E-7F)) {
            var0[0 + var1] = 0.0F;
            var0[1 + var1] = 0.0F;
            var0[2 + var1] = 0.0F;
        } else {
            float var3 = 1.0F / FloatUtil.sqrt(var2);
            var0[0 + var1] *= var3;
            var0[1 + var1] *= var3;
            var0[2 + var1] *= var3;
        }

        return var0;
    }

    public static float[] scaleVec2(float[] var0, float[] var1, float var2) {
        var0[0] = var1[0] * var2;
        var0[1] = var1[1] * var2;
        return var0;
    }

    public static float[] scaleVec3(float[] var0, float[] var1, float var2) {
        var0[0] = var1[0] * var2;
        var0[1] = var1[1] * var2;
        var0[2] = var1[2] * var2;
        return var0;
    }

    public static float[] scaleVec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] * var2[0];
        var0[1] = var1[1] * var2[1];
        var0[2] = var1[2] * var2[2];
        return var0;
    }

    public static float[] scaleVec2(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] * var2[0];
        var0[1] = var1[1] * var2[1];
        return var0;
    }

    public static float[] divVec2(float[] var0, float[] var1, float var2) {
        var0[0] = var1[0] / var2;
        var0[1] = var1[1] / var2;
        return var0;
    }

    public static float[] divVec3(float[] var0, float[] var1, float var2) {
        var0[0] = var1[0] / var2;
        var0[1] = var1[1] / var2;
        var0[2] = var1[2] / var2;
        return var0;
    }

    public static float[] divVec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] / var2[0];
        var0[1] = var1[1] / var2[1];
        var0[2] = var1[2] / var2[2];
        return var0;
    }

    public static float[] divVec2(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] / var2[0];
        var0[1] = var1[1] / var2[1];
        return var0;
    }

    public static float[] addVec2(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] + var2[0];
        var0[1] = var1[1] + var2[1];
        return var0;
    }

    public static float[] addVec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] + var2[0];
        var0[1] = var1[1] + var2[1];
        var0[2] = var1[2] + var2[2];
        return var0;
    }

    public static float[] subVec2(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] - var2[0];
        var0[1] = var1[1] - var2[1];
        return var0;
    }

    // no newer replacement
    public static float[] subVec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[0] - var2[0];
        var0[1] = var1[1] - var2[1];
        var0[2] = var1[2] - var2[2];
        return var0;
    }

    // my replacement for compatibility only
    public static Vec3f subVec3(Vec3f var0, Vec3f var1, Vec3f var2) {
        return var0.minus(var1, var2);
    }

    public static float[] crossVec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = var1[1] * var2[2] - var1[2] * var2[1];
        var0[1] = var1[2] * var2[0] - var1[0] * var2[2];
        var0[2] = var1[0] * var2[1] - var1[1] * var2[0];
        return var0;
    }

    public static float[] crossVec3(float[] var0, int var1, float[] var2, int var3, float[] var4, int var5) {
        var0[0 + var1] = var2[1 + var3] * var4[2 + var5] - var2[2 + var3] * var4[1 + var5];
        var0[1 + var1] = var2[2 + var3] * var4[0 + var5] - var2[0 + var3] * var4[2 + var5];
        var0[2 + var1] = var2[0 + var3] * var4[1 + var5] - var2[1 + var3] * var4[0 + var5];
        return var0;
    }

    public static float[] mulColMat4Vec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = var2[0] * var1[0] + var2[1] * var1[4] + var2[2] * var1[8] + var1[12];
        var0[1] = var2[0] * var1[1] + var2[1] * var1[5] + var2[2] * var1[9] + var1[13];
        var0[2] = var2[0] * var1[2] + var2[1] * var1[6] + var2[2] * var1[10] + var1[14];
        return var0;
    }

    public static float[] mulRowMat4Vec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = var2[0] * var1[0] + var2[1] * var1[1] + var2[2] * var1[2] + var1[3];
        var0[1] = var2[0] * var1[4] + var2[1] * var1[5] + var2[2] * var1[6] + var1[7];
        var0[2] = var2[0] * var1[8] + var2[1] * var1[9] + var2[2] * var1[10] + var1[11];
        return var0;
    }

    public static float mid(float var0, float var1) {
        return (var0 + var1) * 0.5F;
    }

    // Jogl 2.5.0 approach uses Vec3f objects
    public static Vec3f midVec3(Vec3f var0, Vec3f var1, Vec3f var2) {
        var0.set((var1.x() + var2.x()) * 0.5F, (var1.y() + var2.y()) * 0.5F, (var1.z() + var2.z()) * 0.5F);
        return var0;
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
    public static float[] midVec3(float[] var0, float[] var1, float[] var2) {
        var0[0] = (var1[0] + var2[0]) * 0.5F;
        var0[1] = (var1[1] + var2[1]) * 0.5F;
        var0[2] = (var1[2] + var2[2]) * 0.5F;
        return var0;
    }

    // Jogl 2.5.0 approach uses Vec3f objects
    public static float determinantVec3(Vec3f var0, Vec3f var1, Vec3f var2) {
        return var0.x() * var1.y() * var2.z() + var0.y() * var1.z() * var2.x() + var0.z() * var1.x() * var2.y() - var0.x() * var1.z() * var2.y() - var0.y() * var1.x() * var2.z() - var0.z() * var1.y() * var2.x();
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
    public static float determinantVec3(float[] var0, float[] var1, float[] var2) {
        return var0[0] * var1[1] * var2[2] + var0[1] * var1[2] * var2[0] + var0[2] * var1[0] * var2[1] - var0[0] * var1[2] * var2[1] - var0[1] * var1[0] * var2[2] - var0[2] * var1[1] * var2[0];
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
    public static boolean isCollinearVec3(float[] var0, float[] var1, float[] var2) {
        return FloatUtil.isZero(determinantVec3(var0, var1, var2), 1.1920929E-7F);
    }

    // Jogl 2.5.0 approach uses Vec3f objects
    public static boolean isCollinearVec3(Vec3f var0, Vec3f var1, Vec3f var2) {
        return FloatUtil.isZero(determinantVec3(var0, var1, var2), 1.1920929E-7F);
    }

    // Jogl 2.5.0 approach
    public static boolean isInCircleVec2(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3) {
        return (var0.x() * var0.x() + var0.y() * var0.y()) * triAreaVec2(var1, var2, var3) - (var1.x() * var1.x() + var1.y() * var1.y()) * triAreaVec2(var0, var2, var3) + (var2.x() * var2.x() + var2.y() * var2.y()) * triAreaVec2(var0, var1, var3) - (var3.x() * var3.x() + var3.y() * var3.y()) * triAreaVec2(var0, var1, var2) > 0.0F;
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
//    public static boolean isInCircleVec2(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3) {
//        float[] var4 = var0.getCoord();
//        float[] var5 = var1.getCoord();
//        float[] var6 = var2.getCoord();
//        float[] var7 = var3.getCoord();
//        return (var4[0] * var4[0] + var4[1] * var4[1]) * triAreaVec2(var5, var6, var7) - (var5[0] * var5[0] + var5[1] * var5[1]) * triAreaVec2(var4, var6, var7) + (var6[0] * var6[0] + var6[1] * var6[1]) * triAreaVec2(var4, var5, var7) - (var7[0] * var7[0] + var7[1] * var7[1]) * triAreaVec2(var4, var5, var6) > 0.0F;
//    }

    // Jogl 2.5.0 approach
    public static float triAreaVec2(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2) {
        return (var1.x() - var0.x()) * (var2.y() - var0.y()) - (var1.y() - var0.y()) * (var2.x() - var0.x());
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
//    public static float triAreaVec2(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2) {
//        float[] var3 = var0.getCoord();
//        float[] var4 = var1.getCoord();
//        float[] var5 = var2.getCoord();
//        return (var4[0] - var3[0]) * (var5[1] - var3[1]) - (var4[1] - var3[1]) * (var5[0] - var3[0]);
//    }

    public static float triAreaVec2(float[] var0, float[] var1, float[] var2) {
        return (var1[0] - var0[0]) * (var2[1] - var0[1]) - (var1[1] - var0[1]) * (var2[0] - var0[0]);
    }

    // Jogl 2.5.0 approach uses Vec3f objects
    public static boolean isInTriangleVec3(Vec3f var0, Vec3f var1, Vec3f var2, Vec3f var3, Vec3f var4, Vec3f var5, Vec3f var6) {
        var4.minus(var2, var0);
        var5.minus(var1, var0);
        var6.minus(var3, var0);
        float var7 = var4.dot(var4);
        float var8 = var4.dot(var5);
        float var9 = var5.dot(var5);
        float var10 = var4.dot(var6);
        float var11 = var5.dot(var6);
        float var12 = 1.0F / (var7 * var9 - var8 * var8);
        float var13 = (var9 * var10 - var8 * var11) * var12;
        float var14 = (var7 * var11 - var8 * var10) * var12;
        return var13 >= 0.0F && var14 >= 0.0F && var13 + var14 < 1.0F;
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
    public static boolean isInTriangleVec3(float[] var0, float[] var1, float[] var2, float[] var3, float[] var4, float[] var5, float[] var6) {
        subVec3(var4, var2, var0);
        subVec3(var5, var1, var0);
        subVec3(var6, var3, var0);
        float var7 = dotVec3(var4, var4);
        float var8 = dotVec3(var4, var5);
        float var9 = dotVec3(var5, var5);
        float var10 = dotVec3(var4, var6);
        float var11 = dotVec3(var5, var6);
        float var12 = 1.0F / (var7 * var9 - var8 * var8);
        float var13 = (var9 * var10 - var8 * var11) * var12;
        float var14 = (var7 * var11 - var8 * var10) * var12;
        return var13 >= 0.0F && var14 >= 0.0F && var13 + var14 < 1.0F;
    }

    public static boolean isVec3InTriangle3(Vec3f var0, Vec3f var1, Vec3f var2, Vec3f var3, Vec3f var4, Vec3f var5, Vec3f var6, Vec3f var7, Vec3f var8) {
        var6.minus(var2, var0);
        var7.minus(var1, var0);
        float var9 = var6.dot(var6);
        float var10 = var6.dot(var7);
        float var11 = var7.dot(var7);
        float var12 = 1.0F / (var9 * var11 - var10 * var10);
        var8.minus(var3, var0);
        float var13 = var6.dot(var8);
        float var14 = var7.dot(var8);
        float var15 = (var11 * var13 - var10 * var14) * var12;
        float var16 = (var9 * var14 - var10 * var13) * var12;
        if (var15 >= 0.0F && var16 >= 0.0F && var15 + var16 < 1.0F) {
            return true;
        } else {
            var8.minus(var4, var0);
            var13 = var6.dot(var8);
            var14 = var7.dot(var8);
            var15 = (var11 * var13 - var10 * var14) * var12;
            var16 = (var9 * var14 - var10 * var13) * var12;
            if (var15 >= 0.0F && var16 >= 0.0F && var15 + var16 < 1.0F) {
                return true;
            } else {
                var8.minus(var5, var0);
                var13 = var6.dot(var8);
                var14 = var7.dot(var8);
                var15 = (var11 * var13 - var10 * var14) * var12;
                var16 = (var9 * var14 - var10 * var13) * var12;
                return var15 >= 0.0F && var16 >= 0.0F && var15 + var16 < 1.0F;
            }
        }
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
    public static boolean isVec3InTriangle3(float[] var0, float[] var1, float[] var2, float[] var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8) {
        subVec3(var6, var2, var0);
        subVec3(var7, var1, var0);
        float var9 = dotVec3(var6, var6);
        float var10 = dotVec3(var6, var7);
        float var11 = dotVec3(var7, var7);
        float var12 = 1.0F / (var9 * var11 - var10 * var10);
        subVec3(var8, var3, var0);
        float var13 = dotVec3(var6, var8);
        float var14 = dotVec3(var7, var8);
        float var15 = (var11 * var13 - var10 * var14) * var12;
        float var16 = (var9 * var14 - var10 * var13) * var12;
        if (var15 >= 0.0F && var16 >= 0.0F && var15 + var16 < 1.0F) {
            return true;
        } else {
            subVec3(var8, var3, var0);
            var13 = dotVec3(var6, var8);
            var14 = dotVec3(var7, var8);
            var15 = (var11 * var13 - var10 * var14) * var12;
            var16 = (var9 * var14 - var10 * var13) * var12;
            if (var15 >= 0.0F && var16 >= 0.0F && var15 + var16 < 1.0F) {
                return true;
            } else {
                subVec3(var8, var4, var0);
                var13 = dotVec3(var6, var8);
                var14 = dotVec3(var7, var8);
                var15 = (var11 * var13 - var10 * var14) * var12;
                var16 = (var9 * var14 - var10 * var13) * var12;
                return var15 >= 0.0F && var16 >= 0.0F && var15 + var16 < 1.0F;
            }
        }
    }

    // Jogl 2.5.0 approach
    public static boolean isVec3InTriangle3(Vec3f var0, Vec3f var1, Vec3f var2, Vec3f var3, Vec3f var4, Vec3f var5, Vec3f var6, Vec3f var7, Vec3f var8, float var9) {
        var6.minus(var2, var0);
        var7.minus(var1, var0);
        float var10 = var6.dot(var6);
        float var11 = var6.dot(var7);
        float var12 = var7.dot(var7);
        float var13 = 1.0F / (var10 * var12 - var11 * var11);
        var8.minus(var3, var0);
        float var14 = var6.dot(var8);
        float var15 = var7.dot(var8);
        float var16 = (var12 * var14 - var11 * var15) * var13;
        float var17 = (var10 * var15 - var11 * var14) * var13;
        if (FloatUtil.compare(var16, 0.0F, var9) >= 0 && FloatUtil.compare(var17, 0.0F, var9) >= 0 && FloatUtil.compare(var16 + var17, 1.0F, var9) < 0) {
            return true;
        } else {
            var8.minus(var4, var0);
            var14 = var6.dot(var8);
            var15 = var7.dot(var8);
            var16 = (var12 * var14 - var11 * var15) * var13;
            var17 = (var10 * var15 - var11 * var14) * var13;
            if (FloatUtil.compare(var16, 0.0F, var9) >= 0 && FloatUtil.compare(var17, 0.0F, var9) >= 0 && FloatUtil.compare(var16 + var17, 1.0F, var9) < 0) {
                return true;
            } else {
                var8.minus(var5, var0);
                var14 = var6.dot(var8);
                var15 = var7.dot(var8);
                var16 = (var12 * var14 - var11 * var15) * var13;
                var17 = (var10 * var15 - var11 * var14) * var13;
                return FloatUtil.compare(var16, 0.0F, var9) >= 0 && FloatUtil.compare(var17, 0.0F, var9) >= 0 && FloatUtil.compare(var16 + var17, 1.0F, var9) < 0;
            }
        }
    }

    @Deprecated // Jogl 2.4.0 approach is float[] based
    public static boolean isVec3InTriangle3(float[] var0, float[] var1, float[] var2, float[] var3, float[] var4, float[] var5, float[] var6, float[] var7, float[] var8, float var9) {
        subVec3(var6, var2, var0);
        subVec3(var7, var1, var0);
        float var10 = dotVec3(var6, var6);
        float var11 = dotVec3(var6, var7);
        float var12 = dotVec3(var7, var7);
        float var13 = 1.0F / (var10 * var12 - var11 * var11);
        subVec3(var8, var3, var0);
        float var14 = dotVec3(var6, var8);
        float var15 = dotVec3(var7, var8);
        float var16 = (var12 * var14 - var11 * var15) * var13;
        float var17 = (var10 * var15 - var11 * var14) * var13;
        if (FloatUtil.compare(var16, 0.0F, var9) >= 0 && FloatUtil.compare(var17, 0.0F, var9) >= 0 && FloatUtil.compare(var16 + var17, 1.0F, var9) < 0) {
            return true;
        } else {
            subVec3(var8, var3, var0);
            var14 = dotVec3(var6, var8);
            var15 = dotVec3(var7, var8);
            var16 = (var12 * var14 - var11 * var15) * var13;
            var17 = (var10 * var15 - var11 * var14) * var13;
            if (FloatUtil.compare(var16, 0.0F, var9) >= 0 && FloatUtil.compare(var17, 0.0F, var9) >= 0 && FloatUtil.compare(var16 + var17, 1.0F, var9) < 0) {
                return true;
            } else {
                subVec3(var8, var4, var0);
                var14 = dotVec3(var6, var8);
                var15 = dotVec3(var7, var8);
                var16 = (var12 * var14 - var11 * var15) * var13;
                var17 = (var10 * var15 - var11 * var14) * var13;
                return FloatUtil.compare(var16, 0.0F, var9) >= 0 && FloatUtil.compare(var17, 0.0F, var9) >= 0 && FloatUtil.compare(var16 + var17, 1.0F, var9) < 0;
            }
        }
    }

    public static boolean isCCW(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2) {
        return triAreaVec2(var0, var1, var2) > 0.0F;
    }

    public static boolean ccw(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2) {
        return triAreaVec2(var0, var1, var2) > 0.0F;
    }

    public static OldJoglVectorUtil.Winding getWinding(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2) {
        return triAreaVec2(var0, var1, var2) > 0.0F ? OldJoglVectorUtil.Winding.CCW : OldJoglVectorUtil.Winding.CW;
    }

    public static float area(ArrayList<? extends Vert2fImmutable> var0) {
        int var1 = var0.size();
        float var2 = 0.0F;
        int var3 = var1 - 1;

        for(int var4 = 0; var4 < var1; var3 = var4++) {
            Vert2fImmutable var5 = (Vert2fImmutable)var0.get(var3);
            Vert2fImmutable var6 = (Vert2fImmutable)var0.get(var4);
            var2 += var5.x() * var6.y() - var6.x() * var5.y();
            // Vec3f var5 = ((Vert3fImmutable)var0.get(var3)).getCoord();
            // Vec3f var6 = ((Vert3fImmutable)var0.get(var4)).getCoord();
            // var2 += var5[0] * var6[1] - var6[0] * var5[1];
        }

        return var2;
    }

    public static OldJoglVectorUtil.Winding getWinding(ArrayList<? extends Vert2fImmutable> var0) {
        return area(var0) >= 0.0F ? OldJoglVectorUtil.Winding.CCW : OldJoglVectorUtil.Winding.CW;
    }

    public static float[] getNormalVec2(float[] var0, float[] var1, float[] var2) {
        subVec2(var0, var2, var1);
        float var3 = var0[0];
        var0[0] = -var0[1];
        var0[1] = var3;
        return normalizeVec2(var0);
    }

    public static float[] getNormalVec3(float[] var0, float[] var1, float[] var2, float[] var3, float[] var4, float[] var5) {
        subVec3(var4, var2, var1);
        subVec3(var5, var3, var1);
        return normalizeVec3(crossVec3(var0, var4, var5));
    }

    public static Vec4f getPlaneVec3(Vec4f var0, Vec3f var1, Vec3f var2) {
        var0.set(var1, -var1.dot(var2));
        return var0;
    }

    public static Vec4f getPlaneVec3(Vec4f var0, Vec3f var1, Vec3f var2, Vec3f var3, Vec3f var4, Vec3f var5, Vec3f var6) {
        var6.cross(var4.minus(var2, var1), var5.minus(var3, var1)).normalize();
        var0.set(var6, -var6.dot(var1));
        return var0;
    }

    public static Vec3f line2PlaneIntersection(Vec3f var0, Ray var1, Vec4f var2, float var3) {
        Vec3f var4 = new Vec3f(var2);
        float var5 = var1.dir.dot(var4);
        if (Math.abs(var5) < var3) {
            return null;
        } else {
            var0.set(var1.dir);
            return var0.scale(-(var1.orig.dot(var4) + var2.w()) / var5).add(var1.orig);
        }
    }

    public static float[] getPlaneVec3(float[] var0, float[] var1, float[] var2) {
        System.arraycopy(var1, 0, var0, 0, 3);
        var0[3] = -dotVec3(var1, var2);
        return var0;
    }

    public static float[] getPlaneVec3(float[] var0, float[] var1, float[] var2, float[] var3, float[] var4, float[] var5) {
        getNormalVec3(var0, var1, var2, var3, var4, var5);
        var0[3] = -dotVec3(var0, var1);
        return var0;
    }

//    public static float[] line2PlaneIntersection(float[] var0, Ray var1, float[] var2, float var3) {
//        float var4 = dotVec3(var1.dir, var2);
//        if (Math.abs(var4) < var3) {
//            return null;
//        } else {
//            scaleVec3(var0, var1.dir, -(dotVec3(var1.orig, var2) + var2[3]) / var4);
//            return addVec3(var0, var0, var1.orig);
//        }
//    }

    // Jogl 2.5.0 approach uses Vec3f objects
    public static Vec3f seg2SegIntersection(Vec3f var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3, Vert2fImmutable var4) {
        float var5 = (var1.x() - var2.x()) * (var3.y() - var4.y()) - (var1.y() - var2.y()) * (var3.x() - var4.x());
        if (var5 == 0.0F) {
            return null;
        } else {
            float var6 = var1.x() * var2.y() - var1.y() * var2.x();
            float var7 = var3.x() * var4.y() - var3.y() * var4.y();
            float var8 = ((var3.x() - var4.x()) * var6 - (var1.x() - var2.x()) * var7) / var5;
            float var9 = ((var3.y() - var4.y()) * var6 - (var1.y() - var2.y()) * var7) / var5;
            float var10 = (var8 - var1.x()) / (var2.x() - var1.x());
            float var11 = (var8 - var3.x()) / (var4.x() - var3.x());
            if (!(var10 <= 0.0F) && !(var10 >= 1.0F)) {
                return !(var11 <= 0.0F) && !(var11 >= 1.0F) ? var0.set(var8, var9, 0.0F) : null;
            } else {
                return null;
            }
        }
    }

//    @Deprecated // Jogl 2.4.0 approach is float[] based
//    public static float[] seg2SegIntersection(float[] var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3, Vert2fImmutable var4) {
//        float var5 = (var1.getX() - var2.getX()) * (var3.getY() - var4.getY()) - (var1.getY() - var2.getY()) * (var3.getX() - var4.getX());
//        if (var5 == 0.0F) {
//            return null;
//        } else {
//            float var6 = var1.getX() * var2.getY() - var1.getY() * var2.getX();
//            float var7 = var3.getX() * var4.getY() - var3.getY() * var4.getY();
//            float var8 = ((var3.getX() - var4.getX()) * var6 - (var1.getX() - var2.getX()) * var7) / var5;
//            float var9 = ((var3.getY() - var4.getY()) * var6 - (var1.getY() - var2.getY()) * var7) / var5;
//            float var10 = (var8 - var1.getX()) / (var2.getX() - var1.getX());
//            float var11 = (var8 - var3.getX()) / (var4.getX() - var3.getX());
//            if (!(var10 <= 0.0F) && !(var10 >= 1.0F)) {
//                if (!(var11 <= 0.0F) && !(var11 >= 1.0F)) {
//                    var0[0] = var8;
//                    var0[1] = var9;
//                    var0[2] = 0.0F;
//                    return var0;
//                } else {
//                    return null;
//                }
//            } else {
//                return null;
//            }
//        }
//    }
//
//    public static boolean testSeg2SegIntersection(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3) {
//        float[] var4 = var0.getCoord();
//        float[] var5 = var1.getCoord();
//        float[] var6 = var2.getCoord();
//        float[] var7 = var3.getCoord();
//        float var8 = (var4[0] - var5[0]) * (var6[1] - var7[1]) - (var4[1] - var5[1]) * (var6[0] - var7[0]);
//        if (var8 == 0.0F) {
//            return false;
//        } else {
//            float var9 = var4[0] * var5[1] - var4[1] * var5[0];
//            float var10 = var6[0] * var7[1] - var6[1] * var7[1];
//            float var11 = ((var6[0] - var7[0]) * var9 - (var4[0] - var5[0]) * var10) / var8;
//            float var12 = (var11 - var4[0]) / (var5[0] - var4[0]);
//            float var13 = (var11 - var6[0]) / (var7[0] - var6[0]);
//            return !(var12 <= 0.0F) && !(var12 >= 1.0F) && !(var13 <= 0.0F) && !(var13 >= 1.0F);
//        }
//    }

//    public static boolean testSeg2SegIntersection(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3, float var4) {
//        float[] var5 = var0.getCoord();
//        float[] var6 = var1.getCoord();
//        float[] var7 = var2.getCoord();
//        float[] var8 = var3.getCoord();
//        float var9 = (var5[0] - var6[0]) * (var7[1] - var8[1]) - (var5[1] - var6[1]) * (var7[0] - var8[0]);
//        if (FloatUtil.isZero(var9, var4)) {
//            return false;
//        } else {
//            float var10 = var5[0] * var6[1] - var5[1] * var6[0];
//            float var11 = var7[0] * var8[1] - var7[1] * var8[1];
//            float var12 = ((var7[0] - var8[0]) * var10 - (var5[0] - var6[0]) * var11) / var9;
//            float var13 = (var12 - var5[0]) / (var6[0] - var5[0]);
//            float var14 = (var12 - var7[0]) / (var8[0] - var7[0]);
//            if (FloatUtil.compare(var13, 0.0F, var4) > 0 && FloatUtil.compare(var13, 1.0F, var4) < 0 && FloatUtil.compare(var14, 0.0F, var4) > 0 && FloatUtil.compare(var14, 1.0F, var4) < 0) {
//                return !(var13 <= 0.0F) && !(var13 >= 1.0F) && !(var14 <= 0.0F) && !(var14 >= 1.0F);
//            } else {
//                return false;
//            }
//        }
//    }
//
//    public static float[] line2lineIntersection(float[] var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3, Vert2fImmutable var4) {
//        float var5 = (var1.getX() - var2.getX()) * (var3.getY() - var4.getY()) - (var1.getY() - var2.getY()) * (var3.getX() - var4.getX());
//        if (var5 == 0.0F) {
//            return null;
//        } else {
//            float var6 = var1.getX() * var2.getY() - var1.getY() * var2.getX();
//            float var7 = var3.getX() * var4.getY() - var3.getY() * var4.getY();
//            float var8 = ((var3.getX() - var4.getX()) * var6 - (var1.getX() - var2.getX()) * var7) / var5;
//            float var9 = ((var3.getY() - var4.getY()) * var6 - (var1.getY() - var2.getY()) * var7) / var5;
//            var0[0] = var8;
//            var0[1] = var9;
//            var0[2] = 0.0F;
//            return var0;
//        }
//    }

//    public static boolean testTri2SegIntersection(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3, Vert2fImmutable var4) {
//        return testSeg2SegIntersection(var0, var1, var3, var4) || testSeg2SegIntersection(var1, var2, var3, var4) || testSeg2SegIntersection(var0, var2, var3, var4);
//    }

//    public static boolean testTri2SegIntersection(Vert2fImmutable var0, Vert2fImmutable var1, Vert2fImmutable var2, Vert2fImmutable var3, Vert2fImmutable var4, float var5) {
//        return testSeg2SegIntersection(var0, var1, var3, var4, var5) || testSeg2SegIntersection(var1, var2, var3, var4, var5) || testSeg2SegIntersection(var0, var2, var3, var4, var5);
//    }

    public static enum Winding {
        CW(-1),
        CCW(1);

        public final int dir;

        private Winding(int var3) {
            this.dir = var3;
        }
    }
}
