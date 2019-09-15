package WorldGenerationUtils.FastNoiseGeneration;

import WorldGenerationUtils.Maths;

public class Binary4DInterpolator extends BinaryNDInterpolator
{
    private float LLLL;
    private float LLLH;
    private float LLHL;
    private float LLHH;
    private float LHLL;
    private float LHLH;
    private float LHHL;
    private float LHHH;
    private float HLLL;
    private float HLLH;
    private float HLHL;
    private float HLHH;
    private float HHLL;
    private float HHLH;
    private float HHHL;
    private float HHHH;

    private int Lx;
    private int Hx;
    private int Ly;
    private int Hy;
    private int Lz;
    private int Hz;
    private int Lt;
    private int Ht;

    Binary4DInterpolator(long seed, int lengthPowerOf2)
    {
        super(seed, lengthPowerOf2);
    }

    private float getRandomNumber(int x, int y, int z, int t)
    {
        //r.setSeed(seed - (length *4321 + x*777 + y*777000 + z*777000000 + t*7230450));
        //r.setSeed(seed - ((length << 12) + (x << 10) + (y << 19) + (z << 25) + (t << 31));
        r.setSeed(this.seed ^ (73856093L * length) ^ (19349663L * x) ^ (83492791L * y) ^ (63507287L * z) ^ (33512309L * t)); //http://www.beosil.com/download/CollisionDetectionHashing_VMV03.pdf
        return r.nextFloat();
    }

    @Override
    void moveToOrigin()
    {
        stupidMoveTo(0, 0,0, 0);
    }

    private void stupidMoveTo(int x, int y, int z, int t)
    {
        Lx = x & ~lengthMinus1;
        Hx = Lx + length;
        Ly = y & ~lengthMinus1;
        Hy = Ly + length;
        Lz = z & ~lengthMinus1;
        Hz = Lz + length;
        Lt = t & ~lengthMinus1;
        Ht = Lt + length;

        LLLL = getRandomNumber(Lx, Ly, Lz, Lt);
        LLLH = getRandomNumber(Lx, Ly, Lz, Ht);
        LLHL = getRandomNumber(Lx, Ly, Hz, Lt);
        LLHH = getRandomNumber(Lx, Ly, Hz, Ht);
        LHLL = getRandomNumber(Lx, Hy, Lz, Lt);
        LHLH = getRandomNumber(Lx, Hy, Lz, Ht);
        LHHL = getRandomNumber(Lx, Hy, Hz, Lt);
        LHHH = getRandomNumber(Lx, Hy, Hz, Ht);
        HLLL = getRandomNumber(Hx, Ly, Lz, Lt);
        HLLH = getRandomNumber(Hx, Ly, Lz, Ht);
        HLHL = getRandomNumber(Hx, Ly, Hz, Lt);
        HLHH = getRandomNumber(Hx, Ly, Hz, Ht);
        HHLL = getRandomNumber(Hx, Hy, Lz, Lt);
        HHLH = getRandomNumber(Hx, Hy, Lz, Ht);
        HHHL = getRandomNumber(Hx, Hy, Hz, Lt);
        HHHH = getRandomNumber(Hx, Hy, Hz, Ht);
    }

