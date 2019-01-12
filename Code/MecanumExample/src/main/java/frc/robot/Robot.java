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

  private static final HashMap<String,Integer> buttonNames = new HashMap<String,Integer>();

  static{
    buttonNames.put("b_tr",12); //On the six buttons on the base of the joystick, top right.
    buttonNames.put("b_br",11); //On the six buttons on the base of the joystick, bottom right.
    buttonNames.put("b_tc",10); //On the six buttons on the base of the joystick, top center.
    buttonNames.put("b_bc",9); //On the six buttons on the base of the joystick, bottom center.
    buttonNames.put("b_tl",8); //On the six buttons on the base of the joystick, top left. 
    buttonNames.put("b_bl",7); //On the six buttons on the base of the joystick, bottom left.
    buttonNames.put("j_tr",6); //On the four buttons on the top of the joystick, top right.
    buttonNames.put("j_tl",5); //On the four buttons on the top of the joystick, top left.
    buttonNames.put("j_br",4); //On the four buttons on the top of the joystick, bottom right.
    buttonNames.put("j_bl",3); //On the four buttons on the top of the joystick, bottom left.
    buttonNames.put("side_button",2); //The side button on the left of the joystick.
  }

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;

  @Override
  public void robotInit() {
    Jaguar frontLeft = new Jaguar(kFrontLeftChannel);
    Jaguar rearLeft = new Jaguar(kRearLeftChannel);
    Jaguar frontRight = new Jaguar(kFrontRightChannel);
    Jaguar rearRight = new Jaguar(kRearRightChannel);
    
    // Invert the left side motors.
    // You may need to change or remove this to match your robot.
    frontLeft.setInverted(false);
    rearLeft.setInverted(false);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kJoystickChannel);
  }

  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    double damper = 0.35d;
    double xval = -floorClip(m_stick.getX() * damper, 0.1);
    double yval = floorClip(m_stick.getY() * damper, 0.1);
    double zval = -floorClip(m_stick.getZ() * damper, 0.1);

    if(m_stick.getRawButton(buttonNames.get("b_bc")))
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
