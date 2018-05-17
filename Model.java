
/**
 * The physics model.
 *
 * This class is where you should implement your bouncing balls model.
 *
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 *
 * @author Simon Robillard
 *
 */
import java.lang.Math;

class Model {

    double areaWidth, areaHeight;
    private final double gravity;
    private int time;
    private final int WAIT = 10;

    Ball [] balls;

    Model(double width, double height) {
        time = 0;
        areaWidth = width;
        areaHeight = height;
        gravity = 9.82;

        // Initialize the model with a few balls
        balls = new Ball[2];

        //Sätta vx = 0 här? --> "For the horizontal position x there are no forces so x''=0."
        balls[0] = new Ball(width / 3, height * 0.7, 1, 0.8, 0.4);
        //den stora
        balls[1] = new Ball(2 * width / 3, height * 0.7, -0.9, 1, 0.45);

    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        for (Ball b : balls) {

            // compute new position according to the speed of the ball
            //b.vy -= deltaT * gravity;
            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;

            if(time > 0){
                time--;
            }
            else{

                if(detectCollision()){

                    //vinkeln till nya planet
                    //double beta = getAngle();

                    double before = balls[0].radius*balls[0].vx + balls[1].radius*balls[1].vx;

                    System.out.println("Before collision: " + balls[0].radius*balls[0].vx + balls[1].radius*balls[1].vx);

                    //rotera till nya planet
                    //rotate(-beta);

                    //räkna ut ny velocity
                    //calculate();

                    //rotera tillbaka
                    //rotate(beta);


                    // prasanth på fire!!!!!!!!
                    vectorCalc();

                    System.out.println("After collision: " + balls[0].radius*balls[0].vx + balls[1].radius*balls[1].vx);

                    double after = balls[0].radius*balls[0].vx + balls[1].radius*balls[1].vx;

                    System.out.println("Difference: " + (before - after));

                    System.out.println("");

                }
            }
            // detect detectCollision with the border
            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1; // change direction of ball
            }
            if (b.y < b.radius || b.y > areaHeight - b.radius) {
                b.vy *= -1;
            }
            else{
                b.vy -= deltaT * gravity;
            }


        }
    }

    private boolean detectCollision(){
        Ball b1 = balls[0];
        Ball b2 = balls[1];

        double distanceXsquare = (b1.x - b2.x)*(b1.x - b2.x);
        double distanceYsquare = (b1.y - b2.y)*(b1.y - b2.y);
        double totalDiff = distanceYsquare + distanceXsquare;

        time = WAIT;
        return totalDiff < (b1.radius + b2.radius)*(b1.radius + b2.radius);
    }


    //vinkeln mellan hypotenusan och vanliga x-axeln
    private double getAngle(){
        //vinkeln planet lutar

        double x = Math.abs(balls[0].x - balls[1].x);
        double y = Math.abs(balls[0].y - balls[1].y);
        return (Math.atan(y/x));
    }


    private void rotate(double angle){
        //rotera x och y till ny dimension?

        //gamla vx och vy (boll 1)
        double vx1 = balls[0].vx;
        double vy1 = balls[0].vy;

        //gamla vx och vy (boll 2)
        double vx2 = balls[1].vx;
        double vy2 = balls[1].vy;

        balls[0].vx = Math.cos(angle)*vx1 - Math.sin(angle)*vy1;
        balls[0].vy = Math.sin(angle)*vx1 + Math.cos(angle)*vy1;

        balls[1].vx = Math.cos(angle)*vx2 - Math.sin(angle)*vy2;
        balls[1].vy = Math.sin(angle)*vx2 + Math.cos(angle)*vy2;
    }



    private void vectorCalc(){
        //double u1 = balls[0].vx;
        //double u2 = balls[1].vx;
        double m1 = balls[0].radius;
        double m2 = balls[1].radius;

        //normal vector
        Vector2D normal = new Vector2D(balls[0].x - balls[1].x, balls[0].y - balls[1].y);


        //unit vector of n
        Vector2D un = normal.normalize();

        //tangent vector
        Vector2D ut = new Vector2D(-un.dY, un.dX);

        //velocity vector ball1 and 2
        Vector2D u1 = new Vector2D(balls[0].vx, balls[0].vy);
        Vector2D u2 = new Vector2D(balls[1].vx, balls[1].vy);

        //project velocity vectors onto the unit normal and unit tangent vectors
        double u1n = un.dotProduct(u1);
        double u1t = ut.dotProduct(u1);

        double u2n = un.dotProduct(u2);
        double u2t = ut.dotProduct(u2);

        //calculations for collision

        double v1n = ((u1n*(m1 - m2) + (2*m2*u2n)))/(m1+m2);
        double v2n = ((u2n*(m2 - m1) + (2*m1*u1n)))/(m1+m2);

        //convert scalar normal and tangential velocities into vectors
        Vector2D v1nVec = new Vector2D(un.dX*v1n, un.dY*v1n);
        Vector2D v1tVec = new Vector2D(ut.dX*u1t, ut.dY*u1t);

        Vector2D v2nVec = new Vector2D(un.dX*v2n, un.dY*v2n);
        Vector2D v2tVec = new Vector2D(ut.dX*u2t, ut.dY*u2t);

        Vector2D v1 = v1nVec.add(v1tVec);
        Vector2D v2 = v2nVec.add(v2tVec);

        balls[0].vx = v1.dX;
        balls[0].vy = v1.dY;

        balls[1].vx = v2.dX;
        balls[1].vy = v2.dY;
    }



    private void calculate(){
        double u1 = balls[0].vx;
        double u2 = balls[1].vx;
        double m1 = balls[0].radius;
        double m2 = balls[1].radius;

        double R = -(u2 - u1);
        double I = m1*u1 + m2*u2;

        //double v1 = (I - m2*R) / (m1 + m2);
        //double v2 = (I + m1*R) / (m1 + m2);

        double v1 = (u1*(m1 - m2) + 2*m2*u2)/(m1 + m2);
        double v2 = (u2*(m2 - m1) + 2*m1*u1)/(m1 + m2);

        //haha sätt nya vx och vy

        balls[0].vx = v1;
        balls[1].vx = v2;
    }

    /**
     * Simple inner class describing balls.
     */
    class Ball {

        Ball(double x, double y, double vx, double vy, double r) {



            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = r;
        }

        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        double x, y, vx, vy, radius;
    }
}