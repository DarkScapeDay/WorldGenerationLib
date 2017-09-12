package me.happyman;

import java.util.Random;

public class BinaryCubeInterpolator
{
    private float WDN;
    private float WDS;
    private float WUN;
    private float WUS;
    private float EDN;
    private float EDS;
    private float EUN;
    private float EUS;

    private int wX;
    private int eX;
    private int dY;
    private int uY;
    private int nZ;
    private int sZ;

    private final int cubeLength;

    private final long seed;
    private final int lengthMinus1;

    private final byte powerOf2;

    private static final Random r = new Random();

    public BinaryCubeInterpolator(long seed, byte powerOf2, int anyXInCube, int anyYInCube, int anyZInCube)
    {
        this.seed = seed < 0 ? -seed : seed;
        this.powerOf2 = powerOf2;

        int length = 1;
        for (byte i = 0; i < powerOf2; i++)
        {
            length *= 2;
        }
        cubeLength = length;

        lengthMinus1 = cubeLength - 1;

        moveTo(anyXInCube, anyYInCube, anyZInCube);
    }

//                private me.happyman.BinaryCubeInterpolator(me.happyman.BinaryCubeInterpolator referenceCube, DirectionOfNext directionOfNewCube)
//                {
//                    switch (directionOfNewCube)
//                    {
//                        case EAST:
//                            seed = referenceCube.seed;
//                            cubeLength = referenceCube.cubeLength;
//                            lengthMinus1 = referenceCube.lengthMinus1;
//
//                            wX = referenceCube.wX + cubeLength;
//                            dY = referenceCube.dY;
//                            nZ = referenceCube.nZ;
//
//                            WDN = referenceCube.EDN;
//                            WDS = referenceCube.EDS;
//                            WUN = referenceCube.EUN;
//                            WUS = referenceCube.EUS;
//                            EDN = getRandomNumber(wX + cubeLength,        dY             ,        nZ             );
//                            EDS = getRandomNumber(wX + cubeLength,        dY             ,nZ + cubeLength);
//                            EUN = getRandomNumber(wX + cubeLength,dY + cubeLength,        nZ             );
//                            EUS = getRandomNumber(wX + cubeLength,dY + cubeLength,nZ + cubeLength);
//                            break;
//                        case UP:
//                            seed = referenceCube.seed;
//                            cubeLength = referenceCube.cubeLength;
//                            lengthMinus1 = referenceCube.lengthMinus1;
//
//                            wX = referenceCube.wX;
//                            dY = referenceCube.dY + cubeLength;
//                            nZ = referenceCube.nZ;
//
////                    float localWDN = localWUN;
////                    float localWDS = localWUS;
////                    localWUN = getRandomNumber(seed,        chunkX*0x10, chunkY*0x10 + 0x10,        chunkZ*0x10);
////                    localWUS = getRandomNumber(seed,        chunkX*0x10, chunkY*0x10 + 0x10, chunkZ*0x10 + 0x10);
////                    float localEDN = localEUN;
////                    float localEDS = localEUS;
////                    localEUN = getRandomNumber(seed, chunkX*0x10 + 0x10, chunkY*0x10 + 0x10,        chunkZ*0x10);
////                    localEUS = getRandomNumber(seed, chunkX*0x10 + 0x10, chunkY*0x10 + 0x10, chunkZ*0x10 + 0x10);
//                            WDN = referenceCube.WUN;
//                            WDS = referenceCube.WUS;
//                            WUN = getRandomNumber(        wX             ,dY + cubeLength,        nZ             );
//                            WUS = getRandomNumber(        wX             ,dY + cubeLength,nZ + cubeLength);
//                            EDN = referenceCube.EUN;
//                            EDS = referenceCube.EUS;
//                            EUN = getRandomNumber(wX + cubeLength,dY + cubeLength,        nZ             );
//                            EUS = getRandomNumber(wX + cubeLength,dY + cubeLength,nZ + cubeLength);
//                            break;
//                        default: //SOUTH (null throws a NullPointerException)
//                            seed = referenceCube.seed;
//                            cubeLength = referenceCube.cubeLength;
//                            lengthMinus1 = referenceCube.lengthMinus1;
//
//                            wX = referenceCube.wX;
//                            dY = referenceCube.dY;
//                            nZ = referenceCube.nZ + cubeLength;
//
//                            WDN = referenceCube.WDS;
//                            WDS = getRandomNumber(        wX             ,        dY             ,nZ + cubeLength);
//                            WUN = referenceCube.WUS;
//                            WUS = getRandomNumber(        wX             ,dY + cubeLength,nZ + cubeLength);
//                            EDN = referenceCube.EDS;
//                            EDS = getRandomNumber(wX + cubeLength,        dY             ,nZ + cubeLength);
//                            EUN = referenceCube.EUS;
//                            EUS = getRandomNumber(wX + cubeLength,dY + cubeLength,nZ + cubeLength);
//                            break;
//                    }
//                }

//                private void moveTo(DirectionOfNext directionOfNext)
//                {
//                    switch (directionOfNext)
//                    {
//                        case EAST:
//                            wX = eX;
//                            eX += cubeLength;
//
//                            WDN = EDN;
//                            WDS = EDS;
//                            WUN = EUN;
//                            WUS = EUS;
//                            EDN = getRandomNumber(eX, dY, nZ);
//                            EDS = getRandomNumber(eX, dY, sZ);
//                            EUN = getRandomNumber(eX, uY, nZ);
//                            EUS = getRandomNumber(eX, uY, sZ);
//                            break;
//                        case WEST:
//                            eX = wX;
//                            wX -= cubeLength;
//
//                            EDN = WDN;
//                            EDS = WDS;
//                            EUN = WUN;
//                            EUS = WUS;
//                            WUS = getRandomNumber(wX, uY, sZ);
//                            WDN = getRandomNumber(wX, dY, nZ);
//                            WDS = getRandomNumber(wX, dY, sZ);
//                            WUN = getRandomNumber(wX, uY, nZ);
//                            break;
//                        case UP:
//                            dY = uY;
//                            uY += cubeLength;
////                    float localWDN = localWUN;
////                    float localWDS = localWUS;
////                    localWUN = getRandomNumber(seed,        chunkX*0x10, chunkY*0x10 + 0x10,        chunkZ*0x10);
////                    localWUS = getRandomNumber(seed,        chunkX*0x10, chunkY*0x10 + 0x10, chunkZ*0x10 + 0x10);
////                    float localEDN = localEUN;
////                    float localEDS = localEUS;
////                    localEUN = getRandomNumber(seed, chunkX*0x10 + 0x10, chunkY*0x10 + 0x10,        chunkZ*0x10);
////                    localEUS = getRandomNumber(seed, chunkX*0x10 + 0x10, chunkY*0x10 + 0x10, chunkZ*0x10 + 0x10);
//                            WDN = WUN;
//                            WDS = WUS;
//                            EDN = EUN;
//                            EDS = EUS;
//                            WUN = getRandomNumber(wX, uY, nZ);
//                            WUS = getRandomNumber(wX, uY, sZ);
//                            EUN = getRandomNumber(eX, uY, nZ);
//                            EUS = getRandomNumber(eX, uY, sZ);
//                            break;
//                        case DOWN:
//                            uY = dY;
//                            dY -= cubeLength;
//
//                            WUN = WDN;
//                            WUS = WDS;
//                            EUN = EDS;
//                            EUS = EDS;
//                            WDN = getRandomNumber(wX, dY, nZ);
//                            WDS = getRandomNumber(wX, dY, sZ);
//                            EDN = getRandomNumber(eX, dY, nZ);
//                            EDS = getRandomNumber(eX, dY, sZ);
//                            break;
//                        case SOUTH:
//                            nZ = sZ;
//                            sZ += cubeLength;
//
//                            WDN = WDS;
//                            WUN = WUS;
//                            EDN = EDS;
//                            EUN = EUS;
//                            WDS = getRandomNumber(wX, dY, sZ);
//                            WUS = getRandomNumber(wX, uY, sZ);
//                            EDS = getRandomNumber(eX, dY, sZ);
//                            EUS = getRandomNumber(eX, uY, sZ);
//                            break;
//                        case NORTH:
//                            sZ = nZ;
//                            nZ -= cubeLength;
//
//                            WDS = WDN;
//                            WUS = WUN;
//                            EDS = EDN;
//                            EUS = EUN;
//                            WDN = getRandomNumber(wX, dY, nZ);
//                            WUN = getRandomNumber(wX, uY, nZ);
//                            EDN = getRandomNumber(eX, dY, nZ);
//                            EUN = getRandomNumber(eX, uY, nZ);
//                            break;
//                        default:
//                            sendErrorMessage("Error! Major stupidity detected!");
//                            break;
//                    }
//                }

