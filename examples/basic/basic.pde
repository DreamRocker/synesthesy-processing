import de.synesthesy.processing.PSynesthesy;
import de.synesthesy.*;
PSynesthesy synesthesy;

 void setup() {
  size(700, 100);
  background(255);
  fill (0,0,0);
  synesthesy = new PSynesthesy(this);
  }

void noteOn(Note nt){
  println(nt);
  println("Pressed:");
  for (Note note : synesthesy.getPressedNotes()){
    print(note+" ");
  }
}

void draw(){
}
  


