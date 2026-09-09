package com.github.hapily04.skriptnbs;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.github.hapily04.skriptnbs.api.NbsEventBridge;
import com.github.hapily04.skriptnbs.api.SongService;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SkriptNbs extends JavaPlugin {

	private static SkriptNbs instance;

	private SongService songService;

	@Override
	public void onEnable() {
		instance = this;
		songService = new SongService(getDataFolder().toPath().resolve("songs"), getLogger());
		NoteBlockAPI.init();
		NbsEventBridge.register();

		SkriptAddon addon = Skript.registerAddon(this);
		try {
			Class.forName("com.github.hapily04.skriptnbs.elements.Types");
			addon.loadClasses("com.github.hapily04.skriptnbs", "elements");
		} catch (IOException | ClassNotFoundException e) {
			getLogger().severe("An error occurred whilst loading skript-nbs elements: " + e.getMessage());
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

	public static SkriptNbs getInstance() {
		return instance;
	}

}