    private float getRandomNumber(int globalX, int globalY, int globalZ)
    {
        //r.setSeed(seed - ((cubeLength << 12) + (globalX << 10) + (globalY << 19) + (globalZ << 25)));
        r.setSeed(seed - (cubeLength*4321 + globalX*777 + globalY*777000 + globalZ*777000000));
        return r.nextFloat();
    }

    private void moveTo(int anyXInCube, int anyYInCube, int anyZInCube)
    {
        wX = anyXInCube & ~lengthMinus1;
        eX = wX + cubeLength;
        dY = anyYInCube & ~lengthMinus1;
        uY = dY + cubeLength;
        nZ = anyZInCube & ~lengthMinus1;
        sZ = nZ + cubeLength;

        WDN = getRandomNumber(wX, dY, nZ);
        WDS = getRandomNumber(wX, dY, sZ);
        WUN = getRandomNumber(wX, uY, nZ);
        WUS = getRandomNumber(wX, uY, sZ);
        EDN = getRandomNumber(eX, dY, nZ);
        EDS = getRandomNumber(eX, dY, sZ);
        EUN = getRandomNumber(eX, uY, nZ);
        EUS = getRandomNumber(eX, uY, sZ);
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

//    private static float getMeleeDamage(float x, int power)
//    {
//        float result = 1f;
//        for (int i = 0; i < power; i++)
//        {
//            result *= x;
//        }
//        return result;
//    }

    public float getDensity(int globalX, int globalY, int globalZ)
    {
        if (globalX >= eX)
        {
            if (globalX - eX < cubeLength)
            {
                wX = eX;
                eX += cubeLength;

                WDN = EDN;
                WDS = EDS;
                WUN = EUN;
                WUS = EUS;
                EDN = getRandomNumber(eX, dY, nZ);
                EDS = getRandomNumber(eX, dY, sZ);
                EUN = getRandomNumber(eX, uY, nZ);
                EUS = getRandomNumber(eX, uY, sZ);
            }
            else moveTo(globalX, globalY, globalZ);
        }
        else if (globalX < wX)
        {
            if (wX - globalX - 1 < cubeLength)
            {
                eX = wX;
                wX -= cubeLength;

                EDN = WDN;
                EDS = WDS;
                EUN = WUN;
                EUS = WUS;
                WUS = getRandomNumber(wX, uY, sZ);
                WDN = getRandomNumber(wX, dY, nZ);
                WDS = getRandomNumber(wX, dY, sZ);
                WUN = getRandomNumber(wX, uY, nZ);
            }
            else moveTo(globalX, globalY, globalZ);
        }
        if (globalY >= uY)
        {
            if (globalY - uY < cubeLength)
            {
                dY = uY;
                uY += cubeLength;

                WDN = WUN;
                WDS = WUS;
                EDN = EUN;
                EDS = EUS;
                WUN = getRandomNumber(wX, uY, nZ);
                WUS = getRandomNumber(wX, uY, sZ);
                EUN = getRandomNumber(eX, uY, nZ);
                EUS = getRandomNumber(eX, uY, sZ);
            }
            else moveTo(globalX, globalY, globalZ);
        }
        else if (globalY < dY)
        {
            if (dY - globalY - 1 < cubeLength)
            {
                uY = dY;
                dY -= cubeLength;

                WUN = WDN;
                WUS = WDS;
                EUN = EDS;
                EUS = EDS;
                WDN = getRandomNumber(wX, dY, nZ);
                WDS = getRandomNumber(wX, dY, sZ);
                EDN = getRandomNumber(eX, dY, nZ);
                EDS = getRandomNumber(eX, dY, sZ);
            }
            else moveTo(globalX, globalY, globalZ);
        }
        if (globalZ >= sZ)
        {
            if (globalZ - sZ < cubeLength)
            {
                nZ = sZ;
                sZ += cubeLength;

                WDN = WDS;
                WUN = WUS;
                EDN = EDS;
                EUN = EUS;
                WDS = getRandomNumber(wX, dY, sZ);
                WUS = getRandomNumber(wX, uY, sZ);
                EDS = getRandomNumber(eX, dY, sZ);
                EUS = getRandomNumber(eX, uY, sZ);
            }
            else moveTo(globalX, globalY, globalZ);
        }
        else if (globalZ < nZ)
        {
            if (nZ - globalZ - 1 < cubeLength)
            {
                sZ = nZ;
                nZ -= cubeLength;

                WDS = WDN;
                WUS = WUN;
                EDS = EDN;
                EUS = EUN;
                WDN = getRandomNumber(wX, dY, nZ);
                WUN = getRandomNumber(wX, uY, nZ);
                EDN = getRandomNumber(eX, dY, nZ);
                EUN = getRandomNumber(eX, uY, nZ);
            }
            else moveTo(globalX, globalY, globalZ);
        }

//        float localEX = getHermite ((float)(globalX & lengthMinus1)/squareLength);
//        float localSZ = getHermite ((float)(globalZ & lengthMinus1)/squareLength);
//        float localWX = 1f - localEX;
//        float localNZ = 1f - localSZ;
//
//        return (WN*localWX*localNZ + WS*localWX*localSZ +
//                EN*localEX*localNZ + ES*localEX*localSZ);


        float localEX = getHermite((float)(globalX & lengthMinus1)/cubeLength);
        float localUY = getHermite((float)(globalY & lengthMinus1)/cubeLength);
        float localSZ = getHermite((float)(globalZ & lengthMinus1)/cubeLength);
        float localWX = 1f - localEX;
        float localDY = 1f - localUY;
        float localNZ = 1f - localSZ;

        return WDN*localWX*localDY*localNZ +
                WDS*localWX*localDY*localSZ +
                WUN*localWX*localUY*localNZ +
                WUS*localWX*localUY*localSZ +
                EDN*localEX*localDY*localNZ +
                EDS*localEX*localDY*localSZ +
                EUN*localEX*localUY*localNZ +
                EUS*localEX*localUY*localSZ;

//        int localEX = globalX & lengthMinus1;
//        int localUY = globalY & lengthMinus1;
//        int localSZ = globalZ & lengthMinus1;
//        int localWX = cubeLength - localEX;
//        int localDY = cubeLength - localUY;
//        int localNZ = cubeLength - localSZ;
//
//        return (WDN*localWX*localDY*localNZ +
//                WDS*localWX*localDY*localSZ +
//                WUN*localWX*localUY*localNZ +
//                WUS*localWX*localUY*localSZ +
//                EDN*localEX*localDY*localNZ +
//                EDS*localEX*localDY*localSZ +
//                EUN*localEX*localUY*localNZ +
//                EUS*localEX*localUY*localSZ)
//                /(cubeLength*cubeLength*cubeLength);
    }

    public static float getDensity(BinaryCubeInterpolator[] interpolators, int globalX, int globalY, int globalZ)
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
            int cubeLength = interpolators[i].cubeLength;
            totalWeight += cubeLength;
            density += interpolators[i].getDensity(globalX, globalY, globalZ) * cubeLength;
        }
        return density/totalWeight;
    }

    public static float getDensity(BinaryCubeInterpolator[] interpolators, int globalX, int globalY, int globalZ, byte maxPower)
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
                int cubeLength = interpolators[i].cubeLength;
                totalWeight += cubeLength;
                density += interpolators[i].getDensity(globalX, globalY, globalZ) * cubeLength;
            }
        }
        return density/totalWeight;
    }

    public static float getDensity(BinaryCubeInterpolator[] interpolators, int globalX, int globalY, int globalZ, float alpha, byte maxPowerA, byte maxPowerB)
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
            int cubeLength = interpolators[i].cubeLength;
            float densityHere = interpolators[i].getDensity(globalX, globalY, globalZ) * cubeLength;
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