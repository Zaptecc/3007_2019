package frc.robot.sequence;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Jaguar;
import frc.robot.Controls;
import frc.robot.Robot;

public class SequenceClimbDown extends Sequence 
{
    //STATES
    /* 0 = start
     * 1 = wheels are elevated enough (xAng = roughly)
     * 2 = move til robot is flat again
     * 3 = claws get pulled almost all the way under 
     * 4 = slide system extends all the way down
     * 5 = move forward slowly & gradually lower slide system
     * 6 = stop sequence once level again
    */
    private int climbDownProgress = 1;

    /**
     * NOTES
     * 
     * 0 = lift slide system to 0.05 Xang
     * 1 = 
     * 
     * -0.15 for MAX angle as slide system descents
     */

    public int stateTime = 0;
    private CANSparkMax fangMotor;
    private Jaguar pulleyMotor;
    private ADIS16448_IMU tiltSensor;

    public SequenceClimbDown(CANSparkMax fang, Jaguar pulley, ADIS16448_IMU imu)
    {
        super(-1,"ClimbDown");
        fangMotor = fang;
        pulleyMotor = pulley;
        tiltSensor = imu;
    }

    @Override
    public void sequenceUpdate() 
    {
        double xAng = tiltSensor.getAccelX() > 1.0 ? tiltSensor.getAccelX() / 8.0d : tiltSensor.getAccelX();
        
        if(Controls.getClimbModeDisabled())
        {
            length = -1;
        }
        
        if(climbDownProgress == 1)
        {
            if(xAng < 0.1)
            {
                System.out.println("TESTING");
                pulleyMotor.set(pulleyMotor.get() + 0.05d);
            }
            else if(xAng < 1.0d)
            {
                pulleyMotor.set(0.0d);

                if(xAng >= 0.1d && xAng < 1.0d)
                {
                    climbDownProgress = 2;
                }
            }
        }

        if(climbDownProgress == 2)
        {
            Robot.getDrive().driveCartesian(0, 0.1d, 0.0);

            if(Math.abs(xAng) <= 0.05)
            {
                climbDownProgress = 3;
            }
        }
        /*switch(climbDownProgress)
        {
            case 0:
            {
                climbDownProgress = 1;
                break;
            }
            case 1:
            {
                if(xAng < 0.05)
                {
                    pulleyMotor.set(pulleyMotor.get() + 0.05d);
                }
                else
                {
                    pulleyMotor.set(0.0d);

                    if(xAng >= 0.05d)
                    {
                        climbDownProgress = 2;
                    }
                }
                break;
            }
            case 2:
            {
                climbDownProgress = 3;

                break;
            }
            case 3:
            {
                climbDownProgress = 4;
                break;
            }
            case 4:
            {
                climbDownProgress = 5;
                break;
            }
            case 5:
            {
                climbDownProgress = 6;
                break;
            }
            case 6:
            {
                if(length < 0)
                {
                    length = 10;
                }

                SequenceMaster.addSequence(Robot.grabSequence);
                SequenceMaster.addSequence(Robot.climbSequence);
                SequenceMaster.addSequence(Robot.adjustSequence);
                break;
            }
        }*/
    }
}