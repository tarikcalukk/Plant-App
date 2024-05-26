package ba.unsa.etf.rma24.projekat.pomocneKlase

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ba.unsa.etf.rma24.projekat.R

class CustomColorAdapter(context: Context, private val colors: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val color = colors[position]
        val backgroundColor = getColorForBackground(color)
        view.setBackgroundColor(backgroundColor)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        val textColor = getTextColorForBackground(color)
        textView.setTextColor(textColor)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    private fun getColorForBackground(color: String): Int {
        return when (color) {
            "red" -> Color.RED
            "blue" -> Color.BLUE
            "yellow" -> Color.YELLOW
            "orange" -> Color.rgb(255, 165, 0) // Orange color
            "purple" -> Color.rgb(128, 0, 128) // Purple color
            "brown" -> Color.rgb(165, 42, 42) // Brown color
            "green" -> Color.GREEN
            else -> Color.WHITE
        }
    }

    private fun getTextColorForBackground(color: String): Int {
        return when (color) {
            "yellow", "orange", "green" -> Color.BLACK
            else -> Color.WHITE
        }
    }
}