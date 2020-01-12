package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Drivetrain extends SubsystemBase {

  public WPI_TalonSRX frontLeftTalon = null;
  public WPI_TalonSRX rearLeftTalon = null;

  public WPI_TalonSRX frontRightTalon = null;
  public WPI_TalonSRX rearRightTalon = null;

  private DifferentialDrive differentialDrive = null;

  public Drivetrain() {

    super();
    frontLeftTalon = new WPI_TalonSRX(Constants.DRIVETRAIN_FRONT_LEFT_TALON);
    rearLeftTalon = new WPI_TalonSRX(Constants.DRIVETRAIN_REAR_LEFT_TALON); 
    frontRightTalon = new WPI_TalonSRX(Constants.DRIVETRAIN_FRONT_RIGHT_TALON);
    rearRightTalon = new WPI_TalonSRX(Constants.DRIVETRAIN_REAR_RIGHT_TALON);

    frontLeftTalon.setNeutralMode(NeutralMode.Brake);
    rearLeftTalon.setNeutralMode(NeutralMode.Brake);
    frontRightTalon.setNeutralMode(NeutralMode.Brake);
    rearRightTalon.setNeutralMode(NeutralMode.Brake);

    frontLeftTalon.setSafetyEnabled(false);
    rearLeftTalon.setSafetyEnabled(false);
    frontRightTalon.setSafetyEnabled(false);
    rearRightTalon.setSafetyEnabled(false);

    SpeedControllerGroup leftGroup = new SpeedControllerGroup(frontLeftTalon, rearLeftTalon);
    SpeedControllerGroup rightGroup = new SpeedControllerGroup(frontRightTalon, rearRightTalon);

    differentialDrive = new DifferentialDrive(leftGroup, rightGroup);
    differentialDrive.setDeadband(0.02);
  }

  public void arcadeDrive(final double moveSpeed, final double rotateSpeed) {
    System.out.println("arcade drive movespeed: " + moveSpeed + " rotatespeed: " + rotateSpeed);
//    differentialDrive.arcadeDrive(moveSpeed, rotateSpeed);
    frontRightTalon.set(.2);
    rearRightTalon.set(.2);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    //getDefaultCommand().execute();
  }
}
