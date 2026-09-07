package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptnbs.api.SongService;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Position Song Player Range")
@Description("The hearing range of a position song player.")
@Examples("set range of {_pos} to 32")
@Since("1.0.0")
public class ExprPositionRange extends SimplePropertyExpression<SongPlayer, Number> {

	static {
		register(ExprPositionRange.class, Number.class, "[nbs] [song] [player] (range|distance)", "songplayers");
	}

	@Override
	public @Nullable Number convert(SongPlayer player) {
		return SongService.getRange(player);
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
		int range = ((Number) delta[0]).intValue();
		for (SongPlayer player : getExpr().getArray(event)) {
			SongService.setRange(player, range);
		}
	}

	@Override
	protected String getPropertyName() {
		return "range";
	}

	@Override
	public Class<? extends Number> getReturnType() {
		return Number.class;
	}

}