    private void smartMoveTo(int x, int y, int z, int t)
    {
        if (x >= Hx)
        {
            if (x < Hx + length)
            {
                Lx = Hx;
                Hx += length;

                LLLL = HLLL;
                LLLH = HLLH;
                LLHL = HLHL;
                LLHH = HLHH;
                LHLL = HHLL;
                LHLH = HHLH;
                LHHL = HHHL;
                LHHH = HHHH;
                HLLL = getRandomNumber(Hx, Ly, Lz, Lt);
                HLLH = getRandomNumber(Hx, Ly, Lz, Ht);
                HLHL = getRandomNumber(Hx, Ly, Hz, Lt);
                HLHH = getRandomNumber(Hx, Ly, Hz, Ht);
                HHLL = getRandomNumber(Hx, Hy, Lz, Lt);
                HHLH = getRandomNumber(Hx, Hy, Lz, Ht);
                HHHL = getRandomNumber(Hx, Hy, Hz, Lt);
                HHHH = getRandomNumber(Hx, Hy, Hz, Ht);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                return;
            }
        }
        else if (x < Lx)
        {
            if (x >= Lx - length)
            {
                Hx = Lx;
                Lx -= length;

                HLLL = LLLL;
                HLLH = LLLH;
                HLHL = LLHL;
                HLHH = LLHH;
                HHLL = LHLL;
                HHLH = LHLH;
                HHHL = LHHL;
                HHHH = LHHH;
                LLLL = getRandomNumber(Lx, Ly, Lz, Lt);
                LLLH = getRandomNumber(Lx, Ly, Lz, Ht);
                LLHL = getRandomNumber(Lx, Ly, Hz, Lt);
                LLHH = getRandomNumber(Lx, Ly, Hz, Ht);
                LHLL = getRandomNumber(Lx, Hy, Lz, Lt);
                LHLH = getRandomNumber(Lx, Hy, Lz, Ht);
                LHHL = getRandomNumber(Lx, Hy, Hz, Lt);
                LHHH = getRandomNumber(Lx, Hy, Hz, Ht);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                return;
            }
        }

        if (y >= Hy)
        {
            if (y < Hy + length)
            {
                Ly = Hy;
                Hy += length;

                LLLL = LHLL;
                LLLH = LHLH;
                LLHL = LHHL;
                LLHH = LHHH;
                HLLL = HHLL;
                HLLH = HHLH;
                HLHL = HHHL;
                HLHH = HHHH;
                LHLL = getRandomNumber(Lx, Hy, Lz, Lt);
                LHLH = getRandomNumber(Lx, Hy, Lz, Ht);
                LHHL = getRandomNumber(Lx, Hy, Hz, Lt);
                LHHH = getRandomNumber(Lx, Hy, Hz, Ht);
                HHLL = getRandomNumber(Hx, Hy, Lz, Lt);
                HHLH = getRandomNumber(Hx, Hy, Lz, Ht);
                HHHL = getRandomNumber(Hx, Hy, Hz, Lt);
                HHHH = getRandomNumber(Hx, Hy, Hz, Ht);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                return;
            }
        }
        else if (y < Ly)
        {
            if (y >= Ly - length)
            {
                Hy = Ly;
                Ly -= length;

                LHLL = LLLL;
                LHLH = LLLH;
                LHHL = LLHL;
                LHHH = LLHH;
                HHLL = HLLL;
                HHLH = HLLH;
                HHHL = HLHL;
                HHHH = HLHH;
                LLLL = getRandomNumber(Lx, Ly, Lz, Lt);
                LLLH = getRandomNumber(Lx, Ly, Lz, Ht);
                LLHL = getRandomNumber(Lx, Ly, Hz, Lt);
                LLHH = getRandomNumber(Lx, Ly, Hz, Ht);
                HLLL = getRandomNumber(Hx, Ly, Lz, Lt);
                HLLH = getRandomNumber(Hx, Ly, Lz, Ht);
                HLHL = getRandomNumber(Hx, Ly, Hz, Lt);
                HLHH = getRandomNumber(Hx, Ly, Hz, Ht);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                return;
            }
        }

        if (z >= Hz)
        {
            if (z < Hz + length)
            {
                Lz = Hz;
                Hz += length;

                LLLL = LLHL;
                LLLH = LLHH;
                LHLL = LHHL;
                LHLH = LHHH;
                HLLL = HLHL;
                HLLH = HLHH;
                HHLL = HHHL;
                HHLH = HHHH;
                LLHL = getRandomNumber(Lx, Ly, Hz, Lt);
                LLHH = getRandomNumber(Lx, Ly, Hz, Ht);
                LHHL = getRandomNumber(Lx, Hy, Hz, Lt);
                LHHH = getRandomNumber(Lx, Hy, Hz, Ht);
                HLHL = getRandomNumber(Hx, Ly, Hz, Lt);
                HLHH = getRandomNumber(Hx, Ly, Hz, Ht);
                HHHL = getRandomNumber(Hx, Hy, Hz, Lt);
                HHHH = getRandomNumber(Hx, Hy, Hz, Ht);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                return;
            }
        }
        else if (z < Lz)
        {
            if (z >= Lz - length)
            {
                Hz = Lz;
                Lz -= length;

                LLHL = LLLL;
                LLHH = LLLH;
                LHHL = LHLL;
                LHHH = LHLH;
                HLHL = HLLL;
                HLHH = HLLH;
                HHHL = HHLL;
                HHHH = HHLH;
                LLLL = getRandomNumber(Lx, Ly, Lz, Lt);
                LLLH = getRandomNumber(Lx, Ly, Lz, Ht);
                LHLL = getRandomNumber(Lx, Hy, Lz, Lt);
                LHLH = getRandomNumber(Lx, Hy, Lz, Ht);
                HLLL = getRandomNumber(Hx, Ly, Lz, Lt);
                HLLH = getRandomNumber(Hx, Ly, Lz, Ht);
                HHLL = getRandomNumber(Hx, Hy, Lz, Lt);
                HHLH = getRandomNumber(Hx, Hy, Lz, Ht);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                return;
            }
        }

        if (t >= Ht)
        {
            if (t < Ht + length)
            {
                Lt = Ht;
                Ht += length;

                LLLL = LLLH;
                LLHL = LLHH;
                LHLL = LHLH;
                LHHL = LHHH;
                HLLL = HLLH;
                HLHL = HLHH;
                HHLL = HHLH;
                HHHL = HHHH;
                LLLH = getRandomNumber(Lx, Ly, Lz, Ht);
                LLHH = getRandomNumber(Lx, Ly, Hz, Ht);
                LHLH = getRandomNumber(Lx, Hy, Lz, Ht);
                LHHH = getRandomNumber(Lx, Hy, Hz, Ht);
                HLLH = getRandomNumber(Hx, Ly, Lz, Ht);
                HLHH = getRandomNumber(Hx, Ly, Hz, Ht);
                HHLH = getRandomNumber(Hx, Hy, Lz, Ht);
                HHHH = getRandomNumber(Hx, Hy, Hz, Ht);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                //return;
            }
        }
        else if (t < Lt)
        {
            if (t >= Lt - length)
            {
                Ht = Lt;
                Lt -= length;

                LLLH = LLLL;
                LLHH = LLHL;
                LHLH = LHLL;
                LHHH = LHHL;
                HLLH = HLLL;
                HLHH = HLHL;
                HHLH = HHLL;
                HHHH = HHHL;
                LLLL = getRandomNumber(Lx, Ly, Lz, Lt);
                LLHL = getRandomNumber(Lx, Ly, Hz, Lt);
                LHLL = getRandomNumber(Lx, Hy, Lz, Lt);
                LHHL = getRandomNumber(Lx, Hy, Hz, Lt);
                HLLL = getRandomNumber(Hx, Ly, Lz, Lt);
                HLHL = getRandomNumber(Hx, Ly, Hz, Lt);
                HHLL = getRandomNumber(Hx, Hy, Lz, Lt);
                HHHL = getRandomNumber(Hx, Hy, Hz, Lt);
            }
            else
            {
                stupidMoveTo(x, y, z, t);
                //return;
            }
        }
    }

