package com.github.hapily04.skriptnbs.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.registrations.EventValues;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;
import net.minestom.server.entity.Player;
import org.skriptlang.skript.bukkit.lang.eventvalue.EventValue;

public class PlayerRangeStateChangeWrapper extends EventWrapper<PlayerRangeStateChangeEvent> {

	static {
		EventValues.registerEventValue(PlayerRangeStateChangeWrapper.class, SongPlayer.class, from -> from.getEvent().getSongPlayer());
		EventValues.registerEventValue(PlayerRangeStateChangeWrapper.class, Song.class, from -> from.getEvent().getSongPlayer().getSong());
		EventValues.registerEventValue(PlayerRangeStateChangeWrapper.class, Player.class, from -> from.getEvent().getPlayer());
		EventValues.registerEventValue(EventValue.builder(PlayerRangeStateChangeWrapper.class, Boolean.class)
				.patterns("in-range")
				.getter(from -> from.getEvent().isInRange())
				.build());
	}

	public PlayerRangeStateChangeWrapper(PlayerRangeStateChangeEvent event) {
		super(event);
	}

}
