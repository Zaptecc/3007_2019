package frc.robot.sequence;

import frc.robot.Robot;
import frc.robot.Robot.Button;
import frc.robot.smartint.SmartIntegration;

public class SequenceAutoAdjust extends Sequence
{
    /**
     * Create a sequence to auto-adjust the robot's angle based on the limelight vision.
     * @param seconds seconds to spend adjusting
     */
    public SequenceAutoAdjust(int seconds)
    {
        super(seconds, "AutoAdjust");
    }

    @Override
    public void sequenceUpdate() {
        double limeXOff = (double)SmartIntegration.getSmartValue("LimelightX");
        double limeYOff = (double)SmartIntegration.getSmartValue("LimelightY");
        double limeArea = (double)SmartIntegration.getSmartValue("LimelightArea");
        double rotationOff = 0.0d;
        double areaPerc = limeArea / 30.0d;
        if(Math.abs(limeXOff) <= 20.0f && limeArea >= 3.0d && limeArea <= 35.0d && Button.JOY_TOPLEFT.isPressed())
        {
            rotationOff = (limeXOff / 20.0f) * areaPerc;
        }
        
        SmartIntegration.setSmartValue("Limelight Adjust Num", rotationOff);
        SmartIntegration.setSmartValue("Auto Adjust Enabled", Button.JOY_TOPLEFT.isPressed());
        Robot.getDrive().driveCartesian(0d, 0d, rotationOff, 0.0d);
    }
}