void displayPage_3() {
    cursor(CROSS);

    background(0);    //cleaning screen

    //Choosing which area of the graph show
    switch (graphQuadrant) {
    case "1":
      yTranslation = -70;
      break;

    case "1-4":
      yTranslation = -height/2;
      break;
    }

    pushMatrix();
    translate(xTranslation, yTranslation);

    //Drawing axes
    stroke(255, 0, 0);
    line(0 -20, height, width, height);   //x-axis
    stroke(255, 0, 0);
    line(0, height +500, 0, 0);           //y-axis
    stroke(255);

    //TACS
    //managing the tacs when they overlap etc.
    if (zoom == 2) {
      tacControl = 20;
    } else if (zoom == 4) {
      tacControl = 10;
    } else if ((zoom >= 6)&&(zoom <= 10)) {
      tacControl = 5;
    } else if ((zoom >= 12)&&(zoom <= 16)) {
      tacControl = 2;
    } else if ((zoom >= 18)&&(zoom <= 54)) {
      tacControl = 1;
    } else if ((zoom >= 56)&&(zoom <= 118)) {
      tacControl = 0.5;
    } else if ((zoom >= 120)&&(zoom <= 200)) {
      tacControl = 0.25;
    }

    //Drawing xy values with Tacs
    textSize(10);
    stroke(255, 0, 0);
    if (tacControl < 1) {
      for (float i = 0; i < 25; i += tacControl) {
        line(i*zoom, height +tac, i*zoom, height-tac);              //x tacs
        line(0 -tac, height-i*zoom, 0 +tac, height-i*zoom);         //y tacs

        line(0 -tac, height-(-i)*zoom, 0 +tac, height-(-i)*zoom);   //-y tacs

        text(Float.toString(i), i*zoom -5, height +18);             //x values
        text(Float.toString(i), 0 -23, height-i*zoom +5);           //y values

        if (i != 0) {
          text(Float.toString(-i), 0 -23, height-(-i)*zoom +5);     //-y values
        }
      }
    } else {
      for (int i = 0; i < 105; i += tacControl) {
        line(i*zoom, height +tac, i*zoom, height-tac);              //x tacs
        line(0 -tac, height-i*zoom, 0 +tac, height-i*zoom);         //y tacs
        line(0 -tac, height-(-i)*zoom, 0 +tac, height-(-i)*zoom);   //-y tacs

        text(i, i*zoom -5, height +18);    //x values
        text(i, 0 -25, height-i*zoom +5);  //y values
        if (i != 0) {
          text(-i, 0 -25, height-(-i)*zoom +5);  //-y values
        }
      }
    }

    //Selecting Interval Block
    if ((selecting == "on")||(selecting == "lS")) {
      line( mouseX-xTranslation, height, mouseX-xTranslation, getCanvasY( getY( getGraphX( mouseX )) ) );

      //Managing the selection of the lower and upper bounds
      if (mousePressed)
      {
        if (mouseButton == LEFT) { 
          if (selecting == "on") {
            lowerBound = getGraphX(mouseX);
            selecting = "lS";
          } else if (selecting == "lS") {
            upperBound = getGraphX(mouseX);
            selecting = "selected";
          }
        }
        delay(80);
      }
    } else if (selecting == "off") {
      lowerBound = -100;     //resetting the values of the bounds
      upperBound = -100;
      tn = 1;                  //resetting the value that generates n
    }

    //Showing selection process (while selecting and when done)
    stroke(0, 255, 0);
    if (selecting == "lS") {
      line(getCanvasX(lowerBound), height, getCanvasX(lowerBound), getCanvasY(getY(lowerBound)));
    } else if (selecting == "selected") {
      line(getCanvasX(lowerBound), height, getCanvasX(lowerBound), getCanvasY(getY(lowerBound)));
      line(getCanvasX(upperBound), height, getCanvasX(upperBound), getCanvasY(getY(upperBound)));
    }

    //Bounds processing (correction lower and upper and properly selected
    //switching upper and lower if the upper has a lower x than the lower
    if (selecting == "selected") {
      if (lowerBound > upperBound) {
        float temp = lowerBound;
        lowerBound = upperBound;
        upperBound = temp;
      }

      deltaBound = upperBound - lowerBound;   //calculating difference between upper and lower bound

      //giving a message of error when lowerBound == upperBound
      if (lowerBound == upperBound) {
        closeMessage = messageTime + millis();
        displayMessage = true;
        lowerBound = -99;
        upperBound = -100;
      }
      if (displayMessage == true) {
        fill(80, 200);
        rect(-1000, -1000, 5000, 5000);
        textAlign(CENTER);
        fill(0);
        stroke(255, 0, 0);
        rect(width/2-210 -xTranslation, height/2-30 -7 -yTranslation, 420, 60);
        fill(255);
        textSize(25);
        text("You must select different values!", width/2 -xTranslation, height/2 -yTranslation);
        status = "Halted";
        if (closeMessage <= millis()) {     //after presetted time
          displayMessage = false;           //hide message
          selecting = "off";
          status = "Operative";
        }
      }
    }
    textAlign(LEFT);
    textSize(12);

    stroke(255);

    if (showCursorXY) {
      text("X: " + getGraphX(mouseX), mouseX-xTranslation, mouseY-yTranslation+20);
      text("Y: " + getGraphY(mouseY), mouseX-xTranslation, mouseY-yTranslation+40);
    }

    //Resetting the area values of low and high Integration
    lowIntegrationValue = 0;
    highIntegrationValue = 0;

    //deciding whether draw highI or lowI or both based on User Inputs
    //drawing the selected Integration
    stroke(0, 255, 0);
    if (selecting == "selected") {
      if (drawLowI) {
        fill(0, 255, 0, 70);
        drawLowIntegration();
      }
      if (drawHighI) {
        //fill(100, 100, 255, 30);  
        noFill();
        drawHighIntegration();
      }
    }
    noFill();

    //drawing a Vectorial Graph of the function [graphType]
    drawGraph();  

    popMatrix();

    //Box with informations
    fill(0, 180);
    textSize(15);
    //different boxes and data for each selecting case
    if (selecting == "off") {
      rect(width-320, 20, 300, 50);
      fill(255);
      text("Graph Type: " + graphType, width-320+5, 35);
      text("Equation: " + fxEquation, width-320+5, 50);
      text("Interval: Not selected", width-320+5, 65);
      
    } else if (selecting == "on") {
      rect(width-320, 20, 300, 65);
      fill(255);
      text("Graph Type: " + graphType, width-320+5, 35);
      text("Equation: " + fxEquation, width-320+5, 50);
      text("Interval: Select lower bound", width-320+5, 65);
      text("Lower bound: " + getGraphX(mouseX), width-320+5, 80);

    } else if (selecting == "lS") {
      rect(width-320, 20, 300, 95);
      fill(255);
      text("Graph Type: " + graphType, width-320+5, 35);
      text("Equation: " + fxEquation, width-320+5, 50);
      text("Interval: Select upper bound", width-320+5, 65);
      text("Lower bound: " + lowerBound, width-320+5, 80);
      text("Upper bound: " + getGraphX(mouseX), width-320+5, 95);
      text("Δ = " + (getGraphX(mouseX)-lowerBound), width-320+5, 110);

    } else if ((selecting == "selected")&&(-99 != upperBound)&&(lowerBound != upperBound)) {   //additional stuff to not display data when a=b

      if ((drawHighI == false)&&(drawLowI == false)) {
        rect(width-320, 20, 300, 80);
        fill(255);

        text("Graph Type: " + graphType, width-320+5, 35);
        text("Equation: " + fxEquation, width-320+5, 50);
        text("Interval: [ " + lowerBound + " ; " + upperBound + " ]", width-320+5, 65);
        text("Δ = " + (upperBound-lowerBound), width-320+5, 80);
        text("Number of blocks: " + n, width-320+5, 95);
        
      } else if ((drawHighI == false)||(drawLowI == false)) {
        rect(width-320, 20, 300, 95);
        fill(255);

        text("Graph Type: " + graphType, width-320+5, 35);
        text("Equation: " + fxEquation, width-320+5, 50);
        text("Interval: [ " + lowerBound + " ; " + upperBound + " ]", width-320+5, 65);
        text("Δ = " + (upperBound-lowerBound), width-320+5, 80);
        text("Number of blocks: " + n, width-320+5, 95);

        if (drawLowI == false) {
          text("Higher blocks' area: " + highIntegrationValue, width-320+5, 110);
        } 
        if (drawHighI == false) {
          text("Lower blocks' area: " + lowIntegrationValue, width-320+5, 110);
        }
        
      } else if ((drawHighI == true)&&(drawLowI == true)) {

        rect(width-320, 20, 300, 125);
        fill(255);
        text("Graph Type: " + graphType, width-320+5, 35);
        text("Equation: " + fxEquation, width-320+5, 50);
        text("Interval: [ " + lowerBound + " ; " + upperBound + " ]", width-320+5, 65);
        text("Δ = " + (upperBound-lowerBound), width-320+5, 80);
        text("Number of blocks: " + n, width-320+5, 95);
        text("Lower blocks' area: " + lowIntegrationValue, width-320+5, 110);
        text("Higher blocks' area: " + highIntegrationValue, width-320+5, 125);
        text("Areas difference: " + (highIntegrationValue-lowIntegrationValue), width-320+5, 140);
      }
    }

    //Scheme box information
    //------------------------
    //35 Graph Type
    //50 Equation
    //65 Interval
    //n value
    //80 lowI area
    //95 highI area
    //110 highI lowI area diff

    if (scrolling == true) {
      if ((wheelAccumulator > 0)&&(functionIndex < totalFunctions)) {
        functionIndex += +1;
        selecting = "off";
      } else if ((wheelAccumulator < 0)&&(functionIndex > 1)) {
        functionIndex += -1;
        selecting = "off";
      }
      delay(80);
    }

    //selecting the function from its index value and setting the propieties
    switch(functionIndex) {
    case 1:
      graphType = "linear";
      graphQuadrant = "1";
      graphDensity = 1;
      graphStartingPoint = 0 - graphDensity ;
      fxEquation = "y = x";
      break;

    case 2:
      graphType = "parabola";
      graphQuadrant = "1";
      graphDensity = 0.5;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = x^2";
      break;

    case 3:
      graphType = "ln";
      graphQuadrant = "1";
      graphDensity = 0.2;
      graphStartingPoint = 0.1;
      fxEquation = "y = ln(x)";
      break;

    case 4:
      graphType = "sin";
      graphQuadrant = "1-4";
      graphDensity = 0.25;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = sin(x)";
      break;

    case 5:
      graphType = "cos";
      graphQuadrant = "1-4";
      graphDensity = 0.25;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = cos(x)";
      break;

    case 6:
      graphType = "tan";
      graphQuadrant = "1-4";
      graphDensity = 0.01;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = tan(x)";
      break;

    case 7:
      graphType = "custom1";
      graphQuadrant = "1";
      graphDensity = 0.25;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = 0.5(x/4-3)^3-2(x/4-3)^2+(x/4)+10";
      break;

    case 8:
      graphType = "custom2";
      graphQuadrant = "1";
      graphDensity = 0.25;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = e^x";
      break;
    }

    //Keyboard Input Module
    if (refreshControl == 0) {
      if (keyPressed) {
        if ((keyCode == LEFT)||(keyCode == RIGHT)||(keyCode == UP)||(keyCode == DOWN)) {   //if the Key is an arrow don't delay sketch for 80 mills
          switch(keyCode) {               //only for arrows
          case UP:
            zoom += 2;     // => zoom = zoom + 5;
            break;

          case DOWN:
            if (zoom > 12) {
              zoom -= 2;
            }
            break;

          case LEFT:
            if (tn >= 0) {
              tn = tn - 1*sensibility;
            }
            break;

          case RIGHT:
            tn = tn + 1*sensibility;
            break;
          }
        } else {
          if ((key=='s')||(key=='S')) {
            if (selecting == "off") {
              selecting = "on";
            } else if ((selecting == "selected")||(selecting == "lS")||(selecting == "on")) {
              selecting = "off";
            }
          }
          if ((key=='c')||(key=='C')) {
            if (showCursorXY == false) {
              showCursorXY = true;
            } else {
              showCursorXY = false;
            }
          }
          if ((key=='h')||(key=='H')) {
            if (drawHighI == true) {
              drawHighI = false;
            } else {
              drawHighI = true;
            }
          } 
          if ((key=='l')||(key=='L')) {
            if (drawLowI == true) {
              drawLowI = false;
            } else {
              drawLowI = true;
            }
          }
          if ((key=='x')||(key=='X')) {
            if (sensibility == 0.2) {
              sensibility = 1;
            } else if (sensibility == 1) {
              sensibility = 5;
            } else if (sensibility == 5) {
              sensibility = 10;
            } else if (sensibility == 10) {
              sensibility = 100;
            } else if (sensibility == 100) {
              sensibility = 0.2;
            } 
            sensibilityChange = true;
          }
          delay(80);
        }
        n = round(tn);
      }
    }

    if (n < 0) {       //correcting the value of n and tn if they are negative
      n = 0;
      tn = 0;
    }

    if (sensibilityChange == true) {
      textSize(50);
      textAlign(CENTER);
      if (sensibility == 0.2) {
        text(nf(sensibility, 1, 1), width/2, height/2);
      } else {
        text(nf(sensibility, 0, 0), width/2, height/2);
      }

      textAlign(LEFT);

      if (sensibilityChangeCounter == 0) {
        sensibilityChange = false;
      }

      if (sensibilityChangeCounter < 1) {
        sensibilityChangeCounter = 15;
      } else {
        sensibilityChangeCounter -= 1;
      }
    }

    scrolling = false;  //resetting scrolling
}



