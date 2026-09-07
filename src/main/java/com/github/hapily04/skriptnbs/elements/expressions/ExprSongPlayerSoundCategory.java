package com.github.hapily04.skriptnbs.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.util.coll.CollectionUtils;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.kyori.adventure.sound.Sound;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Song Player Sound Category")
@Description("The sound category (source) a song player uses, such as master or records.")
@Examples("""
	set sound category of {_radio} to records
	broadcast "%sound category of {_radio}%""")
@Since("1.0.0")
public class ExprSongPlayerSoundCategory extends SimplePropertyExpression<SongPlayer, Sound.Source> {

	static {
		register(ExprSongPlayerSoundCategory.class, Sound.Source.class,
				"[nbs] [song] [player] sound[ ](category|source)", "songplayers");
	}

	@Override
	public @Nullable Sound.Source convert(SongPlayer player) {
		return player.getSoundSource();
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return mode == ChangeMode.SET ? CollectionUtils.array(Sound.Source.class) : null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (mode != ChangeMode.SET || delta == null || delta.length == 0) {
			return;
		}
		Sound.Source source = (Sound.Source) delta[0];
		for (SongPlayer player : getExpr().getArray(event)) {
			player.setSoundSource(source);
		}
	}

	@Override
	protected String getPropertyName() {
		return "sound category";
	}

	@Override
	public Class<? extends Sound.Source> getReturnType() {
		return Sound.Source.class;
	}

}
