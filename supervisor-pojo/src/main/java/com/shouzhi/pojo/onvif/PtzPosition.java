package com.shouzhi.pojo.onvif;

import java.io.Serializable;

/**
 * PTZ位置
 * @author WX
 * @date 2020-10-28 10:14:21
 */
public class PtzPosition implements Serializable {

    /**
     * 水平坐标，范围-1.00~1.00之间
     */
    private float x;

    /**
     * 垂直坐标，范围-1.00~1.00之间
     */
    private float y;

    /**
     * 放大与缩小，范围0.00-1.00之间
     */
    private float zoom;

    private static final long serialVersionUID = 1L;

    public PtzPosition(float x, float y, float zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    @Override
    public String toString() {
        return "PtzPosition{" +
                "x=" + x +
                ", y=" + y +
                ", zoom=" + zoom +
                '}';
    }
}
