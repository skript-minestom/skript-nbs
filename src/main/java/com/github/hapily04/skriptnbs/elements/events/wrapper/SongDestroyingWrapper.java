package com.github.hapily04.skriptnbs.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.registrations.EventValues;
import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

public class SongDestroyingWrapper extends EventWrapper<SongDestroyingEvent> {

	static {
		EventValues.registerEventValue(SongDestroyingWrapper.class, SongPlayer.class, from -> from.getEvent().getSongPlayer());
		EventValues.registerEventValue(SongDestroyingWrapper.class, Song.class, from -> from.getEvent().getSongPlayer().getSong());
	}

	public SongDestroyingWrapper(SongDestroyingEvent event) {
		super(event);
	}

}
