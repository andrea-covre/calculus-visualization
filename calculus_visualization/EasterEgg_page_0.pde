void displayPage_0() {
  if (dotBackRefresh) {
      background(0);  //clearing screen
    }
    animateDot();
}


//functions page 0-----------------------------------------------------


void animateDot() {
  colorMode(HSB);
  noStroke();
  for (int i = 0; i < dotNumber; i++) {
    fill(dot[i][5], 255, 255);                            //selecting dot color
    ellipse(dot[i][0], dot[i][1], dot[i][4], dot[i][4]);  //drawing dot

    if (dotLine) {
      stroke(dot[i][5], 255, 255);
      line(dot[i][0], dot[i][1], mouseX, mouseY);
      noStroke();
    }

    //increasing dot position
    dot[i][0] += dot[i][2];
    dot[i][1] += dot[i][3];

    if (dotBounce) {
      //making the dot bounce on the edges
      if ((dot[i][0]<0+dot[i][4]/2)||(dot[i][0]>width-dot[i][4]/2)) {
        dot[i][2] = -dot[i][2];
      }
      if ((dot[i][1]<0+dot[i][4]/2)||(dot[i][1]>height-20-dot[i][4]/2)) {
        dot[i][3] = -dot[i][3];
      }
    }

    //changing dot color
    if (dotColorChange) {
      dot[i][5] += dotColorSpeed;

      //restarting Hue value
      if (dot[i][5]>255) {
        dot[i][5] = dot[i][5] - 255;
      }
    }
  }
  colorMode(RGB);
}
