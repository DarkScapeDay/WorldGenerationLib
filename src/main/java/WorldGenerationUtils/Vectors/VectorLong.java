package WorldGenerationUtils.Vectors;

public class VectorLong extends Vector<Long>
{
    public VectorLong(Long[] dimensions)
    {
        super(dimensions);
    }

    private static Long[] toObjects(long ... longs)
    {
        Long[] objectVersion = new Long[longs.length];
        for (int i = 0; i < longs.length; i++)
        {
            objectVersion[i] = longs[i];
        }
        return objectVersion;
    }

    public VectorLong(long ... dimensions)
    {
        super(toObjects(dimensions));
    }

    public VectorLong(int dimensions)
    {
        this(new Long[dimensions]);
        for (int i = 0; i < dimensions; i++)
        {
            mutate(i, zero());
        }
    }

    public static VectorLong zeros(int dimensions)
    {
        return new VectorLong(dimensions);
    }

    @Override
    VectorLong copy(Long[] dimensions)
    {
        return new VectorLong(dimensions);
    }

    @Override
    Long zero()
    {
        return 0L;
    }

    @Override
    Long sum(Long augend, Long addend)
    {
        return augend + addend;
    }

    @Override
    Long difference(Long minuend, Long subtrahend)
    {
        return minuend - subtrahend;
    }

    @Override
    Long product(Long multiplicand, Long multiplier)
    {
        return multiplicand * multiplier;
    }

    @Override
    Long quotient(Long dividend, Long divisor)
    {
        return dividend / divisor;
    }

    @Override
    Long modulus(Long dividend, Long divisor)
    {
        long mod = dividend % divisor;
        return mod < 0L ? mod + divisor : mod;
    }

    @Override
    public <N1 extends Number> Long cast(N1 value)
    {
        return value.longValue();
    }

    @Override
    Long sqrt(Long value)
    {
        return Math.round(Math.sqrt(value));
    }
}
