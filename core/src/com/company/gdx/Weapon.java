package com.company.gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon {
    public enum Type {
        SMALL(0, 60, 1, 500, 2),
        MEDIUM(1, 20, 2, 700, 4);
        final int index;
        final int firePeriod;
        final int damage;
        final int radius;
        final int projectileSpeed;
        Type(int index, int firePeriod, int damage, int radius, int projectileSpeed) {
            this.index = index;
            this.firePeriod = firePeriod;
            this.damage = damage;
            this.radius = radius;
            this.projectileSpeed = projectileSpeed;
        }
    }
    private Type type = Type.SMALL;
    private final float projectileLifeTime;

    public float getProjectileSpeed() {
        return type.projectileSpeed;
    }

    public float getProjectileLifeTime() {
        return projectileLifeTime;
    }

    public int getFirePeriod() {
        return type.firePeriod;
    }

    public int getDamage() {
        return type.damage;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Weapon() {
        this.projectileLifeTime = this.type.radius / this.type.projectileSpeed;
    }
}
