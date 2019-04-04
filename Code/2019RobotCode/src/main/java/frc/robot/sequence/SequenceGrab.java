package frc.robot.sequence;

import edu.wpi.first.wpilibj.Solenoid;

//A sequence to handle the pneumatic arm's states (set in Robot.java)
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
        INACTIVE,//ordinal is 0. Retracts both the arm and the hand.
        ACTIVE,//ordinal is 1. Extends just the arm.
        INITIALIZE,//ordinal is 2. Extends the arm and grabs sooner than HOLDING.
        HOLDING;//ordinal is 3. Extends both the arm and the hand, or just the hand if the arm is extended.
    }

    public GrabStates handState = GrabStates.INITIALIZE;
    public GrabStates prevState = GrabStates.INITIALIZE;
    
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
        if(handState.ordinal() > 0) //Ordinal is the order that the enum is in. So
        {
            int maxLen = (HAND_MAX_OFFSET) - (prevState == GrabStates.ACTIVE && handState == GrabStates.HOLDING ? HAND_MAX_OFFSET : 0);
            if(prevState == GrabStates.ACTIVE && handState == GrabStates.HOLDING)
            {
                handOffset = maxLen - 1;
            }
            ++handOffset;
            armPiston.set(handState == GrabStates.INITIALIZE ? handOffset > 10 : handOffset >= 0);
            handPiston.set(handState == GrabStates.INITIALIZE ? handOffset > 15 : (handState.ordinal() > 1) && handOffset >= (handState == GrabStates.INITIALIZE ? (int)(maxLen * 0.35d) : maxLen));

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