// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.filter.SlewRateLimiter;

import javax.sound.midi.ControllerEventListener;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  Talon motorFrontRight = new Talon(1);
  Talon motorFrontLeft = new Talon(2);
  Talon motorRearRight = new Talon(3);
  Talon motorRearLeft = new Talon(4);

  DifferentialDrive frontDrive = new DifferentialDrive(motorFrontLeft, motorFrontRight);
  DifferentialDrive rearDrive = new DifferentialDrive(motorRearLeft, motorRearRight);

  PS4Controller controller = new PS4Controller(0);

  boolean firstFrame;

  double prevRightJoyX;
  double prevRightJoyY;

  SlewRateLimiter limitedMovementAcc = new SlewRateLimiter(1);
  SlewRateLimiter limitedTurnAcc = new SlewRateLimiter(2);

  double maxMotorAcc = 0.0001; //set max motor acceleration of drivetrain
  double maxTurnAcc = 0.0001; //set max turning acceleration of drivetrain
  double maxMotorSpeed = 0.7;
  double maxTurnSpeed = 0.7;

  boolean isDisabled;

  double previousTime;
  double deltaTime;

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    previousTime = Timer.getFPGATimestamp();

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);


  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() 
  {
    double currentTime = Timer.getFPGATimestamp();
    deltaTime = currentTime - previousTime;
    previousTime = currentTime;
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() 
  {
    prevRightJoyX = controller.getRightX();
    prevRightJoyY = controller.getRightY();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() 
  {
    double rightJoyX = maxMotorSpeed * controller.getRightX();
    double rightJoyY = maxTurnSpeed * controller.getRightY();

    isDisabled = controller.getCircleButton();

    if(isDisabled)
    {
      motorFrontLeft.stopMotor();
      motorFrontRight.stopMotor();
      motorRearLeft.stopMotor();
      motorRearRight.stopMotor();
    }
    else
    {
      frontDrive.arcadeDrive(limitedTurnAcc.calculate(-rightJoyX), limitedMovementAcc.calculate(-rightJoyY));
      frontDrive.arcadeDrive(limitedTurnAcc.calculate(-rightJoyX), limitedMovementAcc.calculate(-rightJoyY));
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
