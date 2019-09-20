package worldgenerationutils.fastnoisegeneration;

public abstract class NoiseFieldND
{
    private static final float MIN_WEIGHT = 0.333333f; //[0, 1]
    private static final float WEIGHT_RANGE = 1f - MIN_WEIGHT; //[0, 1]
    private static final float SIZE_OF_SMALL = 0.25f; //[0, 1]
    private final int smallestPowerOf2;
    private final int largestPowerOf2;

    NoiseFieldND(int smallestPowerOf2, int largestPowerOf2) throws IllegalArgumentException
    {
        this.smallestPowerOf2 = smallestPowerOf2;
        this.largestPowerOf2 = largestPowerOf2;
        if (numberOfInterpolators() <= 0)
        {
            throw new IllegalArgumentException("Error! The bounds [" + smallestPowerOf2 + ", " + largestPowerOf2 + "] for the powers of two are invalid!");
        }
    }

    final int numberOfInterpolators()
    {
        return largestPowerOf2 - smallestPowerOf2 + 1;
    }

    final int getMaxPowerA()
    {
        return smallestPowerOf2 + (int)((largestPowerOf2 - smallestPowerOf2) * SIZE_OF_SMALL);
    }

    final int getMaxPowerB()
    {
        return largestPowerOf2;
    }

    final float getAlpha(float v)
    {
        return v*WEIGHT_RANGE + MIN_WEIGHT;
    }

    static long getWeightSeed(long seed)
    {
        return seed * 30313313;
    }
}
