
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
        balls[0] = new Ball(width / 3, height * 0.9, 0, 1.6, 0.2);
        //den stora
        balls[1] = new Ball(2 * width / 3, height * 0.7, 0, 0.6, 0.3);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        for (Ball b : balls) {
            // detect collision with the border
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

    private boolean collision(Ball b1, Ball b2){
        double distanceXsquare = (b1.x - b2.x)*(b1.x - b2.x);
        double distanceYsquare = (b1.y - b2.y)*(b1.y - b2.y);
        double totalDiff = distanceXsquare + distanceXsquare;
        return totalDiff < (b1.radius + b2.radius)*(b1.radius + b2.radius);
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