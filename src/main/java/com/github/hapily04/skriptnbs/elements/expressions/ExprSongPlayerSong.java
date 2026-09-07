package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.jetbrains.annotations.Nullable;

@Name("Song of Song Player")
@Description("The NBS song currently associated with a song player.")
@Examples("set {_song} to song of {_radio}")
@Since("1.0.0")
public class ExprSongPlayerSong extends SimplePropertyExpression<SongPlayer, Song> {

	static {
		register(ExprSongPlayerSong.class, Song.class, "[nbs] song", "songplayers");
	}

	@Override
	public @Nullable Song convert(SongPlayer player) {
		return player.getSong();
	}

	@Override
	protected String getPropertyName() {
		return "song";
	}

	@Override
	public Class<? extends Song> getReturnType() {
		return Song.class;
	}

}
