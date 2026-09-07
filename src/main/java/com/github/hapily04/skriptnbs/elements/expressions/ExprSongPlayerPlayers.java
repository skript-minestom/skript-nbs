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
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Name("Players of Song Player")
@Description("The online players currently listening to a song player.")
@Examples("broadcast \"%players of {_radio}%\"")
@Since("1.0.0")
public class ExprSongPlayerPlayers extends SimpleExpression<Player> {

	static {
		Skript.registerExpression(ExprSongPlayerPlayers.class, Player.class, ExpressionType.PROPERTY,
				"[the] players of %songplayers%",
				"%songplayers%'[s] players");
	}

	private Expression<SongPlayer> songPlayers;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		songPlayers = (Expression<SongPlayer>) expressions[0];
		return true;
	}

	@Override
	protected Player @Nullable [] get(Event event) {
		List<Player> players = new ArrayList<>();
		for (SongPlayer songPlayer : songPlayers.getArray(event)) {
			for (UUID uuid : songPlayer.getPlayerUUIDs()) {
				Player player = NoteBlockAPI.getOnlinePlayer(uuid);
				if (player != null) {
					players.add(player);
				}
			}
		}
		return players.toArray(Player[]::new);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Player> getReturnType() {
		return Player.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "players of " + songPlayers.toString(event, debug);
	}

}
