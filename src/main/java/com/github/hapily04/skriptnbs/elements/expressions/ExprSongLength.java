package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.jetbrains.annotations.Nullable;

@Name("NBS Song Length")
@Description("The length of an NBS song in ticks.")
@Examples("broadcast \"%song length of {_song}%\"")
@Since("1.0.0")
public class ExprSongLength extends SimplePropertyExpression<Song, Number> {

	static {
		register(ExprSongLength.class, Number.class, "[nbs] song length", "nbssongs");
	}

	@Override
	public @Nullable Number convert(Song song) {
		return song.getLength();
	}

	@Override
	protected String getPropertyName() {
		return "song length";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
