package WorldGenerationUtils.Vectors;

import WorldGenerationUtils.Maths;

import java.util.Iterator;

public abstract class Vector<N extends Number & Comparable<N>> implements Iterable<N>
{
    private final N[] dimensions;

    public final int dimensions()
    {
        return dimensions.length;
    }

    Vector(N ... dimensions)
    {
        this.dimensions = dimensions.clone();
    }

    public final Vector<Float> toFloatVector()
    {
        Float[] floatVersion = new Float[dimensions()];
        for (int i = 0; i < dimensions(); i++)
        {
            floatVersion[i] = getFloat(i);
        }
        return new VectorFloat(floatVersion);
    }


    public final Vector<Double> toDoubleVector()
    {
        double[] doubleVersion = new double[dimensions()];
        for (int i = 0; i < dimensions(); i++)
        {
            doubleVersion[i] = getDouble(i);
        }
        return new VectorDouble(doubleVersion);
    }



    public final N lengthSquared()
    {
        N result = zero();
        for (N coordinate : this)
        {
            result = sum(result, product(coordinate, coordinate));
        }
        return result;
    }

    public final double length()
    {
        return sqrt(lengthSquared()).doubleValue();
    }

    abstract N sqrt(N value);



    public final Vector<N> copy()
    {
        return copy(dimensions);
    }

    abstract Vector<N> copy(N[] dimensions);



    public final Vector<N> zeroVector()
    {
        N[] zeros = dimensions.clone();
        for (int i = 0; i < dimensions(); i++)
        {
            zeros[i] = zero();
        }
        return copy(zeros);
    }

    abstract N zero();



