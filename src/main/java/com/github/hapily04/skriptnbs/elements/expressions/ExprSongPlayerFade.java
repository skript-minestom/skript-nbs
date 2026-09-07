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
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.github.hapily04.skriptminestom.util.NumberUtils;
import com.github.hapily04.skriptnbs.SkriptNbs;
import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.songplayer.Fade;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Song Player Fade")
@Description("Fade in/out duration or type of a song player. Setting a positive duration enables linear fade if type is none.")
@Examples("""
	set fade in duration of {_radio} to 2 seconds
	set fade out type of {_radio} to linear""")
@Since("1.0.0")
public class ExprSongPlayerFade extends SimpleExpression<Object> {

	static {
		Skript.registerExpression(ExprSongPlayerFade.class, Object.class, ExpressionType.PROPERTY,
				"[the] fade in duration of %songplayers%",
				"%songplayers%'[s] fade in duration",
				"[the] fade out duration of %songplayers%",
				"%songplayers%'[s] fade out duration",
				"[the] fade in type of %songplayers%",
				"%songplayers%'[s] fade in type",
				"[the] fade out type of %songplayers%",
				"%songplayers%'[s] fade out type");
	}

	private Expression<SongPlayer> songPlayers;
	private boolean fadeIn;
	private boolean duration;

	@Override
	@SuppressWarnings("unchecked")
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		songPlayers = (Expression<SongPlayer>) expressions[0];
		fadeIn = matchedPattern == 0 || matchedPattern == 1 || matchedPattern == 4 || matchedPattern == 5;
		duration = matchedPattern <= 3;
		return true;
	}

	@Override
	protected Object @Nullable [] get(Event event) {
		SongPlayer[] players = songPlayers.getArray(event);
		Object[] out = new Object[players.length];
		for (int i = 0; i < players.length; i++) {
			Fade fade = fadeIn ? players[i].getFadeIn() : players[i].getFadeOut();
			out[i] = duration ? NumberUtils.timespanFrom(fade.getFadeDuration()) : fade.getType();
		}
		return out;
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		if (mode != ChangeMode.SET) {
			return null;
		}
		return duration ? CollectionUtils.array(Timespan.class) : CollectionUtils.array(FadeType.class);
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET || delta == null || delta.length == 0) {
			return;
		}
		for (SongPlayer player : songPlayers.getArray(event)) {
			if (duration) {
				int ticks = (int) NumberUtils.ticksFrom((Timespan) delta[0]);
				SkriptNbs.getInstance().getSongService().applyFadeDuration(player, fadeIn, ticks);
			} else {
				Fade fade = fadeIn ? player.getFadeIn() : player.getFadeOut();
				fade.setType((FadeType) delta[0]);
			}
		}
	}

	@Override
	public boolean isSingle() {
		return songPlayers.isSingle();
	}

	@Override
	public Class<?> getReturnType() {
		return duration ? Timespan.class : FadeType.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String which = fadeIn ? "fade in" : "fade out";
		String prop = duration ? "duration" : "type";
		return which + " " + prop + " of " + songPlayers.toString(event, debug);
	}

}
