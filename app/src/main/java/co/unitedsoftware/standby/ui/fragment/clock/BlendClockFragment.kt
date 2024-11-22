package co.unitedsoftware.standby.ui.fragment.clock

import android.graphics.BlendMode
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.LAYER_TYPE_HARDWARE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import co.unitedsoftware.standby.R
import co.unitedsoftware.standby.logic.getUriToDrawable
import co.unitedsoftware.standby.ui.widget.BlendView

class BlendClockFragment: Fragment() {

    private lateinit var blendView: BlendView
    private lateinit var hourTextView: TextView
    private lateinit var minuteTextView: TextView
    private lateinit var sepTextView: TextView

    private val overlayPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_blend_clock, container, false)

        blendView = rootView.findViewById(R.id.blend_view)
        sepTextView = rootView.findViewById(R.id.sep)
        hourTextView = rootView.findViewById(R.id.hour)
        minuteTextView = rootView.findViewById(R.id.minute)

        sepTextView.setLayerType(LAYER_TYPE_HARDWARE, overlayPaint)
        hourTextView.setLayerType(LAYER_TYPE_HARDWARE, overlayPaint)
        minuteTextView.setLayerType(LAYER_TYPE_HARDWARE, overlayPaint)

        blendView.setImageUri(requireContext().getUriToDrawable(R.drawable.night))

        return rootView
    }

    override fun onStart() {
        super.onStart()
        blendView.startRotationAnimation()
    }

    override fun onStop() {
        super.onStop()
        blendView.stopRotationAnimation()
    }
}