    public final<N1 extends Number & Comparable<N1>> Vector<N> add(N1 addend)
    {
        N[] newDims = dimensions.clone();
        N converted = cast(addend);
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = sum(get(i), converted);
        }
        return copy(newDims);
    }

    public final<N1 extends Number & Comparable<N1>> Vector<N> add(Vector<N1> addend)
    {
        checkAgainstVector(addend);
        N[] newDims = dimensions.clone();
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = sum(get(i), cast(addend.get(i)));
        }
        return copy(newDims);
    }

    abstract N sum(N augend, N addend);



    public final<N1 extends Number & Comparable<N1>> Vector<N> subtract(N1 subtrahend)
    {
        N[] newDims = dimensions.clone();
        N converted = cast(subtrahend);
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = difference(get(i), converted);
        }
        return copy(newDims);
    }

    public final<N1 extends Number & Comparable<N1>> Vector<N> subtract(Vector<N1> subtrahend)
    {
        checkAgainstVector(subtrahend);
        N[] newDims = dimensions.clone();
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = difference(get(i), cast(subtrahend.get(i)));
        }
        return copy(newDims);
    }

    abstract N difference(N minuend, N subtrahend);



    public final<N1 extends Number & Comparable<N1>> Vector<N> multiply(N1 multiplier)
    {
        N[] newDims = dimensions.clone();
        N converted = cast(multiplier);
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = product(get(i), converted);
        }
        return copy(newDims);
    }

    public final<N1 extends Number & Comparable<N1>> Vector<N> multiply(Vector<N1> multiplier)
    {
        checkAgainstVector(multiplier);
        N[] newDims = dimensions.clone();
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = product(get(i), cast(multiplier.get(i)));
        }
        return copy(newDims);
    }

    abstract N product(N multiplicand, N multiplier);



    public final<N1 extends Number & Comparable<N1>> Vector<N> divide(N1 divisor)
    {
        N[] newDims = dimensions.clone();
        N converted = cast(divisor);
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = quotient(get(i), converted);
        }
        return copy(newDims);
    }

    public final<N1 extends Number & Comparable<N1>> Vector<N> divide(Vector<N1> divisor)
    {
        checkAgainstVector(divisor);
        N[] newDims = dimensions.clone();
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = quotient(get(i), cast(divisor.get(i)));
        }
        return copy(newDims);
    }

    abstract N quotient(N dividend, N divisor);



    public final<N1 extends Number & Comparable<N1>> Vector<N> remainder(N1 divisor)
    {
        N[] newDims = dimensions.clone();
        N converted = cast(divisor);
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = modulus(get(i), converted);
        }
        return copy(newDims);
    }

    abstract N modulus(N dividend, N divisor);



    public final<N1 extends Number & Comparable<N1>> Vector<N> truncate(N1 tongueInCheekResolution)
    {
        N[] newDims = dimensions.clone();
        N converted = cast(tongueInCheekResolution);
        for (int i = 0; i < newDims.length; i++)
        {
            newDims[i] = truncate(get(i), converted);
        }
        return copy(newDims);
    }

    private final N truncate(N value, N tongueInCheekResolution)
    {
        return difference(value, modulus(value, tongueInCheekResolution));
    }



    public final N get(int dimensionIndex)
    {
        return dimensions[dimensionIndex];
    }

    public final int getInt(int dimensionIndex)
    {
        return dimensions[dimensionIndex].intValue();
    }

    public final float getFloat(int dimensionIndex)
    {
        return dimensions[dimensionIndex].floatValue();
    }

    public final double getDouble(int dimensionIndex)
    {
        return dimensions[dimensionIndex].doubleValue();
    }

    public final long getLong(int dimensionIndex)
    {
        return dimensions[dimensionIndex].longValue();
    }

    public final short getShort(int dimensionIndex)
    {
        return dimensions[dimensionIndex].shortValue();
    }

    public final byte getByte(int dimensionIndex)
    {
        return dimensions[dimensionIndex].byteValue();
    }


    public abstract<N1 extends Number> N cast(N1 value);

    public final<N1 extends Number & Comparable<N1>> void mutate(int dimensionIndex, N1 value)
    {
        dimensions[dimensionIndex] = cast(value);
        //        N[] copy = dimensions.clone();
        //        copy[dimensionIndex] = value;
        //        for (int i = 0; i < length(); i++)
        //        {
        //            if (i != dimensionIndex)
        //            {
        //                copy[i] = dimensions[i];
        //            }
        //        }
        //        return copy(copy);
    }

    public final<N1 extends Number & Comparable<N1>> void mutate(Vector<N1> other)
    {
        checkAgainstVector(other);
        for (int i = 0; i < dimensions(); i++)
        {
            dimensions[i] = cast(other.get(i));
        }
    }



    private void checkAgainstVector(Vector other)
    {
        if (other == null)
        {
            throw new NullPointerException("Error! Cannot operate upon a null vector!");
        }
        if (other.dimensions() != dimensions())
        {
            throw new ArrayIndexOutOfBoundsException("Error! The dimension of vector " + other + " did not match this dimension!");
        }
    }


    public final N getX()
    {
        return get(0);
    }

    public final<N1 extends Number & Comparable<N1>> void mutateX(N1 value)
    {
        mutate(0, value);
    }

    public final N getY()
    {
        return get(1);
    }

    public final<N1 extends Number & Comparable<N1>> void mutateY(N1 value)
    {
        mutate(1, value);
    }

    public final N getZ()
    {
        return get(2);
    }

    public final<N1 extends Number & Comparable<N1>> void mutateZ(N1 value)
    {
        mutate(2, value);
    }


    @Override
    public final Iterator<N> iterator()
    {
        return new Iterator<N>()
        {
            private int i = 0;

            @Override
            public void remove()
            {
                throw new IllegalStateException("Error! I didn't realize there was a remove.");
            }

            @Override
            public boolean hasNext()
            {
                return i < dimensions();
            }

            @Override
            public N next()
            {
                return get(i++);
            }
        };
    }

    @Override
    public int hashCode()
    {
        int result = 0x0;
        int i = 0;
        while (i < dimensions())
        {
            result ^= Float.floatToRawIntBits(getFloat(i)) * Maths.getPrime(i);
            i++;
        }
        result ^= lengthSquared().intValue() * Maths.getPrime(i++);
        return result;
    }

    @Override
    public final boolean equals(Object other)
    {
        if (other instanceof Vector)
        {
            Vector otherVector = (Vector)other;
            if (otherVector.dimensions() != dimensions())
            {
                return false;
            }
            for (int i = 0; i < dimensions(); i++)
            {
                if (!dimensions[i].equals(otherVector.dimensions[i]))
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public final String toString()
    {
        if (dimensions() == 0)
        {
            return "Empty";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(dimensions[0]);
        for (int i = 1; i < dimensions(); i++)
        {
            builder.append(", ");
            builder.append(dimensions[i]);
        }
        builder.append(")");
        return builder.toString();
    }
}