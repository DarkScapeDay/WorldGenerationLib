package WorldGenerationUtils.SlowNoiseGeneration;

import WorldGenerationUtils.Maths;
import WorldGenerationUtils.Vectors.Vector;
import WorldGenerationUtils.Vectors.VectorFloat;

import java.util.Random;

public class Interpolator<N extends Number & Comparable<N>>
{
    private static final Random r = new Random();
    private final Vector<N> lowerBound;
    private final Vector<N> upperBound;

    private final long seed;
    private final byte powerOf2;
    private final N length;

    final float[] cornerValues;

    public static boolean lengthPowerOf2IsValid(int powerOf2)
    {
        return powerOf2 >= Byte.MIN_VALUE && powerOf2 <= Byte.MAX_VALUE;
    }

    Interpolator(long seed, int lengthPowerOf2, Vector<N> anyVectorInDomain)
    {
        if (!lengthPowerOf2IsValid(lengthPowerOf2))
        {
            throw new IllegalArgumentException("Error! " + lengthPowerOf2 +
                    " is not a good of a power of 2!");
        }

        this.lowerBound = anyVectorInDomain.zeroVector();
        this.upperBound = anyVectorInDomain.zeroVector();
        this.length = getTwoToThe(lengthPowerOf2);

        this.seed = seed;
        this.powerOf2 = (byte)lengthPowerOf2;

        int corners = 1;
        for (int i = 1; i <= dimensions(); i++)
        {
            corners *= 2;
        }
        this.cornerValues = new float[corners];

        setTo(anyVectorInDomain);
    }

    private N getTwoToThe(int n)
    {
        if (n >= 0)
        {
            int result = 1;
            for (byte i = 1; i <= n; i++)
            {
                result *= 2;
            }
            return lowerBound.cast(result);
        }
        else
        {
            double result = 1;
            for (byte i = -1; i >= n; i--)
            {
                result /= 2;
            }
            return lowerBound.cast(result);
        }
    }

    private boolean pointIsOutside(Vector<N> point)
    {
        checkVector(point);
        for (int i = 0; i < dimensions(); i++)
        {
            N coordinate = point.get(i);
            if (coordinate.compareTo(lowerBound.get(i)) < 0 || coordinate.compareTo(upperBound.get(i)) > 0)
            {
                return true;
            }
        }
        return false;
    }

    private void checkVector(Vector<N> vector)
    {
        if (vector.dimensions() != lowerBound.dimensions())
        {
            throw new IllegalArgumentException("Error! WorldGenerationUtils.Vectors.Vector must have same dimensions!!!");
        }
    }

    private void setTo(Vector<N> newLocation) //@TODO: make this more efficient by reusing previously generated random values
    {
        lowerBound.mutate(newLocation.truncate(length));
        upperBound.mutate(lowerBound.add(length));

        for (int corner = 0; corner < corners(); corner++)
        {
            boolean[] booleans = convertToBooleans(corner);
            Vector<N> temp = lowerBound.copy();
            for (int i = 0; i < dimensions(); i++)
            {
                if (booleans[i])
                {
                    temp.mutate(i, upperBound.get(i));
                }
            }
            cornerValues[corner] = getRandomNumber(seed, temp);
        }
    }

    static<N extends Number & Comparable<N>> float getRandomNumber(long seed, Vector<N> location)
    {
//        long result = 0x0;
//        int i = 0;
//        while (i < location.dimensions())
//        {
//            long prime = primes[i % primes.length];
//            result ^= Float.floatToRawIntBits(location.getFloat(i)) * prime;
//            i++;
//        }
//        result ^= seed * primes[i++ % primes.length];
//        result ^= location.lengthSquared().longValue() * primes[i++ % primes.length];
        r.setSeed(seed + location.hashCode()* 65244451);


//        long result = 0;
//        final int thing = getTwoToThe(10) - 1;
//
//        for (N value : location)
//        {
//            result = result * thing + value.intValue();
//        }
//        result = result * thing + length;
//        result += seed;
//        r.setSeed(result);


//        if (seed < 0)
//        {
//            seed = -seed;
//        }
//        seed -= location.lengthSquared().intValue() * 4321;
//        Integer multiplier = 777;
//        for (N value : location)
//        {
//            seed -= value.longValue() * multiplier;
//            multiplier *= 1000;
//        }
//        r.setSeed(seed);
        return r.nextFloat();
    }

    float getValueAt(Vector<N> point)
    {
        checkVector(point);
        if (pointIsOutside(point))
        {
            setTo(point);
        }

        Vector<Float> alphas = new VectorFloat(dimensions());
        Vector<Float> oneMinusAlphas = new VectorFloat(dimensions());
        for (int i = 0; i < dimensions(); i++)
        {
            float alpha = Maths.getHermite((point.getFloat(i) - lowerBound.getFloat(i)) / length.floatValue());
            alphas.mutate(i, alpha);
            oneMinusAlphas.mutate(i, 1f - alpha);
        }
        //good so far

        float density = 0;
        for (int i = 0; i < corners(); i++)
        {
            boolean[] booleans = convertToBooleans(i);

            float crazyAlpha = 1f;
            for (int j = 0; j < dimensions(); j++)
            {
                crazyAlpha *= booleans[j] ? alphas.get(j) : oneMinusAlphas.get(j);
            }
//            float crazyAlpha = 0f;
//            for (int j = 0; j < dimensions(); j++)
//            {
//                crazyAlpha += booleans[j] ? alphas.get(j) : oneMinusAlphas.get(j);
//            }
//            crazyAlpha /= dimensions();

            density += cornerValues[i] * crazyAlpha;
        }
        return density;
    }

    private int dimensions()
    {
        return lowerBound.dimensions();
    }

    boolean[] convertToBooleans(int value)
    {
        boolean[] result = new boolean[dimensions()];
        for (int i = 0; i < dimensions(); i++, value >>= 1)
        {
            result[i] = (value & 0x1) != 0;
        }
        return result;
    }

    int convertToInt(boolean[] booleans)
    {
        int result = 0x0;
        for (int i = dimensions() - 1; i >= 0; i--)
        {
            result = (result << 1) | (booleans[i] ? 0x1 : 0x0);
        }
        return result;
    }


    private int corners()
    {
        return cornerValues.length;
    }


    private float getCornerValue(boolean ... orthant)
    {
        return getCornerValue(convertToInt(orthant));
    }

    private float getCornerValue(int orthant)
    {
        return cornerValues[orthant];
    }

    private void setCornerValue(float value, boolean ... orthant)
    {
        setCornerValue(value, convertToInt(orthant));
    }

    private void setCornerValue(float value, int index)
    {
        cornerValues[index] = value;
    }

    public N getLength()
    {
        return length;
    }

    public byte getLengthPowerOf2()
    {
        return powerOf2;
    }

    public long getSeed()
    {
        return seed;
    }
}
