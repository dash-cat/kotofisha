package su.cus.announce

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import su.cus.announce.databinding.TitleScreenBinding

class TitleScreen : Fragment() {

    private lateinit var binding: TitleScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TitleScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       animatedButton()
    }

    private fun animatedButton() {
        val welcomeButton = binding.welcomeButton
        welcomeButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        welcomeButton.setOnClickListener {
            val colorAnimation = ValueAnimator.ofArgb(
                ContextCompat.getColor(requireContext(), R.color.colorPrimary),
                ContextCompat.getColor(requireContext(), R.color.colorAccent)
            )
            colorAnimation.duration = 2000 // Duration in milliseconds
            colorAnimation.addUpdateListener { animator ->
                welcomeButton.setBackgroundColor(animator.animatedValue as Int)
            }
            colorAnimation.start()
            findNavController().navigate(R.id.action_titleScreen_to_premiereList)
        }

        val loginButton = binding.loginButton
        loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        // Анимация начинается автоматически, когда фрагмент становится видимым
        val colorAnimation = ValueAnimator.ofArgb(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorAccent)
        )
        colorAnimation.duration = 2000 // Duration in milliseconds
        colorAnimation.addUpdateListener { animator ->
            loginButton.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.start()
        findNavController().navigate(R.id.action_titleScreen_to_loginPage)
    }
}
