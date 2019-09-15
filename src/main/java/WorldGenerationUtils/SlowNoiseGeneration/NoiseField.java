package WorldGenerationUtils.SlowNoiseGeneration;

import WorldGenerationUtils.Vectors.Vector;

import java.util.ArrayList;
import java.util.List;

public class NoiseField<N extends Number & Comparable<N>>
{
    private final List<Interpolator<N>> interpolators;
    private final Interpolator<N> weights;
    private final int smallestPowerOf2;
    private final int largestPowerOf2;

    public NoiseField(long seed, int smallestPowerOf2, int largestPowerOf2, Vector<N> anyVectorInDomain) throws IllegalArgumentException
    {
        int numberOfInterpolators = largestPowerOf2 - smallestPowerOf2 + 1;
        if (numberOfInterpolators < 0)
        {
            throw new IllegalArgumentException("Error! The bounds [" + smallestPowerOf2 + ", " + largestPowerOf2 + "] for the powers of two are invalid!");
        }
        this.interpolators = new ArrayList<Interpolator<N>>(numberOfInterpolators);
        this.weights = new Interpolator<N>(seed / 2, largestPowerOf2, anyVectorInDomain);

        for (int lengthPowerOf2 = smallestPowerOf2; lengthPowerOf2 <= largestPowerOf2; lengthPowerOf2++)
        {
            this.interpolators.add(new Interpolator<N>(seed, lengthPowerOf2, anyVectorInDomain));
        }
        this.smallestPowerOf2 = smallestPowerOf2;
        this.largestPowerOf2 = largestPowerOf2;
    }

    public float getValueAt(Vector<N> coordinates)
    {
        final float weight = weights.getValueAt(coordinates);
        return getValueAt(coordinates, weight, smallestPowerOf2 + (largestPowerOf2 - smallestPowerOf2) / 4, largestPowerOf2);
//        float density = 0;
//        float totalWeight = 0f;
//        for (WorldGenerationUtils.SlowNoiseGeneration.Interpolator<N> interpolator : interpolators)
//        {
//            float cubeLength = interpolator.getLength().floatValue();
//            totalWeight += cubeLength;
//            density += interpolator.valueAt(coordinates) * cubeLength;
//        }
//        return density/totalWeight;
    }

//    private float valueAt(WorldGenerationUtils.Vectors.Vector<N> coordinates, byte maxPower)
//    {
//        float density = 0f;
//        float totalWeight = 0f;
//        for (WorldGenerationUtils.SlowNoiseGeneration.Interpolator<N> interpolator : interpolators)
//        {
//            if (interpolator.getLengthPowerOf2() <= maxPower)
//            {
//                float cubeLength = interpolator.getLength().floatValue();
//                totalWeight += cubeLength;
//                density += interpolator.valueAt(coordinates) * cubeLength;
//            }
//        }
//        return density/totalWeight;
//    }

    private float getValueAt(Vector<N> coordinates, float alpha, int maxPowerA, int maxPowerB)
    {
        float densityA = 0f;
        float maxDensityA = 0f;
        float densityB = 0f;
        float maxDensityB = 0f;
        for (Interpolator<N> interpolator : interpolators)
        {
            final float length = interpolator.getLength().floatValue();
            final float density = interpolator.getValueAt(coordinates) * length;
            final byte powerOf2 = interpolator.getLengthPowerOf2();

            if (powerOf2 <= maxPowerA)
            {
                densityA += density;
                maxDensityA += length;
            }
            if (powerOf2 <= maxPowerB)
            {
                densityB += density;
                maxDensityB += length;
            }
        }
        return (1f - alpha)*densityA/maxDensityA + alpha*densityB/maxDensityB;
    }
}
