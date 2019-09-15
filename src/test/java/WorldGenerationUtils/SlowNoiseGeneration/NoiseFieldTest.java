package WorldGenerationUtils.SlowNoiseGeneration;

import WorldGenerationUtils.FastNoiseGeneration.NoiseField3D;
import WorldGenerationUtils.Vectors.Vector;
import WorldGenerationUtils.Vectors.VectorInt;
import WorldGenerationUtils.Vectors.VectorLong;
import org.testng.annotations.Test;

import java.util.Random;

import static WorldGenerationUtils.SlowNoiseGeneration.Interpolator.getRandomNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class NoiseFieldTest
{
    @Test
    public void testGenericInterpolator()
    {
        Interpolator<Integer> f = new Interpolator<Integer>(231L, 23, new VectorInt(0, 0, 0, 0, 0));
        assertEquals(f.convertToInt(f.convertToBooleans(21)), 21);
        assertEquals(f.convertToInt(f.convertToBooleans(31)), 31);
        assertEquals(f.convertToInt(f.convertToBooleans(1)), 1);
    }

    @Test
    public void testSingleInterpolator()
    {
        final float stdVariationUniformDist;
        {
            Float prev = null;
            float totalVariation = 0;
            long samples = 0;
            float max = 0f;
            float sum = 0f;
            float min = 1f;

            long seed = System.currentTimeMillis();
            for (long i = 0L; i < 1000000L; i++)
            {
                VectorLong v = new VectorLong(i*7);
                float cur = getRandomNumber(seed, v);
                if (prev != null)
                {
                    sum += cur;
                    if (cur > max)
                    {
                        max = cur;
                    }
                    if (cur < min)
                    {
                        min = cur;
                    }

                    totalVariation += Math.abs(cur - prev);
                    samples++;
                }
                prev = cur;
            }
            stdVariationUniformDist = totalVariation/samples;
            System.out.println("Standard deviation 1: " + stdVariationUniformDist);
            System.out.println("Avg: " + sum/samples);
            System.out.println("Max: " + max);
            System.out.println("Min: " + min);
            System.out.println("-----------------------------------");
        }

        {
            Float prev = null;
            float totalVariation = 0;
            long samples = 0;
            float sum = 0;
            float min = 1f;
            float max = 0f;
            Random r = new Random(System.currentTimeMillis());
            for (long i = 0L; i < 1000000L; i++)
            {
                float cur = r.nextFloat();
                if (prev != null)
                {
                    sum += cur;
                    totalVariation += Math.abs(cur - prev);
                    samples++;
                    if (cur > max)
                    {
                        max = cur;
                    }
                    if (cur < min)
                    {
                        min = cur;
                    }
                }
                prev = cur;
            }
            System.out.println("Standard deviation 2: " + totalVariation/samples);
            System.out.println("Avg: " + sum/samples);
            System.out.println("Max: " + max);
            System.out.println("Min: " + min);
            System.out.println("-----------------------------------");
        }

        {
            Interpolator<Integer> interpolator = new Interpolator<Integer>(12, 4, VectorInt.zeros(1));
            for (float value : interpolator.cornerValues)
            {
                if (value < 0f || value > 1f)
                {
                    fail("corner value was " + value);
                }
            }

            Float prev = null;
            for (int i = -1000; i < 1000; i++)
            {
                float cur = interpolator.getValueAt(new VectorInt(new int[] {i}));
                if (cur > 1f || cur < 0f)
                {
                    fail("value is just bad: " + cur);
                }
                if (prev != null && Math.abs(cur - prev) > 0.1f)
                {
                    fail("too far apart: " + prev + " and " + cur);
                }
                prev = cur;
            }
        }


        {
            Interpolator<Integer> interpolator = new Interpolator<Integer>(12, 4, VectorInt.zeros(2));

            assertEquals(16, (int)interpolator.getLength());
            for (float value : interpolator.cornerValues)
            {
                if (value < 0f || value > 1f)
                {
                    fail("corner value was " + value);
                }
            }

            Float prev = null;
            Float prev2 = null;
            float totalVariation = 0f;
            float totallySeparateVariation = 0f;
            int samples = 0;
            int blocks = 0;
            float max = 0f;
            float min = 1f;
            float sum = 0f;
            for (int i = -1000; i < 1000; i++)
            {
                Vector<Integer> location = new VectorInt(0, i);
                float cur = interpolator.getValueAt(location);
                if (cur > 1f || cur < 0f)
                {
                    fail("value at " + location + " is just bad: " + cur);
                }
                if (cur > max)
                {
                    max = cur;
                }
                if (cur < min)
                {
                    min = cur;
                }
                if (prev != null)
                {
                    float variation = Math.abs(cur - prev);
                    if (variation > 0.1f)
                    {
                        fail("too far apart: " + prev + " and " + cur);
                    }
                    if (variation < 0.000001f)
                    {
                        fail("Not enough variation: " + prev + " and " + cur);
                    }
                    totalVariation += variation;
                    sum += cur;
                    if (blocks % interpolator.getLength() == 0)
                    {
                        if (prev2 != null)
                        {
                            totallySeparateVariation += Math.abs(cur - prev2);
                            samples++;
                        }
                        prev2 = cur;
                    }
                    blocks++;
                }
                prev = cur;
            }
            System.out.println("Max value: " + max);
            System.out.println("Min value: " + min);
            System.out.println("Mean value: " + sum/blocks);
            System.out.println("Standard deviation: " + totalVariation/blocks);
            System.out.println("This value should be close to 1: " + (totallySeparateVariation/samples) / 0.33333);
        }
    }

    @Test
    public void test() throws Exception
    {
        VectorInt vectorInt = new VectorInt(3);
        NoiseField<Integer> noiseField = new NoiseField<Integer>(System.currentTimeMillis(), 2, 6, vectorInt);

        for (int x = 0; x < 0x40; x++)
        {
            for (int y = 0; y < 0x100; y++)
            {
                Float prev = null;
                for (int z = 0; z < 0x40; z++)
                {
//                    Float cur = Binary3DInterpolator.valueAt(interpolators, x, y, z);
                    Float cur = noiseField.getValueAt(new VectorInt(x, y, z));
                    if (cur < 0 || cur > 1f)
                    {
                        fail("Height was out of bounds (" + cur + ")");
                    }

                    if (prev != null && Math.abs(cur - prev) > 1)
                    {
                        fail("You have failed me for the last time, commander. " + prev + " and " + cur);
                    }
                    prev = cur;
                }
            }
        }
    }
}