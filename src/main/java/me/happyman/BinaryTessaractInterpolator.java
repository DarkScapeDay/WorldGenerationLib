package me.happyman;

import java.util.Random;

public class BinaryTessaractInterpolator
{
    private float WDNB;
    private float WDNE;
    private float WDSB;
    private float WDSE;
    private float WUNB;
    private float WUNE;
    private float WUSB;
    private float WUSE;
    private float EDNB;
    private float EDNE;
    private float EDSB;
    private float EDSE;
    private float EUNB;
    private float EUNE;
    private float EUSB;
    private float EUSE;

    private int wX;
    private int eX;
    private int dY;
    private int uY;
    private int nZ;
    private int sZ;
    private int bT;
    private int eT;

    private final int tessaractLength;

    private final long seed;
    private final int lengthMinus1;

    private static final Random r = new Random();

    public BinaryTessaractInterpolator(long seed, byte powerOf2, int anyXInCube, int anyYInCube, int anyZInCube, int anyTInCube)
    {
        this.seed = seed < 0 ? -seed : seed;

        int length = 1;
        for (byte i = 0; i < powerOf2; i++)
        {
            length *= 2;
        }
        tessaractLength = length;

        lengthMinus1 = tessaractLength - 1;

        moveTo(anyXInCube, anyYInCube, anyZInCube, anyTInCube);
    }
//                private me.happyman.BinaryCubeInterpolator(me.happyman.BinaryCubeInterpolator referenceCube, DirectionOfNext directionOfNewCube)
//                {
//                    switch (directionOfNewCube)
//                    {
//                        case EAST:
//                            seed = referenceCube.seed;
//                            tessaractLength = referenceCube.tessaractLength;
//                            lengthMinus1 = referenceCube.lengthMinus1;
//
//                            wX = referenceCube.wX + tessaractLength;
//                            dY = referenceCube.dY;
//                            nZ = referenceCube.nZ;
//
//                            WDN = referenceCube.EDN;
//                            WDS = referenceCube.EDS;
//                            WUN = referenceCube.EUN;
//                            WUS = referenceCube.EUS;
//                            EDN = getRandomNumber(wX + tessaractLength,        dY             ,        nZ             );
//                            EDS = getRandomNumber(wX + tessaractLength,        dY             ,nZ + tessaractLength);
//                            EUN = getRandomNumber(wX + tessaractLength,dY + tessaractLength,        nZ             );
//                            EUS = getRandomNumber(wX + tessaractLength,dY + tessaractLength,nZ + tessaractLength);
//                            break;
//                        case UP:
//                            seed = referenceCube.seed;
//                            tessaractLength = referenceCube.tessaractLength;
//                            lengthMinus1 = referenceCube.lengthMinus1;
//
//                            wX = referenceCube.wX;
//                            dY = referenceCube.dY + tessaractLength;
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
//                            WUN = getRandomNumber(        wX             ,dY + tessaractLength,        nZ             );
//                            WUS = getRandomNumber(        wX             ,dY + tessaractLength,nZ + tessaractLength);
//                            EDN = referenceCube.EUN;
//                            EDS = referenceCube.EUS;
//                            EUN = getRandomNumber(wX + tessaractLength,dY + tessaractLength,        nZ             );
//                            EUS = getRandomNumber(wX + tessaractLength,dY + tessaractLength,nZ + tessaractLength);
//                            break;
//                        default: //SOUTH (null throws a NullPointerException)
//                            seed = referenceCube.seed;
//                            tessaractLength = referenceCube.tessaractLength;
//                            lengthMinus1 = referenceCube.lengthMinus1;
//
//                            wX = referenceCube.wX;
//                            dY = referenceCube.dY;
//                            nZ = referenceCube.nZ + tessaractLength;
//
//                            WDN = referenceCube.WDS;
//                            WDS = getRandomNumber(        wX             ,        dY             ,nZ + tessaractLength);
//                            WUN = referenceCube.WUS;
//                            WUS = getRandomNumber(        wX             ,dY + tessaractLength,nZ + tessaractLength);
//                            EDN = referenceCube.EDS;
//                            EDS = getRandomNumber(wX + tessaractLength,        dY             ,nZ + tessaractLength);
//                            EUN = referenceCube.EUS;
//                            EUS = getRandomNumber(wX + tessaractLength,dY + tessaractLength,nZ + tessaractLength);
//                            break;
//                    }
//                }

//                private void moveTo(DirectionOfNext directionOfNext)
//                {
//                    switch (directionOfNext)
//                    {
//                        case EAST:
//                            wX = eX;
//                            eX += tessaractLength;
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
//                            wX -= tessaractLength;
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
//                            uY += tessaractLength;
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
//                            dY -= tessaractLength;
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
//                            sZ += tessaractLength;
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
//                            nZ -= tessaractLength;
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

