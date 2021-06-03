import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Calculus_Simulator_V1_2_2 extends PApplet {

int page = 3;

//UI Parameters
int[] bgColor = {0, 0, 0};  //set default background color
int[] borderColor = {255, 255, 255}; //set default color for the fiugres' borders
float sensibility = 0.2f;//0.2; //set the sensibility of the input when a key is pressed


int green = color(0, 255, 0);
int red = color(255, 0, 0);


//GLOBAL Variables size(1250, 680);
int WIDTH = 1250;
int HEIGHT = 680;
int hW = WIDTH/2;
int hH = HEIGHT/2;
//int page = 0;
String status = "Operative"; //set status of the Software
float responseTime = 0;   //Time in seconds that the code took to calculate the last request
float timestamp = 0;  //timestamp of the request

//Page 0 variables
int dotNumber = 1000;
int dotProperty = 6;
float[][] dot = new float[dotNumber][dotProperty]; //[xPos][yPos][xSpeed][ySpeed][size][hue]
float dotMaxSpeed = 0.5f;
float dotMaxSize = 30;
boolean dotColorChange = true;
float dotColorSpeed = 0.6f;
boolean dotLine = false;
boolean dotBackRefresh = true;
boolean dotBounce = true;
String spawnPoint = "random";      // center - random 

//Page 1 variables
//float r = 150;
//float R = r;
//int n = 3;
float areaInt = 0;
float areaExt = 0;
float fixedR = 1;
//float tn = 3; //temporany value of n

//Page 2 varables
float r = 270;
float R = r;
//int n = 1;    //CONTRADDICTION WITH PAGE 1 VARIABLE
//float tn = 1; //CONTRADDICTION WITH PAGE 1 VARIABLE
int pN = 0;   //previous n
float pR = 0;   //previous r
boolean change = true; //("check if rebuild the image is neccesesary each time")
boolean refresh = true; // make image refresh
int refreshControl = 0;  //to control the steps of the refreshing
String sectionMode = "linear"; // [linear] or [exponential]
boolean extGrid = true; // [true] or [flase] boolean to show external grid or not
float borderSpace = 0.15f; //radius percentage of space between the circle and the frame
float frame = 0;
int intSum = 0;  //somma quadrati interni al cerchio
int extSum = 0;  //somma quadrati esterni al cerchio
float squareArea = 0;

//optimization
//dist of vertexes from center
float v1r = 0;
float v2r = 0;
float v3r = 0;
float v4r = 0;
//condensing stuff  x2 = (width/2-frame) + squareSide*(i+1);
float squareSide = 0; // squareSide = 2*frame/n
float hWF = 0; //(width/2-frame)
float hHF = 0;

//vetrexes of the squares

//top left
float x1 = 0;       
float y1 = 0;
//top right
float x2 = 0;       
float y2 = 0;
//botton right
float x3 = 0;       
float y3 = 0;
//botton left
float x4 = 0;       
float y4= 0;

//Page 3 variables
float zoom = 40;      //zoom of the graph
float tac = 5;       //half size of xy segment values
float tacControl = 1;  //float to control when show tac 1-by-1 or 2-by-2 or 0.5-by-0.5
int xTranslation = 50;
int yTranslation = -70;
String selecting = "off";  //String to control when "s" is pressed to select an interval  (on-off-lS-selected) selected = seleting has been done, lS = lower is selected, waiting for the upper
float lowerBound = -100;    //lowerBound = -100 just so it is easy to reconize when it has been properly selected
float upperBound = -100;
float maxXLimit = 100;
int messageTime = 1000;   //time [milliseconds]that a message will be displayed
float closeMessage = 0;   //timestamp of when a message will expire and will not be showen anymore
boolean displayMessage = false;
float deltaBound = 0;
float lowIntegrationValue = 0;
float highIntegrationValue = 0;
String fxEquation = "";
int n = 0;    //CONTRADDICTION WITH PAGE 1 VARIABLE
float tn = 0; //CONTRADDICTION WITH PAGE 1 VARIABLE
boolean showCursorXY = true;
//boolean showFunctionTab = false;
boolean scrolling = false;
int wheelAccumulator = 1;
int functionIndex = 1;
int totalFunctions = 8;  //number of functions setted------------------------------------
boolean drawHighI = false;   //boolean to decide if draw highI or not
boolean drawLowI = true; 
String graphQuadrant = "1";
float graphDensity = 1;
float graphStartingPoint = 0;
boolean sensibilityChange = false;
int sensibilityChangeCounter = 0;


String graphType = "parabola";



public void settings() {
  size(WIDTH, HEIGHT);
}

public void setup() {
  //size(1250, 680);
  stroke(borderColor[0], borderColor[1], borderColor[2]);
  background(0);
  strokeWeight(1);
  textSize(16);
  noFill();
  frameRate(60);

  //generating randoom dots propieties
  for (int i = 0; i < dotNumber; i++) {

    if (spawnPoint == "center") {
      dot[i][0] = width/2;
      dot[i][1] = height/2;
    } else if (spawnPoint == "random") {
      dot[i][0] = random(50, width-50);
      dot[i][1] = random(50, height-50);
    } 
    dot[i][2] = random(-dotMaxSpeed, dotMaxSpeed);
    dot[i][3] = random(-dotMaxSpeed, dotMaxSpeed);
    dot[i][4] = random(5, dotMaxSize);
    dot[i][5] = random(0, 255);
  }
}

public void draw() {
  if (page == 0) {      //----------- PAGE 0 -----------

    if (dotBackRefresh) {
      background(0);  //clearing screen
    }

    animateDot();
  } else if (page == 1) {                                //----------- PAGE 1 -----------
    stroke(255, 0, 255);
    strokeWeight(1);
    background(bgColor[0], bgColor[1], bgColor[2]);  //clear all

    pushMatrix();
    translate(width/2, height/2);
    rotate(frameCount / 200.0f);

    if (n != 2) {
      polygon(0, 0, r/cos(TWO_PI/(2*n)), n);  //external polygon SPECIAL FORMULA
    }

    strokeWeight(2);
    stroke(255, 0, 0);
    ellipse(0, 0, r*2, r*2);                              //circle of radius r

    strokeWeight(1);
    stroke(255, 0, 255);
    if (n != 2) {
      polygon(0, 0, r, n);                           //internal polygon
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
  } else if (page == 2) {                                //----------- PAGE 2 -----------

    if (change == true) {
      refreshControl = 3;
      change = false;
      timestamp = millis();
    } 

    if (refreshControl == 2) {
      refresh = true;
      refreshControl = 1;
    }



    if (refresh == true) {
      stroke(255, 0, 255);
      strokeWeight(1);
      background(bgColor[0], bgColor[1], bgColor[2]);  //clear all

      pushMatrix();
      //translate(width/2, height/2);

      strokeWeight(0.1f);
      stroke(255, 0, 255);

      frame = r + r*borderSpace;     //set the frame
      intSum = 0;
      extSum = 0;

      //optimization block (varibale precalculated)
      squareSide = 2*frame/n; // it = (2*frame/n) before: x1 = (width/2-frame) + (2*frame/n)*i;
      hWF = width/2-frame;
      hHF = height/2-frame;

      //consctruction of the sectioning
      for (int j = 0; j<n; j++) {
        for (int i = 0; i<n; i++) {
          x1 = hWF + squareSide*i;
          y1 = hHF + squareSide*j;
          x2 = hWF + squareSide*(i+1);
          y2 = hHF + squareSide*j;
          x3 = hWF + squareSide*(i+1);
          y3 = hHF + squareSide*(j+1);
          x4 = hWF + squareSide*i;
          y4 = hHF + squareSide*(j+1);

          // hW hH
          //calc vertex distance from radius 
          v1r = dist(hW, hH, x1, y1);
          v2r = dist(hW, hH, x2, y2);
          v3r = dist(hW, hH, x3, y3);
          v4r = dist(hW, hH, x4, y4);

          //check vertexes position
          if ((v1r <= r)||(v2r <= r)||(v3r <= r)||(v4r <= r)) {
            fill(255, 0, 255, 90);
            if ((v1r <= r)&&(v2r <= r)&&(v3r <= r)&&(v4r <= r)) {
              intSum++;
              fill(90, 180, 255, 120);
            } else {
              extSum++;
            }

            //actual construction of the array of boxes
            if (extGrid == false) {
              beginShape();
              vertex(x1, y1);      //top left vertex
              vertex(x2, y2);      //top right vertex
              vertex(x3, y3);      //botton right vertex
              vertex(x4, y4);      //botton left vertex
              endShape(CLOSE);

              noFill();
            }
          }
          if (extGrid == true) {
            beginShape();
            vertex(x1, y1);      //top left vertex
            vertex(x2, y2);      //top right vertex
            vertex(x3, y3);      //botton right vertex
            vertex(x4, y4);      //botton left vertex
            endShape(CLOSE);

            noFill();
          }
        }
      }

      squareArea = ((2*fixedR +2*fixedR*borderSpace)/n) * ((2*fixedR +2*fixedR*borderSpace)/n);

      strokeWeight(2);
      stroke(255, 0, 0);
      ellipse(width/2, height/2, r*2, r*2);                              //circle of the radius r

      //test ricerca funzioni appropiate
      //for (int i = 0; i<n; i++) {
      //  stroke(255, 0, 255);
      //  line((width/2-r) + (2*r/n)*i, 0, (width/2-r) + (2*r/n)*i, height); //x of top left vertex -> widht/2 -r
      //  line(0, (height/2-r) + (2*r/n)*i, width, (height/2-r) + (2*r/n)*i);  //y of top left vertex -> height/2 -r
      //  stroke(255);
      //  line((width/2-r) + (2*r/n)*(i+1), 0, (width/2-r) + (2*r/n)*(i+1), height); //x of top right vertex widht/2 +r
      //  line(0, (height/2-r) w+ (2*r/n)*(i+1), width, (height/2-r) + (2*r/n)*(i+1)); //y of top left vertex -> height/2 +r
      //}

      popMatrix();

      noFill();
      stroke(255, 0, 255);
      strokeWeight(1);

      //shape's info
      fill(255, 0, 255);
      text("Zoom: " + round((r/R)*100) + "%", 20, 40);
      text("Circle's radius: " + fixedR, 20, 60);
      text("Squares per side: " + n, 20, 80);
      text("Total # of squares: " + (n*n), 20, 100);
      text("Squares in: " + intSum, 20, 120);
      text("Squares on: " + (intSum + extSum), 20, 140);
      text("Square's area: " + squareArea, 20, 160);
      text("Area in: " + (squareArea*intSum), 20, 180);
      text("Area on: " + (squareArea*(intSum + extSum)), 20, 200);
      text("Difference: " + ((squareArea*(intSum + extSum))-(squareArea*intSum)), 20, 220);
      //text("Frame rate: " + frameRate, 20, 220);

      text("Section mode:", 20, 260); //80
      text("Linear  Exponential", 20, 280);
      text("Show external grid:", 20, 320);
      text("Yes  No", 20, 340);

      noFill();

      //drawing the box around [external grid] yes or no
      if (extGrid == true) {
        rect(16, 325, 34, 18);
      } else {
        rect(50, 325, 34, 18);
      }
    }    

    pN = n; //store the prevoius value of n
    pR = r; //store the prevoius value of r

    //input Module
    if (refreshControl == 0) {
      if (keyPressed) {
        switch(keyCode) {
        case UP:
          r = r + 5;
          break;
        case DOWN:
          r = r - 5;
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
      }
    }

    stroke(255, 0, 255);
    if (sectionMode == "exponential") {
      n = round(pow(2, round(tn)));
      rect(75, 265, 95, 18);
    } else {
      n = round(tn);
      rect(16, 265, 55, 18);
    }

    if ((pN != n)||(pR != r)) {
      change = true;
    } else {
      change = false;
    }

    if (refreshControl == 3) {
      status = "Computing"; 
      refreshControl = 2;
    }

    if (refreshControl == 1) {
      refreshControl = 0;
      responseTime = millis() - timestamp; //calculating how much time the code took to compute the last request
    }

    if (refreshControl == 0) {
      status = "Operative";
      refresh = false;
    }
  } else if (page == 3) {                                //----------- PAGE 3 -----------


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
    line(0, height +500, 0, 0);   //y-axis
    stroke(255);

    //TACS
    //managing the tacs when they overlap ect...
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
      tacControl = 0.5f;
    } else if ((zoom >= 120)&&(zoom <= 200)) {
      tacControl = 0.25f;
    }

    //Drawing xy values with Tacs
    textSize(10);
    stroke(255, 0, 0);
    if (tacControl < 1) {
      for (float i = 0; i < 25; i += tacControl) {
        line(i*zoom, height +tac, i*zoom, height-tac);  //x tacs
        line(0 -tac, height-i*zoom, 0 +tac, height-i*zoom);  //y tacs

        line(0 -tac, height-(-i)*zoom, 0 +tac, height-(-i)*zoom);  //-y tacs

        text(Float.toString(i), i*zoom -5, height +18);  //x values
        text(Float.toString(i), 0 -23, height-i*zoom +5);  //y values

        if (i != 0) {
          text(Float.toString(-i), 0 -23, height-(-i)*zoom +5);  //-y values
        }
      }
    } else {
      for (int i = 0; i < 105; i += tacControl) {
        line(i*zoom, height +tac, i*zoom, height-tac);  //x tacs
        line(0 -tac, height-i*zoom, 0 +tac, height-i*zoom);  //y tacs

        line(0 -tac, height-(-i)*zoom, 0 +tac, height-(-i)*zoom);  //-y tacs

        text(i, i*zoom -5, height +18);  //x values
        text(i, 0 -25, height-i*zoom +5);  //y values

        if (i != 0) {
          text(-i, 0 -25, height-(-i)*zoom +5);  //-y values
        }
      }
    }

    //stroke(255, 0, 0);

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

        delay(80);     //!!!!!!!!!!!
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

      //giving a message of error when lowerBound == upperBound              NEW DISPLAY MESSAGE!!!!!
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
        if (closeMessage <= millis()) {      //after presetted time
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

    //vertical line at intersect
    //line(mouseX-xTranslation, height, mouseX-xTranslation, getCanvasY(getY(getGraphX(mouseX), graphType)) );

    //orizontal line at intersect
    //line(0, getCanvasY(getY(getGraphX(mouseX), graphType)), width, getCanvasY(getY(getGraphX(mouseX), graphType)) );

    //Resetting the area values of low and high Integration
    lowIntegrationValue = 0;
    highIntegrationValue = 0;


    //deciding whether draw highI or lowI or both based on User Inputs
    //drawing the selected Integration
    stroke(0, 255, 0);
    if (selecting == "selected") {
      if (drawLowI) {
        fill(0, 255, 0, 70);
        drawLowIntegration();      //super cool function HERE#######################################################################
      }
      if (drawHighI) {
        //fill(100, 100, 255, 30);  
        noFill();
        drawHighIntegration();                   //stuff to CHK===================!!!!!!!!!!!!!!===================
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
      //
    } else if (selecting == "on") {
      rect(width-320, 20, 300, 65);
      fill(255);
      text("Graph Type: " + graphType, width-320+5, 35);
      text("Equation: " + fxEquation, width-320+5, 50);
      text("Interval: Select lower bound", width-320+5, 65);
      text("Lower bound: " + getGraphX(mouseX), width-320+5, 80);
      //
    } else if (selecting == "lS") {
      rect(width-320, 20, 300, 95);
      fill(255);
      text("Graph Type: " + graphType, width-320+5, 35);
      text("Equation: " + fxEquation, width-320+5, 50);
      text("Interval: Select upper bound", width-320+5, 65);
      text("Lower bound: " + lowerBound, width-320+5, 80);
      text("Upper bound: " + getGraphX(mouseX), width-320+5, 95);
      text("\u0394 = " + (getGraphX(mouseX)-lowerBound), width-320+5, 110);
      //
    } else if ((selecting == "selected")&&(-99 != upperBound)&&(lowerBound != upperBound)) {   //aadditional stuff to don't make display data when a=b


      if ((drawHighI == false)&&(drawLowI == false)) {
        rect(width-320, 20, 300, 80);
        fill(255);

        text("Graph Type: " + graphType, width-320+5, 35);
        text("Equation: " + fxEquation, width-320+5, 50);
        text("Interval: [ " + lowerBound + " ; " + upperBound + " ]", width-320+5, 65);
        text("\u0394 = " + (upperBound-lowerBound), width-320+5, 80);
        text("Number of blocks: " + n, width-320+5, 95);
        //
      } else if ((drawHighI == false)||(drawLowI == false)) {
        rect(width-320, 20, 300, 95);
        fill(255);

        text("Graph Type: " + graphType, width-320+5, 35);
        text("Equation: " + fxEquation, width-320+5, 50);
        text("Interval: [ " + lowerBound + " ; " + upperBound + " ]", width-320+5, 65);
        text("\u0394 = " + (upperBound-lowerBound), width-320+5, 80);
        text("Number of blocks: " + n, width-320+5, 95);

        if (drawLowI == false) {
          text("Higher blocks' area: " + highIntegrationValue, width-320+5, 110);
        } 
        if (drawHighI == false) {
          text("Lower blocks' area: " + lowIntegrationValue, width-320+5, 110);
        }
        //
      } else if ((drawHighI == true)&&(drawLowI == true)) {

        rect(width-320, 20, 300, 125);
        fill(255);
        text("Graph Type: " + graphType, width-320+5, 35);
        text("Equation: " + fxEquation, width-320+5, 50);
        text("Interval: [ " + lowerBound + " ; " + upperBound + " ]", width-320+5, 65);
        text("\u0394 = " + (upperBound-lowerBound), width-320+5, 80);
        text("Number of blocks: " + n, width-320+5, 95);
        text("Lower blocks' area: " + lowIntegrationValue, width-320+5, 110);
        text("Higher blocks' area: " + highIntegrationValue, width-320+5, 125);
        text("Areas difference: " + (highIntegrationValue-lowIntegrationValue), width-320+5, 140);
      }
    }
    //text("Interval: " + (upperBound-lowerBound)/n, width-320+5, 65);
    //text("Graph Type: " + graphType, width-320+5, 80);
    //text("Graph Type: " + graphType, width-320+5, 95);
    //text("Graph Type: " + graphType, width-320+5, 110);


    //Scheme box informtations
    //------------------------
    //35 Graph Type
    //50 Equation
    //65 Interval
    //n value
    //80 lowI area
    //95 highI area
    //110 highI lowI area diff

    //Show Function Slection Tab?!?!?!


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

    //selecting the function from its index value
    //and setting the propieties
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
      graphDensity = 0.5f;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = x^2";
      break;

    case 3:
      graphType = "ln";
      graphQuadrant = "1";
      graphDensity = 0.2f;
      graphStartingPoint = 0.1f;
      fxEquation = "y = ln(x)";
      break;

    case 4:
      graphType = "sin";
      graphQuadrant = "1-4";
      graphDensity = 0.25f;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = sin(x)";
      break;

    case 5:
      graphType = "cos";
      graphQuadrant = "1-4";
      graphDensity = 0.25f;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = cos(x)";
      break;

    case 6:
      graphType = "tan";
      graphQuadrant = "1-4";
      graphDensity = 0.01f;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = tan(x)";
      break;

    case 7:
      graphType = "custom1";
      graphQuadrant = "1";
      graphDensity = 0.25f;
      graphStartingPoint = 0 - graphDensity;
      fxEquation = "y = 0.5(x/4-3)^3-2(x/4-3)^2+(x/4)+10";
      break;

    case 8:
      graphType = "custom2";
      graphQuadrant = "1";
      graphDensity = 0.25f;
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
            if (sensibility == 0.2f) {
              sensibility = 1;
            } else if (sensibility == 1) {
              sensibility = 5;
            } else if (sensibility == 5) {
              sensibility = 10;
            } else if (sensibility == 10) {
              sensibility = 100;
            } else if (sensibility == 100) {
              sensibility = 0.2f;
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
      if (sensibility == 0.2f) {
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
  } else if (page == 4) {                                //----------- PAGE 4 -----------
  } else if (page == 5) {                                //----------- PAGE 5 -----------
  }


  //navigation Through pages

  if (keyPressed) {
    if (key == '0') {
      page = 0;
    } else if (key == '1') {
      page = 1;
    } else if (key == '2') {
      page = 2;
    } else if (key == '3') {
      page = 3;
    } else if (key == '4') {
      page = 4;
    } else if (key == '5') {
      page = 5;
    }

    if ((key == '1')||(key == '2')||(key == '3')||(key == '4')||(key == '5')) {
      n = 0;
      tn = 0;
    }
  }




  //OUTSIDE of the pages

  //console log
  //println(n + " " + frameRate + " " + millis());
  //println(lowerBound + " " + upperBound + " " + selecting + " " + n);
  println(n + " " + lowIntegrationValue + " " + highIntegrationValue + " " + wheelAccumulator + " " + scrolling + " " + functionIndex);
  //println(mouseX + " " + mouseY);




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
  text("V 1.2.2", width-45, height-5);                                               //SKetch Version ------
  noFill();
  textSize(16);
  //line(width/2, 0, width/2, height);  //half line to check text distances
}   

//end DRAW()


//functions-----------------------------------------------------------------------------------------------

//functions page 0-----------------------------------------------------

public void animateDot() {
  colorMode(HSB);
  noStroke();
  for (int i = 0; i < dotNumber; i++) {
    fill(dot[i][5], 255, 255);   //selecting dot color
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



//functions page 1-----------------------------------------------------
public void polygon(float x, float y, float radius, int npoints) {
  float angle = TWO_PI / npoints;
  beginShape();
  for (float a = 0; a < TWO_PI; a += angle) {
    float sx = x + cos(a) * radius;
    float sy = y + sin(a) * radius;
    vertex(sx, sy);
  }
  endShape(CLOSE);
}


//functions page 3-----------------------------------------------------

//canvas XY to graph XY conversion
public float getGraphX(float cX) {                //given a canvasX returns the corresponding x value on the graph (canvas xy are translated)
  float gX = ((cX-xTranslation)/zoom);
  return gX;
}

public float getGraphY(float cY) {
  float gY = ((height-(cY-yTranslation))/zoom);
  return gY;
}

//graph XY to canvas XY conversion
public float getCanvasX(float gX) {                //given a canvasX returns the corresponding x value on the graph (canvas xy are translated)
  float cX = (gX*zoom);   //-xTranslation;
  return cX;
}

public float getCanvasY(float gY) {
  float cY = (-((gY*zoom)-height));
  return cY;
}



//graphing Function
public void drawGraph() {   //given a graphing mode (linear, parabola, ln) draw the corresponding graph
  beginShape();
  for (float i = graphStartingPoint; i < maxXLimit; i += graphDensity) {
    stroke(255);
    curveVertex(i*zoom, height- getY(i) *zoom);     //curveVertex( x , f(x) )    points' values times zoom plus correcting the configuartion
  }  
  endShape();
}


//y=f(x) calculating function
public float getY(float x) {

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
    y = 0.5f*pow((x/4 -3), 3) -2*pow((x/4-3), 2) +x/4 +10;
    break;

  case "custom2":  
    y = exp(x);
    break;
  }

  return y;
}

//function for the rects of the integral process   ##################################################################
public void drawLowIntegration() {
  float e = deltaBound/n;
  for (float i = 0; i < n; i++) {

    float functionMin = 0;

    //if ( abs(getY(lowerBound+(e*i))) < abs(getY(lowerBound+(e*(i+1)))) ) {
    //  functionMin = e*i;
    //} else {
    //  functionMin = e*(i+1);
    //}
    
    functionMin = e*i;

    beginShape();
    vertex(getCanvasX(lowerBound+(e*i)), height);                                      //Bottom-Left
    vertex(getCanvasX(lowerBound+(e*i)), getCanvasY(getY(lowerBound+(functionMin))));          //Top-Let
    vertex(getCanvasX(lowerBound+e*(i+1)), getCanvasY(getY(lowerBound+(functionMin))));        //Top-Right
    vertex(getCanvasX(lowerBound+e*(i+1)), height);                                    //Botton-Right
    endShape();

    lowIntegrationValue += e*getY(lowerBound+(functionMin));
  }
}

public void drawHighIntegration() {
  float e = deltaBound/n;
  for (float i = 0; i < n; i++) {

    float functionMax = 0;

    //if ( abs(getY(lowerBound+(e*i))) < abs(getY(lowerBound+(e*(i+1)))) ) {
    //  functionMax = e*(i+1);
    //} else {
    //  functionMax = e*i;
    //}
    
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



//Global functions----------------------------------------------------

public void mouseClicked() {
  if (page == 2) {
    if (refreshControl == 0) {
      if ((mouseX>75)&&(mouseX<75+95)&&(mouseY>265)&&(mouseY<265+18)) {   //mouse on exponential
        if (sectionMode != "exponential") {
          sectionMode = "exponential";
          n = 0;
          tn = 0;
          change = true;
        }
      }

      if ((mouseX>16)&&(mouseX<16+55)&&(mouseY>265)&&(mouseY<265+18)) {   //mouse on linear
        if (sectionMode != "linear") {
          sectionMode = "linear";
          n = 1;
          tn = 1;
          change = true;
        }
      }

      if ((mouseX>16)&&(mouseX<16+34)&&(mouseY>325)&&(mouseY<325+18)) {   //mouse on external gird[yes]
        if (extGrid == false) {
          extGrid = true;
          change = true;
        }
      }

      if ((mouseX>50)&&(mouseX<50+34)&&(mouseY>325)&&(mouseY<325+18)) {   //mouse on external gird[no]
        if (extGrid == true) {
          extGrid = false;
          change = true;
        }
      }
    }
  }
}

public void mouseWheel(MouseEvent event) {
  wheelAccumulator = event.getCount();
  scrolling = true;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#464646", "--hide-stop", "Calculus_Simulator_V1_2_2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
