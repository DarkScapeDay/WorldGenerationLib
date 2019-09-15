package WorldGenerationUtils.FastNoiseGeneration;

import WorldGenerationUtils.Maths;

public class Binary2DInterpolator extends BinaryNDInterpolator
{
    private float LL;
    private float LH;
    private float HL;
    private float HH;

    private int Lx;
    private int Hx;
    private int Lz;
    private int Hz;

    Binary2DInterpolator(long seed, int lengthPowerOf2)
    {
        super(seed, lengthPowerOf2);
    }

    private float getRandomNumber(int x, int z)
    {
        //r.setSeed(seed - (length*4321 + x*777000 + z*777000000));
        //r.setSeed(seed - ((length << 12) + (x << 9) + (z << 19)));
        r.setSeed(this.seed ^ (73856093L * length) ^ (19349663L * x) ^ (63507287L * z)); //http://www.beosil.com/download/CollisionDetectionHashing_VMV03.pdf
        return r.nextFloat();
    }

    @Override
    void moveToOrigin()
    {
        stupidMoveTo(0, 0);
    }

    private void stupidMoveTo(int x, int z)
    {
        Lx = x & ~lengthMinus1;
        Hx = Lx + length;
        Lz = z & ~lengthMinus1;
        Hz = Lz + length;

        LL = getRandomNumber(Lx, Lz);
        LH = getRandomNumber(Lx, Hz);
        HL = getRandomNumber(Hx, Lz);
        HH = getRandomNumber(Hx, Hz);
    }

    private void smartMoveTo(int x, int z)
    {
        if (x >= Hx)
        {
            if (x - Hx < length)
            {
                Lx = Hx;
                Hx += length;

                LL = HL;
                LH = HH;
                HL = getRandomNumber(Hx, Lz);
                HH = getRandomNumber(Hx, Hz);
            }
            else
            {
                stupidMoveTo(x, z);
                return;
            }
        }
        else if (x < Lx)
        {
            if (x >= Lx - length)
            {
                Hx = Lx;
                Lx -= length;

                HL = LL;
                HH = LH;
                LL = getRandomNumber(Lx, Lz);
                LH = getRandomNumber(Lx, Hz);
            }
            else
            {
                stupidMoveTo(x, z);
                return;
            }
        }

        if (z >= Hz)
        {
            if (z - Hz < length)
            {
                Lz = Hz;
                Hz += length;

                LL = LH;
                HL = HH;
                LH = getRandomNumber(Lx, Hz);
                HH = getRandomNumber(Hx, Hz);
            }
            else
            {
                stupidMoveTo(x, z);
                //return;
            }
        }
        else if (z < Lz)
        {
            if (Lz - z - 1 < length)
            {
                Hz = Lz;
                Lz -= length;

                LH = LL;
                HH = HL;
                LL = getRandomNumber(Lx, Lz);
                HL = getRandomNumber(Hx, Lz);
            }
            else
            {
                stupidMoveTo(x, z);
               // return;
            }
        }
    }

    float valueAt(int x, int z)
    {
        smartMoveTo(x, z);

        float localEX = Maths.getHermite((float)(x & lengthMinus1)/length);
        float localSZ = Maths.getHermite((float)(z & lengthMinus1)/length);
        float localWX = 1f - localEX;
        float localNZ = 1f - localSZ;

        return LL*localWX*localNZ +
                LH*localWX*localSZ +
                HL*localEX*localNZ +
                HH*localEX*localSZ;

//        int localHx = x & lengthMinus1;
//        int localHz = z & lengthMinus1;
//        int localLx = length - localHx;
//        int localLz = length - localHz;
//
//        return (localLx*localLz*LL +
//                localLx*localHz*LH +
//                localHx*localLz*HL +
//                localHx*localHz*HH)
//                /(length * length);
    }
}
