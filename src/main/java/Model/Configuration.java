package Model;

public class Configuration {

    public static final int FLAT = 0, RANDOM_GENERATED = 1, IMPORTED = 2;
    public static final int LINE = 0, RANDOM = 1, MEMORY = 2, CONVOLUTION = 3;


    public boolean convectionEnabled = true;
    private float coolingPower = 10f;
    private int coolingType = FLAT;
    private int coolingPath;
    private float igniterMaxSize = 30f;
    private float igniterSpeed = 40f;
    private int igniterCount = 120;
    private int igniterType = MEMORY;
    private float ignitionDensity = 10f;
    public boolean paused = false;
    private int speed = 5;
    private long updateDelay = 20;
    private boolean[][] kernel = new boolean[][]{
            {false,false,false,false,false},
            {false,false,false,false,false},
            {false,false,true,false,false},
            {false,true,true,true,false},
            {true,true,true,true,true},};

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getIgnitionDensity() {
        return ignitionDensity;
    }

    public void setIgnitionDensity(float ignitionDensity) {
        this.ignitionDensity = ignitionDensity;
    }

    public float getCoolingPower() {
        return coolingPower;
    }

    public void setCoolingPower(float coolingPower) {
        this.coolingPower = coolingPower;
    }

    public float getIgniterMaxSize() {
        return igniterMaxSize;
    }

    public void setIgniterMaxSize(float igniterMaxSize) {
        this.igniterMaxSize = igniterMaxSize;
    }

    public float getIgniterSpeed() {
        return igniterSpeed;
    }

    public void setIgniterSpeed(float igniterSpeed) {
        this.igniterSpeed = igniterSpeed;
    }

    public int getIgniterCount() {
        return igniterCount;
    }

    public int getIgniterType() {
        return igniterType;
    }

    public int getCoolingType() {
        return coolingType;
    }

    public void setCoolingType(int coolingType) {
        this.coolingType = coolingType;
    }

    public int getCoolingPath() {
        return coolingPath;
    }

    public void setCoolingPath(int coolingPath) {
        this.coolingPath = coolingPath;
    }

    public boolean isUsingCoolingMap() {
        return (coolingType != 0);
    }

    public boolean[][] getKernel() {
        return kernel;
    }

    public void setKernel(boolean[][] kernel) {
        this.kernel = kernel;
    }

    public long getUpdateDelay() {
        return updateDelay;
    }

    public void setUpdateDelay(long updateDelay) {
        this.updateDelay = updateDelay;
    }

    public void setIgniterCount(int igniterCount) {
        this.igniterCount = igniterCount;
    }

    public void setIgniterType(int igniterType) {
        this.igniterType = igniterType;
        if (igniterType == CONVOLUTION) speed = 1;
        else speed = 5;
    }

    //-----------------------------------------------------------------

    public void nextCoolingPath() {
        coolingPath++;
    }

    public void setOxygen(int oxygen) {
        igniterMaxSize = 5 + (oxygen / 5f * 2.5f);
        igniterSpeed = oxygen / 5f + 10;
        ignitionDensity = oxygen / 5f;
        coolingPower = 12.5f - (oxygen/20f);
    }


}
