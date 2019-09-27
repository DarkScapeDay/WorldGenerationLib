package worldgenerationutils.worldmanagement;

import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public final class WorldManager
{
    private static final String CHUNK_GENERATOR_FILENAME = "chunk-generator";

    private final JavaPlugin plugin;
    private final Map<String, LibChunkGenerator> chunkGenerators;

    public WorldManager(JavaPlugin plugin)
    {
        this.plugin = plugin;
        this.chunkGenerators = new HashMap<>();
    }

    public void loadExistingWorld(String worldName)
    {
        if (canCreateWorld(worldName))
        {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
            {
                try
                {
                    LibChunkGenerator generator = readGenerator(worldName);
                    Bukkit.getScheduler().runTask(plugin, () -> createWorld(worldName, generator));
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            });
        }
    }

    public void loadExistingWorlds()
    {
        loadExistingWorlds((worldName, hasGenerator) -> true);
    }

    public void loadExistingWorlds(WorldFilter filter)
    {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
        {
            for (File file : Objects.requireNonNull(Bukkit.getWorldContainer().listFiles(File::isDirectory)))
            {
                try
                {
                    LibChunkGenerator generator = readGenerator(file.getName());
                    if (canCreateWorld(file.getName()) && filter.test(file, generator != null))
                    {
                        Bukkit.getScheduler().runTask(plugin, () -> createWorld(file.getName(), generator));
                    }
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    public boolean canCreateWorld(String worldName)
    {
        Objects.requireNonNull(worldName);
        return Bukkit.getWorld(worldName) == null;
    }

    public void registerChunkGenerator(LibChunkGenerator generator)
    {
        Objects.requireNonNull(generator);
        if (chunkGenerators.containsKey(generator.getName()))
        {
            throw new IllegalArgumentException("ChunkGenerator with name \"" + generator.getName() + "\" already registered!");
        }
        chunkGenerators.put(generator.getName(), generator);
    }

    private static File getGeneratorNameFile(String worldName)
    {
        File worldFolder = new File(Bukkit.getWorldContainer().getAbsolutePath() + "/" + worldName);
        if (!worldFolder.exists())
        {
            throw new IllegalArgumentException("World " + worldName + " does not have a folder!");
        }
        return new File(worldFolder.getAbsolutePath() + "/" + CHUNK_GENERATOR_FILENAME);
    }

    @CheckForNull
    public LibChunkGenerator getChunkGenerator(String chunkGeneratorName)
    {
        return chunkGenerators.get(chunkGeneratorName);
    }

    public List<LibChunkGenerator> getChunkGenerators()
    {
        List<LibChunkGenerator> chunkGenerators = new ArrayList<>(this.chunkGenerators.values());
        chunkGenerators.sort(Comparator.comparing(LibChunkGenerator::getName));
        return chunkGenerators;
    }

    public List<String> getChunkGeneratorNames()
    {
        List<String> chunkGeneratorNames = new ArrayList<>(this.chunkGenerators.keySet());
        chunkGeneratorNames.sort(Comparator.naturalOrder());
        return chunkGeneratorNames;
    }

    @CheckForNull
    private LibChunkGenerator readGenerator(String worldName) throws IOException
    {
        Objects.requireNonNull(worldName);

        File file = getGeneratorNameFile(worldName);
        if (!file.exists())
        {
            return null;
        }

        String generatorName = new String(Files.readAllBytes(file.toPath()));

        LibChunkGenerator result = getChunkGenerator(generatorName);
        if (result == null)
        {
            throw new IllegalStateException("World " + worldName + " had an invalid chunk generator called " + generatorName);
        }

        Bukkit.broadcastMessage("got Chunk Generator " + result);
        return result;
    }

    private void writeGenerator(World world, LibChunkGenerator chunkGenerator)
    {
        Objects.requireNonNull(chunkGenerator);
        File generatorNameFile = getGeneratorNameFile(world.getName());

        try (FileWriter writer = new FileWriter(generatorNameFile))
        {
            writer.write(chunkGenerator.getName());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public World createWorld(String name, @Nullable LibChunkGenerator chunkGenerator)
    {
        if (!canCreateWorld(name))
        {
            throw new IllegalStateException("World " + name + " already exists!");
        }
        if (!MinecraftServer.getServer().isMainThread())
        {
            throw new IllegalStateException("Must call createWorld from main thread!");
        }

        WorldCreator creator = new WorldCreator(name);
        if (chunkGenerator != null)
        {
            creator = creator.generator(chunkGenerator);
        }
        World bukkitWorld = Bukkit.createWorld(creator);
        if (bukkitWorld == null)
        {
            throw new IllegalStateException("Failed to create world " + name + "!");
        }
        if (chunkGenerator != null)
        {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> writeGenerator(bukkitWorld, chunkGenerator));
        }
        return bukkitWorld;
    }
}
