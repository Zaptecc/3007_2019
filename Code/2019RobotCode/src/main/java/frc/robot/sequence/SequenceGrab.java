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
    
    public enum GrabStates
    {
        INACTIVE,//0 Retracts both the arm and the hand.
        ACTIVE,//1 Extends just the arm.
        INITIALIZE,//2 Extends the arm and grabs sooner than HOLDING.
        HOLDING;//3 Extends both the arm and the hand, or just the hand if the arm is extended.
    }

    public GrabStates handState = GrabStates.INACTIVE;
    
    public SequenceGrab(Solenoid arm, Solenoid hand)
    {
        super(-1,"Grab");
        armPiston = arm;
        handPiston = hand;
    }

    public void setState(GrabStates state)
    {
        handState = state;
    }

    @Override
    public void sequenceUpdate() 
    {
        if(handState.ordinal() > 0)
        {
            int maxLen = (HAND_MAX_OFFSET);
            ++handOffset;
            armPiston.set(true);
            handPiston.set((handState.ordinal() > 1) && handOffset >= maxLen);

            if(handOffset >= maxLen && handState.ordinal() > 1)
            {
                handOffset = maxLen;
            }
        }
        else
        {
            handPiston.set(false);
            --handOffset;

            if(handOffset <= -HAND_MAX_OFFSET)
            {
                armPiston.set(false);
            }
        }
    }
}