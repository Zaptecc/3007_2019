/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 1;
  private static final int kRearLeftChannel = 0;
  private static final int kFrontRightChannel = 3;
  private static final int kRearRightChannel = 2;

  private static final int kJoystickChannel = 0;

  public enum Button
  {
    BASE_TOPRIGHT(12),
    BASE_BOTRIGHT(11),
    BASE_TOPCENTER(10),
    BASE_BOTCENTER(9),
    BASE_TOPLEFT(8),
    BASE_BOTLEFT(7),
    JOY_TOPRIGHT(6),
    JOY_TOPLEFT(5),
    JOY_BOTRIGHT(4),
    JOY_BOTLEFT(3),
    JOY_SIDEBUTTON(2),
    JOY_TRIGGER(1);

    public final int id;
    private Button(int id)
    {
      this.id = id;
    }

    public boolean isPressed()
    {
      return M_Joystick == null ? false : M_Joystick.getRawButtonPressed(this.id);
    }

    public boolean isReleased()
    {
      return M_Joystick == null ? false : M_Joystick.getRawButtonReleased(this.id);
    }
  }

  private MecanumDrive m_robotDrive;
  private static Joystick M_Joystick;

  @Override
  public void robotInit() {
    Jaguar frontLeft = new Jaguar(kFrontLeftChannel);
    Jaguar rearLeft = new Jaguar(kRearLeftChannel);
    Jaguar frontRight = new Jaguar(kFrontRightChannel);
    Jaguar rearRight = new Jaguar(kRearRightChannel);
    
    frontLeft.setInverted(false);
    rearLeft.setInverted(false);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    M_Joystick = new Joystick(kJoystickChannel);
  }

  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    double damper = 0.35d;
    double xval = -floorClip(M_Joystick.getX() * damper, 0.1);
    double yval = floorClip(M_Joystick.getY() * damper, 0.1);
    double zval = -floorClip(M_Joystick.getZ() * damper, 0.1);

    if(M_Joystick.getRawButton(buttonNames.get("b_bc")))
    {
      xval = 0.125d;
    }
    m_robotDrive.driveCartesian(xval, yval, zval, 0.0);
  }

  public static double floorClip(double in, double floor)
  {
    return Math.abs(in) < floor ? 0.0d : in;
  }
}
