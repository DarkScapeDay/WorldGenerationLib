package WorldGenerationUtils.Vectors;

public class VectorInt extends Vector<Integer>
{
    public VectorInt(Integer[] dimensions)
    {
        super(dimensions);
    }

    private static Integer[] toObjects(int ... integers)
    {
        Integer[] objectVersion = new Integer[integers.length];
        for (int i = 0; i < integers.length; i++)
        {
            objectVersion[i] = integers[i];
        }
        return objectVersion;
    }

    public VectorInt(int ... dimensions)
    {
        super(toObjects(dimensions));
    }

    public VectorInt(int dimensions)
    {
        this(new Integer[dimensions]);
        for (int i = 0; i < dimensions; i++)
        {
            mutate(i, zero());
        }
    }

    public static VectorInt zeros(int dimensions)
    {
        return new VectorInt(dimensions);
    }
    
    @Override
    VectorInt copy(Integer[] dimensions)
    {
        return new VectorInt(dimensions);
    }

    @Override
    Integer zero()
    {
        return 0;
    }

    @Override
    Integer sum(Integer augend, Integer addend)
    {
        return augend + addend;
    }

    @Override
    Integer difference(Integer minuend, Integer subtrahend)
    {
        return minuend - subtrahend;
    }

    @Override
    Integer product(Integer multiplicand, Integer multiplier)
    {
        return multiplicand * multiplier;
    }

    @Override
    Integer quotient(Integer dividend, Integer divisor)
    {
        return dividend / divisor;
    }

    @Override
    Integer modulus(Integer dividend, Integer divisor)
    {
        int mod = dividend % divisor;
        return mod < 0 ? mod + divisor : mod;
    }

    @Override
    Integer sqrt(Integer value)
    {
        return (int)Math.round(Math.sqrt(value));
    }

    @Override
    public <N1 extends Number> Integer cast(N1 value)
    {
        return value.intValue();
    }
}
