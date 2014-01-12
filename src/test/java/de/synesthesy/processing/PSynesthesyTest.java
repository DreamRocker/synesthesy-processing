package de.synesthesy.processing;

import java.util.Set;

import processing.core.PApplet;
import de.synesthesy.Synesthesy;
import de.synesthesy.music.Note.Note;
import de.synesthesy.music.cache.CacheDispatcher;
import junit.framework.TestCase;

public class PSynesthesyTest extends TestCase {

	public void testPSynesthesy() {
		PSynesthesy syn = new PSynesthesy(new PApplet());
	}

	public void testGetPressedNotes() {
		PSynesthesy syn = new PSynesthesy(new PApplet());
		Note nt = new Note(0,128,0);
		CacheDispatcher.getInstance().add(nt, 0);
		this.assertEquals(1, syn.getPressedNotes().size());
		nt.registerRelease(1);
		CacheDispatcher.getInstance().update(nt, 0);
		this.assertEquals(0, syn.getPressedNotes().size());
	}
	
	public void testGetPressedNotesConcurent() {
		PSynesthesy syn = new PSynesthesy(new PApplet());
		Note r_nt = new Note(0,128,0);
		CacheDispatcher.getInstance().add(new Note(1,0,0), 0);
		CacheDispatcher.getInstance().add(new Note(2,0,0), 0);
		CacheDispatcher.getInstance().add(new Note(3,0,0), 0);
		CacheDispatcher.getInstance().add(r_nt, 0);
		
		Set<Note> set = syn.getPressedNotes();
		this.assertEquals(4, set.size());
		for (Note n : syn.getPressedNotes()){
			r_nt.registerRelease(1);
			CacheDispatcher.getInstance().update(r_nt, 0);
			}
	}
}
