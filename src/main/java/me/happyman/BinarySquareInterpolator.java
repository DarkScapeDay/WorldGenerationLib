package me.happyman;

import java.util.Random;

public class BinarySquareInterpolator
{
    private float WN;
    private float WS;
    private float EN;
    private float ES;

    private int wX;
    private int eX;
    private int nZ;
    private int sZ;

    private final int squareLength;

    private final long seed;
    private final int lengthMinus1;
    private final byte powerOf2;

    private static final Random r = new Random();

    public BinarySquareInterpolator(long seed, byte powerOf2, int anyXInCube, int anyZInCube)
    {
        this.seed = seed < 0 ? -seed : seed;
        this.powerOf2 = powerOf2;

        int length = 1;
        for (byte i = 0; i < powerOf2; i++)
        {
            length *= 2;
        }
        squareLength = length;

        lengthMinus1 = squareLength - 1;

        moveTo(anyXInCube, anyZInCube);
    }

    private float getRandomNumber(int globalX, int globalZ)
    {
        r.setSeed(seed - (squareLength*4321 + globalX*777000 + globalZ*777000000));
        //r.setSeed(seed - ((squareLength << 12) + (globalX << 9) + (globalZ << 19)));
        return r.nextFloat();
    }

    private void moveTo(int anyXInCube, int anyZInCube)
    {
        wX = anyXInCube & ~lengthMinus1;
        eX = wX + squareLength;
        nZ = anyZInCube & ~lengthMinus1;
        sZ = nZ + squareLength;

        WN = getRandomNumber(wX, nZ);
        WS = getRandomNumber(wX, sZ);
        EN = getRandomNumber(eX, nZ);
        ES = getRandomNumber(eX, sZ);
    }

    private static float getHermite(float value)
    {
        return value*value*(-value*2 + 3);
    }

//    private static int fastfloor(float x)
//    {
//        int xi = (int)x;
//        return x < xi ? xi-1 : xi;
//    }

    public float getHeight(int globalX, int globalZ)
    {
        if (globalX >= eX)
        {
            if (globalX - eX < squareLength)
            {
                wX = eX;
                eX += squareLength;

                WN = EN;
                WS = ES;
                EN = getRandomNumber(eX, nZ);
                ES = getRandomNumber(eX, sZ);
            }
            else moveTo(globalX, globalZ);
        }
        else if (globalX < wX)
        {
            if (wX - globalX - 1 < squareLength)
            {
                eX = wX;
                wX -= squareLength;

                EN = WN;
                ES = WS;
                WN = getRandomNumber(wX, nZ);
                WS = getRandomNumber(wX, sZ);
            }
            else moveTo(globalX, globalZ);
        }
        if (globalZ >= sZ)
        {
            if (globalZ - sZ < squareLength)
            {
                nZ = sZ;
                sZ += squareLength;

                WN = WS;
                EN = ES;
                WS = getRandomNumber(wX, sZ);
                ES = getRandomNumber(eX, sZ);
            }
            else moveTo(globalX, globalZ);
        }
        else if (globalZ < nZ)
        {
            if (nZ - globalZ - 1 < squareLength)
            {
                sZ = nZ;
                nZ -= squareLength;

                WS = WN;
                ES = EN;
                WN = getRandomNumber(wX, nZ);
                EN = getRandomNumber(eX, nZ);
            }
            else moveTo(globalX, globalZ);
        }
        float localEX = getHermite((float)(globalX & lengthMinus1)/squareLength);
        float localSZ = getHermite((float)(globalZ & lengthMinus1)/squareLength);
        float localWX = 1f - localEX;
        float localNZ = 1f - localSZ;

        return (WN*localWX*localNZ + WS*localWX*localSZ +
                EN*localEX*localNZ + ES*localEX*localSZ);
    }

