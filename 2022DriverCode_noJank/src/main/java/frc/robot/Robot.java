// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.Math;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.XboxController;
import com.kauailabs.navx.frc.AHRS;
import  com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private double x;
  private double y;
  private double z;
  private double[] motorSpeed = new double[4];
  private double highestSpeed = 0;
  XboxController controller = new XboxController(1);//change port value later
  CANSparkMax frontLeftMotor = new CANSparkMax(1, MotorType.kBrushless);
  CANSparkMax frontRightMotor = new CANSparkMax(2,MotorType.kBrushless);
  CANSparkMax backLeftMotor = new CANSparkMax(3, MotorType.kBrushless);
  CANSparkMax backRightMotor = new CANSparkMax(4, MotorType.kBrushless);
  AHRS gyro = new AHRS(SPI.Port.kMXP);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    frontRightMotor.setInverted(true);
    backLeftMotor.setInverted(true);
  
    
    


  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

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
  public void teleopInit() {
    gyro.calibrate();
    gyro.reset();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    x = 0.25*controller.getLeftX();
    y = 0.25*controller.getLeftY();
    z = 0.25*controller.getRightY();
    motorSpeed[0] = x - y - z;
    motorSpeed[1] = x + y + z;
    motorSpeed[2] = x + y - z;
    motorSpeed[3] = x - y + z;
    for(double speed : motorSpeed){
      if(Math.abs(speed)> highestSpeed ){
        highestSpeed = speed;
      }
    }
    if(highestSpeed > 1.0){
        for(int index = 0; index<motorSpeed.length; index++){
          motorSpeed[index] = motorSpeed[index]/highestSpeed;
        }
    }
    frontLeftMotor.set(motorSpeed[0]);
    frontRightMotor.set(motorSpeed[1]);
    backLeftMotor.set(motorSpeed[2]);
    frontLeftMotor.set(motorSpeed[3]);

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
}
