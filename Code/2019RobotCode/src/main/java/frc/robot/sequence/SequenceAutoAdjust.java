package frc.robot.sequence;

import frc.robot.Robot;
import frc.robot.smartint.SmartIntegration;

public class SequenceAutoAdjust extends Sequence
{
    public double rotationOffset = 0.0d;
    /**
     * Create a sequence to auto-adjust the robot's angle based on the limelight vision.
     * @param seconds seconds to spend adjusting
     */
    public SequenceAutoAdjust()
    {
        super(-1, "AutoAdjust");
    }

    @Override
    public void sequenceUpdate() {
        double limeXOff = (double)SmartIntegration.getSmartValue("LimelightX");
        double limeYOff = (double)SmartIntegration.getSmartValue("LimelightY");
        double limeArea = (double)SmartIntegration.getSmartValue("LimelightArea");
        double areaPerc = limeArea / 30.0d;
        double speedMultiplier = 2.0 / limeArea * 4.0d;
        rotationOffset = (limeXOff / 20.0f) * areaPerc * speedMultiplier;
        SmartIntegration.setSmartValue("Limelight Adjust Num", rotationOffset);
    }
}