package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.jetbrains.annotations.Nullable;

@Name("NBS Song Author")
@Description("The author of an NBS song.")
@Examples("broadcast author of {_song}")
@Since("1.0.0")
public class ExprSongAuthor extends SimplePropertyExpression<Song, String> {

	static {
		register(ExprSongAuthor.class, String.class, "[nbs] [song] author", "nbssongs");
	}

	@Override
	public @Nullable String convert(Song song) {
		return song.getAuthor();
	}

	@Override
	protected String getPropertyName() {
		return "author";
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

}
