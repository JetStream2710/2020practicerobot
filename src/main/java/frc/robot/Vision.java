package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Vision {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable limelight = inst.getTable("limelight");

    public double horizOffset;    //  Horizontal angle error
    public double vertOffset;     // Vertical angle error
    public double skew;           // Skew angle of target, -45 to +45 degrees
    public double horizontal;
    public double vertical;

    public double areaFromCamera,ledMode;

    public double[] cornX, cornY;

    private NetworkTableEntry ledM, pipeline, camMode, stream, snapshot;
    private NetworkTableEntry nteX, nteY, nteA, nteS, nteHor, nteVer;
    private NetworkTableEntry nteV, nteL, nteCornX, nteCornY;

    private double[] defaultArray = new double[0];          // Default value for returning arrays for networktables

    /**
     * Creates a VisionData object and connects to Limelight Camera
     */
    public Vision() {

//        inst.startClientTeam(6554);

        ledM = limelight.getEntry("ledMode");
        camMode = limelight.getEntry("camMode");
        pipeline = limelight.getEntry("pipeline");
        stream = limelight.getEntry("stream");
        snapshot = limelight.getEntry("snapshot");
    
        nteX = limelight.getEntry("tx");
        nteY = limelight.getEntry("ty");
        nteA = limelight.getEntry("ta"); // area
        nteS = limelight.getEntry("ts"); //skew
        nteHor = limelight.getEntry("thor"); // horizontal value
        nteVer = limelight.getEntry("tvert"); // vertical value

        nteV = limelight.getEntry("tv"); // valid targets
        nteL = limelight.getEntry("tl"); // latency
        nteCornX = limelight.getEntry("tcornx");
        nteCornY = limelight.getEntry("tcorny");

        SmartDashboard.putNumber("Vision pipeline", 1.0);
    // Aim error and angle error based on calibrated limelight cross-hair
    // aimXError = limelight.getEntry("cx0");  // aim error from CrossHair
    }

    public void readCameraData() {
        horizOffset = nteX.getDouble(0);
        vertOffset = nteY.getDouble(0);
        areaFromCamera = nteA.getDouble(0); 
        ledMode = ledM.getDouble(0);
        skew = nteS.getDouble(0);
        skew = (skew<-45) ? skew+90 : skew;  // convert skew from (-90, 0) to (-45, 45)
        horizontal = nteHor.getDouble(0);
        vertical = nteVer.getDouble(0);

        // When we read arrays from the network tables in two sequential statements, we could occasionally get the
        // X array and Y array from different passes of the vision pipeline, so they may not have the same
        // array length.  However, the network tables change slowly (every 5 or 10 ms?), so if we are unlucky 
        // enough to catch a change between reading the X array and the Y array, we can re-try reading both and
        // then they should be synchronized.
        // Try reading up to 3 times to get synchronized arrays.  This should be very robust.
        // We should have at least 5 verticies is 2 targets are recognized.
        int i = 0;
        do {
            cornX = nteCornX.getDoubleArray(defaultArray);
            cornY = nteCornY.getDoubleArray(defaultArray);
            i++;
        } while (i<3 && (cornX.length != cornY.length || cornX.length<5));

        System.out.println("horizontal offset: " + horizOffset + " vertical offset: " + vertOffset);

        SmartDashboard.putNumber("Vision cx.len", cornX.length);
        SmartDashboard.putNumber("Vision cy.len", cornY.length);

        SmartDashboard.putNumber("Vision X", horizOffset);
        SmartDashboard.putNumber("Vision Y", vertOffset);
        SmartDashboard.putNumber("Vision Area", areaFromCamera);
        SmartDashboard.putNumber("Vision Skew", skew);
        SmartDashboard.putNumber("Vision Horizontal", horizontal);
        SmartDashboard.putNumber("Vision Vertical", vertical);
         

    }

    // Turn the LEDS on
    public void turnOnCamLeds() {
        ledM.setDouble(3);           
    }
    // Turn the LEDS off
    public void turnOffCamLeds() {
        ledM.setDouble(1);  
    }

    /**
     * Returns the true pipeline being used
     * @return
     */
    public double getPipeline() {
        return limelight.getEntry("getpipe").getDouble(0);
    }

    /**
     * Sets the pipeline number to use
     * @param pipeNum Pipeline to change to (see limelight web dashboard for details).   (0 = vision, 2 = driver feed)
     */
    public void setPipe(double pipeNum) {
        pipeline.setDouble(pipeNum);
    }

    /**
     * Sets the streaming mode of the cameras
     * @param mode 0 = side-by-side
     * 1 = driver camera lower right corner of vision
     * 2 = vision camera lower right corner of driver camera
     */
    public void setStreamMode(double mode) {
        stream.setDouble(mode);
    }

    /**
     * Enables or disables snapshotting during the match
     * @param mode 0 = no snapshots
     * 1 = two snapshots per second
     */
    public void setSnapshot(double mode) {
        snapshot.setDouble(mode);
    }

      /**
     * 
     * @param modeNumber select a number from 0 to 3.
     * 0 = the LED Mode set in the current pipeline (find this at the ip address of the limelight).
     * 1 = off.
     * 2 = blink.
     * 3 = on.
     * If a number other than 0 to 3 is selected, turn the LEDs off.
     */
    public void setLedMode(int modeNumber) {
        if (modeNumber > 3 || modeNumber < 0) modeNumber = 1;
        ledM.setDouble(modeNumber);
    }

    /**
     * Sets the mode of the camera for use as driver cam or vision processing
     * @param mode 0 = vision; 1 = driver camera
     */
    public void setCamMode(int mode) {
        camMode.setDouble(mode);
    }
    
}