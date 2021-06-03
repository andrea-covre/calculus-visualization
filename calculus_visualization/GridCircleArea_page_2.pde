void displayPage_2() {
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

      strokeWeight(0.1);
      stroke(255, 0, 255);

      frame = r + r*borderSpace;                      //set the frame
      intSum = 0;
      extSum = 0;

      //optimization block (variables precalculated)
      squareSide = 2*frame/n;                         // it = (2*frame/n) before: x1 = (width/2-frame) + (2*frame/n)*i;
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
      ellipse(width/2, height/2, r*2, r*2);                         //circle of the radius r

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
} 
