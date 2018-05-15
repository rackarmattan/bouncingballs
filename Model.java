
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
class Model {

    double areaWidth, areaHeight;
    private final double gravity;


    Ball [] balls;

    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;
        gravity = 9.82;

        // Initialize the model with a few balls
        balls = new Ball[2];

        //Sätta vx = 0 här? --> "For the horizontal position x there are no forces so x''=0."
        balls[0] = new Ball(width / 3, height * 0.9, 0, 1, 0.2);
        //den stora
        balls[1] = new Ball(2 * width / 3, height * 0.7, 0, 1, 0.2);

        balls[1].vx = 1;
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        for (Ball b : balls) {

            if(detectCollision()){
                //vinkeln till nya planet
                double beta = getAngle();

                //rotera till nya planet
                rotate(beta);

                //räkna ut ny velocity
                calculate();

                //rotera tillbaka
                rotate(-beta);
            }
            // detect detectCollision with the border
            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1; // change direction of ball
            }
            if (b.y < b.radius || b.y > areaHeight - b.radius) {
                b.vy *= -1;
            }






            // compute new position according to the speed of the ball
            b.vy += -deltaT * gravity;
            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;

        }
    }

    private boolean detectCollision(){
        Ball b1 = balls[0];
        Ball b2 = balls[1];

        //equations
        double u1 = Math.sqrt(b1.vx*b1.vx + b1.vy*b1.vy);
        double u2 = Math.sqrt(b2.vx*b2.vx + b2.vy*b2.vy);

        double I = b1.radius*u1 + b2.radius*u2;
        double R = u1 - u2;

        double distanceXsquare = (b1.x - b2.x)*(b1.x - b2.x);
        double distanceYsquare = (b1.y - b2.y)*(b1.y - b2.y);
        double totalDiff = distanceYsquare + distanceXsquare;
        return totalDiff < (b1.radius + b2.radius)*(b1.radius + b2.radius);
    }


    //vinkeln mellan hypotenusan och vanliga x-axeln
    private double getAngle(){
        double x = Math.abs(balls[0].x - balls[1].x);
        double y = Math.abs(balls[0].y - balls[1].y);
        double h = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return (Math.acos(x/h));
    }

    private void rotate(double beta){
        //rotera x och y till ny dimension?

        //gamla vx och vy (boll 1)
        double vx11 = balls[0].vx;
        double vy11 = balls[0].vy;

        //gamla vx och vy (boll 2)
        double vx21 = balls[1].vx;
        double vy21 = balls[1].vy;

        //nya vx och vy (boll 1)
        balls[0].vx = Math.cos(beta)*vx11 - Math.sin(beta)*vy11;
        balls[0].vy = Math.sin(beta)*vx11 + Math.cos(beta)*vy11;

        //nya vx och vy (boll 2)
        balls[1].vx = Math.cos(beta)*vx21 - Math.sin(beta)*vy21;
        balls[1].vy = Math.sin(beta)*vx21 + Math.cos(beta)*vy21;
    }


    private void calculate(){
        double u1 = balls[0].vx;
        double u2 = balls[1].vx;
        double m1 = balls[0].radius;
        double m2 = balls[1].radius;

        //kalkulera nya v1 och v2?????????????????++
        double v1 = ((m1 - m2)*u1 + (2*m2*u2))/(m1 + m2);
        double v2 = ((m2 - m1)*u2 + (2*m1*u1))/(m1 + m2);

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