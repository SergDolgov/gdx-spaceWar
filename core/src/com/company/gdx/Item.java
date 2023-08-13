package com.company.gdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


public class Item extends MovableObject{

    public enum Type {
        SHIELD(0), MEDKIT(1);

        final int index;

        Type(int index) {
            this.index = index;
        }
    }
    private final Vector2 velocity;
    private Type type;
    private float time;
    private final float timeMax;

    public Item() {
        super(0,0,"",1);
        this.velocity = new Vector2(0, 0);
        this.type = Type.SHIELD;
        this.timeMax = 5.0f;
        this.time = 0.0f;
        setActive(false);
    }

    public void setup(float x, float y, Type type) {
        setPosition(x, y);
        this.velocity.set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
        this.type = type;
        this.time = 0.0f;
        setActive(true);
    }

    public void render(Batch batch, TextureRegion[][] regions) {
       // super.render(batch);
        if (isActive()) {
            int frameIndex = (int) (time / 0.2f) % regions[type.index].length;
            batch.draw(regions[type.index][frameIndex], getPosition().x - 15, getPosition().y - 15);
        }
    }

    public void update(float dt) {
        time += dt;
        getPosition().mulAdd(velocity, dt);
        if (time > timeMax) {
            deactivate();
        }
    }
}
