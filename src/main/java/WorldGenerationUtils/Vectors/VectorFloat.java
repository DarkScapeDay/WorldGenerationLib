package WorldGenerationUtils.Vectors;

public class VectorFloat extends Vector<Float>
{
    public VectorFloat(Float[] dimensions)
    {
        super(dimensions);
    }

    private static Float[] toObjects(float ... floats)
    {
        Float[] objectVersion = new Float[floats.length];
        for (int i = 0; i < floats.length; i++)
        {
            objectVersion[i] = floats[i];
        }
        return objectVersion;
    }

    public VectorFloat(float ... dimensions)
    {
        super(toObjects(dimensions));
    }

    public VectorFloat(int dimensions)
    {
        this(new Float[dimensions]);
        for (int i = 0; i < dimensions; i++)
        {
            mutate(i, zero());
        }
    }

    public static VectorFloat zeros(int dimensions)
    {
        return new VectorFloat(dimensions);
    }

    @Override
    VectorFloat copy(Float[] dimensions)
    {
        return new VectorFloat(dimensions);
    }
    
    @Override
    Float zero()
    {
        return 0f;
    }

    @Override
    Float sum(Float augend, Float addend)
    {
        return augend + addend;
    }

    @Override
    Float difference(Float minuend, Float subtrahend)
    {
        return minuend - subtrahend;
    }

    @Override
    Float product(Float multiplicand, Float multiplier)
    {
        return multiplicand * multiplier;
    }

    @Override
    Float quotient(Float dividend, Float divisor)
    {
        return dividend / divisor;
    }

    @Override
    Float modulus(Float dividend, Float divisor)
    {
        float mod = dividend % divisor;
        return mod < 0f ? mod + divisor : mod;
    }

    @Override
    public <N1 extends Number> Float cast(N1 value)
    {
        return value.floatValue();
    }

    @Override
    Float sqrt(Float value)
    {
        return (float)Math.sqrt(value);
    }
}
