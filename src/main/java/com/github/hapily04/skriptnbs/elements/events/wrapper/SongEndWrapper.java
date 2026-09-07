package com.github.hapily04.skriptnbs.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.registrations.EventValues;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

public class SongEndWrapper extends EventWrapper<SongEndEvent> {

	static {
		EventValues.registerEventValue(SongEndWrapper.class, SongPlayer.class, from -> from.getEvent().getSongPlayer());
		EventValues.registerEventValue(SongEndWrapper.class, Song.class, from -> from.getEvent().getSongPlayer().getSong());
	}

	public SongEndWrapper(SongEndEvent event) {
		super(event);
	}

}
