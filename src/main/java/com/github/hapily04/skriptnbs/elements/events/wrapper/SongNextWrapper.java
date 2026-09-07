package com.github.hapily04.skriptnbs.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.registrations.EventValues;
import com.xxmicloxx.NoteBlockAPI.event.SongNextEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

public class SongNextWrapper extends EventWrapper<SongNextEvent> {

	static {
		EventValues.registerEventValue(SongNextWrapper.class, SongPlayer.class, from -> from.getEvent().getSongPlayer());
		EventValues.registerEventValue(SongNextWrapper.class, Song.class, from -> from.getEvent().getSongPlayer().getSong());
	}

	public SongNextWrapper(SongNextEvent event) {
		super(event);
	}

}
