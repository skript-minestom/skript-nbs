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

@Name("Song Player Control")
@Description("Starts, pauses, resumes, stops, or destroys song players. Optional fade uses configured fade in/out.")
@Examples("""
	start {_radio}
	start {_radio} with fade
	pause {_radio} with fade
	destroy {_radio}""")
@Since("1.0.0")
public class EffSongPlayerControl extends Effect {

	static {
		Skript.registerEffect(EffSongPlayerControl.class,
				"start %songplayers% [(fade:with fade)]",
				"pause %songplayers% [(fade:with fade)]",
				"resume %songplayers% [(fade:with fade)]",
				"stop %songplayers% [(fade:with fade)]",
				"destroy %songplayers%");
	}

	private Expression<SongPlayer> songPlayers;
	private int action; // 0 start, 1 pause, 2 resume, 3 stop, 4 destroy
	private boolean fade;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		songPlayers = (Expression<SongPlayer>) expressions[0];
		action = matchedPattern;
		fade = parseResult.hasTag("fade");
		return true;
	}

	@Override
	protected void execute(Event event) {
		for (SongPlayer player : songPlayers.getArray(event)) {
			if (player instanceof PositionSongPlayer psp) {
				Player[] listeners = player.getPlayerUUIDs().stream()
						.map(com.xxmicloxx.NoteBlockAPI.NoteBlockAPI::getOnlinePlayer)
						.filter(p -> p != null)
						.toArray(Player[]::new);
				SkriptNbs.getInstance().getSongService().ensurePositionLocation(psp, null, listeners);
			}
			switch (action) {
				case 0, 2 -> player.setPlaying(true, fade);
				case 1, 3 -> player.setPlaying(false, fade);
				case 4 -> player.destroy();
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String verb = switch (action) {
			case 1 -> "pause";
			case 2 -> "resume";
			case 3 -> "stop";
			case 4 -> "destroy";
			default -> "start";
		};
		return verb + " " + songPlayers.toString(event, debug) + (fade ? " with fade" : "");
	}

}