//functions page 3-----------------------------------------------------


//canvas XY to graph XY conversion
float getGraphX(float cX) {                //given a canvasX returns the corresponding x value on the graph (canvas xy are translated)
  float gX = ((cX-xTranslation)/zoom);
  return gX;
}

float getGraphY(float cY) {
  float gY = ((height-(cY-yTranslation))/zoom);
  return gY;
}

//graph XY to canvas XY conversion
float getCanvasX(float gX) {                //given a canvasX returns the corresponding x value on the graph (canvas xy are translated)
  float cX = (gX*zoom);   //-xTranslation;
  return cX;
}

float getCanvasY(float gY) {
  float cY = (-((gY*zoom)-height));
  return cY;
}


//graphing Function
void drawGraph() {   //given a graphing mode (linear, parabola, ln) draw the corresponding graph
  beginShape();
  for (float i = graphStartingPoint; i < maxXLimit; i += graphDensity) {
    stroke(255);
    curveVertex(i*zoom, height- getY(i) *zoom);     //curveVertex( x , f(x) )    points' values times zoom plus correcting the configuartion
  }  
  endShape();
}


//y=f(x) calculating function
float getY(float x) {

  float y = 0;

  switch(graphType) {

  case "linear":
    y = x;
    break;

  case "parabola":
    y = pow(x, 2);
    break;

  case "ln":
    y = log(x);
    break;

  case "sin":
    y = sin(x);
    break;

  case "cos":
    y = cos(x);
    break;

  case "tan":
    y = tan(x);
    break;

  case "custom1":   //f(x) = 0.5(x/4-3)^3-2(x/4-3)^2+(x/4) +10
    y = 0.5*pow((x/4 -3), 3) -2*pow((x/4-3), 2) +x/4 +10;
    break;

  case "custom2":  
    y = exp(x);
    break;
  }

  return y;
}

