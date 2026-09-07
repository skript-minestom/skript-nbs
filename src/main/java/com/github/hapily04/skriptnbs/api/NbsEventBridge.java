package com.github.hapily04.skriptnbs.api;

import com.github.hapily04.skriptnbs.elements.events.wrapper.PlayerRangeStateChangeWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongDestroyingWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongEndWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongLoopWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongNextWrapper;
import com.github.hapily04.skriptnbs.elements.events.wrapper.SongStoppedWrapper;
import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.event.PlayerRangeStateChangeEvent;
import com.xxmicloxx.NoteBlockAPI.event.SongDestroyingEvent;
import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import com.xxmicloxx.NoteBlockAPI.event.SongLoopEvent;
import com.xxmicloxx.NoteBlockAPI.event.SongNextEvent;
import com.xxmicloxx.NoteBlockAPI.event.SongStoppedEvent;
import org.bukkit.Bukkit;

/**
 * Forwards NoteBlockAPI Minestom events into Skript via EventWrappers.
 * Required because NoteBlockAPI fires on its own event node, not Skript's.
 */
public final class NbsEventBridge {

	private NbsEventBridge() {
	}

	public static void register() {
		var node = NoteBlockAPI.getEventNode();
		var plugins = Bukkit.getPluginManager();

		node.addListener(SongEndEvent.class, event ->
				plugins.callEvent(new SongEndWrapper(event)));
		node.addListener(SongStoppedEvent.class, event ->
				plugins.callEvent(new SongStoppedWrapper(event)));
		node.addListener(SongDestroyingEvent.class, event ->
				plugins.callEvent(new SongDestroyingWrapper(event)));
		node.addListener(SongLoopEvent.class, event ->
				plugins.callEvent(new SongLoopWrapper(event)));
		node.addListener(SongNextEvent.class, event ->
				plugins.callEvent(new SongNextWrapper(event)));
		node.addListener(PlayerRangeStateChangeEvent.class, event ->
				plugins.callEvent(new PlayerRangeStateChangeWrapper(event)));
	}

}
