package de.synesthesy.processing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.sound.midi.ShortMessage;

import processing.core.PApplet;

import de.synesthesy.Synesthesy;
import de.synesthesy.midi.MidiHandler;
import de.synesthesy.midi.MidiReceiver;
import de.synesthesy.midi.listeners.IMidiListener;
import de.synesthesy.midi.listeners.NoteOffListener;
import de.synesthesy.music.Note.Note;
import de.synesthesy.music.cache.CacheDispatcher;
import de.synesthesy.music.cache.ICache;
import de.synesthesy.music.cache.PerChannel;
import de.synesthesy.music.cache.PressedNotes;

public class PSynesthesy implements IMidiListener {
	private ConsoleHandler ch = new ConsoleHandler();
	private PApplet parent;
	private Synesthesy synesthesy;
	private Method midiMessageReceiver, noteOn, noteOff;
	private PressedNotes pressedNotes;
	private PerChannel  perChannelCache;

	public PSynesthesy(PApplet parent) {
		this.parent = parent;
		ch.setLevel(Level.ALL);
		synesthesy = synesthesy.getInstance();
		
		pressedNotes = new PressedNotes();
		CacheDispatcher.getInstance().registerCache(pressedNotes);
		
		perChannelCache = new PerChannel();
		CacheDispatcher.getInstance().registerCache(perChannelCache);
		
		try {
			noteOn = parent.getClass().getMethod("noteOn",
					new Class[] { Note.class });
		} catch (Exception e) {
		}

		try {
			noteOff = parent.getClass().getMethod("noteOff",
					new Class[] { Note.class });
		} catch (Exception e) {
		}
		try {
			midiMessageReceiver = parent.getClass().getMethod("midiReceive",
					new Class[] { ShortMessage.class, long.class });
		} catch (Exception e) {
		}
		for (MidiReceiver rec : MidiHandler.getInstance().getReceivers()) {
			rec.registerListener(this);
		}
		Logger log = LogManager.getLogManager().getLogger("");
		for (Handler h : log.getHandlers()) {
			h.setLevel(Level.ALL);
		}
	}

	@Override
	public int getFilter() {
		return 0;
	}

	@Override
	public void receiveMessage(ShortMessage sm, long ts) {
		if (midiMessageReceiver != null) {
			try {
				midiMessageReceiver.invoke(parent, sm, ts);
			} catch (Exception e) {
				midiMessageReceiver = null;
			}
		}
		if (noteOn != null && sm.getCommand() == ShortMessage.NOTE_ON){
			try {
				Note nt = new Note(sm.getData1(), sm.getData2(), ts);
				noteOn.invoke(parent, nt);
			} catch (Exception e) {
				noteOn = null;
				e.printStackTrace();
			}
		}
		
		if (noteOff != null && sm.getCommand() == ShortMessage.NOTE_OFF){
			try {
				Note nt = new Note(sm.getData1(), sm.getData2(), ts);
				for (Note pNote : pressedNotes.getCache()){
					if (nt.equals(pNote)){
						noteOff.invoke(parent, sm);
					}
				}
			} catch (Exception e) {
				noteOff = null;
				e.printStackTrace();
			}
		}
		
	}

	public LinkedHashSet<Note> getPressedNotes() {
		return pressedNotes.getCache();
	}

	public PerChannel getPerChannelCache() {
		return perChannelCache;
	}
}
