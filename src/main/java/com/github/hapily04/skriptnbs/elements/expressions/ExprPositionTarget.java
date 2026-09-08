package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptnbs.api.SongService;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Position Song Player Target")
@Description("The target location of a position song player. Setting keeps the existing instance when possible.")
@Examples("set song target location of {_pos} to {_loc}")
@Since("1.0.0")
public class ExprPositionTarget extends SimplePropertyExpression<SongPlayer, Point> {

	static {
		register(ExprPositionTarget.class, Point.class, "[nbs] song [player] target[ ]location", "songplayers");
	}

	@Override
	public @Nullable Point convert(SongPlayer player) {
		if (player instanceof PositionSongPlayer psp) {
			return psp.getTargetPosition();
		}
		return null;
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(Point.class) : null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET || delta == null || delta.length == 0) {
			return;
		}
		Point point = (Point) delta[0];
		Pos pos = SongService.toPos(point);
		for (SongPlayer player : getExpr().getArray(event)) {
			if (!(player instanceof PositionSongPlayer psp)) {
				continue;
			}
			Instance instance = psp.getInstance();
			psp.setTargetLocation(instance, pos);
		}
	}

	@Override
	protected String getPropertyName() {
		return "song target location";
	}

	@Override
	public Class<? extends Point> getReturnType() {
		return Point.class;
	}

}
