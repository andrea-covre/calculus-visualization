int page = 3; //<>//

//UI Parameters
int[] bgColor = {0, 0, 0};                         //set default background color
int[] borderColor = {255, 255, 255};               //set default color for the fiugres' borders
float sensibility = 0.2;                           //0.2; //set the sensibility of the input when a key is pressed

color green = color(0, 255, 0);
color red = color(255, 0, 0);

//GLOBAL Variables size(1250, 680);
int WIDTH = 1250;
int HEIGHT = 680;
int hW = WIDTH/2;
int hH = HEIGHT/2;
String status = "Operative";                       //set status of the Software
float responseTime = 0;                            //Time in seconds that the code took to calculate the last request
float timestamp = 0;                               //timestamp of the request

//Page 0 variables
int dotNumber = 1000;
int dotProperty = 6;
float[][] dot = new float[dotNumber][dotProperty]; //[xPos][yPos][xSpeed][ySpeed][size][hue]
float dotMaxSpeed = 0.5;
float dotMaxSize = 30;
boolean dotColorChange = true;
float dotColorSpeed = 0.6;
boolean dotLine = false;
boolean dotBackRefresh = true;
boolean dotBounce = true;
String spawnPoint = "random";

//Page 1 variables
float areaInt = 0;
float areaExt = 0;
float fixedR = 1;

//Page 2 varables
float r = 270;
float R = r;

int pN = 0;                              //previous n
float pR = 0;                            //previous r
boolean change = true;                   //used to check if rebuilding the image is neccesesary
boolean refresh = true;
int refreshControl = 0;                  //to control the steps of the refreshing
String sectionMode = "linear";           // [linear] or [exponential]
boolean extGrid = true;                  // [true] or [flase] boolean to show external grid or not
float borderSpace = 0.15;                //radius percentage of space between the circle and the frame
float frame = 0;
int intSum = 0;                          //somma quadrati interni al cerchio
int extSum = 0;                          //somma quadrati esterni al cerchio
float squareArea = 0;

//optimization
//dist of vertexes from center
float v1r = 0;
float v2r = 0;
float v3r = 0;
float v4r = 0;
float squareSide = 0;                    // squareSide = 2*frame/n
float hWF = 0;                           //(width/2-frame)
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
float zoom = 40;                         //zoom of the graph
float tac = 5;                           //half size of xy segment values
float tacControl = 1;                    //float to control when show tac 1-by-1 or 2-by-2 or 0.5-by-0.5
int xTranslation = 50;
int yTranslation = -70;
String selecting = "off";                //String to control when "s" is pressed to select an interval  (on-off-lS-selected) selected = seleting has been done, lS = lower is selected, waiting for the upper
float lowerBound = -100;                 //lowerBound = -100 just so it is easy to reconize when it has been properly selected
float upperBound = -100;
float maxXLimit = 100;
int messageTime = 1000;                 //time [milliseconds]that a message will be displayed
float closeMessage = 0;                 //timestamp of when a message will expire and will not be showen anymore
boolean displayMessage = false;
float deltaBound = 0;
float lowIntegrationValue = 0;
float highIntegrationValue = 0;
String fxEquation = "";
int n = 0;
float tn = 0;
boolean showCursorXY = true;
//boolean showFunctionTab = false;
boolean scrolling = false;
int wheelAccumulator = 1;
int functionIndex = 1;
int totalFunctions = 8;                  //number of functions pre-setted
boolean drawHighI = false;               //boolean to decide if draw highI or not
boolean drawLowI = true; 
String graphQuadrant = "1";
float graphDensity = 1;
float graphStartingPoint = 0;
boolean sensibilityChange = false;
int sensibilityChangeCounter = 0;


String graphType = "parabola";



void settings() {
  size(WIDTH, HEIGHT);
}

void setup() {
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

void draw() {
  if (page == 0) {      //----------- PAGE 0 -----------
    displayPage_0();
  } else if (page == 1) {                                //----------- PAGE 1 -----------
    displayPage_1();
  } else if (page == 2) {                                //----------- PAGE 2 -----------
    displayPage_2();
  } else if (page == 3) {                                //----------- PAGE 3 -----------
    displayPage_3();
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

  //LOGS
  //println(n + " " + lowIntegrationValue + " " + highIntegrationValue + " " + wheelAccumulator + " " + scrolling + " " + functionIndex);

  displayStatusBar();
}   
