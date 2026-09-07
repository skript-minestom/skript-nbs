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
import com.xxmicloxx.NoteBlockAPI.songplayer.PositionSongPlayer;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Song Player Listeners")
@Description("Adds or removes players from a song player.")
@Examples("""
	add player to {_radio}
	add {_players::*} to {_radio}
	remove player from {_radio}""")
@Since("1.0.0")
public class EffSongPlayerListeners extends Effect {

	static {
		Skript.registerEffect(EffSongPlayerListeners.class,
				"add %players% to %songplayers%",
				"remove %players% from %songplayers%");
	}

	private Expression<Player> players;
	private Expression<SongPlayer> songPlayers;
	private boolean add;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		players = (Expression<Player>) expressions[0];
		songPlayers = (Expression<SongPlayer>) expressions[1];
		add = matchedPattern == 0;
		return true;
	}

	@Override
	protected void execute(Event event) {
		Player[] targets = players.getArray(event);
		for (SongPlayer songPlayer : songPlayers.getArray(event)) {
			if (add) {
				if (songPlayer instanceof PositionSongPlayer psp) {
					SkriptNbs.getInstance().getSongService().ensurePositionLocation(psp, null, targets);
				}
				for (Player player : targets) {
					songPlayer.addPlayer(player);
				}
			} else {
				for (Player player : targets) {
					songPlayer.removePlayer(player);
				}
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (add) {
			return "add " + players.toString(event, debug) + " to " + songPlayers.toString(event, debug);
		}
		return "remove " + players.toString(event, debug) + " from " + songPlayers.toString(event, debug);
	}

}
