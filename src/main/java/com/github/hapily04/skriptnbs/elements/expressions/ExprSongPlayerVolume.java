package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Song Player Volume")
@Description("The volume of a song player (0-100).")
@Examples("set volume of {_radio} to 80")
@Since("1.0.0")
public class ExprSongPlayerVolume extends SimplePropertyExpression<SongPlayer, Number> {

	static {
		register(ExprSongPlayerVolume.class, Number.class, "[nbs] [song] [player] volume", "songplayers");
	}

	@Override
	public @Nullable Number convert(SongPlayer player) {
		return player.getVolume();
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(Number.class) : null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET || delta == null || delta.length == 0) {
			return;
		}
		byte volume = ((Number) delta[0]).byteValue();
		for (SongPlayer player : getExpr().getArray(event)) {
			player.setVolume(volume);
		}
	}

	@Override
	protected String getPropertyName() {
		return "volume";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
