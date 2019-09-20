package worldgenerationutils.fastnoisegeneration;

public class NoiseField2D extends NoiseFieldND
{
    private final Binary2DInterpolator[] interpolators;
    private final Binary2DInterpolator[] weights;

    public NoiseField2D(long seed, int smallestPowerOf2, int largestPowerOf2)
    {
        super(smallestPowerOf2, largestPowerOf2);

        this.interpolators = new Binary2DInterpolator[numberOfInterpolators()];
        this.weights = new Binary2DInterpolator[numberOfInterpolators()];
        final long weightSeed = getWeightSeed(seed);

        for (int lengthPowerOf2 = smallestPowerOf2, i = 0; lengthPowerOf2 <= largestPowerOf2; lengthPowerOf2++, i++)
        {
            this.interpolators[i] = new Binary2DInterpolator(seed, lengthPowerOf2);
            this.weights[i] = new Binary2DInterpolator(weightSeed, lengthPowerOf2);
        }
    }

    private float weightValue(int x, int y)
    {
        float value = 0f;
        int maxValue = 0;
        for (Binary2DInterpolator interpolator : weights)
        {
            value += interpolator.valueAt(x, y) * interpolator.length;
            maxValue += interpolator.length;
        }
        return value/maxValue;
    }

    public float valueAt(int x, int y)
    {
        final float alpha = getAlpha(weightValue(x, y));
        final int maxPowerA = getMaxPowerA();
        final int maxPowerB = getMaxPowerB();

        float valueA = 0f;
        int maxValueA = 0;
        float valueB = 0f;
        int maxValueB = 0;
        for (Binary2DInterpolator interpolator : interpolators)
        {
            final int lengthPowerOf2 = interpolator.lengthPowerOf2;
            final int length = interpolator.length;
            final float value = interpolator.valueAt(x, y) * length;

            if (lengthPowerOf2 <= maxPowerA)
            {
                valueA += value;
                maxValueA += length;
            }
            if (lengthPowerOf2 <= maxPowerB)
            {
                valueB += value;
                maxValueB += length;
            }
        }
        return (1f - alpha)*valueA/maxValueA + alpha*valueB/maxValueB;
    }
}