    //@param divisionWeightDistributionMod: between 0 and 1, 0 being smaller divisions, 1 being larger
    public static float getHeight(BinarySquareInterpolator[] interpolators, int globalX, int globalZ)
    {
        int length = interpolators.length;

        if (length == 0)
        {
            return 0;
        }

        float density = 0;
        float totalWeight = 0f;
        for (int i = 0; i < length; i++)
        {
            int cubeLength = interpolators[i].squareLength;
            totalWeight += cubeLength;
            density += interpolators[i].getHeight(globalX, globalZ) * cubeLength;
        }
        return density/totalWeight;
//        int length = interpolatorsSortedBySmallestDivisionsFirst.length;
//
//        switch (length)
//        {
//            case 0: return 0;
//            case 1: return interpolatorsSortedBySmallestDivisionsFirst[0].getHeight(globalX, globalZ);
//        }
//        int last = length - 1;
//        int mid = length / 2;
//
//        float density = 0;
//        float leftWeight = 1f-divisionSizeMod;
//        float rightWeight = divisionSizeMod;
//        float incrementSides = (rightWeight - leftWeight)/last;
//        for (int i = 0; i < mid; i++, leftWeight += incrementSides, rightWeight -= incrementSides)
//        {
//            density += (leftWeight * interpolatorsSortedBySmallestDivisionsFirst[i].getHeight(globalX, globalZ)
//                    + rightWeight * interpolatorsSortedBySmallestDivisionsFirst[last - i].getHeight(globalX, globalZ)) * 2;
//        }
//        if (length % 2 == 1)
//        {
//            density += interpolatorsSortedBySmallestDivisionsFirst[mid].getHeight(globalX, globalZ);
//        }
//        return density/length;
    }


    public static float getHeight(BinarySquareInterpolator[] interpolators, int globalX, int globalZ, byte maxPower)
    {
        int length = interpolators.length;

        if (length == 0)
        {
            return 0;
        }

        float density = 0f;
        float totalWeight = 0f;
        for (int i = 0; i < length; i++)
        {
            if (interpolators[i].powerOf2 <= maxPower)
            {
                int cubeLength = interpolators[i].squareLength;
                totalWeight += cubeLength;
                density += interpolators[i].getHeight(globalX, globalZ) * cubeLength;
            }
        }
        return density/totalWeight;
    }

    public static float getHeight(BinarySquareInterpolator[] interpolators, int globalX, int globalZ, float alpha, byte maxPowerA, byte maxPowerB)
    {
        int length = interpolators.length;
        if (length == 0)
        {
            return 0;
        }

        float heightDivisionB = 0f;
        float heightDivisionA = 0f;
        float totalBWeight = 0f;
        float totalAWeight = 0f;
        for (int i = 0; i < length; i++)
        {
            int cubeLength = interpolators[i].squareLength;
            float densityHere = interpolators[i].getHeight(globalX, globalZ) * cubeLength;
            byte powerOf2 = interpolators[i].powerOf2;
            if (powerOf2 <= maxPowerA)
            {
                totalAWeight += cubeLength;
                heightDivisionA += densityHere;
            }
            if (powerOf2 <= maxPowerB)
            {
                totalBWeight += cubeLength;
                heightDivisionB += densityHere;
            }
        }
        return (1f - alpha)*heightDivisionA/totalAWeight + alpha*heightDivisionB/totalBWeight;
    }

    public static BinaryCubeInterpolator[] getInterpolators(long seed, byte[] powers, int globalX, int globalY, int globalZ)
    {
        BinaryCubeInterpolator[] result = new BinaryCubeInterpolator[powers.length];
        for (byte i = 0; i < powers.length; i++)
        {
            result[i] = new BinaryCubeInterpolator(seed, powers[i], globalX, globalY, globalZ);
        }
        return result;
    }

    public static BinaryCubeInterpolator[] getInterpolators(long seed, byte highestPower, int globalX, int globalY, int globalZ)
    {
        if (highestPower > 1)
        {
            BinaryCubeInterpolator[] result = new BinaryCubeInterpolator[highestPower - 1];
            for (byte i = 0, curPower = highestPower; i < result.length; i++, curPower--)
            {
                result[i] = new BinaryCubeInterpolator(seed, curPower, globalX, globalY, globalZ);
            }
            return result;
        }
        return null;
    }
}
