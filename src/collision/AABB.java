package collision;

import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.util.vector.Vector2f.add;
import static org.lwjgl.util.vector.Vector2f.sub;

public class AABB {

    Vector2f center, halfExtent;

    public AABB(Vector2f center, Vector2f halfExtent) {
        this.center = center;
        this.halfExtent = halfExtent;
    }

    public Vector2f getCenter() {
        return center;
    }

    public void setCenter(Vector2f center) {
        this.center = center;
    }

    public Vector2f getHalfExtent() {
        return halfExtent;
    }

    public void setHalfExtent(Vector2f halfExtent) {
        this.halfExtent = halfExtent;
    }

    public boolean isIntersecting(AABB box2) {
        Vector2f distance = sub(center, box2.center, new Vector2f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);

        distance.sub(add(halfExtent, box2.halfExtent, new Vector2f()));

        return (distance.x < 0 && distance.y < 0);
    }

}
