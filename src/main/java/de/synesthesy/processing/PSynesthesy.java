package de.synesthesy.processing;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedHashSet;
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
import de.synesthesy.music.Note.Note;
import de.synesthesy.music.cache.CacheDispatcher;
import de.synesthesy.music.cache.PerChannel;
import de.synesthesy.music.cache.PressedNotes;

public class PSynesthesy implements IMidiListener {
	private static final Logger log = Logger.getLogger(PSynesthesy.class.
			getName());
	private PApplet parent;
	private Synesthesy synesthesy;
	private Method midiMessageReceiver, noteOn, noteOff;
	private PressedNotes pressedNotes;
	private PerChannel  perChannelCache;

	public PSynesthesy(PApplet parent) {
		this.parent = parent;
		log.fine("Starting PSynestesy..");

		synesthesy = synesthesy.getInstance();
		pressedNotes = new PressedNotes();
		CacheDispatcher.getInstance().registerCache(pressedNotes);
		
		log.finer("Searching noteOn function");
		try {
			noteOn = parent.getClass().getMethod("noteOn",
					new Class[] { Note.class });
		} catch (Exception e) {
			log.fine("noteOn() not found");
		}
		
		log.finer("Searching noteOff function");
		try {
			noteOff = parent.getClass().getMethod("noteOff",
					new Class[] { Note.class });
		} catch (Exception e) {
			log.fine("noteOff() not found");
		}
		log.finer("Searching midiReceive function");
		try {
			midiMessageReceiver = parent.getClass().getMethod("midiReceive",
					new Class[] { ShortMessage.class, long.class });
		} catch (Exception e) {
			log.fine("midiReceive() not found");
		}
		for (MidiReceiver rec : MidiHandler.getInstance().getReceivers()) {
			rec.registerListener(this);
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
						noteOff.invoke(parent, pNote);
						return;
					}
				}
				noteOff.invoke(parent, nt);
			} catch (Exception e) {
				System.out.println(e);
				noteOff = null;
				e.printStackTrace();
			}
		}
		
	}

	public LinkedHashSet<Note> getPressedNotes() {
		return new LinkedHashSet<Note>(pressedNotes.getCache());
	}

	public PerChannel getPerChannelCache() {
		return perChannelCache;
	}
	/**
	 * Sets the logging for all synesthesy classes
	 * 
	 * @param level
	 */
	public void setLogging(Level level){
		Enumeration<String> a = LogManager.getLogManager().getLoggerNames();
		for (;a.hasMoreElements();){
			String name = a.nextElement();
			if (name.contains("de.synesthesy")){
				Logger cu = LogManager.getLogManager().getLogger(name);
				cu.setLevel(level);
			}
		}
		for (Handler h : LogManager.getLogManager().getLogger("").getHandlers()) {
		h.setLevel(level);
		}
	}

	/**
	 * Activates debug mode
	 */
	public void debugMode(){
		this.setLogging(Level.ALL);
	}
}
