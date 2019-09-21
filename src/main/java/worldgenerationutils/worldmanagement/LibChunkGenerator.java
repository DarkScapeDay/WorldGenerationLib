package worldgenerationutils.worldmanagement;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class LibChunkGenerator extends ChunkGenerator
{
    private final String name;
    private final List<BlockPopulator> blockPopulators;

    public LibChunkGenerator(String name, BlockPopulator... populators)
    {
        this.name = name;
        this.blockPopulators = Arrays.asList(populators);
    }

    @Override
    public String toString()
    {
        return name;
    }

    public final String getName()
    {
        return name;
    }

    @Override
    public final List<BlockPopulator> getDefaultPopulators(final World w)
    {
        return blockPopulators;
    }

    protected abstract MaterialData getBlockData(World world, int blockX, int blockY, int blockZ, BiomeGrid biome);

    @Override
    public final ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome)
    {
        ChunkData data = createChunkData(world);

        int blockX = chunkX * 0x10;
        int blockZ = chunkZ * 0x10;

        for (int relY = 0; relY < 0x100; relY++)
        {
            for (int relX = 0, absX = blockX; relX < 0x10; relX++, absX++)
            {
                for (int relZ = 0, absZ = blockZ; relZ < 0x10; relZ++, absZ++)
                {
                    data.setBlock(relX, relY, relZ, getBlockData(world, absX, relY, absZ, biome));
                }
            }
        }
        return data;
    }
}
