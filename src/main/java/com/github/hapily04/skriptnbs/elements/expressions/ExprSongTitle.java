package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.jetbrains.annotations.Nullable;

@Name("NBS Song Title")
@Description("The title of an NBS song.")
@Examples("broadcast title of {_song}")
@Since("1.0.0")
public class ExprSongTitle extends SimplePropertyExpression<Song, String> {

	static {
		register(ExprSongTitle.class, String.class, "[nbs] [song] title", "nbssongs");
	}

	@Override
	public @Nullable String convert(Song song) {
		return song.getTitle();
	}

	@Override
	protected String getPropertyName() {
		return "title";
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

}
