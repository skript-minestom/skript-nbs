package com.github.hapily04.skriptnbs.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.registrations.EventValues;
import com.xxmicloxx.NoteBlockAPI.event.SongStoppedEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

public class SongStoppedWrapper extends EventWrapper<SongStoppedEvent> {

	static {
		EventValues.registerEventValue(SongStoppedWrapper.class, SongPlayer.class, from -> from.getEvent().getSongPlayer());
		EventValues.registerEventValue(SongStoppedWrapper.class, Song.class, from -> from.getEvent().getSongPlayer().getSong());
	}

	public SongStoppedWrapper(SongStoppedEvent event) {
		super(event);
	}

}
