package frc.robot.sequence;

import frc.robot.Robot;
import frc.robot.smartint.SmartIntegration;

public class SequenceAutoAdjust extends Sequence
{
    /**
     * Create a sequence to auto-adjust the robot's angle based on the limelight vision.
     * @param seconds seconds to spend adjusting
     */
    public SequenceAutoAdjust(int seconds)
    {
        super(seconds);
    }

    @Override
    public void sequenceUpdate() {
        double limeXOff = (double)SmartIntegration.getSmartValue("LimelightX");
        double limeYOff = (double)SmartIntegration.getSmartValue("LimelightY");
        double limeArea = (double)SmartIntegration.getSmartValue("LimelightArea");
        
        if(Math.abs(limeXOff) <= 1.5f)
        {
            
        }
        
        Robot.getDrive().driveCartesian(0d, 0d, zRotation, 0.0d);
    }
}