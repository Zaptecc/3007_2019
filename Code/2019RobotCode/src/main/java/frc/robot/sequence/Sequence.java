package frc.robot.sequence;

public abstract class Sequence
{
    /**
     * The length of this sequence left in 60th of seconds.
     * Set to -1 if this sequence is neverending.
     */
    protected int length;

    /**
     * @param length length in seconds.
     */
    public Sequence(int length)
    {
        this.length = length * 60;
    }

    public boolean baseSequenceUpdate()
    {
        --length;
        return length == 0;
    }

    public abstract void sequenceUpdate();
}