package frc.robot.sequence;

import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Robot;
import frc.robot.Robot.*;

public class SequenceGrab extends Sequence
{
    public Solenoid armPiston;
    public Solenoid handPiston;
    public int handOffset = 0;
    public static final int HAND_MAX_OFFSET = 20;
    public static boolean HAND_OUT = false;
    public static boolean ARM_OUT = false;
    public boolean handOut = false;
    public boolean armOut = false;
    
    public SequenceGrab(Solenoid arm, Solenoid hand)
    {
        super(-1,"Grab");
        armPiston = arm;
        handPiston = hand;
    }

    @Override
    public void sequenceUpdate() 
    {
        boolean flag = Button.JOY_TOPLEFT.isPressed(Robot.getJoystick2() == null ? Robot.getJoystick() : Robot.getJoystick2());
        if(flag)
        {
            ++handOffset;
            ARM_OUT = armOut = true;
            if(handOffset >= HAND_MAX_OFFSET)
            {
                HAND_OUT = handOut = true;
                handOffset = HAND_MAX_OFFSET;
            }
        }
        else
        {
            HAND_OUT = handOut = false;
            --handOffset;

            if(handOffset <= -HAND_MAX_OFFSET)
            {
                ARM_OUT = armOut = false;
                length = 0;
            }
        }
    }
}