void mouseClicked() {
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

void mouseWheel(MouseEvent event) {
  wheelAccumulator = event.getCount();
  scrolling = true;
}
