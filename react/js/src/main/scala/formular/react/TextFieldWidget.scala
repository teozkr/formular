package formular.react

import formular.TextField
import formular.react.TextFieldWidget.{EditProps, EditState}
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, ReactComponentB, ReactEventI}

class TextFieldWidgetEditBackend($: BackendScope[EditProps, EditState]) {
  def onChange(event: ReactEventI): Unit = {
    val value = event.target.value
    $.props.change(value)
    $.modState(_.copy(tmpValue = value))
  }
}

object TextFieldWidget {

  val edit = ReactComponentB[EditProps]("TextFieldWidget_edit")
    .initialState(EditState(""))
    .backend(new TextFieldWidgetEditBackend(_))
    .render((props, _, state, backend) =>
    <.div(
      <.label(
        props.field.label,
        ": ",
        <.input(
          ^.name := s"field-${props.field.id}",
          ^.`type` := "text",
          ^.value := props.value.getOrElse(state.tmpValue),
          ^.onChange ==> backend.onChange
        )
      ),
      props.validationMsg.getOrElse(""): String
    )
    )
    .build

  case class EditProps(field: TextField, value: Option[String], validationMsg: Option[String], change: String => Unit)

  case class EditState(tmpValue: String)
}