    private float getRandomNumber(int globalX, int globalY, int globalZ, int globalT)
    {
        r.setSeed(seed - (tessaractLength *4321 + globalX*777 + globalY*777000 + globalZ*777000000 + globalT*7230450));
        return r.nextFloat();
    }

    public void moveTo(int anyXInCube, int anyYInCube, int anyZInCube, int anyTInCube)
    {
        wX = anyXInCube & ~lengthMinus1;
        eX = wX + tessaractLength;
        dY = anyYInCube & ~lengthMinus1;
        uY = dY + tessaractLength;
        nZ = anyZInCube & ~lengthMinus1;
        sZ = nZ + tessaractLength;
        bT = anyTInCube & ~lengthMinus1;
        eT = bT + tessaractLength;

        WDNB = getRandomNumber(wX, dY, nZ, bT);
        WDNE = getRandomNumber(wX, dY, nZ, eT);
        WDSB = getRandomNumber(wX, dY, sZ, bT);
        WDSE = getRandomNumber(wX, dY, sZ, eT);
        WUNB = getRandomNumber(wX, uY, nZ, bT);
        WUNE = getRandomNumber(wX, uY, nZ, eT);
        WUSB = getRandomNumber(wX, uY, sZ, bT);
        WUSE = getRandomNumber(wX, uY, sZ, eT);
        EDNB = getRandomNumber(eX, dY, nZ, bT);
        EDNE = getRandomNumber(eX, dY, nZ, eT);
        EDSB = getRandomNumber(eX, dY, sZ, bT);
        EDSE = getRandomNumber(eX, dY, sZ, eT);
        EUNB = getRandomNumber(eX, uY, nZ, bT);
        EUNE = getRandomNumber(eX, uY, nZ, eT);
        EUSB = getRandomNumber(eX, uY, sZ, bT);
        EUSE = getRandomNumber(eX, uY, sZ, eT);
    }

