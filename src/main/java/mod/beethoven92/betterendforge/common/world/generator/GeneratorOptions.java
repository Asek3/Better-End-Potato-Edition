package mod.beethoven92.betterendforge.common.world.generator;

import mod.beethoven92.betterendforge.config.Configs;
import net.minecraft.util.math.MathHelper;

public class GeneratorOptions {
    public static boolean vanillaEndIntegration;
    private static int biomeSizeLand;
    private static int biomeSizeVoid;
    private static int biomeSizeCaves;
    private static boolean hasPortal;
    private static boolean hasPillars;
    private static boolean newGenerator;
    private static boolean noRingVoid;
    private static int endCityFailChance;
    public static LayerOptions bigOptions;
    public static LayerOptions mediumOptions;
    public static LayerOptions smallOptions;
    private static long islandDistBlock;
    private static int islandDistChunk;
    private static boolean directSpikeHeight;
    public static boolean disableDragonFight;

    public static void init() {
        biomeSizeLand = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeLand", 256);
        biomeSizeVoid = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeVoid", 256);
        biomeSizeCaves = Configs.GENERATOR_CONFIG.getInt("biomeMap", "biomeSizeCaves", 32);
        hasPortal = Configs.GENERATOR_CONFIG.getBoolean("portal", "hasPortal", true);
        hasPillars = Configs.GENERATOR_CONFIG.getBoolean("spikes", "hasSpikes", true);
        vanillaEndIntegration = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "vanillaEndIntegration", true);
        newGenerator = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "useNewGenerator", true);
        noRingVoid = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "noRingVoid", false);
        endCityFailChance = Configs.GENERATOR_CONFIG.getInt("customGenerator", "endCityFailChance", 5);
        disableDragonFight = Configs.GENERATOR_CONFIG.getBoolean("customGenerator", "disableDragonFight", false);
        bigOptions = new LayerOptions("customGenerator.layers.bigIslands", Configs.GENERATOR_CONFIG, 300, 200, 70, 10, false);
        mediumOptions = new LayerOptions("customGenerator.layers.mediumIslands", Configs.GENERATOR_CONFIG, 150, 100, 70, 20, true);
        smallOptions = new LayerOptions("customGenerator.layers.smallIslands", Configs.GENERATOR_CONFIG, 60, 50, 70, 30, false);
        int circleRadius = Configs.GENERATOR_CONFIG.getInt("customGenerator", "voidRingSize", 1000);
        islandDistBlock = (long) circleRadius * (long) circleRadius;
        islandDistChunk = (circleRadius >> 3);
    }

    public static int getBiomeSizeLand() {
        return MathHelper.clamp(biomeSizeLand, 1, 8192);
    }

    public static int getBiomeSizeVoid() {
        return MathHelper.clamp(biomeSizeVoid, 1, 8192);
    }

    public static int getBiomeSizeCaves() {
        return MathHelper.clamp(biomeSizeCaves, 1, 8192);
    }

    public static boolean hasPortal() {
        return hasPortal;
    }

    public static boolean hasPillars() {
        return hasPillars;
    }

    public static boolean noRingVoid() {
        return noRingVoid;
    }

    public static boolean useNewGenerator() {
        return newGenerator;
    }

    public static int getEndCityFailChance() {
        return endCityFailChance;
    }

    public static long getIslandDistBlock() {
        return islandDistBlock;
    }

    public static int getIslandDistChunk() {
        return islandDistChunk;
    }

    public static void setDirectSpikeHeight() {
        directSpikeHeight = true;
    }

    public static boolean isDirectSpikeHeight() {
        boolean height = directSpikeHeight;
        directSpikeHeight = false;
        return height;
    }
}