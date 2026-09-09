package com.github.hapily04.skriptnbs.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.util.SimpleEvent;
import com.github.hapily04.skriptnbs.elements.events.wrapper.PlayerRangeStateChangeWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongDestroyingWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongEndWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongLoopWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongNextWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongStoppedWrapper;

@Name("NBS Song Events")
@Description("""
	NoteBlockAPI song lifecycle events.
	Song destroy and song loop can be cancelled.""")
@Examples("""
	on nbs song end:
		broadcast "Song ended: %title of event-nbssong%"

	on nbs song loop:
		if volume of event-songplayer < 0.5:
			cancel event

	on nbs player range change:
		if event-in-range is true:
			send "You can hear the music" to event-player""")
@Since("1.0.0")
public class SimpleNbsEvents extends SimpleEvent {

	static {
		Skript.registerEvent("NBS Song End", SimpleEvent.class, SongEndWrapper.class,
				"[nbs] song end[ed]");

		Skript.registerEvent("NBS Song Stop", SimpleEvent.class, SongStoppedWrapper.class,
				"[nbs] song stop[ped]");

		Skript.registerEvent("NBS Song Destroy", SimpleEvent.class, SongDestroyingWrapper.class,
				"[nbs] song destroy[ing]");

		Skript.registerEvent("NBS Song Loop", SimpleEvent.class, SongLoopWrapper.class,
				"[nbs] song loop[ing]");

		Skript.registerEvent("NBS Song Next", SimpleEvent.class, SongNextWrapper.class,
				"[nbs] song next");

		Skript.registerEvent("NBS Player Range Change", SimpleEvent.class, PlayerRangeStateChangeWrapper.class,
				"[nbs] [player] range [state] change");
	}

}