    public float getDensity(int globalX, int globalY, int globalZ, int globalT)
    {
        if (globalX >= eX)
        {
            if (globalX - eX < tessaractLength)
            {
                wX = eX;
                eX += tessaractLength;

                WDNB = EDNB;
                WDNE = EDNE;
                WDSB = EDSB;
                WDSE = EDSE;
                WUNB = EUNB;
                WUNE = EUNE;
                WUSB = EUSB;
                WUSE = EUSE;
                EDNB = getRandomNumber(eX, dY, nZ, bT);
                EDNE = getRandomNumber(eX, dY, nZ, eT);
                EDSB = getRandomNumber(eX, dY, sZ, bT);
                EDSE = getRandomNumber(eX, dY, sZ, eT);
                EUNB = getRandomNumber(eX, uY, nZ, bT);
                EUNE = getRandomNumber(eX, uY, nZ, eT);
                EUSB = getRandomNumber(eX, uY, sZ, bT);
                EUSE = getRandomNumber(eX, uY, sZ, eT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }
        else if (globalX < wX)
        {
            if (wX - globalX - 1 < tessaractLength)
            {
                eX = wX;
                wX -= tessaractLength;

                EDNB = WDNB;
                EDNE = WDNE;
                EDSB = WDSB;
                EDSE = WDSE;
                EUNB = WUNB;
                EUNE = WUNE;
                EUSB = WUSB;
                EUSE = WUSE;
                WUSB = getRandomNumber(wX, uY, sZ, bT);
                WUSE = getRandomNumber(wX, uY, sZ, eT);
                WDNB = getRandomNumber(wX, dY, nZ, bT);
                WDNE = getRandomNumber(wX, dY, nZ, eT);
                WDSB = getRandomNumber(wX, dY, sZ, bT);
                WDSE = getRandomNumber(wX, dY, sZ, eT);
                WUNB = getRandomNumber(wX, uY, nZ, bT);
                WUNE = getRandomNumber(wX, uY, nZ, eT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }
        if (globalY >= uY)
        {
            if (globalY - uY < tessaractLength)
            {
                dY = uY;
                uY += tessaractLength;

                WDNB = WUNB;
                WDNE = WUNE;
                WDSB = WUSB;
                WDSE = WUSE;
                EDNB = EUNB;
                EDNE = EUNE;
                EDSB = EUSB;
                EDSE = EUSE;
                WUNB = getRandomNumber(wX, uY, nZ, bT);
                WUNE = getRandomNumber(wX, uY, nZ, eT);
                WUSB = getRandomNumber(wX, uY, sZ, bT);
                WUSE = getRandomNumber(wX, uY, sZ, eT);
                EUNB = getRandomNumber(eX, uY, nZ, bT);
                EUNE = getRandomNumber(eX, uY, nZ, eT);
                EUSB = getRandomNumber(eX, uY, sZ, bT);
                EUSE = getRandomNumber(eX, uY, sZ, eT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }
        else if (globalY < dY)
        {
            if (dY - globalY - 1 < tessaractLength)
            {
                uY = dY;
                dY -= tessaractLength;

                WUNB = WDNB;
                WUNE = WDNE;
                WUSB = WDSB;
                WUSE = WDSE;
                EUNB = EDSB;
                EUNE = EDSE;
                EUSB = EDSB;
                EUSE = EDSE;
                WDNB = getRandomNumber(wX, dY, nZ, bT);
                WDNE = getRandomNumber(wX, dY, nZ, eT);
                WDSB = getRandomNumber(wX, dY, sZ, bT);
                WDSE = getRandomNumber(wX, dY, sZ, eT);
                EDNB = getRandomNumber(eX, dY, nZ, bT);
                EDNE = getRandomNumber(eX, dY, nZ, eT);
                EDSB = getRandomNumber(eX, dY, sZ, bT);
                EDSE = getRandomNumber(eX, dY, sZ, eT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }
        if (globalZ >= sZ)
        {
            if (globalZ - sZ < tessaractLength)
            {
                nZ = sZ;
                sZ += tessaractLength;

                WDNB = WDSB;
                WDNE = WDSE;
                WUNB = WUSB;
                WUNE = WUSE;
                EDNB = EDSB;
                EDNE = EDSE;
                EUNB = EUSB;
                EUNE = EUSE;
                WDSB = getRandomNumber(wX, dY, sZ, bT);
                WDSE = getRandomNumber(wX, dY, sZ, eT);
                WUSB = getRandomNumber(wX, uY, sZ, bT);
                WUSE = getRandomNumber(wX, uY, sZ, eT);
                EDSB = getRandomNumber(eX, dY, sZ, bT);
                EDSE = getRandomNumber(eX, dY, sZ, eT);
                EUSB = getRandomNumber(eX, uY, sZ, bT);
                EUSE = getRandomNumber(eX, uY, sZ, eT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }
        else if (globalZ < nZ)
        {
            if (nZ - globalZ - 1 < tessaractLength)
            {
                sZ = nZ;
                nZ -= tessaractLength;

                WDSB = WDNB;
                WDSE = WDNE;
                WUSB = WUNB;
                WUSE = WUNE;
                EDSB = EDNB;
                EDSE = EDNE;
                EUSB = EUNB;
                EUSE = EUNE;
                WDNB = getRandomNumber(wX, dY, nZ, bT);
                WDNE = getRandomNumber(wX, dY, nZ, eT);
                WUNB = getRandomNumber(wX, uY, nZ, bT);
                WUNE = getRandomNumber(wX, uY, nZ, eT);
                EDNB = getRandomNumber(eX, dY, nZ, bT);
                EDNE = getRandomNumber(eX, dY, nZ, eT);
                EUNB = getRandomNumber(eX, uY, nZ, bT);
                EUNE = getRandomNumber(eX, uY, nZ, eT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }
        if (globalT >= eT)
        {
            if (globalT - eT < tessaractLength)
            {
                bT = eT;
                eT += tessaractLength;

                WDNB = WDNE;
                WDSB = WDSE;
                WUNB = WUNE;
                WUSB = WUSE;
                EDNB = EDNE;
                EDSB = EDSE;
                EUNB = EUNE;
                EUSB = EUSE;
                WDNE = getRandomNumber(wX, dY, nZ, eT);
                WDSE = getRandomNumber(wX, dY, sZ, eT);
                WUNE = getRandomNumber(wX, uY, nZ, eT);
                WUSE = getRandomNumber(wX, uY, sZ, eT);
                EDNE = getRandomNumber(eX, dY, nZ, eT);
                EDSE = getRandomNumber(eX, dY, sZ, eT);
                EUNE = getRandomNumber(eX, uY, nZ, eT);
                EUSE = getRandomNumber(eX, uY, sZ, eT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }
        else if (globalT < bT)
        {
            if (bT - globalT - 1 < tessaractLength)
            {
                eT = bT;
                bT -= tessaractLength;

                WDNE = WDNB;
                WDSE = WDSB;
                WUNE = WUNB;
                WUSE = WUSB;
                EDNE = EDNB;
                EDSE = EDSB;
                EUNE = EUNB;
                EUSE = EUSB;
                WDNB = getRandomNumber(wX, dY, nZ, bT);
                WDSB = getRandomNumber(wX, dY, sZ, bT);
                WUNB = getRandomNumber(wX, uY, nZ, bT);
                WUSB = getRandomNumber(wX, uY, sZ, bT);
                EDNB = getRandomNumber(eX, dY, nZ, bT);
                EDSB = getRandomNumber(eX, dY, sZ, bT);
                EUNB = getRandomNumber(eX, uY, nZ, bT);
                EUSB = getRandomNumber(eX, uY, sZ, bT);
            }
            else moveTo(globalX, globalY, globalZ, globalT);
        }

        int localEX = globalX & lengthMinus1;
        int localUY = globalY & lengthMinus1;
        int localSZ = globalZ & lengthMinus1;
        int localET = globalT & lengthMinus1;

        int localWX = tessaractLength - localEX;
        int localDY = tessaractLength - localUY;
        int localNZ = tessaractLength - localSZ;
        int localBT = tessaractLength - localET;

        return (WDNB*localWX*localDY*localNZ*localBT +
                WDNE*localWX*localDY*localNZ*localET +
                WDSB*localWX*localDY*localSZ*localBT +
                WDSE*localWX*localDY*localSZ*localET +
                WUNB*localWX*localUY*localNZ*localBT +
                WUNE*localWX*localUY*localNZ*localET +
                WUSB*localWX*localUY*localSZ*localBT +
                WUSE*localWX*localUY*localSZ*localET +
                EDNB*localEX*localDY*localNZ*localBT +
                EDNE*localEX*localDY*localNZ*localET +
                EDSB*localEX*localDY*localSZ*localBT +
                EDSE*localEX*localDY*localSZ*localET +
                EUNB*localEX*localUY*localNZ*localBT +
                EUNE*localEX*localUY*localNZ*localET +
                EUSB*localEX*localUY*localSZ*localBT +
                EUSE*localEX*localUY*localSZ*localET)
                /(tessaractLength * tessaractLength * tessaractLength * tessaractLength);
    }

    //@param divisionWeightDistributionMod: between 0 and 1, 0 being smaller divisions, 1 being larger
    public static float getDensity(BinaryTessaractInterpolator[] interpolators, int globalX, int globalY, int globalZ, int globalT)
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
            int cubeLength = interpolators[i].tessaractLength;
            totalWeight += cubeLength;
            density += interpolators[i].getDensity(globalX, globalY, globalZ, globalT) * cubeLength;
        }
        return density/totalWeight;
    }
}
