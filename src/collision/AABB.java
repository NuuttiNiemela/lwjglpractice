package collision;

import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.util.vector.Vector3f.add;
import static org.lwjgl.util.vector.Vector3f.sub;

public class AABB {
    private Vector3f center, halfExtent;

    public AABB(Vector3f center, Vector3f halfExtent) {
        this.center = center;
        this.halfExtent = halfExtent;
    }

    public Collision getCollision(AABB box2) {
        Vector3f distance = sub(box2.center, center, new Vector3f());
        distance.x = Math.abs(distance.x);
        distance.y = Math.abs(distance.y);

        distance = sub(distance,add(box2.halfExtent, halfExtent, new Vector3f()), new Vector3f());

        return new Collision(distance, (distance.x < 0 && distance.y < 0));
    }

    public void correctPosition(AABB box2, Collision data) {
        Vector3f correction = sub(box2.center, center, new Vector3f());
        if(data.distance.x > data.distance.y) {
            if(correction.x > 0) {
                center.add(center, new Vector3f(data.distance.x, 0, 0), new Vector3f());
            } else {
                center.add(center, new Vector3f(-data.distance.x, 0, 0), new Vector3f());
            }
        } else if(data.distance.y > data.distance.x) {
            if(correction.y > 0) {
                center.add(center, new Vector3f(0, data.distance.y, 0), new Vector3f());
            } else {
                center.add(center, new Vector3f(0, -data.distance.y, 0), new Vector3f());
            }
        } else {
            if(correction.z > 0) {
                center.add(center, new Vector3f(0, 0, data.distance.z), new Vector3f());
            } else {
                center.add(center, new Vector3f(0, 0, -data.distance.z), new Vector3f());
            }
        }
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

    public Vector3f getHalfExtent() {
        return halfExtent;
    }

    public void setHalfExtent(Vector3f halfExtent) {
        this.halfExtent = halfExtent;
    }
}
