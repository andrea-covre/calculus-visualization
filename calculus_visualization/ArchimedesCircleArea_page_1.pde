void displayPage_1() {
  stroke(255, 0, 255);
    strokeWeight(1);
    background(bgColor[0], bgColor[1], bgColor[2]);  //clear all

    pushMatrix();
    translate(width/2, height/2);
    rotate(frameCount / 200.0);

    if (n != 2) {
      polygon(0, 0, r/cos(TWO_PI/(2*n)), n);        //external polygon SPECIAL FORMULA
    }

    strokeWeight(2);
    stroke(255, 0, 0);
    ellipse(0, 0, r*2, r*2);                        //circle of radius r

    strokeWeight(1);
    stroke(255, 0, 255);
    if (n != 2) {
      polygon(0, 0, r, n);                          //internal polygon
    }

    popMatrix(); 

    //input Module
    if (keyPressed) {
      switch(keyCode) {
      case UP:
        r = r + 5;
        break;
      case DOWN:
        r = r - 5;
        break;
      case LEFT:
        if (tn >= 2) {
          tn = tn - 1*sensibility;
        }
        break;
      case RIGHT:
        tn = tn + 1*sensibility;
        break;
      }
      n = round(tn);
    }

    fill(255, 0, 255);
    text("Zoom: " + round((r/R)*100) + "%", 20, 40);
    text("Raggio cerchio: " + fixedR, 20, 60);
    if (n >= 3) {
      areaExt = round(tan(PI/n) * n * fixedR * fixedR * (10000));
      areaInt = round(sin(PI/n) * cos(PI/n) * fixedR * fixedR * n * (10000));
      text("Numero di lati: " + n, 20, 80);
      text("Area poligono circoscritto: " + (areaExt/10000), 20, 100);
      text("Area poligono inscritto: " + (areaInt/10000), 20, 120);
      text("Differenza aree: " + (areaExt - areaInt)/10000, 20, 140);
      //text("Frame rate: " + frameRate, 20, 160);
    }
    noFill();
}


//functions page 1-----------------------------------------------------


void polygon(float x, float y, float radius, int npoints) {
  float angle = TWO_PI / npoints;
  beginShape();
  for (float a = 0; a < TWO_PI; a += angle) {
    float sx = x + cos(a) * radius;
    float sy = y + sin(a) * radius;
    vertex(sx, sy);
  }
  endShape(CLOSE);
}
