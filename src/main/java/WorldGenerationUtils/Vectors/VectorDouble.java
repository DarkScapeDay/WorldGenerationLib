package WorldGenerationUtils.Vectors;

public class VectorDouble extends Vector<Double>
{
    public VectorDouble(Double[] dimensions)
    {
        super(dimensions);
    }

    private static Double[] toObjects(double ... doubles)
    {
        Double[] objectVersion = new Double[doubles.length];
        for (int i = 0; i < doubles.length; i++)
        {
            objectVersion[i] = doubles[i];
        }
        return objectVersion;
    }

    public VectorDouble(double ... dimensions)
    {
        super(toObjects(dimensions));
    }

    public VectorDouble(int dimensions)
    {
        this(new Double[dimensions]);
        for (int i = 0; i < dimensions; i++)
        {
            mutate(i, zero());
        }
    }

    public static VectorDouble zeros(int dimensions)
    {
        return new VectorDouble(dimensions);
    }

    @Override
    VectorDouble copy(Double[] dimensions)
    {
        return new VectorDouble(dimensions);
    }
    
    @Override
    Double zero()
    {
        new VectorDouble(23);
        return 0d;
    }

    @Override
    Double sum(Double augend, Double addend)
    {
        return augend + addend;
    }

    @Override
    Double difference(Double minuend, Double subtrahend)
    {
        return minuend - subtrahend;
    }

    @Override
    Double product(Double multiplicand, Double multiplier)
    {
        return multiplicand * multiplier;
    }

    @Override
    Double quotient(Double dividend, Double divisor)
    {
        return dividend / divisor;
    }

    @Override
    Double modulus(Double dividend, Double divisor)
    {
        double mod = dividend % divisor;
        return mod < 0d ? mod + divisor : mod;
    }

    @Override
    public <N1 extends Number> Double cast(N1 value)
    {
        return value.doubleValue();
    }

    @Override
    Double sqrt(Double value)
    {
        return Math.sqrt(value);
    }
}
