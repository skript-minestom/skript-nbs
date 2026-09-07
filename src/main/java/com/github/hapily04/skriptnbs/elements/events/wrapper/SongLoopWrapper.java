package com.github.hapily04.skriptnbs.elements.events.wrapper;

import ch.njol.skript.events.wrapper.EventWrapper;
import ch.njol.skript.registrations.EventValues;
import com.xxmicloxx.NoteBlockAPI.event.SongLoopEvent;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.SongPlayer;

public class SongLoopWrapper extends EventWrapper<SongLoopEvent> {

	static {
		EventValues.registerEventValue(SongLoopWrapper.class, SongPlayer.class, from -> from.getEvent().getSongPlayer());
		EventValues.registerEventValue(SongLoopWrapper.class, Song.class, from -> from.getEvent().getSongPlayer().getSong());
	}

	public SongLoopWrapper(SongLoopEvent event) {
		super(event);
	}

}
