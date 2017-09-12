package me.happyman;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGenerationTools
{
    //be VERY careful before calling these methods
    public static byte getMaterialId(byte[][] chunk, int x, int y, int z)
    {
//                if (getGlobalY > 0xFF || getGlobalY < 0x0 || xRelBase > 0xF || xRelBase < 0x0 || zRelBase > 0xF || zRelBase < 0x0)
//                {
//                    return -1; //Out of bounds
//                }
        if (chunk[y >> 0x4] == null) {
            chunk[y >> 0x4] = new byte[0x10 * 0x10 * 0x10];
        }
        return chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
    }

    public static Material getMaterial(byte[][] chunk, int x, int y, int z)
    {
        return Material.getMaterial(getMaterialId(chunk, x, y, z));
    }

    public static Material getMaterial(byte[][] chunk, byte x, short y, byte z)
    {
        return Material.getMaterial(getMaterialId(chunk, x, y, z));
    }

    public static void setMaterial(byte[][] chunk, byte x, short y, byte z, short materialId)
    {
        setMaterial(chunk, (int)x, (int)y, (int)z, (byte)materialId);
    }

    private static void setMaterial(byte[][] chunk, int x, int y, int z, byte materialId)
    {
//                if (getGlobalY > 0xFF || getGlobalY < 0x0 || xRelBase > 0xF || xRelBase < 0x0 || zRelBase > 0xF || zRelBase < 0x0)
//                {
//                    sendErrorMessage("Error! Block at (" + xRelBase + ", " + getGlobalY + ", " + zRelBase + ") in world generator was out of bounds when setting it!");
//                    return;
//                }
        if (chunk[y >> 0x4] == null) {
            chunk[y >> 0x4] = new byte[0x10 * 0x10 * 0x10];
        }
        chunk[y >> 0x4][((y & 0xF) << 0x8) | (z << 0x4) | x] = materialId;
    }

    public static void setMaterial(byte[][] chunk, byte x, short y, byte z, Material material)
    {
        setMaterial(chunk, (int)x, (int)y, (int)z, (byte)material.getId());
    }

    private static void setMaterial(byte[][] chunk, int x, int y, int z, Material material)
    {
        setMaterial(chunk, x, y, z, (byte) material.getId());
    }

    public static short getMaterialId(Chunk chunk, byte x, short y, byte z)
    {
        return (short)chunk.getBlock(x, y, z).getType().getId();
    }

    public static Material getMaterial(Chunk chunk, byte x, short y, byte z)
    {
        return chunk.getBlock(x, y, z).getType();
    }

    public static void setMaterial(Chunk chunk, byte x, short y, byte z, short materialId) 
    {
        chunk.getBlock(x, y, z).setTypeId(materialId);
    }

    public static void setMaterialData(Chunk chunk, byte x, short y, byte z, byte data)
    {
        chunk.getBlock(x, y, z).setData(data);
    }

    public static void setMaterial(Chunk chunk, byte x, short y, byte z, short materialId, byte data)
    {
        Block b = chunk.getBlock(x, y, z);
        b.setTypeId(materialId);
        b.setData(data);
    }

    public static void setMaterial(Chunk chunk, byte x, short y, byte z, Material material, byte data)
    {
        Block b = chunk.getBlock(x, y, z);
        b.setType(material);
        b.setData(data);
    }

    public static void setMaterial(Chunk chunk, byte x, short y, byte z, Material material)
    {
        chunk.getBlock(x, y, z).setType(material);
    }

    public static void setChunkProtectionLevel(Chunk chunk, ChunkProtectionLevel level)
    {
        switch (level)
        {
            case UNPROTECTED:
                setMaterial(chunk, (byte)7, (short)1, (byte)7, (short) 1);
                setMaterial(chunk, (byte)7, (short)1, (byte)8, (short) 1);
                break;
            case DISABLE_BLOCK_BREAKING:
                setMaterial(chunk, (byte)7, (short)1, (byte)7, (short)7);
                setMaterial(chunk, (byte)7, (short)1, (byte)8, (short)1);
                break;
            case DISABLE_EVERYTHING:
                setMaterial(chunk, (byte)7, (short)1, (byte)7, (short)1);
                setMaterial(chunk, (byte)7, (short)1, (byte)8, (short)7);
                break;
        }
    }

    public static void setChunkProtectionLevel(byte[][] chunk, ChunkProtectionLevel level)
    {
        switch (level)
        {
            case UNPROTECTED:
                setMaterial(chunk, (byte)7, (short)1, (byte)7, (short)1);
                setMaterial(chunk, (byte)7, (short)1, (byte)8, (short)1);
                break;
            case DISABLE_BLOCK_BREAKING:
                setMaterial(chunk, (byte)7, (short)1, (byte)7, (short)7);
                setMaterial(chunk, (byte)7, (short)1, (byte)8, (short)1);
                break;
            case DISABLE_EVERYTHING:
                setMaterial(chunk, (byte)7, (short)1, (byte)7, (short)1);
                setMaterial(chunk, (byte)7, (short)1, (byte)8, (short)7);
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
        setChunkProtectionLevel(world.getChunkAt(anyXInChunk / 0x10, anyZInChunk / 0x10), level);
    }

    public static ChunkProtectionLevel getChunkProtectionLevel(World w, int anyXInChunk, int anyZInChunk)
    {
        return getChunkProtectionLevel(w.getChunkAt(anyXInChunk / 0x10, anyZInChunk / 0x10));
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
        if (getMaterial(chunk, (byte)7, (short)1, (byte)7).equals(Material.BEDROCK))
        {
            return ChunkProtectionLevel.DISABLE_BLOCK_BREAKING;
        }
        else if (getMaterial(chunk, (byte)7, (short)1, (byte)8).equals(Material.BEDROCK))
        {
            return ChunkProtectionLevel.DISABLE_EVERYTHING;
        }
        return ChunkProtectionLevel.UNPROTECTED;
    }

    public static ChunkProtectionLevel getChunkProtectionLevel(byte[][] chunk)
    {
        if (getMaterial(chunk, (byte)7, (short)1, (byte)7).equals(Material.BEDROCK))
        {
            return ChunkProtectionLevel.DISABLE_BLOCK_BREAKING;
        }
        else if (getMaterial(chunk, (byte)7, (short)1, (byte)8).equals(Material.BEDROCK))
        {
            return ChunkProtectionLevel.DISABLE_EVERYTHING;
        }
        return ChunkProtectionLevel.UNPROTECTED;
    }

    public static short getHighestBlockYInChunk(Chunk chunk, byte xInChunk, byte zInChunk)
    {
        short highestBlockY;
        for (highestBlockY = 0xFF; highestBlockY > 0 && WorldGenerationTools.getMaterial(chunk, xInChunk, highestBlockY, zInChunk).equals(Material.AIR); highestBlockY--);
        return highestBlockY;
    }

    public static short getHighestBlockYInChunk(byte[][] chunk, byte xInChunk, byte zInChunk)
    {
        short highestBlockY;
        for (highestBlockY = 0xFF; highestBlockY > 0 && WorldGenerationTools.getMaterialId(chunk, xInChunk, highestBlockY, zInChunk) == 0; highestBlockY--);
        return highestBlockY;
    }
}
