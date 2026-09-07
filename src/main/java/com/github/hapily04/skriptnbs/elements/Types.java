package com.github.hapily04.skriptnbs.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.jetbrains.annotations.Nullable;

public class Types {

	static {
		Classes.registerClass(new ClassInfo<>(Song.class, "nbssong")
				.user("nbs ?songs?")
				.name("NBS Song")
				.description("A Note Block Studio song loaded from an .nbs file.")
				.examples("set {_song} to nbs song from \"demo.nbs\"")
				.since("1.0.0")
				.parser(new Parser<>() {
					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(Song song, int flags) {
						String title = song.getTitle();
						if (title == null || title.isBlank()) {
							return "nbs song";
						}
						return "nbs song \"" + title + "\"";
					}

					@Override
					public String toVariableNameString(Song song) {
						return "nbssong:" + song.getTitle();
					}
				}));

		Classes.registerClass(new ClassInfo<>(SongPlayer.class, "songplayer")
				.user("nbs ?song ?players?", "song ?players?")
				.name("Song Player")
				.description("A NoteBlockAPI song player (radio or position) that can hold listeners and settings.")
				.examples("set {_radio} to a new radio song player for {_song}")
				.since("1.0.0")
				.parser(new Parser<>() {
					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(SongPlayer player, int flags) {
						Song song = player.getSong();
						String title = song == null ? "?" : song.getTitle();
						return "song player (" + title + ")";
					}

					@Override
					public String toVariableNameString(SongPlayer player) {
						return "songplayer:" + System.identityHashCode(player);
					}
				}));

		Classes.registerClass(new EnumClassInfo<>(FadeType.class, "nbsfadetype")
				.user("nbs ?fade ?types?")
				.name("NBS Fade Type")
				.description("Fade curve type for song players: none or linear.")
				.examples("set fade in type of {_radio} to linear")
				.since("1.0.0"));

		Classes.registerClass(new ClassInfo<>(RepeatMode.class, "nbsrepeatmode")
				.user("nbs ?repeat ?modes?")
				.name("NBS Repeat Mode")
				.description("Repeat mode for song players: no/none, one, or all.")
				.examples("set repeat mode of {_radio} to one")
				.since("1.0.0")
				.parser(new Parser<>() {
					@Override
					public @Nullable RepeatMode parse(String s, ParseContext context) {
						String key = s.trim().toLowerCase().replace('_', ' ').replace('-', ' ');
						return switch (key) {
							case "no", "none", "off" -> RepeatMode.NO;
							case "one", "song", "single" -> RepeatMode.ONE;
							case "all", "playlist" -> RepeatMode.ALL;
							default -> null;
						};
					}

					@Override
					public String toString(RepeatMode mode, int flags) {
						return switch (mode) {
							case NO -> "none";
							case ONE -> "one";
							case ALL -> "all";
						};
					}

					@Override
					public String toVariableNameString(RepeatMode mode) {
						return mode.name().toLowerCase();
					}
				}));
	}

}