//function for the rects of the integral process   ##################################################################
void drawLowIntegration() {
  float e = deltaBound/n;
  for (float i = 0; i < n; i++) {

    float functionMin = 0;

    functionMin = e*i;

    beginShape();
    vertex(getCanvasX(lowerBound+(e*i)), height);                                              //Bottom-Left
    vertex(getCanvasX(lowerBound+(e*i)), getCanvasY(getY(lowerBound+(functionMin))));          //Top-Let
    vertex(getCanvasX(lowerBound+e*(i+1)), getCanvasY(getY(lowerBound+(functionMin))));        //Top-Right
    vertex(getCanvasX(lowerBound+e*(i+1)), height);                                            //Botton-Right
    endShape();

    lowIntegrationValue += e*getY(lowerBound+(functionMin));
  }
}

void drawHighIntegration() {
  float e = deltaBound/n;
  for (float i = 0; i < n; i++) {

    float functionMax = 0;

    functionMax = e*(i+1);

    beginShape();
    vertex(getCanvasX(lowerBound+(e*i)), height);
    vertex(getCanvasX(lowerBound+(e*i)), getCanvasY(getY(lowerBound+functionMax)));      
    vertex(getCanvasX(lowerBound+e*(i+1)), getCanvasY(getY(lowerBound+functionMax)));
    vertex(getCanvasX(lowerBound+e*(i+1)), height);
    endShape();

    highIntegrationValue += e*getY(lowerBound+functionMax);
  }
}
