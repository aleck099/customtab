package net.accel.customtab;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ModMain implements ModInitializer {
    public static final String ID = "customtab";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

    public static ServerMonitor MONITOR = new ServerMonitor();
    public static Config CONFIG;

    public static void createDefaultConfig(Path configPath) throws IOException {
        try (var stream = Files.newOutputStream(configPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             var jarStream = ModMain.class.getResourceAsStream("/example.ini")) {
            if (jarStream == null)
                throw new AssertionError();
            jarStream.transferTo(stream);
        }
    }

    static {
        var cpath = FabricLoader.getInstance().getConfigDir().resolve("customtab.ini");
        try {
            if (!Files.exists(cpath)) {
                LOGGER.info("creating default config");
                createDefaultConfig(cpath);
            }
            CONFIG = new Config(cpath);
        } catch (IOException | IllegalArgumentException e) {
            LOGGER.warn("failed to load config, use the default config");
            CONFIG = Config.DEFAULT;
        }
    }

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(s -> {
            MONITOR.update(s.getCurrentPlayerCount(), (int) s.getTickTime());
        });
    }
}