    public float valueAt(int x, int y, int z, int t)
    {
        smartMoveTo(x, y, z, t);

        final float localHx = Maths.getHermite((float)(x & lengthMinus1)/length);
        final float localHy = Maths.getHermite((float)(y & lengthMinus1)/length);
        final float localHz = Maths.getHermite((float)(z & lengthMinus1)/length);
        final float localHt = Maths.getHermite((float)(t & lengthMinus1)/length);
        final float localLx = 1f - localHx;
        final float localLy = 1f - localHy;
        final float localLz = 1f - localHz;
        final float localLt = 1f - localHt;

        return LLLL*localLx*localLy*localLz*localLt +
                LLLH*localLx*localLy*localLz*localHt +
                LLHL*localLx*localLy*localHz*localLt +
                LLHH*localLx*localLy*localHz*localHt +
                LHLL*localLx*localHy*localLz*localLt +
                LHLH*localLx*localHy*localLz*localHt +
                LHHL*localLx*localHy*localHz*localLt +
                LHHH*localLx*localHy*localHz*localHt +
                HLLL*localHx*localLy*localLz*localLt +
                HLLH*localHx*localLy*localLz*localHt +
                HLHL*localHx*localLy*localHz*localLt +
                HLHH*localHx*localLy*localHz*localHt +
                HHLL*localHx*localHy*localLz*localLt +
                HHLH*localHx*localHy*localLz*localHt +
                HHHL*localHx*localHy*localHz*localLt +
                HHHH*localHx*localHy*localHz*localHt;

//        final int localHx = x & lengthMinus1;
//        final int localHy = y & lengthMinus1;
//        final int localHz = z & lengthMinus1;
//        final int localHt = t & lengthMinus1;
//
//        final int localLx = length - localHx;
//        final int localLy = length - localHy;
//        final int localLz = length - localHz;
//        final int localLt = length - localHt;
//
//        return (LLLL*localLx*localLy*localLz*localLt +
//                LLLH*localLx*localLy*localLz*localHt +
//                LLHL*localLx*localLy*localHz*localLt +
//                LLHH*localLx*localLy*localHz*localHt +
//                LHLL*localLx*localHy*localLz*localLt +
//                LHLH*localLx*localHy*localLz*localHt +
//                LHHL*localLx*localHy*localHz*localLt +
//                LHHH*localLx*localHy*localHz*localHt +
//                HLLL*localHx*localLy*localLz*localLt +
//                HLLH*localHx*localLy*localLz*localHt +
//                HLHL*localHx*localLy*localHz*localLt +
//                HLHH*localHx*localLy*localHz*localHt +
//                HHLL*localHx*localHy*localLz*localLt +
//                HHLH*localHx*localHy*localLz*localHt +
//                HHHL*localHx*localHy*localHz*localLt +
//                HHHH*localHx*localHy*localHz*localHt)
//                /(length*length*length*length);
    }
}
