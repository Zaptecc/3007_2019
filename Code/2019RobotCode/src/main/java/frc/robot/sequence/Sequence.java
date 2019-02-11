package frc.robot.sequence;

public abstract class Sequence
{
    /**
     * The length of this sequence left in 60th of seconds.
     * Set to -1 if this sequence is neverending.
     */
    protected int length;
    public final String id;

    /**
     * @param length length in seconds.
     */
    public Sequence(int length, String id)
    {
        this.length = length * 60;
        this.id = id;
    }

    public boolean baseSequenceUpdate()
    {
        --length;
        sequenceUpdate();
        return length != 0;
    }

    public abstract void sequenceUpdate();
}