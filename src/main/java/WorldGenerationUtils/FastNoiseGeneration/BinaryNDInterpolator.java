package WorldGenerationUtils.FastNoiseGeneration;

import java.util.Random;

public abstract class BinaryNDInterpolator
{
    static final Random r = new Random();
    final long seed;
    final int lengthPowerOf2;
    final int length;
    final int lengthMinus1;

    BinaryNDInterpolator(long seed, int lengthPowerOf2)
    {
        if (lengthPowerOf2 < 0 || lengthPowerOf2 > Byte.MAX_VALUE)
        {
            throw new IllegalArgumentException("Error! Invalid power of 2: " + lengthPowerOf2);
        }
        this.seed = seed;
        this.lengthPowerOf2 = lengthPowerOf2;

        int length = 1;
        for (int i = 1; i <= lengthPowerOf2; i++)
        {
            length *= 2;
        }
        this.length = length;
        this.lengthMinus1 = length - 1;

        moveToOrigin();
    }

    abstract void moveToOrigin();
}
