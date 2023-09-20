package net.neo3d;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.neo3d.config.Config;
import net.neo3d.config.VideoResolution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class Initializer implements ClientModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("neo3d");

	private static String VERSION;
	public static Config CONFIG;

	@Override
	public void onInitializeClient() {

		VERSION = FabricLoader.getInstance()
				.getModContainer("neo3d")
				.get()
				.getMetadata()
				.getVersion().getFriendlyString();

		LOGGER.info("== neo3d ==");

		VideoResolution.init();

		var configPath = FabricLoader.getInstance()
				.getConfigDir()
				.resolve("neo3d_settings.json");

		CONFIG = loadConfig(configPath);

	}

	private static Config loadConfig(Path path) {
		Config config = Config.load(path);
		if(config == null) {
			config = new Config();
			config.write();
		}
		return config;
	}

	public static String getVersion() {
		return VERSION;
	}
}
