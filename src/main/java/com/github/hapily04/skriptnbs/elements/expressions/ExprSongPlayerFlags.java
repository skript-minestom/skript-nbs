package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Song Player Flags")
@Description("Boolean flags on a song player: auto destroy, fake stereo, and 10 octave.")
@Examples("""
	set auto destroy of {_radio} to true
	set fake stereo of {_radio} to true
	set 10 octave of {_radio} to true""")
@Since("1.0.0")
public class ExprSongPlayerFlags extends SimpleExpression<Boolean> {

	static {
		Skript.registerExpression(ExprSongPlayerFlags.class, Boolean.class, ExpressionType.PROPERTY,
				"[the] auto destroy of %songplayers%",
				"%songplayers%'[s] auto destroy",
				"[the] fake stereo of %songplayers%",
				"%songplayers%'[s] fake stereo",
				"[the] 10 octave of %songplayers%",
				"%songplayers%'[s] 10 octave");
	}

	private Expression<SongPlayer> songPlayers;
	private int flag; // 0 autoDestroy, 1 fakeStereo, 2 tenOctave

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		songPlayers = (Expression<SongPlayer>) expressions[0];
		flag = matchedPattern / 2;
		return true;
	}

	@Override
	protected Boolean @Nullable [] get(Event event) {
		SongPlayer[] players = songPlayers.getArray(event);
		Boolean[] out = new Boolean[players.length];
		for (int i = 0; i < players.length; i++) {
			out[i] = switch (flag) {
				case 0 -> players[i].getAutoDestroy();
				case 1 -> players[i].isFakeStereo();
				default -> players[i].isEnable10Octave();
			};
		}
		return out;
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(Boolean.class) : null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET || delta == null || delta.length == 0) {
			return;
		}
		boolean value = (Boolean) delta[0];
		for (SongPlayer player : songPlayers.getArray(event)) {
			switch (flag) {
				case 0 -> player.setAutoDestroy(value);
				case 1 -> player.setFakeStereo(value);
				default -> player.setEnable10Octave(value);
			}
		}
	}

	@Override
	public boolean isSingle() {
		return songPlayers.isSingle();
	}

	@Override
	public Class<? extends Boolean> getReturnType() {
		return Boolean.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String name = switch (flag) {
			case 0 -> "auto destroy";
			case 1 -> "fake stereo";
			default -> "10 octave";
		};
		return name + " of " + songPlayers.toString(event, debug);
	}

}
