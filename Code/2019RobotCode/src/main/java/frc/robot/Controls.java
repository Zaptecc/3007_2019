package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controls 
{
    private enum Button
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
  
      /**
       *  Checks to see what the current state of the button is.
       * @return Returns true if the button is pressed.
       */
      public boolean isPressed()
      {
        return isPressed(Robot.getJoystick());
      }
  
      /**
       * Checks the specified joystick to see what the current state of the button is.
       * @param stick The joystick to check.
       * @return Returns true if the button is pressed.
       */
      public boolean isPressed(Joystick stick)
      {
        if(stick == null) {
          System.out.println("Joystick is not found, cannot determine if button is pressed!!");
          return false;
        }
        return stick.getRawButton(this.id);
      }
    }

    public static boolean getPneumaticINACTIVE()
    {
        return Button.BASE_BOTCENTER.isPressed() || Button.BASE_BOTCENTER.isPressed(Robot.getJoystick2());
    }
    public static boolean getPneumaticACTIVE()
    {
        return Button.BASE_TOPLEFT.isPressed() || Button.BASE_TOPLEFT.isPressed(Robot.getJoystick2());
    }
    public static boolean getPneumaticHOLDING()
    {
        return Button.BASE_BOTLEFT.isPressed() || Button.BASE_BOTLEFT.isPressed(Robot.getJoystick2());
    }
    public static boolean getAutoAdjustMode()
    {
        return Button.JOY_TRIGGER.isPressed();
    }
    public static boolean getDriveMode()
    {
        return Button.JOY_BOTLEFT.isPressed();
    }
    public static boolean getClimbModeEnabled()
    {
        return !Robot.IS_PRACTICE_BOT && (Button.BASE_BOTRIGHT.isPressed() || Button.BASE_BOTRIGHT.isPressed(Robot.getJoystick2()));
    }
    public static boolean getClimbModeDisabled()
    {
        return !Robot.IS_PRACTICE_BOT && (Button.BASE_TOPRIGHT.isPressed() || Button.BASE_TOPRIGHT.isPressed(Robot.getJoystick2()));
    }
    public static boolean getFangControl()
    {
        return !Robot.IS_PRACTICE_BOT && (Button.JOY_TOPRIGHT.isPressed() || Button.JOY_TOPRIGHT.isPressed(Robot.getJoystick2()));
    }
    public static boolean isSecondJoystickControllingFangMotor()
    {
        return !Robot.IS_PRACTICE_BOT && Button.JOY_TOPRIGHT.isPressed(Robot.getJoystick2());
    }  
    public static boolean getSlideControl()
    {
        return !Robot.IS_PRACTICE_BOT && (Button.JOY_BOTRIGHT.isPressed() || Button.JOY_BOTRIGHT.isPressed(Robot.getJoystick2()));
    }

    /**
     * 
     * @return true if the second joystick (id 1) is controlling the slide system motor.
     */
    public static boolean isSecondJoystickControllingSlideMotor()
    {
        return !Robot.IS_PRACTICE_BOT && Button.JOY_BOTRIGHT.isPressed(Robot.getJoystick2());
    }  

    /**
     * @return true if either joystick is enabling auto-climb mode.
     */
    public static boolean getAutoFang()
    {
        return !Robot.IS_PRACTICE_BOT && (Button.JOY_TOPLEFT.isPressed() || Button.JOY_TOPLEFT.isPressed(Robot.getJoystick2()));
    }

    /**
     * @return true if the second joystick (id 1) is enabling auto-climb mode.
     */
    public static boolean isSecondJoystickControllingAutoFang()
    {
        return !Robot.IS_PRACTICE_BOT && Button.JOY_TOPLEFT.isPressed(Robot.getJoystick2());
    }  

    public static boolean isSecondJoystickEnabled()
    {
        return Robot.getJoystick2() != null;
    }

    /**
     * ...I know. It's a long name. Don't judge.
     * @return If the fangs and the slide controls are being controlled on seperate joysticks at the same time.
     */
    public static boolean isSlideControlSeperateFromFangControl()
    {
        return (getFangControl() || getAutoFang()) && isSecondJoystickControllingSlideMotor() || getSlideControl() && (isSecondJoystickControllingFangMotor() || isSecondJoystickControllingAutoFang());
    }
}