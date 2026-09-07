package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptnbs.SkriptNbs;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("NBS Song From Path")
@Description("Loads an .nbs song from a file path (absolute, cwd-relative, or plugins/skript-nbs/songs/).")
@Examples("""
	set {_song} to nbs song from "demo.nbs"
	play nbs song from "songs/lobby.nbs" to player""")
@Since("1.0.0")
public class ExprNbsSong extends SimpleExpression<Song> {

	static {
		Skript.registerExpression(ExprNbsSong.class, Song.class, ExpressionType.COMBINED,
				"[the] nbs song from %string%");
	}

	private Expression<String> path;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		path = (Expression<String>) expressions[0];
		return true;
	}

	@Override
	protected Song @Nullable [] get(Event event) {
		String p = path.getSingle(event);
		if (p == null) {
			return new Song[0];
		}
		Song song = SkriptNbs.getInstance().getSongService().loadSong(p);
		return song == null ? new Song[0] : new Song[]{song};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Song> getReturnType() {
		return Song.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "nbs song from " + path.toString(event, debug);
	}

}
