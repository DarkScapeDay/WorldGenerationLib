package WorldGenerationUtils;

import java.util.Random;

public class Maths
{
    private static final Random r = new Random();
    private static final long[] primes = new long[]
    {
        73856093, 19349663, 83492791, 63507287, 33512309, 30313313, 85133231, 23421989, 71358431, 65244451
    };

    public static float getHermite(float value)
    {
        return value*value*(-value*2 + 3);
    }

    public static long getPrime(int n)
    {
        return primes[n % primes.length];
    }

    //    static int fastfloor(float x)
    //    {
    //        int xi = (int)x;
    //        return x < xi ? xi-1 : xi;

//    static<N extends Number & Comparable<N>> float getRandomNumber(long seed, Vector<N> location)
//    {
////        long result = 0x0;
////        int i = 0;
////        while (i < location.dimensions())
////        {
////            long prime = primes[i % primes.length];
////            result ^= Float.floatToRawIntBits(location.getFloat(i)) * prime;
////            i++;
////        }
////        result ^= seed * primes[i++ % primes.length];
////        result ^= location.lengthSquared().longValue() * primes[i++ % primes.length];
//        r.setSeed(seed + location.hashCode()* primes[primes.length - 1]);
//
//
////        long result = 0;
////        final int thing = getTwoToThe(10) - 1;
////
////        for (N value : location)
////        {
////            result = result * thing + value.intValue();
////        }
////        result = result * thing + length;
////        result += seed;
////        r.setSeed(result);
//
//
////        if (seed < 0)
////        {
////            seed = -seed;
////        }
////        seed -= location.lengthSquared().intValue() * 4321;
////        Integer multiplier = 777;
////        for (N value : location)
////        {
////            seed -= value.longValue() * multiplier;
////            multiplier *= 1000;
////        }
////        r.setSeed(seed);
//        return r.nextFloat();
//    }
}
