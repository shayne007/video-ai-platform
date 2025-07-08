package com.keensense.admin.feature;

public class PolygonPoint
{

    private int x;
    private int y;

    public PolygonPoint() {
    }
    public PolygonPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public PolygonPoint(String x, String y) {
        
        int intX = 0;
        int intY = 0;
        if (null != x)
        {
            if (x.contains("."))
            {
                Double  doubleX = Double.valueOf(x);

                intX =  (int)(Math.round(doubleX));
            }
            else
            {
                intX = Integer.valueOf(x);
            }
        }
        
        if (null != y)
        {
            if (y.contains("."))
            {
                Double  doubleY = Double.valueOf(y);
                intY = (int)(Math.round(doubleY));
            }
            else
            {
                intY = Integer.valueOf(y);
            }
        }
        
        this.x = intX;
        this.y = intY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


}
