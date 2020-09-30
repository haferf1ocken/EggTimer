package android.haferflocken.eggtimer.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.haferflocken.eggtimer.R
import android.haferflocken.eggtimer.databinding.FragmentEggTimerBinding
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging

class EggTimerFragment : Fragment() {

    private val TOPIC = "breakfast"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEggTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_egg_timer, container, false
        )

        val viewModel = ViewModelProvider(this).get(EggTimerViewModel::class.java)

        binding.eggTimerViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        createChannel(
            getString(R.string.egg_notification_channel_id),
            getString(R.string.egg_notification_channel_name)
        )
        createChannel(
            getString(R.string.breakfast_notification_channel_id),
            getString(R.string.breakfast_notification_channel_name)
        )
        subscribeTopic()

        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.breakfast_notification_channel_description)

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)

        }
    }

    private fun subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
            .addOnCompleteListener { task ->
                var message = getString(R.string.message_subscribed)
                if (!task.isSuccessful) {
                    message = getString(R.string.message_subscribe_failed)
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        fun newInstance() = EggTimerFragment()
    }
}

