package com.client;

public class MathUtils {

    public static float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(sq(x2 - x1) + sq(y2 - y1));
    }

    public static final float sq(float n) {
        return n * n;
    }

    public static float smoothstep(float edge0, float edge1, float x) {
        // Scale, bias and saturate x to 0..1 range
        x = constrain((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        // Evaluate polynomial
        return x * x * (3 - 2 * x);
    }

    public static final float constrain(float amt, float low, float high) {
        return amt < low ? low : (amt > high ? high : amt);
    }

    public static float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    public static float norm(float value, float start, float stop) {
        return (value - start) / (stop - start);
    }

    public static int lerpColor(int c1, int c2, float amt) {
        if (amt < 0.0F) {
            amt = 0.0F;
        }

        if (amt > 1.0F) {
            amt = 1.0F;
        }

        float a1;
        float a2;
        float ho;
        float so;
        float bo;

        a1 = (float) (c1 >> 24 & 255);
        a2 = (float) (c1 >> 16 & 255);
        float g1 = (float) (c1 >> 8 & 255);
        ho = (float) (c1 & 255);
        so = (float) (c2 >> 24 & 255);
        bo = (float) (c2 >> 16 & 255);
        float g2 = (float) (c2 >> 8 & 255);
        float b2 = (float) (c2 & 255);
        return Math.round(a1 + (so - a1) * amt) << 24 | Math.round(a2 + (bo - a2) * amt) << 16 | Math
                .round(g1 + (g2 - g1) * amt) << 8 | Math.round(ho + (b2 - ho) * amt);
    }
}
