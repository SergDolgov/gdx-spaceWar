package com.company.gdx;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static com.company.gdx.ScreenManager.WORLD_HEIGHT;
import static com.company.gdx.ScreenManager.WORLD_WIDTH;

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

    public int getTimeMoved() {
        return timeMoved;
    }

    public void setTimeMoved(int timeMoved) {
        this.timeMoved = timeMoved;
    }

    @Override
    public void moveTo(Vector2 mePos) {
        if (!canMove()) {
            Vector2 position = getPosition();
            if (position.x <= 0) position.x = 1;
            if (position.y <= getCellSize()) position.y = getCellSize() + 1;
            if (position.x >= WORLD_WIDTH) position.x = WORLD_WIDTH - 1;
            if (position.y >= WORLD_HEIGHT) position.y = WORLD_HEIGHT - 1;
            timeMoved = 0;
        }
        if (timeMoved > 0) timeMoved --;
        else{
            timeMoved = 50;
            direction = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        }
        super.moveTo(new Vector2(direction.getVx(), direction.getVy()));
    }
}
