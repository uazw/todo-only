package controllers.validator

import cats.data.ValidatedNec
import cats.implicits.catsSyntaxTuple2Semigroupal
import cats.syntax.validated._
import controllers.request.CreateTaskRequest

object CreateTaskRequestValidator {

  type ValidationResult[A] = ValidatedNec[DomainValidation, A]

  def validate(request: CreateTaskRequest): ValidationResult[CreateTaskRequest] =
    (validateTaskName(request.taskName),
      validateDescription(request.description)).mapN(CreateTaskRequest)

  private def validateTaskName(taskName: String): ValidationResult[String] = {
    val regex = "^[a-zA-Z0-9]{1,50}$".r
    if (regex.matches(taskName)) taskName.validNec else TaskNameIsOutOfLength.invalidNec
  }

  private def validateDescription(description: String): ValidationResult[String] = if (description.length <= 500) description.validNec else TaskDescriptionOutOfLength.invalidNec

}

sealed trait DomainValidation {
  def errorMessage: String = this.getClass.getName
}

object TaskNameIsOutOfLength extends DomainValidation {
}

object TaskDescriptionOutOfLength extends DomainValidation {
}