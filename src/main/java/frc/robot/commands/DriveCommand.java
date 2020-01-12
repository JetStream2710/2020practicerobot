package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class DriveCommand extends CommandBase {

  public DriveCommand() {
    addRequirements(RobotContainer.drivetrain);
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    double moveSpeed = -1 * RobotContainer.joystick.getRawAxis(Constants.DRIVER_MOVE_AXIS);
    double rotateSpeed = -1* RobotContainer.joystick.getRawAxis(Constants.DRIVER_ROTATE_AXIS);
    System.out.println("execute movespeed: " + moveSpeed + " rotateSpeed: " + rotateSpeed);
    RobotContainer.drivetrain.arcadeDrive(moveSpeed, rotateSpeed);

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.drivetrain.arcadeDrive(0,0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
