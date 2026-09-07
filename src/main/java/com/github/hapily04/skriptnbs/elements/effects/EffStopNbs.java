package com.github.hapily04.skriptnbs.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.github.hapily04.skriptnbs.SkriptNbs;
import net.minestom.server.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Stop/Pause/Resume NBS Songs")
@Description("Stops, pauses, or resumes all song players for the given players. Optional fade uses each player's fade in/out settings.")
@Examples("""
	stop nbs songs of player
	pause nbs songs of player with fade
	resume nbs songs of all players""")
@Since("1.0.0")
public class EffStopNbs extends Effect {

	static {
		Skript.registerEffect(EffStopNbs.class,
				"stop [nbs] songs (of|for) %players%",
				"pause [nbs] songs (of|for) %players% [(fade:with fade)]",
				"resume [nbs] songs (of|for) %players% [(fade:with fade)]");
	}

	private Expression<Player> players;
	private int action; // 0 stop, 1 pause, 2 resume
	private boolean fade;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		players = (Expression<Player>) expressions[0];
		action = matchedPattern;
		fade = parseResult.hasTag("fade");
		return true;
	}

	@Override
	protected void execute(Event event) {
		Player[] targets = players.getArray(event);
		var service = SkriptNbs.getInstance().getSongService();
		switch (action) {
			case 0 -> service.stopForPlayers(targets);
			case 1 -> service.setPlayingForPlayers(targets, false, fade);
			case 2 -> service.setPlayingForPlayers(targets, true, fade);
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String verb = switch (action) {
			case 1 -> "pause";
			case 2 -> "resume";
			default -> "stop";
		};
		return verb + " nbs songs of " + players.toString(event, debug) + (fade ? " with fade" : "");
	}

}
