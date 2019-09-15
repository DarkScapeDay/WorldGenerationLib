package WorldGenerationUtils;

import WorldGenerationUtils.Vectors.Vector;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class GenerationTools //USE RELATIVE CHUNK COORDINATES (relative to chunk's LLL corner) ONLY!!!!!!!!!
{
    public static int CHUNK_LENGTH = 0x10;
    public static int CHUNK_HEIGHT = 0x100;

    public static int CHUNK_X_MAX = CHUNK_LENGTH - 1;
    public static int CHUNK_Y_MAX = CHUNK_HEIGHT - 1;
    public static int CHUNK_Z_MAX = CHUNK_LENGTH - 1;

    private static final int CHUNK_LENGTH_CUBED = CHUNK_LENGTH * CHUNK_LENGTH * CHUNK_LENGTH;

    public static Material getMaterial(short[][] chunk, int x, int y, int z)
    {
        return Material.getMaterial(getMaterialId(chunk, x, y, z));
    }

    public static int getMaterialId(short[][] chunk, int x, int y, int z)
    {
//        x &= CHUNK_X_MAX;
//        y &= CHUNK_Y_MAX;
//        z &= CHUNK_Z_MAX;
        if (chunk[y >> 0x4] == null)
        {
            return 0;
//            chunk[y >> 0x4] = new short[CHUNK_LENGTH_CUBED];
        }
        return chunk[y >> 0x4][((y & 0xF) << 8) | (z << 4) | x];
    }

    public static Material getMaterial(byte[][] chunk, int x, int y, int z)
    {
        return Material.getMaterial(getMaterialId(chunk, x, y, z));
    }

    public static int getMaterialId(byte[][] chunk, int x, int y, int z)
    {
//        x &= CHUNK_X_MAX;
//        y &= CHUNK_Y_MAX;
//        z &= CHUNK_Z_MAX;
        if (chunk[y >> 0x4] == null)
        {
            return 0;
            //chunk[y >> 0x4] = new byte[CHUNK_LENGTH_CUBED];
        }
        return chunk[y >> 0x4][((y & 0xF) << 8) | (z << 4) | x];
    }

    public static int getMaterialId(Chunk chunk, int x, int y, int z)
    {
        return getMaterial(chunk, x, y, z).getId();
    }

    public static Material getMaterial(Chunk chunk, int x, int y, int z)
    {
//        x &= CHUNK_X_MAX;
//        y &= CHUNK_Y_MAX;
//        z &= CHUNK_Z_MAX;
        return chunk.getBlock(x, y, z).getType();
    }


    public static void setMaterial(short[][] chunk, Vector vector, int materialID)
    {
        setMaterial(chunk, vector.getX().intValue(), vector.getY().intValue(), vector.getZ().intValue(), materialID);
    }

    public static void setMaterial(short[][] chunk, Vector vector, Material material)
    {
        setMaterial(chunk, vector.getX().byteValue(), vector.getY().byteValue(), vector.getZ().byteValue(), material);
    }

    public static void setMaterial(short[][] chunk, int x, int y, int z, Material material)
    {
        setMaterial(chunk, x, y, z, material.getId());
    }

    public static void setMaterial(short[][] chunk, int x, int y, int z, int materialId)
    {
//        if (materialId < Byte.MIN_VALUE || materialId > Byte.MAX_VALUE)
//        {
//            materialId = 1;
//        }
        setMaterial(chunk, x, y, z, (short)materialId);
    }

    public static void setMaterial(short[][] chunk, int x, int y, int z, short materialId)
    {
        //        x &= CHUNK_X_MAX;
        //        y &= CHUNK_Y_MAX;
        //        z &= CHUNK_Z_MAX;
        if (chunk[y >> 0x4] == null)
        {
            chunk[y >> 0x4] = new short[CHUNK_LENGTH_CUBED];
        }
        chunk[y >> 0x4][((y & 0xF) << 0x8) | (z << 0x4) | x] = materialId;
    }



    public static void setMaterial(byte[][] chunk, Vector vector, Material material)
    {
        setMaterial(chunk, vector, material.getId());
    }

    public static void setMaterial(byte[][] chunk, Vector vector, int materialID)
    {
        setMaterial(chunk, vector.getX().intValue(), vector.getY().intValue(), vector.getZ().intValue(), materialID);
    }

    public static void setMaterial(byte[][] chunk, int x, int y, int z, Material material)
    {
        setMaterial(chunk, x, y, z, material.getId());
    }

    public static void setMaterial(byte[][] chunk, int x, int y, int z, int materialId)
    {
//        x &= CHUNK_X_MAX;
//        y &= CHUNK_Y_MAX;
//        z &= CHUNK_Z_MAX;
        if (chunk[y >> 0x4] == null)
        {
            chunk[y >> 0x4] = new byte[CHUNK_LENGTH_CUBED];
        }
        chunk[y >> 0x4][((y & 0xF) << 0x8) | (z << 0x4) | x] = (byte)materialId;
    }


    public static void setMaterial(Chunk chunk, int x, int y, int z, Material material)
    {
        setMaterial(chunk, x, y, z, material.getId());
    }

    public static void setMaterial(Chunk chunk, int x, int y, int z, int materialId) 
    {
//        x &= CHUNK_X_MAX;
//        y &= CHUNK_Y_MAX;
//        z &= CHUNK_Z_MAX;
        chunk.getBlock(x, y, z).setTypeId(materialId);
    }

    public static void setMaterial(Chunk chunk, int x, int y, int z, Material material, byte data)
    {
        setMaterial(chunk, x, y, z, material.getId(), data);
    }

    public static void setMaterial(Chunk chunk, int x, int y, int z, int materialId, byte data)
    {
//        x &= CHUNK_X_MAX;
//        y &= CHUNK_Y_MAX;
//        z &= CHUNK_Z_MAX;
        final Block b = chunk.getBlock(x, y, z);
        b.setTypeId(materialId);
        b.setData(data);
    }

    public static void setMaterialData(Chunk chunk, int x, int y, int z, byte data)
    {
//        x &= CHUNK_X_MAX;
//        y &= CHUNK_Y_MAX;
//        z &= CHUNK_Z_MAX;
        chunk.getBlock(x, y, z).setData(data);
    }

    public static void setChunkProtectionLevel(Chunk chunk, ChunkProtectionLevel level)
    {
        switch (level)
        {
            case UNPROTECTED:
                setMaterial(chunk, 7, 1, 7,  1);
                setMaterial(chunk, 7, 1, 8,  1);
                break;
            case DISABLE_BLOCK_BREAKING:
                setMaterial(chunk, 7, 1, 7, 7);
                setMaterial(chunk, 7, 1, 8, 1);
                break;
            case DISABLE_EVERYTHING:
                setMaterial(chunk, 7, 1, 7, 1);
                setMaterial(chunk, 7, 1, 8, 7);
                break;
        }
    }

    public static void setChunkProtectionLevel(byte[][] chunk, ChunkProtectionLevel level)
    {
        switch (level)
        {
            case UNPROTECTED:
                setMaterial(chunk, 7, 1, 7, 1);
                setMaterial(chunk, 7, 1, 8, 1);
                break;
            case DISABLE_BLOCK_BREAKING:
                setMaterial(chunk, 7, 1, 7, 7);
                setMaterial(chunk, 7, 1, 8, 1);
                break;
            case DISABLE_EVERYTHING:
                setMaterial(chunk, 7, 1, 7, 1);
                setMaterial(chunk, 7, 1, 8, 7);
                break;
        }
    }

    public enum ChunkProtectionLevel
    {
        UNPROTECTED,
        DISABLE_BLOCK_BREAKING,
        DISABLE_EVERYTHING
    }

    public static void setChunkProtectionLevel(World world, int anyXInChunk, int anyZInChunk, ChunkProtectionLevel level)
    {
        setChunkProtectionLevel(world.getChunkAt(anyXInChunk >> 4, anyZInChunk >> 4), level);
    }

    public static ChunkProtectionLevel getChunkProtectionLevel(World w, int anyXInChunk, int anyZInChunk)
    {
        return getChunkProtectionLevel(w.getChunkAt(anyXInChunk >> 4, anyZInChunk >> 4));
    }

    public static ChunkProtectionLevel getChunkProtectionLevel(Location location)
    {
        return getChunkProtectionLevel(location.getChunk());
    }

    public static ChunkProtectionLevel getChunkProtectionLevel(Block b)
    {
        return getChunkProtectionLevel(b.getChunk());
    }

    public static boolean buildingIsPrevented(Block b)
    {
        return getChunkProtectionLevel(b) != ChunkProtectionLevel.UNPROTECTED;
    }


    public static ChunkProtectionLevel getChunkProtectionLevel(Chunk chunk)
    {
        if (getMaterialId(chunk, 7, 1, 7) == 7)
        {
            return ChunkProtectionLevel.DISABLE_BLOCK_BREAKING;
        }
        else if (getMaterialId(chunk, 7, 1, 8) == 7)
        {
            return ChunkProtectionLevel.DISABLE_EVERYTHING;
        }
        return ChunkProtectionLevel.UNPROTECTED;
    }

    public static ChunkProtectionLevel getChunkProtectionLevel(byte[][] chunk)
    {
        if (getMaterialId(chunk, 7, 1, 7) == 7)
        {
            return ChunkProtectionLevel.DISABLE_BLOCK_BREAKING;
        }
        else if (getMaterialId(chunk, 7, 1, 8) == 7)
        {
            return ChunkProtectionLevel.DISABLE_EVERYTHING;
        }
        return ChunkProtectionLevel.UNPROTECTED;
    }

    public static int getHighestBlockYInChunk(Chunk chunk, int x, int z)
    {
//        x &= CHUNK_X_MAX;
//        z &= CHUNK_Z_MAX;
        int highestBlockY;
        for (highestBlockY = CHUNK_Y_MAX; highestBlockY > 0 && GenerationTools.getMaterialId(chunk, x, highestBlockY, z) == 0; highestBlockY--);
        return highestBlockY;
    }

    public static int getHighestBlockYInChunk(byte[][] chunk, int x, int z)
    {
//        x &= CHUNK_X_MAX;
//        z &= CHUNK_Z_MAX;
        int highestBlockY;
        for (highestBlockY = CHUNK_Y_MAX; highestBlockY > 0 && GenerationTools.getMaterialId(chunk, x, highestBlockY, z) == 0; highestBlockY--);
        return highestBlockY;
    }
}
