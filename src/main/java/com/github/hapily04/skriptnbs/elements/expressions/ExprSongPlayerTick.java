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

@Name("Song Player Tick")
@Description("The current playback tick of a song player.")
@Examples("set tick of {_radio} to 0")
@Since("1.0.0")
public class ExprSongPlayerTick extends SimplePropertyExpression<SongPlayer, Number> {

	static {
		register(ExprSongPlayerTick.class, Number.class, "[nbs] [song] [player] tick", "songplayers");
	}

	@Override
	public @Nullable Number convert(SongPlayer player) {
		return player.getTick();
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
		short tick = ((Number) delta[0]).shortValue();
		for (SongPlayer player : getExpr().getArray(event)) {
			player.setTick(tick);
		}
	}

	@Override
	protected String getPropertyName() {
		return "tick";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
