void displayStatusBar() {
  //Informazioni sullo Sketch          -------------======= STATUS BAR =======----------------

  stroke(255, 255, 255, 150);
  fill(0);
  rect(-10, height-20, width+10, 30);   //clear or re-clear just the lower status bar

  fill(255);
  textSize(12);
  text("Calculus Visualization by Andrea Covre", 5, height-5);
  text("Frame rate: " + frameRate, width/2-210, height-5);
  text("Status: ", width/2 -40, height-5); //15
  if (status == "Operative") {
    fill(0, 255, 0);
  } else if (status == "Computing") {
    fill(255, 180, 0);
  } else {
    fill(255, 13, 13);
  }
  text(status, width/2+5, height-5);
  fill(255);
  text("Response Time: " + responseTime/1000, width/2 + 100, height-5);

  //CPU load bar
  text("CPU load", width/2 + 250, height-5);
  strokeWeight(0);
  fill(lerpColor(red, green, frameRate/60));
  rect(width/2 + 305, height-15, 100-100*(frameRate/60), 10);
  noFill();
  strokeWeight(1);
  rect(width/2 + 305, height-15, 100, 10);

  fill(255);
  text("V 1.2.2", width-45, height-5);      //Sketch Version ------
  noFill();
  textSize(16);
}
