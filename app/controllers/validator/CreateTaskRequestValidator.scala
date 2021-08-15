package controllers.validator

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import cats.implicits.catsSyntaxTuple2Semigroupal
import controllers.request.{CreateTaskRequest, UpdateTaskRequest}

object CreateTaskRequestValidator {

  type ValidationResult[A] = ValidatedNel[DomainValidation, A]

  private val regex = "^[a-zA-Z0-9]{1,50}$".r

  def validate(request: CreateTaskRequest): Either[NonEmptyList[String], CreateTaskRequest] =
    (validateTaskName(request.taskName),
      validateDescription(request.description)).mapN(CreateTaskRequest).leftMap(_.map(_.errorMessage)).toEither

  private def validateTaskName(taskName: String): ValidationResult[String] = {
    Validated.condNel(regex.matches(taskName), taskName, TaskNameIsOutOfLength)
  }

  private def validateDescription(description: String): ValidationResult[String] = {
    Validated.condNel(description.length <= 500, description, TaskDescriptionOutOfLength)
  }
}

object UpdateTaskRequestValidator {

  type ValidationResult[A] = ValidatedNel[DomainValidation, A]

  private val regex = "^[a-zA-Z0-9]{1,50}$".r

  def validate(request: UpdateTaskRequest): Either[NonEmptyList[String], UpdateTaskRequest] =
    (validateTaskName(request.taskName),
      validateDescription(request.description)).mapN(UpdateTaskRequest).leftMap(_.map(_.errorMessage)).toEither

  private def validateTaskName(taskName: String): ValidationResult[String] = {
    Validated.condNel(regex.matches(taskName), taskName, TaskNameIsOutOfLength)
  }

  private def validateDescription(description: String): ValidationResult[String] = {
    Validated.condNel(description.length <= 500, description, TaskDescriptionOutOfLength)
  }
}


sealed trait DomainValidation {
  def errorMessage: String = this.getClass.getName
}

object TaskNameIsOutOfLength extends DomainValidation {
}

object TaskDescriptionOutOfLength extends DomainValidation {
}