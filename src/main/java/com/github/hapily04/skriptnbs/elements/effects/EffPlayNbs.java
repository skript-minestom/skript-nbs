package com.github.hapily04.skriptnbs.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptminestom.util.NumberUtils;
import com.github.hapily04.skriptnbs.SkriptNbs;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Play NBS Song")
@Description("Plays an NBS song to players as radio or at a location. Optional volume (0-100), sound category, fade in/out, and repeating.")
@Examples("""
	play nbs song "demo.nbs" to player
	play {_song} at {_loc} with range 32 to all players
	play {_song} with volume 80 in records with fade in 2 seconds repeating to player""")
@Since("1.0.0")
public class EffPlayNbs extends Effect {

	static {
		Skript.registerEffect(EffPlayNbs.class,
				"play [nbs] [song] %string/nbssong% [at %-point%] [with range %-number%] [with volume %-number%] [(in|from) %-soundcategory%] with fade in %timespan% and fade out %timespan% [:repeating] (to|for) %players%",
				"play [nbs] [song] %string/nbssong% [at %-point%] [with range %-number%] [with volume %-number%] [(in|from) %-soundcategory%] with fade in %timespan% [:repeating] (to|for) %players%",
				"play [nbs] [song] %string/nbssong% [at %-point%] [with range %-number%] [with volume %-number%] [(in|from) %-soundcategory%] with fade out %timespan% [:repeating] (to|for) %players%",
				"play [nbs] [song] %string/nbssong% [at %-point%] [with range %-number%] [with volume %-number%] [(in|from) %-soundcategory%] [:repeating] (to|for) %players%");
	}

	private Expression<?> songSource;
	@Nullable
	private Expression<Point> point;
	@Nullable
	private Expression<Number> range;
	@Nullable
	private Expression<Number> volume;
	@Nullable
	private Expression<Sound.Source> category;
	@Nullable
	private Expression<Timespan> fadeIn;
	@Nullable
	private Expression<Timespan> fadeOut;
	private Expression<Player> players;
	private boolean repeating;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		repeating = parseResult.hasTag("repeating");
		songSource = expressions[0];
		point = (Expression<Point>) expressions[1];
		range = (Expression<Number>) expressions[2];
		volume = (Expression<Number>) expressions[3];
		category = (Expression<Sound.Source>) expressions[4];

		switch (matchedPattern) {
			case 0 -> {
				fadeIn = (Expression<Timespan>) expressions[5];
				fadeOut = (Expression<Timespan>) expressions[6];
				players = (Expression<Player>) expressions[7];
			}
			case 1 -> {
				fadeIn = (Expression<Timespan>) expressions[5];
				players = (Expression<Player>) expressions[6];
			}
			case 2 -> {
				fadeOut = (Expression<Timespan>) expressions[5];
				players = (Expression<Player>) expressions[6];
			}
			default -> players = (Expression<Player>) expressions[5];
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		Object source = songSource.getSingle(event);
		Player[] targets = players.getArray(event);
		Point loc = point == null ? null : point.getSingle(event);
		Number rangeNum = range == null ? null : range.getSingle(event);
		Number volumeNum = volume == null ? null : volume.getSingle(event);
		Sound.Source sourceCategory = category == null ? null : category.getSingle(event);
		Timespan fadeInSpan = fadeIn == null ? null : fadeIn.getSingle(event);
		Timespan fadeOutSpan = fadeOut == null ? null : fadeOut.getSingle(event);
		Integer rangeVal = rangeNum == null ? null : rangeNum.intValue();
		Byte volumeVal = volumeNum == null ? null : volumeNum.byteValue();
		Integer fadeInTicks = fadeInSpan == null ? null : (int) NumberUtils.ticksFrom(fadeInSpan);
		Integer fadeOutTicks = fadeOutSpan == null ? null : (int) NumberUtils.ticksFrom(fadeOutSpan);

		SkriptNbs.getInstance().getSongService().playSimple(
				source,
				targets,
				loc,
				rangeVal,
				volumeVal,
				sourceCategory,
				fadeInTicks,
				fadeOutTicks,
				repeating
		);
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		StringBuilder sb = new StringBuilder("play ").append(songSource.toString(event, debug));
		if (point != null) {
			sb.append(" at ").append(point.toString(event, debug));
		}
		if (range != null) {
			sb.append(" with range ").append(range.toString(event, debug));
		}
		if (volume != null) {
			sb.append(" with volume ").append(volume.toString(event, debug));
		}
		if (category != null) {
			sb.append(" in ").append(category.toString(event, debug));
		}
		if (fadeIn != null) {
			sb.append(" with fade in ").append(fadeIn.toString(event, debug));
		}
		if (fadeOut != null) {
			sb.append(fadeIn != null ? " and fade out " : " with fade out ").append(fadeOut.toString(event, debug));
		}
		if (repeating) {
			sb.append(" repeating");
		}
		sb.append(" to ").append(players.toString(event, debug));
		return sb.toString();
	}

}
