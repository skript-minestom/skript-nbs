package com.github.hapily04.skriptnbs;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.github.hapily04.skriptnbs.api.NbsEventBridge;
import com.github.hapily04.skriptnbs.api.SongService;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SkriptNbs extends JavaPlugin {

	private static SkriptNbs instance;

	private final Logger logger = LoggerFactory.getLogger(SkriptNbs.class);
	private SongService songService;

	@Override
	public void onEnable() {
		instance = this;
		songService = new SongService(getDataFolder().toPath().resolve("songs"), logger);
		NoteBlockAPI.init();
		NbsEventBridge.register();

		SkriptAddon addon = Skript.registerAddon(this);
		try {
			Class.forName("com.github.hapily04.skriptnbs.elements.Types");
			addon.loadClasses("com.github.hapily04.skriptnbs", "elements");
		} catch (IOException | ClassNotFoundException e) {
			logger.error("An error occurred whilst loading skript-nbs elements: {}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		NoteBlockAPI.shutdown();
	}

	public SongService getSongService() {
		return songService;
	}

	public Logger logger() {
		return logger;
	}

	public static SkriptNbs getInstance() {
		return instance;
	}

}
