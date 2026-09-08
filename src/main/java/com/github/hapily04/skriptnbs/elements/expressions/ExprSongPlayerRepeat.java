package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Song Player Repeat Mode")
@Description("The repeat mode of a song player (none, one, or all).")
@Examples("set song repeat mode of {_radio} to one")
@Since("1.0.0")
public class ExprSongPlayerRepeat extends SimplePropertyExpression<SongPlayer, RepeatMode> {

	static {
		register(ExprSongPlayerRepeat.class, RepeatMode.class, "[nbs] song [player] repeat[ ]mode", "songplayers");
	}

	@Override
	public @Nullable RepeatMode convert(SongPlayer player) {
		return player.getRepeatMode();
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(RepeatMode.class) : null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET || delta == null || delta.length == 0) {
			return;
		}
		RepeatMode repeatMode = (RepeatMode) delta[0];
		for (SongPlayer player : getExpr().getArray(event)) {
			player.setRepeatMode(repeatMode);
		}
	}

	@Override
	protected String getPropertyName() {
		return "song repeat mode";
	}

	@Override
	public Class<? extends RepeatMode> getReturnType() {
		return RepeatMode.class;
	}

}
