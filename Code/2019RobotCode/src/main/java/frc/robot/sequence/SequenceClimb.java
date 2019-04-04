package frc.robot.sequence;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.*;
import frc.robot.Controls;
import frc.robot.Robot;

//Handles all the climbing code, and tells the slide-system motors and the fang motors where to move.
public class SequenceClimb extends Sequence
{
    private CANSparkMax fangMotor;
    private Jaguar pulleyMotor;
    private ADIS16448_IMU tiltSensor;
    public boolean changingSlide = false;

    public enum ClimbStates {
        UP,
        DOWN,
        DISABLED;
    }

    public SequenceClimb(CANSparkMax fang, Jaguar pulley, ADIS16448_IMU imu)
    {
        super(-1,"ClimbStep");
        fangMotor = fang;
        pulleyMotor = pulley;
        tiltSensor = imu;
    }

    public void sequenceUpdate() 
    {
        if(Robot.CLIMB_STATE == ClimbStates.UP)
        {
            //If neither fang controls are being pressed, stop the slide system.
            if (!Controls.getFangControl() && !Controls.getAutoFang())
            {
                pulleyMotor.set(0.0d);
                changingSlide = false;
            }
            else if(Controls.getFangControl() || Controls.getAutoFang())
            {
                double motorSpeed = (1.0d / 30.0d);
                double xAng = tiltSensor.getAccelX();
                if(fangMotor.get() > 0.05d && xAng > -0.1 && xAng < 0 && !Controls.getSlideControl())
                {
                    Robot.getDrive().driveCartesian(0.0d, -0.2d, 0.0d);
                }

                if(!Controls.getFangControl() && (!Controls.getSlideControl() && Controls.getAutoFang()))
                {
           //            fangMotor.set(motorSpeed);
                    double threshold = 0.05d;//0.1
                    double offset = 0.05d;//0.255
                    if(xAng > offset + threshold)
                    {
                        pulleyMotor.set(pulleyMotor.get() - motorSpeed);
                        changingSlide = true;
                    }
                    else if(Math.abs(xAng) < offset * 0.5d + threshold * 0.05d && Math.abs(xAng) > offset * 0.5 - threshold * 0.05d)
                    {
                        pulleyMotor.set(0.0d); 
                        changingSlide = false;
                    }
                    else if(xAng < offset - threshold)
                    {
                        pulleyMotor.set(pulleyMotor.get() + motorSpeed);
                        changingSlide = true;
                    }    
                    else
                    {
                        pulleyMotor.set(0.0d);
                        changingSlide = false;
                    }
                }
            }
        }
        else
        {
            pulleyMotor.set(0.0d);
            //negative for the pulley is up,
            //positive for the pulley is down.
            //pulleyMotor.setPosition(0.0d);
        }
    }
}