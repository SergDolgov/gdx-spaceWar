package com.company.gdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private Vector2 velocity = new Vector2();;
    private Type type = Type.SHIELD;;
    private float time;
    private float timeMax;
    private final static TextureAtlas atlas = new TextureAtlas("powerUp.pack");
    private final static TextureRegion[][] itemRegions = new TextureRegion(atlas.findRegion("powerUps")).split(60, 60);

    public Item() {
        super("Item.png",60);
    }

    public void activate(float x, float y, Type type) {
        super.activate(x, y, 1);
        this.velocity.set(MathUtils.random(-50, 50), MathUtils.random(-50, 50));
        this.type = type;
        timeMax = 5.0f;
        time = 0.0f;
    }

    public void render(Batch batch) {
       // super.render(batch);
        if (isActive()) {
            int frameIndex = (int) (time / 0.2f) % itemRegions[type.index].length;
            batch.draw(itemRegions[type.index][frameIndex], getPosition().x - 15, getPosition().y - 15);
        }
    }

    public void moveTo() {
        time += 0.03;
        getPosition().mulAdd(velocity, 0.03f);
        if (time > timeMax) {
            deactivate();
        }
    }
}
