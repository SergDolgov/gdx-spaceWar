package com.company.gdx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EnemyShip extends Ship{
    private Direction direction;
    private int timeMoved;
    private final float pursuilRadius;
    public EnemyShip(float x, float y, String textureName, int speed) {
        super(x, y, textureName, speed);
        this.timeMoved = 50;
        this.direction = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        setActive(true);
        pursuilRadius = 300.0f;
    }

    public float getPursuilRadius() {
        return pursuilRadius;
    }

    @Override
    public void moveTo(Vector2 mePos) {
        if (timeMoved > 0) timeMoved --;
        else{
            timeMoved = 50;
            direction = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        }
        super.moveTo(new Vector2(direction.getVx(), direction.getVy()));
    }
}
