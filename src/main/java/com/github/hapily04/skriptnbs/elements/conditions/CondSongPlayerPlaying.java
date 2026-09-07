package com.github.hapily04.skriptnbs.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

@Name("Song Player Playing")
@Description("Checks whether a song player is currently playing.")
@Examples("""
	if {_radio} is playing:
		broadcast "music on"""")
@Since("1.0.0")
public class CondSongPlayerPlaying extends PropertyCondition<SongPlayer> {

	static {
		register(CondSongPlayerPlaying.class, "playing", "songplayers");
	}

	@Override
	public boolean check(SongPlayer songPlayer) {
		return songPlayer.isPlaying();
	}

	@Override
	protected String getPropertyName() {
		return "playing";
	}

}
