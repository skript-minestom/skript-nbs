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
import com.github.hapily04.skriptnbs.api.SongService;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("New Song Player")
@Description("Creates a radio or position song player for an NBS song. Does not start playing until you start it.")
@Examples("""
	set {_radio} to a new radio song player for {_song}
	set {_radio} to a new radio song player for "demo.nbs"
	set {_pos} to a new position song player for {_song} at {_loc} with range 24""")
@Since("1.0.0")
public class ExprNewSongPlayer extends SimpleExpression<SongPlayer> {

	static {
		Skript.registerExpression(ExprNewSongPlayer.class, SongPlayer.class, ExpressionType.COMBINED,
				"[a] [new] radio [nbs] song player (for|of|from) %string/nbssong%",
				"[a] [new] position[al] [nbs] song player (for|of|from) %string/nbssong% at %point% [in %-instance%] [with range %-number%]");
	}

	private Expression<?> songSource;
	@Nullable
	private Expression<Point> point;
	@Nullable
	private Expression<Instance> instance;
	@Nullable
	private Expression<Number> range;
	private boolean position;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		position = matchedPattern == 1;
		songSource = expressions[0];
		if (position) {
			point = (Expression<Point>) expressions[1];
			instance = (Expression<Instance>) expressions[2];
			range = (Expression<Number>) expressions[3];
		}
		return true;
	}

	@Override
	protected SongPlayer @Nullable [] get(Event event) {
		SongService service = SkriptNbs.getInstance().getSongService();
		Song song = service.resolveSong(songSource.getSingle(event));
		if (song == null) {
			return new SongPlayer[0];
		}
		if (!position) {
			RadioSongPlayer radio = service.createRadio(song);
			return new SongPlayer[]{radio};
		}
		Point loc = point == null ? null : point.getSingle(event);
		if (loc == null) {
			return new SongPlayer[0];
		}
		Instance inst = instance == null ? null : instance.getSingle(event);
		Number rangeNum = range == null ? null : range.getSingle(event);
		Integer rangeVal = rangeNum == null ? null : rangeNum.intValue();
		PositionSongPlayer psp = service.createPosition(song, loc, inst, rangeVal);
		return new SongPlayer[]{psp};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends SongPlayer> getReturnType() {
		return SongPlayer.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (!position) {
			return "a new radio song player for " + songSource.toString(event, debug);
		}
		return "a new position song player for " + songSource.toString(event, debug)
				+ " at " + (point == null ? "null" : point.toString(event, debug));
	}

}
