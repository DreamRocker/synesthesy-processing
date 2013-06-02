import de.synesthesy.PSynesthesy;
PSynesthesy synesthesy;

 void setup() {
  size(700, 100);
  background(255);
  fill (0,0,0);
  synesthesy = new PSynesthesy(this);
  }

void noteOn(themidibus.Note nt){
  println(nt.getTicks());
  
}

void draw(){
}
  


