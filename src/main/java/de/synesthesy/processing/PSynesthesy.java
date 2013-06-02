package de.synesthesy.processing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.sound.midi.ShortMessage;

import processing.core.PApplet;

import de.synesthesy.midi.listeners.IMidiListener;
import de.synesthesy.midi.listeners.NoteOffListener;
import de.synesthesy.music.Note.Note;
import de.synesthesy.music.cache.ICache;

public class PSynesthesy implements IMidiListener, ICache{
	
	PApplet parent;
	Method midiMessageReceiver, noteOn, noteOff;

	PSynesthesy(PApplet parent){
		this.parent = parent;
		try {
			noteOn = parent.getClass().getMethod("noteOn", new Class[] { Note.class});
		} catch(Exception e) {
		}
		
		try {
			noteOff = parent.getClass().getMethod("noteOff", new Class[] { Note.class });
		} catch(Exception e) {
		}
		try {
			midiMessageReceiver = parent.getClass().getMethod("midiReceive", new Class[] { ShortMessage.class, long.class });
		} catch(Exception e) {
		}
	}
	@Override
	public int getFilter() {
		return 0;
	}

	@Override
	public void receiveMessage(ShortMessage arg0, long arg1) {
		try {
			midiMessageReceiver.invoke(arg0, arg1);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			midiMessageReceiver = null;
		}
	}
	
	@Override
	public void addNote(Note arg0, int arg1) {
		try {
			noteOn.invoke(arg0, arg1);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			noteOn=null;
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
		try {
			noteOff.invoke(arg0, arg1);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			noteOff = null;
		}
		return true;
	}
	

}
