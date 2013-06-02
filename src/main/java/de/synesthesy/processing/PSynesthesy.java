package de.synesthesy.processing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

public class PSynesthesy implements IMidiListener, ICache {
	ConsoleHandler ch = new ConsoleHandler();
	PApplet parent;
	Synesthesy synesthesy;
	Method midiMessageReceiver, noteOn, noteOff;

	public PSynesthesy(PApplet parent) {
		this.parent = parent;
		ch.setLevel(Level.ALL);
		synesthesy = synesthesy.getInstance();
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
		CacheDispatcher.getInstance().registerCache(this);
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
	public void receiveMessage(ShortMessage arg0, long arg1) {
		if (midiMessageReceiver != null) {
			try {
				midiMessageReceiver.invoke(parent, arg0, arg1);
			} catch (Exception e) {
				midiMessageReceiver = null;
			}
		}
	}

	@Override
	public void addNote(Note arg0, int arg1) {
		if (noteOn != null) {
			try {
				noteOn.invoke(parent, arg0);
			} catch (Exception e) {
				noteOn = null;
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean intressed(Note arg0, int arg1) {
		return true;
	}

	@Override
	public boolean intressedOnUpdate(Note arg0, int arg1) {
		return true;
	}

	@Override
	public boolean update(Note arg0, int arg1) {
		if (noteOff != null) {
			try {
				noteOff.invoke(parent, arg0);
			} catch (Exception e) {
				noteOff = null;
			}
		}
		return false;
	}
}
