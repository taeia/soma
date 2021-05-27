package com.soma.viewmodel.window.panel;

public class Bounds {

    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;
    private final double width;
    private final double height;

    private Bounds(double minX, double minY, double maxX, double maxY, double width, double height) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.width = width;
        this.height = height;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Bounds withMinX(double minX) {
        return new Bounds(minX, minY, maxX, maxY, maxX - minX, height);
    }

    public Bounds withMinY(double minY) {
        return new Bounds(minX, minY, maxX, maxY, width, maxY - minY);
    }

    public Bounds withMaxX(double maxX) {
        return new Bounds(minX, minY, maxX, maxY, maxX - minX, height);
    }

    public Bounds withMaxY(double maxY) {
        return new Bounds(minX, minY, maxX, maxY, width, maxY - minY);
    }

    public Bounds withWidth(double width) {
        return new Bounds(minX, minY, minX + width, maxY, width, height);
    }

    public Bounds withHeight(double height) {
        return new Bounds(minX, minY, maxX, minY + height, width, height);
    }

    public Bounds withSize(double width, double height) {
        return new Bounds(minX, minY, minX + width, minY + height, width, height);
    }

    public static class Builder {

        private Double minX;
        private Double minY;
        private Double maxX;
        private Double maxY;
        private Double width;
        private Double height;

        public Builder minX(double minX) {
            this.minX = minX;
            return this;
        }

        public Builder minY(double minY) {
            this.minY = minY;
            return this;
        }

        public Builder maxX(double maxX) {
            this.maxX = maxX;
            return this;
        }

        public Builder maxY(double maxY) {
            this.maxY = maxY;
            return this;
        }

        public Builder width(double width) {
            this.width = width;
            return this;
        }

        public Builder height(double height) {
            this.height = height;
            return this;
        }

        public Bounds build() {
            if (minX != null && minY != null && maxX != null && maxY != null && width == null && height == null) {
                return new Bounds(minX, minY, maxX, maxY, maxX - minX, maxY - minY);
            } else if (minX != null && minY != null && width != null && height != null && maxX == null && maxY == null) {
                return new Bounds(minX, minY, minX + width, minY + height, width, height);
            } else {
                throw new IllegalStateException();
            }
        }
    }
}