package WorldGenerationUtils.FastNoiseGeneration;

import WorldGenerationUtils.Maths;

public class Binary3DInterpolator extends BinaryNDInterpolator
{
    private float LLL;
    private float LLH;
    private float LHL;
    private float LHH;
    private float HLL;
    private float HLH;
    private float HHL;
    private float HHH;

    private int Lx;
    private int Hx;
    private int Ly;
    private int Hy;
    private int Lz;
    private int Hz;

    Binary3DInterpolator(long seed, int lengthPowerOf2)
    {
        super(seed, lengthPowerOf2);
    }

    private float getRandomNumber(int x, int y, int z)
    {
        //r.setSeed(seed - (length * 4321 + x*777 + y*777000 + z*777000000));
        //r.setSeed(seed - ((length << 12) + (x << 10) + (y << 19) + (z << 25)));
        r.setSeed(this.seed ^ (73856093L * length) ^ (19349663L * x) ^ (83492791L * y) ^
                  (63507287L *
                   z)); //http://www.beosil.com/download/CollisionDetectionHashing_VMV03.pdf
        return r.nextFloat();
    }

    @Override
    void moveToOrigin()
    {
        stupidMoveTo(0, 0, 0);
    }

    private void stupidMoveTo(int x, int y, int z)
    {
        Lx = x & ~lengthMinus1;
        Hx = Lx + length;
        Ly = y & ~lengthMinus1;
        Hy = Ly + length;
        Lz = z & ~lengthMinus1;
        Hz = Lz + length;

        LLL = getRandomNumber(Lx, Ly, Lz);
        LLH = getRandomNumber(Lx, Ly, Hz);
        LHL = getRandomNumber(Lx, Hy, Lz);
        LHH = getRandomNumber(Lx, Hy, Hz);
        HLL = getRandomNumber(Hx, Ly, Lz);
        HLH = getRandomNumber(Hx, Ly, Hz);
        HHL = getRandomNumber(Hx, Hy, Lz);
        HHH = getRandomNumber(Hx, Hy, Hz);
    }

    private void smartMoveTo(int x, int y, int z)
    {
        if (x >= Hx)
        {
            if (x < Hx + length)
            {
                Lx = Hx;
                Hx += length;

                LLL = HLL;
                LLH = HLH;
                LHL = HHL;
                LHH = HHH;
                HLL = getRandomNumber(Hx, Ly, Lz);
                HLH = getRandomNumber(Hx, Ly, Hz);
                HHL = getRandomNumber(Hx, Hy, Lz);
                HHH = getRandomNumber(Hx, Hy, Hz);
            }
            else
            {
                stupidMoveTo(x, y, z);
                return;
            }
        }
        else if (x < Lx)
        {
            if (x >= Lx - length)
            {
                Hx = Lx;
                Lx -= length;

                HLL = LLL;
                HLH = LLH;
                HHL = LHL;
                HHH = LHH;
                LHH = getRandomNumber(Lx, Hy, Hz);
                LLL = getRandomNumber(Lx, Ly, Lz);
                LLH = getRandomNumber(Lx, Ly, Hz);
                LHL = getRandomNumber(Lx, Hy, Lz);
            }
            else
            {
                stupidMoveTo(x, y, z);
                return;
            }
        }

        if (y >= Hy)
        {
            if (y < Hy + length)
            {
                Ly = Hy;
                Hy += length;

                LLL = LHL;
                LLH = LHH;
                HLL = HHL;
                HLH = HHH;
                LHL = getRandomNumber(Lx, Hy, Lz);
                LHH = getRandomNumber(Lx, Hy, Hz);
                HHL = getRandomNumber(Hx, Hy, Lz);
                HHH = getRandomNumber(Hx, Hy, Hz);
            }
            else
            {
                stupidMoveTo(x, y, z);
                return;
            }
        }
        else if (y < Ly)
        {
            if (y >= Ly - length)
            {
                Hy = Ly;
                Ly -= length;

                LHL = LLL;
                LHH = LLH;
                HHL = HLL;
                HHH = HLH;
                LLL = getRandomNumber(Lx, Ly, Lz);
                LLH = getRandomNumber(Lx, Ly, Hz);
                HLL = getRandomNumber(Hx, Ly, Lz);
                HLH = getRandomNumber(Hx, Ly, Hz);
            }
            else
            {
                stupidMoveTo(x, y, z);
                return;
            }
        }

        if (z >= Hz)
        {
            if (z < Hz + length)
            {
                Lz = Hz;
                Hz += length;

                LLL = LLH;
                LHL = LHH;
                HLL = HLH;
                HHL = HHH;
                LLH = getRandomNumber(Lx, Ly, Hz);
                LHH = getRandomNumber(Lx, Hy, Hz);
                HLH = getRandomNumber(Hx, Ly, Hz);
                HHH = getRandomNumber(Hx, Hy, Hz);
            }
            else
            {
                stupidMoveTo(x, y, z);
                //return;
            }
        }
        else if (z < Lz)
        {
            if (z >= Lz - length)
            {
                Hz = Lz;
                Lz -= length;

                LLH = LLL;
                LHH = LHL;
                HLH = HLL;
                HHH = HHL;
                LLL = getRandomNumber(Lx, Ly, Lz);
                LHL = getRandomNumber(Lx, Hy, Lz);
                HLL = getRandomNumber(Hx, Ly, Lz);
                HHL = getRandomNumber(Hx, Hy, Lz);
            }
            else
            {
                stupidMoveTo(x, y, z);
                //return;
            }
        }
    }

    float valueAt(int x, int y, int z)
    {
        smartMoveTo(x, y, z);

        float localHx = Maths.getHermite((float)(x & lengthMinus1) / length);
        float localHy = Maths.getHermite((float)(y & lengthMinus1) / length);
        float localHz = Maths.getHermite((float)(z & lengthMinus1) / length);
        float localLx = 1f - localHx;
        float localLy = 1f - localHy;
        float localLz = 1f - localHz;

        return LLL * localLx * localLy * localLz + LLH * localLx * localLy * localHz +
               LHL * localLx * localHy * localLz + LHH * localLx * localHy * localHz +
               HLL * localHx * localLy * localLz + HLH * localHx * localLy * localHz +
               HHL * localHx * localHy * localLz + HHH * localHx * localHy * localHz;

//        int localHx = x & lengthMinus1;
//        int localHy = y & lengthMinus1;
//        int localHz = z & lengthMinus1;
//        int localLx = length - localHx;
//        int localLy = length - localHy;
//        int localLz = length - localHz;
//
//        return (localLx*localLy*localLz*LLL +
//                localLx*localLy*localHz*LLH +
//                localLx*localHy*localLz*LHL +
//                localLx*localHy*localHz*LHH +
//                localHx*localLy*localLz*HLL +
//                localHx*localLy*localHz*HLH +
//                localHx*localHy*localLz*HHL +
//                localHx*localHy*localHz*HHH)
//                /(length*length*length);
    }
}