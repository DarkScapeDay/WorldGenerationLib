package me.happyman;

// JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.
public class LameNoise
{
    private static final int p[] = new int[0x200];
    private static final int permutation[] =
    {
        151,160,137,91,90,15,
        131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
        190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
        88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
        77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
        102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
        135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
        5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
        223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
        129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
        251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
        49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
        138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };
    static
    {
        for (int i = 0; i < 0x100; i++)
        {
            int random = permutation[i];
            p[i] = random;
            p[i+0x100] = random;
        }
    }

    private static float fade(float t)
    {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static float grad(int hash, float x, float y, float z)
    {
        switch(hash & 0xF)
        {
            case 0x0: return  x + y;
            case 0x1: return -x + y;
            case 0x2: return  x - y;
            case 0x3: return -x - y;
            case 0x4: return  x + z;
            case 0x5: return -x + z;
            case 0x6: return  x - z;
            case 0x7: return -x - z;
            case 0x8: return  y + z;
            case 0x9: return -y + z;
            case 0xA: return  y - z;
            case 0xB: return -y - z;
            case 0xC: return  y + x;
            case 0xD: return -y + z;
            case 0xE: return  y - x;
            case 0xF: return -y - z;
        }
        return 0; //impossible
    }

    private static float lerp(float t, float a, float b)
    {
        return a + t * (b - a);
    }
    public static float getDensity(float x, float y, float z) {
        int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
                Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
                Z = (int)Math.floor(z) & 255;
        x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
        y -= Math.floor(y);                                // OF POINT IN CUBE.
        z -= Math.floor(z);
        float u = fade(x),                                // COMPUTE FADE CURVES
                v = fade(y),                                // FOR EACH OF X,Y,Z.
                w = fade(z);
        int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
                B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

        return lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  // AND ADD
                grad(p[BA  ], x-1, y  , z   )), // BLENDED
                lerp(u, grad(p[AB  ], x  , y-1, z   ),  // RESULTS
                        grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
                lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  // CORNERS
                        grad(p[BA+1], x-1, y  , z-1 )), // OF CUBE
                        lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
                                grad(p[BB+1], x-1, y-1, z-1 ))));
    }

    /*private static float getDensity(float x, float y, float z)
    {
        float xFloored = (float)Math.floor(x);
        float yFloored = (float)Math.floor(y);
        float zFloored = (float)Math.floor(z);
        int X = (int)xFloored & 0xFF;                  // FIND UNIT CUBE THAT
        int Y = (int)yFloored & 0xFF;                  // CONTAINS POINT.
        int Z = (int)zFloored & 0xFF;
        x -= xFloored;                                // FIND RELATIVE X,Y,Z
        y -= yFloored;                                // OF POINT IN unit CUBE.
        z -= zFloored;
        float fadedX = x;//fade(x);                      // compute fade curves
        float fadedY = y;//fade(y);                      // for each of X, Y, Z
        float fadedZ = z;//fade(z);

        int W = p[X  ]+Y;
        int E = p[X+1]+Y;

        int WD = p[W  ]+Z;
        int WU = p[W+1]+Z;
        int ED = p[E  ]+Z;
        int EU = p[E+1]+Z;

        int WDN = p[WD  ];
        int WDS = p[WD+1];
        int WUN = p[WU  ];
        int WUS = p[WU+1];
        int EDN = p[ED  ];
        int EDS = p[ED+1];
        int EUN = p[EU  ];
        int EUS = p[EU+1];

        return lerp(fadedX, lerp(fadedY, lerp(fadedZ, grad(WDN,   x  ,   y  ,   z  ),  // AND ADD BLENDED RESULTS FROM 8 CORNERS OF CUBE
                                                      grad(WDS,   x  ,   y  ,z-1)
                                             ),
                                         lerp(fadedZ, grad(WUN,   x  ,y-1,   z  ),
                                                      grad(WUS,   x  ,y-1,z-1)
                                             )
                                ),
                            lerp(fadedY, lerp(fadedZ, grad(EDN,x-1,   y  ,   z  ),
                                                      grad(EDS,x-1,   y  ,z-1)
                                             ),
                                         lerp(fadedZ, grad(EUN,x-1,y-1,   z  ),
                                                      grad(EUS,x-1,y-1,z-1)
                                             )
                                )
                  );
//        return lerp(fadedZ, lerp(fadedY, lerp(fadedX, grad(WDN,   x  ,   y  ,   z  ),  // AND ADD BLENDED RESULTS FROM 8 CORNERS OF CUBE
//                                                      grad(EDN,x-1,   y  ,   z  )
//                                             ),
//                                         lerp(fadedX, grad(WUN,   x  ,y-1,   z  ),
//                                                      grad(EUN,x-1,y-1,   z  )
//                                             )
//                                ),
//                            lerp(fadedY, lerp(fadedX, grad(WDS,   x  ,   y  ,z-1),
//                                                      grad(EDS,x-1,   y  ,z-1)
//                                             ),
//                                         lerp(fadedX, grad(WUS,   x  ,y-1,z-1),
//                                                      grad(EUS,x-1,y-1,z-1)
//                                             )
//                                )
//                   );
    }*/

    public static float getDensity(int octaves, float x, float y, float z)
    {
        float density = getDensity(x, y, z);

        float amplitude = 1f;
        for (int i = 0; i < octaves - 1; i++)
        {
            x *= 2;
            y *= 2;
            z *= 2;
            amplitude *= 0.5f;
            density += getDensity(x, y, z) * amplitude;
        }
        float totalWeight = 2f - amplitude;

        return density/totalWeight;
    }
}