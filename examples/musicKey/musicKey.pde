import de.synesthesy.processing.PSynesthesy;
import de.synesthesy.*;
import java.util.Vector;
PSynesthesy pSynesthesy;

 void setup() {
  size(700, 100);
  background(255);
  fill (0,0,0);
  pSynesthesy = new PSynesthesy(this);
  }

void noteOn(Note nt){
  println(nt);
  println("Pressed:");
  if (pSynesthesy.getPressedNotes().size()>=3){
    MusicKeyAnalyzer keyAn = new MusicKeyAnalyzer();
    MusicKey mKey = keyAn.getMusicKey(new Vector<Note>(pSynesthesy.getPressedNotes()));
    println ("Music key: "+mKey.toString());
  }
  for (Note note : pSynesthesy.getPressedNotes()){
    print(note+" ");
  }
}

void draw(){
}
  


