package cs3318.raytracing;

import cs3318.raytracing.controller.Controller;

/***************************************************
 *
 *   An instructional Ray-Tracing Renderer written
 *   for MIT 6.837  Fall '98 by Leonard McMillan.
 *
 *   A fairly primitive Ray-Tracing program written
 *   on a Sunday afternoon before Monday's class.
 *   Everything is contained in a single file. The
 *   structure should be fairly easy to extend, with
 *   new primitives, features and other such stuff.
 *
 *   I tend to write things bottom up (old K&R C
 *   habits die slowly). If you want the big picture
 *   scroll to the applet code at the end and work
 *   your way back here.
 *
 ****************************************************/

public class Main{

    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.setTestScene();
        controller.renderImage();
        controller.exportImage("RenderTest.png");
    }
}
