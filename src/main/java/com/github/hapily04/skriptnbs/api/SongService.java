package com.github.hapily04.skriptnbs.api;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.RangeSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Loads/caches NBS songs and builds song players for simple and advanced syntax.
 */
public final class SongService {

	private final Path songsFolder;
	private final Logger logger;
	private final Map<String, Song> cache = new ConcurrentHashMap<>();

	public SongService(Path songsFolder, Logger logger) {
		this.songsFolder = songsFolder;
		this.logger = logger;
		try {
			Files.createDirectories(songsFolder);
		} catch (IOException e) {
			logger.warn("Could not create songs folder {}: {}", songsFolder, e.getMessage());
		}
	}

	public Path getSongsFolder() {
		return songsFolder;
	}

	@Nullable
	public Song loadSong(String path) {
		if (path == null || path.isBlank()) {
			return null;
		}
		String key = path.replace('\\', '/');
		Song cached = cache.get(key);
		if (cached != null) {
			return cached;
		}
		Path resolved = resolvePath(path);
		if (resolved == null || !Files.isRegularFile(resolved)) {
			logger.warn("NBS song not found: {}", path);
			return null;
		}
		try {
			Song song = NBSDecoder.parse(resolved);
			cache.put(key, song);
			return song;
		} catch (IOException e) {
			logger.warn("Failed to load NBS song {}: {}", path, e.getMessage());
			return null;
		}
	}

	@Nullable
	public Song resolveSong(@Nullable Object source) {
		if (source instanceof Song song) {
			return song;
		}
		if (source instanceof String path) {
			return loadSong(path);
		}
		return null;
	}

	@Nullable
	private Path resolvePath(String path) {
		Path asIs = Path.of(path);
		if (asIs.isAbsolute() && Files.isRegularFile(asIs)) {
			return asIs;
		}
		Path cwd = Path.of("").toAbsolutePath().resolve(path);
		if (Files.isRegularFile(cwd)) {
			return cwd;
		}
		Path inSongs = songsFolder.resolve(path);
		if (Files.isRegularFile(inSongs)) {
			return inSongs;
		}
		return Files.isRegularFile(asIs) ? asIs : null;
	}

	public RadioSongPlayer createRadio(Song song) {
		return new RadioSongPlayer(song);
	}

	public PositionSongPlayer createPosition(Song song, Point point, @Nullable Instance instance, @Nullable Integer range) {
		PositionSongPlayer player = new PositionSongPlayer(song);
		// Instance may be null until listeners are added; coordinates are still stored.
		player.setTargetLocation(instance, toPos(point));
		if (range != null) {
			player.setDistance(range);
		}
		return player;
	}

	/**
	 * Ensure a position song player has an instance, preferring an explicit one then the first listener.
	 */
	public boolean ensurePositionLocation(PositionSongPlayer player, @Nullable Instance instance, Player... listeners) {
		if (player.getInstance() != null && player.getTargetPosition() != null) {
			return true;
		}
		Pos pos = player.getTargetPosition();
		Instance resolved = instance;
		if (resolved == null) {
			for (Player listener : listeners) {
				if (listener.getInstance() != null) {
					resolved = listener.getInstance();
					break;
				}
			}
		}
		if (resolved == null || pos == null) {
			return false;
		}
		player.setTargetLocation(resolved, pos);
		return true;
	}

	public void applyFadeIn(SongPlayer player, int ticks) {
		if (ticks <= 0) {
			return;
		}
		player.getFadeIn().setType(FadeType.LINEAR);
		player.getFadeIn().setFadeDuration(ticks);
	}

	public void applyFadeOut(SongPlayer player, int ticks) {
		if (ticks <= 0) {
			return;
		}
		player.getFadeOut().setType(FadeType.LINEAR);
		player.getFadeOut().setFadeDuration(ticks);
	}

	public void applyFadeDuration(SongPlayer player, boolean fadeIn, int ticks) {
		if (ticks < 0) {
			return;
		}
		var fade = fadeIn ? player.getFadeIn() : player.getFadeOut();
		fade.setFadeDuration(ticks);
		if (ticks > 0 && fade.getType() == FadeType.NONE) {
			fade.setType(FadeType.LINEAR);
		}
		if (ticks == 0) {
			fade.setType(FadeType.NONE);
		}
	}

	@Nullable
	public SongPlayer playSimple(
			Object songSource,
			Player[] players,
			@Nullable Point point,
			@Nullable Integer range,
			@Nullable Byte volume,
			@Nullable Sound.Source soundSource,
			@Nullable Integer fadeInTicks,
			@Nullable Integer fadeOutTicks,
			boolean repeating
	) {
		Song song = resolveSong(songSource);
		if (song == null || players == null || players.length == 0) {
			return null;
		}

		SongPlayer songPlayer;
		if (point != null) {
			Instance instance = null;
			for (Player player : players) {
				if (player.getInstance() != null) {
					instance = player.getInstance();
					break;
				}
			}
			if (instance == null) {
				logger.warn("Cannot play positional NBS song: no player is in an instance");
				return null;
			}
			PositionSongPlayer psp = new PositionSongPlayer(song);
			psp.setTargetLocation(instance, toPos(point));
			if (range != null) {
				psp.setDistance(range);
			}
			songPlayer = psp;
		} else {
			songPlayer = new RadioSongPlayer(song);
		}

		if (volume != null) {
			songPlayer.setVolume(volume);
		}
		if (soundSource != null) {
			songPlayer.setSoundSource(soundSource);
		}
		if (repeating) {
			songPlayer.setRepeatMode(RepeatMode.ONE);
		}
		if (fadeInTicks != null) {
			applyFadeIn(songPlayer, fadeInTicks);
		}
		if (fadeOutTicks != null) {
			applyFadeOut(songPlayer, fadeOutTicks);
		}

		for (Player player : players) {
			songPlayer.addPlayer(player);
		}

		boolean useFade = fadeInTicks != null && fadeInTicks > 0
				&& songPlayer.getFadeIn().getType() != FadeType.NONE;
		songPlayer.setPlaying(true, useFade);
		return songPlayer;
	}

	public void stopForPlayers(Player[] players) {
		for (Player player : players) {
			NoteBlockAPI.stopPlaying(player);
		}
	}

	public void setPlayingForPlayers(Player[] players, boolean playing, boolean fade) {
		for (Player player : players) {
			ArrayList<SongPlayer> list = NoteBlockAPI.getSongPlayersByPlayer(player);
			if (list == null) {
				continue;
			}
			for (SongPlayer songPlayer : List.copyOf(list)) {
				songPlayer.setPlaying(playing, fade);
			}
		}
	}

	public static Pos toPos(Point point) {
		if (point instanceof Pos pos) {
			return pos;
		}
		return new Pos(point.x(), point.y(), point.z());
	}

	public static void setRange(SongPlayer player, int range) {
		if (player instanceof RangeSongPlayer rangePlayer) {
			rangePlayer.setDistance(range);
		}
	}

	@Nullable
	public static Integer getRange(SongPlayer player) {
		if (player instanceof RangeSongPlayer rangePlayer) {
			return rangePlayer.getDistance();
		}
		return null;
	}

}
