package com.company.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import static com.company.gdx.ScreenManager.*;

public abstract class MovableObject {
    private int cellSize = 64;
    private float halfSize = cellSize / 2;
    private final Vector2 position = new Vector2();
    private final Vector2 angle = new Vector2();
    private Texture texture;

    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    private TextureRegion textureRegion;
    private int speed;
    private boolean active;

    public boolean isActive() {
        return active;
    }

    public Vector2 getAngle() {
        return angle;
    }

    public void deactivate() {
        setActive(false);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public MovableObject(float x, float y, String textureName, int speed) {
        texture = new Texture(textureName);
        textureRegion = new TextureRegion(texture);
        this.speed = speed;
        position.set(x, y);
    }

    public MovableObject(float x, float y, String textureName, int speed, int cellSize) {
        this(x, y, textureName, speed);
        this.cellSize = cellSize;
        this.halfSize = cellSize / 2;
    }
    public void render(Batch batch) {
        if(isActive())
            batch.draw(
                textureRegion,
                position.x,
                position.y,
                halfSize,
                halfSize,
                cellSize,
                cellSize, 1, 1,
                angle.angleDeg() - 90
            );
    }

    public void dispose() {
        texture.dispose();
    }

    public void moveTo(Vector2 direction) {
        if (canMove()) position.add(direction.x * speed, direction.y * speed);
    }

    public void rotateTo(Vector2 rotateTo) {
        angle.set(rotateTo).sub(position.x + halfSize, position.y + halfSize);
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(float x, float y) {
        position.set(x,y);
    }
    public void setPosition(Vector2 position) {
        setPosition(position.x,position.y);
    }
    public void moveTo() {
    }

    public int getCellSize() {
        return cellSize;
    }

    public boolean canMove() {
        if (!(position.x >= 0 && position.x <= WORLD_WIDTH && position.y >= cellSize && position.y <= WORLD_HEIGHT)){
            if (position.x <= 0) position.x = 1;
            if (position.y <= cellSize) position.y = cellSize + 1;
            if (position.x >= WORLD_WIDTH) position.x = WORLD_WIDTH - 1;
            if (position.y >= WORLD_HEIGHT) position.y = WORLD_HEIGHT - 1;
            return false;
        }
        return true;
    }
}
